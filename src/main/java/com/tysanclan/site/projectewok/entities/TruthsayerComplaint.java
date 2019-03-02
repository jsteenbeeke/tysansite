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

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(indexes = { //
		@Index(name = "IDX_TSCOMPLAINT_TS", columnList = "truthsayer_id"),
		@Index(name = "IDX_TSCOMPLAINT_COMPLAINER", columnList = "complainer_id") })
public class TruthsayerComplaint extends BaseDomainObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TruthsayerComplaint")
	@SequenceGenerator(name = "TruthsayerComplaint", sequenceName = "SEQ_ID_TruthsayerComplaint", allocationSize = 1, initialValue = 1)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User truthsayer;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User complainer;

	@Column(nullable = false)
	@Lob

	private String complaint;

	@Column(nullable = false)
	private Date start;

	@Column(nullable = false)
	private boolean mediated;

	@Column(nullable = false)
	private boolean observed;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "complaint", cascade = CascadeType.ALL)
	private List<TruthsayerComplaintVote> votes;

	public TruthsayerComplaint() {
		this.mediated = false;
	}

	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getComplainer() {
		return complainer;
	}

	public void setComplainer(User complainer) {
		this.complainer = complainer;
	}

	public String getComplaint() {
		return complaint;
	}

	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}

	public User getTruthsayer() {
		return truthsayer;
	}

	public void setTruthsayer(User truthsayer) {
		this.truthsayer = truthsayer;
	}

	public List<TruthsayerComplaintVote> getVotes() {
		if (votes == null)
			votes = new ArrayList<TruthsayerComplaintVote>(0);

		return votes;
	}

	public void setVotes(List<TruthsayerComplaintVote> votes) {
		this.votes = votes;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public boolean isMediated() {
		return mediated;
	}

	public void setMediated(boolean mediated) {
		this.mediated = mediated;
	}

	public boolean isObserved() {
		return observed;
	}

	public void setObserved(boolean observed) {
		this.observed = observed;
	}

	public boolean hasVoted(User user) {
		for (TruthsayerComplaintVote vote : getVotes()) {
			if (vote.getCaster().equals(user)) {
				return true;
			}
		}

		return false;
	}
}
