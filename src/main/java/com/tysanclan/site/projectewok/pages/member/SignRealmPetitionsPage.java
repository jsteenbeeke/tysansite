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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.RealmService;
import com.tysanclan.site.projectewok.components.DateLabel;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.RealmPetition;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.RealmPetitionDAO;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured({ Rank.CHANCELLOR, Rank.FULL_MEMBER, Rank.SENIOR_MEMBER,
		Rank.SENATOR, Rank.TRUTHSAYER, Rank.REVERED_MEMBER,
		Rank.JUNIOR_MEMBER })
public class SignRealmPetitionsPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RealmService realmService;

	@SpringBean
	private RealmPetitionDAO realmPetitionDAO;

	/**
	 *
	 */
	public SignRealmPetitionsPage() {
		super("Sign realm petitions");

		add(new Label("count", new Model<Integer>(
				realmService.getRequiredPetitionSignatures())));
		add(new ListView<RealmPetition>("petitions",
				ModelMaker.wrap(realmPetitionDAO.findAll().toJavaList())) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<RealmPetition> item) {
				RealmPetition petition = item.getModelObject();

				Form<RealmPetition> form = new Form<RealmPetition>("form",
						ModelMaker.wrap(petition)) {
					private static final long serialVersionUID = 1L;

					/**
					 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
					 */
					@Override
					protected void onSubmit() {
						super.onSubmit();

						RealmPetition rp = getModelObject();

						realmService.signPetition(rp, getUser());

						setResponsePage(new SignRealmPetitionsPage());
					}
				};

				item.add(form);

				item.add(new Label("name",
						petition.getGame().getName() + " on " + (
								petition.getName() != null ?
										petition.getName() :
										petition.getRealm().getName())));
				form.add(new Label("realm", petition.getName() != null ?
						petition.getName() :
						petition.getRealm().getName()));

				form.add(new Label("game", petition.getGame().getName()));

				form.add(new Label("starter",
						petition.getRequester().getUsername()));

				form.add(new DateLabel("expires", petition.getExpires()));

				form.add(new ListView<User>("signatures",
						ModelMaker.wrap(petition.getSignatures())) {

					private static final long serialVersionUID = 1L;

					/**
					 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
					 */
					@Override
					protected void populateItem(ListItem<User> innerItem) {
						User user = innerItem.getModelObject();
						innerItem.add(new MemberListItem("user", user));
					}
				});

				form.add(new WebMarkupContainer("sign").setVisible(
						!petition.getSignatures().contains(getUser())
								&& !petition.getRequester().equals(getUser())));
			}
		});
	}
}
