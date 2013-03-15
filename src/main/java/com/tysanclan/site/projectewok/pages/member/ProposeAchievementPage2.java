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

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import wicket.contrib.tinymce.TinyMceBehavior;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.AchievementService;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.components.StoredImageResource;
import com.tysanclan.site.projectewok.components.TysanTinyMCESettings;
import com.tysanclan.site.projectewok.entities.AchievementIcon;
import com.tysanclan.site.projectewok.entities.AchievementProposal;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Group;

/**
 * @author Jeroen Steenbeeke
 */
public class ProposeAchievementPage2 extends AbstractSingleAccordionMemberPage {

	private static final long serialVersionUID = 1L;

	private static enum AchievementType {
		NORMAL, GAME, GROUP;

		private final String description;

		private AchievementType() {
			this.description = new StringBuilder()
					.append(name().substring(0, 1).toUpperCase())
					.append(name().substring(1).toLowerCase()).toString();
		}

		@Override
		public String toString() {
			return description;
		}
	}

	private IModel<AchievementIcon> iconModel;

	@SpringBean
	private GameService gameService;

	public ProposeAchievementPage2(AchievementIcon icon) {
		super("Propose new achievement");

		iconModel = ModelMaker.wrap(icon);

		List<Game> games = gameService.getActiveGames();
		List<Group> groups = new LinkedList<Group>();

		for (Group group : getUser().getGroups()) {
			if (getUser().equals(group.getLeader())) {
				groups.add(group);
			}
		}

		List<AchievementType> types = new LinkedList<AchievementType>();
		types.add(AchievementType.NORMAL);
		if (!games.isEmpty())
			types.add(AchievementType.GAME);
		if (!groups.isEmpty())
			types.add(AchievementType.GROUP);

		final DropDownChoice<AchievementType> typeChoice = new DropDownChoice<AchievementType>(
				"type", new Model<AchievementType>(AchievementType.NORMAL),
				types);

		final WebMarkupContainer gameContainer = new WebMarkupContainer(
				"gameContainer");
		gameContainer.setVisible(false);
		gameContainer.setOutputMarkupPlaceholderTag(true);
		gameContainer.setOutputMarkupId(true);

		final WebMarkupContainer groupContainer = new WebMarkupContainer(
				"groupContainer");
		groupContainer.setVisible(false);
		groupContainer.setOutputMarkupPlaceholderTag(true);
		groupContainer.setOutputMarkupId(true);

		final DropDownChoice<Game> gameChoice = new DropDownChoice<Game>(
				"game", ModelMaker.wrap((Game) null), ModelMaker.wrap(games),
				new Game.Renderer());
		gameContainer.add(gameChoice);

		final DropDownChoice<Group> groupChoice = new DropDownChoice<Group>(
				"group", ModelMaker.wrap((Group) null), ModelMaker.wrap(groups));
		groupContainer.add(groupChoice);

		final TextField<String> nameField = new TextField<String>("name",
				new Model<String>(""));
		nameField.setRequired(true);

		final TextArea<String> descriptionArea = new TextArea<String>(
				"description", new Model<String>(""));
		descriptionArea.setRequired(true);
		descriptionArea.add(new TinyMceBehavior(new TysanTinyMCESettings()));

		typeChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				AchievementType type = typeChoice.getModelObject();

				if (target != null) {
					switch (type) {
						case NORMAL:
							gameContainer.setVisible(false);
							groupContainer.setVisible(false);

							gameChoice.setRequired(false);
							groupChoice.setRequired(false);

							gameChoice.setModelObject(null);
							groupChoice.setModelObject(null);
							target.add(gameContainer);
							target.add(groupContainer);

							break;
						case GAME:
							gameContainer.setVisible(true);
							groupContainer.setVisible(false);

							gameChoice.setRequired(true);
							groupChoice.setRequired(false);

							groupChoice.setModelObject(null);
							target.add(gameContainer);
							target.add(groupContainer);
							break;
						case GROUP:
							gameContainer.setVisible(false);
							groupContainer.setVisible(true);

							gameChoice.setRequired(false);
							groupChoice.setRequired(true);

							gameChoice.setModelObject(null);
							target.add(gameContainer);
							target.add(groupContainer);

							break;

					}
				}
			}

		});

		Form<AchievementProposal> proposalForm = new Form<AchievementProposal>(
				"proposalForm") {

			private static final long serialVersionUID = 1L;

			@SpringBean
			private AchievementService achievementService;

			@Override
			protected void onSubmit() {

				AchievementType type = typeChoice.getModelObject();

				Game game = gameChoice.getModelObject();
				Group group = groupChoice.getModelObject();

				switch (type) {
					case NORMAL:
						game = null;
						group = null;
						break;
					case GAME:
						if (game == null) {
							error("You need to select a game");
							return;
						}
						group = null;
						break;
					case GROUP:
						if (group == null) {
							error("You need to select a group");
						}
						game = null;
						break;
				}

				AchievementProposal proposal = achievementService
						.createProposal(getUser(), nameField.getModelObject(),
								descriptionArea.getModelObject(),
								iconModel.getObject(), game, group);

				if (proposal == null) {
					error("Unable to create proposal");
					return;
				}

				setResponsePage(new ProposeAchievementPage(0));
			}

		};

		proposalForm.add(new Image("icon", new StoredImageResource(icon
				.getImage())));

		proposalForm.add(typeChoice);
		proposalForm.add(groupContainer);
		proposalForm.add(gameContainer);
		proposalForm.add(nameField);
		proposalForm.add(descriptionArea);

		getAccordion().add(proposalForm);

	}

	@Override
	protected void onDetach() {

		super.onDetach();
		iconModel.detach();
	}
}
