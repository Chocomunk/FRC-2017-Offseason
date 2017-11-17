package com.palyrobotics.frc2017.vision;



public class TimeoutHandler extends TimeoutProcedureBase {

//TODO: More in depth README, add more methods into TimeoutProcedureBase instead of TimeoutHandler

	public enum TimeoutType{
		LINEAR, 
		DEFAULT;
	}
	
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
	public static TimeoutHandler get(String key, TimeoutType currTimeoutType){
		if(currTimeoutType == null){
			currTimeoutType = TimeoutType.DEFAULT;
		}
		
		if (key == "nexus_connected"){
			currTimeoutType = TimeoutType.LINEAR;
			TimeoutHandler nexus = new TimeoutHandler(); 
			return nexus; 
		}
		else{
			key = "key for default??";
			TimeoutHandler newHandler = new TimeoutHandler(); 
			return newHandler; 
		}
	
	
	}
	
	/**
	* Function of the count of failures
	* @param numFailures
	* @return sleep time
	*/
	public int getFailureTime(int numFailures){
		return numFailures * 100; 
	}
}

