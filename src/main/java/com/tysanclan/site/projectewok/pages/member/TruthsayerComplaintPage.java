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

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.LawEnforcementService;
import com.tysanclan.site.projectewok.components.BBCodeTextArea;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;

public class TruthsayerComplaintPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDAO userDAO;

	public TruthsayerComplaintPage() {
		super("File Truthsayer Complaint");

		UserFilter filter = new UserFilter();
		filter.rank(Rank.TRUTHSAYER);
		filter.username().orderBy(true);

		final DropDownChoice<User> userChoice = new DropDownChoice<User>(
				"user", ModelMaker.wrap((User) null),
				ModelMaker.wrapChoices(userDAO.findByFilter(filter).toJavaList()));
		userChoice.setRequired(true);
		userChoice.setNullValid(true);

		final TextArea<String> motivationArea = new BBCodeTextArea(
				"motivation", "");
		motivationArea.setRequired(true);

		Form<User> complaintForm = new Form<User>("complaintForm") {

			private static final long serialVersionUID = 1L;

			@SpringBean
			private LawEnforcementService lawEnforcementService;

			@Override
			protected void onSubmit() {
				super.onSubmit();

				lawEnforcementService.fileComplaint(getUser(),
						userChoice.getModelObject(),
						motivationArea.getModelObject());

				setResponsePage(new OverviewPage());
			}
		};

		complaintForm.add(userChoice);
		complaintForm.add(motivationArea);

		add(complaintForm);
	}
}
