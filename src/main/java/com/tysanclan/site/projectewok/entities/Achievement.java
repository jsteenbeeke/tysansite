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
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(columnList = "game_id", name = "IDX_Achievement_Game"),
		@Index(columnList = "group_id", name = "IDX_Achievement_Group") //
})
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Achievement implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Achievement")
	@SequenceGenerator(name = "Achievement", sequenceName="SEQ_ID_Achievement", allocationSize=1)
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_achievement", joinColumns = @JoinColumn(name = "achievement_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> achievedBy;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private Game game;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private Group group;

	@OneToOne(fetch = FetchType.LAZY, optional = false, mappedBy = "achievement")
	// @Index(name = "IDX_Achievement_Icon")
	private AchievementIcon icon;

	@Column(nullable = false)
	private String description;

	// $P$

	/**
	 * Creates a new Achievement object
	 */
	public Achievement() {
		this.achievedBy = new LinkedList<User>();
		// $H$
	}

	/**
	 * Returns the ID of this Achievement
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this Achievement
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Name of this Achievement
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the Name of this Achievement
	 * @param name The Name of this Achievement
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The AchievedBy of this Achievement
	 */
	public List<User> getAchievedBy() {
		return this.achievedBy;
	}

	/**
	 * Sets the AchievedBy of this Achievement
	 * @param achievedBy The AchievedBy of this Achievement
	 */
	public void setAchievedBy(List<User> achievedBy) {
		this.achievedBy = achievedBy;
	}

	public AchievementIcon getIcon() {
		return icon;
	}

	public void setIcon(AchievementIcon icon) {
		this.icon = icon;
	}

	/**
	 * @return The Description of this Achievement
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the Description of this Achievement
	 * @param description The Description of this Achievement
	 */
	public void setDescription(String description) {
		this.description = description;
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

	// $GS$
}
