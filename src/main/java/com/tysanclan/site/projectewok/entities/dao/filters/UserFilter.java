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
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.MemberUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class UserFilter extends SearchFilter<User> {
	private static final long serialVersionUID = 1L;

	private IModel<Group> group = new Model<Group>();
	private String password;
	private String username;
	private String email;
	private Set<Rank> ranks;
	private Date activeSince;
	private Date activeBefore;
	private Boolean truthsayerNominated;
	private Boolean vacation;
	private IModel<Realm> realm = new Model<Realm>();
	private IModel<Game> game = new Model<Game>();
	private Boolean retired;

	private Boolean bugReportMaster;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the group
	 */
	public Group getGroup() {
		return group.getObject();
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(Group group) {
		this.group = ModelMaker.wrap(group);
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		if (MemberUtil.isHashedPassword(password)) {
			this.password = password;
		} else {
			this.password = MemberUtil.hashPassword(password);
		}
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the rank
	 */
	public Set<Rank> getRanks() {
		return ranks;
	}

	/**
	 * @param rank
	 *            the rank to set
	 */
	public void addRank(Rank rank) {
		if (ranks == null) {
			ranks = new HashSet<Rank>();
		}

		ranks.add(rank);
	}

	/**
	 * @return the activeSince
	 */
	public Date getActiveSince() {
		return activeSince;
	}

	/**
	 * @param activeSince
	 *            the activeSince to set
	 */
	public void setActiveSince(Date activeSince) {
		this.activeSince = activeSince;
	}

	/**
	 * @return the activeBefore
	 */
	public Date getActiveBefore() {
		return activeBefore;
	}

	/**
	 * @param activeBefore
	 *            the activeBefore to set
	 */
	public void setActiveBefore(Date activeBefore) {
		this.activeBefore = activeBefore;
	}

	/**
	 * @return the retired
	 */
	public Boolean getRetired() {
		return retired;
	}

	/**
	 * @param retired
	 *            the retired to set
	 */
	public void setRetired(Boolean retired) {
		this.retired = retired;
	}

	/**
	 * @return the truthsayerNominated
	 */
	public Boolean getTruthsayerNominated() {
		return truthsayerNominated;
	}

	/**
	 * @param truthsayerNominated
	 *            the truthsayerNominated to set
	 */
	public void setTruthsayerNominated(Boolean truthsayerNominated) {
		this.truthsayerNominated = truthsayerNominated;
	}

	/**
	 * @return the vacation
	 */
	public Boolean getVacation() {
		return vacation;
	}

	/**
	 * @param vacation
	 *            the vacation to set
	 */
	public void setVacation(Boolean vacation) {
		this.vacation = vacation;
	}

	public void setRealm(Realm realm) {
		this.realm = ModelMaker.wrap(realm);
	}

	public Realm getRealm() {
		return realm.getObject();
	}

	/**
	 * @return the game
	 */
	public Game getGame() {
		return game.getObject();
	}

	/**
	 * @param game
	 *            the game to set
	 */
	public void setGame(Game game) {
		this.game = ModelMaker.wrap(game);
	}

	@Override
	public void detach() {
		super.detach();
		group.detach();
		realm.detach();
		game.detach();

	}

	public Boolean getBugReportMaster() {
		return bugReportMaster;
	}

	public void setBugReportMaster(Boolean bugReportMaster) {
		this.bugReportMaster = bugReportMaster;

	}

}
