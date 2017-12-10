package com.palyrobotics.frc2017.vision.procedures;

import com.palyrobotics.frc2017.vision.TimeoutHandler;

public class LogarithmicTimeout extends TimeoutHandler {
	public LogarithmicTimeout(double baseValue, double scalarValue){
		super(baseValue, scalarValue);
	}
}
