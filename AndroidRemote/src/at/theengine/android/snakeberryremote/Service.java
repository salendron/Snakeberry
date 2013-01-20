package at.theengine.android.snakeberryremote;

public class Service {

	private String mDisplayName;
	private String mBaseUrl;
	
	public Service(String mDisplayName, String baseUrl) {
		super();
		this.mDisplayName = mDisplayName;
		this.mBaseUrl = baseUrl;
	}

	public String getDisplayName() {
		return mDisplayName;
	}

	public String getBaseUrl() {
		return mBaseUrl;
	}
	
	
}
