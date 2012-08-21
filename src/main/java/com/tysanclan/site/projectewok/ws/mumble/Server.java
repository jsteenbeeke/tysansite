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
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Jeroen
 */
@XmlRootElement(name = "server")
public class Server implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String murmurId;
	private String name;
	private String status;
	private String motd;
	private String host;
	private int port;
	private int slots;
	private String murmurVersion;
	private Date createdAt;
	private Date updatedAt;
	private Location location;

	@XmlElement(name = "id")
	public long getId() {
		return id;
	}

	@XmlElement(name = "murmur_id")
	public String getMurmurId() {
		return murmurId;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	@XmlElement(name = "status")
	public String getStatus() {
		return status;
	}

	@XmlElement(name = "motd")
	public String getMotd() {
		return motd;
	}

	@XmlElement(name = "host")
	public String getHost() {
		return host;
	}

	@XmlElement(name = "port")
	public int getPort() {
		return port;
	}

	@XmlElement(name = "slots")
	public int getSlots() {
		return slots;
	}

	@XmlElement(name = "murmur_version")
	public String getMurmurVersion() {
		return murmurVersion;
	}

	@XmlElement(name = "created_at")
	public Date getCreatedAt() {
		return createdAt;
	}

	@XmlElement(name = "updated_at")
	public Date getUpdatedAt() {
		return updatedAt;
	}

	@XmlElement(name = "location")
	public Location getLocation() {
		return location;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMurmurId(String murmurId) {
		this.murmurId = murmurId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMotd(String motd) {
		this.motd = motd;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setSlots(int slots) {
		this.slots = slots;
	}

	public void setMurmurVersion(String murmurVersion) {
		this.murmurVersion = murmurVersion;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
