package com.tysanclan.site.projectewok.entities.dao.hibernate;

import com.jeroensteenbeeke.hyperion.solstice.data.HibernateDAO;
import com.tysanclan.site.projectewok.entities.DisneyHunt;
import com.tysanclan.site.projectewok.entities.dao.DisneyHuntDAO;
import com.tysanclan.site.projectewok.entities.filter.DisneyHuntFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
class DisneyHuntDAOImpl extends HibernateDAO<DisneyHunt, DisneyHuntFilter>
		implements DisneyHuntDAO {
}
