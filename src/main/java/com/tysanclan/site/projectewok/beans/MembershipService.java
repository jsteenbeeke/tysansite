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
package com.tysanclan.site.projectewok.beans;

import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.JoinApplication;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;

/**
 * Various functions relating to clan membership
 * 
 * @author Jeroen Steenbeeke
 */
public interface MembershipService {

	/**
	 * Indicates that the given user has just done something
	 * 
	 * @param user
	 *            The user that just did something
	 */
	void registerAction(User user);

	ForumThread applyForMembership(User user, String motivation, Game game,
			Realm realm);

	/**
	 * Sets the mentor for the given applicant
	 * 
	 * @param application
	 *            The application to set the mentor of
	 * @param mentor
	 *            The mentor to assign
	 */
	void setMentor(JoinApplication application, User mentor);

	/**
	 * Registers the vote of a given senator regarding the given join
	 * application
	 * 
	 * @param application
	 *            The join application to vote for
	 * @param senator
	 *            The senator casting the vote
	 * @param inFavor
	 *            {@code true} if the user wants to applying user to be a
	 *            member. {@code false} otherwise
	 */
	void setJoinApplicationVote(JoinApplication application, User senator,
			boolean inFavor);

	void performAutoPromotion(User user);

	public void feelLucky(User luckyOne);

	void onLogin(User u);

	void expireMembers();

	void determinePromotions();

	void bumpAccounts();

	void terminateMembership(User _user, boolean judicial);
}
