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

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_BattleNetUserPresence_Channel", columnList = "channel_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class BattleNetUserPresence implements DomainObject {
	public static final long serialVersionUID = 1L;

	// Constants courtesy of JavaOP
	public static final int USER_BLIZZREP = 0x01;
	public static final int USER_CHANNELOP = 0x02;
	public static final int USER_SPEAKER = 0x04;
	public static final int USER_ADMIN = 0x08;
	public static final int USER_NOUDP = 0x10;
	public static final int USER_SQUELCHED = 0x20;
	public static final int USER_GUEST = 0x40;

	private static final String[] KNOWN_CLIENTS = { "LTRD", "VD2D", "PX2D",
			"PXES", "RATS", "NB2W", "PX3W", "3RAW" };

	public static enum SpecialType {
		SERVER_ADMIN("bnet-battlenet.gif", USER_ADMIN), BLIZZREP(
				"bnet-blizzard.gif", USER_BLIZZREP), OPERATOR(
				"bnet-channelops.gif", USER_CHANNELOP), SPEAKER(
				"bnet-speaker.gif", USER_SPEAKER), SQUELCHED("bnet-squelch.gif",
				USER_SQUELCHED), NONE(null, 0x0);

		private final String image;
		private final int requiredFlag;

		private SpecialType(String image, int requiredFlag) {
			this.image = image;
			this.requiredFlag = requiredFlag;
		}

		/**
		 * @return the image
		 */
		public String getImage() {
			return image;
		}

		public static SpecialType get(int flags) {
			for (SpecialType type : values()) {
				if ((type.requiredFlag & flags) == type.requiredFlag) {
					return type;
				}
			}

			return NONE;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BattleNetUserPresence")
	@SequenceGenerator(name = "BattleNetUserPresence", sequenceName="SEQ_ID_BattleNetUserPresence", allocationSize=1)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private BattleNetChannel channel;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SpecialType specialType;

	@Column(nullable = false)
	private String client;

	@Column(nullable = false)
	private Date lastUpdate;

	// $P$

	/**
	 * Creates a new BattleNetUserPresence object
	 */
	public BattleNetUserPresence() {
		// $H$
	}

	/**
	 * Returns the ID of this BattleNetUserPresence
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this BattleNetUserPresence
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the channel
	 */
	public BattleNetChannel getChannel() {
		return channel;
	}

	/**
	 * @param channel
	 *            the channel to set
	 */
	public void setChannel(BattleNetChannel channel) {
		this.channel = channel;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the specialType
	 */
	public SpecialType getSpecialType() {
		return specialType;
	}

	/**
	 * @param specialType
	 *            the specialType to set
	 */
	public void setSpecialType(SpecialType specialType) {
		this.specialType = specialType;
	}

	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(String client) {
		this.client = client;
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

	public static boolean isKnownClient(String client2) {
		return Arrays.asList(KNOWN_CLIENTS).contains(client2);
	}

	// $GS$
}
