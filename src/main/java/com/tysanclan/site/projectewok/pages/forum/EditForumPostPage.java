/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.pages.forum;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.components.ForumPostEditorPanel;
import com.tysanclan.site.projectewok.components.ThreadLink;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ForumPostDAO;
import com.tysanclan.site.projectewok.pages.ForumThreadPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class EditForumPostPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public EditForumPostPage(ForumPost post) {
		super("Edit post");

		final ForumPostEditorPanel editor = new ForumPostEditorPanel("editor",
				post.getContent());

		final TextField<String> threadTitleField = new TextField<String>(
				"title", new Model<String>(post.getThread().getTitle()));

		Form<Long> form = new Form<Long>("editform",
				new Model<Long>(post.getId())) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private ForumService forumService;

			@SpringBean
			private ForumPostDAO postDAO;

			@Override
			protected void onSubmit() {
				postDAO.load(getModelObject()).forEach(fp -> {
					User currentUser = EditForumPostPage.this.getUser();
					forumService.editPost(fp, editor.getEditorContent(),
							currentUser);

					String newTitle = threadTitleField.getModelObject();

					if (newTitle != null && !newTitle.isEmpty() && !newTitle
							.equals(fp.getThread().getTitle())) {
						forumService.setThreadTitle(fp.getThread(), newTitle);
					}

					setResponsePage(
							new ForumThreadPage(fp.getThread().getId(), 1,
									false));
				});
			}

		};

		form.add(editor);

		threadTitleField
				.setVisible(post.getThread().getPosts().indexOf(post) == 0);

		form.add(threadTitleField);

		add(form);

		add(new ThreadLink("backtothread", post.getThread(), false));

	}
}
