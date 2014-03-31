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
package com.tysanclan.site.projectewok.pages;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.components.AchievementsPanel;
import com.tysanclan.site.projectewok.components.AutoGroupLink;
import com.tysanclan.site.projectewok.components.AutoThreadLink;
import com.tysanclan.site.projectewok.components.ChancellorElectedSincePanel;
import com.tysanclan.site.projectewok.components.DateTimeLabel;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.RankIcon;
import com.tysanclan.site.projectewok.components.SenateElectedSincePanel;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.GameAccount;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.Profile;
import com.tysanclan.site.projectewok.entities.Role;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.UserGameRealm;
import com.tysanclan.site.projectewok.entities.dao.ForumPostDAO;
import com.tysanclan.site.projectewok.entities.dao.GroupDAO;
import com.tysanclan.site.projectewok.entities.dao.RoleDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.ForumPostFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.GroupFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.RoleFilter;
import com.tysanclan.site.projectewok.pages.member.MessageListPage;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class MemberPage extends TysanPage {

	public static class MemberPageParams {
		private final Long userId;

		public MemberPageParams(Long userId) {
			super();
			this.userId = userId;
		}

		public Long getUserId() {
			return userId;
		}
	}

	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDAO dao;

	@SpringBean
	private ForumPostDAO forumPostDAO;

	@SpringBean
	private GroupDAO groupDAO;

	@SpringBean
	private RoleDAO roleDAO;

	@SpringBean
	private ForumService forumService;

	public MemberPage(PageParameters params) {
		super("");

		MemberPageParams parameters;

		try {
			parameters = requiredLong("userid").forParameters(params).toClass(
					MemberPageParams.class);
		} catch (PageParameterExtractorException e) {
			throw new AbortWithHttpErrorCodeException(
					HttpServletResponse.SC_NOT_FOUND);
		}

		User u = dao.get(parameters.getUserId());

		if (u == null || !MemberUtil.isMember(u)) {
			throw new RestartResponseAtInterceptPageException(RosterPage.class);
		}

		initComponents(u);
	}

	private void initComponents(User u) {
		super.setPageTitle(u.getUsername() + " - Member");

		TimeZone tz = TimeZone.getTimeZone("America/New_York");
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
		sdf.setTimeZone(tz);
		add(new Label("membersince", new Model<String>(sdf.format(u
				.getJoinDate()))));

		add(new Label("lastlogin", new Model<String>(
				u.getLastAction() != null ? sdf.format(u.getLastAction())
						: null)));

		Profile profile = u.getProfile();

		add(new Label("realname", profile != null
				&& profile.getRealName() != null ? profile.getRealName() : "")
				.setVisible(profile != null && profile.getRealName() != null
						&& getUser() != null && MemberUtil.isMember(getUser())));

		WebMarkupContainer photo = new WebMarkupContainer("photo");
		photo.setVisible(false);

		add(photo);

		if (profile != null && profile.getPhotoURL() != null) {
			photo.add(AttributeModifier.replace("src", profile.getPhotoURL()));
			photo.setVisible(profile.isPhotoPublic()
					|| (getUser() != null && MemberUtil.isMember(getUser())));
		}

		add(new Label(
				"age",
				profile != null && profile.getBirthDate() != null ? Integer
						.toString(DateUtil.calculateAge(profile.getBirthDate()))
						: "Unknown").setVisible(profile != null
				&& profile.getBirthDate() != null && u.getRank() != Rank.HERO));
		add(new Label("username", u.getUsername()));

		WebMarkupContainer aboutMe = new WebMarkupContainer("aboutme");

		aboutMe.add(new Label("publicDescription", profile != null
				&& profile.getPublicDescription() != null ? profile
				.getPublicDescription() : "").setEscapeModelStrings(false)
				.setVisible(
						profile != null
								&& profile.getPublicDescription() != null));

		aboutMe.add(new Label("privateDescription", profile != null
				&& profile.getPrivateDescription() != null ? profile
				.getPrivateDescription() : "").setEscapeModelStrings(false)
				.setVisible(
						profile != null
								&& profile.getPrivateDescription() != null
								&& getUser() != null
								&& MemberUtil.isMember(getUser())));

		add(aboutMe
				.setVisible(profile != null
						&& (profile.getPublicDescription() != null || (profile
								.getPrivateDescription() != null
								&& getUser() != null && MemberUtil
									.isMember(getUser())))));

		GroupFilter gfilter = new GroupFilter();
		gfilter.addIncludedMember(u);
		List<Group> groups = groupDAO.findByFilter(gfilter);

		add(new ListView<Group>("groups", ModelMaker.wrap(groups)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<Group> item) {
				Group group = item.getModelObject();

				item.add(new AutoGroupLink("name", group));
			}
		}.setVisible(!groups.isEmpty()));

		RoleFilter rfilter = new RoleFilter();
		rfilter.setUser(u);

		add(new Label("usernameroles", u.getUsername()));

		List<Role> roles = roleDAO.findByFilter(rfilter);

		add(new ListView<Role>("roles", ModelMaker.wrap(roles)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<Role> item) {
				Role role = item.getModelObject();

				item.add(new Label("name", role.getName()));
				item.add(new Label("description", role.getDescription())
						.setEscapeModelStrings(false));
			}
		}.setVisible(!roles.isEmpty()));

		add(new RankIcon("rank", u.getRank()));
		add(new Label("rankName", u.getRank().toString()));

		if (u.getRank() == Rank.CHANCELLOR || u.getRank() == Rank.SENATOR) {
			if (u.getRank() == Rank.CHANCELLOR) {
				add(new ChancellorElectedSincePanel("electionDatePanel", u));
			} else {
				add(new SenateElectedSincePanel("electionDatePanel", u));
			}
		} else {
			add(new WebMarkupContainer("electionDatePanel").setVisible(false));
		}

		ForumPostFilter filter = new ForumPostFilter();
		filter.setShadow(false);
		filter.setUser(u);
		filter.addOrderBy("time", false);

		List<ForumPost> posts = forumPostDAO.findByFilter(filter);

		posts = forumService.filterPosts(getUser(), true, posts);

		List<ForumPost> topPosts = new LinkedList<ForumPost>();
		for (int i = 0; i < Math.min(posts.size(), 5); i++) {
			topPosts.add(posts.get(i));
		}

		ListView<ForumPost> lastPosts = new ListView<ForumPost>("lastposts",
				ModelMaker.wrap(topPosts)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ForumPost> item) {
				ForumPost post = item.getModelObject();

				item.add(new AutoThreadLink("thread", post.getThread()));

				item.add(new DateTimeLabel("time", post.getTime()));
			}

		};

		add(lastPosts.setVisible(!topPosts.isEmpty()));

		WebMarkupContainer container = new WebMarkupContainer("gamescontainer");

		List<UserGameRealm> played = new LinkedList<UserGameRealm>();
		played.addAll(u.getPlayedGames());

		Collections.sort(played, new Comparator<UserGameRealm>() {
			/**
			 * @see java.util.Comparator#compare(java.lang.Object,
			 *      java.lang.Object)
			 */
			@Override
			public int compare(UserGameRealm o1, UserGameRealm o2) {
				return o1.getGame().getName()
						.compareToIgnoreCase(o2.getGame().getName());
			}
		});

		container.add(new ListView<UserGameRealm>("games", ModelMaker
				.wrap(played)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<UserGameRealm> item) {
				UserGameRealm ugr = item.getModelObject();

				item.add(new Label("game", ugr.getGame().getName()));
				item.add(new Label("realm", ugr.getRealm().getName()));
				StringBuilder builder = new StringBuilder();

				for (GameAccount account : ugr.getAccounts()) {
					if (builder.length() > 0) {
						builder.append(", ");
					}
					builder.append(account.getName());
				}

				if (builder.length() == 0) {
					builder.append('-');
				}

				item.add(new Label("accounts", builder.toString()));

			}

		});

		add(container);

		boolean twitviz = profile != null && profile.getTwitterUID() != null;

		add(new Label("twitterhead", u.getUsername() + " on Twitter")
				.setVisible(twitviz));

		String url = "http://twitter.com/"
				+ (twitviz && profile != null ? profile.getTwitterUID() : "");

		add(new Label("twitterprofile", url).add(AttributeModifier.replace(
				"href", url)));

		add(new AchievementsPanel("achievements", u));

		add(new IconLink.Builder("images/icons/email_add.png",
				new DefaultClickResponder<User>(ModelMaker.wrap(u)) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new MessageListPage(getModelObject()));
					}
				})
				.setText("Send Message")
				.newInstance("sendMessage")
				.setVisible(getUser() != null && MemberUtil.isMember(getUser())));
	}
}
