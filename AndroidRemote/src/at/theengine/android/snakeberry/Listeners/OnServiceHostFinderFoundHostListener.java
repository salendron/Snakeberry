package at.theengine.android.snakeberry.Listeners;

import at.theengine.android.snakeberry.dataobjects.ServiceHost;

public abstract class OnServiceHostFinderFoundHostListener {

	public abstract void onHostFound(ServiceHost host);
	
	public abstract void onSearchComplete();
	
}
