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
package com.tysanclan.site.projectewok.ws.mumble;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.xml.XMLSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeroen Steenbeeke
 */
public class MMOMumbleServerStatus {
	private static final long FIVE_MINUTES = (60 * 5 * 1000);
	private static Map<Long, Long> lastUpdate = new HashMap<Long, Long>();
	private static Map<Long, ServerStatus> lastData = new HashMap<Long, ServerStatus>();

	private static final Logger log = LoggerFactory
			.getLogger(MMOMumbleServerStatus.class);

	public static List<Server> getServers(String token, String secret) {
		String url = get("servers.xml", token, secret);

		WebClient client = WebClient.create(url);

		XMLSource source = client.get(XMLSource.class);
		source.setBuffering(true);

		Response r = client.getResponse();

		if (r.getStatus() == 200) {
			return Arrays.asList(source.getNodes(
					"/request/response/servers/server", Server.class));
		}
		return new LinkedList<Server>();
	}

	public static Server getServer(long serverId, String token, String secret) {
		WebClient client = WebClient.create(get("servers/" + serverId + ".xml",
				token, secret));

		XMLSource source = client.get(XMLSource.class);

		return source.getNode("//server/server", Server.class);
	}

	public static synchronized ServerStatus getServerStatus(long serverId,
			String token, String secret) {
		if (isBuffered(serverId)) {
			return lastData.get(serverId);
		}

		return new ServerStatus();

	}

	/**
	 * @param serverId
	 * @param token
	 * @param secret
	 * @return
	 */
	public static ServerStatus fetchStatus(long serverId, String token,
			String secret) {
		lastData.remove(serverId);
		lastUpdate.remove(serverId);

		ServerStatus status = new ServerStatus();
		try {
			WebClient client = WebClient.create(get("servers/" + serverId
					+ "/status.xml", token, secret));

			XMLSource source = client.get(XMLSource.class);

			source.setBuffering(true);

			List<Channel> channels = new LinkedList<Channel>();
			List<MumbleUser> users = new LinkedList<MumbleUser>();

			Channel[] chans = source.getNodes("//channels/channel",
					Channel.class);

			if (chans != null) {
				channels.addAll(Arrays.asList(chans));
			}

			source = client.get(XMLSource.class);

			MumbleUser[] ua = source.getNodes("//users/user", MumbleUser.class);

			if (ua != null) {
				users.addAll(Arrays.asList(ua));
			}

			status.setChannels(channels);
			status.setUsers(users);
			buffer(serverId, status);
		} catch (Exception e) {
			log.error("Communication error with Mumble server status: "
					+ e.getMessage());
		}

		return status;
	}

	private static void buffer(long serverId, ServerStatus status) {
		lastUpdate.put(serverId, System.currentTimeMillis());
		lastData.put(serverId, status);
	}

	private static boolean isBuffered(long serverId) {
		if (!lastUpdate.containsKey(serverId)
				|| !lastData.containsKey(serverId)) {
			return false;
		}

		long timeSince = System.currentTimeMillis() - lastUpdate.get(serverId);

		return timeSince < FIVE_MINUTES;
	}

	private static String get(String basePath, String token, String secret) {
		StringBuilder builder = new StringBuilder();

		builder.append("https://mmo-mumble.com/account/");
		builder.append(basePath);
		builder.append("?token=");
		builder.append(token);
		builder.append("&secret=");
		builder.append(secret);

		return builder.toString();
	}
}