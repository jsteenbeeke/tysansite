/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
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
package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.MembershipStatusChange;
import com.tysanclan.site.projectewok.entities.dao.MembershipStatusChangeDAO;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.GraphUtil;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class ClanStatisticsPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private MembershipStatusChangeDAO dao;

	@SpringBean
	private UserService userService;

	public ClanStatisticsPage() {
		super("Clan Statistics");

		add(GraphUtil.makeMemberCountLineChart("members", "Clan Size",
				determineSizes()));

		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.MONTH, -1);

		int applied = 0;
		int senateaccepted = 0;
		int senaterejected = 0;
		int lost = 0;
		int votedout = 0;
		int inactive = 0;
		int terminateself = 0;
		int banned = 0;

		for (MembershipStatusChange change : dao.findAll()) {
			if (change.getChangeTime().after(cal.getTime())) {
				switch (change.getChangeType()) {
					case APPLIED:
						applied++;
						break;
					case FORCED_OUT:
						lost++;
						banned++;
						break;
					case INACTIVITY_TIMEOUT:
						lost++;
						inactive++;
						break;
					case LEFT_VOLUNTARILY:
						lost++;
						terminateself++;
						break;
					case TRIAL_DENIED:
						senaterejected++;
						break;
					case TRIAL_GRANTED:
						senateaccepted++;
						break;
					case MEMBERSHIP_DENIED:
						votedout++;
						lost++;
						break;
					default:
				}
			}
		}

		add(new Label("applied", new Model<Integer>(applied)));
		add(new Label("senateaccepted", new Model<Integer>(senateaccepted)));
		add(new Label("senaterejected", new Model<Integer>(senaterejected)));
		add(new Label("lost", new Model<Integer>(lost)));
		add(new Label("votedout", new Model<Integer>(votedout)));
		add(new Label("inactive", new Model<Integer>(inactive)));
		add(new Label("terminateself", new Model<Integer>(terminateself)));
		add(new Label("banned", new Model<Integer>(banned)));

	}

	private SortedMap<Date, Integer> determineSizes() {
		SortedMap<Date, Integer> mutations = determineMutations();
		SortedMap<Date, Integer> sizes = new TreeMap<Date, Integer>();

		Date first = mutations.firstKey();
		int cur = mutations.get(first);
		sizes.put(first, cur);
		for (Entry<Date, Integer> entry : mutations.entrySet()) {
			if (!entry.getKey().equals(first)) {
				cur = cur - entry.getValue();
				sizes.put(entry.getKey(), cur);
			}
		}
		return sizes;
	}

	private SortedMap<Date, Integer> determineMutations() {
		SortedMap<Date, Integer> mutations = new TreeMap<Date, Integer>(
				new Comparator<Date>() {
					@Override
					public int compare(Date o1, Date o2) {

						return -o1.compareTo(o2);
					}
				});

		mutations.put(new Date(), userService.getMembers().size());

		for (MembershipStatusChange change : dao.findAll()) {
			if (mutations.containsKey(change.getChangeTime())) {
				mutations.put(change.getChangeTime(),
						mutations.get(change.getChangeTime()) + change
								.getMemberSizeMutation());
			} else {
				mutations.put(change.getChangeTime(),
						change.getMemberSizeMutation());
			}
		}
		return mutations;
	}
}
