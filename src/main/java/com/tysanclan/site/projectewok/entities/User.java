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
package com.tysanclan.site.projectewok.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import com.fortuityframework.core.annotation.jpa.FortuityProperty;
import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.tysanclan.site.projectewok.event.RankChangeEvent;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(name = "TUSER")
@AccessType("field")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class User extends BaseDomainObject implements DomainObject {
	private static final long serialVersionUID = 1L;

	/**
	 * @author Jeroen Steenbeeke
	 */
	public static final class CaseInsensitiveUserComparator implements
			Comparator<User>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(User o1, User o2) {
			return o1.getUsername().compareToIgnoreCase(o2.getUsername());
		}
	}

	public static final class Renderer implements IChoiceRenderer<User> {
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(User object) {
			return object.getUsername();
		}

		@Override
		public String getIdValue(User object, int index) {
			return Long.toString(object.getId());
		}
	}

	@Column
	private String customTitle;

	@Column(nullable = false, unique = true)
	private String eMail;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserSeq")
	@SequenceGenerator(name = "UserSeq", sequenceName = "SEQ_ID_User", allocationSize = 1)
	private Long id;

	@Column
	private String imageURL;

	@Column(nullable = false)
	@Index(name = "IDX_TUSER_JOINDATE")
	private Date joinDate;

	@Column(nullable = false)
	private String password;

	@Column
	@Enumerated(EnumType.STRING)
	@FortuityProperty(onChange = RankChangeEvent.class)
	@Index(name = "IDX_TUSER_RANK")
	private Rank rank;

	@Column
	@Type(type = "org.hibernate.type.StringClobType")
	@Lob
	private String signature;

	@Column(nullable = false, unique = true)
	@Index(name = "IDX_TUSER_USERNAME")
	private String username;

	@Column(nullable = false)
	private boolean retired;

	@Column(nullable = false)
	private boolean vacation;

	@Column(nullable = false, columnDefinition = "boolean not null default false")
	private boolean collapseForums;

	@Column
	private Date lastAction;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "groupmembers", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
	@OrderBy("name")
	private List<Group> groups;

	@Column(nullable = true)
	private String timezone;

	@OneToMany(mappedBy = "endorses", fetch = FetchType.LAZY)
	private Set<User> endorsedBy;

	@OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
	private Set<Role> roles;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_TUSER_ENDORSES")
	private User endorses;

	@OneToMany(mappedBy = "endorsesForSenate", fetch = FetchType.LAZY)
	private Set<User> endorsedForSenateBy;

	@OneToMany(mappedBy = "requestedBy", fetch = FetchType.LAZY)
	private List<AchievementRequest> achievementRequests;

	@OneToOne(mappedBy = "subscriber", fetch = FetchType.EAGER, optional = true)
	private Subscription subscription;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_TUSER_ENDORSESSENATE")
	private User endorsesForSenate;

	@OneToOne(optional = true, mappedBy = "user", fetch = FetchType.LAZY, targetEntity = Profile.class)
	private Profile profile;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<PenaltyPoint> penaltyPoints;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<UserGameRealm> playedGames;

	@Column
	private Integer luckyScore;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<GalleryImage> galleryImages;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@Index(name = "IDX_TUSER_MENTOR")
	private User mentor;

	@OneToMany(mappedBy = "mentor", fetch = FetchType.LAZY)
	@Where(clause = "rank = 'TRIAL'")
	private List<User> pupils;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<YoutubeGalleryItem> youtubeGalleryItems;

	@OneToMany(mappedBy = "donator", fetch = FetchType.LAZY)
	private List<Donation> donations;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private List<UnreadForumPost> unreadForumPosts;

	@Column(nullable = false, columnDefinition = "boolean default false not null")
	private boolean bugReportMaster;

	@Column(nullable = true)
	private String paypalAddress;

	@OneToMany(mappedBy = "requester", fetch = FetchType.LAZY)
	private List<PaymentRequest> paymentRequests;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<SubscriptionPayment> payments;

	/**
	 * @return the mentor
	 */
	public User getMentor() {
		return mentor;
	}

	/**
	 * @param mentor
	 *            the mentor to set
	 */
	public void setMentor(User mentor) {
		this.mentor = mentor;
	}

	/**
	 * @return the pupils
	 */
	public List<User> getPupils() {
		if (pupils == null)
			pupils = new LinkedList<User>();

		return pupils;
	}

	/**
	 * @param pupils
	 *            the pupils to set
	 */
	public void setPupils(List<User> pupils) {
		this.pupils = pupils;
	}

	@Column(nullable = false, columnDefinition = "bigint not null default 0")
	private int loginCount;

	@JoinTable(name = "user_achievement", inverseJoinColumns = @JoinColumn(name = "achievement_id"), joinColumns = @JoinColumn(name = "user_id"))
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Achievement> achievements;

	// $P$

	public User() {
		this.endorsedBy = new HashSet<User>();
		this.penaltyPoints = new HashSet<PenaltyPoint>();
		this.achievements = new LinkedList<Achievement>();
		// $H$
		groups = new LinkedList<Group>();
		this.playedGames = new LinkedList<UserGameRealm>();
		this.luckyScore = 0;
	}

	public String getCustomTitle() {
		return customTitle;
	}

	public String getEMail() {
		return eMail;
	}

	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return id;
	}

	public String getImageURL() {
		return imageURL;
	}

	/**
	 * @return the joinDate
	 */
	public Date getJoinDate() {
		return joinDate;
	}

	public String getPassword() {
		return password;
	}

	public Rank getRank() {
		return rank;
	}

	public String getSignature() {
		return signature;
	}

	public String getUsername() {
		return username;
	}

	public void setCustomTitle(String customTitle) {
		this.customTitle = customTitle;
	}

	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	/**
	 * @param joinDate
	 *            the joinDate to set
	 */
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the retired
	 */
	public boolean isRetired() {
		return retired;
	}

	/**
	 * @param retired
	 *            the retired to set
	 */
	public void setRetired(boolean retired) {
		this.retired = retired;
	}

	/**
	 * @return the lastAction
	 */
	public Date getLastAction() {
		return lastAction;
	}

	/**
	 * @param lastAction
	 *            the lastAction to set
	 */
	public void setLastAction(Date lastAction) {
		this.lastAction = lastAction;
	}

	/**
	 * @return the groups
	 */
	public List<Group> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone
	 *            the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the vacation
	 */
	public boolean isVacation() {
		return vacation;
	}

	/**
	 * @param vacation
	 *            the vacation to set
	 */
	public void setVacation(boolean vacation) {
		this.vacation = vacation;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getUsername();
	}

	/**
	 * @return The Endorses of this User
	 */
	public User getEndorses() {
		return this.endorses;
	}

	/**
	 * Sets the Endorses of this User
	 * 
	 * @param endorses
	 *            The Endorses of this User
	 */
	public void setEndorses(User endorses) {
		this.endorses = endorses;
	}

	/**
	 * @return The EndorsedBy of this User
	 */
	public Set<User> getEndorsedBy() {
		return this.endorsedBy;
	}

	/**
	 * Sets the EndorsedBy of this User
	 * 
	 * @param endorsedBy
	 *            The EndorsedBy of this User
	 */
	public void setEndorsedBy(Set<User> endorsedBy) {
		this.endorsedBy = endorsedBy;
	}

	/**
	 * @return the endorsedForSenateBy
	 */
	public Set<User> getEndorsedForSenateBy() {
		return endorsedForSenateBy;
	}

	/**
	 * @param endorsedForSenateBy
	 *            the endorsedForSenateBy to set
	 */
	public void setEndorsedForSenateBy(Set<User> endorsedForSenateBy) {
		this.endorsedForSenateBy = endorsedForSenateBy;
	}

	/**
	 * @return the endorsesForSenate
	 */
	public User getEndorsesForSenate() {
		return endorsesForSenate;
	}

	/**
	 * @param endorsesForSenate
	 *            the endorsesForSenate to set
	 */
	public void setEndorsesForSenate(User endorsesForSenate) {
		this.endorsesForSenate = endorsesForSenate;
	}

	/**
	 * @return The Profile of this User
	 */
	public Profile getProfile() {
		return this.profile;
	}

	/**
	 * Sets the Profile of this User
	 * 
	 * @param profile
	 *            The Profile of this User
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	/**
	 * @return The PenaltyPoints of this User
	 */
	public Set<PenaltyPoint> getPenaltyPoints() {
		return this.penaltyPoints;
	}

	/**
	 * Sets the PenaltyPoints of this User
	 * 
	 * @param penaltyPoints
	 *            The PenaltyPoints of this User
	 */
	public void setPenaltyPoints(Set<PenaltyPoint> penaltyPoints) {
		this.penaltyPoints = penaltyPoints;
	}

	/**
	 * @return the playedGames
	 */
	public List<UserGameRealm> getPlayedGames() {
		return playedGames;
	}

	/**
	 * @param playedGames
	 *            the playedGames to set
	 */
	public void setPlayedGames(List<UserGameRealm> playedGames) {
		this.playedGames = playedGames;
	}

	/**
	 * @return the luckyScore
	 */
	public Integer getLuckyScore() {
		return luckyScore;
	}

	/**
	 * @param luckyScore
	 *            the luckyScore to set
	 */
	public void setLuckyScore(Integer luckyScore) {
		if (luckyScore == null) {
			this.luckyScore = 0;
		} else
			this.luckyScore = luckyScore;
	}

	/**
	 * @return the roles
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return the galleryImages
	 */
	public List<GalleryImage> getGalleryImages() {
		if (galleryImages == null) {
			galleryImages = new LinkedList<GalleryImage>();
		}

		return galleryImages;
	}

	/**
	 * @param galleryImages
	 *            the galleryImages to set
	 */
	public void setGalleryImages(List<GalleryImage> galleryImages) {
		this.galleryImages = galleryImages;
	}

	/**
	 * @return the loginCount
	 */
	public int getLoginCount() {
		return loginCount;
	}

	/**
	 * @param loginCount
	 *            the loginCount to set
	 */
	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	/**
	 * @return the youtubeGalleryItems
	 */
	public List<YoutubeGalleryItem> getYoutubeGalleryItems() {
		if (youtubeGalleryItems == null)
			youtubeGalleryItems = new LinkedList<YoutubeGalleryItem>();

		return youtubeGalleryItems;
	}

	/**
	 * @param youtubeGalleryItems
	 *            the youtubeGalleryItems to set
	 */
	public void setYoutubeGalleryItems(
			List<YoutubeGalleryItem> youtubeGalleryItems) {
		this.youtubeGalleryItems = youtubeGalleryItems;
	}

	public boolean hasDonatedAtLeast(BigDecimal total) {
		Calendar sixMonthsAgo = DateUtil.getCalendarInstance();
		sixMonthsAgo.add(Calendar.MONTH, -6);

		BigDecimal sum = BigDecimal.ZERO;

		for (Donation d : getDonations()) {
			if (d.getDonationTime().after(sixMonthsAgo.getTime())) {
				sum = sum.add(d.getAmount());
			}
		}

		return total.compareTo(sum) <= 0;
	}

	/**
	 * @return the donations
	 */
	public List<Donation> getDonations() {
		return donations;
	}

	/**
	 * @param donations
	 *            the donations to set
	 */
	public void setDonations(List<Donation> donations) {
		this.donations = donations;
	}

	/**
	 * @return The Achievements of this User
	 */
	public List<Achievement> getAchievements() {
		return this.achievements;
	}

	/**
	 * Sets the Achievements of this User
	 * @param achievements The Achievements of this User
	 */
	public void setAchievements(List<Achievement> achievements) {
		this.achievements = achievements;
	}

	public List<AchievementRequest> getAchievementRequests() {
		return achievementRequests;
	}

	public void setAchievementRequests(
			List<AchievementRequest> achievementRequests) {
		this.achievementRequests = achievementRequests;
	}

	public void setUnreadForumPosts(List<UnreadForumPost> unreadForumPosts) {
		this.unreadForumPosts = unreadForumPosts;
	}

	public List<UnreadForumPost> getUnreadForumPosts() {
		if (unreadForumPosts == null) {
			return new ArrayList<UnreadForumPost>();
		}
		return unreadForumPosts;
	}

	public boolean isCollapseForums() {
		return collapseForums;
	}

	public void setCollapseForums(boolean collapseForums) {
		this.collapseForums = collapseForums;
	}

	public boolean isBugReportMaster() {
		return bugReportMaster;
	}

	public void setBugReportMaster(boolean bugReportMaster) {
		this.bugReportMaster = bugReportMaster;
	}

	public String getPaypalAddress() {
		return paypalAddress;
	}

	public void setPaypalAddress(String paypalAddress) {
		this.paypalAddress = paypalAddress;
	}

	public List<PaymentRequest> getPaymentRequests() {
		if (paymentRequests == null)
			paymentRequests = new ArrayList<PaymentRequest>(0);

		return paymentRequests;
	}

	public void setPaymentRequests(List<PaymentRequest> paymentRequests) {
		this.paymentRequests = paymentRequests;
	}

	public List<SubscriptionPayment> getPayments() {
		if (payments == null)
			payments = new ArrayList<SubscriptionPayment>(0);

		return payments;
	}

	public void setPayments(List<SubscriptionPayment> payments) {
		this.payments = payments;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	// $GS$

}
