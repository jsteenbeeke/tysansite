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
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_TRIAL_ACCUSED", columnList = "accused_id"), //
		@Index(name = "IDX_TRIAL_ACCUSER", columnList = "accuser_id"), //
		@Index(name = "IDX_TRIAL_JUDGE", columnList = "judge_id"), //
		@Index(name = "IDX_TRIAL_THREAD", columnList = "trialThread_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "forum")
public class Trial extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	public enum Verdict {
		INNOCENT, MINOR, MEDIUM, MAJOR;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Trial")
	@SequenceGenerator(name = "Trial", sequenceName = "SEQ_ID_Trial")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User accused;

	@ManyToOne(fetch = FetchType.LAZY)
	private User accuser;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private User judge;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "trial_regulations", joinColumns = @JoinColumn(name = "trial_id"), inverseJoinColumns = @JoinColumn(name = "regulation_id"))
	@OrderBy(value = "name asc")
	private List<Regulation> regulations;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	private ForumThread trialThread;

	@Enumerated(EnumType.STRING)
	private Verdict verdict;

	@Column
	@Lob

	private String motivation;

	@Column(nullable = true)
	private Boolean restrained;

	// $P$

	/**
	 * Creates a new Trial object
	 */
	public Trial() {
		// $H$
	}

	/**
	 * Returns the ID of this Trial
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this Trial
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Accused of this Trial
	 */
	public User getAccused() {
		return this.accused;
	}

	/**
	 * Sets the Accused of this Trial
	 *
	 * @param accused
	 *            The Accused of this Trial
	 */
	public void setAccused(User accused) {
		this.accused = accused;
	}

	/**
	 * @return The Judge of this Trial
	 */
	public User getJudge() {
		return this.judge;
	}

	/**
	 * Sets the Judge of this Trial
	 *
	 * @param judge
	 *            The Judge of this Trial
	 */
	public void setJudge(User judge) {
		this.judge = judge;
	}

	/**
	 * @return the regulations
	 */
	public List<Regulation> getRegulations() {
		return regulations;
	}

	/**
	 * @param regulations
	 *            the regulations to set
	 */
	public void setRegulations(List<Regulation> regulations) {
		this.regulations = regulations;
	}

	/**
	 * @return The TrialThread of this Trial
	 */
	public ForumThread getTrialThread() {
		return this.trialThread;
	}

	/**
	 * Sets the TrialThread of this Trial
	 *
	 * @param trialThread
	 *            The TrialThread of this Trial
	 */
	public void setTrialThread(ForumThread trialThread) {
		this.trialThread = trialThread;
	}

	/**
	 * @return the verdict
	 */
	public Verdict getVerdict() {
		return verdict;
	}

	/**
	 * @param verdict
	 *            the verdict to set
	 */
	public void setVerdict(Verdict verdict) {
		this.verdict = verdict;
	}

	/**
	 * @return the restrained
	 */
	public boolean isRestrained() {
		return restrained != null ? restrained : false;
	}

	/**
	 * @param restrained
	 *            the restrained to set
	 */
	public void setRestrained(boolean restrained) {
		this.restrained = restrained;
	}

	/**
	 * @return the accuser
	 */
	public User getAccuser() {
		return accuser;
	}

	/**
	 * @param accuser
	 *            the accuser to set
	 */
	public void setAccuser(User accuser) {
		this.accuser = accuser;
	}

	/**
	 * @return the motivation
	 */
	public String getMotivation() {
		return motivation;
	}

	/**
	 * @param motivation
	 *            the motivation to set
	 */
	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	// $GS$
}
