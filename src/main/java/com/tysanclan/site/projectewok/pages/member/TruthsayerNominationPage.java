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
package com.tysanclan.site.projectewok.pages.member;

import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.LawEnforcementService;
import com.tysanclan.site.projectewok.components.TysanDropDownChoice;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.UserFilter;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class TruthsayerNominationPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDAO userDAO;

	/**
	 * 
	 */
	public TruthsayerNominationPage() {
		super("Nominate Truthsayer");

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);
		accordion.getOptions().put("heightStyle", "content");

		Form<?> nominateForm = new Form<Void>("nominateform") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private LawEnforcementService lawEnforcementService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TysanDropDownChoice<User> userChoice = (TysanDropDownChoice<User>) get("user");
				User user = userChoice.getModelObject();

				if (user != null) {
					lawEnforcementService.nominateTruthsayer(getUser(), user);

					setResponsePage(new TruthsayerNominationPage());
				} else {
					error("You must select a user!");
				}
			}

		};

		UserFilter filter = new UserFilter();
		filter.setRetired(false);
		filter.addRank(Rank.REVERED_MEMBER);
		filter.addRank(Rank.FULL_MEMBER);
		filter.addRank(Rank.SENIOR_MEMBER);
		filter.setTruthsayerNominated(false);
		filter.addOrderBy("username", true);

		List<User> users = userDAO.findByFilter(filter);

		nominateForm.add(new TysanDropDownChoice<User>("user", null, users)
				.setNullValid(false));

		nominateForm.setVisible(!users.isEmpty());

		if (users.isEmpty()) {
			info("There are no users who qualify to become a Truthsayer");
		}

		accordion.add(nominateForm);

		add(accordion);
	}
}
