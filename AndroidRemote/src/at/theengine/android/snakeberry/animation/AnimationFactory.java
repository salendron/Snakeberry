package at.theengine.android.snakeberry.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class AnimationFactory {

	public static Animation getButtonClickAnimation(){	
		Animation anim;
		
		anim = new AlphaAnimation(0, 1);
		
		anim.setDuration(500);
		anim.setRepeatMode(0);
		anim.setFillAfter(true);
		anim.setRepeatCount(0);
		
		return anim;
	}
	
	public static void doFadeAnimation(View outView, View inView, Context c){	
		final View iv = inView;
		final View ov = outView;
		
		final Animation fadeInAnim = AnimationUtils.makeInAnimation(c, false);
		
		fadeInAnim.setDuration(500);
		fadeInAnim.setRepeatMode(0);
		fadeInAnim.setFillAfter(false);
		fadeInAnim.setRepeatCount(0);	
		
		final Animation fadeOutAnim = AnimationUtils.makeOutAnimation(c, true);
		fadeOutAnim.setDuration(500);
		fadeOutAnim.setRepeatMode(0);
		fadeOutAnim.setFillAfter(false);
		fadeOutAnim.setRepeatCount(0);	
		
		fadeOutAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) { }
			
			@Override
			public void onAnimationRepeat(Animation animation) { }
			
			@Override
			public void onAnimationEnd(Animation animation) {
				ov.setVisibility(View.GONE);
				
				if(iv != null){
					iv.setVisibility(View.VISIBLE);
					iv.startAnimation(fadeInAnim);
				}
			}
		});
		
		if(ov != null){
			ov.startAnimation(fadeOutAnim);
		} else {
			iv.startAnimation(fadeInAnim);
		}
	}	
}
