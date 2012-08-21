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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jeroen Steenbeeke
 */
public class RecordHandlerLocator {
	private Map<String, RecordHandler> handlers;

	private static RecordHandlerLocator instance;

	/**
     * 
     */
	public RecordHandlerLocator() {
		handlers = new HashMap<String, RecordHandler>();

		init();
	}

	protected void init() {
		addHandler(new UserRecordHandler());
		addHandler(new RoleRecordHandler());
		addHandler(new ForumCategoryRecordHandler());
		addHandler(new ForumRecordHandler());
		addHandler(new ForumThreadRecordHandler());
		addHandler(new ForumPostRecordHandler());
		addHandler(new MessageRecordHandler());
		addHandler(new RegulationRecordHandler());
		addHandler(new GameRecordHandler());
		addHandler(new GamingGroupRecordHandler());
		addHandler(new ExpenseRecordHandler());
		addHandler(new GamingGroupMemberRecordHandler());
		addHandler(new DonationRecordHandler());
	}

	
	private void addHandler(RecordHandler recordHandler) {
		if (!handlers.containsKey(recordHandler
		        .getRecordDescriptor())) {
			handlers.put(recordHandler
			        .getRecordDescriptor(), recordHandler);
		}
	}

	public static RecordHandlerLocator getInstance() {
		if (instance == null) {
			instance = new RecordHandlerLocator();
		}

		return instance;
	}

	public RecordHandler getHandler(String recordType) {
		if (handlers.containsKey(recordType)) {
			return handlers.get(recordType);
		}

		return new NoActionRecordHandler();
	}
}
