package com.tysanclan.site.projectewok.beans.impl.byrank;

import com.jeroensteenbeeke.hyperion.solstice.spring.IEntityPopulator;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public abstract class SingleRankPopulator implements IEntityPopulator {
	private final Rank rank;

	@Autowired
	private UserService userService;

	@Autowired
	private MembershipService memberService;

	@Autowired
	private UserDAO userDAO;

	protected SingleRankPopulator(Rank rank) {
		this.rank = rank;
	}

	@Override
	public void createEntities() {
		Instant i = LocalDate.now().minusYears(5L).atStartOfDay().toInstant(ZoneOffset.UTC);

		User user1 = userService.createUser("User1", "test", "j.steenbeeke+tysanuser1@gmail.com");

		if (userService.setUserImportData(user1.getId(), this.rank, i.getEpochSecond())) {
			userDAO.load(user1.getId()).peek(this::onUserCreated);
		}
	}

	protected void onUserCreated(User user) {

	}

	@Override
	public int getPriority() {
		return 0;
	}
}
