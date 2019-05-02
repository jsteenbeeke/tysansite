package com.tysanclan.site.projectewok.beans.impl.senate;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.jeroensteenbeeke.hyperion.solstice.spring.IEntityPopulator;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.SenateElection;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.SenateElectionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

@Dataset(SenateActivePopulator.KEY)
@Component
public class SenateActivePopulator implements IEntityPopulator {
	public static final String KEY = "start-senate-election-when-one-active";
	@Autowired
	private SenateElectionDAO senateElectionDAO;

	@Autowired
	private UserService userService;

	@Autowired
	private MembershipService memberService;

	@Override
	public void createEntities() {
		Instant i = LocalDate.now().minusYears(5L).atStartOfDay().toInstant(ZoneOffset.UTC);

		User user1 = userService.createUser("User1", "test", "j.steenbeeke+tysanuser1@gmail.com");
		User user2 = userService.createUser("User2", "test", "j.steenbeeke+tysanuser2@gmail.com");
		User user3 = userService.createUser("User3", "test", "j.steenbeeke+tysanuser3@gmail.com");
		User user4 = userService.createUser("User4", "test", "j.steenbeeke+tysanuser4@gmail.com");
		User user5 = userService.createUser("User5", "test", "j.steenbeeke+tysanuser5@gmail.com");
		User user6 = userService.createUser("User6", "test", "j.steenbeeke+tysanuser6@gmail.com");

		userService.setUserImportData(user1.getId(), Rank.CHANCELLOR, i.getEpochSecond());
		userService.setUserImportData(user2.getId(), Rank.SENATOR, i.getEpochSecond());
		userService.setUserImportData(user3.getId(), Rank.SENATOR, i.getEpochSecond());
		userService.setUserImportData(user4.getId(), Rank.TRUTHSAYER, i.getEpochSecond());
		userService.setUserImportData(user5.getId(), Rank.SENIOR_MEMBER, i.getEpochSecond());
		userService.setUserImportData(user6.getId(), Rank.REVERED_MEMBER, i.getEpochSecond());

		memberService.registerAction(user1);
		memberService.registerAction(user2);
		memberService.registerAction(user3);
		memberService.registerAction(user4);
		memberService.registerAction(user5);
		memberService.registerAction(user6);

		SenateElection e = new SenateElection();
		e.setSeats(2);
		e.setStart(Date.from(LocalDate.now().minusDays(2L).atStartOfDay().toInstant(ZoneOffset.UTC)));

		senateElectionDAO.save(e);

	}

	@Override
	public int getPriority() {
		return 0;
	}
}
