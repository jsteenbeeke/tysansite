package com.tysanclan.site.projectewok.entities.dao.hibernate;

import com.jeroensteenbeeke.hyperion.solstice.data.HibernateDAO;
import com.tysanclan.site.projectewok.entities.RestApplicationChallenge;
import com.tysanclan.site.projectewok.entities.dao.RestApplicationChallengeDAO;
import com.tysanclan.site.projectewok.entities.filter.RestApplicationChallengeFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class RestApplicationChallengeDAOImpl extends
		HibernateDAO<RestApplicationChallenge, RestApplicationChallengeFilter>
		implements RestApplicationChallengeDAO {

}
