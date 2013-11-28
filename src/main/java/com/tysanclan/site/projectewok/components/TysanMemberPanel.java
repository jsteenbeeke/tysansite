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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.dialog.Dialog;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.TysanTopPanel;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.ConversationParticipation;
import com.tysanclan.site.projectewok.entities.Message;
import com.tysanclan.site.projectewok.entities.MumbleServer;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ConversationParticipationDAO;
import com.tysanclan.site.projectewok.entities.dao.MumbleServerDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.ConversationParticipationFilter;
import com.tysanclan.site.projectewok.pages.ForumOverviewPage;
import com.tysanclan.site.projectewok.pages.member.MessageListPage;
import com.tysanclan.site.projectewok.pages.member.OverviewPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class TysanMemberPanel extends TysanTopPanel {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private MembershipService memberService;

	@SpringBean
	private ForumService forumService;

	@SpringBean
	private MumbleServerDAO serverDAO;

	@SpringBean
	private UserService userService;

	public TysanMemberPanel(String id, User user) {
		super(id, ModelMaker.wrap(user));

		forumService.addUnreadPosts(user);

		memberService.registerAction(user);

		addForumLink();
		addOverviewLink();

		addMembersOnlineLink();

		addMessageLink(user);

		add(new LogoutLink("logout"));
	}

	private void addMessageLink(User user) {
		Link<Void> messageLink = new Link<Void>("messages") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new MessageListPage());
			}
		};

		messageLink.add(new Label("count", new UnreadMessagesModel(user)));

		add(messageLink);
	}

	/**
	 * 
	 */
	private void addForumLink() {
		Link<Void> link = new Link<Void>("forums") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ForumOverviewPage());

			}

		};

		TysanSession session = (TysanSession) Session.get();

		link.add(new Label("count", new Model<Integer>(
				(session != null && session.getUser() != null) ? forumService
						.countUnread(session.getUser()) : 0)));

		add(link);
	}

	/**
	 	 */
	private Dialog addMembersOnlineLink() {
		Dialog window = new Dialog("onlinewindow");
		window.setTitle("Members online");
		window.setOutputMarkupId(true);
		window.setOutputMarkupPlaceholderTag(true);
		window.setWidth(400);

		window.add(new OtterSniperPanel("otterSniperPanel", 4));

		AjaxLink<Dialog> link = new AjaxLink<Dialog>("online",
				new Model<Dialog>(window)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				if (target != null) {
					target.appendJavaScript(getModelObject().open().render()
							.toString());
				}

			}

		};

		link.add(new Label("count", new MembersOnlineCountModel())
				.setOutputMarkupId(true));

		window.add(new ListView<User>("members", ModelMaker.wrap(userService
				.getMembersOnline())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<User> item) {
				MemberListItem memberLink = new MemberListItem("link", item
						.getModelObject());

				item.add(memberLink);

			}

		});

		window.add(new ListView<MumbleServer>("servers", ModelMaker
				.wrap(serverDAO.findAll())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MumbleServer> item) {
				MumbleServer server = item.getModelObject();

				item.add(new WebMarkupContainer("server").add(
						AttributeModifier.replace("data-token",
								server.getApiToken())).add(
						AttributeModifier.replace("data-id",
								server.getServerID())));

				item.add(new ExternalLink("url", server.getUrl()).setBody(Model
						.of(server.getUrl())));
				item.add(new Label("password", server.getPassword()));

			}

		});

		add(link);
		add(window);
		return window;
	}

	/**
	 * 
	 */
	private void addOverviewLink() {
		add(new Link<Void>("overview") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new OverviewPage());
			}
		});
	}

	private static class MembersOnlineCountModel implements IModel<Integer> {
		private static final long serialVersionUID = 1L;

		@SpringBean
		private UserService _service;

		@SpringBean
		private MumbleServerDAO mumbleDAO;

		/**
		 * @see org.apache.wicket.model.IModel#getObject()
		 */
		@Override
		public Integer getObject() {
			if (_service == null) {
				Injector.get().inject(this);
			}

			return _service.getMembersOnline().size();
		}

		/**
		 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
		 */
		@Override
		public void setObject(Integer object) {
			throw new UnsupportedOperationException(
					"setObject not allowed on a read-only model");
		}

		/**
		 * @see org.apache.wicket.model.IDetachable#detach()
		 */
		@Override
		public void detach() {
			_service = null;

		}
	}

	private static class UnreadMessagesModel implements IModel<Integer> {
		private static final long serialVersionUID = 1L;

		private IModel<User> userModel;

		@SpringBean
		private ConversationParticipationDAO _service;

		/**
		 * 
		 */
		public UnreadMessagesModel(User user) {
			userModel = ModelMaker.wrap(user);
		}

		/**
		 * @see org.apache.wicket.model.IModel#getObject()
		 */
		@Override
		public Integer getObject() {
			if (_service == null) {
				Injector.get().inject(this);
			}

			ConversationParticipationFilter filter = new ConversationParticipationFilter();
			filter.setUser(userModel.getObject());

			List<ConversationParticipation> participations = _service
					.findByFilter(filter);

			int count = 0;

			for (ConversationParticipation participation : participations) {
				Set<Message> unread = new HashSet<Message>();
				unread.addAll(participation.getConversation().getMessages());
				unread.removeAll(participation.getReadMessages());
				count += unread.size();
			}

			return count;
		}

		/**
		 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
		 */
		@Override
		public void setObject(Integer object) {
			throw new UnsupportedOperationException(
					"Can not set object on read-only model");

		}

		/**
		 * @see org.apache.wicket.model.IDetachable#detach()
		 */
		@Override
		public void detach() {
			userModel.detach();
			_service = null;
		}

	}
}
