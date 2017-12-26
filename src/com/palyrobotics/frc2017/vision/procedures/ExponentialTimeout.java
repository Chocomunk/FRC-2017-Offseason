package com.palyrobotics.frc2017.vision.procedures;
import com.palyrobotics.frc2017.vision.TimeoutHandler;

public class ExponentialTimeout extends TimeoutHandler {
	public ExponentialTimeout(double baseValue, double scalarValue){
		super(baseValue, scalarValue); 
	}
	
	public int getDuration(int failureCount) { // call in TimeoutHandler I think
		return (int)Math.pow(2, failureCount) * 100; // example I think
	}
}
