package com.tysanclan.insurgence.contract.datamodel;

import org.codehaus.jackson.annotate.JsonProperty;

public class RSUser {
	@JsonProperty
	private Long id;

	@JsonProperty
	private String username;

	@JsonProperty
	private RSUserRank rank;

	public RSUser() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public RSUserRank getRank() {
		return rank;
	}

	public void setRank(RSUserRank rank) {
		this.rank = rank;
	}

}
