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
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_JOINAPPLICATION_APPLICANT", columnList = "applicant_id"),
		//
		@Index(name = "IDX_JOINAPPLICATION_MENTOR", columnList = "mentor_id"),
		//
		@Index(name = "IDX_JOINAPPLICATION_JOINTHREAD", columnList = "joinThread_id"),
		//
		@Index(name = "IDX_JOINAPPLICATION_PRIMARYREALM", columnList = "primaryRealm_id"),
		//
		@Index(name = "IDX_JOINAPPLICATION_PRIMARYGAME", columnList = "primaryGame_id")

})
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class JoinApplication extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JoinApplication")
	@SequenceGenerator(name = "JoinApplication", sequenceName="SEQ_ID_JoinApplication", allocationSize=1)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	private User applicant;

	@ManyToOne(fetch = FetchType.LAZY)
	private User mentor;

	@OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<JoinVerdict> verdicts;

	@OneToOne(fetch = FetchType.LAZY)
	private ForumThread joinThread;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private Realm primaryRealm;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private Game primaryGame;

	@Column
	private Date startDate;

	// $P$

	/**
	 * Creates a new JoinApplication object
	 */
	public JoinApplication() {
		this.verdicts = new HashSet<JoinVerdict>();
		// $H$
	}

	/**
	 * Returns the ID of this JoinApplication
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this JoinApplication
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Applicant of this JoinApplication
	 */
	public User getApplicant() {
		return this.applicant;
	}

	/**
	 * Sets the Applicant of this JoinApplication
	 *
	 * @param applicant
	 *            The Applicant of this JoinApplication
	 */
	public void setApplicant(User applicant) {
		this.applicant = applicant;
	}

	/**
	 * @return The Mentor of this JoinApplication
	 */
	public User getMentor() {
		return this.mentor;
	}

	/**
	 * Sets the Mentor of this JoinApplication
	 *
	 * @param mentor
	 *            The Mentor of this JoinApplication
	 */
	public void setMentor(User mentor) {
		this.mentor = mentor;
	}

	/**
	 * @return The Verdicts of this JoinApplication
	 */
	public Set<JoinVerdict> getVerdicts() {
		return this.verdicts;
	}

	/**
	 * Sets the Verdicts of this JoinApplication
	 *
	 * @param verdicts
	 *            The Verdicts of this JoinApplication
	 */
	public void setVerdicts(Set<JoinVerdict> verdicts) {
		this.verdicts = verdicts;
	}

	/**
	 * @return The JoinThread of this JoinApplication
	 */
	public ForumThread getJoinThread() {
		return this.joinThread;
	}

	/**
	 * Sets the JoinThread of this JoinApplication
	 *
	 * @param joinThread
	 *            The JoinThread of this JoinApplication
	 */
	public void setJoinThread(ForumThread joinThread) {
		this.joinThread = joinThread;
	}

	/**
	 * @return The StartDate of this JoinApplication
	 */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
	 * Sets the StartDate of this JoinApplication
	 *
	 * @param startDate
	 *            The StartDate of this JoinApplication
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Game getPrimaryGame() {
		return primaryGame;
	}

	public void setPrimaryGame(Game primaryGame) {
		this.primaryGame = primaryGame;
	}

	public Realm getPrimaryRealm() {
		return primaryRealm;
	}

	public void setPrimaryRealm(Realm primaryRealm) {
		this.primaryRealm = primaryRealm;
	}

	// $GS$
}
