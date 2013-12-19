package com.tysanclan.site.projectewok.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;

import com.google.common.base.Joiner;
import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.jeroensteenbeeke.hyperion.util.HashUtil;

@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class RestToken extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	private static final long TOKEN_VALID = 1000L * 60L * 60L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RestToken")
	@SequenceGenerator(name = "RestToken", sequenceName = "SEQ_ID_RestToken")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@Index(name = "IDX_RestToken_user")
	private User user;

	@Column(nullable = false)
	private String hash;

	@Column(nullable = false)
	private long expires;

	public RestToken(User user) {
		this.user = user;
		setExpires(System.currentTimeMillis() + TOKEN_VALID);
		this.hash = HashUtil.sha1Hash(Joiner.on("!").join(user.getUsername(),
				new Date().toString()));

	}

	protected RestToken() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public void refreshToken() {
		setExpires(System.currentTimeMillis() + TOKEN_VALID);
	}

	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

}