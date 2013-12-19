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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.beans.HumorService;
import com.tysanclan.site.projectewok.entities.GlobalSetting.GlobalSettings;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.GlobalSettingDAO;
import com.tysanclan.site.projectewok.entities.dao.OtterSightingDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.OtterSightingFilter;

/**
 * @author Ties
 */
public class OtterSniperPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private String[] otters = new String[] { "seaotter1.jpg", "seaotter2.jpg",
			"seaotter3.jpg", "seaotter4.jpg", "seaotter1.jpg" };

	@SpringBean
	private OtterSightingDAO otterSightDao;

	@SpringBean
	private GlobalSettingDAO globalSetting;

	@SpringBean
	private HumorService humorService;

	private int otterId;

	private WebMarkupContainer livingGnomeContainer;

	private WebMarkupContainer deadGnomeContainer;

	public OtterSniperPanel(String id, int otterNumber) {
		super(id);
		this.otterId = otterNumber;

		if (otterNumber > otters.length) {
			otterId = 0;
		} else if (otterNumber < 0) {
			otterId = 0;
		} else {
			otterId = otterNumber;
		}

		User currentUser = null;
		if (TysanSession.get() != null) {
			currentUser = TysanSession.get().getUser();
		}

		OtterSightingFilter filter = new OtterSightingFilter();
		filter.setUser(currentUser);
		filter.setOtterNumber(otterNumber);

		boolean hasSighting = otterSightDao.countByFilter(filter) > 0;

		AjaxLink<User> otterLink = new AjaxLink<User>("otterLink",
				ModelMaker.wrap(currentUser)) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				User user = getModelObject();

				if (user != null) {
					humorService.otterSighted(TysanSession.get().getUser(),
							otterId);
					livingGnomeContainer.setVisible(false);
					deadGnomeContainer.setVisible(true);
					target.add(deadGnomeContainer);
					target.add(livingGnomeContainer);
				}
			}
		};

		otterLink.add(new ContextImage("gnome", "images/blah/"
				+ otters[otterId]));

		livingGnomeContainer = new WebMarkupContainer("livingGnomeContainer");
		livingGnomeContainer.setOutputMarkupPlaceholderTag(true);
		livingGnomeContainer.add(otterLink);
		livingGnomeContainer.setVisible(currentUser != null && !hasSighting);

		deadGnomeContainer = new WebMarkupContainer("deadGnomeContainer");
		deadGnomeContainer.setOutputMarkupPlaceholderTag(true);
		deadGnomeContainer.add(new ContextImage("otter",
				"images/blah/welldonesir.png"));
		deadGnomeContainer.setVisible(false);
		add(livingGnomeContainer);
		add(deadGnomeContainer);

		this.setVisible(globalSetting.getGlobalSetting(GlobalSettings.BLAH)
				.getValue().equals("allhailblah"));
	}
}
