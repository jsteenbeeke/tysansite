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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.NotificationService;
import com.tysanclan.site.projectewok.components.DateLabel;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.OtterSniperPanel;
import com.tysanclan.site.projectewok.entities.Notification;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class NotificationsPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private NotificationService notificationService;

	public NotificationsPage() {
		super("Notifications");

		add(new OtterSniperPanel("otterSniperPanel", 0));

		add(
				new ListView<Notification>("notifications", ModelMaker
						.wrap(notificationService
								.getNotificationsForUser(getUser()))) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<Notification> item) {
						Notification notification = item.getModelObject();
						item.add(new DateLabel("time", notification.getDate()));
						item.add(new Label("notification", notification
								.getMessage()));
						item.add(new IconLink.Builder("images/icons/cross.png",
								new DefaultClickResponder<Notification>(
										ModelMaker.wrap(notification)) {
									private static final long serialVersionUID = 1L;

									/**
									* @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
									*/
									@Override
									public void onClick() {
										notificationService
												.dismissNotification(getModelObject());
										setResponsePage(new NotificationsPage());
									}

								}).newInstance("dismiss"));
					}

				});
	}
}
