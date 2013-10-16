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

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import wicket.contrib.tinymce.TinyMceBehavior;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.MessageService;
import com.tysanclan.site.projectewok.components.models.EntityClickListener;
import com.tysanclan.site.projectewok.entities.ConversationParticipation;
import com.tysanclan.site.projectewok.entities.Message;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ConversationDAO;
import com.tysanclan.site.projectewok.entities.dao.ConversationParticipationDAO;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class ConversationContentPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private Long participationId;

	private TextArea<String> editor;

	private boolean editorVisible;

	@SpringBean
	private ConversationParticipationDAO conversationParticipationDAO;

	public void activate(AjaxRequestTarget target) {
		Accordion accordion = (Accordion) get("accordion");

		int i = 0;

		for (Message message : getParticipation().getConversation()
				.getMessages()) {
			if (!getParticipation().getReadMessages().contains(message)) {
				if (target != null) {
					target.appendJavaScript(accordion.activate(i).render()
							.toString());
				}
				return;
			}
			i++;
		}

		if (target != null) {
			target.appendJavaScript(accordion.activate(i - 1).render()
					.toString());
		}

	}

	@SpringBean
	private MessageService service;

	public ConversationContentPanel(String id,
			ConversationParticipation participation,
			final EntityClickListener<ConversationParticipation> listener) {
		super(id);

		participationId = participation.getId();

		for (Message m : participation.getConversation().getMessages()) {
			if (!participation.getReadMessages().contains(m)) {
				service.markAsRead(participation, m);
			}
		}

		add(new ListView<Message>("messages", new MessageList(participation
				.getConversation().getId())) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Message> item) {
				Message message = item.getModelObject();

				Accordion accordion = new Accordion("accordion");
				accordion
						.setHeader(new AccordionHeader(new LiteralOption("h2")));
				accordion.setAutoHeight(false);
				accordion.getOptions().put("heightStyle", "'content'");

				item.add(accordion);

				accordion.add(new Label("user",
						message.getSender() != null ? message.getSender()
								.getUsername() : "System"));
				accordion.add(new Label("time", DateUtil
						.getTimezoneFormattedString(message.getSendTime(),
								getUser().getTimezone())));
				accordion.add(new Label("content", message.getContent())
						.setEscapeModelStrings(false));
			}

		}.setOutputMarkupId(true).add(
				new AjaxSelfUpdatingTimerBehavior(Duration.seconds(15))));

		List<ConversationParticipation> plist = new LinkedList<ConversationParticipation>();
		plist.addAll(participation.getConversation().getParticipants());

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);

		accordion.add(new ListView<ConversationParticipation>("participants",
				ModelMaker.wrap(plist)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ConversationParticipation> item) {
				item.add(new MemberListItem("participant", item
						.getModelObject().getUser()));

			}

		});

		add(accordion);

		Accordion accordion2 = new Accordion("accordion2");
		accordion2.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion2.setAutoHeight(false);

		Form<ConversationParticipation> respondForm = new Form<ConversationParticipation>(
				"respondForm", ModelMaker.wrap(participation)) {
			private static final long serialVersionUID = 1L;
			@SpringBean
			private MessageService messageService;

			@Override
			protected void onSubmit() {
				String message = getEditorComponent().getModelObject();
				ConversationParticipation part = getModelObject();

				Message msg = messageService.respondToConversation(
						part.getConversation(), part.getUser(), message);

				messageService.markAsRead(part, msg);

				listener.onEntityClick(null, part);
			}
		};

		editorVisible = participation.getConversation().getParticipants()
				.size() > 1;

		accordion2.add(new WebMarkupContainer("respondheader")
				.setVisible(editorVisible));

		respondForm.setVisible(editorVisible);

		editor = new TextArea<String>("messagecontent", new Model<String>(""));
		editor.add(new TinyMceBehavior(new TysanTinyMCESettings()));
		editor.setOutputMarkupId(true);
		editor.setOutputMarkupPlaceholderTag(true);

		respondForm.add(editor);

		accordion2.add(respondForm);

		add(accordion2);
	}

	public ConversationParticipation getParticipation() {
		return conversationParticipationDAO.load(participationId);
	}

	private User getUser() {
		return getParticipation().getUser();
	}

	/**
	 * @return the editorVisible
	 */
	public boolean isEditorVisible() {
		return editorVisible;
	}

	/**
	 * @see org.apache.wicket.Component#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();

		if (editor != null)
			editor.detach();
	}

	public TextArea<String> getEditorComponent() {
		return editor;
	}

	private static class MessageList implements IModel<List<Message>> {
		private static final long serialVersionUID = 1L;

		private final Long conversationID;

		@SpringBean
		private ConversationDAO conversationDAO;

		/**
		 * 
		 */
		public MessageList(Long conversationID) {
			this.conversationID = conversationID;
		}

		/**
		 * @see org.apache.wicket.model.IModel#getObject()
		 */
		@Override
		public List<Message> getObject() {
			if (conversationDAO == null) {
				Injector.get().inject(this);
			}

			return conversationDAO.load(conversationID).getMessages();
		}

		/**
		 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
		 */
		@Override
		public void setObject(List<Message> object) {
			throw new UnsupportedOperationException(
					"Can not set object on self-updating model. Change the list in the database instead");

		}

		/**
		 * @see org.apache.wicket.model.IDetachable#detach()
		 */
		@Override
		public void detach() {
			conversationDAO = null;

		}

	}
}
