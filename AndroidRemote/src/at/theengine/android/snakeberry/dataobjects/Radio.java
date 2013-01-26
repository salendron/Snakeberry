package at.theengine.android.snakeberry.dataobjects;

public class Radio {

	private String mStreamUrl;
	private String mDisplayName;
	private String mRadioId;
		
	public Radio(String streamUrl, String displayName, String radioId) {
		super();
		this.mStreamUrl = streamUrl;
		this.mDisplayName = displayName;
		this.mRadioId = radioId;
	}
	
	public String getStreamUrl() {
		return mStreamUrl;
	}
	public void setStreamUrl(String streamUrl) {
		this.mStreamUrl = streamUrl;
	}
	public String getDisplayName() {
		return mDisplayName;
	}
	public void setDisplayName(String displayName) {
		this.mDisplayName = displayName;
	}
	public String getRadioId() {
		return mRadioId;
	}
	public void setRadioId(String radioId) {
		this.mRadioId = radioId;
	}
	
	
	
}
