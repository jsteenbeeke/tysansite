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
package com.tysanclan.site.projectewok.components;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.entities.Donation;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.DonationDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.DonationFilter;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class DonatorPanel extends Panel {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private DonationDAO donationDAO;

	public DonatorPanel(String id, User user) {
		super(id);

		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.MONTH, -6);

		DonationFilter filter = new DonationFilter();
		filter.setFrom(cal.getTime());
		filter.setDonator(user);

		BigDecimal value = BigDecimal.ZERO;
		List<Donation> donations = donationDAO.findByFilter(filter);
		for (Donation donation : donations) {
			value = value.add(donation.getAmount());
		}

		List<BigDecimal> dollarsigns = new LinkedList<BigDecimal>();

		int max = Math.min(value.intValue(), 100);

		for (int i = 0; i < max; i += 20) {
			dollarsigns.add(value);
		}

		add(new ListView<BigDecimal>("dollars", dollarsigns) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<BigDecimal> item) {
				BigDecimal bdValue = item.getModelObject();

				String dollarLabel = NumberFormat
						.getCurrencyInstance(Locale.US).format(
								bdValue.doubleValue())
						+ " donated in the last 6 months";

				item.add(new ContextImage("dollar",
						"images/icons/money_dollar.png").add(
						AttributeModifier.replace("alt", dollarLabel)).add(
						AttributeModifier.replace("title", dollarLabel)));
			}

		});

		setVisible(user != null && !dollarsigns.isEmpty());

	}

}
