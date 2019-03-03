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

import com.tysanclan.site.projectewok.entities.*;

import java.util.List;

/**
 * @author Jeroen Steenbeeke 
 */
public interface AchievementService {
	AchievementProposal createProposal(User proposer, String name,
			String description, AchievementIcon icon, Game game, Group group);

	AchievementIcon uploadAchievementIcon(User author, byte[] image,
			String purpose, Boolean privateIcon);

	void approveIcon(User truthsayer, AchievementIcon icon);

	void rejectIcon(User truthsayer, AchievementIcon icon);

	void approveProposal(AchievementProposal proposal, User senator);

	void rejectProposal(AchievementProposal proposal, User senator);

	void rejectAchievement(AchievementProposal proposal, Regulation regulation,
			boolean charterViolation, User truthsayer);

	AchievementRequest requestAchievement(User user, Achievement achievement,
			byte[] evidencePicture, String evidenceDescription);

	void approveRequest(User truthsayer, AchievementRequest request);

	void denyRequest(User truthsayer, AchievementRequest request);

	List<AchievementProposal> getProposedAchievements(User user);

	void resolvePendingProposals();

	List<AchievementIcon> getAvailableIcons(User user);

	void deleteIcon(User deleter, AchievementIcon icon);

	void vetoProposal(User chancellor, AchievementProposal proposal);

	void checkProposal(User chancellor, AchievementProposal proposal);
}
