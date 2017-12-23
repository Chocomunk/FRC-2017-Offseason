package com.palyrobotics.frc2017.vision;

import java.util.HashMap;
import com.palyrobotics.frc2017.vision.procedures.*; 

public class TimeoutHandler extends TimeoutProcedureBase {
	public static double baseValue; 
	public static double scalarValue; 
//TODO: More in depth README, add more methods into TimeoutProcedureBase instead of TimeoutHandler
	public TimeoutHandler(double baseValue, double scalarValue) {
		 super(baseValue, scalarValue); 
	} 
	public enum TimeoutType {
		EXPONENTIAL, 
		LINEAR, 
		LOGARITHMIC; 
	}
	TimeoutType currType; 
	public int initialMill = 0; 	
	
	/**
	* Returns a unique timeout instance
	* @param key
	* @param currTimeoutType
	* @return TimeoutProcedureBase
	*/
	HashMap <TimeoutType, TimeoutProcedureBase> mTimeoutMap = new HashMap<>(); 
	public static TimeoutHandler get(HashMap mTimeoutMap, TimeoutType key, TimeoutProcedureBase value){
		if (!mTimeoutMap.containsKey(key)) {
			TimeoutHandler newThing = new TimeoutHandler(key,value); // fix please
		}
		if (!mTimeoutMap.containsValue(value)) {
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
	
}

