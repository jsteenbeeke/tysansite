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
package com.tysanclan.site.projectewok.pages.member.group;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.Builder;
import com.tysanclan.site.projectewok.entities.Committee;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import com.tysanclan.site.projectewok.pages.member.OverviewPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class DisbandGroupPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	private IModel<Group> groupModel;

	/**
	 * 
	 */
	public DisbandGroupPage(Group group) {
		super("Disband " + group.getName());

		if (((group instanceof Committee) && getUser().getRank() != Rank.CHANCELLOR)
				|| (!(group instanceof Committee) && !group.getLeader().equals(
						getUser()))) {

			throw new RestartResponseAtInterceptPageException(
					new AccessDeniedPage());
		}

		groupModel = ModelMaker.wrap(group);

		add(new Label("name", group.getName()));
		Builder builder = new IconLink.Builder("images/icons/tick.png",
				new IconLink.DefaultClickResponder<Void>() {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private GroupService groupService;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						groupService.disbandGroup(getUser(), getGroup());
						setResponsePage(new OverviewPage());
					}

				});

		builder.setText("Yes, I understand the consequences and wish to proceed with disbanding this group");

		add(builder.newInstance("yes"));

		builder = new IconLink.Builder("images/icons/cross.png",
				new IconLink.DefaultClickResponder<Void>() {
					private static final long serialVersionUID = 1L;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						setResponsePage(new OverviewPage());
					}
				});

		builder.setText("No, I have changed my mind and do not wish to disband this group");

		add(builder.newInstance("no"));

	}

	public Group getGroup() {
		return groupModel.getObject();
	}

	/**
	 * @see org.apache.wicket.Page#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();

		groupModel.detach();
	}
}
