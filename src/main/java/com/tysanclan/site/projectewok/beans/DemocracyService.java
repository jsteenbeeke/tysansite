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
package com.tysanclan.site.projectewok.beans;

import com.tysanclan.site.projectewok.entities.*;

import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
public interface DemocracyService {
	/**
	 * The percentage of votes in favor required to succesfully end the trial
	 * period
	 */
	public static final int MEMBERSHIP_ACCEPTANCE_VOTE_PERCENTAGE_REQUIRED = 60;

	/**
	 * The percentage of votes in favor required to be granted a trial period
	 */
	public static final int TRIAL_ACCEPTANCE_VOTE_PERCENTAGE_REQUIRED = 66;

	/**
	 * Creates a new senate election
	 *
	 * @return A new election
	 */
	SenateElection createSenateElection();

	/**
	 * Creates a new chancellor election
	 *
	 * @return A new election
	 */
	ChancellorElection createChancellorElection();

	/**
	 * Creates a new election for the leader of a group
	 *
	 * @param group
	 *            The group to elect a leader for
	 * @return The new election object
	 */
	GroupLeaderElection createGroupLeaderElection(Group group);

	/**
	 * @param election
	 *            The election to resolve
	 */
	void resolveGroupLeaderElection(GroupLeaderElection election);

	/**
	 * @param election
	 *            The election to resolve
	 */
	void resolveChancellorElection(ChancellorElection election);

	/**
	 * @param election
	 *            The election to resolve
	 */
	void resolveSenateElection(SenateElection election);

	void checkAcceptanceVote(User user);

	void resolveAcceptanceVote(AcceptanceVote vote);

	void resolveJoinApplication(JoinApplication application);

	void endorseUserForSenate(User endorser, User target);

	void endorseUserForChancellor(User endorser, User target);

	SenateElection getCurrentSenateElection();

	ChancellorElection getCurrentChancellorElection();

	boolean isElectionCandidate(User user);

	long getRequiredChancellorEndorsements();

	long getRequiredSenateEndorsements();

	boolean isEligibleChancellorCandidate(User user);

	boolean isEligibleSenateCandidate(User user);

	boolean addChancellorCandidate(User user);

	boolean addSenateCandidate(User user);

	boolean addGroupLeaderCandidate(User user, GroupLeaderElection election);

	public CompoundVote createVote(User caster, List<User> votesFor,
			Election election);

	void castAcceptanceVote(AcceptanceVote vote, User caster, boolean verdict);

	void createImpeachmentVote(User starter);

	void castImpeachmentVote(User voter, boolean inFavor);

	void resolveImpeachment();

	void stepDownAsSenator(User senator);

	void stepDownAsChancellor(User senator);

	void stepDownAsTruthsayer(User senator);

	void createUntenabilityVote(User user, Regulation regulation);

	void castUntenabilityVote(User user, UntenabilityVote vote,
			boolean inFavor);

	void resolveUntenabilityVote(UntenabilityVote vote);

	RegulationChange createAddRegulationVote(User user, String title,
			String description);

	RegulationChange createModifyRegulationVote(User user,
			Regulation regulation, String newTitle, String newDescription);

	RegulationChange createRepealRegulationVote(User user,
			Regulation regulation);

	void voteForRegulation(User senator, RegulationChange regulationChange,
			boolean inFavor);

	void vetoRegulationChange(User chancellor,
			RegulationChange regulationChange);

	void resolveRegulationVote(RegulationChange change);

	void checkAcceptanceVotes();

	void resolveAcceptanceVotes();

	void checkChancellorElections();

	void resolveChancellorElections();

	void checkSenatorElections();

	void resolveSenatorElections();

	void resolveGroupLeaderElections();

	void resolveJoinApplications();

	void resolveRegulationVotes();

	void resolveUntenabilityVotes();

	void removeAcceptanceVotes(User user);

	void resetChancellorElectionIfUserIsParticipating(User user);

	void resetSenateElectionIfUserIsParticipating(User user);
}
