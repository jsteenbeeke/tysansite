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
package com.tysanclan.site.projectewok.util.forum;

import java.util.List;

import com.tysanclan.site.projectewok.entities.*;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * Forum context for banned users
 *
 * @author Jeroen Steenbeeke
 */
public class ShadowForumViewContext extends PublicForumViewContext {

	private static final long serialVersionUID = 1L;

	@Override
	public int countThreads(EntityManager em, Forum context, User viewer) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

		Root<ForumThread> root = criteriaQuery.from(ForumThread.class);

		Subquery<Trial> trialSubquery = criteriaQuery.subquery(Trial.class);
		Root<Trial> trialRoot = trialSubquery.from(Trial.class);

		Subquery<ForumPost> forumPostSubquery = criteriaQuery.subquery(ForumPost.class);
		Root<ForumPost> forumPostRoot = forumPostSubquery.from(ForumPost.class);


		criteriaQuery
				.select(criteriaBuilder.count(root))
				.where(criteriaBuilder.equal(root.get(ForumThread_.forum), context),
						criteriaBuilder.not(criteriaBuilder.exists(
								trialSubquery.select(trialRoot)
										.where(
												criteriaBuilder.equal(
														trialRoot.get(Trial_.trialThread),
														root.get(ForumThread_.id))))),
						criteriaBuilder.exists(
								forumPostSubquery.select(forumPostRoot).where(
										criteriaBuilder.or(criteriaBuilder.equal(forumPostRoot.get(ForumPost_.shadow), false),
												criteriaBuilder.equal(forumPostRoot.get(ForumPost_.poster), viewer)
										),
										criteriaBuilder.equal(forumPostRoot.get(ForumPost_.thread), root.get(ForumThread_.id))
								)
						)
				);

		return count(em, criteriaQuery);

	}

	@Override
	public List<ForumThread> getThreads(EntityManager em, Forum context,
										User viewer, int offset, int count) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<ForumThread> criteriaQuery = criteriaBuilder.createQuery(ForumThread.class);

		Root<ForumThread> root = criteriaQuery.from(ForumThread.class);

		Subquery<Trial> trialSubquery = criteriaQuery.subquery(Trial.class);
		Root<Trial> trialRoot = trialSubquery.from(Trial.class);

		Subquery<ForumPost> forumPostSubquery = criteriaQuery.subquery(ForumPost.class);
		Root<ForumPost> forumPostRoot = forumPostSubquery.from(ForumPost.class);


		criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(ForumThread_.forum), context),
				criteriaBuilder.not(criteriaBuilder.exists(
						trialSubquery.select(trialRoot)
								.where(
										criteriaBuilder.equal(
												trialRoot.get(Trial_.trialThread),
												root.get(ForumThread_.id))))),
				criteriaBuilder.exists(
						forumPostSubquery.select(forumPostRoot).where(
								criteriaBuilder.or(criteriaBuilder.equal(forumPostRoot.get(ForumPost_.shadow), false),
										criteriaBuilder.equal(forumPostRoot.get(ForumPost_.poster), viewer)),
								criteriaBuilder.equal(forumPostRoot.get(ForumPost_.thread), root
										.get(ForumThread_.id))
						)
				)
		);

		return listOf(em, criteriaQuery);
	}

	@Override
	public int countPosts(EntityManager em, ForumThread context, User viewer) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<ForumPost> root = criteriaQuery.from(ForumPost.class);

		criteriaQuery.select(criteriaBuilder.count(root)).where(
				criteriaBuilder.or(
						criteriaBuilder.equal(root.get(ForumPost_.shadow), false),
						criteriaBuilder.equal(root.get(ForumPost_.poster), viewer)),
				criteriaBuilder.equal(root.get(ForumPost_.thread), context)
		);


		return count(em, criteriaQuery);
	}

	@Override
	public List<ForumPost> getPosts(EntityManager em, ForumThread context,
									User viewer, int offset, int count) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<ForumPost> criteriaQuery = criteriaBuilder.createQuery(ForumPost.class);
		Root<ForumPost> root = criteriaQuery.from(ForumPost.class);

		criteriaQuery.select(root).where(
				criteriaBuilder.or(
						criteriaBuilder.equal(root.get(ForumPost_.shadow), false),
						criteriaBuilder.equal(root.get(ForumPost_.poster), viewer)),
				criteriaBuilder.equal(root.get(ForumPost_.thread), context)
		);

		return listOf(em, criteriaQuery, count, offset);
	}
}
