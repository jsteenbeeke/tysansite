package com.tysanclan.site.projectewok.util.bbcode;

class BBTokenStream {
	private String input;
	private int position;

	public BBTokenStream(String input) {
		this.input = input != null ? input : "";
		this.position = 0;
	}

	public void consumeToken() {
		this.position++;
	}

	public void consumeTokens(int n) {
		this.position += n;
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

	public String peekUntil(char close) {
		StringBuilder lookahead = new StringBuilder();

		for (int i = position; i < input.length(); i++) {
			char next = input.charAt(i);

			if (next == close) {
				return lookahead.toString();
			}

			lookahead.append(next);

		}

		return null; // Unclosed tag, parse as literal
	}

	public int getRemainingTokens() {
		return input.length() - position;
	}

}