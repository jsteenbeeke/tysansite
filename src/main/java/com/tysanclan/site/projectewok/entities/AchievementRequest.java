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
package com.tysanclan.site.projectewok.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.jeroensteenbeeke.hyperion.data.DomainObject;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class AchievementRequest implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AchievementRequest")
	@SequenceGenerator(name = "AchievementRequest", sequenceName = "SEQ_ID_AchievementRequest")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_AchievementRequest_achievement")
	private Achievement achievement;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_AchievementRequest_requestedBy")
	private User requestedBy;

	@Lob
	@Column
	private byte[] evidencePicture;

	@Lob
	@Column
	@Type(type = "org.hibernate.type.StringClobType")
	private String evidenceDescription;

	// $P$

	/**
	 * Creates a new AchievementRequest object
	 */
	public AchievementRequest() {
		// $H$
	}

	/**
	 * Returns the ID of this AchievementRequest
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this AchievementRequest
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Achievement of this AchievementRequest
	 */
	public Achievement getAchievement() {
		return this.achievement;
	}

	/**
	 * Sets the Achievement of this AchievementRequest
	 * @param achievement The Achievement of this AchievementRequest
	 */
	public void setAchievement(Achievement achievement) {
		this.achievement = achievement;
	}

	/**
	 * @return The RequestedBy of this AchievementRequest
	 */
	public User getRequestedBy() {
		return this.requestedBy;
	}

	/**
	 * Sets the RequestedBy of this AchievementRequest
	 * @param requestedBy The RequestedBy of this AchievementRequest
	 */
	public void setRequestedBy(User requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getEvidenceDescription() {
		return evidenceDescription;
	}

	public byte[] getEvidencePicture() {
		return evidencePicture;
	}

	public void setEvidenceDescription(String evidenceDescription) {
		this.evidenceDescription = evidenceDescription;
	}

	public void setEvidencePicture(byte[] evidencePicture) {
		this.evidencePicture = evidencePicture;
	}

	// $GS$
}
