package com.tysanclan.site.projectewok.components;

import org.apache.wicket.resource.JQueryPluginResourceReference;

public class JQueryTextareaCaretPluginResource
		extends JQueryPluginResourceReference {
	private static final long serialVersionUID = 1L;

	private static final JQueryTextareaCaretPluginResource instance = new JQueryTextareaCaretPluginResource();

	private JQueryTextareaCaretPluginResource() {
		super(JQueryTextareaCaretPluginResource.class,
				"js/jquery.textarea.caret.js");
	}

	public static JQueryTextareaCaretPluginResource get() {
		return instance;
	}
}
