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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tysanclan.site.projectewok.beans.MumbleService;
import com.tysanclan.site.projectewok.entities.MumbleServer;
import com.tysanclan.site.projectewok.entities.dao.MumbleServerDAO;
import com.tysanclan.site.projectewok.ws.mumble.MMOMumbleServerStatus;
import com.tysanclan.site.projectewok.ws.mumble.ServerStatus;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class MumbleServiceImpl implements MumbleService {
	private static final Logger log = LoggerFactory
			.getLogger(MumbleServiceImpl.class);

	@Autowired
	private MumbleServerDAO mumbleServerDAO;

	public void setMumbleServerDAO(MumbleServerDAO mumbleServerDAO) {
		this.mumbleServerDAO = mumbleServerDAO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public MumbleServer createServer(int serverId, String name, String address,
			String password, String apiToken, String apiSecret) {
		MumbleServer server = new MumbleServer();

		server.setApiSecret(apiSecret);
		server.setApiToken(apiToken);
		server.setName(name);
		server.setPassword(password);
		server.setServerID(serverId);
		server.setUrl(address);

		mumbleServerDAO.save(server);

		return server;
	}

	@Override
	public ServerStatus getServerStatus(MumbleServer server) {
		try {
			return MMOMumbleServerStatus.getServerStatus(server.getServerID(),
					server.getApiToken(), server.getApiSecret());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ServerStatus();
		}
	}
}
