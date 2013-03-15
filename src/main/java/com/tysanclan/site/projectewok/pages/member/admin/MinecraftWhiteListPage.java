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
package com.tysanclan.site.projectewok.pages.member.admin;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.GameAccount;
import com.tysanclan.site.projectewok.entities.GameAccount.AccountType;
import com.tysanclan.site.projectewok.entities.UserGameRealm;
import com.tysanclan.site.projectewok.entities.dao.GameDAO;
import com.tysanclan.site.projectewok.util.MemberUtil;

public class MinecraftWhiteListPage extends WebPage {
	private static final long serialVersionUID = 1L;
	@SpringBean
	private GameDAO gameDAO;

	public MinecraftWhiteListPage() {
		List<Game> games = gameDAO.findAll();
		List<String> whitelist = new LinkedList<String>();

		StringBuilder whiteListBuilder = new StringBuilder();

		for (Game g : games) {
			for (UserGameRealm ugr : g.getPlayers()) {
				for (GameAccount acc : ugr.getAccounts()) {
					if (acc.getType() == AccountType.MINECRAFT
							&& MemberUtil.isMember(ugr.getUser())) {
						whitelist.add(acc.getName());
					}
				}
			}
		}

		Collections.sort(whitelist, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
		});

		for (String account : whitelist) {
			whiteListBuilder.append(account);
			whiteListBuilder.append(System.getProperty("line.separator"));
		}

		IResource wl = new ByteArrayResource("text/plain", whiteListBuilder
				.toString().getBytes());
		IRequestHandler requestHandler = new ResourceRequestHandler(wl, null);
		requestHandler.respond(getRequestCycle());

	}
}
