/**
 * 
 */
package org.jivesoftware.smack.tcp.client_60;

import java.io.FileWriter;
import java.io.IOException;
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
public class getClient extends abstractClientWrapper implements Runnable {
	protected String time[] = new String[count];
	
	public void run() {
		final ExecutorService service = Executors.newFixedThreadPool(count);
		double mean = 13.27;
		PoissonDistribution p = new PoissonDistribution(mean);
		long wait = p.sample();
		int i = 0;
		while (i < count) {
			final Future<String> task;
			try {
				task = service.submit(new clientsThread("get:"));
				String call_return = task.get();
				time[i] = call_return.substring(0, call_return.indexOf("\n"));
				String location = call_return.substring(call_return.indexOf(":")+1);
				if(location != "null")
					empty_slots_list.remove(location);
				PoissonDistribution wait_distribution = new PoissonDistribution(mean_wait_time);
				long departure = wait_distribution.sample();
				filled_slots_map.put((System.currentTimeMillis()/1000.0)+departure, location);
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
			wait = p.sample();
		}
		try {
			FileWriter service_time = new FileWriter("src/main/java/org/jivesoftware/smack/tcp/client_60/Results/get_simulation_service_time_60_run1.csv");
			FileWriter result = new FileWriter("src/main/java/org/jivesoftware/smack/tcp/client_60/Results/get_simulation_result_60_run1.csv");
			writeTocsvFile(service_time, result,mean,count,time);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		service.shutdown();
	}
}
