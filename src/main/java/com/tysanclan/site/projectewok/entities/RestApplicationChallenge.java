package com.tysanclan.site.projectewok.entities;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(indexes = {
		@Index(columnList = "application_id", name = "IDX_RESTCHAL_APP") })
public class RestApplicationChallenge extends BaseDomainObject {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RestApplicationChallenge")
	@SequenceGenerator(name = "RestApplicationChallenge", sequenceName="SEQ_ID_RestApplicationChallenge", allocationSize=1)
	private Long id;

	@Column(nullable = false, unique = true)
	private String challengeString;

	@Column(nullable = false)
	private String expectedResponse;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private AuthorizedRestApplication application;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date issueDate;

	public RestApplicationChallenge() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChallengeString() {
		return challengeString;
	}

	public void setChallengeString(String challengeString) {
		this.challengeString = challengeString;
	}

	public String getExpectedResponse() {
		return expectedResponse;
	}

	public void setExpectedResponse(String expectedResponse) {
		this.expectedResponse = expectedResponse;
	}

	public AuthorizedRestApplication getApplication() {
		return application;
	}

	public void setApplication(AuthorizedRestApplication application) {
		this.application = application;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	@Override
	public final Serializable getDomainObjectId() {
		return getId();
	}
}
