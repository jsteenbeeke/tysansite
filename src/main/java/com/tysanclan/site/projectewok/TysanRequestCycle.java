/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
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
package com.tysanclan.site.projectewok;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;

import com.tysanclan.site.projectewok.pages.SessionTimeoutPage;
import com.tysanclan.site.projectewok.pages.TysanErrorPage;

/**
 * @author Jeroen Steenbeeke
 */
public class TysanRequestCycle extends WebRequestCycle {

	public TysanRequestCycle(WebApplication application, WebRequest request,
			Response response) {
		super(application, request, response);
	}

	/**
	 * @see org.apache.wicket.RequestCycle#onRuntimeException(org.apache.wicket.Page,
	 *      java.lang.RuntimeException)
	 */
	@Override
	public Page onRuntimeException(Page page, RuntimeException e) {
		if (getApplication().getConfigurationType().equals(
				Application.DEVELOPMENT)) {
			return super.onRuntimeException(page, e);
		}

		if (e instanceof PageExpiredException) {
			return new SessionTimeoutPage();
		}

		return new TysanErrorPage(page, e);
	}

}
