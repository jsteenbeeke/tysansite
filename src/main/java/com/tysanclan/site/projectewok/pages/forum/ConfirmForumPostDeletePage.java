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
package com.tysanclan.site.projectewok.pages.forum;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanLoginSecured;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.components.PostPanel;
import com.tysanclan.site.projectewok.components.ThreadLink;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.dao.ForumDAO;
import com.tysanclan.site.projectewok.pages.ForumPage;
import com.tysanclan.site.projectewok.pages.ForumThreadPage;

@TysanLoginSecured
public class ConfirmForumPostDeletePage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public ConfirmForumPostDeletePage(ForumPost post) {
		super("Confirm delete");

		Form<ForumPost> form = new Form<ForumPost>("deleteform",
				ModelMaker.wrap(post)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private ForumDAO forumDAO;

			@SpringBean
			private ForumService forumService;

			@Override
			protected void onSubmit() {

				boolean threadExists = getModelObject().getThread().getPosts()
						.size() > 1;

				forumService.deletePost(getModelObject(),
						ConfirmForumPostDeletePage.this.getTysanSession()
								.getUser());
				if (threadExists) {
					setResponsePage(new ForumThreadPage(getModelObject()
							.getThread().getId(), 1, false));
				} else {
					setResponsePage(new ForumPage(
							forumDAO.load(getModelObject().getThread()
									.getForum().getId())));
				}
			}
		};

		form.add(new PostPanel("postpanel", post, false));

		form.add(new ThreadLink("no", post.getThread(), false));

		add(form);
	}
}
