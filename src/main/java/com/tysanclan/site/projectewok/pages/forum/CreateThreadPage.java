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
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanLoginSecured;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.components.ForumPostEditorPanel;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.TrialDAO;
import com.tysanclan.site.projectewok.entities.filter.ForumThreadFilter;
import com.tysanclan.site.projectewok.entities.filter.TrialFilter;
import com.tysanclan.site.projectewok.pages.ForumThreadPage;
import com.tysanclan.site.projectewok.util.DateUtil;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;

@TysanLoginSecured
public class CreateThreadPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public CreateThreadPage(final Forum forum) {
		super("Create new thread");

		final TextField<String> tf = new TextField<String>("title",
				new Model<String>(""));

		final ForumPostEditorPanel editor = new ForumPostEditorPanel("content",
				"");

		Form<Forum> form = new Form<Forum>("threadform",
				ModelMaker.wrap(forum)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private ForumService forumService;

			@SpringBean
			private TrialDAO trialDAO;

			@SpringBean
			private MembershipService mService;

			@Override
			protected void onSubmit() {
				Forum f = getModelObject();

				User u = getUser();

				if (u != null && f != null) {
					Calendar oneWeekAgo = DateUtil.getCalendarInstance();
					oneWeekAgo.add(Calendar.WEEK_OF_YEAR, -1);

					TrialFilter filter = new TrialFilter();
					filter.trialThread(new ForumThreadFilter().postTime()
							.greaterThan(oneWeekAgo.getTime()));
					filter.restrained(true);
					filter.accused(u);

					long count = trialDAO.countByFilter(filter);

					if (count == 0) {
						ForumThread thread = forumService
								.createForumThread(forum, tf.getModelObject(),
										editor.getEditorContent(), u);

						if (thread != null) {
							mService.registerAction(u);

							setResponsePage(
									new ForumThreadPage(thread.getId(), 1,
											false));
						} else {
							error("Could not create new thread");
						}
					} else {
						error("Your posting privileges have been revoked due to a currently running trial, you can only post in your Trial threads");
					}
				} else {
					error("Login expired or invalid forum");
				}
			}
		};

		form.add(tf);
		form.add(editor);

		add(form);
	}
}
