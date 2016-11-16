package org.jivesoftware.smack.tcp.client_20;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

public class calculateServiceTime {
	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws XMPPException
	 * @throws IOException
	 * @throws SmackException
	 */
	public static void main(String[] args) throws SmackException, IOException, XMPPException, InterruptedException {
		abstractClientWrapper.initializeEmptySet();
		Thread putClient = new Thread(new putClient());
		putClient.start();
		putClient.join();
		Thread getClient = new Thread(new getClient());
		getClient.start();
		getClient.join();
		Thread deleteClient = new Thread(new deleteClient());
		deleteClient.start();
		deleteClient.join();
	}
}
