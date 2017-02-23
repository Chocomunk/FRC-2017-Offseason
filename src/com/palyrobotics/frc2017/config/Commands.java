package com.palyrobotics.frc2017.config;

import java.util.ArrayList;
import java.util.Optional;

import com.palyrobotics.frc2017.behavior.Routine;
import com.palyrobotics.frc2017.util.archive.DriveSignal;
import com.palyrobotics.frc2017.subsystems.SimpleClimber;
import com.palyrobotics.frc2017.subsystems.Climber;
import com.palyrobotics.frc2017.subsystems.Drive;
import com.palyrobotics.frc2017.subsystems.Flippers;
import com.palyrobotics.frc2017.subsystems.Intake;
import com.palyrobotics.frc2017.subsystems.SimpleSlider;
import com.palyrobotics.frc2017.subsystems.Slider;
import com.palyrobotics.frc2017.subsystems.Spatula;

import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * Commands represent the desired setpoints and subsystem states for the robot. <br />
 * Store Requests (enum) for each subsystem and setpoints {@link Setpoints} <br />
 * Directly stores real or mock Joystick input in {@link JoystickInput} <br />
 * Variables are public and have default values to prevent NullPointerExceptions
 * @author Nihar
 *
 */
public class Commands {
	public ArrayList<Routine> wantedRoutines = new ArrayList<Routine>();

	// Store WantedStates for each subsystem state machine
	public Drive.DriveState wantedDriveState = Drive.DriveState.NEUTRAL;
	public Flippers.FlipperSignal wantedFlipperSignal = new Flippers.FlipperSignal(
			DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kForward);
	public Slider.SliderState wantedSliderState = Slider.SliderState.IDLE;
	public SimpleSlider.SimpleSliderState wantedSimpleSliderState = SimpleSlider.SimpleSliderState.IDLE;
	public Spatula.SpatulaState wantedSpatulaState = Spatula.SpatulaState.UP;
	public Intake.IntakeState wantedIntakeState = Intake.IntakeState.IDLE;
	public Climber.ClimberState wantedClimberState = Climber.ClimberState.IDLE;
	public SimpleClimber.ClimberState wantedSimpleClimberState = SimpleClimber.ClimberState.IDLE;

	public void addWantedRoutine(Routine wantedRoutine) {
		for(Routine routine : wantedRoutines) {
			if(routine.getClass().equals(wantedRoutine.getClass())) {
				System.out.println("tried to add duplicate routine!");
				return;
			}
		}
		wantedRoutines.add(wantedRoutine);
	}
	
	/**
	 * Stores numeric setpoints
	 * @author Nihar
	 */
	public static class Setpoints {
		public static final Optional<Double> NULLOPT = Optional.empty();
		
		public Optional<DriveSignal> drivePowerSetpoint = Optional.empty();
		public Slider.SliderTarget sliderSetpoint = Slider.SliderTarget.NONE;
		public Optional<Double> sliderPowerSetpoint = Optional.empty();

		/**
		 * Resets all the setpoints
		 */
		public void reset() {
			drivePowerSetpoint = Optional.empty();
			sliderSetpoint = Slider.SliderTarget.NONE;
		}
	}
	// All robot setpoints
	public Setpoints robotSetpoints = new Setpoints();
	
	/**
	 * Class to store Joystick input
	 * @author Nihar
	 */
	public static class JoystickInput {
		public static class XboxInput extends JoystickInput {
			public double leftX, leftY, rightX, rightY;
			public XboxInput(double leftX, double leftY, double rightX, double rightY) {
				super(leftX, leftY, false);
				this.leftX = leftX;
				this.leftY = leftY;
				this.rightX = rightX;
				this.rightY = rightY;
			}
		}
		public double x,y;
		public boolean triggerPressed;
		public JoystickInput(double x, double y, boolean triggerPressed) {
			this.x = x; this.y = y; this.triggerPressed = triggerPressed;
		}
	}
	// Stores Joystick values
	public JoystickInput leftStickInput = new JoystickInput(0,0, false);
	public JoystickInput rightStickInput = new JoystickInput(0,0, false);
	public JoystickInput operatorStickInput = new JoystickInput(0, 0, false);

	// Allows you to cancel all running routines
	public boolean cancelCurrentRoutines = false;

	/**
	 *
	 * @return a copy of these commands
	 */
	public Commands copy() {
		Commands copy = new Commands();
		copy.wantedDriveState = this.wantedDriveState;
		copy.wantedFlipperSignal = this.wantedFlipperSignal;
		copy.wantedSpatulaState = this.wantedSpatulaState;
		copy.wantedIntakeState = this.wantedIntakeState;
		copy.wantedClimberState = this.wantedClimberState;

		copy.cancelCurrentRoutines = this.cancelCurrentRoutines;
		copy.leftStickInput = this.leftStickInput;
		copy.rightStickInput = this.rightStickInput;
		copy.operatorStickInput = this.operatorStickInput;

		// Copy robot setpoints
		copy.robotSetpoints = new Setpoints();
		// Copy optionals that are present
		robotSetpoints.drivePowerSetpoint.ifPresent((DriveSignal signal) -> copy.robotSetpoints.drivePowerSetpoint = Optional.of(signal));
		return copy;
	}
}