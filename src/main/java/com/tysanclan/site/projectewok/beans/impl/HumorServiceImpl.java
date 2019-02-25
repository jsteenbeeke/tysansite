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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tysanclan.site.projectewok.entities.OtterSighting;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.OtterSightingDAO;
import com.tysanclan.site.projectewok.entities.filter.OtterSightingFilter;

/**
 * @author Ties
 */
@Component
@Scope("request")
class HumorServiceImpl implements
		com.tysanclan.site.projectewok.beans.HumorService {
	@Autowired
	private OtterSightingDAO otterSightingDAO;

	public void setOtterSightingDAO(OtterSightingDAO otterSightingDAO) {
		this.otterSightingDAO = otterSightingDAO;
	}

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
}
