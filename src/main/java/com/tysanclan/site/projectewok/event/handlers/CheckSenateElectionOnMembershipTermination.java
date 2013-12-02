package com.tysanclan.site.projectewok.event.handlers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.beans.LogService;
import com.tysanclan.site.projectewok.beans.NotificationService;
import com.tysanclan.site.projectewok.entities.CompoundVote;
import com.tysanclan.site.projectewok.entities.CompoundVoteChoice;
import com.tysanclan.site.projectewok.entities.SenateElection;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.CompoundVoteChoiceDAO;
import com.tysanclan.site.projectewok.entities.dao.CompoundVoteDAO;
import com.tysanclan.site.projectewok.entities.dao.SenateElectionDAO;
import com.tysanclan.site.projectewok.event.MembershipTerminatedEvent;

public class CheckSenateElectionOnMembershipTermination implements
		EventHandler<MembershipTerminatedEvent> {
	private DemocracyService democracyService;

	private CompoundVoteDAO compoundVoteDAO;

	private CompoundVoteChoiceDAO compoundVoteChoiceDAO;

	private NotificationService notificationService;

	private LogService logService;

	private SenateElectionDAO electionDAO;

	@Autowired
	public CheckSenateElectionOnMembershipTermination(
			DemocracyService democracyService, CompoundVoteDAO compoundVoteDAO,
			CompoundVoteChoiceDAO compoundVoteChoiceDAO,
			NotificationService notificationService, LogService logService,
			SenateElectionDAO electionDAO) {
		this.democracyService = democracyService;
		this.compoundVoteDAO = compoundVoteDAO;
		this.compoundVoteChoiceDAO = compoundVoteChoiceDAO;
		this.notificationService = notificationService;
		this.logService = logService;
		this.electionDAO = electionDAO;
	}

	protected CheckSenateElectionOnMembershipTermination() {
	}

	@Override
	public EventResult onEvent(MembershipTerminatedEvent event) {
		User user = event.getSubject();

		SenateElection election = democracyService.getCurrentSenateElection();
		if (election != null) {
			if (election.isNominationOpen()
					&& election.getCandidates().contains(user)) {
				election.getCandidates().remove(user);

			} else if (election.getCandidates().contains(user)) {
				// Reset election to nomination period
				election.setStart(new Date());
				for (CompoundVote vote : election.getVotes()) {
					for (CompoundVoteChoice choice : vote.getChoices()) {
						compoundVoteChoiceDAO.delete(choice);
					}
					compoundVoteDAO.delete(vote);

					notificationService
							.notifyUser(
									vote.getCaster(),
									"The Senate election was restarted due to a candidate's membership being terminated. You will need to vote again in a week");

				}

				logService
						.logSystemAction("Democracy",
								"Senate election restarted due to candidate membership termination");
			}

			electionDAO.update(election);
		}

		return EventResult.ok();
	}
}
