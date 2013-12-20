package com.tysanclan.site.projectewok.util.bbcode;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public abstract class DefaultNode implements BBAstNode {
	private final List<BBAstNode> children = Lists.newLinkedList();

	private final BBAstNode parent;

	protected DefaultNode(BBAstNode parent) {
		this.parent = parent;
	}

	@Override
	public void addChild(BBAstNode node) {
		children.add(node);
	}

	@Override
	public List<BBAstNode> getChildren() {
		return ImmutableList.copyOf(children);
	}

	@Override
	public BBAstNode getParent() {
		return parent;
	}

}
