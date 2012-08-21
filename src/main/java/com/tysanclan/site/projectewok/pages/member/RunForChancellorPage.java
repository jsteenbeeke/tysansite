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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.Donation;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.DonationDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.DonationFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured( { Rank.CHANCELLOR, Rank.SENATOR, Rank.TRUTHSAYER,
		Rank.REVERED_MEMBER, Rank.SENIOR_MEMBER, Rank.FULL_MEMBER })
public class RunForChancellorPage extends AbstractMemberPage {
	@SpringBean
	private DemocracyService democracyService;

	@SpringBean
	private DonationDAO donationDAO;

	/**
	 * 
	 */
	public RunForChancellorPage() {
		super("Run for Chancellor");

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);

		int gained = getUser().getEndorsedBy().size();
		int requiredEndorsements = democracyService
				.getRequiredChancellorEndorsements();

		accordion.add(new Label("required", new Model<Integer>(
				requiredEndorsements)));

		accordion.add(new Label("gained", new Model<Integer>(gained)));

		List<User> endorsers = new LinkedList<User>();
		Set<User> endorsements = getUser().getEndorsedBy();
		endorsers.addAll(endorsements);
		Collections.sort(endorsers, new User.CaseInsensitiveUserComparator());

		accordion.add(new ListView<User>("endorsers", ModelMaker
				.wrap(endorsers)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<User> item) {
				item.add(new MemberListItem("user", item.getModelObject()));

			}

		});

		BigDecimal value = getDonatedAmount();

		accordion.add(new Label("donated", NumberFormat.getCurrencyInstance(
				Locale.US).format(value.doubleValue())));

		accordion.add(new Form<User>("runForChancellorForm", ModelMaker
				.wrap(getUser())) {
			private static final long serialVersionUID = 1L;

			/**
			* @see org.apache.wicket.markup.html.form.Form#onSubmit()
			*/
			@Override
			protected void onSubmit() {
				User user = getModelObject();

				if (MemberUtil.isEligibleForElectedRank(user, Rank.CHANCELLOR)) {
					if (democracyService.addChancellorCandidate(user)) {
						setResponsePage(new ChancellorElectionPage(
								democracyService.getCurrentChancellorElection()));
					} else {
						error("Failed to finalize candidacy");
					}
				}
			}

		}.setVisible(democracyService.isEligibleChancellorCandidate(getUser())));

		add(accordion);

	}

	/**
	 	 */
	private BigDecimal getDonatedAmount() {
		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.MONTH, -6);

		DonationFilter filter = new DonationFilter();
		filter.setFrom(cal.getTime());
		filter.setDonator(getUser());

		BigDecimal value = BigDecimal.ZERO;
		List<Donation> donations = donationDAO.findByFilter(filter);
		for (Donation donation : donations) {
			value = value.add(donation.getAmount());
		}
		return value;
	}
}
