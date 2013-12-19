/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.util;

import java.util.HashMap;
import java.util.Map;

import com.tysanclan.site.projectewok.TysanSession;

/**
 * Utilities for ensuring HTML is properly sanitized before being stored in the
 * database and sent to the user. This way we can prevent XSS vulnerabilities.
 * 
 * @author Jeroen Steenbeeke
 */
public class HTMLSanitizer {
	static class HTMLAttributeParser {
		private enum MODE {
			ATTRNAME, EQUALS, INQUOTE, WHITESPACE
		}

		private Map<String, String> attributes;

		public HTMLAttributeParser(HTMLTokenStream stream) {
			attributes = new HashMap<String, String>();

			String current = "";
			String attrname = "";

			MODE currentMode = MODE.WHITESPACE;

			while (stream.hasMoreTokens() && stream.nextToken() != '>') {
				char next = stream.nextToken();

				// Short circuit combined tag
				if (next == '/' && stream.peekTokens(2).equals("/>")) {
					stream.consumeToken();
					continue;
				}

				switch (currentMode) {
					case WHITESPACE:
						if (!Character.isWhitespace(next)) {
							currentMode = MODE.ATTRNAME;
							current = "";
							continue;
						}

						break;
					case ATTRNAME:
						if (Character.isWhitespace(next)) {
							currentMode = MODE.WHITESPACE;
							// Invalid attribute, skip
							current = "";
							continue;
						} else if (next == '=') {
							currentMode = MODE.EQUALS;
							continue;
						}

						current += next;
						break;
					case EQUALS:
						if (next != '=') {
							currentMode = MODE.WHITESPACE;
							continue;
						}
						stream.consumeToken();
						next = stream.nextToken();
						if (next != '"') {
							currentMode = MODE.WHITESPACE;
							continue;
						}

						attrname = current;
						current = "";

						currentMode = MODE.INQUOTE;
						break;
					case INQUOTE:
						if (next == '"') {
							attributes.put(attrname, current);

							stream.consumeToken();
							currentMode = MODE.WHITESPACE;
							continue;
						}

						current += next;
						break;
					default:
						break;
				}

				stream.consumeToken();
			}

		}

		public String getAttribute(String name) {
			return attributes.containsKey(name) ? attributes.get(name) : "";
		}

	}

	/**
	 * @author Jeroen Steenbeeke
	 */
	static class HTMLTokenStream {
		private String input;
		private int position;

		public HTMLTokenStream(String input) {
			this.input = input != null ? input : "";
			this.position = 0;
		}

		public void consumeToken() {
			this.position++;
		}

		public boolean hasMoreTokens() {
			return position < input.length();
		}

		public char nextToken() {
			return input.charAt(position);
		}

		public String peekTokens(int lookahead) {
			if (input.length() < (position + lookahead)) {
				return input.substring(position);
			}

			return input.substring(position, position + lookahead);
		}

		public void skipTokens(int count) {
			this.position = position + count;
		}
	}

	private static final String[] allowedTags = { "img", "br", "hr",
			"blockquote", "/blockquote", "a", "/a", "span", "/span", "em",
			"/em", "p", "/p", "strong", "/strong", "ul", "/ul", "li", "/li",
			"ol", "/ol", "object", "/object", "embed", "/embed", "param",
			"/param", "div", "/div" };

	private static boolean checkScript(String input) {
		if (input.indexOf(':') != -1) {
			return input.startsWith("http://") || input.startsWith("https://");
		}

		return true;
	}

	public static String filterURL(String url) {
		if (url == null)
			return null;

		if (url.startsWith("http://") || url.startsWith("https://")
				|| url.startsWith("/")) {
			return url;
		}

		return "";
	}

