package org.jivesoftware.smack.tcp.client;

import java.io.IOException;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

/**
 * @author paridhika
 *
 */
public class XMPPOpenfireConnection {

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws XMPPException
	 * @throws IOException
	 * @throws SmackException
	 */
	public static void main(String[] args) throws SmackException, IOException, XMPPException, InterruptedException {
		double mean = 1;
		int count = 2;
		while (mean < 2) {
			PoissonDistribution p = new PoissonDistribution(mean);
			long wait = p.sample();
			int i = 1;
			while (i < count) {
				System.out.println("Looping again");
				Thread t = new Thread(new clientsThread(i));
				t.start();
				i++;
				Thread.sleep(wait);
				wait = p.sample();
			}	
			mean *= 2;
		}
	}

	public static class clientsThread implements Runnable {
		private final ConnectionListener connectionListener = new AbstractConnectionListener();

		public clientsThread(int i) throws SmackException, IOException, XMPPException, InterruptedException {
			this.i = i;
		}

		int i;
		public void run() {
			XMPPTCPConnectionConfiguration config = null;
			try {
				config = XMPPTCPConnectionConfiguration.builder()
						.setXmppDomain("paridhika-satellite-c55-c:9090")
						.setHost("paridhika-satellite-c55-c")
						.setPort(5222)
						.setSecurityMode(SecurityMode.disabled)
						.setDebuggerEnabled(true).build();
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
				conn2.login("test", "test");
			} catch (XMPPException | SmackException | IOException | InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			conn2.addConnectionListener(connectionListener);
			PacketCollector collector = conn2.createPacketCollector(new MessageTypeFilter(Message.Type.chat));
			EntityJid jid = null;
			try {
				jid = (EntityJid) JidCreate.from("client" + i + "@paridhika-satellite-c55-c");
			} catch (XmppStringprepException e1) {
				e1.printStackTrace();
			}
			Message msg = new Message(jid, Message.Type.chat);	
			msg.setBody("put");
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
	//		System.out.println("Received Stanza " + rcv.getBody());
			conn2.disconnect();
		}
	}
}
