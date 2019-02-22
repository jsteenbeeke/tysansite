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
package com.tysanclan.site.projectewok.entities.dao.hibernate;

import com.jeroensteenbeeke.hyperion.solstice.data.HibernateDAO;
import com.tysanclan.site.projectewok.entities.JoinApplication;
import com.tysanclan.site.projectewok.entities.JoinVerdict;
import com.tysanclan.site.projectewok.entities.filter.JoinVerdictFilter;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class JoinVerdictDAOImpl extends HibernateDAO<JoinVerdict, JoinVerdictFilter> implements
		com.tysanclan.site.projectewok.entities.dao.JoinVerdictDAO {


	@Override
	@Transactional(propagation = Propagation.NESTED, readOnly = false)
	public void deleteForApplication(JoinApplication application) {
		JoinVerdictFilter filter = new JoinVerdictFilter();
		filter.application(application);

		List<JoinVerdict> list = findByFilter(filter).toJavaList();

		for (JoinVerdict v : list) {
			delete(v);
		}
	}
}
