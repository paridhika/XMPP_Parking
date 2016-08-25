/**
 * $Revision: $
 * $Date: $
 *
 * Copyright (C) 2005-2008 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jivesoftware.openfire.net;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.dom4j.Element;
import org.jivesoftware.openfire.Connection;
import org.jivesoftware.openfire.PacketRouter;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.session.ConnectionSettings;
import org.jivesoftware.openfire.session.LocalClientSession;
import org.jivesoftware.util.JiveGlobals;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.Message;
import org.xmpp.packet.Presence;

/**
 * Handler of XML stanzas sent by clients connected directly to the server.
 * Received packet will have their FROM attribute overriden to avoid spoofing.
 * <p>
 *
 * By default the hostname specified in the stream header sent by clients will
 * not be validated. When validated the TO attribute of the stream header has to
 * match the server name or a valid subdomain. If the value of the 'to'
 * attribute is not valid then a host-unknown error will be returned. To enable
 * the validation set the system property <b>xmpp.client.validate.host</b> to
 * true.
 *
 * @author Gaston Dombiak
 */
public class ClientStanzaHandler extends StanzaHandler {

	private static int SIZE = 10;
	private static String[][] my_map = new String[SIZE][SIZE];
	private final Lock lock = new ReentrantLock();

	public ClientStanzaHandler(PacketRouter router, Connection connection) {
		super(router, connection);
		initializeMap();
	}

	@Deprecated
	public ClientStanzaHandler(PacketRouter router, String serverName, Connection connection) {
		super(router, connection);
	}

	private void initializeMap() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				my_map[i][j] = "EMPTY";
			}
		}
	}

	private String findLocation() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (my_map[i][j].equals("EMPTY")) {
					my_map[i][j] = "OCCUPIED";
					return i + " " + j;
				}
			}
		}
		return null;
	}

	private void deleteLocation() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (my_map[i][j].equals("OCCUPIED")) {
					my_map[i][j] = "EMPTY";
					return;
				}
			}
		}
	}

	private void printMap() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++)
				System.out.print(my_map[i][j] + " ");
			System.out.println();
		}
	}

	/**
	 * Only packets of type Message, Presence and IQ can be processed by this
	 * class. Any other type of packet is unknown and thus rejected generating
	 * the connection to be closed.
	 *
	 * @param doc
	 *            the unknown DOM element that was received
	 * @return always false.
	 */
	@Override
	boolean processUnknowPacket(Element doc) {
		return false;
	}

	@Override
	String getNamespace() {
		return "jabber:client";
	}

	@Override
	boolean validateHost() {
		return JiveGlobals.getBooleanProperty("xmpp.client.validate.host", false);
	}

	@Override
	boolean validateJIDs() {
		return true;
	}

	@Override
	boolean createSession(String namespace, String serverName, XmlPullParser xpp, Connection connection)
			throws XmlPullParserException {
		if ("jabber:client".equals(namespace)) {
			// The connected client is a regular client so create a
			// ClientSession
			session = LocalClientSession.createSession(serverName, xpp, connection);
			return true;
		}
		return false;
	}

	@Override
	protected void processIQ(IQ packet) throws UnauthorizedException {
		// Overwrite the FROM attribute to avoid spoofing
		packet.setFrom(session.getAddress());
		super.processIQ(packet);
	}

	@Override
	protected void processPresence(Presence packet) throws UnauthorizedException {
		// Overwrite the FROM attribute to avoid spoofing
		packet.setFrom(session.getAddress());
		super.processPresence(packet);
	}

	@Override
	protected void processMessage(Message packet) throws UnauthorizedException {
		// Overwrite the FROM attribute to avoid spoofing
		System.out.println("Client Stanza Handler for Paridhika");
		packet.setFrom(session.getAddress());
		parkingHandler(packet);
		super.processMessage(packet);
	}

	private void parkingHandler(Message packet) {
		my_map[0][0] = packet.getTo().toString();
		String body = packet.getBody();
		if (body.contains("put")) {
			lock.lock();
			String location = findLocation();
			lock.unlock();
			packet.setBody(location);
		} else if (body.contains("delete")) {
			lock.lock();
			deleteLocation();
			lock.unlock();
		} else if (body.contains("get")) {
			lock.lock();
			findLocation();
			lock.unlock();
		}
		lock.lock();
		printMap();
		lock.unlock();
	}

	@Override
	void startTLS() throws Exception {
		connection.startTLS(false);
	}
}
