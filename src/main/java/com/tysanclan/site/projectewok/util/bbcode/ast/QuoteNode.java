package com.tysanclan.site.projectewok.util.bbcode.ast;

import com.tysanclan.site.projectewok.util.bbcode.BBAstNode;
import com.tysanclan.site.projectewok.util.bbcode.DefaultNode;

public class QuoteNode extends DefaultNode {
	private final String user;

	public QuoteNode(BBAstNode parent, String user) {
		super(parent);

		this.user = user;
	}

	@Override
	public void renderTo(StringBuilder builder) {
		builder.append("<blockquote><p><b>");

		if (user == null) {
			builder.append("Quote");
		} else {
			builder.append("Quoting ");
			builder.append(user);
		}

		builder.append("</b></p> <p>");
		for (BBAstNode node : getChildren()) {
			node.renderTo(builder);
		}
		builder.append("</p></blockquote>");
	}
}
