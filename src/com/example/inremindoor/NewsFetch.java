package com.example.inremindoor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

public class NewsFetch {
	public static JSONObject getJSON(){
		  try{
		    URL url;
		    String seconds = ((Integer)(int)(System.currentTimeMillis() / 1000)).toString();
		    url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=40.443388,-79.944878&departure_time="+seconds+"&destinations=40.447729,-80.003057&mode=driving&language=en-EN&key=AIzaSyCtoYhCPs0M0FUTY7nRtEwE5lnY6hoD8Wc");
		    URLConnection connection = (URLConnection)url.openConnection();
		    //Log.e("TTStatus", "Connection set");
		    InputStream is = connection.getInputStream();
		    //Log.e("TTStatus", "Stream set");	
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    //Log.e("TTStatus", "Reader set"); 
		    StringBuffer json = new StringBuffer(1024);
		    String tmp="";
		    while((tmp=reader.readLine())!=null){
		      json.append(tmp).append("\n");
		    }
		    reader.close();
		    JSONObject data = new JSONObject(json.toString());
		    //Log.e("TTData", json.toString()); 
		    //if(data.getString("status") != "OK"){
			      //return null;
			//}
		    return data;
		  }
		  catch(Exception e){
		    return null;
		  }   
		}

}
