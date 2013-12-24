package com.tysanclan.site.projectewok.util.bbcode.ast;

import com.tysanclan.site.projectewok.util.bbcode.BBAstNode;
import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;
import com.tysanclan.site.projectewok.util.bbcode.DefaultNode;

public class ImageNode extends DefaultNode {
	private Integer width;

	private Integer height;

	public ImageNode(BBAstNode parent) {
		this(parent, null, null);
	}

	public ImageNode(BBAstNode parent, Integer width, Integer height) {
		super(parent);
		this.width = width;
		this.height = height;
	}

	@Override
	public void renderTo(StringBuilder builder) {
		StringBuilder children = new StringBuilder();
		for (BBAstNode node : getChildren()) {
			node.renderTo(children);
		}

		builder.append("<img alt=\"");
		builder.append(BBCodeUtil.filterURL(children.toString()));
		builder.append("\" ");

		if (width != null || height != null) {
			builder.append("style=\"");
			if (width != null) {
				builder.append("width: ");
				builder.append(width);
				builder.append("px;");

				if (height != null) {
					builder.append(' ');
				}
			}
			if (height != null) {
				builder.append("height: ");
				builder.append(height);
				builder.append("px;");
			}
			builder.append("\" ");
		}
		builder.append("src=\"");
		builder.append(BBCodeUtil.filterURL(children.toString()));
		builder.append("\" />");
	}
}
