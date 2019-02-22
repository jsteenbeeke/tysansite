package com.tysanclan.site.projectewok.entities.dao.hibernate;

import com.jeroensteenbeeke.hyperion.solstice.data.HibernateDAO;
import com.tysanclan.site.projectewok.entities.AuthorizedRestApplication;
import com.tysanclan.site.projectewok.entities.dao.AuthorizedRestApplicationDAO;
import com.tysanclan.site.projectewok.entities.filter.AuthorizedRestApplicationFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
class AuthorizedRestApplicationDAOImpl extends
		HibernateDAO<AuthorizedRestApplication, AuthorizedRestApplicationFilter> implements
		AuthorizedRestApplicationDAO {

}
