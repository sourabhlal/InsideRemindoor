package com.example.inremindoor;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class conversationState {

   int currentState;
   int timeDelay;
   String residentName;
   Map<String,String> conversations = new HashMap<String,String>();

   public conversationState(String owner){
	   this.residentName = owner;
	   this.currentState = 1;
   }
   
public void setQuestions(){
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
	   conversations.put("Start", "Good "+timeOfDay+" "+residentName+", I will quickly prepare your reminders. Please wait a moment. ");
	   conversations.put("Loaded", " Okay, all information loaded. ");
	   conversations.put("News", "There is no new Breaking News in the last 30 minutes");
	   conversations.put("Repeat", " Is there anything I should repeat? Perhaps about traffic, news, or weather? ");
	   conversations.put("End", " And that is all for now! Have a good day! ");
	   
	  
   }
}