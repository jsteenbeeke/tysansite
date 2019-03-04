/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok;

import com.jeroensteenbeeke.hyperion.meld.web.EntityEncapsulator;
import com.jeroensteenbeeke.hyperion.rollbar.IRollBarDeployNotifier;
import com.jeroensteenbeeke.hyperion.rollbar.RollBarDeployNotifier;
import com.jeroensteenbeeke.hyperion.solstice.data.factory.SolsticeEntityEncapsulatorFactory;
import com.jeroensteenbeeke.hyperion.solstice.spring.ApplicationContextProvider;
import com.jeroensteenbeeke.hyperion.tardis.scheduler.intervals.Interval;
import com.jeroensteenbeeke.hyperion.tardis.scheduler.intervals.Intervals;
import com.jeroensteenbeeke.hyperion.tardis.scheduler.wicket.HyperionScheduler;
import com.tysanclan.site.projectewok.auth.TysanSecurity;
import com.tysanclan.site.projectewok.beans.PopulationService;
import com.tysanclan.site.projectewok.components.resources.DaysInTysanImageResourceReference;
import com.tysanclan.site.projectewok.components.resources.HaleyAccidentResourceReference;
import com.tysanclan.site.projectewok.components.resources.MinecraftWhitelistResourceReference;
import com.tysanclan.site.projectewok.components.resources.UUIDMinecraftWhitelistResourceReference;
import com.tysanclan.site.projectewok.pages.*;
import com.tysanclan.site.projectewok.pages.forum.ActivationPage;
import com.tysanclan.site.projectewok.pages.member.ReportBugPage;
import com.tysanclan.site.projectewok.pages.member.RequestFeaturePage;
import com.tysanclan.site.projectewok.pages.member.SubscriptionPaymentResolvedPage;
import com.tysanclan.site.projectewok.pages.member.ViewBugPage;
import com.tysanclan.site.projectewok.pages.member.admin.OldExpensesPage;
import com.tysanclan.site.projectewok.pages.member.admin.ProcessPaymentRequestPage;
import com.tysanclan.site.projectewok.tasks.*;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.apache.wicket.protocol.http.mock.MockHttpSession;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.settings.ExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wicketstuff.wiquery.ui.themes.WiQueryCoreThemeResourceReference;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Jeroen Steenbeeke
 */
