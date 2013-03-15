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
package com.tysanclan.site.projectewok.pages.member.senate;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.LawEnforcementService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.TruthsayerComplaint;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.TruthsayerComplaintDAO;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;

@TysanRankSecured(Rank.SENATOR)
public class SenateTruthsayerComplaintPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	public class ComplaintResponder extends
			DefaultClickResponder<TruthsayerComplaint> {

		private static final long serialVersionUID = 1L;

		private final boolean inFavor;

		private IModel<User> userModel;

		@SpringBean
		private LawEnforcementService lawEnforcementService;

		public ComplaintResponder(TruthsayerComplaint complaint, User user,
				boolean inFavor) {
			super(ModelMaker.wrap(complaint));
			this.inFavor = inFavor;
			this.userModel = ModelMaker.wrap(user);
		}

		@Override
		public void onClick() {
			lawEnforcementService.passComplaintVote(getModelObject(),
					userModel.getObject(), inFavor);

			setResponsePage(new SenateTruthsayerComplaintPage());
		}

		@Override
		protected void onDetach() {
			super.onDetach();
			userModel.detach();
		}

	}

	@SpringBean
	private TruthsayerComplaintDAO complaintDAO;

	public SenateTruthsayerComplaintPage() {
		super("Truthsayer Complaints");

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);

		accordion.add(new ListView<TruthsayerComplaint>("complaints",
				ModelMaker.wrap(complaintDAO.findAll())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<TruthsayerComplaint> item) {
				TruthsayerComplaint c = item.getModelObject();

				item.add(new MemberListItem("complainer", c.getComplainer()));
				item.add(new MemberListItem("truthsayer", c.getTruthsayer()));

				item.add(new Label("title", "Complaint against "
						+ c.getTruthsayer().getUsername()));
				item.add(new Label("complaint", c.getComplaint())
						.setEscapeModelStrings(false));

				boolean hasVoted = c.hasVoted(getUser());

				item.add(new IconLink.Builder("images/icons/tick.png",
						new ComplaintResponder(c, getUser(), true))
						.setText(
								"Yes, I feel this complaint is justified, and warrants stripping the rank of Truthsayer")
						.newInstance("yes").setVisible(!hasVoted));
				item.add(new IconLink.Builder("images/icons/cross.png",
						new ComplaintResponder(c, getUser(), false))
						.setText(
								"No, I feel this complaint is without merit, and does not warrant a dismissal")
						.newInstance("no").setVisible(!hasVoted));
			}
		});

		add(accordion);

	}
}
