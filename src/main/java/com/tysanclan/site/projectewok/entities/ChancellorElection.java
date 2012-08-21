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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;

import com.tysanclan.site.projectewok.util.MemberUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@DiscriminatorValue("ChancellorElection")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class ChancellorElection extends Election {
	public static final long serialVersionUID = 1L;

	private static class ChancellorEligibility implements Eligibility {
		/**
		 * @see com.tysanclan.site.projectewok.entities.Election.Eligibility#isEligible(com.tysanclan.site.projectewok.entities.User)
		 */
		@Override
		public boolean isEligible(User user) {
			return MemberUtil.isMember(user) && !user.isRetired()
					&& user.getRank() != Rank.TRIAL
					&& user.getRank() != Rank.JUNIOR_MEMBER;
		}

	}

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_ChancellorElection_Winner")
	private User winner;

	// $P$

	/**
	 * Creates a new ChancellorElection object
	 */
	public ChancellorElection() {
		// $H$
	}

	/**
	 * @return The Winner of this ChancellorElection
	 */
	public User getWinner() {
		return this.winner;
	}

	/**
	 * Sets the Winner of this ChancellorElection
	 * 
	 * @param winner
	 *            The Winner of this ChancellorElection
	 */
	public void setWinner(User winner) {
		this.winner = winner;
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.Election#getEligibility()
	 */
	@Override
	public Eligibility getEligibility() {
		return new ChancellorEligibility();
	}

	// $GS$
}
