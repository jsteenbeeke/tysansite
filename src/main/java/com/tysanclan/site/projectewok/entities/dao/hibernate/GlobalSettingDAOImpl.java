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
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.GlobalSetting;
import com.tysanclan.site.projectewok.entities.GlobalSetting.GlobalSettings;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class GlobalSettingDAOImpl extends EwokHibernateDAO<GlobalSetting> implements
		com.tysanclan.site.projectewok.entities.dao.GlobalSettingDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<GlobalSetting> filter) {
		Criteria criteria = getSession().createCriteria(GlobalSetting.class);

		// if (filter instanceof GlobalSettingFilter) {
		// GlobalSettingFilter cf = (GlobalSettingFilter) filter;
		// }

		return criteria;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public GlobalSetting getGlobalSetting(GlobalSettings id) {
		Criteria criteria = getSession().createCriteria(GlobalSetting.class);
		criteria.add(Restrictions.idEq(id.toString()));
		GlobalSetting setting = unique(criteria);
		if (setting == null) {
			setting = id.getNewDefaultGlobalSetting();
			save(setting);
		}
		return setting;
	}
}
