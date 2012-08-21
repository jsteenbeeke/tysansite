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
package com.tysanclan.site.projectewok.pages.member;

import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;

import com.tysanclan.site.projectewok.components.OtterSniperPanel;
import com.tysanclan.site.projectewok.dataaccess.FilterProvider;
import com.tysanclan.site.projectewok.entities.LogItem;
import com.tysanclan.site.projectewok.entities.dao.LogItemDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.LogItemFilter;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class LogPage extends AbstractMemberPage {
	public LogPage() {
		super("Clan Log");

		LogItemFilter filter = new LogItemFilter();
		filter.addOrderBy("LogTime", false);

		DataView<LogItem> pageable = new DataView<LogItem>("log",
				FilterProvider.of(LogItemDAO.class, filter)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<LogItem> item) {
				LogItem logItem = item.getModelObject();

				item.add(new Label("time", DateUtil
						.getESTFormattedString(new Date(logItem.getLogTime()))));
				item.add(new Label("category", logItem.getCategory()));
				item.add(new Label("user", logItem.getVisibleUsername()));
				item.add(new Label("action", logItem.getMessage()));

			}

		};

		pageable.setItemsPerPage(20);

		add(pageable);
		add(new OtterSniperPanel("otterSniperPanel", 1));

		add(new PagingNavigator("nav", pageable));
	}
}
