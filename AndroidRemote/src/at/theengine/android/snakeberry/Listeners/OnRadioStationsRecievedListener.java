package at.theengine.android.snakeberry.Listeners;

import java.util.ArrayList;

import at.theengine.android.snakeberry.dataobjects.Radio;

public abstract class OnRadioStationsRecievedListener {

	public abstract void onRadioStationsRecieved(ArrayList<Radio> radioStations);
	public abstract void onError(Exception ex);
	
}
