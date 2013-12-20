package com.tysanclan.site.projectewok.util.bbcode.ast;

import com.tysanclan.site.projectewok.util.bbcode.BBAstNode;
import com.tysanclan.site.projectewok.util.bbcode.DefaultNode;

public class ItalicNode extends DefaultNode {

	public ItalicNode(BBAstNode parent) {
		super(parent);
	}

	@Override
	public void renderTo(StringBuilder builder) {
		builder.append("<i>");

		for (BBAstNode child : getChildren()) {
			child.renderTo(builder);
		}

		builder.append("</i>");

	}
}
