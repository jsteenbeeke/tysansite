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
package com.tysanclan.site.projectewok.components;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.EventDAO;
import com.tysanclan.site.projectewok.entities.dao.TrialDAO;
import com.tysanclan.site.projectewok.pages.forum.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
public class ForumThreadModeratorPanel extends Panel {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ForumService forumService;

	@SpringBean
	private TrialDAO trialDAO;

	@SpringBean
	private EventDAO eventDAO;

	public ForumThreadModeratorPanel(String id, ForumThread thread) {
		super(id);

		IModel<ForumThread> model = ModelMaker.wrap(thread);

		add(new Link<ForumThread>("delete", model) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(
						new ConfirmForumThreadDeletePage(getModelObject()));
			}
		}.setVisible(!thread.isLocked() && !thread.isPostSticky()
				&& trialDAO.getTrialByThread(thread) == null
				&& eventDAO.getEventByThread(thread) == null));
		add(new Link<ForumThread>("lock", model) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(
						new ConfirmForumThreadLockPage(getModelObject()));
			}
		}.setVisible(!thread.isLocked()));

		add(new Link<ForumThread>("unlock", model) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(
						new ConfirmForumThreadUnlockPage(getModelObject()));
			}
		}.setVisible(thread.isLocked()));

		add(new Link<ForumThread>("sticky", model) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(
						new ConfirmForumThreadStickyPage(getModelObject()));
			}
		}.setVisible(!thread.isLocked() && !thread.isPostSticky()));

		add(new Link<ForumThread>("unsticky", model) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(
						new ConfirmForumThreadUnstickyPage(getModelObject()));
			}
		}.setVisible(!thread.isLocked() && thread.isPostSticky()));

		User u = TysanSession.session().flatMap(TysanSession::getUser)
				.getOrNull();

		add(new Link<ForumThread>("move", model) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ForumThreadMovePage(getModelObject()));
			}
		}.setVisible(
				!forumService.getValidDestinationForums(thread.getForum(), u)
						.isEmpty() && !thread.isLocked() && !thread
						.isPostSticky()));
		add(new Link<ForumThread>("split", model) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ForumThreadSplitPage(getModelObject()));
			}
		}.setVisible(!thread.isLocked() && thread.getPosts().size() > 1));
	}
}
