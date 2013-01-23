package at.theengine.android.snakeberry.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import at.theengine.android.snakeberry.dataobjects.SnakeberryException;
import at.theengine.android.snakeberryremote.RemoteStart;

public class Utils {
	
	public static String SNAKEBERRY_PORT = "8888"; //TODO Get this from settings!

	public static String getLocalIp() {
		try{
		for(Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); 
				nis.hasMoreElements();) {
			NetworkInterface nitf = nis.nextElement();
			for (Enumeration<InetAddress> ipAddr = nitf.getInetAddresses(); ipAddr.hasMoreElements();) {
				InetAddress ip = ipAddr.nextElement();
				if (!ip.isLoopbackAddress()  && InetAddressUtils.isIPv4Address(ip.getHostAddress())) {
					return ip.getHostAddress();
				}
			}
		}
		}catch(SocketException ex){
			Log.w(RemoteStart.TAG, "Could not get local Ip! SocketException: " + ex.getMessage());
		}
		
		Log.w(RemoteStart.TAG, "Could not get local Ip!");
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static String getStringResponseFromGetRequest(String requestUrl) throws IOException{
		URL url1;
		URLConnection urlConnection;
		DataInputStream inStream;

		url1 = new URL(requestUrl);
		urlConnection = url1.openConnection();
		((HttpURLConnection)urlConnection).setRequestMethod("GET");
		urlConnection.setDoInput(true);
		urlConnection.setDoOutput(false);
		urlConnection.setUseCaches(false);

		inStream = new DataInputStream(urlConnection.getInputStream());
		
		return inStream.readLine();
	}

	public static JSONObject handleServiceResponse(String rawResponse) throws JSONException, SnakeberryException{
		JSONObject response = new JSONObject(rawResponse);
		
		//check if the service returned an error
		int errCode = response.getInt("ErrorCode");
		String errMsg = response.getString("ErrorMessage");
		if(errCode != 0){
			throw new SnakeberryException(errCode, errMsg);
		}
		
		return response;
	}
}
