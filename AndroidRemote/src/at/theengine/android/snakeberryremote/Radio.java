package at.theengine.android.snakeberryremote;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import at.theengine.android.snakeberry.Listeners.OnNowPlayingRecieveListener;
import at.theengine.android.snakeberry.Listeners.OnRadioPlayListener;
import at.theengine.android.snakeberry.Listeners.OnRadioStationsRecievedListener;
import at.theengine.android.snakeberry.Listeners.OnRadioStopListener;
import at.theengine.android.snakeberry.dataobjects.NowPlaying;
import at.theengine.android.snakeberry.dataobjects.ServiceHost;
import at.theengine.android.snakeberry.net.MediaSystemService;
import at.theengine.android.snakeberry.net.RadioService;
import at.theengine.android.snakeberry.widget.VolumeSlider;
import at.theengine.android.snakeberryremote.R;

public class Radio extends Activity {
	public static String TAG = "Snakeberry";

	private Context mContext;
	private ServiceHost mHost;
	private ArrayList<at.theengine.android.snakeberry.dataobjects.Radio> mRadioStations;
	
	//controlls
	private TextView mTvRadio;
	private VolumeSlider mVolumeWidget;
	private TextView mTvNowPlayingType;
	private TextView mTvNowPlayingDescription;
	private ImageButton mBtnStop;
	private ListView mLvStations;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radio);
		
		mContext = this;
		mHost = RemoteStart.getHost();
		if(mHost == null){
			this.finish();
		}
		
		initViews();
		loadRadioStations();
		loadNowPlaying();
	}
	
	private void initViews(){
		mLvStations = (ListView) findViewById(R.id.lvStations);
		
		mTvRadio = (TextView) findViewById(R.id.tvRadio);
		mTvRadio.setText(mHost.getDisplayName());
		
		mVolumeWidget = (VolumeSlider) findViewById(R.id.vsVolume);
		mVolumeWidget.setHost(mHost);
		
		mTvNowPlayingType  = (TextView) findViewById(R.id.tvNowPlayingType);
		mTvNowPlayingDescription = (TextView) findViewById(R.id.tvNowPlayingDescription);
		
		mBtnStop = (ImageButton) findViewById(R.id.btnStop);
		mBtnStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RadioService service = new RadioService();
				service.stop(mHost.getIp(), new OnRadioStopListener() {
					
					@Override
					public void onRadioStopped() {
						loadNowPlaying();
					}
					
					@Override
					public void onError(Exception ex) {
						Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		
		mLvStations.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				RadioService service = new RadioService();
				service.play(mHost.getIp(), mRadioStations.get(arg2).getRadioId(), new OnRadioPlayListener() {
					
					@Override
					public void onError(Exception ex) {
						Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
					}

					@Override
					public void onRadioPlays() {
						loadNowPlaying();
					}
				});
			}
		});
	}	
	
	private void loadNowPlaying(){
		MediaSystemService service = new MediaSystemService();
		service.getNowPlaying(mHost.getIp(), new OnNowPlayingRecieveListener() {
			
			@Override
			public void onNowPlayingRecieved(NowPlaying nowplaying) {
				mTvNowPlayingType.setText(nowplaying.getMediaType());
				mTvNowPlayingDescription.setText(nowplaying.getDescription());
			}
			
			@Override
			public void onError(Exception ex) {
				Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
			}
		});
	}
	
	private void loadRadioStations(){
		mRadioStations = null;
		RadioService service = new RadioService();
		service.getRadioStations(mHost.getIp(), new OnRadioStationsRecievedListener() {
			
			
			@Override
			public void onError(Exception ex) {
				Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onRadioStationsRecieved(
					ArrayList<at.theengine.android.snakeberry.dataobjects.Radio> radioStations) {
				mRadioStations = radioStations;
				mLvStations.setAdapter(new RadioStationAdapter(mContext, R.id.ll_listitem_radioStation,mRadioStations));
			}
		});
	}
	
	private class RadioStationAdapter extends ArrayAdapter<at.theengine.android.snakeberry.dataobjects.Radio> {

	    private ArrayList<at.theengine.android.snakeberry.dataobjects.Radio> items;

	    public RadioStationAdapter(Context context, int viewResourceId, ArrayList<at.theengine.android.snakeberry.dataobjects.Radio> items) {
	            super(context, viewResourceId, items);
	            this.items = items;
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	            View v = convertView;
	            if (v == null) {
	                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                v = vi.inflate(R.layout.listitem_radiostation, null);
	            }
	            at.theengine.android.snakeberry.dataobjects.Radio o = items.get(position);
	            if (o != null) {
	            	TextView tvRadioStationName = (TextView) v.findViewById(R.id.tvRadioStationName);
                    if (tvRadioStationName != null) {
                    	tvRadioStationName.setText(o.getDisplayName());
                    }
	            }
	            return v;
	    }
	}
	
}
