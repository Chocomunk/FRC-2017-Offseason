package com.palyrobotics.frc2017.vision;

import java.util.HashMap;
import com.palyrobotics.frc2017.vision.procedures.*; 
//TODO: More in depth README, add more methods into TimeoutProcedureBase instead of TimeoutHandler
public class TimeoutHandler extends TimeoutProcedureBase {
	public static double baseValue; 
	public static double scalarValue; 
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
	public static HashMap<String, TimeoutProcedureBase> mTimeoutMap = new HashMap<>();  
	public static TimeoutProcedureBase get(String key, TimeoutProcedureBase value){
		if (!mTimeoutMap.containsKey(key)) {
			mTimeoutMap.put("default", value); 
		}
		if (!mTimeoutMap.containsValue(value)){
			switch(key){
				case "EXPONENTIAL": 
					value = new ExponentialTimeout(baseValue,scalarValue); 
				case "LINEAR": 
					value = new LinearTimeout(baseValue, scalarValue); 
				case "LOGARITHMIC": 
					value = new LogarithmicTimeout(baseValue, scalarValue); 
			}
			 
		}
		mTimeoutMap.put(key, value);
		return value; 
	}
	@Override
	public int getDuration(int mFailureCount) {
		return mFailureCount * 100; 
	}
	
	
}

