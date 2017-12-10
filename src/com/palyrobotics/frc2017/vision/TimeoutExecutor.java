package com.palyrobotics.frc2017.vision;



public class TimeoutExecutor extends TimeoutHandler {
public TimeoutExecutor(double baseValue, double scalarValue){
	super(baseValue, scalarValue); 
}
public static void main(String[]args){
	TimeoutProcedureBase proc = TimeoutHandler.get("nexus_connected", TimeoutHandler.TimeoutType.LINEAR);
		while(((TimeoutHandler) proc).getFailureTime(numFailures)  < 1000){
			try {
				assert 1 + 1 == 3;
				proc.success();	// Clear failure count and move on
			} catch (AssertionError e) {
				proc.failure();	// Increment failure count and then do a wait
			}
		
		}
	
	}

}

