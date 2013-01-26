package at.theengine.android.snakeberry.net;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import at.theengine.android.snakeberry.Listeners.OnRadioPlayListener;
import at.theengine.android.snakeberry.Listeners.OnRadioStationsRecievedListener;
import at.theengine.android.snakeberry.Listeners.OnRadioStopListener;
import at.theengine.android.snakeberry.dataobjects.Radio;

public class RadioService {

	public void stop(String hostIp, OnRadioStopListener listener){
		final OnRadioStopListener l = listener;
		final String ip = hostIp;
		
		AsyncTask<Object, Integer, Exception> radioStopper = new AsyncTask<Object, Integer, Exception>(){

			@Override
			protected void onPostExecute(Exception result) {
				if(result == null){
					l.onRadioStopped();
				} else {
					l.onError(result);
				}
			}
			
			@Override
			protected Exception doInBackground(Object... params) {
				try {
					String rawResponse = Utils.getStringResponseFromGetRequest(
						"http://" + ip + ":" + Utils.SNAKEBERRY_PORT + "/radio/stop");
					Utils.handleServiceResponse(rawResponse);
				} catch(Exception ex){
					return ex;
				}
				
				return null;
			}
		};
		
		radioStopper.execute(new Object[0]);
	}
	
	public void play(String hostIp, String radioId, OnRadioPlayListener listener){
		final OnRadioPlayListener l = listener;
		final String ip = hostIp;
		
		AsyncTask<String, Integer, Exception> radioPlayer = new AsyncTask<String, Integer, Exception>(){

			@Override
			protected void onPostExecute(Exception result) {
				if(result == null){
					l.onRadioPlays();
				} else {
					l.onError(result);
				}
			}
			
			@Override
			protected Exception doInBackground(String... params) {
				try {
					String rawResponse = Utils.getStringResponseFromGetRequest(
						"http://" + ip + ":" + Utils.SNAKEBERRY_PORT + "/radio/play/" + 
					params[0]);
					Utils.handleServiceResponse(rawResponse);
				} catch(Exception ex){
					return ex;
				}
				
				return null;
			}
		};
		
		radioPlayer.execute(radioId);
	}
	
	public void getRadioStations(String hostIp, OnRadioStationsRecievedListener listener){
		final OnRadioStationsRecievedListener l = listener;
		final String ip = hostIp;
		
		AsyncTask<Object, Integer, Exception> radioStopper = new AsyncTask<Object, Integer, Exception>(){

			private ArrayList<Radio> mRadios;
			
			@Override
			protected void onPostExecute(Exception result) {
				if(result == null){
					l.onRadioStationsRecieved(mRadios);
				} else {
					l.onError(result);
				}
			}
			
			@Override
			protected Exception doInBackground(Object... params) {
				try {
					String rawResponse = Utils.getStringResponseFromGetRequest(
						"http://" + ip + ":" + Utils.SNAKEBERRY_PORT + "/radios");
					JSONArray jsonRadios = ((JSONObject) Utils.handleServiceResponse(rawResponse)
							.get("ResponseData")).getJSONArray("Radios");
					
					mRadios = new ArrayList<Radio>();
					for(int i = 0; i < jsonRadios.length(); i++){
						JSONObject radio = (JSONObject) jsonRadios.get(i);
						mRadios.add(new Radio(
								radio.getString("StreamUrl"), 
								radio.getString("DisplayName"), 
								radio.getString("RadioId")));
					}
					
				} catch(Exception ex){
					return ex;
				}
				
				return null;
			}
		};
		
		radioStopper.execute(new Object[0]);
	}
	
}
