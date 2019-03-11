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
package com.tysanclan.site.projectewok.pages.member;

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.jeroensteenbeeke.hyperion.meld.SearchFilter;
import com.jeroensteenbeeke.hyperion.meld.filter.StringFilterField;
import com.jeroensteenbeeke.hyperion.solstice.data.FilterDataProvider;
import com.tysanclan.site.projectewok.components.OtterSniperPanel;
import com.tysanclan.site.projectewok.entities.LogItem;
import com.tysanclan.site.projectewok.entities.dao.LogItemDAO;
import com.tysanclan.site.projectewok.entities.filter.LogItemFilter;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Jeroen Steenbeeke
 */
public class LogPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private LogItemDAO logItemDAO;

	public LogPage() {
		this("", "", "");
	}

	public LogPage(@Nonnull String categoryText, @Nonnull String messageText, @Nonnull String usernameText) {
		super("Clan Log");

		LogItemFilter filter = new LogItemFilter();
		if (!messageText.isEmpty()) {
			apply(filter, LogItemFilter::message, LogItemFilter::orMessage, messageText);

			setPageTitle("Clan Log Search Results");
		}
		if (!categoryText.isEmpty()) {
			apply(filter, LogItemFilter::category, LogItemFilter::orCategory, categoryText);

			setPageTitle("Clan Log Search Results");
		}
		if (!usernameText.isEmpty()) {
			if ("system".equalsIgnoreCase(usernameText)) {
				filter.user().isNull();
			} else {
				filter.user(apply(new UserFilter(), UserFilter::username, UserFilter::orUsername, usernameText));
				setPageTitle("Clan Log Search Results");
			}
		}
		filter.logTime().orderBy(false);

		DataView<LogItem> pageable = new DataView<LogItem>("log",
														   FilterDataProvider.of(filter, logItemDAO)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<LogItem> item) {
				LogItem logItem = item.getModelObject();

				item.add(new Label("time", DateUtil.getESTFormattedString(
						new Date(logItem.getLogTime()))));
				item.add(new Label("category", logItem.getCategory()));
				item.add(new Label("user", logItem.getVisibleUsername()));
				item.add(new Label("action", logItem.getMessage()));

			}

		};

		pageable.setItemsPerPage(20);

		TextField<String> messageField = new TextField<>("message", Model.of(messageText));
		TextField<String> categoryField = new TextField<>("category", Model.of(categoryText));
		TextField<String> userField = new TextField<>("user", Model.of(usernameText));

		Form<LogItem> filterForm = new Form<LogItem>("filterform") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				String categorySelection = valueOf(categoryField);
				String messageSelection = valueOf(messageField);
				String userSelection = valueOf(userField);

				setResponsePage(new LogPage(categorySelection, messageSelection, userSelection));
			}

			private String valueOf(TextField<String> textField) {
				return Optional.ofNullable(textField).map(TextField::getModelObject)
							   .orElse("");
			}
		};
		filterForm.add(messageField);
		filterForm.add(categoryField);
		filterForm.add(userField);
		add(filterForm);


		add(pageable);
		add(new OtterSniperPanel("otterSniperPanel", 1));

		add(new PagingNavigator("nav", pageable));
	}

	private <E extends DomainObject, T extends SearchFilter<E,T>> T apply(T filter,
																		 Function<T, StringFilterField<E,String,?>> first,
																		 Function<T, StringFilterField<E,String,?>> subsequent,
																		 String search) {
		String needle = search.replaceAll("%", "");

		first.apply(filter).ilike("%"+ needle);
		subsequent.apply(filter).ilike(needle + "%");
		subsequent.apply(filter).ilike("%"+ needle + "%");

		return filter;
	}
}
