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
package com.tysanclan.site.projectewok.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.pages.ForumThreadPage;

/**
 * @author Jeroen Steenbeeke
 */
public class LastPostLink extends Panel {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ForumService forumService;

	public LastPostLink(String id, ForumThread thread) {
		super(id);

		TysanSession sess = TysanSession.get();

		PageParameters params = new PageParameters();
		params.add("threadid", thread.getId().toString());

		int postIndex = 0;

		ForumPost firstPost = null;

		if (sess != null && sess.getUser() != null) {
			for (ForumPost post : thread.getPosts()) {
				if (forumService.isPostUnread(sess.getUser(), post)) {
					firstPost = post;
					break;
				}
				postIndex++;
			}

			setVisible(firstPost != null);
		} else {
			setVisible(false);
		}

		params.add("pageid",
				Integer.valueOf(1 + (postIndex / ForumThread.POSTS_PER_PAGE))
						.toString());

		WebMarkupContainer threadLink = new WebMarkupContainer("link");

		String url = RequestCycle.get().urlFor(ForumThreadPage.class, params)
				.toString();

		if (firstPost != null) {
			threadLink.add(AttributeModifier.replace("href", url + "#"
					+ firstPost.getId().toString()));
		}

		threadLink
				.add(new ContextImage("icon", "images/icons/arrow_right.png"));

		add(threadLink);

	}
}
