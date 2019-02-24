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

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.entities.*;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * @author Jeroen Steenbeeke
 */
public class MemberForumViewContext extends AbstractForumViewContext {
	private static final long serialVersionUID = 1L;

	@Override
	public int countCategories(EntityManager em, User viewer) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

		Root<ForumCategory> root = criteriaQuery.from(ForumCategory.class);

		Subquery<Forum> subquery = criteriaQuery.subquery(Forum.class);
		Root<Forum> subqueryRoot = subquery.from(Forum.class);

		Subquery<Long> groupSubquery = criteriaQuery.subquery(Long.class);
		Root<Group> groupRoot = groupSubquery.from(Group.class);
		groupSubquery.select(groupRoot.get(Group_.id));
		groupSubquery.where(groupRoot.get(Group_.groupMembers).in(viewer));

		subquery.where(criteriaBuilder.or(criteriaBuilder.notEqual(subqueryRoot.get("dtype"), "GroupForum"),
				subqueryRoot.get(GroupForum_.id).in(groupSubquery)),
				criteriaBuilder.equal(subqueryRoot.get(Forum_.category), root.get(ForumCategory_.id)));

		criteriaQuery.select(criteriaBuilder.count(root));
		criteriaQuery.where(criteriaBuilder.exists(subquery));
		return count(em, criteriaQuery);
	}

	@Override
	public List<ForumCategory> getCategories(EntityManager em, User viewer,
											 int offset, int count) {

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<ForumCategory> criteriaQuery = criteriaBuilder.createQuery(ForumCategory.class);

		Root<ForumCategory> root = criteriaQuery.from(ForumCategory.class);

		Subquery<Forum> subquery = criteriaQuery.subquery(Forum.class);
		Root<Forum> subqueryRoot = subquery.from(Forum.class);

		Subquery<Long> groupSubquery = criteriaQuery.subquery(Long.class);
		Root<Group> groupRoot = groupSubquery.from(Group.class);
		groupSubquery.select(groupRoot.get(Group_.id));
		groupSubquery.where(groupRoot.get(Group_.groupMembers).in(viewer));

		subquery.where(criteriaBuilder.or(criteriaBuilder.notEqual(subqueryRoot.get("dtype"), "GroupForum"),
				subqueryRoot.get(GroupForum_.id).in(groupSubquery)),
				criteriaBuilder.equal(subqueryRoot.get(Forum_.category), root.get(ForumCategory_.id)));

		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.exists(subquery));
		return listOf(em, criteriaQuery, count, offset);
	}

	@Override
	public int countForums(EntityManager em, ForumCategory context, User viewer) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

		Root<Forum> root = criteriaQuery.from(Forum.class);
		Subquery<Long> groupSubquery = criteriaQuery.subquery(Long.class);
		Root<Group> groupRoot = groupSubquery.from(Group.class);
		groupSubquery.select(groupRoot.get(Group_.id));
		groupSubquery.where(groupRoot.get(Group_.groupMembers).in(viewer));

		criteriaQuery.select(criteriaBuilder.count(root));
		criteriaQuery.where(criteriaBuilder.or(criteriaBuilder.notEqual(root.get("dtype"), "GroupForum"),
				root.get(GroupForum_.id).in(groupSubquery)));

		return count(em, criteriaQuery);
	}

	@Override
	public List<Forum> getForums(EntityManager em, ForumCategory context,
								 User viewer, int offset, int count) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Forum> criteriaQuery = criteriaBuilder.createQuery(Forum.class);

		Root<Forum> root = criteriaQuery.from(Forum.class);
		Subquery<Long> groupSubquery = criteriaQuery.subquery(Long.class);
		Root<Group> groupRoot = groupSubquery.from(Group.class);
		groupSubquery.select(groupRoot.get(Group_.id));
		groupSubquery.where(groupRoot.get(Group_.groupMembers).in(viewer));

		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.or(criteriaBuilder.notEqual(root.get("dtype"), "GroupForum"),
				root.get(GroupForum_.id).in(groupSubquery)));

		return listOf(em, criteriaQuery, count, offset);
	}

	@Override
	public int countThreads(EntityManager em, Forum context, User viewer) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<ForumThread> root = criteriaQuery.from(ForumThread.class);

		Subquery<ForumPost> forumPostQuery = criteriaQuery.subquery(ForumPost.class);
		Root<ForumPost> forumPostRoot = forumPostQuery.from(ForumPost.class);
		forumPostQuery.select(forumPostRoot);
		forumPostQuery.where(criteriaBuilder.equal(forumPostRoot.get(ForumPost_.shadow), false),
				criteriaBuilder.equal(forumPostRoot.get(ForumPost_.thread), root.get(ForumThread_.id)));

		Subquery<User> userOfRankQuery = criteriaQuery.subquery(User.class);
		Root<User> userOfRankRoot = userOfRankQuery.from(User.class);
		userOfRankQuery.select(userOfRankRoot);
		userOfRankQuery.where(userOfRankRoot.get(User_.rank).in(Rank.CHANCELLOR, Rank.SENATOR, Rank.TRUTHSAYER),
				criteriaBuilder.equal(userOfRankRoot.get(User_.id), viewer.getId()));

		Subquery<Trial> trialQuery = criteriaQuery.subquery(Trial.class);
		Root<Trial> trialRoot = trialQuery.from(Trial.class);
		trialQuery.select(trialRoot);
		trialQuery.where(criteriaBuilder.equal(trialRoot.get(Trial_.trialThread), root.get(ForumThread_.id)),
				criteriaBuilder.notEqual(trialRoot.get(Trial_.accused), viewer),
				criteriaBuilder.not(criteriaBuilder.exists(userOfRankQuery))
		);

		criteriaQuery.select(criteriaBuilder.count(root));
		criteriaQuery.where(criteriaBuilder.equal(root.get(ForumThread_.forum), context),
				criteriaBuilder.equal(root.get(ForumThread_.shadow), false),
				criteriaBuilder.not(criteriaBuilder.exists(trialQuery)),
				criteriaBuilder.exists(forumPostQuery)
		);

		return count(em, criteriaQuery);

	}

	@Override
	public List<ForumThread> getThreads(EntityManager em, Forum context,
										User viewer, int offset, int count) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<ForumThread> criteriaQuery = criteriaBuilder.createQuery(ForumThread.class);
		Root<ForumThread> root = criteriaQuery.from(ForumThread.class);

		Subquery<ForumPost> forumPostQuery = criteriaQuery.subquery(ForumPost.class);
		Root<ForumPost> forumPostRoot = forumPostQuery.from(ForumPost.class);
		forumPostQuery.select(forumPostRoot);
		forumPostQuery.where(criteriaBuilder.equal(forumPostRoot.get(ForumPost_.shadow), false),
				criteriaBuilder.equal(forumPostRoot.get(ForumPost_.thread), root.get(ForumThread_.id)));

		Subquery<User> userOfRankQuery = criteriaQuery.subquery(User.class);
		Root<User> userOfRankRoot = userOfRankQuery.from(User.class);
		userOfRankQuery.select(userOfRankRoot);
		userOfRankQuery.where(userOfRankRoot.get(User_.rank).in(Rank.CHANCELLOR, Rank.SENATOR, Rank.TRUTHSAYER),
				criteriaBuilder.equal(userOfRankRoot.get(User_.id), viewer.getId()));

		Subquery<Trial> trialQuery = criteriaQuery.subquery(Trial.class);
		Root<Trial> trialRoot = trialQuery.from(Trial.class);
		trialQuery.select(trialRoot);
		trialQuery.where(criteriaBuilder.equal(trialRoot.get(Trial_.trialThread), root.get(ForumThread_.id)),
				criteriaBuilder.notEqual(trialRoot.get(Trial_.accused), viewer),
				criteriaBuilder.not(criteriaBuilder.exists(userOfRankQuery))
		);

		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get(ForumThread_.forum), context),
				criteriaBuilder.equal(root.get(ForumThread_.shadow), false),
				criteriaBuilder.not(criteriaBuilder.exists(trialQuery)),
				criteriaBuilder.exists(forumPostQuery)
		);

		return listOf(em, criteriaQuery, count, offset);
	}

	@Override
	public int countPosts(EntityManager em, ForumThread context, User viewer) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<ForumPost> root = criteriaQuery.from(ForumPost.class);

		criteriaQuery.select(criteriaBuilder.count(root));
		criteriaQuery.where(criteriaBuilder.equal(root.get(ForumPost_.shadow), false),
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

		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get(ForumPost_.shadow), false),
				criteriaBuilder.equal(root.get(ForumPost_.thread), context)
		);

		return listOf(em, criteriaQuery, count, offset);
	}
}
