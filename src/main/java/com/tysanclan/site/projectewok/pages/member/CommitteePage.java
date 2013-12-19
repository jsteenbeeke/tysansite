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

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import wicket.contrib.tinymce.TinyMceBehavior;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.beans.LogService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.Builder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.components.TysanTinyMCESettings;
import com.tysanclan.site.projectewok.entities.Committee;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.CommitteeDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.CommitteeFilter;
import com.tysanclan.site.projectewok.pages.GroupPage;
import com.tysanclan.site.projectewok.pages.member.group.DisbandGroupPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class CommitteePage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserService userService;

	@SpringBean
	private LogService logService;

	@SpringBean
	private CommitteeDAO committeeDAO;

	/**
	 * 
	 */
	public CommitteePage() {
		super("Committee Management");

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);
		accordion.getOptions().put("heightStyle", "'content'");

		Form<Committee> createForm = new Form<Committee>("committeeform") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private GroupService groupService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TextArea<String> descriptionArea = (TextArea<String>) get("description");
				TextField<String> nameField = (TextField<String>) get("name");
				DropDownChoice<User> userChoice = (DropDownChoice<User>) get("leader");

				String description = descriptionArea.getModelObject();
				String name = nameField.getModelObject();
				User leader = userChoice.getModelObject();

				if (name == null || name.isEmpty()) {
					error("Name may not be empty");
				} else if (description == null || description.isEmpty()) {
					error("Description may not be empty");
				} else if (leader == null) {
					error("You must select a chairman for this committee");
				} else {
					Committee committee = groupService.createCommittee(name,
							description);
					groupService.setGroupLeader(leader, committee);
					groupService.addUserToGroup(leader, committee);
					logService.logUserAction(getUser(), "Groups",
							"Committee created: " + name);

					setResponsePage(new GroupPage(committee));
				}
			}

		};

		List<User> users = userService.getMembers();
		createForm
				.add(new DropDownChoice<User>("leader", ModelMaker.wrap(
						(User) null, true), ModelMaker.wrap(users))
						.setNullValid(false));

		createForm.add(new TextArea<String>("description",
				new Model<String>("")).add(new TinyMceBehavior(
				new TysanTinyMCESettings())));
		createForm.add(new TextField<String>("name", new Model<String>("")));

		accordion.add(createForm);

		CommitteeFilter filter = new CommitteeFilter();
		filter.addOrderBy("name", true);

		accordion.add(new ListView<Committee>("committees", ModelMaker
				.wrap(committeeDAO.findByFilter(filter))) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Committee> item) {
				Committee committee = item.getModelObject();

				item.add(new Label("name", committee.getName()));
				item.add(new MemberListItem("chairman", committee.getLeader()));
				IconLink.Builder builder = new Builder(
						"images/icons/delete.png", new DisbandResponder(
								committee));
				item.add(builder.newInstance("disband"));

			}

			class DisbandResponder extends
					IconLink.DefaultClickResponder<Committee> {
				private static final long serialVersionUID = 1L;

				/**
				 * 
				 */
				public DisbandResponder(Committee committee) {
					super(ModelMaker.wrap(committee));
				}

				/**
				 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
				 */
				@Override
				public void onClick() {
					setResponsePage(new DisbandGroupPage(getModelObject()));
				}
			}

		});

		add(accordion);
	}
}
