package com.tysanclan.site.projectewok.components;

import org.apache.wicket.resource.JQueryPluginResourceReference;

public class BBCodePanelInitScriptResource extends
		JQueryPluginResourceReference {
	private static final long serialVersionUID = 1L;

	private static final BBCodePanelInitScriptResource instance = new BBCodePanelInitScriptResource();

	private BBCodePanelInitScriptResource() {
		super(BBCodePanelInitScriptResource.class, "js/bbpanel.js");
	}

	public static BBCodePanelInitScriptResource get() {
		return instance;
	}
}
