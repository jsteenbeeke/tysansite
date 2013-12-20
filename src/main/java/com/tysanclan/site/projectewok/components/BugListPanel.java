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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.FilterDataProvider;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.entities.Bug;
import com.tysanclan.site.projectewok.entities.Bug.BugStatus;
import com.tysanclan.site.projectewok.entities.dao.BugDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.BugFilter;

public abstract class BugListPanel extends Panel {
	private static final long serialVersionUID = 1L;
	@SpringBean
	private BugDAO bugDAO;

	public BugListPanel(String id, String title, BugFilter filter) {
		super(id);

		add(new Label("title", title));

		DataView<Bug> bugView = new DataView<Bug>("bugs",
				FilterDataProvider.of(filter, bugDAO)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Bug> item) {
				Bug bug = item.getModelObject();

				Link<Bug> link = new Link<Bug>("link", ModelMaker.wrap(bug)) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						BugListPanel.this.onClick(getModelObject());
					}
				};

				link.add(new Label("title", bug.getTitle())
						.setRenderBodyOnly(true));

				item.add(link);
				item.add(new DateLabel("lastUpdate",
						bug.getUpdated() != null ? bug.getUpdated() : bug
								.getReported()));
				if (bug.getReporter() != null) {
					item.add(new MemberListItem("reportedBy", bug.getReporter()));
				} else {
					item.add(new BBCodePanel("reportedBy",
							"[i]Someone not logged in[/i]"));
				}
				if (bug.getAssignedTo() != null) {
					item.add(new MemberListItem("assignedTo", bug
							.getAssignedTo()));
				} else {
					item.add(new BBCodePanel("assignedTo", "[i]Nobody[/i]"));
				}
				item.add(new Label("status", new Model<BugStatus>(bug
						.getStatus())));

			}
		};
		bugView.setItemsPerPage(10);

		add(new PagingNavigator("paging", bugView));
		add(bugView);
	}

	public abstract void onClick(Bug bug);
}
