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

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.components.PostPanel;
import com.tysanclan.site.projectewok.components.ThreadLink;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.pages.ForumThreadPage;

@TysanMemberSecured
public class ConfirmForumThreadUnstickyPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public ConfirmForumThreadUnstickyPage(ForumThread thread) {
		super("Make sticky: " + thread.getTitle());

		ForumPost firstPost = thread.getPosts().get(0);

		PostPanel panel = new PostPanel("postpanel", firstPost, false);

		Form<ForumThread> form = new Form<ForumThread>("unstickyform",
				ModelMaker.wrap(thread)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			ForumService service;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				if (!service.unstickyThread(getModelObject(),
						ConfirmForumThreadUnstickyPage.this.getUser())) {
					error("Unable to unsticky thread: Permission denied");
				}
				setResponsePage(new ForumThreadPage(getModelObject().getId(),
						1, false));
			}
		};

		form.add(panel);
		form.add(new ThreadLink("no", thread, false));
		add(form);
	}
}
