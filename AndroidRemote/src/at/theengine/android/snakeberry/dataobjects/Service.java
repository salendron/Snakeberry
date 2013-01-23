package at.theengine.android.snakeberry.dataobjects;

public class Service {

	private String displayName;
	private String baseUrl;
	
	public Service(String displayName, String baseUrl) {
		super();
		this.displayName = displayName;
		this.baseUrl = baseUrl;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	
	
}
