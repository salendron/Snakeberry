package at.theengine.android.snakeberryremote;

import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import at.theengine.android.warp.R;
import at.theengine.android.warp.WarpDevices.DeviceListAdapter;

public class Services extends Activity {

	private Context mContext;
	private ListView mLstServices;
	private ArrayList<Service> mServices;
	private ProgressDialog pdLoadServices;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_services);
		
		mContext = this;
	}
	
	private void loadViews(){
		mLstServices = (ListView) findViewById(R.id.lstServices);
		
		pdLoadServices = new ProgressDialog(mContext);
		pdLoadServices.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pdLoadServices.setMessage("Working...");
		pdLoadServices.setIndeterminate(true);
		pdLoadServices.setCancelable(false);
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
					mLstServices.setAdapter((new ServiceListAdapter(this,R.layout.list_item_with_image, mDevices));
				}
			}
			
			@Override
			protected Object doInBackground(Object... params) {
				URL url1;
				URLConnection urlConnection;
				DataInputStream inStream;
				
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
					JSONArray jsonServices = (JSONArray) responseObject.get("Servcies");
					
					JSONObject service;
					for(int i = 0; i < jsonServices.length(); i++){
						service = (JSONObject) jsonServices.get(i);
						mServices.add(new Service(service.getString("DisplayName"), service.getString("BaseUrl")));
					}
					
				} catch(Exception ex){
					mEx = ex;
				} finally {
					inStream.close();
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
	                v = vi.inflate(R.layout., null);
	            }
	            Service o = items.get(position);
	            if (o != null) {
		            	TextView tvCaption = (TextView) v.findViewById(R.id.);
	                    if (tvCaption != null) {
	                    	tvCaption.setText(o.deviceName);
	                    }
	                    
	                    ImageView ivIcon = (ImageView) v.findViewById(R.id.litIvItemIcon);
	                    if(ivIcon != null){
	                    	ivIcon.setImageResource(R.drawable.ic_launcher);
	                    }
	            }
	            return v;
	    }
	}	
}
