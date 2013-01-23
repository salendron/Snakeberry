package at.theengine.android.snakeberry.dataobjects;

public class SnakeberryException extends Exception {

	private static final long serialVersionUID = 7117884007368842788L;
	
	private int mCode;
	private String mMsg;
	
	public SnakeberryException(int code, String msg){
		this.mCode = code;
		this.mMsg = msg;
	}

	public int getmCode() {
		return mCode;
	}

	public String getmMsg() {
		return mMsg;
	}
}
