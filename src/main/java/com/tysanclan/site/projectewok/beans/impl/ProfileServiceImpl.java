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
package com.tysanclan.site.projectewok.beans.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tysanclan.site.projectewok.entities.Profile;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ProfileDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.util.HTMLSanitizer;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ProfileServiceImpl implements
		com.tysanclan.site.projectewok.beans.ProfileService {
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ProfileDAO profileDAO;

	/**
	 * @see com.tysanclan.site.projectewok.beans.ProfileService#createProfile(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Profile createProfile(User user) {
		User _user = userDAO.load(user.getId());

		if (_user.getProfile() == null) {
			Profile profile = new Profile();
			profile.setUser(_user);
			profileDAO.save(profile);

			userDAO.evict(_user);

			return profile;
		}

		return _user.getProfile();
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ProfileService#setBirthDate(com.tysanclan.site.projectewok.entities.Profile,
	 *      java.util.Date)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setBirthDate(Profile profile, Date birthDate) {
		Profile _profile = profileDAO.load(profile.getId());

		_profile.setBirthDate(birthDate);

		profileDAO.update(_profile);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ProfileService#setPhotoURL(com.tysanclan.site.projectewok.entities.Profile,
	 *      java.lang.String, boolean)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setPhotoURL(Profile profile, String url,
			boolean publiclyViewable) {
		Profile _profile = profileDAO.load(profile.getId());

		_profile.setPhotoURL(HTMLSanitizer.filterURL(url));
		_profile.setPhotoPublic(publiclyViewable);

		profileDAO.update(_profile);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ProfileService#setAIMAddress(com.tysanclan.site.projectewok.entities.Profile,
	 *      java.lang.String, java.lang.Boolean)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setAIMAddress(Profile profile, String aimName, Boolean aimPublic) {
		Profile _profile = profileDAO.load(profile.getId());

		_profile.setInstantMessengerAddress(aimName);
		_profile.setInstantMessengerPublic(aimPublic);

		profileDAO.update(_profile);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ProfileService#setPrivateDescription(com.tysanclan.site.projectewok.entities.Profile,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setPrivateDescription(Profile profile, String description) {
		Profile _profile = profileDAO.load(profile.getId());

		_profile.setPrivateDescription(HTMLSanitizer.sanitize(description));

		profileDAO.update(_profile);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ProfileService#setPublicDescription(com.tysanclan.site.projectewok.entities.Profile,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setPublicDescription(Profile profile, String description) {
		Profile _profile = profileDAO.load(profile.getId());

		_profile.setPublicDescription(HTMLSanitizer.sanitize(description));

		profileDAO.update(_profile);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ProfileService#setRealname(com.tysanclan.site.projectewok.entities.Profile,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setRealname(Profile profile, String realname) {
		Profile _profile = profileDAO.load(profile.getId());

		_profile.setRealName(HTMLSanitizer.stripTags(realname));

		profileDAO.update(_profile);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ProfileService#setTwitterUID(com.tysanclan.site.projectewok.entities.Profile,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setTwitterUID(Profile profile, String twitter) {
		Profile _profile = profileDAO.load(profile.getId());

		_profile.setTwitterUID(twitter);

		profileDAO.update(_profile);
	}

	/**
	 * @param profileDAO
	 *            the profileDAO to set
	 */
	public void setProfileDAO(ProfileDAO profileDAO) {
		this.profileDAO = profileDAO;
	}

	/**
	 * @param userDAO
	 *            the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
}
