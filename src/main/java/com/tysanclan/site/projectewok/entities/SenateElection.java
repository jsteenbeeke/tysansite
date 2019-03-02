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

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.util.MemberUtil;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@DiscriminatorValue("SenateElection")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class SenateElection extends Election {
	public static final long serialVersionUID = 1L;

	private static class SenateEligibility implements Eligibility {
		/**
		 * @see com.tysanclan.site.projectewok.entities.Election.Eligibility#isEligible(com.tysanclan.site.projectewok.entities.User)
		 */
		@Override
		public boolean isEligible(User user) {
			return MemberUtil.isMember(user) && !user.isRetired()
					&& user.getRank() != Rank.TRIAL
					&& user.getRank() != Rank.CHANCELLOR
					&& user.getRank() != Rank.JUNIOR_MEMBER;
		}
	}

	@ManyToMany(fetch = FetchType.LAZY)
	private Set<User> winners;

	@Column
	private int seats;

	// $P$

	/**
	 * Creates a new SenateElection object
	 */
	public SenateElection() {
		this.winners = new HashSet<User>();
		// $H$
	}

	/**
	 * @return The Winners of this SenateElection
	 */
	public Set<User> getWinners() {
		return this.winners;
	}

	/**
	 * Sets the Winners of this SenateElection
	 *
	 * @param winners
	 *            The Winners of this SenateElection
	 */
	public void setWinners(Set<User> winners) {
		this.winners = winners;
	}

	/**
	 * @return The Seats of this SenateElection
	 */
	public int getSeats() {
		return this.seats;
	}

	/**
	 * Sets the Seats of this SenateElection
	 *
	 * @param seats
	 *            The Seats of this SenateElection
	 */
	public void setSeats(int seats) {
		this.seats = seats;
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.Election#getEligibility()
	 */
	@Override
	public Eligibility getEligibility() {
		return new SenateEligibility();
	}

	// $GS$
}
