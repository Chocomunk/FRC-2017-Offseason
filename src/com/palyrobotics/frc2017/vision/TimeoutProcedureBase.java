package com.palyrobotics.frc2017.vision;

public abstract class TimeoutProcedureBase {
	public double baseValue; 
	public double scalarValue; 
	public double duration;
	public TimeoutProcedureBase(double baseValue, double scalarValue, double duration){
		this.baseValue = baseValue; 
		this.scalarValue = scalarValue; 
		this.duration = duration;
	}
	public void success(){
		numFailures = 0;
	}
	public void failure(){
		numFailures++;
	}
	
	private int getFailureTime(int failureCount) { // TODO: change multiplier constant based on procedure
		return failureCount * 100;
	}
	public void doWait(){
		try {
			Thread.sleep(getFailureTime(numFailures));
			System.out.println(getFailureTime(numFailures));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public double getDuration(){
		return duration; 
	}
	protected double getBaseValue(){
		return baseValue; 
	}
	public static int numFailures; 
}
