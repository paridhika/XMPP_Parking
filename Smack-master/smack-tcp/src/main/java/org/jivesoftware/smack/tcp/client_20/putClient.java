/**
 * 
 */
package org.jivesoftware.smack.tcp.client_20;

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
public class putClient extends abstractClientWrapper implements Runnable {
	protected String time[] = new String[count];
	public void run() {
		final ExecutorService service = Executors.newFixedThreadPool(count);
		double mean = 7.05;
		PoissonDistribution arrival_distribution = new PoissonDistribution(mean);
		PoissonDistribution wait_distribution = new PoissonDistribution(mean_wait_time);
		long wait = arrival_distribution.sample();
		int i = 0;
		while (i < count) {
			String location = "0,0";
			if(!abstractClientWrapper.empty_slots_list.isEmpty())
				location = abstractClientWrapper.empty_slots_list.remove(0);
			long departure = wait_distribution.sample();
			filled_slots_map.put((System.currentTimeMillis()/1000.0)+departure, location);
			try {
				Future<String>task = service.submit(new clientsThread("put:" + location));
				String call_return = task.get();
				time[i] = call_return.substring(0, call_return.indexOf("\n"));
			} catch (SmackException | IOException | XMPPException | InterruptedException | ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			i++;
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			wait = arrival_distribution.sample();
		}
		service.shutdown();
		try {
			FileWriter service_time = new FileWriter("src/main/java/org/jivesoftware/smack/tcp/client_20/Results/put_simulation_service_time_20_run1.csv");
			FileWriter result = new FileWriter("src/main/java/org/jivesoftware/smack/tcp/client_20/Results/put_simulation_result_20_run1.csv");
			writeTocsvFile(service_time, result,mean,count,time);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
	}
}