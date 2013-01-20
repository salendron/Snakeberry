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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Services extends Activity {

	private Context mContext;
	private ListView mLstServices;
	private ArrayList<Service> mServices;
	private ProgressDialog pdLoadServices;
	
	public static String TAG = "Snakeberry";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_services);
		
		mContext = this;
		
		loadViews();
		loadServices();
	}
	
	private void loadViews(){
		mLstServices = (ListView) findViewById(R.id.lstServices);
		
		pdLoadServices = new ProgressDialog(mContext);
		pdLoadServices.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pdLoadServices.setMessage("Working...");
		pdLoadServices.setIndeterminate(true);
		pdLoadServices.setCancelable(false);
		
		mLstServices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent device = new Intent(); 
				device.setClass(mContext, Radio.class); 
				startActivity(device);
			}
		});
	}
	
	private void loadServices(){
		AsyncTask serviceLoader = new AsyncTask(){
			
			private Exception mEx;
			
			@Override
			protected void onPostExecute(Object result) {
				pdLoadServices.dismiss();
				if(mEx != null){
					Toast.makeText(mContext, mEx.getMessage(), Toast.LENGTH_LONG).show();
				} else {
					mLstServices.setAdapter((new ServiceListAdapter(mContext,R.layout.listitem_service, mServices)));
				}
			}
			
			@Override
			protected Object doInBackground(Object... params) {
				URL url1;
				URLConnection urlConnection;
				DataInputStream inStream = null;
				
				try{
					// Create connection
					url1 = new URL("http://10.0.0.150:8888/"); //TODO get Url from Settings
					urlConnection = url1.openConnection();
					((HttpURLConnection)urlConnection).setRequestMethod("GET");
					urlConnection.setDoInput(true);
					urlConnection.setDoOutput(false);
					urlConnection.setUseCaches(false);

					inStream = new DataInputStream(urlConnection.getInputStream());

					JSONObject responseObject = new JSONObject(inStream.readLine());
					
					//TODO check if it is not an error response
					JSONArray jsonServices = (JSONArray) responseObject.get("Services");
					
					JSONObject service;
					mServices = new ArrayList<Service>();
					for(int i = 0; i < jsonServices.length(); i++){
						service = (JSONObject) jsonServices.get(i);
						mServices.add(new Service(service.getString("DisplayName"), service.getString("BaseUrl")));
					}
					
				} catch(Exception ex){
					mEx = ex;
				} finally {
					try {
						inStream.close();
					} catch (IOException e) {
						Log.e(TAG, e.getMessage());
					}
				}
				
				return null;
			}
			
		};
		
		pdLoadServices.show();
		serviceLoader.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_services, menu);
		return true;
	}

	//custom listadapter for showing listitems with icon (depending on type)
    private class ServiceListAdapter extends ArrayAdapter<Service> {

	    private ArrayList<Service> items;

	    public ServiceListAdapter(Context context, int textViewResourceId, ArrayList<Service> items) {
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
	            Service o = items.get(position);
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
