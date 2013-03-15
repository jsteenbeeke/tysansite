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
package com.tysanclan.site.projectewok.ws.mumble;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Jeroen Steenbeeke
 */
@XmlRootElement(name = "hash")
public class ServerStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Channel> channels;

	private List<MumbleUser> users;

	@XmlElementWrapper(name = "channels")
	public List<Channel> getChannels() {
		if (channels == null)
			channels = new LinkedList<Channel>();

		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	@XmlElementWrapper(name = "users")
	public List<MumbleUser> getUsers() {
		if (users == null)
			users = new LinkedList<MumbleUser>();

		return users;
	}

	public void setUsers(List<MumbleUser> users) {
		this.users = users;
	}

}
