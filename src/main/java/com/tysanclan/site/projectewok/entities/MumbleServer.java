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
package com.tysanclan.site.projectewok.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;

import com.jeroensteenbeeke.hyperion.data.DomainObject;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class MumbleServer implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MumbleServer")
	@SequenceGenerator(name = "MumbleServer", sequenceName = "SEQ_ID_MumbleServer")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String url;

	@Column(nullable = false)
	private int serverID;

	@Column(nullable = false)
	private String apiToken;

	@Column(nullable = false)
	private String apiSecret;

	// $P$

	/**
	 * Creates a new MumbleServer object
	 */
	public MumbleServer() {
		// $H$
	}

	/**
	 * Returns the ID of this MumbleServer
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this MumbleServer
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Name of this MumbleServer
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the Name of this MumbleServer
	 * @param name The Name of this MumbleServer
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The Password of this MumbleServer
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Sets the Password of this MumbleServer
	 * @param password The Password of this MumbleServer
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return The Url of this MumbleServer
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Sets the Url of this MumbleServer
	 * @param url The Url of this MumbleServer
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return The ServerID of this MumbleServer
	 */
	public int getServerID() {
		return this.serverID;
	}

	/**
	 * Sets the ServerID of this MumbleServer
	 * @param serverID The ServerID of this MumbleServer
	 */
	public void setServerID(int serverID) {
		this.serverID = serverID;
	}

	/**
	 * @return The ApiToken of this MumbleServer
	 */
	public String getApiToken() {
		return this.apiToken;
	}

	/**
	 * Sets the ApiToken of this MumbleServer
	 * @param apiToken The ApiToken of this MumbleServer
	 */
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	/**
	 * @return The ApiSecret of this MumbleServer
	 */
	public String getApiSecret() {
		return this.apiSecret;
	}

	/**
	 * Sets the ApiSecret of this MumbleServer
	 * @param apiSecret The ApiSecret of this MumbleServer
	 */
	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	// $GS$
}
