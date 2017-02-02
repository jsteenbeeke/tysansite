package com.tysanclan.site.projectewok.entities.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.HibernateDAO;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.RestApplicationChallenge;
import com.tysanclan.site.projectewok.entities.dao.RestApplicationChallengeDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.RestApplicationChallengeFilter;

@Component
@Scope("request")
public class RestApplicationChallengeDAOImpl
		extends HibernateDAO<RestApplicationChallenge>
		implements RestApplicationChallengeDAO {
	@Override
	protected Criteria createCriteria(
			SearchFilter<RestApplicationChallenge> racf) {
		Criteria criteria = getSession()
				.createCriteria(RestApplicationChallenge.class);

		if (racf instanceof RestApplicationChallengeFilter) {
			RestApplicationChallengeFilter filter = (RestApplicationChallengeFilter) racf;

			if (filter.getApplication() != null) {
				criteria.add(Restrictions.eq("application",
						filter.getApplication()));
			}
			if (filter.getChallenge() != null) {
				criteria.add(Restrictions.eq("challengeString",
						filter.getChallenge()));
			}
			if (filter.getResponse() != null) {
				criteria.add(Restrictions.eq("expectedResponse",
						filter.getResponse()));
			}
			if (filter.getIssueDate() != null) {
				criteria.add(
						Restrictions.eq("issueDate", filter.getIssueDate()));
			}
			if (filter.getIssueDateBefore() != null) {
				criteria.add(Restrictions.lt("issueDate",
						filter.getIssueDateBefore()));
			}
			if (filter.getIssueDateAfter() != null) {
				criteria.add(Restrictions.gt("issueDate",
						filter.getIssueDateAfter()));
			}
		}

		return criteria;
	}
}
