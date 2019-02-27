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

import com.google.common.collect.ImmutableList;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.wicketstuff.wiquery.ui.JQueryUIJavaScriptResourceReference;

import java.util.List;

public class TysanJQueryUIInitialisationResourceReference extends
		JavaScriptResourceReference {
	private static final long serialVersionUID = 4490574626493003959L;

	private static TysanJQueryUIInitialisationResourceReference instance = new TysanJQueryUIInitialisationResourceReference();

	private TysanJQueryUIInitialisationResourceReference() {
		super(TysanJQueryUIInitialisationResourceReference.class,
				"tysan.jq-ui.init.js");
	}

	public static TysanJQueryUIInitialisationResourceReference get() {
		return instance;
	}

	@Override
	public List<HeaderItem> getDependencies() {
		return ImmutableList.of(
				JavaScriptHeaderItem.forReference(JQueryUIJavaScriptResourceReference.get())
		);
	}
}
