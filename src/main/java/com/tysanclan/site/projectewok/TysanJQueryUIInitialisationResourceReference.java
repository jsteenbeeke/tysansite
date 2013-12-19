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
package com.tysanclan.site.projectewok;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.odlabs.wiquery.core.resources.JavaScriptHeaderItems;
import org.odlabs.wiquery.ui.accordion.AccordionJavaScriptResourceReference;
import org.odlabs.wiquery.ui.dialog.DialogJavaScriptResourceReference;
import org.odlabs.wiquery.ui.tabs.TabsJavaScriptResourceReference;

public class TysanJQueryUIInitialisationResourceReference extends
		JavaScriptResourceReference {
	private static final long serialVersionUID = 4490574626493003959L;

	private static TysanJQueryUIInitialisationResourceReference instance = new TysanJQueryUIInitialisationResourceReference();

	/**
	 * Builds a new instance of {@link DialogJavaScriptResourceReference}.
	 */
	private TysanJQueryUIInitialisationResourceReference() {
		super(TysanJQueryUIInitialisationResourceReference.class,
				"tysan.jq-ui.init.js");
	}

	/**
	 * Returns the {@link DialogJavaScriptResourceReference} instance.
	 */
	public static TysanJQueryUIInitialisationResourceReference get() {
		return instance;
	}

	@Override
	public Iterable<? extends HeaderItem> getDependencies() {
		return JavaScriptHeaderItems.forReferences(
				AccordionJavaScriptResourceReference.get(),
				TabsJavaScriptResourceReference.get());
	}
}
