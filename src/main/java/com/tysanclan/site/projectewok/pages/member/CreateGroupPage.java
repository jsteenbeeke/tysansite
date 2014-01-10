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

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.components.BBCodeTextArea;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.GroupCreationRequest;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.dao.GroupCreationRequestDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.GroupCreationRequestFilter;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured({ Rank.CHANCELLOR, Rank.FULL_MEMBER, Rank.REVERED_MEMBER,
		Rank.SENATOR, Rank.TRUTHSAYER, Rank.SENIOR_MEMBER })
public class CreateGroupPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private GameService gameService;

	@SpringBean
	private GroupCreationRequestDAO groupCreationRequestDAO;

	/**
	 * 
	 */
	public CreateGroupPage() {
		super("Create Group");

		add(createGamingGroupForm());
		add(createSocialGroupForm());
		addPendingRequests();
	}

	private void addPendingRequests() {
		GroupCreationRequestFilter filter = new GroupCreationRequestFilter();
		filter.setRequester(getUser());

		List<GroupCreationRequest> requests = groupCreationRequestDAO
				.findByFilter(filter);

		String intro = (requests.size() == 0) ? "You have no pending group creation requests"
				: "You have " + requests.size()
						+ " pending group creation requests:";
		add(new Label("pendingtext", intro));
		add(new ListView<GroupCreationRequest>("requests",
				ModelMaker.wrap(requests)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<GroupCreationRequest> item) {
				GroupCreationRequest request = item.getModelObject();
				item.add(new Label("name", request.getName()));
				item.add(new Label("type",
						request.getGame() == null ? "Social Group"
								: "Gaming Group"));
				item.add(new Label("description", request.getDescription())
						.setEscapeModelStrings(false));
				item.add(new Label("motivation", request.getMotivation())
						.setEscapeModelStrings(false));

			}

		}.setVisible(requests.size() > 0));

	}

	/**
	 	 */
	private Form<Group> createSocialGroupForm() {
		Form<Group> socialGroupForm = new Form<Group>("createsocialgroupform") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private GroupService groupService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TextField<String> nameField = (TextField<String>) get("name");
				TextArea<String> descriptionArea = (TextArea<String>) get("publicdescription");
				TextArea<String> motivationArea = (TextArea<String>) get("motivation");

				String name = nameField.getModelObject();
				String description = descriptionArea.getModelObject();
				String motivation = motivationArea.getModelObject();

				groupService.createSocialGroupRequest(getUser(), name,
						description, motivation);

				setResponsePage(new CreateGroupPage());
			}

		};

		socialGroupForm
				.add(new TextField<String>("name", new Model<String>(""))
						.setRequired(true));

		socialGroupForm.add(new BBCodeTextArea("publicdescription", "")
				.setRequired(true));
		socialGroupForm.add(new BBCodeTextArea("motivation", "")
				.setRequired(true));

		return socialGroupForm;
	}

	/**
	 	 */
	private Form<Group> createGamingGroupForm() {
		Form<Group> gamingGroupForm = new Form<Group>("creategaminggroupform") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private GroupService groupService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TextField<String> nameField = (TextField<String>) get("name");
				TextArea<String> descriptionArea = (TextArea<String>) get("publicdescription");
				TextArea<String> motivationArea = (TextArea<String>) get("motivation");
				DropDownChoice<GameRealm> gameGroup = (DropDownChoice<GameRealm>) get("gamerealm");

				String name = nameField.getModelObject();
				String description = descriptionArea.getModelObject();
				String motivation = motivationArea.getModelObject();
				GameRealm gameRealm = gameGroup.getModelObject();
				Game game = gameRealm.getGame();
				Realm realm = gameRealm.getRealm();

				if (name.isEmpty()) {
					error("Name must not be empty");
				} else if (description.isEmpty()) {
					error("Description must not be empty");
				} else if (motivation.isEmpty()) {
					error("Motivation must not be empty");
				} else {
					groupService.createGamingGroupRequest(getUser(), game,
							realm, name, description, motivation);

					setResponsePage(new CreateGroupPage());
				}
			}

		};

		List<Game> games = gameService.getActiveGames();

		List<GameRealm> grlms = new LinkedList<GameRealm>();

		if (games.isEmpty()) {
			gamingGroupForm.setEnabled(false);
			gamingGroupForm
					.warn("No games available! Cannot create gaming group!");
		}

		for (Game game : games) {
			for (Realm realm : game.getRealms()) {
				grlms.add(new GameRealm(game, realm));
			}
		}

		if (grlms.isEmpty()) {
			gamingGroupForm.setEnabled(false);
			gamingGroupForm
					.warn("No realms available! Cannot create gaming group!");
		}

		gamingGroupForm.add(new DropDownChoice<GameRealm>("gamerealm",
				new Model<GameRealm>(null), grlms, new GameRealmRenderer())
				.setRequired(true));

		gamingGroupForm
				.add(new TextField<String>("name", new Model<String>(""))
						.setRequired(true));

		gamingGroupForm.add(new BBCodeTextArea("publicdescription", "")
				.setRequired(true));
		gamingGroupForm.add(new BBCodeTextArea("motivation", "")
				.setRequired(true));

		return gamingGroupForm;
	}

	private static class GameRealm implements IDetachable {
		private static final long serialVersionUID = 1L;

		private IModel<Game> gameModel;
		private IModel<Realm> realmModel;

		/**
		 * 
		 */
		public GameRealm(Game game, Realm realm) {
			this.gameModel = ModelMaker.wrap(game);
			this.realmModel = ModelMaker.wrap(realm);
		}

		public Realm getRealm() {
			return realmModel.getObject();
		}

		public Game getGame() {
			return gameModel.getObject();
		}

		/**
		 * @see org.apache.wicket.model.IDetachable#detach()
		 */
		@Override
		public void detach() {
			gameModel.detach();
			realmModel.detach();

		}
	}

	private static class GameRealmRenderer implements
			IChoiceRenderer<GameRealm> {
		private static final long serialVersionUID = 1L;

		/**
		 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
		 */
		@Override
		public Object getDisplayValue(GameRealm object) {
			return object.getGame().getName() + " on "
					+ object.getRealm().getName();
		}

		/**
		 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object,
		 *      int)
		 */
		@Override
		public String getIdValue(GameRealm object, int index) {
			return Integer.toString(index);
		}
	}

}
