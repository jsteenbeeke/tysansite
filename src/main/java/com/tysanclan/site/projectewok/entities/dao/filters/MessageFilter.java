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
package com.tysanclan.site.projectewok.entities.dao.filters;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.Message;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class MessageFilter extends SearchFilter<Message> {
	private static final long serialVersionUID = 1L;

	private IModel<User> sender = new Model<User>();
	private IModel<User> receiver = new Model<User>();
	private Boolean messageRead;

	/**
	 * @return the sender
	 */
	public User getSender() {
		return sender.getObject();
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(User sender) {
		this.sender = ModelMaker.wrap(sender);
	}

	/**
	 * @return the receiver
	 */
	public User getReceiver() {
		return receiver.getObject();
	}

	/**
	 * @param receiver
	 *            the receiver to set
	 */
	public void setReceiver(User receiver) {
		this.receiver = ModelMaker.wrap(receiver);
	}

	/**
	 * @param read
	 *            Whether or not the message has been read
	 */
	public void setMessageRead(Boolean read) {
		this.messageRead = read;
	}

	/**
	 * @return the messageRead
	 */
	public Boolean getMessageRead() {
		return messageRead;
	}

	@Override
	public void detach() {
		super.detach();
		receiver.detach();
		sender.detach();

	}
}
