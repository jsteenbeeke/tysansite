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
package com.tysanclan.site.projectewok.components;

import java.util.Arrays;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.GameAccount;
import com.tysanclan.site.projectewok.entities.GameAccount.AccountType;
import com.tysanclan.site.projectewok.entities.UserGameRealm;
import com.tysanclan.site.projectewok.pages.member.EditAccountsPage;

/**
 * @author Jeroen Steenbeeke
 */
public class AccountPanel extends Panel {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private GameService gameService;

	private IModel<UserGameRealm> ugrModel;

	/**
	 * 
	 */
	public AccountPanel(String id, UserGameRealm userGameRealm) {
		super(id);

		ugrModel = ModelMaker.wrap(userGameRealm);

		Accordion accordion = new Accordion("accordion");
		accordion.setAutoHeight(false);
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.getOptions().put("heightStyle", "'content'");

		accordion.add(new Label("realmgamename", userGameRealm.getGame()
				.getName() + " on " + userGameRealm.getRealm().getName()));

		accordion.add(new ListView<GameAccount>("accounts", ModelMaker
				.wrap(userGameRealm.getAccounts())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<GameAccount> item) {
				GameAccount account = item.getModelObject();

				item.add(new Label("name", account.toString()));
				item.add(new IconLink.Builder("images/icons/cross.png",
						new DefaultClickResponder<GameAccount>(ModelMaker
								.wrap(account)) {

							private static final long serialVersionUID = 1L;

							/**
							 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
							 */
							@Override
							public void onClick() {
								gameService.deleteAccount(getModelObject());

								setResponsePage(new EditAccountsPage());
							}

						}).newInstance("delete"));

			}

		});

		accordion.add(new ListView<AccountType>("addAccountForms", Arrays
				.asList(AccountType.values())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<AccountType> item) {
				AccountType type = item.getModelObject();

				item.add(type.createAddForm("addPanel", ugrModel.getObject()));
			}
		});

		add(accordion);

		accordion.add(new IconLink.Builder("images/icons/cross.png",
				new DefaultClickResponder<UserGameRealm>(ModelMaker
						.wrap(userGameRealm)) {
					private static final long serialVersionUID = 1L;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						gameService.removePlayedGame(getModelObject());

						setResponsePage(new EditAccountsPage());
					}
				}).setText("I no longer play this game on this realm")
				.newInstance("stopplaying"));

	}

	@Override
	protected void onDetach() {
		ugrModel.detach();

		super.onDetach();
	}
}
