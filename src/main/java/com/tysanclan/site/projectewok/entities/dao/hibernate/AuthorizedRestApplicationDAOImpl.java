package com.tysanclan.site.projectewok.entities.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.HibernateDAO;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.AuthorizedRestApplication;
import com.tysanclan.site.projectewok.entities.dao.AuthorizedRestApplicationDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.AuthorizedRestApplicationFilter;

@Component
@Scope("request")
class AuthorizedRestApplicationDAOImpl extends
		HibernateDAO<AuthorizedRestApplication> implements
		AuthorizedRestApplicationDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<AuthorizedRestApplication> sf) {
		Criteria criteria = getSession().createCriteria(
				AuthorizedRestApplication.class);

		if (sf instanceof AuthorizedRestApplicationFilter) {
			AuthorizedRestApplicationFilter filter = (AuthorizedRestApplicationFilter) sf;

			if (filter.getActive() != null) {
				criteria.add(Restrictions.eq("active", filter.getActive()));
			}
			if (filter.getName() != null) {
				criteria.add(Restrictions.eq("name", filter.getName()));
			}
			if (filter.getClientId() != null) {
				criteria.add(Restrictions.eq("clientId", filter.getClientId()));
			}
			if (filter.getClientSecret() != null) {
				criteria.add(Restrictions.eq("clientSecret",
						filter.getClientSecret()));
			}
		}

		return criteria;
	}
}
