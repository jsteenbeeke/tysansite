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
package com.tysanclan.site.projectewok.tasks;

import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.UserFilter;
import com.tysanclan.site.projectewok.util.MemberUtil;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

/**
 * @author Jeroen Steenbeeke
 */
public class AutomaticPromotionTask extends PeriodicTask {
	@SpringBean
	private UserDAO userDAO;

	@SpringBean
	private MembershipService membershipService;

	/**
     * 
     */
	public AutomaticPromotionTask() {
		super("Promotion", "Members", ExecutionMode.DAILY);
	}

	/**
	 * @see com.tysanclan.site.projectewok.util.scheduler.TysanTask#run()
	 */
	@Override
	public void run() {
		UserFilter filter = new UserFilter();
		filter.addRank(Rank.JUNIOR_MEMBER);
		filter.addRank(Rank.FULL_MEMBER);
		filter.addRank(Rank.SENIOR_MEMBER);

		List<User> users = userDAO.findByFilter(filter);
		for (User user : users) {
			if (MemberUtil.determineRankByJoinDate(user
			        .getJoinDate()) != user.getRank()) {
				membershipService
				        .performAutoPromotion(user);
			}
		}

	}

}
