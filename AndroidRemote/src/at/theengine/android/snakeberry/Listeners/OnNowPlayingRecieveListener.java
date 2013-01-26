package at.theengine.android.snakeberry.Listeners;

import at.theengine.android.snakeberry.dataobjects.NowPlaying;

public abstract class OnNowPlayingRecieveListener {

	public abstract void onNowPlayingRecieved(NowPlaying nowplaying);
	public abstract void onError(Exception ex);
	
}
