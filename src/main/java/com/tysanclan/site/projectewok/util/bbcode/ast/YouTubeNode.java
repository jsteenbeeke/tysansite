package com.tysanclan.site.projectewok.util.bbcode.ast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tysanclan.site.projectewok.util.bbcode.BBAstNode;
import com.tysanclan.site.projectewok.util.bbcode.DefaultNode;

public class YouTubeNode extends DefaultNode {
	public static final String PATTERN = "(\\w|_|-|[a-zA-Z]|[0-9])+";

	public static final String URL_PATTERN = "http(s)?://www\\.youtube\\.com/watch\\?v=((\\w|_|-|[a-zA-Z]|[0-9])+)";

	public YouTubeNode(BBAstNode parent) {
		super(parent);
	}

	@Override
	public void renderTo(StringBuilder builder) {
		StringBuilder children = new StringBuilder();
		for (BBAstNode node : getChildren()) {
			node.renderTo(children);
		}

		String videoId = children.toString();

		if (videoId.matches(PATTERN)) {
			builder.append("<div class=\"youtube-replace\">").append(videoId)
					.append("</div>");

			// builder.append("<iframe id=\"ytplayer-");
			// builder.append(videoId);
			// builder.append("\" type=\"text/html\" width=\"560\" height=\"315\" src=\"http://www.youtube.com/embed/");
			// builder.append(videoId);
			// builder.append("?origin=https://www.tysanclan.com/\" frameborder=\"0\" />");
		} else {
			Matcher matcher = Pattern.compile(URL_PATTERN).matcher(videoId);
			if (matcher.matches()) {
				videoId = matcher.group(2);

				builder.append("<div class=\"youtube-replace\">")
						.append(videoId).append("</div>");
				//
				// builder.append("<iframe id=\"ytplayer-");
				// builder.append(videoId);
				// builder.append("\" type=\"text/html\" width=\"560\" height=\"315\" src=\"http://www.youtube.com/embed/");
				// builder.append(videoId);
				// builder.append("?origin=https://www.tysanclan.com/\" frameborder=\"0\" />");
			} else {
				builder.append(videoId);
			}
		}

	}
}