package at.theengine.android.snakeberry.net;

import org.json.JSONObject;

import android.os.AsyncTask;
import at.theengine.android.snakeberry.Listeners.OnNowPlayingRecieveListener;
import at.theengine.android.snakeberry.Listeners.OnVolumeRecieveListener;
import at.theengine.android.snakeberry.Listeners.OnVolumeSetListener;
import at.theengine.android.snakeberry.dataobjects.NowPlaying;

public class MediaSystemService {

	public void setVolume(String hostIp, int volume, OnVolumeSetListener listener){
		final OnVolumeSetListener l = listener;
		final String ip = hostIp;
		
		AsyncTask<Integer, Integer, Exception> volumeSetter = new AsyncTask<Integer, Integer, Exception>(){

			@Override
			protected void onPostExecute(Exception result) {
				if(result == null){
					l.onVolumeSet();
				} else {
					l.onError(result);
				}
			}
			
			@Override
			protected Exception doInBackground(Integer... params) {
				try {
					String rawResponse = Utils.getStringResponseFromGetRequest(
						"http://" + ip + ":" + Utils.SNAKEBERRY_PORT + "/setvolume/" + 
						String.valueOf(params[0]));
					Utils.handleServiceResponse(rawResponse);
				} catch(Exception ex){
					return ex;
				}
				
				return null;
			}
		};
		
		volumeSetter.execute(volume);
	}
	
	public void getVolume(String hostIp, OnVolumeRecieveListener listener){
		final OnVolumeRecieveListener l = listener;
		final String ip = hostIp;
		
		AsyncTask<Object, Integer, Exception> volumeGetter = new AsyncTask<Object, Integer, Exception>(){

			private int mVolume;
			
			@Override
			protected void onPostExecute(Exception result) {
				if(result == null){
					l.onVolumeRecieved(mVolume);
				} else {
					l.onError(result);
				}
			}
			
			@Override
			protected Exception doInBackground(Object... params) {
				try {
					String rawResponse = Utils.getStringResponseFromGetRequest(
						"http://" + ip + ":" + Utils.SNAKEBERRY_PORT + "/getvolume");
					mVolume =  Utils.handleServiceResponse(rawResponse).getInt("ResponseData");
				} catch(Exception ex){
					return ex;
				}
				
				return null;
			}
		};
		
		volumeGetter.execute(new Object[0]);
	}
	
	public void getNowPlaying(String hostIp, OnNowPlayingRecieveListener listener){
		final OnNowPlayingRecieveListener l = listener;
		final String ip = hostIp;
		
		AsyncTask<Object, Integer, Exception> nowPlayingGetter = new AsyncTask<Object, Integer, Exception>(){

			private NowPlaying mNowPlaying;
			
			@Override
			protected void onPostExecute(Exception result) {
				if(result == null){
					l.onNowPlayingRecieved(mNowPlaying);
				} else {
					l.onError(result);
				}
			}
			
			@Override
			protected Exception doInBackground(Object... params) {
				try {
					String rawResponse = Utils.getStringResponseFromGetRequest(
						"http://" + ip + ":" + Utils.SNAKEBERRY_PORT + "/radio/nowplaying");
					JSONObject jsonNowPlaying =  (JSONObject) Utils.handleServiceResponse(rawResponse).get("ResponseData");
					if(jsonNowPlaying == null){
						mNowPlaying = new NowPlaying(
								"", 
								"", 
								"---", 
								"---");
					} else {
						mNowPlaying = new NowPlaying(
								jsonNowPlaying.getString("MediaURI"), 
								jsonNowPlaying.getString("PID"), 
								jsonNowPlaying.getString("MediaType"), 
								jsonNowPlaying.getString("Description"));
					}
				} catch(Exception ex){
					return ex;
				}
				
				return null;
			}
		};
		
		nowPlayingGetter.execute(new Object[0]);
	}
}
