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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.components.ForumPostEditorPanel;
import com.tysanclan.site.projectewok.components.PostPanel;
import com.tysanclan.site.projectewok.components.ThreadLink;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.ForumThreadPage;

/**
 * Page for splitting a thread, creating a new thread
 * 
 * @author Jeroen Steenbeeke
 */
public class ForumThreadSplitPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public static class PostSelectionPair {
		private ForumPost post;
		private Boolean selected;
		private PostSelectionPairModel parent;

		/**
		 * 
		 */
		public PostSelectionPair(PostSelectionPairModel parent, ForumPost post,
				Boolean selected) {
			this.post = post;
			this.selected = selected;
			this.parent = parent;
		}

		/**
		 * @return the post
		 */
		public ForumPost getPost() {
			return post;
		}

		/**
		 * @return the selected
		 */
		public Boolean getSelected() {
			return selected;
		}

		public void setSelected(Boolean selected) {
			parent.select(this, selected);
		}
	}

	private static class PostSelectionPairModel extends
			LoadableDetachableModel<List<PostSelectionPair>> {
		private static final long serialVersionUID = 1L;

		private IModel<List<ForumPost>> posts;

		private List<Boolean> selected;

		public PostSelectionPairModel(List<ForumPost> posts) {
			super();

			selected = new LinkedList<Boolean>();
			List<ForumPost> baselist = new LinkedList<ForumPost>();
			for (ForumPost post : posts) {
				baselist.add(post);
				selected.add(false);
			}
			this.posts = ModelMaker.wrap(baselist);
		}

		public void select(PostSelectionPair pair, boolean select) {
			int idx = posts.getObject().indexOf(pair.getPost());

			selected.remove(idx);
			selected.add(idx, select);

		}

		/**
		 * @see org.apache.wicket.model.LoadableDetachableModel#detach()
		 */
		@Override
		public void detach() {
			super.detach();
			posts.detach();
		}

		/**
		 * @see org.apache.wicket.model.LoadableDetachableModel#getObject()
		 */
		@Override
		public List<PostSelectionPair> getObject() {
			return load();
		}

		/**
		 * @see org.apache.wicket.model.LoadableDetachableModel#load()
		 */
		@Override
		protected List<PostSelectionPair> load() {
			List<ForumPost> postlist = posts.getObject();

			List<PostSelectionPair> result = new LinkedList<PostSelectionPair>();
			for (int i = 0; i < postlist.size(); i++) {
				result.add(new PostSelectionPair(this, postlist.get(i),
						selected.get(i)));
			}

			return result;
		}

	}

	@SpringBean
	private ForumService forumService;

	public ForumThreadSplitPage(ForumThread thread) {
		super("Split thread: " + thread.getTitle());

		TysanSession sess = (TysanSession) Session.get();
		User u = sess != null ? sess.getUser() : null;

		List<ForumPost> posts = forumService.filterPosts(u, false,
				thread.getPosts());
		ForumPost firstPost = posts.get(0);
		List<ForumPost> filtered = new ArrayList<ForumPost>(posts.size() - 1);

		for (ForumPost post : posts) {
			if (!firstPost.equals(post)) {
				filtered.add(post);
			}
		}

		final ListView<PostSelectionPair> postselection = new ListView<PostSelectionPair>(
				"postselection", new PostSelectionPairModel(posts)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<PostSelectionPair> item) {
				IModel<PostSelectionPair> model = item.getModel();

				item.add(new CheckBox("checkbox", new PropertyModel<Boolean>(
						model, "selected")));
				item.add(new PostPanel("postpanel", model.getObject().getPost()));
			}

		};

		final TextField<String> titleField = new TextField<String>("title",
				new Model<String>(""));

		final ForumPostEditorPanel editor = new ForumPostEditorPanel("editor",
				"");

		Form<ForumThread> form = new Form<ForumThread>("splitform",
				ModelMaker.wrap(thread)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				ForumThread t = getModelObject();

				TysanSession session = (TysanSession) Session.get();
				User user = session != null ? session.getUser() : null;

				List<PostSelectionPair> pairsLIst = postselection
						.getModelObject();
				List<ForumPost> postList = new LinkedList<ForumPost>();
				for (PostSelectionPair pair : pairsLIst) {
					if (pair.getSelected() == true) {
						postList.add(pair.getPost());
					}
				}

				if (titleField.getModelObject() == null
						|| titleField.getModelObject().trim().isEmpty()) {
					error("Title may not be empty!");
				} else if (editor.getEditorContent().trim().isEmpty()) {
					error("Thread opening may not be empty");
				} else if (postList.isEmpty()) {
					error("You must select at least 1 post to move to the new thread");
				} else {
					if (forumService.isModerator(user, t.getForum())) {

						ForumThread result = forumService.splitThread(t,
								postList, titleField.getModelObject(),
								editor.getEditorContent(), user);
						if (result == null) {
							error("Could not split thread: permission denied!");
						}
					} else {
						error("Could not split thread: permission denied!");
					}

					setResponsePage(new ForumThreadPage(t.getId(), 1, false));
				}
			}
		};

		form.add(editor);

		form.add(titleField);

		form.add(postselection);

		form.add(new PostPanel("threadstart", firstPost, false));

		form.add(new ThreadLink("no", thread));

		add(form);
	}
}
