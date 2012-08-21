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

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.components.ForumPostEditorPanel;
import com.tysanclan.site.projectewok.components.PostPanel;
import com.tysanclan.site.projectewok.components.ThreadLink;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.Trial;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ForumThreadDAO;
import com.tysanclan.site.projectewok.entities.dao.TrialDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.TrialFilter;
import com.tysanclan.site.projectewok.pages.ForumThreadPage;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class ReplyPage extends TysanPage {

	@SpringBean
	private ForumService forumService;

	public ReplyPage(ForumThread thread, int currentPage) {
		super("Reply to thread");

		final ForumPostEditorPanel editor = new ForumPostEditorPanel("editor",
				"");

		Form<Long> form = new Form<Long>("replyform", new Model<Long>(
				thread.getId())) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private ForumThreadDAO threadDAO;

			@SpringBean
			private TrialDAO trialDAO;

			@SpringBean
			private MembershipService mService;

			@Override
			protected void onSubmit() {
				ForumThread ft = threadDAO.load(getModelObject());
				User currentUser = ReplyPage.this.getTysanSession().getUser();

				Calendar oneWeekAgo = DateUtil.getCalendarInstance();
				oneWeekAgo.add(Calendar.WEEK_OF_YEAR, -1);

				TrialFilter filter = new TrialFilter();
				filter.setStartAfter(oneWeekAgo.getTime());
				filter.setRestrained(true);
				filter.setAccused(currentUser);

				List<Trial> trials = trialDAO.findByFilter(filter);
				boolean hasThread = false;
				for (Trial trial : trials) {
					if (trial.getTrialThread().equals(ft)) {
						hasThread = true;
						break;
					}
				}

				if (trials.isEmpty()
						|| hasThread
						|| (!ft.getPosts().isEmpty() && ft.getPosts().get(0)
								.getPoster().equals(getUser()))) {
					ForumPost post = forumService.replyToThread(ft,
							editor.getEditorContent(), currentUser);

					forumService.markForumPostRead(currentUser, post);

					int page = ((ft.getPosts().size() - 1) / ForumThread.POSTS_PER_PAGE) + 1;

					mService.registerAction(currentUser);

					setResponsePage(new ForumThreadPage(ft.getId(), page, false));
				} else {
					error("Your posting privileges have been revoked due to a currently running trial, you can only post in your Trial threads");
				}
			}

		};

		form.add(editor);

		List<ForumPost> revList = new LinkedList<ForumPost>();
		revList.addAll(thread.getPosts());
		Collections.reverse(revList);
		for (int i = revList.size() - 1; i >= 5; i--) {
			revList.remove(i);
		}

		ListView<ForumPost> backwards = new ListView<ForumPost>(
				"topicbackwards", ModelMaker.wrap(revList)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ForumPost> item) {
				item.add(new PostPanel("post", item.getModelObject()));
			}

		};

		add(form);
		add(backwards);

		add(new ThreadLink("backtothread", thread, currentPage, false));
	}
}
