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
package com.tysanclan.site.projectewok.beans.mock;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.tysanclan.site.projectewok.entities.Committee;
import com.tysanclan.site.projectewok.entities.Expense;
import com.tysanclan.site.projectewok.entities.Expense.ExpensePeriod;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumCategory;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.Group.JoinPolicy;
import com.tysanclan.site.projectewok.entities.LogItem;
import com.tysanclan.site.projectewok.entities.MessageFolder;
import com.tysanclan.site.projectewok.entities.NewsForum;
import com.tysanclan.site.projectewok.entities.PaymentRequest;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.Regulation;
import com.tysanclan.site.projectewok.entities.Role;
import com.tysanclan.site.projectewok.entities.Role.RoleType;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.StringUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Component
public class MockObjectRepository implements InitializingBean {
	private static long userCounter = 1L;

	@Autowired
	private SessionFactory factory;

	public MockObjectRepository() {
	}

	public void setFactory(SessionFactory factory) {
		this.factory = factory;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void afterPropertiesSet() throws Exception {

		init();
	}

	/**
	 * 
	 */
	private void init() {
		Session sess = factory.openSession();
		sess.beginTransaction();

		ForumCategory fc = new ForumCategory();
		fc.setId(1L);
		fc.setName("Main");
		addObject(sess, fc);

		List<Forum> forums = new LinkedList<Forum>();
		NewsForum f = new NewsForum();
		f.setId(1L);
		f.setCategory(fc);
		f.setDescription("This is a test forum");
		f.setName("Test Forum");
		f.setPublicAccess(true);
		addObject(sess, f);

		Forum f2 = new Forum();
		f2.setId(2L);
		f2.setCategory(fc);
		f2.setDescription("This is also a test forum");
		f2.setName("Test Forum 2");
		f2.setPublicAccess(true);
		addObject(sess, f2);

		// 1
		User prospero = genUser(sess, "Prospero", Rank.REVERED_MEMBER);

		Committee web = new Committee();
		web.setId(1L);
		web.setName("Website Committee");
		web.setLeader(prospero);
		web.getGroupMembers().add(prospero);
		web.setJoinPolicy(JoinPolicy.INVITATION);
		web.setDescription("The committee in charge of developing new features for the website as well as ensuring the current site stays operational");

		// 2
		genUser(sess, "LordInfiniti", Rank.TRUTHSAYER);
		// 3
		genUser(sess, "Thalos", Rank.SENATOR);
		// 4
		genUser(sess, "SilentPothead", Rank.CHANCELLOR);
		// 5
		genUser(sess, "Aragorn", Rank.SENIOR_MEMBER);
		// 6
		genUser(sess, "GreenfireStorm", Rank.REVERED_MEMBER);
		// 7
		genUser(sess, "Mach114", Rank.REVERED_MEMBER);
		// 8
		genUser(sess, "Rene", Rank.SENIOR_MEMBER);
		// 9
		genUser(sess, "Arclizes", Rank.SENIOR_MEMBER);
		// 10
		genUser(sess, "Kali", Rank.FULL_MEMBER);
		// 11
		genUser(sess, "Wolfie", Rank.FULL_MEMBER);
		// 12
		genUser(sess, "tobymanc", Rank.JUNIOR_MEMBER);
		// 13
		genUser(sess, "James", Rank.JUNIOR_MEMBER);
		// 14
		genUser(sess, "Strike", Rank.FULL_MEMBER);
		// 15
		User archonares = genUser(sess, "ArchonAres", Rank.FULL_MEMBER);
		// 16
		genUser(sess, "sukhoi", Rank.TRIAL);
		// 17
		genUser(sess, "firecrackerd", Rank.TRIAL);

		web.getGroupMembers().add(archonares);

		addObject(sess, web);

		Regulation lawOfSandwiches = new Regulation();
		lawOfSandwiches.setDrafter(prospero);
		lawOfSandwiches.setId(1L);
		lawOfSandwiches.setName("Law of Sandwiches");
		lawOfSandwiches
				.setContents("All members are required to eat ridiculously large sandwiches at all times");
		addObject(sess, lawOfSandwiches);

		forums.add(f);
		forums.add(f2);

		ForumThread ft = new ForumThread();
		ft.setForum(f);
		ft.setPoster(prospero);
		ft.setId(1L);
		ft.setPosts(new LinkedList<ForumPost>());
		ft.setPostTime(new Date());
		ft.setShadow(false);
		ft.setTitle("Test thread");
		addObject(sess, ft);

		f.getThreads().add(ft);

		ForumPost fp = new ForumPost();
		fp.setId(1L);
		fp.setPoster(prospero);
		fp.setContent("Test message");
		fp.setShadow(false);
		fp.setTime(new Date());
		addObject(sess, fp);

		ft.getPosts().add(fp);

		fp.setThread(ft);

		fc.setForums(forums);

		MessageFolder messageFolder = new MessageFolder();
		messageFolder.setId(1L);
		messageFolder.setName("Test folder");
		messageFolder.setOwner(prospero);
		messageFolder.setParent(null);
		addObject(sess, messageFolder);

		LogItem logItem = new LogItem();
		logItem.setCategory("Test");
		logItem.setId(1L);
		logItem.setLogTime(1234567890L);
		logItem.setMessage("Jouw moeder is lelijk!");
		logItem.setUser(prospero);
		addObject(sess, logItem);

		logItem = new LogItem();
		logItem.setCategory("Test");
		logItem.setId(2L);
		logItem.setLogTime(123456900L);
		logItem.setMessage("Jouw moeder is FUCKING lelijk!");
		logItem.setUser(archonares);
		addObject(sess, logItem);

		Expense expense = new Expense();
		Calendar cal = Calendar.getInstance();
		expense.setLastPayment(cal.getTime());
		cal.add(Calendar.YEAR, -2);
		expense.setStart(cal.getTime());
		expense.setAmount(new BigDecimal(15));
		expense.setName("SSL Certificate");
		expense.setPeriod(ExpensePeriod.ANNUALLY);
		addObject(sess, expense);

		expense = new Expense();
		expense.setAmount(new BigDecimal(95).setScale(1));
		expense.setName("Domain name");
		expense.setPeriod(ExpensePeriod.ANNUALLY);
		cal = Calendar.getInstance();
		expense.setLastPayment(cal.getTime());
		cal.add(Calendar.YEAR, -2);
		expense.setStart(cal.getTime());
		addObject(sess, expense);

		expense = new Expense();
		cal = Calendar.getInstance();
		expense.setLastPayment(cal.getTime());
		cal.add(Calendar.YEAR, -2);
		expense.setStart(cal.getTime());
		expense.setAmount(new BigDecimal(20));
		expense.setName("VPS Hosting");
		expense.setPeriod(ExpensePeriod.MONTHLY);
		addObject(sess, expense);

		createRole(sess, RoleType.TREASURER);
		createRole(sess, RoleType.HERALD);
		createRole(sess, RoleType.STEWARD);

		PaymentRequest req = new PaymentRequest();
		req.setAmount(BigDecimal.TEN);
		req.setItem("Cookies");
		req.setRequester(archonares);
		addObject(sess, req);

		sess.flush();
		sess.getTransaction().commit();
		sess.close();

	}

	private Role createRole(Session sess, RoleType type) {
		final String prettyName = StringUtil.capitalizeFirstFunction().apply(
				type.name().toLowerCase());

		Role role = new Role();
		role.setAssignedTo(null);
		role.setDescription(prettyName);
		role.setName(prettyName);
		role.setRoleType(type);
		addObject(sess, role);

		return role;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void addObject(Session sess, DomainObject d) {

		sess.save(d);

	}

	@Transactional(propagation = Propagation.REQUIRED)
	private User genUser(Session sess, String username, Rank rank) {

		User next = new User();
		next.setId(userCounter++);
		next.setCustomTitle("");
		next.setEMail(username + "@tysanclan.com");
		next.setImageURL("");
		next.setPassword("test");
		next.setRank(rank);
		next.setSignature("");
		next.setUsername(username);
		next.setJoinDate(new Date());
		addObject(sess, next);

		return next;
	}
}
