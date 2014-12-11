package com.example.inremindoor;

import org.json.JSONObject;

import java.util.*;
import java.util.Map.Entry;

import android.util.Log;


public class WeatherActivity {
	
	LinkedHashMap <String,String> weatherData = new LinkedHashMap <String,String>();
	
	public String getWeatherData() {
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<String, String> currentData = weatherData;
		for (Entry<String, String> e : currentData.entrySet()){
			sb.append(e.getValue());
		}
		Log.e("weatherstring", sb.toString());
		return sb.toString();
	}
	
	public void updateWeatherData(){
	    new Thread(){
	        public void run(){
	    	    final JSONObject json = WeatherFetch.getJSON();
	    	    if(json == null){
	    	        Log.e("WJSON", "Not Found");
	    	    } else {
	    	    	Log.e("WJSON", "Data Found");
	    	    	weatherData = renderWeather(json);
	    	    }                  
	        }
	    }.start();   
	}
	
	private LinkedHashMap <String,String> renderWeather(JSONObject json){
		LinkedHashMap <String,String> incomingData = new LinkedHashMap<String,String>();
		   int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		   String timeOfDay = "Day"; 
		   if(hour >19 && hour < 5){
			   timeOfDay = "Night";
		   }
		   else if(hour >4 && hour < 12){
			   timeOfDay = "Morning";
		   }
		   else if(hour >11 && hour < 16){
			   timeOfDay = "Afternoon";
		   }
		   else if(hour >15 && hour < 20){
			   timeOfDay = "Evening";
		   }
	    try {
	    	incomingData.put("City", "Today's weather forecast in "+ json.getString("name"));
//			Log.e("City", "Today's weather forecast in "+ json.getString("name"));
	    	
	    	JSONObject details = json.getJSONArray("weather").getJSONObject(0);
	        
	    	JSONObject main = json.getJSONObject("main");
	        
	    	incomingData.put("Description", " is that of "+details.getString("description")+".");
//	    	Log.e("Description", " is that of "+details.getString("description")+".");

	    	incomingData.put("Temperature", " The current temperature is " + String.format("%.1f", main.getDouble("temp"))+ " degrees celsius ");
//	    	Log.e("Temperature", " The current temperature is" + String.format("%.2f", main.getDouble("temp"))+ " degrees celsius ");
	    	
	    	incomingData.put("Humidity", "and there is a " + main.getString("humidity") + " percent chance of rain today. ");
//	    	Log.e("Humidity", "and there is a " + main.getString("humidity") + " percent chance of rain today.");
	    	int humidity = Integer.parseInt(main.getString("humidity"));
	    
	    	if (main.getDouble("temp") < 0){
	    		if (humidity > 70){
	    			incomingData.put("Suggestion", " Since temperatures are below the freezing point, I would suggest you pick up a coat and a warm beverage before you leave. It is also wise to carry an umbrella, given the increased humidity levels");
	    		}
	    		else{
	    			incomingData.put("Suggestion", " Since temperatures are below the freezing point, I would suggest you pick up a coat and a warm beverage before you leave");	
	    		}	    		
	    	}
	    	else if (main.getDouble("temp") < 10){
	    		if (humidity > 70){
	    			incomingData.put("Suggestion", " As it is a brisk "+timeOfDay+", perhaps you would like to pick up a sweater. You should also pick up an umbrella, given the high likelihood of rain today. ");
	    		}
	    		else{
	    			incomingData.put("Suggestion", " As it is a brisk "+timeOfDay+", perhaps you would like to pick up a sweater");	
	    		}
	    	}
	    	else if(main.getDouble("temp") > 30){
	    		if (humidity > 70){
	    			incomingData.put("Suggestion", " It's a hot and humid day today, make sure to pick up a water bottle and an umbrella before you leave!.");
	    		}
	    		else{
	    			incomingData.put("Suggestion", " It's a hot day today, make sure you stay well hydrated.");	
	    		}
	    	}
	    	else if(humidity >70){
	    		incomingData.put("Suggestion", " It will probably rain today. Don't forget your umbrella!");
	    	}
	    	
	    	
	    }catch(Exception e){
	        Log.e("SimpleWeather", "One or more fields not found in the JSON data");
	    }
		return incomingData;
	}
	
	
}
