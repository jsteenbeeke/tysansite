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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.entities.User;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import java.util.Date;

/**
 * @author Jeroen Steenbeeke
 */
public class MentorPanel extends TysanOverviewPanel<User> {

	private static final long serialVersionUID = 1L;

	public MentorPanel(String id, User mentor) {
		super(id, ModelMaker.wrap(mentor), "Pupils");

		setVisible(!mentor.getPupils().isEmpty());

		add(new Label("count", new Model<>(mentor.getPupils().size())));

		add(new ListView<User>("pupils", ModelMaker.wrap(mentor.getPupils())) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<User> item) {
				item.add(new UserContactInfoPanel("pupil",
						item.getModelObject()));

			}
		});

		Date previousLogin = TysanSession.get().getPreviousLogin();

		if (previousLogin != null) {
			for (User pupil : getUser().getPupils()) {
				if (pupil.getJoinDate() != null && pupil.getJoinDate()
						.after(previousLogin)) {
					super.requiresAttention();
					break;
				}
			}
		}
	}

}
