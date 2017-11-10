package com.palyrobotics.frc2017.vision;

public class TimeoutHandler implements TimeoutProcedureBase {
	//Different types of timeouts
	public static int numFailures = 0;
	
	public enum TimeoutType{
		LINEAR, DEFAULT;
	}
	public int get(String key, TimeoutType currTimeout){
		if (key == null){
			//return TimeoutHandler; 
		}
		return 0; 
	}
	public void success(){
		numFailures = 0; 
	}
	public void failure(){
		numFailures++;
		try {
			Thread.sleep(numFailures);
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
