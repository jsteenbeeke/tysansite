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
package com.tysanclan.site.projectewok.beans;

import java.util.Collection;

import com.tysanclan.site.projectewok.entities.Regulation;
import com.tysanclan.site.projectewok.entities.Trial;
import com.tysanclan.site.projectewok.entities.Trial.Verdict;
import com.tysanclan.site.projectewok.entities.TruthsayerComplaint;
import com.tysanclan.site.projectewok.entities.TruthsayerNomination;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public interface LawEnforcementService {
	public TruthsayerNomination nominateTruthsayer(User nominator, User nominee);

	public void acceptTruthsayerNomination(TruthsayerNomination nomination);

	public void declineTruthsayerNomination(TruthsayerNomination nomination);

	public void voteInFavor(User senator, TruthsayerNomination nomination);

	public void voteAgainst(User senator, TruthsayerNomination nomination);

	public void resolveNomination(TruthsayerNomination nomination);

	public Trial startTrial(User accuser, User accused, String motivation,
			Collection<Regulation> regulations);

	public Trial confirmTrial(User truthsayer, Trial trial);

	public void dismissTrial(User truthsayer, Trial trial);

	public void passVerdict(Trial trial, Verdict verdict);

	public void checkPenaltyPoints();

	public void restrainAccused(Trial trial);

	public void unrestrainAccused(Trial trial);

	public void fileComplaint(User complainer, User truthsayer,
			String motivation);

	public void complaintMediated(TruthsayerComplaint complaint);

	public void complaintToSenate(TruthsayerComplaint complaint,
			boolean byChancellor);

	public void passComplaintVote(TruthsayerComplaint complaint, User senator,
			boolean inFavor);

	public void resolveComplaint(TruthsayerComplaint complaint);

	public void setComplaintObserved(TruthsayerComplaint c);
}
