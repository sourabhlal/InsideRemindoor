package com.example.inremindoor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	TextToSpeech ttobj;
	WeatherActivity weatherData;
	String userResponse;
	TravelTimeActivity travelTimeData;
	//NewsActivity newsData;
	conversationState cs;
	TreeMap<Integer, LinkedList<String>> reminders;
	int category;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        userResponse="";
        reminders = new TreeMap<Integer, LinkedList<String>>();
        weatherData = new WeatherActivity();
        weatherData.updateWeatherData();
        travelTimeData = new TravelTimeActivity();
        travelTimeData.updateTravelTimeData();
        TextView txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
           	
    	category = 0;
        
        ttobj=new TextToSpeech(getApplicationContext(), 
        new TextToSpeech.OnInitListener() {    
        @Override
        public void onInit(int status) {
           if(status != TextToSpeech.ERROR){
               ttobj.setLanguage(Locale.UK);
              }				
           }
        });
        cs = new conversationState("John");
        // hide the action bar
        getActionBar().hide();
    }
    
    public void updateData(){
    	weatherData.updateWeatherData();
    	cs.conversations.put("Weather", weatherData.getWeatherData());
    	travelTimeData.updateTravelTimeData();
    	cs.conversations.put("Traffic", travelTimeData.getTravelTimeData());
    	//newsData.updateNewsData();
    	//information.put("News", newsData.getNewsData());
    	StringBuilder sb = new StringBuilder();
		for (Entry<Integer, LinkedList<String>> e : reminders.entrySet()){
			sb.append("I was supposed to remind you that, ");
			for (String s : e.getValue()){
				sb.append(s);
				sb.append(". Additionally, ");
			}
		}
		cs.conversations.put("Reminders", sb.toString());
    	
    }
    
    public void addReminder(View view){
    	category = 0;
    	speakReminder("Please state your reminder.", 3000);
    	promptSpeechInput();
    	
    }
    
    public void activateRemindoor(View view){
       cs.setQuestions();
       speakText("Start",10000);
       updateData();
  	  try {
		    Thread.sleep(3000);
		} catch (InterruptedException e) {
		    Thread.currentThread().interrupt();
		    return;
		}
       speakText("Loaded", 3000);
       speakText("News", 4000);
       speakText("Weather", 25000);
       speakText("Traffic", 15000);
     
       category = 1;
       speakText("Repeat", 6000);
       promptSpeechInput();
       }
    
    public void repeatReminders(String response){
    	if (!response.toLowerCase().contains("no")){
	   		if (response.toLowerCase().contains("weather") || response.toLowerCase().contains("temperature") || response.toLowerCase().contains("climate") || response.toLowerCase().contains("whether")){
	   			speakText("Weather", 20000);
	   			category = 1;
	   			speakText("Repeat",6000);
	   			promptSpeechInput();
	   		}
	   		else if (response.toLowerCase().contains("news") || response.toLowerCase().contains("local") || response.toLowerCase().contains("announcement") || response.toLowerCase().contains("report")){
	   			speakText("News", 4000);
	   			category = 1;
	   			speakText("Repeat",6000);
	   			promptSpeechInput();
	   		}
	   		else if (response.toLowerCase().contains("time") || response.toLowerCase().contains("travel") || response.toLowerCase().contains("commute") || response.toLowerCase().contains("traffic")){
	   			speakText("Traffic", 15000);
	   			category = 1;
	   			speakText("Repeat",6000);
	   			promptSpeechInput();
	   		}
/*	   		else if (response.toLowerCase().contains("alert") || response.toLowerCase().contains("reminder") || response.toLowerCase().contains("notification")){
	   	       //speakText("Reminders");
	   			category = 1;
	   		   speakText("Repeat",6000);
	   		   promptSpeechInput();
	   		}*/
       }
    	else{
    		speakText("Reminders",20000);
    		reminders = new TreeMap<Integer, LinkedList<String>>();
    	}
    }

    public void speakReminder(String toSpeak, int timeDelay){
   	   Log.d("convo", toSpeak);
  	   ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
  	  try {
 		    Thread.sleep(timeDelay);
 		} catch (InterruptedException e) {
 		    Thread.currentThread().interrupt();
 		    return;
 		}
     }
    
    public void speakText(String key, int timeDelay){
  	   String toSpeak = cs.conversations.get(key);
 	   if (key == "Reminders"){
 		toSpeak = toSpeak + cs.conversations.get("End");
 	   }
  	   Log.d("convo", toSpeak);
 	   ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
 	  try {
		    Thread.sleep(timeDelay);
		} catch (InterruptedException e) {
		    Thread.currentThread().interrupt();
		    return;
		}
    }
     
    private TextView txtSpeechInput;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    
    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case REQ_CODE_SPEECH_INPUT: {
            if (resultCode == RESULT_OK && null != data) {

                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (category == 0){
                	String inputString = result.get(0).replaceAll("I ", "You ");
                	inputString = inputString.replaceAll("i ", " you ");
                    inputString = inputString.replaceAll(" my ", " your ");
                    inputString = inputString.replaceAll(" My", " Your ");
                    
                    if (inputString.toLowerCase().contains("essential") || inputString.toLowerCase().contains("crucial") || inputString.toLowerCase().contains("vital")|| inputString.toLowerCase().contains("important")){
                    	LinkedList<String> newList = new LinkedList<String>();
                    	if (reminders.containsKey(1)){
                    		newList  = reminders.get(1);	
                    	}
                    	newList.add(inputString);
                    	reminders.put(1, newList);
                    }
                    else if (inputString.toLowerCase().contains("useful") ){
                    	LinkedList<String> newList = new LinkedList<String>();
                    	if (reminders.containsKey(2)){
                    		newList  = reminders.get(2);	
                    	}
                    	newList.add(inputString);
                    	reminders.put(2, newList);
                    }
                    else{
                    	LinkedList<String> newList = new LinkedList<String>();
                    	if (reminders.containsKey(3)){
                    		newList  = reminders.get(3);	
                    	}
                    	newList.add(inputString);
                    	reminders.put(3, newList);
                    }
                    speakReminder("Reminder recorded.", 4000);
                }
                else{
                	userResponse = result.get(0);
                	repeatReminders(result.get(0));
                }
            }
            break;
        }

        }
    }
    
    @Override
    public void onPause(){
       if(ttobj !=null){
          ttobj.stop();
       }
       super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
