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
package com.tysanclan.site.projectewok.pages;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.components.AutoGroupLink;
import com.tysanclan.site.projectewok.entities.Committee;
import com.tysanclan.site.projectewok.entities.GamingGroup;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.SocialGroup;
import com.tysanclan.site.projectewok.entities.dao.CommitteeDAO;
import com.tysanclan.site.projectewok.entities.dao.GamingGroupDAO;
import com.tysanclan.site.projectewok.entities.dao.SocialGroupDAO;

/**
 * @author Jeroen Steenbeeke
 */
public class GroupsPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private SocialGroupDAO socialGroupDAO;

	@SpringBean
	private GamingGroupDAO gamingGroupDAO;

	@SpringBean
	private CommitteeDAO commiteeDAO;

	public GroupsPage() {
		super("Groups");

		add(new GroupLister<SocialGroup>("social", socialGroupDAO.findAll()));
		add(new GroupLister<GamingGroup>("gaming", gamingGroupDAO.findAll()));
		add(new GroupLister<Committee>("committee", commiteeDAO.findAll()));

	}

	private static class GroupLister<T extends Group> extends ListView<T> {
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public GroupLister(String id, List<T> groups) {
			super(id, ModelMaker.wrap(groups));
			setVisible(!groups.isEmpty());
		}

		@Override
		protected void populateItem(ListItem<T> item) {
			item.add(new AutoGroupLink("link", item.getModelObject()));
		}

	}
}
