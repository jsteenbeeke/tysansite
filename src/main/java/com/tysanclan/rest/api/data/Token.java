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
package com.tysanclan.rest.api.data;

public class Token {
	private RestUser user;

	private String tokenString;

	private long validUntil;

	public Token(RestUser user, String tokenString, long validUntil) {
		super();
		this.user = user;
		this.tokenString = tokenString;
		this.validUntil = validUntil;
	}

	public Token() {

	}

	public RestUser getUser() {
		return user;
	}

	public void setUser(RestUser user) {
		this.user = user;
	}

	public String getTokenString() {
		return tokenString;
	}

	public void setTokenString(String tokenString) {
		this.tokenString = tokenString;
	}

	public long getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(long validUntil) {
		this.validUntil = validUntil;
	}

	public boolean hasExpired() {
		return System.currentTimeMillis() > validUntil;
	}
}