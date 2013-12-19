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

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.GroupLeaderElection;

/**
 * @author Jeroen Steenbeeke
 */
public class RunForGroupLeaderPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	public RunForGroupLeaderPage(GroupLeaderElection election) {
		super("Run for group leader for " + election.getGroup().getName());

		add(new IconLink.Builder("images/icons/tick.png",
				new DefaultClickResponder<GroupLeaderElection>(
						ModelMaker.wrap(election)) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private DemocracyService democracyService;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						if (democracyService.addGroupLeaderCandidate(getUser(),
								getModelObject())) {
							setResponsePage(new OverviewPage());
						} else {
							error("Already a candidate or not eligible");
						}

					}

				}).setText("Yes, I want to run for group leader").newInstance(
				"yes"));
		add(new IconLink.Builder("images/icons/cross.png",
				new DefaultClickResponder<GroupLeaderElection>(
						ModelMaker.wrap(election)) {
					private static final long serialVersionUID = 1L;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						setResponsePage(new OverviewPage());
					}

				}).setText("No, I do not want to run for group leader")
				.newInstance("no"));
	}
}
