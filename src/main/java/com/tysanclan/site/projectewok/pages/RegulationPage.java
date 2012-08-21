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
package com.tysanclan.site.projectewok.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionAnimated;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.entities.Regulation;
import com.tysanclan.site.projectewok.entities.dao.RegulationDAO;

/**
 * @author Jeroen Steenbeeke
 */
public class RegulationPage extends TysanPage {
	@SpringBean
	private RegulationDAO dao;

	/**
	 * 
	 */
	public RegulationPage() {
		super("Regulations");

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAnimated(new AccordionAnimated("slide"));
		accordion.setAutoHeight(false);

		accordion.add(new ListView<Regulation>("regulations", ModelMaker
				.wrap(dao.findAll())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Regulation> item) {
				Regulation rel = item.getModelObject();
				item.add(new Label("name", rel.getName()));
				item.add(new Label("contents", rel.getContents())
						.setEscapeModelStrings(false));

			}

		});

		add(accordion);

	}
}
