/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.beans.impl;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tysanclan.site.projectewok.beans.MembershipStatusBean;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.MembershipStatusChange;
import com.tysanclan.site.projectewok.entities.MembershipStatusChange.ChangeType;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.MembershipStatusChangeDAO;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class MembershipStatusBeanImpl implements MembershipStatusBean {
	@Autowired
	private MembershipStatusChangeDAO membershipStatusChangeDAO;

	@Autowired
	private UserService membershipService;

	public void setMembershipStatusChangeDAO(
			MembershipStatusChangeDAO membershipStatusChangeDAO) {
		this.membershipStatusChangeDAO = membershipStatusChangeDAO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addStatus(ChangeType type, User user) {
		MembershipStatusChange change = new MembershipStatusChange();
		change.setChangeTime(new Date());
		change.setChangeType(type);
		change.setMemberSizeMutation(type.getMemberCountMutation());
		change.setUser(user);
		membershipStatusChangeDAO.save(change);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Map<Date, Long> getHistoricMemberCounts(Date start, Date end) {
		SortedMap<Date, Long> mutations = membershipStatusChangeDAO
				.getMutationsByDate(start, end);

		Map<Date, Long> historicCounts = new TreeMap<Date, Long>();

		long current = membershipService.countMembers();
		historicCounts.put(new Date(), current);

		for (Entry<Date, Long> next : mutations.entrySet()) {
			current = current - next.getValue();
			historicCounts.put(next.getKey(), current);
		}

		return historicCounts;
	}
}
