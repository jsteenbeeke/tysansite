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

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "forum")
public class ForumCategory extends BaseDomainObject {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ForumCategory")
	@SequenceGenerator(name = "ForumCategory", sequenceName = "SEQ_ID_ForumCategory", allocationSize = 1)
	private Long id;

	@Column
	private String name;

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	@OrderBy(value = "position asc")
	private List<Forum> forums;

	@Column
	private boolean allowPublicGroupForums;

	// $P$

	public ForumCategory() {
		forums = new LinkedList<Forum>();
		// $H$
	}

	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            The ID to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Forum> getForums() {
		return forums;
	}

	public void setForums(List<Forum> forums) {
		this.forums = forums;
	}

	/**
	 * @return the allowPublicGroupForums
	 */
	public boolean isAllowPublicGroupForums() {
		return allowPublicGroupForums;
	}

	/**
	 * @param allowPublicGroupForums
	 *            the allowPublicGroupForums to set
	 */
	public void setAllowPublicGroupForums(boolean allowPublicGroupForums) {
		this.allowPublicGroupForums = allowPublicGroupForums;
	}

}
