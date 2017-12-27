package com.palyrobotics.frc2017.vision;

public abstract class TimeoutProcedureBase {
	public double baseValue; 
	public double scalarValue; 
	public TimeoutProcedureBase(double baseValue, double scalarValue){
		this.baseValue = baseValue; 
		this.scalarValue = scalarValue; 
	}
	
	/**
	 * Resets numFailures to 0
	 */
	public void success(){
		numFailures = 0;
	}
	
	/**
	 * Adds a failure to numFailures
	 */
	public void failure(){
		numFailures++;
		doWait();
	}
	
	/**
	 * Returns duration of timeout
	 * @param failureCount
	 * @return Length of time to wait
	 */
	public abstract int getDuration(int failureCount);
	
	/**
	 * Sleeps program based on procedure type and failure count
	 */
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
