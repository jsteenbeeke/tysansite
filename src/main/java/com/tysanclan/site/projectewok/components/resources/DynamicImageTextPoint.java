package com.tysanclan.site.projectewok.components.resources;

import java.awt.*;

public class DynamicImageTextPoint {
	private final String text;
	private final int x;
	private final int y;

	public DynamicImageTextPoint(String text, int x, int y) {
		this.text = text;
		this.x = x;
		this.y = y;
	}

	public String getText() {
		return text;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/**
	 * Override this to edit the graphics just before the draw
	 * @param graphics
	 */
	protected void onBeforeDraw(Graphics graphics) {
	}
}
