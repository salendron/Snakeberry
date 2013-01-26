package at.theengine.android.snakeberry.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.RejectedExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import at.theengine.android.snakeberry.Listeners.OnServiceHostFinderFoundHostListener;
import at.theengine.android.snakeberry.dataobjects.Service;
import at.theengine.android.snakeberry.dataobjects.ServiceHost;
import at.theengine.android.snakeberry.dataobjects.SnakeberryException;
import at.theengine.android.snakeberryremote.RemoteStart;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ServiceHostFinder {
	
	private OnServiceHostFinderFoundHostListener mListener;
	private int mSearchCount;
	
	public void setOnServiceHostFinderFoundHostListener(OnServiceHostFinderFoundHostListener listener){
		this.mListener = listener;
	}
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void findserviceHosts(){
		mSearchCount = 0;
		
		//get the first segments of local IP
		String baseIp = Utils.getLocalIp();
		baseIp = baseIp.substring(0, baseIp.lastIndexOf('.') + 1);
		
		Log.d(RemoteStart.TAG, "Searching for Snakeberry Services in Network: " + baseIp + "0");
		
		String ip;
		
		for(int i = 1; i < 255; i++){
			AsyncTask<String, Integer, ServiceHost> hostFinder = new AsyncTask<String, Integer, ServiceHost>(){
	
				@Override
				protected void onPostExecute(ServiceHost host) {
					if(host != null){
						if(mListener != null){
							mListener.onHostFound(host);
						}
					} 
					
					//notify when done
					mSearchCount++;
					if(mSearchCount == 254){
						if(mListener != null){
							mListener.onSearchComplete();
						}
					}
				}
				
				@Override
				protected ServiceHost doInBackground(String... params) {
					InetAddress addr = null;
					try {
						addr = InetAddress.getByName(params[0]);
					} catch (UnknownHostException e) {
						Log.d(RemoteStart.TAG, "No host at " + params[0]);
						return null;
					}
					
					try {
						if(addr.isReachable(5000)) {
							ServiceHost host = tryGetSnakeberryHost(params[0], addr.getHostName());
							if(host != null){
								Log.i(RemoteStart.TAG, "Found host at " + params[0]);
								return host;
							} else {
								Log.d(RemoteStart.TAG, "There is something running on " + params[0] + 
										", but it is no Snakeberry.");
								return null;
							}
						} else {
							Log.d(RemoteStart.TAG, "No host at " + params[0]);
							return null;
						}
					} catch (IOException e) {
						Log.d(RemoteStart.TAG, "No host at " + params[0]);
						return null;
						
					}
				}
				
			};
			
			ip = baseIp + String.valueOf(i);
			
			try{
				if(android.os.Build.VERSION.SDK.equals("10")){
					hostFinder.execute(ip);
				} else {
					hostFinder.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,ip);
				}
			} catch(RejectedExecutionException ex) {
				i--; //retry host
			}
		}
	}
	
	public ServiceHost tryGetSnakeberryHost(String ip, String hostname){
		try {
			//call host to list available services
			String rawResponse = Utils.getStringResponseFromGetRequest("http://" + ip + ":" + Utils.SNAKEBERRY_PORT);
			JSONObject response = Utils.handleServiceResponse(rawResponse);
			
			//create new host
			ServiceHost host = new ServiceHost(ip, hostname, hostname, ""); //to try to override host name from DB or so...
			
			//build service list
			JSONArray services = (JSONArray) ((JSONObject) response.get("ResponseData")).get("Services");
			JSONObject service;
			for(int i = 0; i < services.length(); i++){
				service = (JSONObject) services.get(i);
				host.addService(new Service(service.getString("DisplayName"), service.getString("BaseUrl")));
			}
			
			//getMac
			rawResponse = Utils.getStringResponseFromGetRequest("http://" + ip + ":" + Utils.SNAKEBERRY_PORT + "/getmac");
			response = Utils.handleServiceResponse(rawResponse);
			host.setMac(response.getString("ResponseData"));
			
			return host;
		} catch (IOException e) {
			Log.d(RemoteStart.TAG, "Could not connect to Snakeberry Host on: " + ip + " - IOException: " + e.getMessage());
			return null;
		} catch (JSONException e) {
			Log.d(RemoteStart.TAG, "Could not connect to Snakeberry Host on: " + ip + " - JSONException: " + e.getMessage());
		} catch (SnakeberryException e) {
			Log.d(RemoteStart.TAG, "Could not connect to Snakeberry Host on: " + ip + " - SnakeberryException: " + e.getMessage());
		}
		
		return null;
	}
	 
}
