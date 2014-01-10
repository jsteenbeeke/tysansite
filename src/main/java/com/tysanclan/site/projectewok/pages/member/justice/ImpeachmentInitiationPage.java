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
package com.tysanclan.site.projectewok.pages.member.justice;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ImpeachmentDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.UserFilter;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import com.tysanclan.site.projectewok.pages.member.OverviewPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.TRUTHSAYER)
public class ImpeachmentInitiationPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDAO userDAO;

	@SpringBean
	private ImpeachmentDAO impeachmentDAO;

	/**
	 * 
	 */
	public ImpeachmentInitiationPage() {
		super("Impeach Chancellor");

		UserFilter filter = new UserFilter();
		filter.addRank(Rank.CHANCELLOR);

		List<User> chancellors = userDAO.findByFilter(filter);
		User chancellor = chancellors.isEmpty() ? null : chancellors.get(0);

		String chancellorName = chancellor != null ? chancellor.getUsername()
				: "Nobody";

		add(new IconLink.Builder("images/icons/tick.png",
				new DefaultClickResponder<User>(ModelMaker.wrap(getUser())) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private DemocracyService democracyService;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						democracyService
								.createImpeachmentVote(getModelObject());

						setResponsePage(new OverviewPage());
					}
				}).setText("Yes, I want to start the impeachment procedure")
				.newInstance("yes").setVisible(impeachmentDAO.countAll() == 0));
		add(new IconLink.Builder("images/icons/cross.png",
				new DefaultClickResponder<User>(ModelMaker.wrap(getUser())) {
					private static final long serialVersionUID = 1L;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						setResponsePage(new OverviewPage());
					}
				}).setText("No, I do not want to impeach " + chancellorName)
				.newInstance("no").setVisible(impeachmentDAO.countAll() == 0));
		add(new WebMarkupContainer("alreadyactive").setVisible(impeachmentDAO
				.countAll() != 0));

	}
}
