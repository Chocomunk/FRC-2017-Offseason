package com.palyrobotics.frc2017.vision;

public abstract class TimeoutProcedureBase {
	public double baseValue; 
	public double scalarValue; 
	public TimeoutProcedureBase(double baseValue, double scalarValue){
		this.baseValue = baseValue; 
		this.scalarValue = scalarValue; 
	}
	public void success(){
		
	}
	public void failure(){
		
	}
	public boolean didSucceed(){
		return true; 
	}
	public double doWait(){
		return 0; 
	}
	public boolean succeed(){
		return true; 
	}
	public double getDuration(){
		return 0; 
	}
	protected double getBaseValue(){
		return 0; 
	}
	public static int numFailures; 
}
