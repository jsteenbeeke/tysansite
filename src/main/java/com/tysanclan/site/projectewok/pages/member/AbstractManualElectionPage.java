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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.entities.Election;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;

/**
 * Non-JQuery alternative to the election page
 * 
 * @author Jeroen Steenbeeke
 */
public abstract class AbstractManualElectionPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	private List<Long> preferences;

	/**
	 * 
	 */
	public AbstractManualElectionPage(String title, Election election) {
		super(title);

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);
		accordion.getOptions().put("heightStyle", "content");

		accordion.add(new ContextImage("chrome", "images/browser/chrome.jpg"));
		accordion
				.add(new ContextImage("firefox", "images/browser/firefox.png"));
		accordion.add(new ContextImage("opera", "images/browser/opera.jpg"));

		final boolean isNominationOpen = election.isNominationOpen();
		final int totalSize = election.getCandidates().size();

		accordion.add(new Label("label", isNominationOpen ? "Candidates"
				: "Cast your vote!"));

		preferences = new LinkedList<Long>();

		Form<Election> voteForm = new Form<Election>("vote",
				ModelMaker.wrap(election)) {

			private static final long serialVersionUID = 1L;

			@SpringBean
			private UserDAO userDAO;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				DropDownChoice<User> userChoice = (DropDownChoice<User>) get("candidate");
				Label positionLabel = (Label) get("position");

				preferences.add(userChoice.getModelObject().getId());

				if (preferences.size() == totalSize) {
					List<User> users = new LinkedList<User>();

					for (Long id : preferences) {
						users.add(userDAO.load(id));
					}

					onVoteSubmit(users);
				} else {
					List<User> remaining = new LinkedList<User>();
					remaining.addAll(userChoice.getChoices());
					remaining.remove(userChoice.getModelObject());

					positionLabel.replaceWith(new Label("position",
							new Model<Integer>(preferences.size() + 1)));

					userChoice.setChoices(ModelMaker.wrapChoices(remaining));
					userChoice.setModel(ModelMaker.wrap((User) null, true));
				}

			}

		};

		List<User> candidates = new LinkedList<User>();
		candidates.addAll(election.getCandidates());
		Collections.sort(candidates, new Comparator<User>() {
			/**
			 * @see java.util.Comparator#compare(java.lang.Object,
			 *      java.lang.Object)
			 */
			@Override
			public int compare(User o1, User o2) {
				return o1.getUsername().compareToIgnoreCase(o2.getUsername());
			}
		});

		voteForm.add(new Label("position", new Model<Integer>(1)));

		voteForm.add(new DropDownChoice<User>("candidate", ModelMaker.wrap(
				(User) null, true), ModelMaker.wrapChoices(candidates)));

		accordion.add(voteForm);
		add(accordion);

	}

	public abstract void onVoteSubmit(List<User> userList);
}
