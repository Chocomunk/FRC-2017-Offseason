package com.palyrobotics.frc2017.vision;

import java.util.HashMap;
import com.palyrobotics.frc2017.vision.procedures.*; 

public class TimeoutHandler extends TimeoutProcedureBase {
	public static double baseValue; 
	public static double scalarValue; 
//TODO: More in depth README, add more methods into TimeoutProcedureBase instead of TimeoutHandler
	public TimeoutHandler(double baseValue, double scalarValue){
		 super(baseValue, scalarValue); 
	} 
	public enum TimeoutType{
		EXPONENTIAL, 
		LINEAR, 
		LOGARITHMIC; 
	}
	TimeoutType currType; 
	public int initialMill = 0; 
	
	/**
	* Resets the number of failures to 0
	*/
	public void success(){
		numFailures = 0; 
	}
	/**
	* Increments the number of failures
	* Sleeps the code based on the number of failures
	*/
	public void failure(){
		numFailures++;
		try {
			Thread.sleep(getFailureTime(numFailures));
			System.out.println(getFailureTime(numFailures) - initialMill);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Returns a unique timeout instance
	* @param key
	* @param currTimeoutType
	* @return TimeoutProcedureBase
	*/
	HashMap <TimeoutType, TimeoutProcedureBase> mTimeoutMap = new HashMap<>(); 
	public static TimeoutHandler get(HashMap mTimeoutMap, TimeoutType key, TimeoutProcedureBase value){
		if (!mTimeoutMap.containsKey(key)){
			TimeoutHandler newThing = new TimeoutHandler(key,value); 
		}
		if (!mTimeoutMap.containsValue(value)){
			switch(key){
				case EXPONENTIAL: 
					value = new ExponentialTimeout(baseValue,scalarValue); 
				case LINEAR: 
					value = new LinearTimeout(baseValue, scalarValue); 
				case LOGARITHMIC: 
					value = new LogarithmicTimeout(baseValue, scalarValue); 
				
			}
		}
		else{
			//Should set to some default procedure type
			 
		}
		TimeoutHandler randomThing = new TimeoutHandler(0,0); 
		return randomThing; 
	}
	/*public static TimeoutHandler get(String key, TimeoutType currTimeoutType){
		if(currTimeoutType == null){
			currTimeoutType = TimeoutType.DEFAULT;
		}
		
		if (key == "nexus_connected"){
			currTimeoutType = TimeoutType.LINEAR;
			TimeoutHandler nexus = new TimeoutHandler(0,0); 
			return nexus; 
		}
		else{
			key = "key for default??";
			TimeoutHandler newHandler = new TimeoutHandler(0,0); 
			return newHandler; 
		}
	
	
	}*/ 
	
	/**
	* Function of the count of failures
	* @param numFailures
	* @return sleep time
	*/
	public int getFailureTime(int numFailures){
		return numFailures * 100; 
	}
}

