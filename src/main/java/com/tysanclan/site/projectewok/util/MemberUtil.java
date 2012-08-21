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
package com.tysanclan.site.projectewok.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanSecurity;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class MemberUtil {
	private static final String PASSWORD_SALT = "IK_BEN_JE_OMA!1";
	private static final Rank[] MEMBER_RANKS = new Rank[] { Rank.CHANCELLOR,
			Rank.SENATOR, Rank.TRUTHSAYER, Rank.REVERED_MEMBER,
			Rank.SENIOR_MEMBER, Rank.FULL_MEMBER, Rank.JUNIOR_MEMBER,
			Rank.TRIAL };

	private static List<Rank> nonTrialRanks = null;

	private static final TysanSecurity security = new TysanSecurity();

	/**
	 * Checks whether or not the user is a member
	 * 
	 * @param user
	 *            The user to check
	 * @return <code>true</code> if the user is a member, <code>false</code>
	 *         otherwise
	 */
	public static boolean isMember(User user) {
		return user != null
				&& (user.getRank() != Rank.BANNED && user.getRank() != Rank.FORUM);
	}

	public static boolean isHashedPassword(String password) {
		if (password.length() != 40) {
			return false;
		}
		for (byte character : password.getBytes()) {
			if (!Character.isDigit(character)
					&& !(character >= 97 && character <= 102)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if the given user can be a mentor
	 * 
	 * @param user
	 *            The user to check
	 * @return {@code true} if the user can be a mentor, {@code false} otherwise
	 */
	public static boolean canUserBeMentor(User user) {
		boolean visible = false;

		if (user != null) {
			switch (user.getRank()) {
				case CHANCELLOR:
				case FULL_MEMBER:
				case REVERED_MEMBER:
				case SENIOR_MEMBER:
				case SENATOR:
				case TRUTHSAYER:
					visible = true;
					break;
				case JUNIOR_MEMBER:
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MONTH, -2);

					if (user.getJoinDate().before(cal.getTime())) {
						visible = true;
					}
					break;
			}
		}

		return visible;
	}

	public static boolean canUserGrantEndorsement(User user) {
		boolean visible = false;

		if (user != null) {
			switch (user.getRank()) {
				case CHANCELLOR:
				case JUNIOR_MEMBER:
				case FULL_MEMBER:
				case REVERED_MEMBER:
				case SENIOR_MEMBER:
				case SENATOR:
				case TRUTHSAYER:
					visible = true;
					break;
			}
		}

		return visible;
	}

	public static String hashPassword(String password) {
		return StringUtil.sha1Hash(StringUtil.combineStrings(password,
				PASSWORD_SALT));

	}

	public static boolean isEligibleForElectedRank(User user, Rank rank) {
		// Nothingness has no rights
		if (user == null)
			return false;

		// Anybody is eligible for nothing
		if (rank == null)
			return isMember(user);

		switch (rank) {
			case CHANCELLOR:
				return isMember(user) && user.getRank() != Rank.TRIAL
						&& user.getRank() != Rank.JUNIOR_MEMBER
						&& !user.isRetired();
			case SENATOR:
				return isMember(user) && user.getRank() != Rank.TRIAL
						&& user.getRank() != Rank.JUNIOR_MEMBER
						&& user.getRank() != Rank.CHANCELLOR
						&& !user.isRetired();
			case TRUTHSAYER:
				return isMember(user) && user.getRank() != Rank.TRIAL
						&& user.getRank() != Rank.JUNIOR_MEMBER
						&& user.getRank() != Rank.CHANCELLOR
						&& !user.isRetired();
			default:
				// Does not make sense for other ranks
				return false;
		}
	}

	public static Rank determineRankByJoinDate(Date joinDate) {
		long now = DateUtil.getCalendarInstance().getTimeInMillis();
		long then = joinDate.getTime();

		long difference = now - then;

		long seconds = difference / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;

		if (days > (5 * 365)) {
			return Rank.REVERED_MEMBER;
		} else if (days > (2 * 365)) {
			return Rank.SENIOR_MEMBER;
		} else if (days > (4 * 30)) {
			return Rank.FULL_MEMBER;
		}

		return Rank.JUNIOR_MEMBER;
	}

	public static boolean canUserVote(User user) {
		boolean visible = false;

		if (user != null) {
			switch (user.getRank()) {
				case CHANCELLOR:
				case FULL_MEMBER:
				case JUNIOR_MEMBER:
				case REVERED_MEMBER:
				case SENIOR_MEMBER:
				case SENATOR:
				case TRUTHSAYER:
					visible = true;
					break;
			}
		}

		return visible;
	}

	public static boolean hasPermission(User user,
			Class<? extends TysanPage> pageClass) {
		return security.authorize(pageClass);
	}

	public static synchronized List<Rank> getNonTrialRanks() {
		if (nonTrialRanks == null) {
			nonTrialRanks = new ArrayList<Rank>(getMemberRanks().length - 1);

			for (Rank r : getMemberRanks()) {
				if (r != Rank.TRIAL) {
					nonTrialRanks.add(r);
				}
			}
		}

		return nonTrialRanks;
	}

	public static Rank[] getMemberRanks() {
		return MEMBER_RANKS;
	}
}
