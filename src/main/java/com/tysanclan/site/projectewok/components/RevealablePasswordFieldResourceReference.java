package com.tysanclan.site.projectewok.components;

import org.apache.wicket.resource.JQueryPluginResourceReference;

public class RevealablePasswordFieldResourceReference
		extends JQueryPluginResourceReference {
	private static final long serialVersionUID = 1L;

	private static final RevealablePasswordFieldResourceReference REFERENCE = new RevealablePasswordFieldResourceReference();

	private RevealablePasswordFieldResourceReference() {
		super(RevealablePasswordFieldResourceReference.class,
				"js/revealable.js");
	}

	public static RevealablePasswordFieldResourceReference get() {
		return REFERENCE;
	}

}
