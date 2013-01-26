package at.theengine.android.snakeberry.dataobjects;

public class NowPlaying {
	private String mMediaURI;
	private String mPID;
	private String meMdiaType;
	private String mDescription;
	
	public NowPlaying(String mediaURI, String pID, String mediaType,
			String description) {
		super();
		this.mMediaURI = mediaURI;
		this.mPID = pID;
		this.meMdiaType = mediaType;
		this.mDescription = description;
	}
	
	public String getMediaURI() {
		return mMediaURI;
	}
	public void setMediaURI(String mediaURI) {
		this.mMediaURI = mediaURI;
	}
	public String getpID() {
		return mPID;
	}
	public void setpID(String pID) {
		this.mPID = pID;
	}
	public String getMediaType() {
		return meMdiaType;
	}
	public void setMediaType(String mediaType) {
		this.meMdiaType = mediaType;
	}
	public String getDescription() {
		return mDescription;
	}
	public void setDescription(String description) {
		this.mDescription = description;
	}
	
	
}
