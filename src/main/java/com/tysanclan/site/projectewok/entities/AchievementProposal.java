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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(columnList = "suggestor_id", name = "IDX_AchievementProposal_suggestor"),
		//
		@Index(columnList = "game_id", name = "IDX_AchievementProposal_Game"),
		//
		@Index(columnList = "group_id", name = "IDX_AchievementProposal_Group")
		//
})
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class AchievementProposal implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AchievementProposal")
	@SequenceGenerator(name = "AchievementProposal", sequenceName = "SEQ_ID_AchievementProposal")
	private Long id;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "proposal", cascade = CascadeType.ALL)
	private List<AchievementProposalVote> approvedBy;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "proposal")
	// @Index(name = "IDX_AchievementProposal_icon")
	private AchievementIcon icon;

	@Column
	private String name;

	@Column
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	private User suggestor;

	@ManyToOne(fetch = FetchType.LAZY)
	private Game game;

	@ManyToOne(fetch = FetchType.LAZY)
	private Group group;

	@Column
	private Date startDate;

	@Column(nullable = false)
	private boolean truthsayerReviewed;

	@Column(nullable = true)
	private Boolean chancellorVeto;

	// $P$

	/**
	 * Creates a new AchievementProposal object
	 */
	public AchievementProposal() {
		this.approvedBy = new LinkedList<AchievementProposalVote>();
		// $H$
	}

	/**
	 * Returns the ID of this AchievementProposal
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this AchievementProposal
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The ApprovedBy of this AchievementProposal
	 */
	public List<AchievementProposalVote> getApprovedBy() {
		return this.approvedBy;
	}

	/**
	 * Sets the ApprovedBy of this AchievementProposal
	 * @param approvedBy The ApprovedBy of this AchievementProposal
	 */
	public void setApprovedBy(List<AchievementProposalVote> approvedBy) {
		this.approvedBy = approvedBy;
	}

	public void setIcon(AchievementIcon icon) {
		this.icon = icon;
	}

	public AchievementIcon getIcon() {
		return icon;
	}

	/**
	 * @return The Name of this AchievementProposal
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the Name of this AchievementProposal
	 * @param name The Name of this AchievementProposal
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The Suggestor of this AchievementProposal
	 */
	public User getSuggestor() {
		return this.suggestor;
	}

	/**
	 * Sets the Suggestor of this AchievementProposal
	 * @param suggestor The Suggestor of this AchievementProposal
	 */
	public void setSuggestor(User suggestor) {
		this.suggestor = suggestor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public boolean isTruthsayerReviewed() {
		return truthsayerReviewed;
	}

	public void setTruthsayerReviewed(boolean truthsayerReviewed) {
		this.truthsayerReviewed = truthsayerReviewed;
	}

	public Boolean getChancellorVeto() {
		return chancellorVeto;
	}

	public void setChancellorVeto(Boolean chancellorVeto) {
		this.chancellorVeto = chancellorVeto;
	}

	// $GS$
}
