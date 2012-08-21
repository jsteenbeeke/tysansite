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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.injection.web.InjectorHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tysanclan.site.projectewok.util.StringUtil;
import com.tysanclan.site.projectewok.util.scheduler.SingleExecutionTask;

/**
 * @author Jeroen Steenbeeke
 */
public class TangoImportTask extends SingleExecutionTask {
	private static final Logger logger = LoggerFactory
	        .getLogger(TangoImportTask.class);

	private List<String> data;

	/**
     * 
     */
	public TangoImportTask(InputStream stream) {
		super("Tango Import task", "Imports");
		data = new LinkedList<String>();

		try {
			BufferedReader br = new BufferedReader(
			        new InputStreamReader(stream));
			String next = br.readLine();

			while (next != null) {
				data.add(next);
				next = br.readLine();
			}

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.util.scheduler.TysanTask#run()
	 */
	@Override
	public void run() {
		RecordHandlerLocator handlerLocator = RecordHandlerLocator
		        .getInstance();

		TangoImporterCallback callback = new TangoImporterCallback();
		for (String line : data) {
			String[] parts = line.split("\t\t");
			RecordHandler handler = handlerLocator
			        .getHandler(parts[0]);
			InjectorHolder.getInjector().inject(handler);
			if (handler.handle(parts, callback)) {
				if (parts.length > 1) {
					logger.info(StringUtil.combineStrings(
					        "Record ", parts[1],
					        " succesfully imported!"));
				}
			} else {
				if (parts.length > 1) {
					logger.warn(StringUtil.combineStrings(
					        "Record ", parts[1],
					        " could not be imported!"));
				}

			}
			handler.cleanup();
		}
		callback = null;
	}
}
