package com.tysanclan.site.projectewok.util.bbcode.ast;

import com.tysanclan.site.projectewok.util.bbcode.BBAstNode;
import com.tysanclan.site.projectewok.util.bbcode.DefaultNode;

public class UnderlineNode extends DefaultNode {

	public UnderlineNode(BBAstNode parent) {
		super(parent);
	}

	@Override
	public void renderTo(StringBuilder builder) {
		builder.append("<span class=\"underline\">");

		for (BBAstNode child : getChildren()) {
			child.renderTo(builder);
		}

		builder.append("</span>");

	}
}
