package at.theengine.android.snakeberry.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.Toast;
import at.theengine.android.snakeberry.Listeners.OnVolumeRecieveListener;
import at.theengine.android.snakeberry.Listeners.OnVolumeSetListener;
import at.theengine.android.snakeberry.dataobjects.ServiceHost;
import at.theengine.android.snakeberry.net.MediaSystemService;
import at.theengine.android.snakeberryremote.R;

public class VolumeSlider extends LinearLayout {

	//the image that represents the slider
	private ImageView slider;

	//the application context
	private Context ctx;
	
	private ServiceHost mHost;

	//slider position and size
    private int offset_x = 0;
    private int leftEnd = 0;
    private int rightEnd = 0;
    private int sliderWidth = 0;
    
    static final int[] VolumeBounds = {-4000,4398};
    
    private MediaSystemService mSettings;
    
    //state indicators
    private boolean initialized = false;
    
    
	/* constructor
	 * initializes the slider 
     */
	public VolumeSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(HORIZONTAL);
		this.ctx = context;

		initButton();
	}
    
	/* 
	 * called from layout when this view should assign a size
	 * and a position to all of its children.
	 */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
    	//get bounds
    	this.leftEnd = l; 
    	this.rightEnd = r;
    	
    	super.onLayout(changed, l, t, r, b); //call super
    }
    
    /*
     * initializes the button and add touch events to the slider
     */
    @SuppressWarnings("deprecation")
	private void initButton()
    {
    	if(!initialized){ //initialize just once
    		
    		//initialize the slider image view and add it to the parent view
	    	this.slider = new ImageView(ctx); 
	    	
	    	Drawable d = ctx.getResources().getDrawable(R.drawable.slider);
	    	slider.setBackgroundDrawable(d);
	    	slider.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

	    	this.addView(slider);

	    	//add an onTouchListener to the slider
			this.slider.setOnTouchListener(new View.OnTouchListener() {

				//ontouch get the xoffset of the event
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	                    switch(event.getActionMasked())
	                    {
	                            case MotionEvent.ACTION_DOWN:
	                                    offset_x = (int)event.getX();
	                                    break;
	                            default:
	                                    break;
	                    }

	                    return false;
	            }
			});

			//add an ontouchlistiner to the whole control
	    	this.setOnTouchListener(new View.OnTouchListener() {

	    		//drag the control
		        @Override
		        public boolean onTouch(View v, MotionEvent event) {
		        	int x = 0;
		        	LinearLayout.LayoutParams lp;

		        	switch(event.getActionMasked())
		            {
		            	case MotionEvent.ACTION_MOVE:
		            		//drag the control
		            		x = (int)event.getX() - offset_x;

		            		if(x > rightEnd -30 - sliderWidth)
		                    	x = rightEnd -30 - sliderWidth;

		            		if(x < leftEnd -20)
		                    	x = leftEnd -20;

		                    lp = new LinearLayout.LayoutParams(
		                    		new ViewGroup.MarginLayoutParams(
		                    				LinearLayout.LayoutParams.WRAP_CONTENT,
		                                    LinearLayout.LayoutParams.WRAP_CONTENT));

		                    lp.setMargins(x, 0, 0, 0);
		                    slider.setLayoutParams(lp);
		                    break;

		            	case MotionEvent.ACTION_UP:
		            		if(mHost != null){
			            		int val = (int) ((int)event.getX() / (rightEnd / 100.0));
			            		int newVolume = (int) (VolumeBounds[0] + ((VolumeBounds[1] / 100.0) * val));
			            		mSettings.setVolume(
			            				mHost.getIp(), 
			            				newVolume, 
			            				new OnVolumeSetListener() {
											
											@Override
											public void onVolumeSet() { }
											
											@Override
											public void onError(Exception ex) {
												Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
											}
										});
		            		}
		            	default:
		            		break;
		            }
		            return true;
		        }
		   });
    	}
    }
    
    private void setValue(int value){
    	int val = (int) (((rightEnd - 30) / 100.0) * value);
    	
    	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        				new ViewGroup.MarginLayoutParams(
        					LinearLayout.LayoutParams.WRAP_CONTENT,
        					LinearLayout.LayoutParams.WRAP_CONTENT));

        lp.setMargins(val, 0, 0, 0);
        slider.setLayoutParams(lp);
    }

    /*
     * finalize inflating a view from XML.
     */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	public ServiceHost getHost() {
		return mHost;
	}

	public void setHost(ServiceHost host) {
		this.mHost = host;
		if(mHost != null){
			mSettings = new MediaSystemService();
			
			//get volume from host
			mSettings.getVolume(mHost.getIp(), new OnVolumeRecieveListener() {
				
				@Override
				public void onVolumeRecieved(int volume) {
					if(volume < VolumeBounds[0]){
						volume = 0;
					} else if(volume > (VolumeBounds[0] + VolumeBounds[1])){
						volume = 100;
					} else {
						volume = volume + (-VolumeBounds[0]);
						volume = (int) (volume / (VolumeBounds[1] / 100.0));
					}
					
					setValue(volume);
				}
				
				@Override
				public void onError(Exception ex) {
					Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
