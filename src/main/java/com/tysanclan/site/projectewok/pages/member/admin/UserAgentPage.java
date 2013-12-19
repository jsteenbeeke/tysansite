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
package com.tysanclan.site.projectewok.pages.member.admin;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.FilterDataProvider;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.beans.UserAgentService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.MobileUserAgent;
import com.tysanclan.site.projectewok.entities.dao.MobileUserAgentDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.MobileUserAgentFilter;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractSingleAccordionMemberPage;

/**
 * @author Jeroen Steenbeeke
 */
public class UserAgentPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	@SpringBean
	private UserAgentService userAgentService;

	@SpringBean
	private MobileUserAgentDAO userAgentDAO;

	public UserAgentPage() {
		super("User agents");

		if (!getUser().equals(roleService.getSteward())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		MobileUserAgentFilter filter = new MobileUserAgentFilter();
		filter.addOrderBy("identifier", true);

		add(
				new DataView<MobileUserAgent>("agents", FilterDataProvider.of(
						filter, userAgentDAO)) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final Item<MobileUserAgent> item) {
						MobileUserAgent agent = item.getModelObject();

						item.add(new Label("identifier", agent.getIdentifier()));

						String curr = "Unknown";

						if (agent.getMobile() != null) {
							if (agent.getMobile()) {
								curr = "Mobile";
							} else {
								curr = "Normal";
							}
						}

						item.add(new Label("current", curr));

						item.add(new IconLink.Builder("images/icons/phone.png",
								new DefaultClickResponder<MobileUserAgent>(
										ModelMaker.wrap(agent)) {
									private static final long serialVersionUID = 1L;

									@Override
									public void onClick() {
										userAgentService.setAgentStatus(
												getModelObject(), true);

										setResponsePage(new UserAgentPage());
									}

								}).newInstance("yes")
								.setVisible(
										agent.getMobile() == null
												|| !agent.getMobile()));
						item.add(new IconLink.Builder(
								"images/icons/computer.png",
								new DefaultClickResponder<MobileUserAgent>(
										ModelMaker.wrap(agent)) {
									private static final long serialVersionUID = 1L;

									@Override
									public void onClick() {
										userAgentService.setAgentStatus(
												getModelObject(), false);

										setResponsePage(new UserAgentPage());

										setResponsePage(new UserAgentPage());
									}

								}).newInstance("no").setVisible(
								agent.getMobile() == null || agent.getMobile()));
					}

				});
	}
}
