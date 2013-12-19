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
package com.tysanclan.site.projectewok.entities.dao.filters;

import java.util.Date;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.AchievementProposal;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class AchievementProposalFilter extends
		SearchFilter<AchievementProposal> {
	private static final long serialVersionUID = 1L;

	private IModel<User> suggestor = new Model<User>();

	private Date startsBefore;

	private Boolean truthsayerReviewed;

	private boolean vetoUndecided = false;

	public User getSuggestor() {
		return suggestor.getObject();
	}

	public void setSuggestor(User suggestor) {
		this.suggestor = ModelMaker.wrap(suggestor);
	}

	public Date getStartsBefore() {
		return startsBefore;
	}

	public void setStartsBefore(Date startsBefore) {
		this.startsBefore = startsBefore;
	}

	@Override
	public void detach() {
		super.detach();
		suggestor.detach();
	}

	public Boolean getTruthsayerReviewed() {
		return truthsayerReviewed;
	}

	public void setTruthsayerReviewed(Boolean truthsayerReviewed) {
		this.truthsayerReviewed = truthsayerReviewed;
	}

	public boolean isVetoUndecided() {
		return vetoUndecided;
	}

	public void setVetoUndecided(boolean vetoUndecided) {
		this.vetoUndecided = vetoUndecided;
	}

}
