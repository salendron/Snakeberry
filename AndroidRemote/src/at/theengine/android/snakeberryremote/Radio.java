package at.theengine.android.snakeberryremote;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class Radio extends Activity {
	
	private Context mContext;
	
	private ListView mLstStationList;
	private ImageButton mBtnRadioStop;
	private Button mBtnVolumeUp;
	private Button mBtnVolumeDown;
	
	private ArrayList<RadioStation> mStations;
	private ProgressDialog mPdLoading;
	
	private int mVolumeRange = 4000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radio);
		
		mContext = this;
		
		loadViews();
		loadStations();
	}
	
	private void loadStations(){
		AsyncTask serviceLoader = new AsyncTask(){
			
			private Exception mEx;
			
			@Override
			protected void onPostExecute(Object result) {
				mPdLoading.dismiss();
				if(mEx != null){
					Toast.makeText(mContext, mEx.getMessage(), Toast.LENGTH_LONG).show();
				} else {
					mLstStationList.setAdapter((new StationListAdapter(mContext,R.layout.listitem_service, mStations)));
				}
			}
			
			@Override
			protected Object doInBackground(Object... params) {
				URL url1;
				URLConnection urlConnection;
				DataInputStream inStream = null;
				
				try{
					// Create connection
					url1 = new URL("http://10.0.0.150:8888/radios"); //TODO get Url from other intent
					urlConnection = url1.openConnection();
					((HttpURLConnection)urlConnection).setRequestMethod("GET");
					urlConnection.setDoInput(true);
					urlConnection.setDoOutput(false);
					urlConnection.setUseCaches(false);

					inStream = new DataInputStream(urlConnection.getInputStream());

					JSONObject responseObject = new JSONObject(inStream.readLine());
					
					//TODO check if it is not an error response
					JSONArray jsonServices = (JSONArray) responseObject.get("Radios");
					
					JSONObject service;
					mStations = new ArrayList<RadioStation>();
					for(int i = 0; i < jsonServices.length(); i++){
						service = (JSONObject) jsonServices.get(i);
						mStations.add(new RadioStation(service.getString("RadioId")));
					}
					
				} catch(Exception ex){
					mEx = ex;
				} finally {
					try {
						inStream.close();
					} catch (IOException e) {
						Log.e(Services.TAG, e.getMessage());
					}
				}
				
				return null;
			}
			
		};
		
		mPdLoading.show();
		serviceLoader.execute();
	}
	
	private void loadViews(){
		mLstStationList = (ListView) findViewById(R.id.lstStationList);
		mBtnRadioStop = (ImageButton) findViewById(R.id.btnRadioStop);
		mBtnVolumeUp = (Button) findViewById(R.id.btnVolumeUp);
		mBtnVolumeDown = (Button) findViewById(R.id.btnVolumeDown);
		
		mPdLoading = new ProgressDialog(mContext);
		mPdLoading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mPdLoading.setMessage("Working...");
		mPdLoading.setIndeterminate(true);
		mPdLoading.setCancelable(false);
		
		mBtnRadioStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopStation();
			}
		});
		
		mBtnVolumeUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setVolume(true);
			}
		});

		mBtnVolumeDown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setVolume(false);
			}
		});
		
		mLstStationList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				startStation(mStations.get(arg2).getDisplayName());
			}
		});
	}
	
	private void startStation(String stationId){	
		final String station = stationId;
		AsyncTask stationSetter = new AsyncTask(){
   			
   			private Exception mEx;
   			
   			@Override
   			protected void onPostExecute(Object result) {
   				if(mEx != null){
   					Toast.makeText(mContext, mEx.getMessage(), Toast.LENGTH_LONG).show();
   				} else { }
   			}
   			
   			@Override
   			protected Object doInBackground(Object... params) {
   				URL url1;
   				URLConnection urlConnection;
   				DataInputStream inStream = null;
   				
   				try{
   					// Create connection
   					url1 = new URL("http://10.0.0.150:8888/radio/play/" + station); 
   					urlConnection = url1.openConnection();
   					((HttpURLConnection)urlConnection).setRequestMethod("GET");
   					urlConnection.setDoInput(true);
   					urlConnection.setDoOutput(false);
   					urlConnection.setUseCaches(false);

   					inStream = new DataInputStream(urlConnection.getInputStream());

   					JSONObject responseObject = new JSONObject(inStream.readLine());
   					
   					//TODO check if it is not an error response
   					
   				} catch(Exception ex){
   					mEx = ex;
   				} finally {
   					try {
   						inStream.close();
   					} catch (IOException e) {
   						Log.e(Services.TAG, e.getMessage());
   					}
   				}
   				
   				return null;
   			}
   			
   		};
   		stationSetter.execute();
	}
	
	private void stopStation(){	
		AsyncTask stationStopper = new AsyncTask(){
   			
   			private Exception mEx;
   			
   			@Override
   			protected void onPostExecute(Object result) {
   				if(mEx != null){
   					Toast.makeText(mContext, mEx.getMessage(), Toast.LENGTH_LONG).show();
   				} else { }
   			}
   			
   			@Override
   			protected Object doInBackground(Object... params) {
   				URL url1;
   				URLConnection urlConnection;
   				DataInputStream inStream = null;
   				
   				try{
   					// Create connection
   					url1 = new URL("http://10.0.0.150:8888/radio/stop"); 
   					urlConnection = url1.openConnection();
   					((HttpURLConnection)urlConnection).setRequestMethod("GET");
   					urlConnection.setDoInput(true);
   					urlConnection.setDoOutput(false);
   					urlConnection.setUseCaches(false);

   					inStream = new DataInputStream(urlConnection.getInputStream());

   					JSONObject responseObject = new JSONObject(inStream.readLine());
   					
   					//TODO check if it is not an error response
   					
   				} catch(Exception ex){
   					mEx = ex;
   				} finally {
   					try {
   						inStream.close();
   					} catch (IOException e) {
   						Log.e(Services.TAG, e.getMessage());
   					}
   				}
   				
   				return null;
   			}
   			
   		};
   		stationStopper.execute();
	}
	
	private void setVolume(boolean up){
		final boolean volUp = up;
		
		AsyncTask volumeSetter = new AsyncTask(){
   			
   			private Exception mEx;
   			
   			@Override
   			protected void onPostExecute(Object result) {
   				if(mEx != null){
   					Toast.makeText(mContext, mEx.getMessage(), Toast.LENGTH_LONG).show();
   				} else { }
   			}
   			
   			@Override
   			protected Object doInBackground(Object... params) {
   				URL url1;
   				URLConnection urlConnection;
   				DataInputStream inStream = null;
   				
   				try{
   					// Create connection
   					url1 = new URL("http://10.0.0.150:8888/getvolume"); 
   					urlConnection = url1.openConnection();
   					((HttpURLConnection)urlConnection).setRequestMethod("GET");
   					urlConnection.setDoInput(true);
   					urlConnection.setDoOutput(false);
   					urlConnection.setUseCaches(false);

   					inStream = new DataInputStream(urlConnection.getInputStream());

   					JSONObject responseObject = new JSONObject(inStream.readLine());
   					
   					int volume = responseObject.getInt("Message");
   					
   					if(volUp){
   						volume += 1000;
   					} else {
   						volume -= 1000;
   					}
   					
   					if(volume < -10238){ volume = -10238; }
   					if(volume > 0){ volume = 0; }
   					
   					// Create connection
   					url1 = new URL("http://10.0.0.150:8888/setvolume/" + String.valueOf(volume)); 
   					urlConnection = url1.openConnection();
   					((HttpURLConnection)urlConnection).setRequestMethod("GET");
   					urlConnection.setDoInput(true);
   					urlConnection.setDoOutput(false);
   					urlConnection.setUseCaches(false);

   					inStream = new DataInputStream(urlConnection.getInputStream());

   					JSONObject responseObject2 = new JSONObject(inStream.readLine());
   					
   					//TODO check if it is not an error response
   					
   				} catch(Exception ex){
   					mEx = ex;
   				} finally {
   					try {
   						inStream.close();
   					} catch (IOException e) {
   						Log.e(Services.TAG, e.getMessage());
   					}
   				}
   				
   				return null;
   			}
   			
   		};
   		volumeSetter.execute();
	}
	
	//custom listadapter for showing listitems with icon (depending on type)
    private class StationListAdapter extends ArrayAdapter<RadioStation> {

	    private ArrayList<RadioStation> items;

	    public StationListAdapter(Context context, int textViewResourceId, ArrayList<RadioStation> items) {
	            super(context, textViewResourceId, items);
	            this.items = items;
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	            View v = convertView;
	            if (v == null) {
	                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                v = vi.inflate(R.layout.listitem_service, null);
	            }
	            RadioStation o = items.get(position);
	            if (o != null) {
		            	TextView tvCaption = (TextView) v.findViewById(R.id.tvServiceName);
	                    if (tvCaption != null) {
	                    	tvCaption.setText(o.getDisplayName());
	                    }
	            }
	            return v;
	    }
	}	

}
