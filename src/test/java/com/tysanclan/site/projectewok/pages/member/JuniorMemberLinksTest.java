package com.tysanclan.site.projectewok.pages.member;

import org.junit.Test;

public class JuniorMemberLinksTest extends AbstractOverviewLinksTest {
	public JuniorMemberLinksTest() {
		super(13L);
	}

	@Test
	public void testMessagesLink() {
		verifyIconLink("accordion:basicpanel:messages", MessageListPage.class);
	}

	@Test
	public void testNotificationLink() {
		verifyIconLink("accordion:basicpanel:notification",
				NotificationsPage.class);
	}

	@Test
	public void testPreferencesLink() {
		verifyLink("accordion:basicpanel:preferences",
				MemberPreferencesPage.class);
	}

	/*
	 * <tr> <th>Personal</th> <th>Gaming</th> </tr> <tr> <td
	 * class="topCenteredCell"> <div wicket:id="messages">Messages</div>
	 * <wicket:enclosure child="accepttruthsayer"><a
	 * wicket:id="accepttruthsayer"><img wicket:id="icon" /> Truthsayer
	 * Invitation</a><br /></wicket:enclosure> <div
	 * wicket:id="treasurerAccept">Treasurer Nomination</div> <div
	 * wicket:id="heraldAccept">Herald Nomination</div> <div
	 * wicket:id="stewardAccept">Steward Nomination</div> <div
	 * wicket:id="pendingSubscription">Subscription</div> <div
	 * wicket:id="notification"></div> <div wicket:id="lucky"></div>
	 * 
	 * <a wicket:id="preferences">Preferences</a><br /> </td> <td
	 * class="topCenteredCell"> <div wicket:id="accounts"></div> <a
	 * wicket:id="requestAchievement">Request Achievement</a><br />
	 * <wicket:enclosure child="proposeAchievement"><a
	 * wicket:id="proposeAchievement">Propose new achievement</a><br
	 * /></wicket:enclosure> <wicket:enclosure child="creategamepetition"><a
	 * wicket:id="creategamepetition">Create Game Petition</a><br
	 * /></wicket:enclosure> <wicket:enclosure child="createrealmpetition"><a
	 * wicket:id="createrealmpetition">Create Realm Petition</a><br
	 * /></wicket:enclosure>
	 * 
	 * </td> </tr> <tr> <td colspan="2" style="height: 15px;"></td> </tr> <tr>
	 * <th>Democracy</th> <th>Social</th> </tr> <tr> <td
	 * class="topCenteredCell"> <wicket:enclosure child="endorsement"><a
	 * wicket:id="endorsement">Endorsements</a></wicket:enclosure> <div
	 * wicket:id="votes"></div> <div wicket:id="chancellorElection"></div> <div
	 * wicket:id="senateElection"></div> <div
	 * wicket:id="signgamepetition"></div> <div
	 * wicket:id="signrealmpetition"></div> <wicket:enclosure
	 * child="untenable"><div wicket:id="untenable"></div><br
	 * /></wicket:enclosure> <div wicket:id="runforchancellor"></div> <div
	 * wicket:id="runforsenator"></div> <a wicket:id="pastelections">Past
	 * Elections</a><br />
	 * 
	 * </td> <td class="topCenteredCell">
	 * 
	 * <div wicket:id="calendar">Calendar</div> <a wicket:id="gallery">Image and
	 * Video Galleries</a><br /> <a wicket:id="aim">AIM accounts</a><br /> <div
	 * wicket:id="joingroup">Join Group</div> <wicket:enclosure
	 * child="creategroup"><a wicket:id="creategroup">Create Group</a><br
	 * /></wicket:enclosure> </td> </tr> <tr> <td colspan="2"
	 * style="height: 15px;"></td> </tr>
	 * 
	 * <tr> <th colspan="2">Clan</th> </tr> <tr> <td colspan="2"
	 * class="topCenteredCell"> <a wicket:id="log">Clan Log</a><br /> <a
	 * wicket:id="finance">Clan Finances</a><br /> <a wicket:id="clanstats">Clan
	 * Statistics</a><br /> <wicket:enclosure child="trial"><a
	 * wicket:id="trial">Report Rules Violation</a><br /></wicket:enclosure>
	 * <wicket:enclosure child="complaint"><a wicket:id="complaint">File
	 * Truthsayer Complaint</a><br /></wicket:enclosure> <a
	 * wicket:id="bugs">Bugs and Feature Requests</a> </td> </tr>
	 */
}
