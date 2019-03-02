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

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = {
		@Index(columnList = "realm_id", name = "IDX_BattleNetChannel_Realm") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class BattleNetChannel extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BattleNetChannel")
	@SequenceGenerator(name = "BattleNetChannel", sequenceName = "SEQ_ID_BattleNetChannel")
	private Long id;

	@Column(nullable = false)
	private String channelName;

	@ManyToOne(fetch = FetchType.LAZY)
	private Realm realm;

	@Column(nullable = false, unique = true)
	private String webServiceUserId;

	@Column(nullable = false)
	private String webServicePassword;

	@Column(nullable = false)
	private Date lastUpdate;

	@OneToMany(mappedBy = "channel", fetch = FetchType.LAZY)
	@OrderBy("specialType desc, username asc")
	private List<BattleNetUserPresence> users;

	// $P$

	/**
	 * Creates a new BattleNetChannel object
	 */
	public BattleNetChannel() {
		// $H$
	}

	/**
	 * Returns the ID of this BattleNetChannel
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this BattleNetChannel
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the channelName
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * @param channelName
	 *            the channelName to set
	 */
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	/**
	 * @return the realm
	 */
	public Realm getRealm() {
		return realm;
	}

	/**
	 * @param realm
	 *            the realm to set
	 */
	public void setRealm(Realm realm) {
		this.realm = realm;
	}

	/**
	 * @return the webServiceUserId
	 */
	public String getWebServiceUserId() {
		return webServiceUserId;
	}

	/**
	 * @param webServiceUserId
	 *            the webServiceUserId to set
	 */
	public void setWebServiceUserId(String webServiceUserId) {
		this.webServiceUserId = webServiceUserId;
	}

	/**
	 * @return the webServicePassword
	 */
	public String getWebServicePassword() {
		return webServicePassword;
	}

	/**
	 * @param webServicePassword
	 *            the webServicePassword to set
	 */
	public void setWebServicePassword(String webServicePassword) {
		this.webServicePassword = webServicePassword;
	}

	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate
	 *            the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @return the users
	 */
	public List<BattleNetUserPresence> getUsers() {
		return users;
	}

	/**
	 * @param users
	 *            the users to set
	 */
	public void setUsers(List<BattleNetUserPresence> users) {
		this.users = users;
	}

	// $GS$
}
