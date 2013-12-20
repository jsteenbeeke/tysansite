package com.tysanclan.site.projectewok.util.bbcode.ast;

import com.tysanclan.site.projectewok.util.bbcode.BBAstNode;
import com.tysanclan.site.projectewok.util.bbcode.DefaultNode;

public class BoldNode extends DefaultNode {

	public BoldNode(BBAstNode parent) {
		super(parent);
	}

	@Override
	public void renderTo(StringBuilder builder) {
		builder.append("<b>");

		for (BBAstNode child : getChildren()) {
			child.renderTo(builder);
		}

		builder.append("</b>");

	}
}
