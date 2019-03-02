/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.beans.impl;

import com.tysanclan.site.projectewok.entities.DisneyCharacter;
import com.tysanclan.site.projectewok.entities.DisneyHunt;
import com.tysanclan.site.projectewok.entities.OtterSighting;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.DisneyHuntDAO;
import com.tysanclan.site.projectewok.entities.dao.OtterSightingDAO;
import com.tysanclan.site.projectewok.entities.filter.DisneyHuntFilter;
import com.tysanclan.site.projectewok.entities.filter.OtterSightingFilter;
import io.vavr.collection.Array;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/**
 * @author Ties
 */
@Component
@Scope("request")
class HumorServiceImpl
		implements com.tysanclan.site.projectewok.beans.HumorService {
	@Autowired
	private OtterSightingDAO otterSightingDAO;

	@Autowired
	private DisneyHuntDAO disneyHuntDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public OtterSighting otterSighted(User user, Integer otterNumber) {
		OtterSightingFilter filter = new OtterSightingFilter();
		filter.user(user);
		filter.otterNumber(otterNumber);

		if (otterSightingDAO.countByFilter(filter) == 0) {
			OtterSighting sighting = new OtterSighting(user, otterNumber);

			otterSightingDAO.save(sighting);

			return sighting;
		}

		return otterSightingDAO.getUniqueByFilter(filter).getOrNull();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Option<DisneyCharacter> findDisneyCharacter(User user,
			Random random) {
		Array<DisneyCharacter> available = Array.of(DisneyCharacter.values());

		DisneyHuntFilter filter = new DisneyHuntFilter();
		filter.user(user);

		for (DisneyCharacter property : disneyHuntDAO
				.properties(filter.type(), filter)) {
			available = available.remove(property);
		}

		Array<DisneyCharacter> selectable = Array.empty();
		for (DisneyCharacter character : available) {
			for (int i = 0; i < character.getPrevalence(); i++) {
				selectable = selectable.append(character);
			}
		}

		return selectable.isEmpty() ?
				Option.none() :
				Option.of(selectable.toJavaArray(DisneyCharacter.class)[random
						.nextInt(selectable.length())]);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void catchDisneyCharacter(User user, DisneyCharacter character) {
		DisneyHuntFilter filter = new DisneyHuntFilter();
		filter.user(user);
		filter.type(character);

		if (disneyHuntDAO.countByFilter(filter) == 0) {
			DisneyHunt hunt = new DisneyHunt();
			hunt.setType(character);
			hunt.setUser(user);
			disneyHuntDAO.save(hunt);
		}
	}

	@Override
	public Option<String> getDisneyScore(User user) {
		DisneyHuntFilter filter = new DisneyHuntFilter();
		filter.user(user);

		long count = disneyHuntDAO.countByFilter(filter);

		if (count > 0) {
			return Option.of((100 * count) / DisneyCharacter.values().length)
					.map(p -> String.format("%d%%", p));
		}

		return Option.none();
	}
}
