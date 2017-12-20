package com.palyrobotics.frc2017.vision;

public abstract class TimeoutProcedureBase {
	public double baseValue; 
	public double scalarValue; 
	public TimeoutProcedureBase(double baseValue, double scalarValue){
		this.baseValue = baseValue; 
		this.scalarValue = scalarValue; 
	}
	public void success(){
		numFailures = 0;
	}
	public void failure(){
		numFailures++;
	}
	
	private int getDuration(int failureCount) { // TODO: change multiplier constant based on type of procedure
		return failureCount * 100;
	}
	public void doWait(){
		try {
			Thread.sleep(getDuration(numFailures));
			System.out.println(getDuration(numFailures));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	protected double getBaseValue(){
		return baseValue; 
	}
	public static int numFailures; 
}
