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
package com.tysanclan.site.projectewok.entities.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.BattleNetChannel;

/**
 * @author Jeroen Steenbeeke
 */
@Component("channelDAO")
@Scope("request")
class BattleNetChannelDAOImpl extends EwokHibernateDAO<BattleNetChannel>
		implements
		com.tysanclan.site.projectewok.entities.dao.BattleNetChannelDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<BattleNetChannel> filter) {
		Criteria criteria = getSession().createCriteria(BattleNetChannel.class);

		// if (filter instanceof BattleNetChannelFilter) {
		// BattleNetChannelFilter cf = (BattleNetChannelFilter) filter;
		// }

		return criteria;
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.BattleNetChannelDAO#getPasswordByUID(java.lang.String)
	 */
	@Override
	public String getPasswordByUID(String identifier) {
		Criteria criteria = getSession().createCriteria(BattleNetChannel.class);

		criteria.add(Restrictions.eq("webServiceUserId", identifier));
		criteria.setProjection(Projections.property("webServicePassword"));

		return (String) criteria.uniqueResult();
	}

	@Override
	public BattleNetChannel getChannelByUID(String identifier) {
		Criteria criteria = getSession().createCriteria(BattleNetChannel.class);

		criteria.add(Restrictions.eq("webServiceUserId", identifier));

		return (BattleNetChannel) criteria.uniqueResult();
	}
}
