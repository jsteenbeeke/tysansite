package com.tysanclan.site.projectewok.util.bbcode;

import java.util.List;

public interface BBAstNode {
	List<BBAstNode> getChildren();

	BBAstNode getParent();

	void renderTo(StringBuilder builder);

	void addChild(BBAstNode node);
}
