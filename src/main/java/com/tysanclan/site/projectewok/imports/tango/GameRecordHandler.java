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

import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.entities.Game;

/**
 * @author Jeroen Steenbeeke
 */
public class GameRecordHandler implements RecordHandler {
	@SpringBean
	private GameService gameService;

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#cleanup()
	 */
	@Override
	public void cleanup() {
		gameService = null;

	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#getRecordDescriptor()
	 */
	@Override
	public String getRecordDescriptor() {
		return "G";
	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#handle(java.lang.String[],
	 *      com.tysanclan.site.projectewok.imports.tango.TangoImporterCallback)
	 */
	@Override
	public boolean handle(String[] data,
	        TangoImporterCallback callback) {
		long key = Long.parseLong(data[1]);
		String name = data[2], binaryData = data[3];

		byte[] imageData = new byte[binaryData.length() / 2];

		for (int i = 0; i < binaryData.length(); i = i + 2) {
			imageData[i / 2] = (byte) Short.parseShort(
			        binaryData.toUpperCase().substring(i,
			                i + 2), 16);

		}

		Game game = gameService.createGame(name, imageData);
		if (game != null) {
			callback.registerImportedObject(key, game);
			return true;
		}

		return false;
	}
}
