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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;

import com.jeroensteenbeeke.hyperion.data.DomainObject;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class AchievementIcon implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AchievementIcon")
	@SequenceGenerator(name = "AchievementIcon", sequenceName = "SEQ_ID_AchievementIcon")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_AchievementIcon_Creator")
	private User creator;

	@Column
	private Boolean approved;

	@Column
	private byte[] image;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@Index(name = "IDX_AchievementIcon_Proposal")
	private AchievementProposal proposal;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@Index(name = "IDX_AchievementIcon_Achievement")
	private Achievement achievement;

	@Column(nullable = true)
	private Boolean creatorOnly;

	@Column(nullable = true)
	private String purpose;

	// $P$

	/**
	 * Creates a new AchievementIcon object
	 */
	public AchievementIcon() {
		// $H$
	}

	/**
	 * Returns the ID of this AchievementIcon
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this AchievementIcon
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Creator of this AchievementIcon
	 */
	public User getCreator() {
		return this.creator;
	}

	/**
	 * Sets the Creator of this AchievementIcon
	 * @param creator The Creator of this AchievementIcon
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * @return The Approved of this AchievementIcon
	 */
	public Boolean getApproved() {
		return this.approved;
	}

	/**
	 * Sets the Approved of this AchievementIcon
	 * @param approved The Approved of this AchievementIcon
	 */
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	/**
	 * @return The Image of this AchievementIcon
	 */
	public byte[] getImage() {
		return this.image;
	}

	/**
	 * Sets the Image of this AchievementIcon
	 * @param image The Image of this AchievementIcon
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}

	public Achievement getAchievement() {
		return achievement;
	}

	public AchievementProposal getProposal() {
		return proposal;
	}

	public void setAchievement(Achievement achievement) {
		this.achievement = achievement;
	}

	public void setProposal(AchievementProposal proposal) {
		this.proposal = proposal;
	}

	public Boolean getCreatorOnly() {
		return creatorOnly;
	}

	public void setCreatorOnly(Boolean creatorOnly) {
		this.creatorOnly = creatorOnly;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	// $GS$
}
