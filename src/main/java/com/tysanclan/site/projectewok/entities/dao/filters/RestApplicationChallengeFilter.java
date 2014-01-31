package com.tysanclan.site.projectewok.entities.dao.filters;

import java.util.Date;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.AuthorizedRestApplication;
import com.tysanclan.site.projectewok.entities.RestApplicationChallenge;

public class RestApplicationChallengeFilter extends
		SearchFilter<RestApplicationChallenge> {
	private static final long serialVersionUID = 1L;

	private String challenge;

	private String response;

	private Date issueDate;

	private Date issueDateBefore;

	private Date issueDateAfter;

	private IModel<AuthorizedRestApplication> application = Model
			.of((AuthorizedRestApplication) null);

	public Date getIssueDateBefore() {
		return issueDateBefore;
	}

	public void setIssueDateBefore(Date issueDateBefore) {
		this.issueDateBefore = issueDateBefore;
	}

	public Date getIssueDateAfter() {
		return issueDateAfter;
	}

	public void setIssueDateAfter(Date issueDateAfter) {
		this.issueDateAfter = issueDateAfter;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getChallenge() {
		return challenge;
	}

	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public AuthorizedRestApplication getApplication() {
		return application.getObject();
	}

	public void setApplication(AuthorizedRestApplication application) {
		this.application = ModelMaker.wrap(application);
	}

	@Override
	public void detach() {
		super.detach();
		application.detach();
	}
}