	private static String consumeTag(String tag, HTMLTokenStream stream) {
		String res = "<" + tag;

		stream.skipTokens(tag.length());

		if (tag.equalsIgnoreCase("img")) {
			HTMLAttributeParser att = new HTMLAttributeParser(stream);

			String src = att.getAttribute("src");
			if (checkScript(src) && !src.isEmpty()) {
				res += " src=\"" + src + "\"";
			} else {
				stream.consumeToken(); // Closing brace
				return "";
			}

			String alt = att.getAttribute("alt");
			res += " alt=\"" + alt + "\"";

			String width = att.getAttribute("width");
			if (!width.isEmpty()) {
				try {
					res += " width=\"" + Integer.parseInt(width) + "\"";
				} catch (NumberFormatException nfe) {
					// Ignore width if not an actual integer
				}
			}

			String height = att.getAttribute("height");
			if (!height.isEmpty()) {
				try {
					res += " height=\"" + Integer.parseInt(height) + "\"";
				} catch (NumberFormatException nfe) {
					// Ignore height tag if not an actual integer
				}
			}

			res += " /";

		} else if (tag.equalsIgnoreCase("a")) {
			HTMLAttributeParser att = new HTMLAttributeParser(stream);

			String src = att.getAttribute("href");
			if (checkScript(src)) {
				res += " href=\"" + src + "\"";
			} else {
				stream.consumeToken(); // Closing brace
				return "";
			}
		} else if (tag.equalsIgnoreCase("span")) {
			HTMLAttributeParser att = new HTMLAttributeParser(stream);

			String style = att.getAttribute("style");

			if (style.equals("text-decoration: underline;")) {
				res += " style=\"text-decoration: underline;\"";
			}
		} else if (tag.equalsIgnoreCase("br")) {
			new HTMLAttributeParser(stream);

			res += " /";
		} else if (tag.equalsIgnoreCase("object")) {
			// Only allow object tags for members
			if (!TysanSession.exists()) {
				return "";
			}
			TysanSession ts = TysanSession.get();
			if (ts == null || !MemberUtil.isMember(ts.getUser())) {
				return "";
			}

			HTMLAttributeParser att = new HTMLAttributeParser(stream);

			String width = att.getAttribute("width");

			if (!width.isEmpty()) {
				try {
					res += " width=\"" + Integer.parseInt(width) + "\"";
				} catch (NumberFormatException nfe) {
					// Ignore width if not an actual integer
				}
			}

			String height = att.getAttribute("height");

			if (!height.isEmpty()) {
				try {
					res += " height=\"" + Integer.parseInt(height) + "\"";
				} catch (NumberFormatException nfe) {
					// Ignore height tag if not an actual integer
				}
			}
		} else if (tag.equalsIgnoreCase("param")) {
			// Only allow param tags for members
			if (!TysanSession.exists()) {
				return "";
			}
			TysanSession ts = TysanSession.get();
			if (ts == null || !MemberUtil.isMember(ts.getUser())) {
				return "";
			}

			HTMLAttributeParser att = new HTMLAttributeParser(stream);

			String name = att.getAttribute("name");
			res += " name=\"" + name + "\"";

			String value = att.getAttribute("value");
			res += " value=\"" + value + "\"";

		} else if (tag.equalsIgnoreCase("embed")) {
			// Only allow embed tags for members
			if (!TysanSession.exists()) {
				return "";
			}

			TysanSession ts = TysanSession.get();
			if (ts == null || !MemberUtil.isMember(ts.getUser())) {
				return "";
			}

			HTMLAttributeParser att = new HTMLAttributeParser(stream);

			String width = att.getAttribute("width");

			if (!width.isEmpty()) {
				try {
					res += " width=\"" + Integer.parseInt(width) + "\"";
				} catch (NumberFormatException nfe) {
					// Ignore width if not an actual integer
				}
			}

			String height = att.getAttribute("height");

			if (!height.isEmpty()) {
				try {
					res += " height=\"" + Integer.parseInt(height) + "\"";
				} catch (NumberFormatException nfe) {
					// Ignore height tag if not an actual integer
				}
			}

			String type = att.getAttribute("type");

			if (!type.equals("application/x-shockwave-flash")) {
				return "";
			}

			String src = att.getAttribute("src");

			src = filterURL(src);

			if (src.isEmpty()) {
				return "";
			}

			res += " src=\"" + src + "\" allowScriptAccess=\"never\"";
		}

		// Consume closing brace
		stream.consumeToken();

		return res + ">";
	}

	private static String parseTag(HTMLTokenStream stream) {
		String res = "";
		stream.consumeToken();

		boolean found = false;
		for (String atag : allowedTags) {
			String peeked = stream.peekTokens(atag.length() + 1);
			if (peeked.equalsIgnoreCase(atag + " ")
					|| peeked.equalsIgnoreCase(atag + ">")) {
				res = consumeTag(atag, stream);
				found = true;
				break;
			}
		}

		if (!found) {
			res = "&lt;";
		}

		return res;
	}

	/**
	 * Removes all harmful tags and attributes from a String
	 * 
	 * @param original
	 *            The original String
	 * @return The sanitized String
	 */
	public static String sanitize(String original) {
		StringBuilder res = new StringBuilder();
		HTMLTokenStream stream = new HTMLTokenStream(original);

		while (stream.hasMoreTokens()) {
			char next = stream.nextToken();

			switch (next) {
				// Ignore malformed US-ASCII
				case 188:
				case 190:
					stream.consumeToken();
					break;
				// Open tag
				case '<':
					res.append(parseTag(stream));
					break;
				// Out of context close tag
				case '>':
					res.append("&gt;");
					stream.consumeToken();
					break;
				// Quote character
				case '"':
					res.append("&quot;");
					stream.consumeToken();
					break;
				// Anything else
				default:
					res.append(stream.nextToken());
					stream.consumeToken();
			}
		}

		return res.toString();
	}

	/**
	 * Turns all tags in a String into HTML entities
	 * 
	 * @param original
	 *            The original string
	 * @return A String that has no HTML in it, only text and entities
	 */
	public static String stripTags(String original) {
		if (original == null) {
			return "";
		}

		StringBuilder res = new StringBuilder();

		for (int i = 0; i < original.length(); i++) {
			char next = original.charAt(i);
			switch (next) {
				case '<':
					res.append("&lt;");
					break;
				case '>':
					res.append("&gt;");
					break;
				case '&':
					res.append("&amp;");
					break;
				case '"':
					res.append("&quot;");
					break;
				default:
					res.append(next);
			}
		}

		return res.toString();
	}

	public static String newlinesToParagraphs(String content) {
		StringBuilder res = new StringBuilder();

		res.append("<p>");

		int n = content.length(), i = 0;

		for (char c : content.toCharArray()) {
			if (i++ != (n - 1) && c == '\n') {
				res.append("</p>");
				res.append("<p>");
			} else if (c != '\n') {
				res.append(c);
			}
		}

		res.append("</p>");

		return res.toString();
	}

	public static String paragraphsToNewlines(String content) {
		HTMLTokenStream stream = new HTMLTokenStream(content);

		StringBuilder result = new StringBuilder();

		while (stream.hasMoreTokens()) {
			if (stream.peekTokens(4).equals("</p>")) {
				stream.skipTokens(4);
				result.append("\n");
			} else if (stream.peekTokens(3).equals("<p>")) {
				stream.skipTokens(3);
			} else {
				result.append(sanitize(Character.toString(stream.nextToken())));
				stream.consumeToken();
			}
		}

		return result.toString();
	}
}
