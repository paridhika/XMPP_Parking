package org.jivesoftware.smack.tcp.client;

import java.io.IOException;
import java.util.List;

import javax.security.sasl.Sasl;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.test.util.WaitForPacketListener;
import org.jivesoftware.smack.util.DNSUtil;
import org.jivesoftware.smack.util.dns.HostAddress;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.Jid;
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
		int count = 50;
		while (mean < 1000) {
			PoissonDistribution p = new PoissonDistribution(mean);
			long wait = p.sample();
			int i = 1;
			while (i < count) {
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
		private ChatManager chatmanager;
		private Chat newChat;

		public clientsThread(int i) throws SmackException, IOException, XMPPException, InterruptedException {
			this.i = i;
		}

		int i;

		public void run() {
			XMPPTCPConnectionConfiguration config = null;

			try {
				config = XMPPTCPConnectionConfiguration.builder().setXmppDomain("paridhika-satellite-c55-c:9090")
						.setHost("paridhika-satellite-c55-c").setPort(5222).setSecurityMode(SecurityMode.disabled)
						.setDebuggerEnabled(true).build();
			} catch (XmppStringprepException e2) {
				e2.printStackTrace();
			}
			AbstractXMPPConnection conn2 = new XMPPTCPConnection(config);
			try {
				conn2.connect();
			} catch (SmackException | IOException | XMPPException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			conn2.addConnectionListener(connectionListener);
			// System.out.println(conn2.isAuthenticated());
			// System.out.println(SASLAuthentication.getRegisterdSASLMechanisms().keySet());
			// System.out.println(SASLAuthentication.getRegisterdSASLMechanisms().values());
			// SASLAuthentication.registerSASLMechanism(Sasl.SERVER_AUTH,"TRUE");
			// System.out.println(SASLAuthentication.isSaslMechanismRegistered("PLAIN"));
			/*
			 * try { conn2.login("test", "test"); } catch (XMPPException |
			 * SmackException | IOException | InterruptedException e1) { // TODO
			 * Auto-generated catch block e1.printStackTrace(); }
			 */
			chatmanager = ChatManager.getInstanceFor(conn2);
			EntityJid jid = null;
			try {
				jid = (EntityJid) JidCreate.from("client" + i + "@paridhika-satellite-c55-c");
			} catch (XmppStringprepException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			newChat = chatmanager.createChat(jid);
			try {
				try {
					newChat.sendMessage("Paridhika Kayal! from client put" + i);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SmackException.NotConnectedException e) {
				e.printStackTrace();
			}
		}
	}
}
