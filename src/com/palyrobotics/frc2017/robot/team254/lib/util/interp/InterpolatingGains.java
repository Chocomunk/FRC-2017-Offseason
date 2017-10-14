package com.palyrobotics.frc2017.robot.team254.lib.util.interp;

public class InterpolatingGains {
	
	private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> p;
	private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> i;
	private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> d;
	private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> f;
	private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> v;
	private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> a;
	
	public double turnP;
	public double turnD;
	
	public InterpolatingGains() {
		
	}
	
	public double getP(InterpolatingDouble velocity) {
		return p.getInterpolated(velocity).value;
	}
	
	public double getI(InterpolatingDouble velocity) {
		return i.getInterpolated(velocity).value;
	}
	
	public double getD(InterpolatingDouble velocity) {
		return d.getInterpolated(velocity).value;
	}
	
	public double getF(InterpolatingDouble velocity) {
		return f.getInterpolated(velocity).value;
	}
	
	public double getV(InterpolatingDouble velocity) {
		return v.getInterpolated(velocity).value;
	}
	
	public double getA(InterpolatingDouble velocity) {
		return a.getInterpolated(velocity).value;
	}
	
	
	
}
