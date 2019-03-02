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
package com.tysanclan.site.projectewok.pages.member.justice;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.AchievementService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.AchievementIcon;
import com.tysanclan.site.projectewok.entities.dao.AchievementIconDAO;
import com.tysanclan.site.projectewok.entities.filter.AchievementIconFilter;
import com.tysanclan.site.projectewok.pages.member.AbstractSingleAccordionMemberPage;
import com.tysanclan.site.projectewok.util.ImageUtil;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(value = Rank.TRUTHSAYER)
public class AchievementIconApprovalPage
		extends AbstractSingleAccordionMemberPage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private AchievementIconDAO achievementIconDAO;

	@SpringBean
	private AchievementService achievementService;

	public AchievementIconApprovalPage() {
		super("Achievement Icons");

		add(new ListView<AchievementIcon>("pendingIcons",
				ModelMaker.wrap(getPendingIcons())) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<AchievementIcon> item) {
				AchievementIcon icon = item.getModelObject();
				item.add(new Image("icon", new ByteArrayResource(
						ImageUtil.getMimeType(icon.getImage()),
						icon.getImage())));
				item.add(new Label("purpose",
						item.getModelObject().getPurpose()));
				item.add(new MemberListItem("creator",
						item.getModelObject().getCreator()));
				item.add(new IconLink.Builder("images/icons/tick.png",
						new DefaultClickResponder<AchievementIcon>() {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								achievementService.approveIcon(getUser(),
										item.getModelObject());

								setResponsePage(
										new AchievementIconApprovalPage());

							}
						}).newInstance("yes"));
				item.add(new IconLink.Builder("images/icons/cross.png",
						new DefaultClickResponder<AchievementIcon>() {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								achievementService.rejectIcon(getUser(),
										item.getModelObject());

								setResponsePage(
										new AchievementIconApprovalPage());

							}
						}).newInstance("no"));

			}

		});
	}

	private List<AchievementIcon> getPendingIcons() {
		AchievementIconFilter filter = new AchievementIconFilter();
		filter.approved().isNull();

		return achievementIconDAO.findByFilter(filter).asJava();
	}
}
