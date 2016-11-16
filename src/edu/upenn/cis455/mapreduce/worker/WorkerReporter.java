package edu.upenn.cis455.mapreduce.worker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class WorkerReporter extends Thread{
	int port;
	String master;
	boolean keepReporting = true;
	
	public WorkerReporter(String master, int port){
		this.master = master;
		this.port = port;
	}
	
	@Override
	public void run(){
		while(keepReporting) {
			String url = "http://"+ master + "/workerstatus";
			String query = "port=" + port +"&status=" + WorkerStatus.getStatus() +"&job=" + WorkerStatus.getJob() + "&keysread=" + WorkerStatus.getKeysRead() +"&keyswritten=" + WorkerStatus.getKeysWritten() + "&results=" + WorkerStatus.getResult().toString();
			try {
				Thread.sleep(10000);
				
				URL http_url = new URL(url + "?" + query);
				
				System.out.println(http_url);
				
				HttpURLConnection urlConnection = (HttpURLConnection)http_url.openConnection();
//				urlConnection.connect();
				urlConnection.setRequestMethod("GET");
				urlConnection.getResponseCode();
				urlConnection.getResponseMessage();
		
			} catch (InterruptedException | IOException e) {
				//e.printStackTrace();
				System.out.println("Worker Port: " + port + " Reporting status failed");
				continue;
			}
		}
	}
	
	public String resultsSerialization(List<String> results){
		StringBuilder sb = new StringBuilder();
		for(String str : results) {
			if(sb.length() != 0) sb.append(",");
			str = str.replaceAll(":", "-");
			sb.append(str);
		}
		return sb.toString();
	}
	
	public void shutdown(){
		keepReporting = false;
	}
}