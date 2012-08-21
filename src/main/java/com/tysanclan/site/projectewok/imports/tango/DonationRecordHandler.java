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
package com.tysanclan.site.projectewok.imports.tango;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.FinanceService;
import com.tysanclan.site.projectewok.entities.Donation;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class DonationRecordHandler implements RecordHandler {
	@SpringBean
	private FinanceService donationService;

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#cleanup()
	 */
	@Override
	public void cleanup() {
		donationService = null;

	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#getRecordDescriptor()
	 */
	@Override
	public String getRecordDescriptor() {
		return "D";
	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#handle(java.lang.String[],
	 *      com.tysanclan.site.projectewok.imports.tango.TangoImporterCallback)
	 */
	@Override
	public boolean handle(String[] data, TangoImporterCallback callback) {
		// D key userkey amount time
		Long key = Long.parseLong(data[1]), userkey = data[2].isEmpty() ? 0L
				: Long.parseLong(data[2]), donationTimeStamp = Long
				.parseLong(data[4]);
		BigDecimal amount = new BigDecimal(data[3]);

		if (callback.hasImportedObject(userkey, User.class) || userkey == 0L) {
			User user = userkey != 0L ? callback.getImportedObject(userkey,
					User.class) : null;
			Donation donation = donationService.createDonation(user, amount,
					new Date(donationTimeStamp));
			if (donation != null) {
				callback.registerImportedObject(key, donation);
				return true;
			}
		}

		return false;
	}

}
