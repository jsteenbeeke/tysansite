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
package com.tysanclan.site.projectewok.pages.member.group;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import com.tysanclan.site.projectewok.pages.member.OverviewPage;

/**
 * @author Jeroen Steenbeeke
 */
public class LeaveGroupPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	public LeaveGroupPage(Group group) {
		super("Leave " + group.getName());

		Accordion accordion = new Accordion("accordion");
		accordion.setAutoHeight(false);
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.getOptions().put("heightStyle", "'content'");

		accordion.add(new Label("title", "Leave " + group.getName()));

		Link<Group> yesLink = new Link<Group>("yes", ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private GroupService groupService;

			@Override
			public void onClick() {
				groupService.leaveGroup(getUser(), getModelObject());

				setResponsePage(new OverviewPage());
			}

		};

		yesLink.add(new ContextImage("icon", "images/icons/tick.png"));

		Link<Group> noLink = new Link<Group>("no", ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new OverviewPage());
			}

		};

		noLink.add(new ContextImage("icon", "images/icons/cross.png"));

		accordion.add(yesLink);
		accordion.add(noLink);

		accordion.add(new Label("name", group.getName()));

		add(accordion);
	}
}