public class TysanApplication extends WebApplication
		implements ApplicationContextProvider {
	private static Logger log = LoggerFactory.getLogger(TysanApplication.class);

	private static String version = null;

	public static final String MASTER_KEY = "Sethai Janora Kumirez Dechai";

	public final List<SiteWideNotification> notifications = new LinkedList<SiteWideNotification>();

	private ApplicationContext context = null;

	/**
	 * Creates a new application object for the Tysan website
	 */
	public TysanApplication() {
		this(System.getProperty("ewok.testmode") != null);
	}

	public ApplicationContext getApplicationContext() {
		return context;
	}

	public static String getApplicationVersion() {
		if (version == null) {
			try {
				Properties props = new Properties();

				InputStream stream = get().getServletContext()
										  .getResourceAsStream("/META-INF/MANIFEST.MF");

				if (stream != null) {

					props.load(stream);

					version = props.getProperty("Implementation-Build");
				} else {
					version = "SNAPSHOT";
				}
			} catch (IOException ioe) {
				version = "SNAPSHOT";
			}
		}

		return version;
	}

	private final boolean testMode;

	/**
	 * Creates a new application object for the Tysan website
	 *
	 * @param isTestMode Whether or not we are running this site in test mode
	 */
	public TysanApplication(boolean isTestMode) {
		this.testMode = isTestMode;
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return NewsPage.class;
	}

	/**
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	@Override
	protected void init() {
		super.init();

		context = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());

		getComponentInstantiationListeners().add(new TysanSecurity());

		SpringComponentInjector injector = new SpringComponentInjector(this);
		getComponentInstantiationListeners().add(injector);

		getApplicationListeners().add(new RollbarDeployListener(context.getBean(IRollBarDeployNotifier.class)));

		mountBookmarkablePages();
		mountResources();

		getExceptionSettings().setUnexpectedExceptionDisplay(ExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);

		getApplicationSettings().setInternalErrorPage(TysanErrorPage.class);
		getApplicationSettings().setAccessDeniedPage(AccessDeniedPage.class);
		getApplicationSettings()
				.setPageExpiredErrorPage(SessionTimeoutPage.class);

		EntityEncapsulator.setFactory(new SolsticeEntityEncapsulatorFactory());

		HyperionScheduler.getScheduler().setApplication(this);

		if (!testMode) {
			scheduleDefaultTasks();
		} else {
			runSitePopulator();
			TysanApplicationReference.INSTANCE.setApplication(this);
		}

		// if (usesDeploymentConfig())
		// getApplicationSettings().setInternalErrorPage(TysanErrorPage.class);

		getRequestCycleListeners().add(new ErrorReporterListener());

		getResourceSettings().setUseMinifiedResources(false);

		addResourceReplacement(WiQueryCoreThemeResourceReference.get(),
							   new CssResourceReference(TysanApplication.class,
														"themes/ui-darkness/jquery-ui-1.10.2.custom.css"));
	}

	private void runSitePopulator() {
		MockServletContext sctx = new MockServletContext(this,
														 "/src/main/webapp/");
		MockHttpServletRequest request = new MockHttpServletRequest(this,
																	new MockHttpSession(sctx), sctx);
		RequestAttributes attr = new ServletRequestAttributes(request);

		RequestContextHolder.setRequestAttributes(attr);

		context.getBean(PopulationService.class).createDebugSite();

		RequestContextHolder.resetRequestAttributes();
	}

	/**
	 *
	 */
	private void scheduleDefaultTasks() {

		Interval daily = Intervals.days(1);
		Interval hourly = Intervals.hours(1);
		Interval everyFourHours = Intervals.hours(4);
		Interval everySixHours = Intervals.hours(6);

		HyperionScheduler.getScheduler()
						 .scheduleRepeatingTask(daily, new AcceptanceVoteStartTask());

		HyperionScheduler.getScheduler().scheduleRepeatingTask(everyFourHours,
															   new AcceptanceVoteStopTask());
		HyperionScheduler.getScheduler()
						 .scheduleRepeatingTask(daily, new AutomaticPromotionTask());

		HyperionScheduler.getScheduler()
						 .scheduleRepeatingTask(hourly, new ChancellorElectionChecker());
		HyperionScheduler.getScheduler().scheduleRepeatingTask(everyFourHours,
															   new ChancellorElectionResolutionTask());
		HyperionScheduler.getScheduler().scheduleRepeatingTask(daily,
															   new EmailChangeConfirmationExpirationTask());
		HyperionScheduler.getScheduler().scheduleRepeatingTask(daily,
															   new GroupLeaderElectionResolutionTask());
		HyperionScheduler.getScheduler().scheduleRepeatingTask(hourly,
															   new MemberApplicationResolutionTask());
		HyperionScheduler.getScheduler()
						 .scheduleRepeatingTask(hourly, new MembershipExpirationTask());

		HyperionScheduler.getScheduler().scheduleRepeatingTask(hourly,
															   new PasswordRequestExpirationTask());
		HyperionScheduler.getScheduler().scheduleRepeatingTask(everyFourHours,
															   new RegulationChangeResolutionTask());
		HyperionScheduler.getScheduler()
						 .scheduleRepeatingTask(daily, new ResolveImpeachmentTask());
		HyperionScheduler.getScheduler()
						 .scheduleRepeatingTask(hourly, new SenateElectionChecker());
		HyperionScheduler.getScheduler().scheduleRepeatingTask(everyFourHours,
															   new SenateElectionResolutionTask());
		HyperionScheduler.getScheduler().scheduleRepeatingTask(daily,
															   new TruthsayerAcceptanceVoteResolver());
		HyperionScheduler.getScheduler().scheduleRepeatingTask(daily,
															   new UntenabilityVoteResolutionTask());
		HyperionScheduler.getScheduler()
						 .scheduleRepeatingTask(hourly, new AchievementProposalTask());
		HyperionScheduler.getScheduler()
						 .scheduleRepeatingTask(hourly, new WarnInactiveMembersTask());

		HyperionScheduler.getScheduler().scheduleRepeatingTask(everySixHours,
															   new ResolveRoleTransferTask());
		HyperionScheduler.getScheduler().scheduleRepeatingTask(everySixHours,
															   new CheckSubscriptionsDueTask());
		HyperionScheduler.getScheduler().scheduleRepeatingTask(everySixHours,
															   new ResolveTruthsayerComplaintTask());
		HyperionScheduler.getScheduler()
						 .scheduleRepeatingTask(Intervals.minutes(5),
												new RestTokenCleanupTask());

		if (System.getProperty("tysan.debug") != null) {
			HyperionScheduler.getScheduler()
							 .scheduleRepeatingTask(hourly, new NoAccountExpireTask());
		}
	}

	/**
	 *
	 */
	private void mountBookmarkablePages() {

		mountPage("/news", NewsPage.class);
		mountPage("/charter", CharterPage.class);
		mountPage("/about", AboutPage.class);
		mountPage("/history", HistoryPage.class);
		mountPage("/member/${userid}", MemberPage.class);
		mountPage("/members", RosterPage.class);
		mountPage("/regulations", RegulationPage.class);
		mountPage("/realm/${id}", RealmPage.class);

		mountPage("groups", GroupsPage.class);

		mountPage("/register", JoinOverviewPage.class);

		mountPage("vacation", VacationPage.class);

		mountPage("/threads/${threadid}/${pageid}", ForumThreadPage.class);
		mountPage("/group/${groupid}", GroupPage.class);

		mountPage("/forums/${forumid}/${pageid}", ForumPage.class);
		mountPage("/listforums", ForumOverviewPage.class);
		mountPage("/register/application", RegisterAndApplyPage.class);
		mountPage("/register/forums", RegistrationPage.class);
		mountPage("/activation/${key}", ActivationPage.class);

		mountPage("/resetpassword/${key}",
				  PasswordRequestConfirmationPage.class);

		mountPage("/accessdenied", AccessDeniedPage.class);

		mountPage("/tracker/reportbug", ReportBugPage.class);
		mountPage("/tracker/requestfeature", RequestFeaturePage.class);

		mountPage("/processPaymentRequest/${requestId}/${confirmationKey}",
				  ProcessPaymentRequestPage.class);

		mountPage("/processSubscriptionPayment/${paymentId}/${confirmationKey}",
				  SubscriptionPaymentResolvedPage.class);

		mountPage("/bug/${id}", ViewBugPage.class);
		mountPage("/feature/${id}", ViewBugPage.class);

		if (System.getProperty("tysan.install") != null) {
			mountPage("/oldexpenses", OldExpensesPage.class);
		}
	}

	private void mountResources() {
		mountResource("/images/signatures/daysintysan/${username}",
					  new DaysInTysanImageResourceReference());
		mountResource("/images/signatures/haley",
					  new HaleyAccidentResourceReference());
		mountResource("/mc-whitelist/",
					  new MinecraftWhitelistResourceReference());
		mountResource("/uuid-mc-whitelist/",
					  new UUIDMinecraftWhitelistResourceReference());
	}

	/**
	 * @return The current TysanApplication
	 */
	public static TysanApplication get() {
		return (TysanApplication) Application.get();
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new TysanSession(request);
	}

	@Override
	public WebRequest newWebRequest(HttpServletRequest servletRequest,
									String filterPath) {
		WebRequest request = super.newWebRequest(servletRequest, filterPath);
		getSessionStore().setAttribute(request, "wickery-theme",
									   new CssResourceReference(TysanApplication.class,
																"themes/ui-darkness/jquery-ui-1.7.2.custom.css"));

		return request;
	}

	/**
	 * @see org.apache.wicket.Application#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.shutdown();
		} catch (SchedulerException e) {
			log.error("Could not stop Quartz Scheduler", e);
		}
	}

	public void notify(SiteWideNotification notification) {
		synchronized (notifications) {
			notifications.add(notification);
		}
	}

	public List<SiteWideNotification> getActiveNotifications() {
		synchronized (notifications) {
			Set<SiteWideNotification> exit = new HashSet<>();

			for (SiteWideNotification next : notifications) {
				if (next.isExpired()) {
					exit.add(next);
				}
			}

			notifications.removeAll(exit);

			return notifications;
		}
	}

	public static class VersionDescriptor
			implements Comparable<VersionDescriptor> {
		public static VersionDescriptor of(String ver) {
			String[] versionData = ver.split("\\.", 4);

			int major = Integer.parseInt(versionData[0]);
			int minor = Integer.parseInt(versionData[1]);
			int date = Integer.parseInt(versionData[2]);
			int time = Integer.parseInt(versionData[3]);

			return new VersionDescriptor(major, minor, date, time);
		}

		private final Integer major;

		private final Integer minor;

		private final Integer date;

		private final Integer time;

		public VersionDescriptor(int major, int minor, int date, int time) {
			super();
			this.major = major;
			this.minor = minor;
			this.date = date;
			this.time = time;
		}

		public int getMajor() {
			return major;
		}

		public int getMinor() {
			return minor;
		}

		public int getDate() {
			return date;
		}

		public int getTime() {
			return time;
		}

		@Override
		public int compareTo(VersionDescriptor o) {
			int compareMajor = major.compareTo(o.major);
			int compareMinor = minor.compareTo(o.minor);
			int compareDate = date.compareTo(o.date);
			int compareTime = time.compareTo(o.time);

			if (compareMajor != 0)
				return compareMajor;

			if (compareMinor != 0)
				return compareMinor;

			if (compareDate != 0)
				return compareDate;

			if (compareTime != 0)
				return compareTime;

			return 0;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((date == null) ? 0 : date.hashCode());
			result = prime * result + ((major == null) ? 0 : major.hashCode());
			result = prime * result + ((minor == null) ? 0 : minor.hashCode());
			result = prime * result + ((time == null) ? 0 : time.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			VersionDescriptor other = (VersionDescriptor) obj;
			if (date == null) {
				if (other.date != null)
					return false;
			} else if (!date.equals(other.date))
				return false;
			if (major == null) {
				if (other.major != null)
					return false;
			} else if (!major.equals(other.major))
				return false;
			if (minor == null) {
				if (other.minor != null)
					return false;
			} else if (!minor.equals(other.minor))
				return false;
			if (time == null) {
				if (other.time != null)
					return false;
			} else if (!time.equals(other.time))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return major + "." + minor + "." + date + "." + time;
		}

		public VersionDescriptor next() {

			return new VersionDescriptor(major, minor, date, time + 1);
		}

	}
}
