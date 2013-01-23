package at.theengine.android.snakeberryremote;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import at.theengine.android.snakeberry.animation.AnimationFactory;
import at.theengine.android.snakeberry.dataobjects.ServiceHost;
import at.theengine.android.snakeberry.net.ServiceHostFinder;
import at.theengine.android.snakeberry.net.onServiceHostFinderFoundHostListener;

public class RemoteStart extends Activity {
	
	public static String TAG = "Snakeberry";

	private Context mContext;
	private ServiceHostFinder mFinder;
	
	//controlls
	private LinearLayout mLlSearchingHosts;
	private LinearLayout mLlrefreshHosts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote_start);
		
		mContext = this;
		
		initializeHostFinder();
		initViews();
		
		//search for Snakeberries
		findServiceHosts();
	}
	
	private void initViews(){
		mLlSearchingHosts = (LinearLayout) findViewById(R.id.llSearchingHosts);
		mLlrefreshHosts = (LinearLayout) findViewById(R.id.llrefreshHosts);
		
		mLlrefreshHosts.setVisibility(View.GONE);
		mLlSearchingHosts.setVisibility(View.GONE);
		
		mLlrefreshHosts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				findServiceHosts();
			}
		});
	}
	
	private void initializeHostFinder(){
		onServiceHostFinderFoundHostListener serviceHostListener = new onServiceHostFinderFoundHostListener() {
			
			@Override
			public void onHostFound(ServiceHost host) {
				Toast.makeText(mContext, host.getIp() + " - " + host.getHostName() , Toast.LENGTH_SHORT).show();
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
	public void findServiceHosts(){
		AnimationFactory.doFadeAnimation(mLlrefreshHosts, mLlSearchingHosts, mContext);
		(new AsyncTask() {
			@Override
			protected Object doInBackground(Object... params) {
				mFinder.findserviceHosts();
				return null;
			}
		}).execute();
	}

}
