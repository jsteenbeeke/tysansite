package com.tysanclan.site.projectewok.components;

import java.util.List;

import com.google.common.collect.ImmutableList;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.resource.JQueryPluginResourceReference;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class BBCodeTextAreaPluginResource extends JQueryPluginResourceReference {
	private static final long serialVersionUID = 1L;

	private static final BBCodeTextAreaPluginResource instance = new BBCodeTextAreaPluginResource();

	private BBCodeTextAreaPluginResource() {
		super(BBCodeTextAreaPluginResource.class, "js/bbcode.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> deps = Lists.newArrayListWithExpectedSize(1);

		deps.add(JavaScriptHeaderItem
				.forReference(JQueryTextareaCaretPluginResource.get()));

		return ImmutableList.copyOf(Iterables.concat(super.getDependencies(), deps));
	}

	public static BBCodeTextAreaPluginResource get() {
		return instance;
	}
}
