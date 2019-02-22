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
package com.tysanclan.site.projectewok.pages.forum;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanNonMemberSecured;
import com.tysanclan.site.projectewok.components.ThreadLink;
import com.tysanclan.site.projectewok.entities.JoinApplication;
import com.tysanclan.site.projectewok.entities.dao.JoinApplicationDAO;
import com.tysanclan.site.projectewok.entities.filter.JoinApplicationFilter;

/**
 * @author Jeroen Steenbeeke
 */
@TysanNonMemberSecured
public class JoinPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private JoinApplicationDAO joinApplicationDAO;

	public JoinPage() {
		super("Join Tysan");

		WebMarkupContainer container = new WebMarkupContainer("container");
		WebMarkupContainer container2 = new WebMarkupContainer("container2");

		JoinApplicationFilter filter = new JoinApplicationFilter();
		filter.setApplicant(getUser());
		List<JoinApplication> applications = joinApplicationDAO
				.findByFilter(filter);

		int count = applications.size();

		container.add(new Link<JoinPage2>("continue") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new JoinPage2());

			}
		});
		add(container);
		container.setVisible(count == 0);

		container2.add(new ThreadLink("join", applications.isEmpty() ? null
				: applications.get(0).getJoinThread()));
		add(container2);

		container2.setVisible(count > 0);

	}
}
