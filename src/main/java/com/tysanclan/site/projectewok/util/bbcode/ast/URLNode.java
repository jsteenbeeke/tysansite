package com.tysanclan.site.projectewok.util.bbcode.ast;

import com.tysanclan.site.projectewok.util.bbcode.BBAstNode;
import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;
import com.tysanclan.site.projectewok.util.bbcode.DefaultNode;

public class URLNode extends DefaultNode {
	private final String link;

	public URLNode(BBAstNode parent, String link) {
		super(parent);

		this.link = link;
	}

	@Override
	public void renderTo(StringBuilder builder) {
		builder.append("<a href=\"");

		StringBuilder children = new StringBuilder();
		for (BBAstNode node : getChildren()) {
			node.renderTo(children);
		}

		if (link == null) {
			// Child nodes are link
			builder.append(BBCodeUtil.filterURL(children.toString()));
			builder.append("\">");
			builder.append(children.toString());
		} else {
			// Child nodes are descriptive text
			builder.append(BBCodeUtil.filterURL(link));
			builder.append("\">");
			builder.append(children.toString());
		}

		builder.append("</a>");
	}
}
