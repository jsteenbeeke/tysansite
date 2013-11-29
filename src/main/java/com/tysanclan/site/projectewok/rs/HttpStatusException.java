package com.tysanclan.site.projectewok.rs;

public class HttpStatusException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final int code;

	public HttpStatusException(int code, String message) {
		super(message);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
