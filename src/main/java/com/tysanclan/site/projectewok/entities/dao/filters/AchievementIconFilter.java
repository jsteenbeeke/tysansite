/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
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
package com.tysanclan.site.projectewok.entities.dao.filters;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.AchievementIcon;
import com.tysanclan.site.projectewok.entities.User;

public class AchievementIconFilter extends SearchFilter<AchievementIcon> {

	private static final long serialVersionUID = 1L;

	private Boolean unclaimed;

	private Boolean creatorOnly;

	private IModel<User> creator = new Model<User>();

	private Boolean approved;

	private boolean creatorOnlyAsNull = false;

	private boolean approvedAsNull = false;

	public Boolean getUnclaimed() {
		return unclaimed;
	}

	public void setUnclaimed(Boolean unclaimed) {
		this.unclaimed = unclaimed;
	}

	public Boolean getCreatorOnly() {
		return creatorOnly;
	}

	public void setCreatorOnly(Boolean creatorOnly) {
		this.creatorOnly = creatorOnly;
	}

	public User getCreator() {
		return creator.getObject();
	}

	public void setCreator(User creator) {
		this.creator = ModelMaker.wrap(creator);
	}

	@Override
	public void detach() {
		super.detach();
		creator.detach();
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public boolean isCreatorOnlyAsNull() {
		return creatorOnlyAsNull;
	}

	public void setCreatorOnlyAsNull(boolean creatorOnlyAsNull) {
		this.creatorOnlyAsNull = creatorOnlyAsNull;
	}

	public boolean isApprovedAsNull() {
		return approvedAsNull;
	}

	public void setApprovedAsNull(boolean approvedAsNull) {
		this.approvedAsNull = approvedAsNull;
	}

}
