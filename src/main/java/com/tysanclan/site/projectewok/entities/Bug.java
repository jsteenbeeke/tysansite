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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.wicket.Page;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.TysanApplication.VersionDescriptor;
import com.tysanclan.site.projectewok.pages.member.BugOverviewPage;
import com.tysanclan.site.projectewok.pages.member.FeatureOverviewPage;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(indexes = {
		@Index(columnList = "assignedTo_id", name = "IDX_BUG_ASSIGNEDTO"),
		@Index(columnList = "duplicateOf_id", name = "IDX_BUG_DUPEOF"),
		@Index(columnList = "reporter_id", name = "IDX_BUG_REPORER")

})
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Bug extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	public static enum ReportType {
		BUGREPORT("Bug Report") {
			@Override
			public Page getOverviewPage() {
				return new BugOverviewPage();
			}
		},
		FEATUREREQUEST("Feature Request") {
			public String getUrl(Long id) {
				return "https://www.tysanclan.com/feature/" + id.toString()
						+ "/";
			}

			@Override
			public Page getOverviewPage() {
				return new FeatureOverviewPage();
			}
		},
		CRASHREPORT("Crash Report") {
			@Override
			public Page getOverviewPage() {
				return new BugOverviewPage();
			}
		};

		private final String description;

		private ReportType(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

		public String getUrl(Long id) {
			return "https://www.tysanclan.com/bug/" + id.toString() + "/";
		}

		public abstract Page getOverviewPage();
	}

	public static enum BugStatus {
		NEW, ACKNOWLEDGED, RESOLVED, CLOSED;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Bug")
	@SequenceGenerator(name = "Bug", sequenceName = "SEQ_ID_Bug", allocationSize = 1, initialValue = 1)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String description;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private User reporter;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private User assignedTo;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date reported;

	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;

	@OrderBy("time")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bug")
	private List<BugComment> comments = Lists.newArrayList();

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ReportType reportType;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BugStatus status;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Bug duplicateOf;

	@Column(nullable = true)
	private String resolutionVersion;

	// $P$

	/**
	 * Creates a new Bug object
	 */
	public Bug() {
		// $H$
	}

	/**
	 * Returns the ID of this Bug
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this Bug
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getReporter() {
		return reporter;
	}

	public void setReporter(User reporter) {
		this.reporter = reporter;
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Date getReported() {
		return reported;
	}

	public void setReported(Date reported) {
		this.reported = reported;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public List<BugComment> getComments() {
		return comments;
	}

	public void setComments(List<BugComment> comments) {
		this.comments = comments;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public Bug getDuplicateOf() {
		return duplicateOf;
	}

	public void setDuplicateOf(Bug duplicateOf) {
		this.duplicateOf = duplicateOf;
	}

	public String getResolutionVersion() {
		return resolutionVersion;
	}

	public void setResolutionVersion(String resolutionVersion) {
		this.resolutionVersion = resolutionVersion;
	}

	public BugStatus getStatus() {
		return status;
	}

	public void setStatus(BugStatus status) {
		this.status = status;
	}

	@Transient
	public boolean isResolvedInCurrentVersion() {
		VersionDescriptor current = VersionDescriptor.of(TysanApplication
				.getApplicationVersion());
		VersionDescriptor resolved = VersionDescriptor
				.of(getResolutionVersion());

		return current.compareTo(resolved) >= 0;

	}

	// $GS$
}
