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
package com.tysanclan.site.projectewok.components;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.beans.HumorService;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.GameAccount;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.UserGameRealm;
import com.tysanclan.site.projectewok.pages.MemberPage;
import com.tysanclan.site.projectewok.pages.forum.ConfirmForumPostDeletePage;
import com.tysanclan.site.projectewok.pages.forum.EditForumPostPage;
import com.tysanclan.site.projectewok.pages.member.MessageListPage;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;
import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;
import io.vavr.control.Option;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
public class PostPanel extends Panel {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ForumService forumService;

	@SpringBean
	private HumorService humorService;

	public PostPanel(String id, ForumPost post) {
		this(id, post, true);
	}

	public PostPanel(String id, ForumPost post, boolean moderatorOptions) {
		super(id, ModelMaker.wrap(post));

		if (post == null) {
			WebMarkupContainer container = new WebMarkupContainer("container");
			container.setVisible(false);

			addRealms(container, null);

			container.add(new WebMarkupContainer("lucky").setVisible(false));
			container.add(new WebMarkupContainer("bpm").setVisible(false));
			container.add(new WebMarkupContainer("disney").setVisible(false));
			container.add(new WebMarkupContainer("poster").setVisible(false));
			container.add(new WebMarkupContainer("customtitle")
					.setVisible(false));
			container.add(new WebMarkupContainer("posttime").setVisible(false));
			container.add(new WebMarkupContainer("rank").setVisible(false));
			container
					.add(new WebMarkupContainer("donations").setVisible(false));
			container.add(new WebMarkupContainer("avatar").setVisible(false));
			container.add(new WebMarkupContainer("body").setVisible(false));

			WebMarkupContainer branchNote = new WebMarkupContainer(
					"branchnote");
			branchNote.add(new WebMarkupContainer("source").setVisible(false));

			container.add(branchNote);

			container
					.add(new WebMarkupContainer("signature").setVisible(false));
			container.add(new WebMarkupContainer("icon").setVisible(false));
			container.add(new WebMarkupContainer("edit").setVisible(false));
			container.add(new WebMarkupContainer("delete").setVisible(false));

			add(container);

		} else {
			User u = TysanSession.session().flatMap(TysanSession::getUser)
					.getOrNull();

			WebMarkupContainer container = new WebMarkupContainer("container");

			addRealms(container, post.getPoster());

			Integer luckyScore = post.getPoster() != null
					&& post.getPoster().getLuckyScore() != null ?
					post.getPoster().getLuckyScore() :
					0;

			Label lucky = new Label("lucky",
					new Model<>(luckyScore));

			Calendar cal = DateUtil.getCalendarInstance();

			// Feeling lucky score visible after april 1st 2010
			lucky.setVisible(luckyScore > 0 && (cal.get(Calendar.YEAR) > 2010
					|| cal.get(Calendar.YEAR) == 2010
					&& cal.get(Calendar.MONTH) >= 3

			));

			container.add(lucky);

			Option<String> disneyScore = humorService.getDisneyScore(post.getPoster());

			Label disney = new Label("disney",
					new Model<>(disneyScore.getOrNull()));
			disney.setVisible(disneyScore.isDefined() && (cal.get(Calendar.YEAR) > 2019
					|| cal.get(Calendar.YEAR) == 2019
					&& cal.get(Calendar.MONTH) >= 3)

			);
			container.add(disney);

			Integer bpm = post.getPoster().getBpm();
			container.add(new Label("bpm", bpm).setVisible(bpm != null));
			container.add(new ContextImage("bpmicon", "images/techno-icon.gif")
					.setVisible(bpm != null));

			final Long posterId =
					post.getPoster() != null ? post.getPoster().getId() : null;

			WebMarkupContainer poster = new WebMarkupContainer("poster");
			poster.add(
					AttributeModifier.replace("name", post.getId().toString()));

			poster.add(new Label("name", post.getPosterVisibleName())
					.setRenderBodyOnly(true));

			container.add(new IconLink.Builder("images/icons/email_add.png",
					new DefaultClickResponder<User>(
							ModelMaker.wrap(post.getPoster())) {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {
							setResponsePage(
									new MessageListPage(getModelObject()));

						}
					}).newInstance("sendMessage").setVisible(
					u != null && MemberUtil.isMember(u) && MemberUtil
							.isMember(post.getPoster())).add(AttributeModifier
					.replace("style", "display: inline;")));

			container.add(new IconLink.Builder("images/icons/vcard.png",
					new DefaultClickResponder<User>(ModelMaker.wrap(u)) {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {

							PageParameters params = new PageParameters();

							params.add("userid", Long.toString(posterId));

							setResponsePage(MemberPage.class, params);
						}
					}).newInstance("viewProfile").setVisible(
					posterId != null && u != null && MemberUtil.isMember(u)
							&& MemberUtil.isMember(post.getPoster()))
					.add(AttributeModifier
							.replace("style", "display: inline;")));

			container.add(poster);

			container.add(new BBCodePanel("customtitle",
					post.getPoster() != null ?
							post.getPoster().getCustomTitle() :
							""));

			container.add(new DateTimeLabel("posttime", post.getTime()));

			container.add(new RankIcon("rank", post.getPoster() != null ?
					post.getPoster().getRank() :
					Rank.FORUM).setVisible(post.getPoster() != null ?
					post.getPoster().getRank() != Rank.FORUM
							&& post.getPoster().getRank() != Rank.BANNED :
					false));
			container.add(new DonatorPanel("donations", post.getPoster()));
			container.add(new Label("rankName", post.getPoster() != null ?
					post.getPoster().getRank().toString() :
					"Forum User"));

			WebMarkupContainer image = new WebMarkupContainer("avatar");
			final String url = post.getPoster() != null ?
					BBCodeUtil.filterURL(post.getPoster().getImageURL()) :
					"";
			if (url != null && (url.isEmpty() || "#".equals(url))) {
				image.setVisible(false);
			} else {
				image.add(AttributeModifier.replace("src", url));
			}
			container.add(image);

			BBCodePanel body = new BBCodePanel("body",
					new Model<String>(aprilFilter(post.getContent())));

			WebMarkupContainer branchnote = new WebMarkupContainer(
					"branchnote");
			if (post.getBranchTo() != null) {
				branchnote
						.add(new AutoThreadLink("source", post.getBranchTo()));
			} else {
				branchnote.add(new WebMarkupContainer("source"));
			}

			branchnote.setVisible(post.getBranchTo() != null);
			container.add(branchnote);

			container.add(body);
			container.add(new BBCodePanel("signature",
					post.getPoster() != null ?
							post.getPoster().getSignature() :
							"").setVisible(
					post.getPoster() != null && !post.getPoster().getSignature()
							.isEmpty()));
			container.add(new ContextImage("icon", "images/icons/new.png")
					.setVisible(
							u != null && forumService.isPostUnread(u, post)));

			if (u != null) {
				forumService.markForumPostRead(u, post);
			}

			boolean canEdit =
					moderatorOptions && forumService.canEditPost(u, post);

			Link<ForumPost> edit = new Link<ForumPost>("edit",
					ModelMaker.wrap(post)) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(new EditForumPostPage(getModelObject()));
				}
			};

