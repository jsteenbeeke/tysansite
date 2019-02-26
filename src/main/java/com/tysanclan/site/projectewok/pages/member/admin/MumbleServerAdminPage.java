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
package com.tysanclan.site.projectewok.pages.member.admin;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.MumbleService;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.MumbleServer;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.MumbleServerDAO;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;

/**
 * @author Jeroen Steenbeeke
 */
public class MumbleServerAdminPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	@SpringBean
	private MumbleServerDAO mumbleServerDAO;

	@SpringBean
	private MumbleService mumbleService;

	public MumbleServerAdminPage(User user) {
		super("Mumble Server administration");

		if (!user.equals(roleService.getHerald())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		add(new ListView<MumbleServer>("servers",
				ModelMaker.wrap(mumbleServerDAO.findAll().toJavaList())) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MumbleServer> item) {
				MumbleServer server = item.getModelObject();

				item.add(new Label("serverId", new Model<Integer>(server
						.getServerID())));
				item.add(new Label("name", server.getName()));
				item.add(new Label("url", server.getUrl()));
				item.add(new Label("password", server.getPassword()));
				item.add(new IconLink.Builder("images/icons/server_delete.png",
						new DefaultClickResponder<MumbleServer>() {

							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								mumbleServerDAO.delete(getModelObject());

								setResponsePage(new MumbleServerAdminPage(
										getUser()));
							}

						}).newInstance("del"));
			}

		});

		final TextField<Integer> serverIdField = new TextField<Integer>(
				"serverId", new Model<Integer>(), Integer.class);
		final TextField<String> nameField = new TextField<String>("name",
				new Model<String>());
		final TextField<String> urlField = new TextField<String>("url",
				new Model<String>());
		final TextField<String> passwordField = new TextField<String>(
				"password", new Model<String>());
		final TextField<String> tokenField = new TextField<String>("token",
				new Model<String>());
		final TextField<String> secretField = new TextField<String>("secret",
				new Model<String>());

		Form<MumbleServer> addForm = new Form<MumbleServer>("addForm") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				mumbleService.createServer(serverIdField.getModelObject(),
						nameField.getModelObject(), urlField.getModelObject(),
						passwordField.getModelObject(),
						tokenField.getModelObject(),
						secretField.getModelObject());

				setResponsePage(new MumbleServerAdminPage(getUser()));
			}
		};

		addForm.add(serverIdField);
		addForm.add(nameField);
		addForm.add(urlField);
		addForm.add(passwordField);
		addForm.add(tokenField);
		addForm.add(secretField);

		add(addForm);

	}
}
