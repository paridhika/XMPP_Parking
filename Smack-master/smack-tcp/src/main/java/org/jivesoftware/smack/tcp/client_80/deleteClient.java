/**
 * 
 */
package org.jivesoftware.smack.tcp.client_80;

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
public class deleteClient extends abstractClientWrapper implements Runnable {
	protected String time[] = new String[2*count];
	
	public void run() {
		double mean_wait = 0;
		int i = 0;		
		final ExecutorService service = Executors.newFixedThreadPool(2*count);
		while (i < 2*count) {
			final Future<String> task;	
			while(filled_slots_map.isEmpty()){
				//wait
			//	System.out.println("Empty");
				if(i >= (2*count)-50)
					break;
			}
			if(i >= (2*count)-50)
				break;
			Double start_time = filled_slots_map.firstKey();
			while(start_time > (System.currentTimeMillis()/1000)){
				//wait
			//	System.out.println("time");
			}
			double wait = (System.currentTimeMillis() - start_time)/1000.0;
			mean_wait += wait;
			String location = filled_slots_map.remove(start_time);
			
			empty_slots_list.add(location);
			try {
				task = service.submit(new clientsThread("delete:" + location));
				String call_return = task.get();
				time[i] = call_return.substring(0, call_return.indexOf("\n"));
				i++;
			} catch (SmackException | IOException | XMPPException | InterruptedException | ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		try {
			FileWriter service_time = new FileWriter("src/main/java/org/jivesoftware/smack/tcp/client_80/Results/delete_simulation_service_time_80_run1.csv");
			FileWriter result = new FileWriter("src/main/java/org/jivesoftware/smack/tcp/client_80/Results/delete_simulation_result_80_run1.csv");
			writeTocsvFile(service_time, result,mean_wait/(2*count)-50,2*count - 50,time);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		service.shutdown();
	}
}