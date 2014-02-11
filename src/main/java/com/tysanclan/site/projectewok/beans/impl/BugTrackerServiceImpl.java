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
package com.tysanclan.site.projectewok.beans.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tysanclan.site.projectewok.beans.BugTrackerService;
import com.tysanclan.site.projectewok.entities.Bug;
import com.tysanclan.site.projectewok.entities.Bug.BugStatus;
import com.tysanclan.site.projectewok.entities.Bug.ReportType;
import com.tysanclan.site.projectewok.entities.BugComment;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.BugCommentDAO;
import com.tysanclan.site.projectewok.entities.dao.BugDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.BugFilter;

@Component
@Scope("request")
class BugTrackerServiceImpl implements BugTrackerService {
	private static Logger log = LoggerFactory
			.getLogger(BugTrackerServiceImpl.class);

	@Autowired
	private BugDAO bugDAO;

	@Autowired
	private BugCommentDAO bugCommentDAO;

	@Autowired
	private UserDAO userDAO;

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setBugDAO(BugDAO bugDAO) {
		this.bugDAO = bugDAO;
	}

	public void setBugCommentDAO(BugCommentDAO bugCommentDAO) {
		this.bugCommentDAO = bugCommentDAO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public Bug reportCrash(User user, String page, Exception exception) {

		if (!isKnownIssue(exception)) {
			log.error("Logging previously unknown error");

			String stackTrace = exceptionToString(exception);

			Bug report = new Bug();
			report.setAssignedTo(null);
			report.setDescription(stackTrace);
			report.setReported(new Date());
			report.setTitle("Autogenerated crash report for page " + page);
			report.setReporter(user);
			report.setReportType(ReportType.CRASHREPORT);
			report.setStatus(BugStatus.NEW);

			bugDAO.save(report);

			return report;
		} else {
			log.error("Found existing error");

			BugFilter filter = getExceptionFilter(exception);

			Bug report = bugDAO.getUniqueByFilter(filter);

			if (report.getStatus() == BugStatus.CLOSED
					|| report.getStatus() == BugStatus.RESOLVED) {
				reopenBug(report, user);
			}

			return report;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void markAsDuplicate(Bug bug, Bug duplicateOf) {
		bug.setStatus(BugStatus.RESOLVED);
		bug.setUpdated(new Date());
		bug.setDuplicateOf(duplicateOf);
		bugDAO.update(bug);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void assignBug(Bug bug, User assignTo) {
		bug.setAssignedTo(assignTo);
		bug.setStatus(BugStatus.ACKNOWLEDGED);
		bug.setUpdated(new Date());
		bugDAO.update(bug);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void bugResolved(Bug bug, String resolution, String fixedInVersion) {
		addCommentToBug(bug, bug.getAssignedTo(), resolution);

		bug.setResolutionVersion(fixedInVersion);
		bug.setStatus(BugStatus.RESOLVED);
		bug.setUpdated(new Date());
		bug.setResolutionVersion(fixedInVersion);
		bugDAO.update(bug);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Bug reportBug(User user, String title, String description) {
		Bug report = new Bug();
		report.setAssignedTo(null);
		report.setDescription(description);
		report.setReported(new Date());
		report.setTitle(title);
		report.setReporter(user);
		report.setReportType(ReportType.BUGREPORT);
		report.setStatus(BugStatus.NEW);

		bugDAO.save(report);

		return report;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Bug requestFeature(User user, String title, String description) {
		Bug report = new Bug();
		report.setAssignedTo(null);
		report.setDescription(description);
		report.setReported(new Date());
		report.setTitle(title);
		report.setReporter(user);
		report.setReportType(ReportType.FEATUREREQUEST);
		report.setStatus(BugStatus.NEW);

		bugDAO.save(report);

		return report;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public boolean isKnownIssue(Exception exception) {
		BugFilter filter = getExceptionFilter(exception);

		return bugDAO.countByFilter(filter) > 0;
	}

	private BugFilter getExceptionFilter(Exception exception) {
		BugFilter filter = new BugFilter();
		filter.setDescription(exceptionToString(exception));
		return filter;
	}

	private String exceptionToString(Exception exception) {
		StringBuilder builder = new StringBuilder();

		Throwable e = exception;

		while (e != null) {

			builder.append(e.toString());
			builder.append("\n");

			for (StackTraceElement el : e.getStackTrace()) {
				builder.append("\t");
				builder.append(el.toString());
				builder.append("\n");
			}

			e = e.getCause();
			if (e != null) {
				builder.append("caused by ");
			}
		}

		return builder.toString();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void grantBugRights(User user) {
		user.setBugReportMaster(true);
		userDAO.update(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void revokeBugRights(User user) {
		user.setBugReportMaster(false);
		userDAO.update(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addCommentToBug(Bug report, User user, String description) {
		BugComment comment = new BugComment();
		comment.setDescription(description);
		comment.setBug(report);
		comment.setCommenter(user);
		comment.setTime(new Date());

		bugCommentDAO.save(comment);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void closeBug(Bug bug) {
		bug.setStatus(BugStatus.CLOSED);
		bugDAO.update(bug);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void reopenBug(Bug bug, User user) {
		if (bug.getStatus() == BugStatus.CLOSED
				|| bug.getStatus() == BugStatus.RESOLVED) {
			bug.setStatus(BugStatus.NEW);
			bug.setAssignedTo(null);
			bugDAO.update(bug);

			addCommentToBug(bug, user,
					"Regression: Bug still occurs in current version");
		}
	}
}
