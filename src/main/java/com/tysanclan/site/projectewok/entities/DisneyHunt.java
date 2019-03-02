package com.tysanclan.site.projectewok.entities;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class DisneyHunt extends BaseDomainObject {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DisneyHunt")
	@SequenceGenerator(name = "DisneyHunt", sequenceName = "seq_id_disneyhunt", allocationSize = 1)
	private Long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DisneyCharacter type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DisneyCharacter getType() {
		return type;
	}

	public void setType(DisneyCharacter type) {
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
