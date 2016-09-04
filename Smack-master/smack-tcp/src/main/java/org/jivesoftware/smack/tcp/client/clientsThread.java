/**
 * 
 */
package org.jivesoftware.smack.tcp.client;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

/**
 * @author paridhika
 *
 */
public class clientsThread implements Callable<String> {
	private final ConnectionListener connectionListener = new AbstractConnectionListener();

	public clientsThread(String client) throws SmackException, IOException, XMPPException, InterruptedException {
		this.client = client;
	}

	private String client;

	public String call() {
		long startTime = System.currentTimeMillis();
		XMPPTCPConnectionConfiguration config = null;
		try {
			config = XMPPTCPConnectionConfiguration.builder().setXmppDomain("paridhika-satellite-c55-c:9090")
					.setHost("paridhika-satellite-c55-c").setPort(5222).setSecurityMode(SecurityMode.disabled)
					.setDebuggerEnabled(false).build();
		} catch (XmppStringprepException e2) {
			e2.printStackTrace();
		}
		XMPPTCPConnection conn2 = new XMPPTCPConnection(config);
		try {
			conn2.connect();
		} catch (SmackException | IOException | XMPPException | InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			conn2.login("admin", "admin");
		} catch (XMPPException | SmackException | IOException | InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		conn2.addConnectionListener(connectionListener);

		PacketCollector collector = conn2.createPacketCollector(new MessageTypeFilter(Message.Type.chat));
		EntityJid jid = null;
		try {
			jid = (EntityJid) JidCreate.from("client@paridhika-satellite-c55-c");
		} catch (XmppStringprepException e1) {
			e1.printStackTrace();
		}
		Presence presence = new Presence(Presence.Type.available);
		presence.setTo(jid);
		try {
			conn2.sendStanza(presence);
		} catch (NotConnectedException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Message msg = new Message(jid, Message.Type.chat);

		msg.setBody(this.client);

		try {
			conn2.sendStanza(msg);
		} catch (NotConnectedException | InterruptedException e) {
			e.printStackTrace();
		}
		Message rcv = null;
		try {
			rcv = (Message) collector.nextResult();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(rcv.getBody());
		conn2.disconnect();
		long endTime = System.currentTimeMillis();
		return String.valueOf((endTime - startTime)/1000.0) + "\n" + rcv.getBody();
	}
}
