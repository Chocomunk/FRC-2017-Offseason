package com.palyrobotics.frc2017.vision.procedures;
import com.palyrobotics.frc2017.vision.TimeoutHandler;

public class ExponentialTimeout extends TimeoutHandler {
	public ExponentialTimeout(double baseValue, double scalarValue){
		super(baseValue, scalarValue); 
	}
}
