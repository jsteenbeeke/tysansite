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
package com.tysanclan.site.projectewok.components.accountpanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.entities.GameAccount;
import com.tysanclan.site.projectewok.entities.GameAccount.AccountType;
import com.tysanclan.site.projectewok.entities.UserGameRealm;
import com.tysanclan.site.projectewok.pages.member.EditAccountsPage;

public class MineCraftPanel extends Panel {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private GameService gameService;

	public MineCraftPanel(String id, UserGameRealm userGameRealm) {
		super(id);

		Form<UserGameRealm> form = new Form<UserGameRealm>("addMinecraftForm",
				ModelMaker.wrap(userGameRealm)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				UserGameRealm ugr = getModelObject();

				TextField<String> nameField = (TextField<String>) get("name");

				GameAccount acc = gameService.createMinecraftAccount(ugr,
						nameField.getModelObject());

				if (acc == null) {
					error("Invalid account name");
					return;
				}

				setResponsePage(new EditAccountsPage());
			}

		};

		form.add(new TextField<String>("name", new Model<String>(""))
				.setRequired(true));

		setVisible(gameService.isValidAccountType(userGameRealm.getGame(),
				userGameRealm.getRealm(), AccountType.MINECRAFT));

		add(form);

	}

}
