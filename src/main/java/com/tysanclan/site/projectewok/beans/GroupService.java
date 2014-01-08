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

import java.util.List;

import com.tysanclan.site.projectewok.entities.Committee;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.GamingGroup;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.Group.JoinPolicy;
import com.tysanclan.site.projectewok.entities.GroupCreationRequest;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.SocialGroup;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public interface GroupService {
	@Deprecated()
	public GamingGroup createGamingGroup(String name, String description,
			Game game);

	@Deprecated
	public SocialGroup createSocialGroup(String name, String description);

	public Committee createCommittee(String name, String description);

	public GroupCreationRequest createGamingGroupRequest(User requester,
			Game game, Realm realm, String name, String description,
			String motivation);

	public GroupCreationRequest createSocialGroupRequest(User requester,
			String name, String description, String motivation);

	public boolean addUserToGroup(User user, Group group);

	public boolean inviteUserToGroup(User user, Group group);

	public boolean setGroupLeader(User user, Group group);

	public void applyToGroup(User user, Group group);

	public void declineInvitation(User user, Group group);

	public void acceptInvitation(User user, Group group);

	public void acceptGroupJoinRequest(User accepter, Group group,
			User applicant);

	public void declineGroupJoinRequest(User accepter, Group group,
			User applicant);

	public void leaveGroup(User user, Group group);

	public void disbandGroup(User disbander, Group group);

	public void declineRequest(User decliner, GroupCreationRequest request);

	public void acceptRequest(User accepter, GroupCreationRequest request);

	public void setJoinPolicy(Group group, JoinPolicy joinPolicy);

	public void setGroupMOTD(Group group, String motd);

	public void setGroupDescription(Group group, String description);

	public void removeFromGroup(User modelObject, Group object);

	void clearRequestedGroups(User user);

	List<Group> clearGroupLeaderStatus(User user);

	void clearGroupMemberships(User user);
}
