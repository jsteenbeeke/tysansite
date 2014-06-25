package com.tysanclan.site.projectewok.components.resources.silverblue;

import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import com.google.common.collect.Lists;

public class SilverblueBootstrapJavaScriptReference extends JavaScriptResourceReference {
	private static final long serialVersionUID = 1L;

	private final Iterable<HeaderItem> dependencies = initDependencies();

	private static final SilverblueBootstrapJavaScriptReference instance = new SilverblueBootstrapJavaScriptReference();

	public SilverblueBootstrapJavaScriptReference() {
		super(SilverblueBootstrapJavaScriptReference.class, "js/bootstrap.js");
	}

	public static SilverblueBootstrapJavaScriptReference get() {
		return instance;
	}

	@Override
	public Iterable<? extends HeaderItem> getDependencies() {
		return dependencies;
	}

	private Iterable<HeaderItem> initDependencies() {
		List<HeaderItem> dependencies = Lists.newArrayList();
		dependencies.add(JavaScriptHeaderItem.forReference(Application.get()
				.getJavaScriptLibrarySettings().getJQueryReference()));
		dependencies.add(CssReferenceHeaderItem.forUrl("css/silverblue.css"));

		return dependencies;
	}
}
