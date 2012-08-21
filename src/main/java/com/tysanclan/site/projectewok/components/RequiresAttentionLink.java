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
package com.tysanclan.site.projectewok.components;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.components.IconLink.ClickResponder;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.AttentionSuppression;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.AttentionSuppressionDAO;
import com.tysanclan.site.projectewok.pages.member.OverviewPage;

/**
 * @author Jeroen Steenbeeke
 */
public class RequiresAttentionLink extends Panel {

	private static final long serialVersionUID = 1L;

	public static interface IRequiresAttentionCondition extends Serializable {
		AttentionType requiresAttention();

		Long getDismissableId();
	}

	public static enum AttentionType {
		INFO("images/icons/information.png"), WARNING("images/icons/error.png"), ERROR(
				"images/icons/delete.png");

		private String iconPath;

		/**
		 * 
		 */
		private AttentionType(String iconPath) {
			this.iconPath = iconPath;
		}

		/**
		 * @return the iconPath
		 */
		public String getIconPath() {
			return iconPath;
		}
	}

	public static class Builder {
		private ClickResponder responder;

		private String text;

		private boolean invisibleIfNotNotified;

		private boolean visibilityAllowed;

		private List<String> conditionList = null;

		public Builder(String text, ClickResponder responder) {
			this.text = text;
			this.responder = responder;
			this.invisibleIfNotNotified = false;
			this.visibilityAllowed = true;
		}

		/**
		 * @param invisibleIfNotNotified
		 *            the invisibleIfNotNotified to set
		 */
		public Builder setInvisibleIfNotNotified(boolean invisibleIfNotNotified) {
			this.invisibleIfNotNotified = invisibleIfNotNotified;
			return this;
		}

		public RequiresAttentionLink newInstance(String id,
				IRequiresAttentionCondition condition,
				AttentionSuppressionDAO attentionDAO) {
			RequiresAttentionLink instance = new RequiresAttentionLink(id,
					condition);

			TysanSession session = TysanSession.get();
			User user = session != null ? session.getUser() : null;

			instance.label = new IconLink.Builder("", responder)
					.setImageVisible(false).setText(text).newInstance("label");

			AttentionType type = condition.requiresAttention();

			boolean active = type != null;

			instance.setVisible(active || !invisibleIfNotNotified);
			instance.setVisibilityAllowed(visibilityAllowed);

			instance.add(instance.label);

			instance.dismissed = attentionDAO.isSuppressed(
					condition.getClass(), condition.getDismissableId(), user);

			if (!instance.dismissed && active) {
				conditionList.add(id);
			}

			return instance;

		}

		public Builder disableVisibility() {
			this.visibilityAllowed = false;
			return this;
		}

		public void setConditionList(List<String> conditionList) {
			this.conditionList = conditionList;

		}
	}

	private IconLink icon;

	private IconLink label;

	private AttentionType attentionType;

	private Class<? extends IRequiresAttentionCondition> conditionClass;

	private Long dismissalId;

	private boolean dismissed = false;

	@SpringBean
	private AttentionSuppressionDAO attentionDAO;

	private RequiresAttentionLink(String id,
			IRequiresAttentionCondition condition) {
		super(id);

		this.attentionType = condition.requiresAttention();
		this.dismissalId = condition.getDismissableId();
		this.conditionClass = condition.getClass();

		String iconPath = attentionType != null ? attentionType.getIconPath()
				: "";

		icon = new IconLink.Builder(iconPath,
				new DefaultClickResponder<AttentionSuppression>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						attentionDAO.suppress(conditionClass, dismissalId,
								getUser());
						setResponsePage(new OverviewPage());
					}
				}).setImageVisible(attentionType != null)
				.setEnabled(dismissalId != null).newInstance("icon");

		add(icon);

		if (condition instanceof IDetachable) {
			((IDetachable) condition).detach();
		}
	}

	public boolean isDismissed() {
		return dismissed;
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();

		icon.setVisible(!dismissed);

	}

	private User getUser() {
		TysanSession session = TysanSession.get();
		return session != null ? session.getUser() : null;
	}

}
