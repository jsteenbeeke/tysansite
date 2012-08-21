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

import java.util.Arrays;
import java.util.Date;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.MessageService;
import com.tysanclan.site.projectewok.entities.ConversationParticipation;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class MessageRecordHandler implements RecordHandler {
	@SpringBean
	private MessageService messageService;

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#cleanup()
	 */
	@Override
	public void cleanup() {
		messageService = null;
	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#getRecordDescriptor()
	 */
	@Override
	public String getRecordDescriptor() {
		return "M";
	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#handle(java.lang.String[],
	 *      com.tysanclan.site.projectewok.imports.tango.TangoImporterCallback)
	 */
	@Override
	public boolean handle(String[] data,
	        TangoImporterCallback callback) {
		// M [1]key [2]senderKey [3]receiverKey [4]content [5]date
		long key = Long.parseLong(data[1]), senderKey = Long
		        .parseLong(data[2]), receiverKey = Long
		        .parseLong(data[3]), messageTime = Long
		        .parseLong(data[5]);
		String content = data[4];

		User sender = callback.getImportedObject(senderKey,
		        User.class);
		User receiver = callback.getImportedObject(
		        receiverKey, User.class);

		if (sender != null && receiver != null) {
			ConversationParticipation participation = messageService
			        .createConversation(
			                sender,
			                Arrays.asList(new User[] {
			                        receiver, sender }),
			                "Imported message from old site",
			                content);
			if (participation != null) {
				messageService.setImported(participation
				        .getConversation(), new Date(
				        messageTime));
				callback.registerImportedObject(key,
				        participation);
				return true;
			}
		}

		return false;
	}
}
