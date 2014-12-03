package com.example.inremindoor;

import org.json.JSONObject;

import java.util.*;
import java.util.Map.Entry;

import android.util.Log;

public class TravelTimeActivity {

LinkedHashMap <String,String> travelTimeData = new LinkedHashMap <String,String>();
	
	public String getTravelTimeData() {
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<String, String> currentData = travelTimeData;
		for (Entry<String, String> e : currentData.entrySet()){
			sb.append(e.getValue());
		}
		Log.e("ttstring", sb.toString());
		return sb.toString();
	}
	
	public void updateTravelTimeData(){
	    new Thread(){
	        public void run(){
	    	    final JSONObject json = TravelTimeFetch.getJSON();
	    	    if(json == null){
	    	        Log.e("TTJSON", "Not Found");
	    	    } else {
	    	    	Log.e("TTJSON", "Data Found");
	    	    	travelTimeData = renderWeather(json);
	    	    }                  
	        }
	    }.start();   
	}
	
	private LinkedHashMap <String,String> renderWeather(JSONObject json){
		LinkedHashMap <String,String> incomingData = new LinkedHashMap<String,String>();
		
	    try {

	    	JSONObject data = json.getJSONArray("rows").getJSONObject(0);
	    	JSONObject duration = data.getJSONArray("elements").getJSONObject(0).getJSONObject("duration");
	    	
	    	duration.getString("text");
	    	duration.getInt("value");
	    	
	    	String Z = duration.getString("text");
	    	int i = Z.indexOf(' ');
	    	String X = Z.substring(0, i);
	    	String Y = ((Integer)duration.getInt("value")).toString();
	        
	        
	    	incomingData.put("Travel time", "Expected travel time to get to work is "+X+" minutes. ");
	    	//Log.e("Travel time", "Expected travel time to get to work is "+X+" minutes. ");
	    	
	    	if (duration.getInt("value")<900){
		    	incomingData.put("Difference", "This is less than the usual amount of time it takes. ");
		    	//Log.e("Difference", "This is less than the usual amount of time it takes, ");
		    	incomingData.put("Suggestion", "As you have a couple of extra minutes, there is no need to rush! Make sure you have picked up everything you need! ");
		    	//Log.e("Suggestion", "As you have a couple of extra minutes, there is no need to rush! Make sure you have picked up everything you need! ");

	    	}
	    	else{
		    	incomingData.put("Difference", "This is more than the usual amount of time it takes, ");
		    	//Log.e("Difference", "This is less than the usual amount of time it takes ");
		    	incomingData.put("Suggestion", "so I would suggest that you leave a bit early to beat the rush! ");
		    	//Log.e("Suggestion", "so I would suggest that you leave a bit early to beat the rush! ");

	    	}

	    	
	    	
	    }catch(Exception e){
	        Log.e("SimpleWeather", "One or more fields not found in the JSON data");
	    }
		return incomingData;
	}
	
	
	
}
