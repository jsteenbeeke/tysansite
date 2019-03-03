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
package com.tysanclan.site.projectewok.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author Jeroen Steenbeeke
 */
public class IconLink extends Panel {
	private static final long serialVersionUID = 1L;

	public static class Builder {
		private final String iconPath;
		private final ClickResponder responder;
		private boolean imageVisible = true;

		private String text = null;
		private boolean enabled = true;

		public Builder(String iconPath, ClickResponder responder) {
			this.iconPath = iconPath;
			this.responder = responder;

		}

		public Builder setText(String text) {
			this.text = text;
			return this;
		}

		public IconLink newInstance(String id) {
			IconLink instance = new IconLink(id);

			AbstractLink responderLink = instance
					.createResponderLink(responder);

			responderLink.add(new ContextImage("icon", iconPath)
					.setVisible(imageVisible));
			if (text == null) {
				responderLink
						.add(new WebMarkupContainer("text").setVisible(false));
			} else {
				responderLink.add(new Label("text", text));
			}
			responderLink.setEnabled(enabled);

			instance.add(responderLink);

			return instance;
		}

		public Builder setImageVisible(boolean b) {
			imageVisible = b;
			return this;
		}

		public Builder setEnabled(boolean enabled) {
			this.enabled = enabled;
			return this;
		}

	}

	protected IconLink(String id) {
		super(id);
	}

	protected AbstractLink createResponderLink(ClickResponder responder) {
		AbstractLink responderLink = null;

		if (responder.isUseAjax()) {
			responderLink = new AjaxLink<ClickResponder>("link",
					new Model<ClickResponder>(responder)) {
				private static final long serialVersionUID = 1L;

				/**
				 * @see org.apache.wicket.Component#onDetach()
				 */
				@Override
				protected void onDetach() {
					super.onDetach();

					ClickResponder cr = getModelObject();
					cr.detach();
				}

				@Override
				public void onClick(AjaxRequestTarget target) {
					ClickResponder cr = getModelObject();
					Injector.get().inject(cr);
					cr.onClick(target);
				}
			};
		} else {
			responderLink = new Link<ClickResponder>("link",
					new Model<ClickResponder>(responder)) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					ClickResponder cr = getModelObject();
					Injector.get().inject(cr);

					cr.onClick(null);
				}
			};
		}

		return responderLink;
	}

	public static interface ClickResponder extends IDetachable {
		public void onClick(AjaxRequestTarget target);

		public boolean isUseAjax();
	}

	public static abstract class DefaultClickResponder<T>
			implements ClickResponder {
		private static final long serialVersionUID = 1L;

		private final IModel<T> model;

		/**
		 *
		 */
		public DefaultClickResponder() {
			this.model = null;
		}

		/**
		 *
		 */
		public DefaultClickResponder(IModel<T> model) {
			this.model = model;
		}

		/**
		 * @see com.tysanclan.site.projectewok.components.IconLink.ClickResponder#isUseAjax()
		 */
		@Override
		public boolean isUseAjax() {
			return false;
		}

		/**
		 * @see com.tysanclan.site.projectewok.components.IconLink.ClickResponder#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
		 */
		@Override
		public void onClick(AjaxRequestTarget target) {
			onClick();
		}

		public void onClick() {

		}

		/**
		 * @return the model
		 */
		public IModel<T> getModel() {
			return model;
		}

		public T getModelObject() {
			return model != null ? model.getObject() : null;
		}

		protected void onDetach() {
			// Template method
		}

		/**
		 * @see org.apache.wicket.model.IDetachable#detach()
		 */
		@Override
		public final void detach() {
			if (model != null) {
				model.detach();
			}

			onDetach();
		}
	}
}
