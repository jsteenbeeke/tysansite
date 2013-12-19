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
package com.tysanclan.site.projectewok.beans.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Jeroen Steenbeeke
 */
public class PetitionRequirementTest {
	@Test
	public void testPetitionValues() {
		assertEquals(9, GameServiceImpl.petitionFormula(150));
		assertEquals(9, GameServiceImpl.petitionFormula(75));
		assertEquals(8, GameServiceImpl.petitionFormula(74));
		assertEquals(8, GameServiceImpl.petitionFormula(65));
		assertEquals(7, GameServiceImpl.petitionFormula(64));
		assertEquals(7, GameServiceImpl.petitionFormula(55));
		assertEquals(6, GameServiceImpl.petitionFormula(54));
		assertEquals(6, GameServiceImpl.petitionFormula(45));
		assertEquals(5, GameServiceImpl.petitionFormula(44));
		assertEquals(5, GameServiceImpl.petitionFormula(35));
		assertEquals(4, GameServiceImpl.petitionFormula(34));
		assertEquals(4, GameServiceImpl.petitionFormula(25));
		assertEquals(4, GameServiceImpl.petitionFormula(24));
		assertEquals(4, GameServiceImpl.petitionFormula(2));

	}

}
