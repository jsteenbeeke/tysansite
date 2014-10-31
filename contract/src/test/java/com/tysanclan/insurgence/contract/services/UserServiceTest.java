package com.tysanclan.insurgence.contract.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.tysanclan.insurgence.contract.datamodel.RSUser;
import com.tysanclan.insurgence.contract.datamodel.RSUserRank;

@RunWith(Enclosed.class)
public class UserServiceTest {
	private static final RSUser USER_ROGER = makeUser("Roger",
			RSUserRank.INITIATE);

	private static final RSUser USER_MIKE = makeUser("Mike", RSUserRank.MEMBER);

	private static final RSUser USER_NIXON = makeUser("Nixon",
			RSUserRank.MEMBER);

	private static final RSUser USER_JOHNSON = makeUser("Johnson",
			RSUserRank.MEMBER);

	private static final RSUser USER_JOE = makeUser("Joe",
			RSUserRank.TRUTHSAYER);

	private static final RSUser USER_STEVE = makeUser("Steve",
			RSUserRank.COUNCILMEMBER);

	private static final RSUser USER_BOB = makeUser("Bob",
			RSUserRank.COUNCILMEMBER);

	private static final List<RSUser> ALL_USERS = initUsers();

	private static long counter = 1;

	private static List<RSUser> initUsers() {
		List<RSUser> users = Lists.newArrayListWithExpectedSize(8);
		users.add(USER_BOB);
		users.add(USER_STEVE);
		users.add(USER_JOE);
		users.add(USER_MIKE);
		users.add(USER_ROGER);

		return users;
	}

	private static RSUser makeUser(String name, RSUserRank rank) {
		RSUser user = new RSUser();
		user.setId(counter++);
		user.setUsername(name);
		user.setRank(rank);
		return user;
	}

	public static class ListOperations extends AbstractServiceTest<UserService> {
		public ListOperations() {
			super(UserService.class);
		}

		private static final List<RSUser> INITIATES = Lists
				.newArrayList(USER_ROGER);
		private static final List<RSUser> MEMBERS = Lists.newArrayList(
				USER_MIKE, USER_NIXON, USER_JOHNSON);
		private static final List<RSUser> TRUTHSAYERS = Lists
				.newArrayList(USER_JOE);
		private static final List<RSUser> COUNCIL = Lists.newArrayList(
				USER_BOB, USER_STEVE);

		@Override
		protected void setupMock(UserService base) {
			when(base.getUsers()).thenReturn(ALL_USERS);
			when(base.getUsersByRank(RSUserRank.COUNCILMEMBER)).thenReturn(
					COUNCIL);
			when(base.getUsersByRank(RSUserRank.TRUTHSAYER)).thenReturn(
					TRUTHSAYERS);
			when(base.getUsersByRank(RSUserRank.MEMBER)).thenReturn(MEMBERS);
			when(base.getUsersByRank(RSUserRank.INITIATE))
					.thenReturn(INITIATES);
			when(base.getUsersByRank(null)).thenReturn(
					Collections.<RSUser> emptyList());
		}

		private void testRank(RSUserRank rank) {
			List<RSUser> users = service().getUsersByRank(rank);
			assertNotNull(String.format("Can fetch users of type %s", rank),
					users);
			for (RSUser user : users) {
				assertEquals(String.format(
						"All fetched users of type %s actually have rank %s",
						rank, rank), rank, user.getRank());
			}
		}

		@Test
		public final void listAll() {
			assertEquals(ALL_USERS, service().getUsers());
		}

		@Test
		public final void listNull() {
			testRank(null);
		}

		@Test
		public final void listCouncilMembers() {
			testRank(RSUserRank.COUNCILMEMBER);
		}

		@Test
		public final void listTruthSayers() {
			testRank(RSUserRank.TRUTHSAYER);
		}

		@Test
		public final void listMembers() {
			testRank(RSUserRank.MEMBER);
		}

		@Test
		public final void listInitiates() {
			testRank(RSUserRank.INITIATE);
		}
	}

}
