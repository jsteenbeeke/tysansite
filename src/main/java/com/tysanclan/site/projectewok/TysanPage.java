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

import com.google.common.collect.Lists;
import com.tysanclan.site.projectewok.components.*;
import com.tysanclan.site.projectewok.entities.GlobalSetting;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.GlobalSettingDAO;
import com.tysanclan.site.projectewok.util.AprilFools;
import com.tysanclan.site.projectewok.util.MemberUtil;
import io.vavr.control.Option;
import org.apache.wicket.Application;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.StringValueConversionException;
import org.apache.wicket.util.time.Duration;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.wicketstuff.wiquery.ui.dialog.Dialog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * Web page for use within the Tysan Clan
 *
 * @author Jeroen Steenbeeke
 */
public class TysanPage extends WebPage {
	private static final long serialVersionUID = 1L;

	private static final boolean ENABLE_MAGIC_PUSHTBUTTON = false;

	@SpringBean
	private GlobalSettingDAO globalSettingDAO;

	private Dialog animalDialog;

	private Label headerLabel;

	private Label titleLabel;

	private final WebMarkupContainer topPanel;

	private final Dialog notificationWindow;

	private boolean autoCollapse = false;

	public TysanPage(String title) {
		this(title, null);
	}

	public TysanPage(String title, IModel<?> model) {
		super(model);

		notificationWindow = new Dialog("notificationWindow");
		notificationWindow.setTitle("Urgent Message");
		notificationWindow.setOutputMarkupId(true)
				.setOutputMarkupPlaceholderTag(true);

		notificationWindow
				.add(new ComponentFeedbackPanel("messages", notificationWindow)
						.setOutputMarkupId(true)
						.setOutputMarkupPlaceholderTag(true));
		notificationWindow.setAutoOpen(false);
		notificationWindow.setVisible(false);

		add(notificationWindow);

		headerLabel = new Label("header", title);
		titleLabel = new Label("title", title + getTitleSuffix());

		headerLabel.setEscapeModelStrings(false);
		titleLabel.setEscapeModelStrings(false);

		add(headerLabel);
		add(titleLabel);
		add(new FeedbackPanel("feedback").setOutputMarkupId(true));

		Dialog window = new Dialog("debugWindow");
		window.setTitle("Debug Information");
		window.add(new DebugWindow("debugPanel", this.getPageClass()));
		window.setWidth(600);
		window.setHeight(300);
		window.setResizable(false);

		window.add(new AjaxLink<Void>("magicpushbutton") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {

			}

		}.setVisible(ENABLE_MAGIC_PUSHTBUTTON));

		add(window);

