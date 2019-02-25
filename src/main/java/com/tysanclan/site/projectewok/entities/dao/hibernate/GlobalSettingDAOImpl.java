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
package com.tysanclan.site.projectewok.entities.dao.hibernate;

import com.jeroensteenbeeke.hyperion.solstice.data.HibernateDAO;
import com.tysanclan.site.projectewok.entities.GlobalSetting;
import com.tysanclan.site.projectewok.entities.GlobalSetting.GlobalSettings;
import com.tysanclan.site.projectewok.entities.filter.GlobalSettingFilter;
import io.vavr.control.Option;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class GlobalSettingDAOImpl extends HibernateDAO<GlobalSetting, GlobalSettingFilter> implements
		com.tysanclan.site.projectewok.entities.dao.GlobalSettingDAO {
	@Override
	public Option<GlobalSetting> get(String id) {
		GlobalSettingFilter filter = new GlobalSettingFilter();
		filter.id().equalTo(id);

		return getUniqueByFilter(filter);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public GlobalSetting getGlobalSetting(GlobalSettings id) {
		GlobalSettingFilter filter = new GlobalSettingFilter();
		filter.id().equalTo(id.name());

		long count = countByFilter(filter);
		if (count == 0) {
			GlobalSetting setting = id.getNewDefaultGlobalSetting();
			save(setting);
			flush();

			return setting;

		} else if (count == 1) {
			return getUniqueByFilter(filter).getOrNull();
		} else {
			// Two entries?? WTF
			return findByFilter(filter).get();
		}
	}
}
