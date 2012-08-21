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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Jeroen Steenbeeke
 */
@XmlRootElement(name = "user")
public class MumbleUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private int secondsOnline;

	private int channel;

	private String name;

	private String state;

	private boolean deaf;

	private boolean mute;

	@XmlElement(name = "online")
	public int getSecondsOnline() {
		return secondsOnline;
	}

	public void setSecondsOnline(int secondsOnline) {
		this.secondsOnline = secondsOnline;
	}

	@XmlElement(name = "channel")
	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "state")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@XmlElement(name = "deaf", type = Boolean.class)
	public boolean isDeaf() {
		return deaf;
	}

	public void setDeaf(boolean deaf) {
		this.deaf = deaf;
	}

	@XmlElement(name = "mute", type = Boolean.class)
	public boolean isMute() {
		return mute;
	}

	public void setMute(boolean mute) {
		this.mute = mute;
	}

}
