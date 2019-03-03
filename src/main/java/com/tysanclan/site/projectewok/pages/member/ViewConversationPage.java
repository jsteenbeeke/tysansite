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
package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.components.ConversationContentPanel;
import com.tysanclan.site.projectewok.components.models.EntityClickListener;
import com.tysanclan.site.projectewok.entities.ConversationParticipation;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.Link;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class ViewConversationPage extends TysanPage
		implements EntityClickListener<ConversationParticipation> {
	private static final long serialVersionUID = 1L;

	public ViewConversationPage(ConversationParticipation conversation) {
		super(conversation.getConversation().getTitle());

		add(new Link<Void>("back") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new MessageListPage());

			}
		});

		add(new ConversationContentPanel("conversation", conversation, this));
	}

	@Override
	public void onEntityClick(AjaxRequestTarget target,
			ConversationParticipation entity) {
		setResponsePage(new MessageListPage());

	}
}
