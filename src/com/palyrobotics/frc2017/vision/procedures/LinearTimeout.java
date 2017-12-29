package com.palyrobotics.frc2017.vision.procedures;

import com.palyrobotics.frc2017.vision.TimeoutHandler;

public class LinearTimeout extends TimeoutHandler {
	public LinearTimeout(double baseValue, double scalarValue){
		super(baseValue, scalarValue); 
	}
//	public int getDuration(int failureCount) { 
//		return failureCount * 100;
//	}
}
