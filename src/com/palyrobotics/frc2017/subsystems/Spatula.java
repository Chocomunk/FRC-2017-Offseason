package com.palyrobotics.frc2017.subsystems;

import com.palyrobotics.frc2017.config.Commands;
import com.palyrobotics.frc2017.config.RobotState;
import com.palyrobotics.frc2017.util.Subsystem;
import com.palyrobotics.frc2017.util.SubsystemLoop;

import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * STEIK SPATULA
 * @author Ailyn Tong
 * Represents a "spatula" that stores gears and allows for passive scoring
 * Controlled by one DoubleSolenoid which toggles between UP and DOWN
 */
public class Spatula extends Subsystem implements SubsystemLoop {
	private static Spatula instance = new Spatula();
	private SpatulaState mState;
	public static Spatula getInstance() {
		return instance;
	}
	
	public enum SpatulaState { UP, DOWN }
	
	private DoubleSolenoid.Value mOutput;
	
	private Spatula() {
		super("Spatula");
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public void update(Commands commands, RobotState robotState) {
		//TODO forward vs reverse
		switch (commands.wantedSpatulaState) {
		case UP:
			mOutput = DoubleSolenoid.Value.kForward;
			mState = SpatulaState.UP;
			break;
		case DOWN:
			mOutput = DoubleSolenoid.Value.kReverse;
			mState = SpatulaState.DOWN;
			break;
		}
	}
	
	public DoubleSolenoid.Value getOutput() {
		return mOutput;
	}
	
	public SpatulaState getState() {
		return mState;
	}
}
