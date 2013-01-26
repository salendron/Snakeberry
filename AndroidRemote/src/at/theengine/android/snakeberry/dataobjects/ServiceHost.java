package at.theengine.android.snakeberry.dataobjects;

import java.util.ArrayList;

public class ServiceHost {
	
	private String mIp;
	private String mHostName;
	private String mDisplayName;
	private String mMac;
	private ArrayList<Service> mServices;
	
	public ServiceHost(String ip, String hostName, String displayName, String mac) {
		super();
		this.mIp = ip;
		this.mHostName = hostName;
		this.mDisplayName = displayName;
		this.mMac = mac;
		
		this.mServices = new ArrayList<Service>();
	}
	
	public ArrayList<Service> getServices(){
		return mServices;
	}
	
	public void setServices(ArrayList<Service> services){
		mServices = services;
	}
	
	public void addService(Service service){
		mServices.add(service);
	}
	
	public String getIp() {
		return mIp;
	}
	
	public void setIp(String ip) {
		this.mIp = ip;
	}
	
	public String getHostName() {
		return mHostName;
	}
	
	public void setHostName(String hostName) {
		this.mHostName = hostName;
	}
	
	public String getDisplayName() {
		return mDisplayName;
	}
	
	public void setDisplayName(String displayName) {
		this.mDisplayName = displayName;
	}
	
	public String getMac() {
		return mMac;
	}
	
	public void setMac(String mac) {
		this.mMac = mac;
	}
}
