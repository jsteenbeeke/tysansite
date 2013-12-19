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
