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
package com.tysanclan.site.projectewok.pages.member;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.AchievementService;
import com.tysanclan.site.projectewok.components.StoredImageResource;
import com.tysanclan.site.projectewok.entities.AchievementIcon;
import com.tysanclan.site.projectewok.entities.AchievementProposal;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.AchievementIconDAO;
import com.tysanclan.site.projectewok.entities.dao.AchievementProposalDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.AchievementIconFilter;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured({ Rank.CHANCELLOR, Rank.SENATOR, Rank.TRUTHSAYER,
		Rank.REVERED_MEMBER, Rank.SENIOR_MEMBER, Rank.FULL_MEMBER,
		Rank.JUNIOR_MEMBER })
public class ProposeAchievementPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private AchievementProposalDAO achievementProposalDAO;

	@SpringBean
	private AchievementService achievementService;

	@SpringBean
	private AchievementIconDAO achievementIconDAO;

	public ProposeAchievementPage() {
		this(0);
	}

	private final int tabIndex;

	public ProposeAchievementPage(int tabIndex) {
		super("Propose Achievement");

		this.tabIndex = tabIndex;

		add(new ListView<AchievementProposal>("proposals",
				ModelMaker.wrap(achievementProposalDAO.findAll())) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<AchievementProposal> item) {
				AchievementProposal proposal = item.getModelObject();

				item.add(new Image("icon", new StoredImageResource(proposal
						.getIcon().getImage())));

				item.add(new Label("name", proposal.getName()));
				item.add(new Label("description", proposal.getDescription())
						.setEscapeModelStrings(false));
				item.add(new Label("game",
						proposal.getGame() != null ? proposal.getGame()
								.getName() : "-"));
				item.add(new Label("group",
						proposal.getGroup() != null ? proposal.getGroup()
								.getName() : "-"));
			}

		});

		List<AchievementIcon> icons = achievementService
				.getAvailableIcons(getUser());

		add(getIconListview("icons", icons, new IconClickResponder() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AchievementIcon icon) {
				setResponsePage(new ProposeAchievementPage2(icon));
			}
		}));

		add(new WebMarkupContainer("noIcons").setVisible(icons.isEmpty()));

		add(getIconListview("approved", getApprovedIcons(getUser()), null));
		add(getIconListview("pending", getPendingIcons(getUser()), null));

		add(getIconListview("rejected", getRejectedIcons(getUser()),
				new IconClickResponder() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AchievementIcon icon) {
						achievementService.deleteIcon(getUser(), icon);

						setResponsePage(new ProposeAchievementPage(2));
					}
				}));

		final FileUploadField uploadField = new FileUploadField("file",
				new ListModel<FileUpload>());
		uploadField.setRequired(true);

		final TextField<String> purposeField = new TextField<String>("purpose",
				new Model<String>(""));
		purposeField.setRequired(true);

		final CheckBox privateIconBox = new CheckBox("private",
				new Model<Boolean>(true));

		Form<AchievementIcon> uploadForm = new Form<AchievementIcon>(
				"uploadIcon") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				List<FileUpload> uploads = uploadField.getModelObject();
				String purpose = purposeField.getModelObject();
				Boolean privateIcon = privateIconBox.getModelObject();

				if (uploads != null && !uploads.isEmpty()) {
					FileUpload upload = uploads.get(0);

					AchievementIcon icon = achievementService
							.uploadAchievementIcon(getUser(),
									upload.getBytes(), purpose, privateIcon);

					if (icon != null) {
						setResponsePage(new ProposeAchievementPage(2));
					} else {
						error("Invalid image type, or not a 48x48 pixel image");
					}
				} else {
					error("No valid file uploaded");
				}

			}

		};

		uploadForm.setMultiPart(true);

		uploadForm.add(uploadField);
		uploadForm.add(purposeField);
		uploadForm.add(privateIconBox);

		add(uploadForm);

	}

	@Override
	protected Integer getAutoTabIndex() {
		return tabIndex;
	}

	private List<AchievementIcon> getRejectedIcons(User user) {
		AchievementIconFilter filter = new AchievementIconFilter();

		filter.setCreator(user);
		filter.setApproved(false);

		return achievementIconDAO.findByFilter(filter);
	}

	private List<AchievementIcon> getApprovedIcons(User user) {
		AchievementIconFilter filter = new AchievementIconFilter();

		filter.setCreator(user);
		filter.setApproved(true);

		return achievementIconDAO.findByFilter(filter);
	}

	private List<AchievementIcon> getPendingIcons(User user) {
		AchievementIconFilter filter = new AchievementIconFilter();

		filter.setCreator(user);
		filter.setApprovedAsNull(true);

		return achievementIconDAO.findByFilter(filter);
	}

	private ListView<List<Long>> getIconListview(String id,
			List<AchievementIcon> icons, final IconClickResponder responder) {
		List<List<Long>> pagedList = new ArrayList<List<Long>>(
				1 + icons.size() / 5);

		for (int i = 0; i < icons.size(); i += 5) {

			int remaining = icons.size() - i;

			int limit = icons.size();

			if (remaining > 5) {
				limit = i + 5;
			}

			List<Long> next = new ArrayList<Long>(limit - i);

			for (int j = i; j < limit; j++) {
				next.add(icons.get(j).getId());
			}

			pagedList.add(next);
		}

		return new ListView<List<Long>>(id, pagedList) {

			private static final long serialVersionUID = 1L;

			@SpringBean
			private AchievementIconDAO iconDAO;

			@Override
			protected void populateItem(final ListItem<List<Long>> item) {
				item.add(new ListView<Long>("sublist", item.getModelObject()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final ListItem<Long> innerItem) {
						AchievementIcon icon = iconDAO.load(innerItem
								.getModelObject());

						Link<AchievementIcon> iconLink = new Link<AchievementIcon>(
								"iconLink", ModelMaker.wrap(icon)) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								if (responder != null) {
									responder.onClick(getModelObject());
								}
							}
						};

						iconLink.setEnabled(responder != null);
						iconLink.setRenderBodyOnly(responder == null);

						iconLink.add(new Image("icon", new StoredImageResource(
								icon.getImage())));

						innerItem.add(iconLink);
					}
				});

			}

		};
	}

	private static interface IconClickResponder extends Serializable {
		void onClick(AchievementIcon icon);
	}
}
