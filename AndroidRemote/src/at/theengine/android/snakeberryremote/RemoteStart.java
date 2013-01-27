package at.theengine.android.snakeberryremote;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import at.theengine.android.snakeberry.Listeners.OnServiceHostFinderFoundHostListener;
import at.theengine.android.snakeberry.animation.AnimationFactory;
import at.theengine.android.snakeberry.dataobjects.ServiceHost;
import at.theengine.android.snakeberry.net.ServiceHostFinder;
import at.theengine.android.snakeberryremote.R;

public class RemoteStart extends Activity {
	
	public static String TAG = "Snakeberry";

	private Context mContext;
	private ServiceHostFinder mFinder;
	private ArrayList<ServiceHost> mHosts;
	
	private static ServiceHost _SelectedHost;
	public static ServiceHost getHost(){
		return _SelectedHost;
	}
	
	//controlls
	private LinearLayout mLlSearchingHosts;
	private LinearLayout mLlrefreshHosts;
	private ListView mLvHosts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote_start);
		
		mContext = this;
		mHosts = new ArrayList<ServiceHost>();
		
		initializeHostFinder();
		initViews();
		
		//search for Snakeberries
		findServiceHosts();		
	}
	
	private void initViews(){
		mLlSearchingHosts = (LinearLayout) findViewById(R.id.llSearchingHosts);
		mLlrefreshHosts = (LinearLayout) findViewById(R.id.llrefreshHosts);
		mLvHosts = (ListView) findViewById(R.id.lvhosts);
		
		mLlrefreshHosts.setVisibility(View.GONE);
		mLlSearchingHosts.setVisibility(View.GONE);
		
		mLlrefreshHosts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				findServiceHosts();
			}
		});
		
		mLvHosts.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				_SelectedHost = mHosts.get(arg2);
				Intent radio = new Intent(); 
				radio.setClass(mContext, Radio.class); 
				startActivity(radio);
			}
		});
	}
	
	private void initializeHostFinder(){
		OnServiceHostFinderFoundHostListener serviceHostListener = new OnServiceHostFinderFoundHostListener() {
			
			@Override
			public void onHostFound(ServiceHost host) {
				addHost(host);
			}

			@Override
			public void onSearchComplete() {
				AnimationFactory.doFadeAnimation(mLlSearchingHosts, mLlrefreshHosts, mContext);
			}
		};
		
		mFinder = new ServiceHostFinder();
		mFinder.setOnServiceHostFinderFoundHostListener(serviceHostListener);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void findServiceHosts(){
		AnimationFactory.doFadeAnimation(mLlrefreshHosts, mLlSearchingHosts, mContext);
		mHosts = new ArrayList<ServiceHost>();
		(new AsyncTask() {
			@Override
			protected Object doInBackground(Object... params) {
				mFinder.findserviceHosts();
				return null;
			}
		}).execute();
	}
	
	private void addHost(ServiceHost host){
		mHosts.add(host);
		mLvHosts.setAdapter(new HostAdapter(mContext, R.id.ll_listitem_servicehost,mHosts));
	}
	
	private class HostAdapter extends ArrayAdapter<ServiceHost> {

	    private ArrayList<ServiceHost> items;

	    public HostAdapter(Context context, int viewResourceId, ArrayList<ServiceHost> items) {
	            super(context, viewResourceId, items);
	            this.items = items;
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	            View v = convertView;
	            if (v == null) {
	                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                v = vi.inflate(R.layout.listitem_servicehost, null);
	            }
	            ServiceHost o = items.get(position);
	            if (o != null) {
	            	TextView tvHostname = (TextView) v.findViewById(R.id.tvhostname);
                    if (tvHostname != null) {
                    	tvHostname.setText(o.getHostName());
                    }
                    
                    TextView tvHostip = (TextView) v.findViewById(R.id.tvhostip);
                    if (tvHostip != null) {
                    	tvHostip.setText(o.getIp());
                    }
	            }
	            return v;
	    }
	}
}
