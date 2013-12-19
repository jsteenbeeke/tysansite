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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Index;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

@Entity
public class TruthsayerComplaintVote extends BaseDomainObject {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TruthsayerComplaintVote")
	@SequenceGenerator(name = "TruthsayerComplaintVote", sequenceName = "SEQ_ID_TruthsayerComplaintVote", allocationSize = 1, initialValue = 1)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@Index(name = "IDX_TSCOMPLAINTVOTE_COMPLAINT")
	private TruthsayerComplaint complaint;

	@Column(nullable = false)
	private boolean inFavor;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@Index(name = "IDX_TSCOMPLAINTVOTE_CASTER")
	private User caster;

	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setComplaint(TruthsayerComplaint complaint) {
		this.complaint = complaint;
	}

	public TruthsayerComplaint getComplaint() {
		return complaint;
	}

	public void setInFavor(boolean inFavor) {
		this.inFavor = inFavor;
	}

	public boolean isInFavor() {
		return inFavor;
	}

	public User getCaster() {
		return caster;
	}

	public void setCaster(User caster) {
		this.caster = caster;
	}
}
