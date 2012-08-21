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

import java.util.Date;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.JoinApplication;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class JoinApplicationFilter extends SearchFilter<JoinApplication> {
	private static final long serialVersionUID = 1L;

	private IModel<User> applicant = new Model<User>();
	private IModel<User> mentor = new Model<User>();
	private IModel<ForumThread> joinThread = new Model<ForumThread>();
	private Date dateBefore;

	/**
	 * @return the applicant
	 */
	public User getApplicant() {
		return applicant.getObject();
	}

	/**
	 * @param applicant
	 *            the applicant to set
	 */
	public void setApplicant(User applicant) {
		this.applicant = ModelMaker.wrap(applicant);
	}

	/**
	 * @return the mentor
	 */
	public User getMentor() {
		return mentor.getObject();
	}

	/**
	 * @param mentor
	 *            the mentor to set
	 */
	public void setMentor(User mentor) {
		this.mentor = ModelMaker.wrap(mentor);
	}

	/**
	 * @return the joinThread
	 */
	public ForumThread getJoinThread() {
		return joinThread.getObject();
	}

	/**
	 * @param joinThread
	 *            the joinThread to set
	 */
	public void setJoinThread(ForumThread joinThread) {
		this.joinThread = ModelMaker.wrap(joinThread);
	}

	/**
	 * @return the dateBefore
	 */
	public Date getDateBefore() {
		return dateBefore;
	}

	/**
	 * @param dateBefore
	 *            the dateBefore to set
	 */
	public void setDateBefore(Date dateBefore) {
		this.dateBefore = dateBefore;
	}

	@Override
	public void detach() {
		super.detach();
		applicant.detach();
		mentor.detach();
		joinThread.detach();

	}
}
