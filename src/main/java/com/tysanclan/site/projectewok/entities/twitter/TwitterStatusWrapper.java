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
package com.tysanclan.site.projectewok.entities.twitter;

import java.io.Serializable;
import java.util.Date;

import twitter4j.Status;

/**
 * @author Jeroen Steenbeeke
 */
public class TwitterStatusWrapper implements ITweet,
        Serializable {
	private static final long serialVersionUID = 1L;

	private Status status;

	public TwitterStatusWrapper(Status status) {
		this.status = status;
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.twitter.ITweet#getContents()
	 */
	@Override
	public String getContents() {
		return status.getText();
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.twitter.ITweet#getName()
	 */
	@Override
	public String getName() {
		return status.getUser().getName();
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.twitter.ITweet#getPosted()
	 */
	@Override
	public Date getPosted() {
		return status.getCreatedAt();
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.twitter.ITweet#getScreenName()
	 */
	@Override
	public String getScreenName() {
		return status.getUser().getScreenName();
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.twitter.ITweet#getSource()
	 */
	@Override
	public String getSource() {
		return status.getSource();
	}

}
