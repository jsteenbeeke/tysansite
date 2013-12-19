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
package com.tysanclan.site.projectewok.pages.member.senate;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.RegulationChange;
import com.tysanclan.site.projectewok.entities.RegulationChange.ChangeType;
import com.tysanclan.site.projectewok.entities.dao.RegulationChangeDAO;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class VetoPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RegulationChangeDAO regulationChangeDAO;

	public VetoPage() {
		super("Proposed regulation changes");

		add(new ListView<RegulationChange>("votes",
				ModelMaker.wrap(regulationChangeDAO.findAll())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<RegulationChange> item) {
				RegulationChange change = item.getModelObject();

				String name = change.getChangeType() == ChangeType.ADD ? change
						.getTitle() : change.getRegulation().getName();

				item.add(new Label("title", ""
						+ change.getChangeType().toString() + " regulation "
						+ name));

				if (change.getChangeType() == ChangeType.MODIFY) {
					item.add(new Label("name", change.getTitle()));
				} else {
					item.add(new WebMarkupContainer("name").setVisible(false));
				}

				if (change.getChangeType() != ChangeType.ADD) {
					item.add(new Label("current", change.getRegulation()
							.getContents()).setEscapeModelStrings(false));
				} else {
					item.add(new WebMarkupContainer("current")
							.setVisible(false));
				}

				if (change.getChangeType() == ChangeType.REPEAL) {
					item.add(new WebMarkupContainer("body").setVisible(false));
				} else {
					item.add(new Label("body", change.getDescription())
							.setEscapeModelStrings(false));
				}

				item.add(new Form<RegulationChange>("vetoForm", ModelMaker
						.wrap(change)) {

					private static final long serialVersionUID = 1L;

					@SpringBean
					private DemocracyService democracyService;

					@Override
					protected void onSubmit() {
						democracyService.vetoRegulationChange(getUser(),
								getModelObject());
					}

				}.setVisible(!change.isVeto()));

				item.add(new WebMarkupContainer("veto").setVisible(change
						.isVeto()));
			}

		});

	}
}
