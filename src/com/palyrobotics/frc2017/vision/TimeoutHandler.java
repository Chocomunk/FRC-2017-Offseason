//Still needs lot of improvement(it is very wrong I know) 
//But now we have actual stuff to work on

package com.palyrobotics.frc2017.vision;

public class TimeoutHandler implements TimeoutProcedureBase {
	//Different types of timeouts
	public static int numFailures = 0;
	
	public enum TimeoutType{
		LINEAR, DEFAULT;
	}
	TimeoutHandler defaultCase = new TimeoutHandler(); 
	//get is still really sus and I know it's wrong and needs lot of improvement :)
	public TimeoutProcedureBase get(String key, TimeoutType currTimeout){
		if (key == "nexus_connected"){
			TimeoutHandler nexus = new TimeoutHandler(); 
			return nexus; 
		}
		else if (key == null){
			return defaultCase;
		}
		else{
			return defaultCase;
		}
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
