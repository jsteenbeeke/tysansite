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
package com.tysanclan.site.projectewok.entities.dao.filters;

import java.util.LinkedList;
import java.util.List;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.Bug;
import com.tysanclan.site.projectewok.entities.Bug.BugStatus;
import com.tysanclan.site.projectewok.entities.Bug.ReportType;

public class BugFilter extends SearchFilter<Bug> {
	private static final long serialVersionUID = 1L;
	private String description;

	private Long exclude;

	private List<ReportType> reportTypes = new LinkedList<ReportType>();

	private List<BugStatus> allowedStatus = new LinkedList<BugStatus>();

	public void setExclude(Long exclude) {
		this.exclude = exclude;
	}

	public Long getExclude() {
		return exclude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ReportType> getReportTypes() {
		return reportTypes;
	}

	public void addReportType(ReportType type) {
		reportTypes.add(type);
	}

	public List<BugStatus> getAllowedStatus() {
		return allowedStatus;
	}

	public void addAllowedStatus(BugStatus status) {
		allowedStatus.add(status);
	}

}
