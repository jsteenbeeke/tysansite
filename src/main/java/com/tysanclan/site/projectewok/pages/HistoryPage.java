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

import io.vavr.collection.Seq;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;

/**
 * @author Jeroen Steenbeeke
 */
public class HistoryPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDAO userDAO;

	/**
	 * 
	 */
	public HistoryPage() {
		super("Clan History");

		add(new ContextImage("ccimage", "images/cc-by-nc-nd.png"));

		User prospero = null;

		UserFilter filter = new UserFilter();
		filter.username("prospero");

		Seq<User> prosperos = userDAO.findByFilter(filter);
		if (!prosperos.isEmpty()) {
			prospero = prosperos.get(0);
		}

		PageParameters pp = new PageParameters();
		pp.add("userid", prospero != null ? prospero.getId().toString() : "");

		add(new BookmarkablePageLink<User>("prosperolink", MemberPage.class, pp)
				.setVisible(prospero != null));
	}
}