		add(new AjaxLink<Dialog>("debugLink", new Model<Dialog>(window)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				Dialog _window = getModelObject();
				target.appendJavaScript(_window.open().render().toString());

			}

		}.setVisible(Application.get().getConfigurationType()
				== RuntimeConfigurationType.DEVELOPMENT));

		User u = getUser();
		WebMarkupContainer subMenu = new WebMarkupContainer("topMenu");
		if (u != null) {
			if (MemberUtil.isMember(u)) {
				topPanel = new WebMarkupContainer("topbar");
				subMenu = new TysanMemberPanel("topMenu", u);
			} else {
				topPanel = new WebMarkupContainer("topbar");
				subMenu = new TysanUserPanel("topMenu", u);
			}
		} else {
			topPanel = new TysanLoginPanel("topbar");
		}
		add(new TysanMenu("menu", u != null));
		add(subMenu);

		add(topPanel);

		add(new Label("version", TysanApplication.getApplicationVersion()));

		if (u != null) {
			get("version").add(new AjaxSelfUpdatingTimerBehavior(
					Duration.seconds(30)) {
				private static final long serialVersionUID = 1L;

				/**
				 * @see org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior#onPostProcessTarget(org.apache.wicket.ajax.AjaxRequestTarget)
				 */
				@Override
				protected void onPostProcessTarget(AjaxRequestTarget target) {
					Dialog d = getNotificationWindow();
					TysanSession t = TysanSession.get();
					int i = 0;

					for (SiteWideNotification swn : TysanApplication.get()
							.getActiveNotifications()) {
						if (t != null && !t.notificationSeen(swn)) {
							swn.display(d);
							i++;
						}
					}

					if (i > 0) {
						d.setAutoOpen(true);
						d.setVisible(true);
						target.add(d);
						getNotificationWindow().open(target);
					}
				}
			});

		}
		addAnimalPanel();

		add(new Label("year", LocalDate.now().getYear())
				.setRenderBodyOnly(true));
		add(new WebMarkupContainer("texas").setVisible(isAprilFoolsDay(2017)));
	}

	private String getTitleSuffix() {
		if (isAprilFoolsDay(2019)) {
			return " - The Disney Clan";
		} else if (isAprilFoolsDay(2017)) {
			return " - The Texas Clan";
		}

		return " - The Tysan Clan ";
	}

	public void setAutoCollapse(boolean autoCollapse) {
		this.autoCollapse = autoCollapse;
	}

	public User getUser() {
		return getTysanSession().flatMap(TysanSession::getUser).getOrNull();
	}

	public Option<TysanSession> getTysanSession() {
		return TysanSession.session();
	}

	/**
	 * @return the notificationWindow
	 */
	public Dialog getNotificationWindow() {
		return notificationWindow;
	}

	public void setPageTitle(String title) {
		remove(headerLabel);
		remove(titleLabel);

		String nTitle = title + " - The Tysan Clan";

		headerLabel = new Label("header", title);
		titleLabel = new Label("title", nTitle);

		headerLabel.setEscapeModelStrings(false);
		titleLabel.setEscapeModelStrings(false);

		add(headerLabel);
		add(titleLabel);

	}

	private void addAnimalPanel() {
		Option<GlobalSetting> animalSetting = globalSettingDAO
				.get(AprilFools.KEY_ANIMALS);

		boolean isAprilFoolsDay2011 = isAprilFoolsDay(2011);

		if (getUser() != null && MemberUtil.isMember(getUser())) {
			if (isAprilFoolsDay2011) {

				String validOption = "";

				if (animalSetting.isDefined()) {
					validOption = animalSetting.get().getValue();
				} else {
					validOption = AprilFools.getRandomAnimalOption();
				}

				int showChance = 249;

				boolean show = showChance > AprilFools.rand.nextInt(1000);

				animalDialog = new Dialog("animals");
				animalDialog.setAutoOpen(show);
				animalDialog.setTitle("The animals!");
				animalDialog.setVisible(show);

				animalDialog.add(new ContextImage("picture",
						AprilFools.getRandomAnimal()));

				animalDialog
						.add(new AnimalOptionListView("options", validOption));

				add(animalDialog);

			} else {
				add(new WebMarkupContainer("animals").setVisible(false));
			}
		} else {
			add(new WebMarkupContainer("animals").setVisible(false));
		}
	}

	public boolean isAprilFoolsDay(int year) {
		final DateTime easternStandardTime = new DateTime(
				DateTimeZone.forID("EST"));

		return easternStandardTime.getDayOfMonth() == 1
				&& easternStandardTime.getMonthOfYear() == 4
				&& easternStandardTime.getYear() == year;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		if (isAprilFoolsDay(2019)) {
			response.render(CssHeaderItem.forUrl("css/styled.css"));
		} else if (isAprilFoolsDay(2017)) {
			response.render(CssHeaderItem.forUrl("css/texas.css"));
		} else {
			response.render(CssHeaderItem.forUrl("css/style.css"));
		}

		response.render(JavaScriptHeaderItem.forReference(
				TysanJQueryUIInitialisationResourceReference.get()));

		Integer autoTabIndex = getAutoTabIndex();

		if (autoTabIndex != null) {
			response.render(OnDomReadyHeaderItem.forScript(
					String.format("$('.jqui-tabs-auto').tabs({ active: %d });",
							autoTabIndex)));
		} else {
			response.render(OnDomReadyHeaderItem
					.forScript("$('.jqui-tabs-auto').tabs();"));
		}

		StringBuilder collapsibles = new StringBuilder();
		collapsibles.append("$('.jqui-accordion-collapsible').accordion({\n");
		collapsibles.append("\tautoHeight: true,\n");
		collapsibles.append("\theader: 'h2',\n");
		collapsibles.append("\theightStyle: 'content',\n");
		collapsibles.append("\tcollapsible: true,\n");
		if (autoCollapse) {
			collapsibles.append("\tactive: false\n");
		} else {
			collapsibles.append("\tactive: 0\n");
		}
		collapsibles.append("});");

		response.render(OnDomReadyHeaderItem.forScript(collapsibles));

		String openFirst = "$('.jqui-accordion-collapsible:first').accordion( 'option', 'active', 0 )";
		response.render(OnDomReadyHeaderItem.forScript(openFirst));
	}

	protected Integer getAutoTabIndex() {
		return null;
	}

	private class AnimalOptionListView extends ListView<String> {

		private static final long serialVersionUID = 1L;

		private final String validOption;

		public AnimalOptionListView(String id, String validOption) {
			super(id, Arrays.asList(AprilFools.getOptions()));

			this.validOption = validOption;
		}

		@Override
		protected void populateItem(ListItem<String> item) {
			String option = item.getModelObject();

			AjaxLink<String> optionLink = new AjaxLink<String>("choice",
					new Model<String>(option)) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					if (!getModelObject().equals(validOption)) {
						// Perform rickroll
						AprilFools.performRickRoll();
					} else {
						// Close window
						animalDialog.close(target);
					}
				}
			};

			optionLink.add(new Label("label", option));

			item.add(optionLink);

		}

	}

	private static enum ParamType {
		BOOLEAN {
			@Override
			protected void checkType(StringValue value)
					throws PageParameterExtractorException {
				try {
					getValue(value);
				} catch (StringValueConversionException svce) {
					throw new PageParameterExtractorException(
							"%s is not a valid boolean", value.toString());
				}
			}

			@Override
			public Object getValue(StringValue value) {
				return value.toOptionalBoolean();
			}
		}, INT {
			@Override
			protected void checkType(StringValue value)
					throws PageParameterExtractorException {
				try {
					getValue(value);
				} catch (StringValueConversionException svce) {
					throw new PageParameterExtractorException(
							"%s is not a valid integer", value.toString());
				}
			}

			@Override
			public Object getValue(StringValue value) {
				return value.toOptionalInteger();
			}
		}, LONG {
			@Override
			protected void checkType(StringValue value)
					throws PageParameterExtractorException {
				try {
					getValue(value);
				} catch (StringValueConversionException svce) {
					throw new PageParameterExtractorException(
							"%s is not a valid long", value.toString());
				}
			}

			@Override
			public Object getValue(StringValue value) {
				return value.toOptionalLong();
			}
		}, STRING {
			@Override
			protected void checkType(StringValue value)
					throws PageParameterExtractorException {
				try {
					getValue(value);
				} catch (StringValueConversionException svce) {
					throw new PageParameterExtractorException(
							"%s is not a valid string", value.toString());
				}
			}

			@Override
			public Object getValue(StringValue value) {
				return value.toOptionalString();
			}
		};

		public void check(PageParameters params, String identifier,
				boolean required) throws PageParameterExtractorException {
			StringValue value = params.get(identifier);

			if (value.isEmpty() || value.isNull()) {
				if (required) {
					throw new PageParameterExtractorException(
							"Required parameter %s is null or empty",
							identifier);
				}
			}

			checkType(value);

		}

		protected abstract void checkType(StringValue value)
				throws PageParameterExtractorException;

		public abstract Object getValue(StringValue value);
	}

	protected static PageParameterExtractorBuilder optionalBoolean(
			String identifier) {
		return new PageParameterExtractorBuilder(identifier, ParamType.BOOLEAN,
				false);
	}

	protected static PageParameterExtractorBuilder optionalInt(
			String identifier) {
		return new PageParameterExtractorBuilder(identifier, ParamType.INT,
				false);
	}

	protected static PageParameterExtractorBuilder optionalLong(
			String identifier) {
		return new PageParameterExtractorBuilder(identifier, ParamType.LONG,
				false);
	}

	protected static PageParameterExtractorBuilder optionalString(
			String identifier) {
		return new PageParameterExtractorBuilder(identifier, ParamType.STRING,
				false);
	}

	protected static PageParameterExtractorBuilder requiredBoolean(
			String identifier) {
		return new PageParameterExtractorBuilder(identifier, ParamType.BOOLEAN,
				true);
	}

	protected static PageParameterExtractorBuilder requiredInt(
			String identifier) {
		return new PageParameterExtractorBuilder(identifier, ParamType.INT,
				true);
	}

	protected static PageParameterExtractorBuilder requiredLong(
			String identifier) {
		return new PageParameterExtractorBuilder(identifier, ParamType.LONG,
				true);
	}

	protected static PageParameterExtractorBuilder requiredString(
			String identifier) {
		return new PageParameterExtractorBuilder(identifier, ParamType.STRING,
				true);
	}

	protected static class PageParameterExtractor {
		private final List<Object> constructorParams;

		private PageParameterExtractor() {
			constructorParams = Lists.newArrayList();
		}

		private void addValue(Object value) {
			constructorParams.add(value);
		}

		public <T> T toClass(Class<T> targetClass)
				throws PageParameterExtractorException {
			Class<?>[] pClass = new Class<?>[constructorParams.size()];
			Object[] p = new Object[constructorParams.size()];
			int i = 0;
			for (Object v : constructorParams) {

				p[i] = v;
				pClass[i++] = v.getClass();
			}

			try {
				Constructor<T> con = targetClass.getConstructor(pClass);
				return con.newInstance(p);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new PageParameterExtractorException(
						"Cannot store parameters in target class: %s",
						e.getMessage());
			}
		}

	}

	protected static class PageParameterExtractorException extends Exception {
		private static final long serialVersionUID = 1L;

		public PageParameterExtractorException(String message,
				Object... params) {
			super(String.format(message, params));
		}

	}

	protected static class PageParameterExtractorBuilder {
		private final String identifier;

		private final ParamType paramType;

		private final boolean required;

		private final PageParameterExtractorBuilder prev;

		private PageParameterExtractorBuilder(String identifier,
				ParamType paramType, boolean required) {
			this(identifier, paramType, required, null);
		}

		private PageParameterExtractorBuilder(String identifier,
				ParamType paramType, boolean required,
				PageParameterExtractorBuilder prev) {
			super();
			this.identifier = identifier;
			this.paramType = paramType;
			this.prev = prev;
			this.required = required;
		}

		public PageParameterExtractorBuilder optionalBoolean(
				String identifier) {
			return new PageParameterExtractorBuilder(identifier,
					ParamType.BOOLEAN, false, this);
		}

		public PageParameterExtractorBuilder optionalInt(String identifier) {
			return new PageParameterExtractorBuilder(identifier, ParamType.INT,
					false, this);
		}

		public PageParameterExtractorBuilder optionalLong(String identifier) {
			return new PageParameterExtractorBuilder(identifier, ParamType.LONG,
					false, this);
		}

		public PageParameterExtractorBuilder optionalString(String identifier) {
			return new PageParameterExtractorBuilder(identifier,
					ParamType.STRING, false, this);
		}

		public PageParameterExtractorBuilder requiredBoolean(
				String identifier) {
			return new PageParameterExtractorBuilder(identifier,
					ParamType.BOOLEAN, true, this);
		}

		public PageParameterExtractorBuilder requiredInt(String identifier) {
			return new PageParameterExtractorBuilder(identifier, ParamType.INT,
					true, this);
		}

		public PageParameterExtractorBuilder requiredLong(String identifier) {
			return new PageParameterExtractorBuilder(identifier, ParamType.LONG,
					true, this);
		}

		public PageParameterExtractorBuilder requiredString(String identifier) {
			return new PageParameterExtractorBuilder(identifier,
					ParamType.STRING, true, this);
		}

		public PageParameterExtractor forParameters(PageParameters params)
				throws PageParameterExtractorException {
			check(params);

			PageParameterExtractor extractor = new PageParameterExtractor();
			addType(extractor, params);

			return extractor;
		}

		private void addType(PageParameterExtractor extractor,
				PageParameters params) {
			if (prev != null) {
				prev.addType(extractor, params);
			}

			extractor.addValue(paramType.getValue(params.get(identifier)));
		}

		private void check(PageParameters params)
				throws PageParameterExtractorException {
			paramType.check(params, identifier, required);
			if (prev != null) {
				prev.check(params);
			}
		}
	}
}
