/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.LawEnforcementService;
import com.tysanclan.site.projectewok.components.TysanDropDownChoice;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.TruthsayerNominationDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.TruthsayerNominationFilter;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class TruthsayerNominationPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDAO userDAO;

	@SpringBean
	private TruthsayerNominationDAO nominationDAO;

	/**
	 *
	 */
	public TruthsayerNominationPage() {
		super("Nominate Truthsayer");

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
				TysanDropDownChoice<User> userChoice = (TysanDropDownChoice<User>) get(
						"user");
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
		filter.retired(false);
		filter.rank(Rank.REVERED_MEMBER);
		filter.orRank(Rank.FULL_MEMBER);
		filter.orRank(Rank.SENIOR_MEMBER);
		filter.username().orderBy(false);

		List<User> users = userDAO.findByFilter(filter).filter(user ->
				nominationDAO.countByFilter(
						new TruthsayerNominationFilter().user(user)) == 0)
				.toJavaList();

		nominateForm.add(new TysanDropDownChoice<User>("user", null, users)
				.setNullValid(false));

		nominateForm.setVisible(!users.isEmpty());

		if (users.isEmpty()) {
			info("There are no users who qualify to become a Truthsayer");
		}

		add(nominateForm);

	}
}
