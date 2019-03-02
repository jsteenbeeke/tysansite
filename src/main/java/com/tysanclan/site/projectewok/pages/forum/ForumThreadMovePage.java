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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.webcomponents.core.form.choice.LambdaRenderer;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.components.PostPanel;
import com.tysanclan.site.projectewok.components.ThreadLink;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.ForumPage;
import com.tysanclan.site.projectewok.pages.ForumThreadPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ForumThreadMovePage extends TysanPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ForumService forumService;

	public ForumThreadMovePage(ForumThread thread) {
		super("Move thread: " + thread.getTitle());

		final User u = getUser();

		List<Forum> forums = forumService
				.getValidDestinationForums(thread.getForum(), u);

		final DropDownChoice<Forum> targetForum = new DropDownChoice<Forum>(
				"target");
		targetForum.setChoices(forums);
		targetForum.setModel(ModelMaker.wrap(forums.get(0), true));
		targetForum.setChoiceRenderer(LambdaRenderer.of(Forum::getName));

		Form<ForumThread> form = new Form<ForumThread>("moveform",
				ModelMaker.wrap(thread)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private ForumService service;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				if (!service.moveThread(getModelObject(),
						targetForum.getModelObject(), u)) {
					error("Unable to move thread: permission denied!");
					setResponsePage(
							new ForumThreadPage(getModelObject().getId(), 1,
									false));
				} else {
					setResponsePage(
							new ForumPage(targetForum.getModelObject()));
				}
			}

		};

		form.add(targetForum);

		form.add(new PostPanel("postpanel", thread.getPosts().get(0), false));

		form.add(new ThreadLink("no", thread));

		add(form);
	}
}
