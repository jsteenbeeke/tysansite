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

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.GameAccount;

public class GameAccountFilter extends SearchFilter<GameAccount> {
	private static final long serialVersionUID = 1L;

	private String accountName;
	private String channelWebServiceUID;
	private Boolean botLinked;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the channelWebServiceUID
	 */
	public String getChannelWebServiceUID() {
		return channelWebServiceUID;
	}

	/**
	 * @param channelWebServiceUID
	 *            the channelWebServiceUID to set
	 */
	public void setChannelWebServiceUID(String channelWebServiceUID) {
		this.channelWebServiceUID = channelWebServiceUID;
	}

	public Boolean getBotLinked() {
		return botLinked;
	}

	public void setBotLinked(Boolean botLinked) {
		this.botLinked = botLinked;
	}

}
