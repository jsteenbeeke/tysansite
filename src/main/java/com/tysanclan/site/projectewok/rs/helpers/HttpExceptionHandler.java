package com.tysanclan.site.projectewok.rs.helpers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.tysanclan.site.projectewok.rs.HttpStatusException;

@Component
@Provider
public class HttpExceptionHandler implements
		ExceptionMapper<HttpStatusException> {

	@Override
	public Response toResponse(HttpStatusException exception) {
		return Response.status(exception.getCode())
				.entity(exception.getMessage()).build();
	}
}
