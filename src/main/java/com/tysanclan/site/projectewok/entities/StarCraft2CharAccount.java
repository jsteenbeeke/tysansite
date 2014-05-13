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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@DiscriminatorValue("StarCraft2CharAccount")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class StarCraft2CharAccount extends GameAccount {
	public static final long serialVersionUID = 1L;

	@Column
	private int characterCode;

	// $P$

	/** 
	 * Creates a new StarCraft2CharAccount object
	 */
	public StarCraft2CharAccount() {
		// $H$
	}

	/**
	 * @return The CharacterCode of this StarCraft2CharAccount
	 */
	public int getCharacterCode() {
		return this.characterCode;
	}

	/**
	 * Sets the CharacterCode of this StarCraft2CharAccount
	 * @param characterCode The CharacterCode of this StarCraft2CharAccount
	 */
	public void setCharacterCode(int characterCode) {
		this.characterCode = characterCode;
	}

	@Override
	protected String render() {
		return getName() + " (" + getCharacterCode() + ")";
	}

	@Override
	public boolean isValid() {
		for (char c : getName().toCharArray()) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}

		if (getCharacterCode() <= 0 || getCharacterCode() >= 1000)
			return false;

		return true;
	}

	@Override
	public AccountType getType() {
		return AccountType.STARCRAFT2;
	}

	// $GS$
}
