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
package com.tysanclan.site.projectewok.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;

import com.google.common.base.Joiner;
import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.jeroensteenbeeke.hyperion.util.HashUtil;

@Entity
@Table(indexes = { //
		@Index(name = "IDX_RestToken_user", columnList = "user_id"), //
		@Index(name = "IDX_RESTTOKEN_APPLICATION", columnList = "application_id") //
})
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class RestToken extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	private static final long TOKEN_VALID = 1000L * 60L * 60L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RestToken")
	@SequenceGenerator(name = "RestToken", sequenceName = "SEQ_ID_RestToken")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private User user;

	@Column(nullable = false)
	private String hash;

	@Column(nullable = false)
	private long expires;

	@ManyToOne(optional = false)
	private AuthorizedRestApplication application;

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
	public final Serializable getDomainObjectId() {
		return getId();
	}

	public AuthorizedRestApplication getApplication() {
		return application;
	}

	public void setApplication(AuthorizedRestApplication application) {
		this.application = application;
	}

}
