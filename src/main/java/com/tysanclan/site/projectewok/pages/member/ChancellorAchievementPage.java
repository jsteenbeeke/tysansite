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
package com.tysanclan.site.projectewok.pages.member;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.FilterDataProvider;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.AchievementService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.AchievementIcon;
import com.tysanclan.site.projectewok.entities.AchievementProposal;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.dao.AchievementProposalDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.AchievementProposalFilter;
import com.tysanclan.site.projectewok.util.ImageUtil;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class ChancellorAchievementPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private AchievementProposalDAO achievementProposalDAO;

	@SpringBean
	private AchievementService service;

	public ChancellorAchievementPage() {
		super("Pending achievement proposals");

		AchievementProposalFilter filter = new AchievementProposalFilter();
		filter.setVetoUndecided(true);

		Accordion acc = new Accordion("accordion");
		acc.setHeader(new AccordionHeader(new LiteralOption("h2")));
		acc.setAutoHeight(false);
		acc.getOptions().put("heightStyle", "content");

		acc.add(new DataView<AchievementProposal>("proposals",
				FilterDataProvider.of(filter, achievementProposalDAO)) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<AchievementProposal> item) {
				AchievementProposal proposal = item.getModelObject();
				AchievementIcon icon = proposal.getIcon();
				Game game = proposal.getGame();

				item.add(new Label("name", proposal.getName()));

				item.add(new Image("icon", new ByteArrayResource(ImageUtil
						.getMimeType(icon.getImage()), icon.getImage())));

				byte[] gameImage = game != null ? game.getImage() : new byte[0];

				item.add(new Image("game", new ByteArrayResource(ImageUtil
						.getMimeType(gameImage), gameImage))
						.setVisible(game != null));

				boolean hasGroup = proposal.getGroup() != null;

				item.add(new Label("group", hasGroup ? proposal.getGroup()
						.getName() : "-").setVisible(hasGroup));
				item.add(new Label("description", proposal.getDescription())
						.setEscapeModelStrings(false));

				item.add(new IconLink.Builder("images/icons/tick.png",
						new DefaultClickResponder<AchievementProposal>(
								ModelMaker.wrap(proposal)) {

							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								service.checkProposal(getUser(),
										getModelObject());
								setResponsePage(new ChancellorAchievementPage());
							}

						}).setText("Yes, this achievement is fine with me")
						.newInstance("yes"));
				item.add(new IconLink.Builder("images/icons/cross.png",
						new DefaultClickResponder<AchievementProposal>(
								ModelMaker.wrap(proposal)) {

							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								service.vetoProposal(getUser(),
										getModelObject());

								setResponsePage(new ChancellorAchievementPage());
							}

						}).setText(
						"No, this achievement would not be a good addition")
						.newInstance("no"));
			}

		});

		add(acc);
	}
}
