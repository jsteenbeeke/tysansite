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
package com.tysanclan.site.projectewok.imports.tango;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.RegulationService;
import com.tysanclan.site.projectewok.entities.Regulation;

/**
 * @author Jeroen Steenbeeke
 */
public class RegulationRecordHandler implements
        RecordHandler {
	@SpringBean
	private RegulationService regulationService;

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#cleanup()
	 */
	@Override
	public void cleanup() {
		regulationService = null;

	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#getRecordDescriptor()
	 */
	@Override
	public String getRecordDescriptor() {
		return "RG";
	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#handle(java.lang.String[],
	 *      com.tysanclan.site.projectewok.imports.tango.TangoImporterCallback)
	 */
	@Override
	public boolean handle(String[] data,
	        TangoImporterCallback callback) {
		// RG key name contents
		long key = Long.parseLong(data[1]);
		String name = data[2], contents = data[3];

		Regulation regulation = regulationService
		        .importRegulation(name, contents);
		if (regulation != null) {
			callback.getImportedObject(key,
			        Regulation.class);
			return true;
		}

		return false;
	}

}
