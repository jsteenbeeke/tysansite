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
package com.tysanclan.site.projectewok.pages.member;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * @author Jeroen Steenbeeke
 */
public class YouTubeUrlValidator implements
        IValidator<String> {

	private static final long serialVersionUID = 1L;

	public static final Pattern REGEX = Pattern
	        .compile("^http://www.youtube.com/watch\\?v=(([a-zA-Z0-9]|-|_)+)(&.*)?$");

	/**
	 * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	public void validate(IValidatable<String> validatable) {
		String value = validatable.getValue();

		Matcher m = REGEX.matcher(value);

		if (!m.matches()) {
			validatable.error(new ValidationError()
			        .setMessage("Not a valid YouTube URL"));
		}

	}

}
