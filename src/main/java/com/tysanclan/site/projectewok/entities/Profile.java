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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Type;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
@Index(name = "IDX_PROFILE_USER", columnList = "user_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Profile extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Profile")
	@SequenceGenerator(name = "Profile", sequenceName = "SEQ_ID_Profile")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	private User user;

	@Column
	private String realName;

	@Column
	private Date birthDate;

	@Column
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String privateDescription;

	@Column
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String publicDescription;

	@Column
	private String instantMessengerAddress;

	@Column
	private boolean instantMessengerPublic;

	@Column
	private String photoURL;

	@Column
	private boolean photoPublic;

	@Column
	private String twitterUID;

	@Column
	private Date lastTwitterUpdate;

	// $P$

	/**
	 * Creates a new Profile object
	 */
	public Profile() {
		// $H$
	}

	/**
	 * Returns the ID of this Profile
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this Profile
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The User of this Profile
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the User of this Profile
	 * 
	 * @param user
	 *            The User of this Profile
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return The RealName of this Profile
	 */
	public String getRealName() {
		return this.realName;
	}

	/**
	 * Sets the RealName of this Profile
	 * 
	 * @param realName
	 *            The RealName of this Profile
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @return The BirthDate of this Profile
	 */
	public Date getBirthDate() {
		return this.birthDate;
	}

	/**
	 * Sets the BirthDate of this Profile
	 * 
	 * @param birthDate
	 *            The BirthDate of this Profile
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return The PrivateDescription of this Profile
	 */
	public String getPrivateDescription() {
		return this.privateDescription;
	}

	/**
	 * Sets the PrivateDescription of this Profile
	 * 
	 * @param privateDescription
	 *            The PrivateDescription of this Profile
	 */
	public void setPrivateDescription(String privateDescription) {
		this.privateDescription = privateDescription;
	}

	/**
	 * @return The PublicDescription of this Profile
	 */
	public String getPublicDescription() {
		return this.publicDescription;
	}

	/**
	 * Sets the PublicDescription of this Profile
	 * 
	 * @param publicDescription
	 *            The PublicDescription of this Profile
	 */
	public void setPublicDescription(String publicDescription) {
		this.publicDescription = publicDescription;
	}

	/**
	 * @return The PhotoURL of this Profile
	 */
	public String getPhotoURL() {
		return this.photoURL;
	}

	/**
	 * Sets the PhotoURL of this Profile
	 * 
	 * @param photoURL
	 *            The PhotoURL of this Profile
	 */
	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	/**
	 * @return The PhotoPublic of this Profile
	 */
	public boolean isPhotoPublic() {
		return this.photoPublic;
	}

	/**
	 * Sets the PhotoPublic of this Profile
	 * 
	 * @param photoPublic
	 *            The PhotoPublic of this Profile
	 */
	public void setPhotoPublic(boolean photoPublic) {
		this.photoPublic = photoPublic;
	}

	/**
	 * @return the instantMessengerAddress
	 */
	public String getInstantMessengerAddress() {
		return instantMessengerAddress;
	}

	/**
	 * @param instantMessengerAddress
	 *            the instantMessengerAddress to set
	 */
	public void setInstantMessengerAddress(String instantMessengerAddress) {
		this.instantMessengerAddress = instantMessengerAddress;
	}

	/**
	 * @return the instantMessengerPublic
	 */
	public boolean isInstantMessengerPublic() {
		return instantMessengerPublic;
	}

	/**
	 * @param instantMessengerPublic
	 *            the instantMessengerPublic to set
	 */
	public void setInstantMessengerPublic(boolean instantMessengerPublic) {
		this.instantMessengerPublic = instantMessengerPublic;
	}

	/**
	 * @return the twitterUID
	 */
	public String getTwitterUID() {
		return twitterUID;
	}

	/**
	 * @param twitterUID
	 *            the twitterUID to set
	 */
	public void setTwitterUID(String twitterUID) {
		this.twitterUID = twitterUID;
	}

	/**
	 * @return the lastTwitterUpdate
	 */
	public Date getLastTwitterUpdate() {
		return lastTwitterUpdate;
	}

	/**
	 * @param lastTwitterUpdate
	 *            the lastTwitterUpdate to set
	 */
	public void setLastTwitterUpdate(Date lastTwitterUpdate) {
		this.lastTwitterUpdate = lastTwitterUpdate;
	}

	// $GS$
}