			edit.setVisible(canEdit);
			container.add(edit);

			container.add(new Link<ForumPost>("delete", ModelMaker.wrap(post)) {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(
							new ConfirmForumPostDeletePage(getModelObject()));

				}
			});

			add(container);
		}

	}

	private String aprilFilter(String content) {
		Calendar cal = DateUtil.getCalendarInstance();

		if (cal.get(Calendar.MONTH) == Calendar.APRIL
				&& cal.get(Calendar.DAY_OF_MONTH) == 1) {
			if (cal.get(Calendar.YEAR) == 2011) {
				return content
						+ "<p>On a less serious note: I love ponies!</p>";
			}
		}

		return content;
	}

	private void addRealms(WebMarkupContainer parent, User poster) {
		List<UserGameRealm> realms = new LinkedList<UserGameRealm>();

		if (MemberUtil.isMember(poster)) {
			for (UserGameRealm ugr : poster.getPlayedGames()) {
				if (!ugr.getAccounts().isEmpty()) {
					realms.add(ugr);
				}
			}
		}

		parent.add(
				new ListView<UserGameRealm>("realms", ModelMaker.wrap(realms)) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<UserGameRealm> item) {
						UserGameRealm ugr = item.getModelObject();

						StringBuilder accountList = new StringBuilder();

						for (GameAccount acc : ugr.getAccounts()) {
							if (accountList.length() > 0) {
								accountList.append(", ");
							}

							accountList.append(acc.toString());
						}

						item.add(new Label("realm", ugr.getRealm().getName()));
						item.add(new Label("accounts", accountList.toString()));

					}
				});
	}
}
