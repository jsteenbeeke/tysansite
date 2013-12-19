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

import org.hibernate.Criteria;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.jeroensteenbeeke.hyperion.util.HashUtil;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.SubscriptionPayment;
import com.tysanclan.site.projectewok.entities.dao.SubscriptionPaymentDAO;

@Component
@Scope("request")
class SubscriptionPaymentDAOImpl extends EwokHibernateDAO<SubscriptionPayment>
		implements SubscriptionPaymentDAO {
	@Override
	public String getConfirmationKey(SubscriptionPayment payment) {
		return HashUtil.sha1Hash(payment.getId()
				+ payment.getUser().getUsername());
	}

	@Override
	protected Criteria createCriteria(SearchFilter<SubscriptionPayment> filter) {
		Criteria criteria = getSession().createCriteria(
				SubscriptionPayment.class);

		return criteria;
	}
}
