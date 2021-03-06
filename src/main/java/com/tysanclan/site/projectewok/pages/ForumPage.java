/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.pages;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.components.ForumPanel;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.dao.ForumDAO;
import com.tysanclan.site.projectewok.entities.dao.ForumThreadDAO;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jeroen Steenbeeke
 */
public class ForumPage extends TysanPage {
	public static class ForumPageParams {
		private final long forumId;

		private final long pageNumber;

		public ForumPageParams(Long forumId, Long pageNumber) {
			this.forumId = forumId;
			this.pageNumber = pageNumber;
		}

		public long getForumId() {
			return forumId;
		}

		public long getPageNumber() {
			return pageNumber;
		}
	}

	private static final long serialVersionUID = 1L;

	@SpringBean
	private ForumDAO forumDAO;

	@SpringBean
	private ForumThreadDAO forumThreadDAO;

	private IModel<Forum> forumModel;

	public ForumPage(PageParameters params) {
		super("");

		ForumPageParams parameters;

		try {
			parameters = requiredLong("forumid").requiredLong("pageid")
					.forParameters(params).toClass(ForumPageParams.class);
		} catch (PageParameterExtractorException e) {
			throw new AbortWithHttpErrorCodeException(
					HttpServletResponse.SC_NOT_FOUND);
		}

		if (parameters.getPageNumber() <= 0) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		Forum forum = forumDAO.load(parameters.getForumId()).getOrElseThrow(
				() -> new RestartResponseAtInterceptPageException(
						AccessDeniedPage.class));

		if (!forum.canView(getUser())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		initComponents(forum, parameters.getPageNumber());
	}

	public ForumPage(final Forum forum) {
		super("");
		initComponents(forum, 1L);
	}

	public ForumPage(final Forum forum, final long pageNumber) {
		super("");
		initComponents(forum, pageNumber);
	}

	/**
	 * @return the forumDAO
	 */
	public ForumDAO getForumDAO() {
		return forumDAO;
	}

	/**
	 * @return the forumThreadDAO
	 */
	public ForumThreadDAO getForumThreadDAO() {
		return forumThreadDAO;
	}

	public void initComponents(final Forum forum, final long pageNumber) {
		forumModel = ModelMaker.wrap(forum);

		boolean publicView = getUser() == null;

		setPageTitle(forum.getName());

		add(new Link<Void>("overview") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new ForumOverviewPage());

			}
		});

		add(new ForumPanel("threads", forum, pageNumber, publicView));

	}

	/**
	 * @param forumDAO
	 *            the forumDAO to set
	 */
	public void setForumDAO(ForumDAO forumDAO) {
		this.forumDAO = forumDAO;
	}

	public void setForumThreadDAO(ForumThreadDAO forumThreadDAO) {
		this.forumThreadDAO = forumThreadDAO;
	}

	/**
	 * @see org.apache.wicket.Page#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();
		if (forumModel != null) {
			forumModel.detach();

		}
	}

	public Forum getForum() {
		return forumModel.getObject();
	}

}
