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
package com.tysanclan.site.projectewok.beans;

import com.tysanclan.site.projectewok.entities.DisneyCharacter;
import com.tysanclan.site.projectewok.entities.OtterSighting;
import com.tysanclan.site.projectewok.entities.User;
import io.vavr.control.Option;

import java.util.Random;

/**
 * @author Ties
 */
public interface HumorService {
	OtterSighting otterSighted(User user, Integer otterNumber);

	Option<DisneyCharacter> findDisneyCharacter(User user, Random random);

	void catchDisneyCharacter(User user, DisneyCharacter character);

	Option<String> getDisneyScore(User user);
}
