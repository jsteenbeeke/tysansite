package com.tysanclan.site.projectewok.util.bbcode;

/**
 * Class for parsing BBCode and outputting HTML
 *  
 * @author steenbeeke
 */
public class BBCodeUtil {
	private static final String[] ENTITIES = { "&lt;", "&gt;", "&amp;",
			"&quot;" };

	private BBCodeUtil() {

	}

	public static String filterURL(String url) {
		if (url == null)
			return "#";

		if (url.startsWith("http://") || url.startsWith("https://")
				|| url.startsWith("/")) {
			return url;
		}

		return "#";
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
					final String on = original.substring(i);
					boolean replace = true;

					for (String ent : ENTITIES) {
						if (on.startsWith(ent)) {
							replace = false;
							break;
						}
					}

					if (replace) {
						res.append("&amp;");
					} else {
						res.append(next);
					}
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

	public static BBCodeVerification verify(String input) {
		BBParser parser = new BBParser(input);
		try {
			parser.getAbstractSyntaxTree();
		} catch (BBParseException e) {
			return BBCodeVerification.error(e.getMessage());
		}
		return BBCodeVerification.ok();
	}

	public static String toHtml(String input) throws BBParseException {
		BBParser parser = new BBParser(input);
		BBAstNode tree = parser.getAbstractSyntaxTree();
		StringBuilder sb = new StringBuilder();
		tree.renderTo(sb);

		return sb.toString();
	}

	public static class BBCodeVerification {
		private final boolean ok;

		private final String errorMessage;

		private BBCodeVerification(boolean ok, String errorMessage) {
			super();
			this.ok = ok;
			this.errorMessage = errorMessage;
		}

		public boolean isOk() {
			return ok;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		static BBCodeVerification ok() {
			return new BBCodeVerification(true, null);
		}

		static BBCodeVerification error(String message) {
			return new BBCodeVerification(false, message);
		}
	}
}
