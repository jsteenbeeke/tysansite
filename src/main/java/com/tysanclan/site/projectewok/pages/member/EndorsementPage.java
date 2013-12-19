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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.UserFilter;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class EndorsementPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDAO userDAO;

	/**
	 * 
	 */
	public EndorsementPage() {
		super("Endorsements");

		Set<User> endorsements = getUser().getEndorsedBy();
		Set<User> endorsements2 = getUser().getEndorsedForSenateBy();

		add(new WebMarkupContainer("nousers")
				.setVisible(endorsements.isEmpty()));

		add(new WebMarkupContainer("nousers3").setVisible(endorsements2
				.isEmpty()));

		List<User> endorsers = new ArrayList<User>();
		endorsers.addAll(endorsements);

		List<User> endorsers2 = new ArrayList<User>();
		endorsers2.addAll(endorsements2);

		Collections.sort(endorsers, new Comparator<User>() {
			@Override
			public int compare(User u1, User u2) {
				return u1.getUsername().compareTo(u2.getUsername());
			}

		});

		Collections.sort(endorsers2, new Comparator<User>() {
			@Override
			public int compare(User u1, User u2) {
				return u1.getUsername().compareTo(u2.getUsername());
			}

		});

		add(new ListView<User>("users", ModelMaker.wrap(endorsers)) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<User> item) {
				item.add(new MemberListItem("user", item.getModelObject()));
			}

		}.setVisible(!endorsements.isEmpty()));

		add(new ListView<User>("users2", ModelMaker.wrap(endorsers2)) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<User> item) {
				item.add(new MemberListItem("user", item.getModelObject()));
			}

		}.setVisible(!endorsements2.isEmpty()));

		User endorses = getUser().getEndorses();
		String endorsesName = endorses == null ? "Nobody" : endorses
				.getUsername();

		add(new WebMarkupContainer("nousers2").setVisible(endorses == null));
		WebMarkupContainer container = new WebMarkupContainer("endorsed");
		container.add(new Label("user", endorsesName));
		container.setVisible(endorses != null);

		add(container);

		Form<User> endorsementForm = new Form<User>("endorseform") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private DemocracyService democracyService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				DropDownChoice<User> userSelect = (DropDownChoice<User>) get("targetUser");
				User target = userSelect.getModelObject();
				User endorser = getUser();

				if (democracyService.isElectionCandidate(getUser()
						.getEndorses())) {
					error("You are currently endorsing a running candidate,"
							+ " you must wait until after elections before you can change your endorsement");
				} else {
					democracyService.endorseUserForChancellor(endorser, target);

					setResponsePage(new EndorsementPage());
				}
			}

		};

		UserFilter filter = new UserFilter();
		filter.addRank(Rank.CHANCELLOR);
		filter.addRank(Rank.SENATOR);
		filter.addRank(Rank.TRUTHSAYER);
		filter.addRank(Rank.REVERED_MEMBER);
		filter.addRank(Rank.SENIOR_MEMBER);
		filter.addRank(Rank.FULL_MEMBER);
		filter.setRetired(false);
		filter.addOrderBy("username", true);

		List<User> eligible = userDAO.findByFilter(filter);
		eligible.remove(getUser());

		endorsementForm.add(new DropDownChoice<User>("targetUser", ModelMaker
				.wrap(endorses), ModelMaker.wrap(eligible)).setNullValid(true));

		add(endorsementForm);

		Form<User> endorsementForm2 = new Form<User>("endorseform2") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private DemocracyService democracyService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				DropDownChoice<User> userSelect = (DropDownChoice<User>) get("targetUser");
				User target = userSelect.getModelObject();
				User endorser = getUser();

				if (democracyService.isElectionCandidate(getUser()
						.getEndorsesForSenate())) {
					error("You are currently endorsing a running candidate,"
							+ " you must wait until after elections before you can change your endorsement");
				} else {
					democracyService.endorseUserForSenate(endorser, target);

					setResponsePage(new EndorsementPage());
				}
			}

		};

		UserFilter filter2 = new UserFilter();
		filter2.addRank(Rank.CHANCELLOR);
		filter2.addRank(Rank.SENATOR);
		filter2.addRank(Rank.TRUTHSAYER);
		filter2.addRank(Rank.REVERED_MEMBER);
		filter2.addRank(Rank.SENIOR_MEMBER);
		filter2.addRank(Rank.FULL_MEMBER);
		filter2.setRetired(false);

		List<User> eligible2 = userDAO.findByFilter(filter2);
		eligible2.remove(getUser());

		User endorsesForSenate = getUser().getEndorsesForSenate();
		String endorsesForSenateName = endorsesForSenate == null ? "Nobody"
				: endorsesForSenate.getUsername();

		endorsementForm2.add(new DropDownChoice<User>("targetUser", ModelMaker
				.wrap(endorsesForSenate), ModelMaker.wrap(eligible))
				.setNullValid(true));

		add(endorsementForm2);

		add(new WebMarkupContainer("nousers4")
				.setVisible(endorsesForSenate == null));
		WebMarkupContainer container2 = new WebMarkupContainer("endorsed2");
		container2.add(new Label("user", endorsesForSenateName));
		container2.setVisible(endorsesForSenate != null);

		add(container2);
	}
}
