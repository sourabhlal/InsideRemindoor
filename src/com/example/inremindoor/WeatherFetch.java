package com.example.inremindoor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;
import android.util.Log;
 
public class WeatherFetch {
     
/*	public static JSONObject getJSON(){
		try{
		  String json = getResponse();
		  Log.e("frackin hell",json);
	      JSONObject data = new JSONObject(json);
	      
	      if(data.getInt("cod") != 200){
	          return null;
	      }
	       
	      return data;	
		}
		catch(Exception e){
			Log.e("Error", "failure to find problem");
			return null;
		}
	}*/
	
	public static JSONObject getJSON(){
		  try{
		    URL url;
		    url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Pittsburgh&units=metric");
		    URLConnection connection = (URLConnection)url.openConnection();
		    connection.addRequestProperty("x-api-key", "68f6639a09bfb74609967efd59688e11");
		    //Log.e("Status", "Connection set");
		    InputStream is = connection.getInputStream();
		    //Log.e("Status", "Stream set");	
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    //Log.e("Status", "Reader set"); 
		    StringBuffer json = new StringBuffer(1024);
		    String tmp="";
		    while((tmp=reader.readLine())!=null){
		      json.append(tmp).append("\n");
		    }
		    reader.close();
		    JSONObject data = new JSONObject(json.toString());

		    //Log.e("TTData", json.toString()); 
		    // This value will be 404 if the request was not successful
		    if(data.getInt("cod") != 200){
		      return null;
		    }
		    return data;
		  }
		  catch(Exception e){
		    return null;
		  }   
		}
}
