package org.jivesoftware.smack.tcp.client;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.math3.distribution.PoissonDistribution;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
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
		abstractClientWrapper.initializeEmptySet();
		Thread putClient = new Thread(new putClient());
		putClient.start();
		putClient.join();
//		Thread getClient = new Thread(new getClient());
//		getClient.start();
//		getClient.join();
		Thread deleteClient = new Thread(new deleteClient());
		deleteClient.start();
		deleteClient.join();
	}

}
