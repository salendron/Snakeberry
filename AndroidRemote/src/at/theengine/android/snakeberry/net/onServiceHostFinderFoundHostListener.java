package at.theengine.android.snakeberry.net;

import at.theengine.android.snakeberry.dataobjects.ServiceHost;

public abstract class onServiceHostFinderFoundHostListener {

	public abstract void onHostFound(ServiceHost host);
	
	public abstract void onSearchComplete();
	
}
