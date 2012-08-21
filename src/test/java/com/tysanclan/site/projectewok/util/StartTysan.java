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
package com.tysanclan.site.projectewok.util;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.security.SslSocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class StartTysan {
	/**
	 * Construct.
	 */
	StartTysan() {
		super();
	}

	/**
	 * Main function, starts the jetty server.
	 * 
	 * @param args Program arguments
	 */
	public static void main(String[] args) {
		Server server = new Server();
		SslSocketConnector connector = new SslSocketConnector();
		connector.setPort(8443);
		connector.setPassword("jetty6");
		connector.setKeystore("target/jetty-ssl.keystore");
		connector.setKeyPassword("jetty6");
		server.setConnectors(new Connector[] { connector });

		WebAppContext web = new WebAppContext();
		web.setContextPath("/ProjectEwok");
		web.setWar("src/main/webapp");
		server.addHandler(web);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
	}
}
