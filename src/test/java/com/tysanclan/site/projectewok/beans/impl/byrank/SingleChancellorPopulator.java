package com.tysanclan.site.projectewok.beans.impl.byrank;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.beans.RealmService;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Role;
import com.tysanclan.site.projectewok.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Dataset(SingleChancellorPopulator.KEY)
public class SingleChancellorPopulator extends SingleRankPopulator {
	public static final String KEY = "single-chancellor";

	@Autowired
	private GameService gameService;

	@Autowired
	private RealmService realmService;

	@Autowired
	private RoleService roleService;

	public SingleChancellorPopulator() {
		super(Rank.CHANCELLOR);
	}

	@Override
	protected void onUserCreated(User user) {
		super.onUserCreated(user);

		Game game = gameService.createGame("Diablo 9", new byte[0]);
		gameService.setGameSupervisor(game, user);
		realmService.createRealm("USNorth", game, user);

		Role role = roleService.createRole(user, "Treasurer",
										   "The lord of cash", Role.RoleType.TREASURER);
		roleService.assignTo(user.getId(), role.getId(),
							 user.getId());
		role = roleService
			.createRole(user, "Steward", "The lord of code",
						Role.RoleType.STEWARD);
		roleService.assignTo(user.getId(), role.getId(),
							 user.getId());
		role = roleService.createRole(user, "Herald",
									  "The voice of Tysan", Role.RoleType.HERALD);
		roleService.assignTo(user.getId(), role.getId(),
							 user.getId());

	}
}
