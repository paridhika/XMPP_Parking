/**
 * 
 */
package org.jivesoftware.smack.tcp.client_60;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author paridhika
 * 
 */
public abstract class abstractClientWrapper {
	protected static SortedMap<Double, String> filled_slots_map = Collections.synchronizedSortedMap(new TreeMap<Double, String>());
	protected static List<String> empty_slots_list = Collections.synchronizedList(new ArrayList<String>());
	private static int SIZE = 100;
	protected static int count = 1000;
	protected double mean_wait_time = 0.3;
	
	public static void initializeEmptySet(){
		for(int i = 0; i<SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				empty_slots_list.add(i+","+j);
			}
		}
	}
	public static void printEmptySet(){
		for(int i = 0; i< empty_slots_list.size();i++)
			System.out.println(empty_slots_list.get(i));
	}
	protected void writeTocsvFile(FileWriter service_time, FileWriter result, double mean, int count, String []time
	) throws IOException{
		
		//Write the CSV file header
		double avg_service_time = 0;
		service_time.append("Mean");
		service_time.append(",");
		service_time.append("Count");
		service_time.append(",");
		service_time.append("Service Time");
		for(int i = 0; i < count; i++){
			service_time.append("\n");
			service_time.append(String.valueOf(mean));
			service_time.append(",");
			service_time.append(String.valueOf(i));
			service_time.append(",");
			service_time.append(time[i]);
			avg_service_time += Double.parseDouble(time[i]);
		}
		service_time.flush();
		service_time.close();
		result.append("Arrival Rate");
		result.append(",");
		result.append("Service Time");
		result.append(",");
		result.append("Throughput");
		result.append("\n");
		result.append(String.valueOf(mean));
		result.append(",");
		result.append(String.valueOf(avg_service_time/count));
		result.append(",");
		result.append(String.valueOf(count*3600/avg_service_time));
		result.flush();
		result.close();
	}

}
