package com.tysanclan.site.projectewok.util.bbcode.ast;

import com.tysanclan.site.projectewok.util.bbcode.BBAstNode;
import com.tysanclan.site.projectewok.util.bbcode.DefaultNode;

public class StrikeThroughNode extends DefaultNode {
	public StrikeThroughNode(BBAstNode parent) {
		super(parent);
	}

	@Override
	public void renderTo(StringBuilder builder) {
		builder.append("<span class=\"strikethrough\">");

		for (BBAstNode child : getChildren()) {
			child.renderTo(builder);
		}

		builder.append("</span>");

	}
}
