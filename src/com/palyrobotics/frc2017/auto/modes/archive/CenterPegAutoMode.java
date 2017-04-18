package com.palyrobotics.frc2017.auto.modes.archive;

import com.palyrobotics.frc2017.auto.AutoModeBase;
import com.palyrobotics.frc2017.behavior.ParallelRoutine;
import com.palyrobotics.frc2017.behavior.Routine;
import com.palyrobotics.frc2017.behavior.SequentialRoutine;
import com.palyrobotics.frc2017.behavior.routines.TimeoutRoutine;
import com.palyrobotics.frc2017.behavior.routines.drive.CANTalonRoutine;
import com.palyrobotics.frc2017.behavior.routines.scoring.CustomPositioningSliderRoutine;
import com.palyrobotics.frc2017.config.AutoDistances;
import com.palyrobotics.frc2017.config.Constants;
import com.palyrobotics.frc2017.config.Gains;
import com.palyrobotics.frc2017.util.archive.DriveSignal;
import com.palyrobotics.frc2017.util.logger.Logger;

import java.util.ArrayList;

/**
 * Created by Nihar on 2/11/17.
 */
public class CenterPegAutoMode extends AutoModeBase {
	public enum Alliance {
		BLUE, RED
	}
	private final Alliance mAlliance;
	private SequentialRoutine mSequentialRoutine;
	private boolean mBackup = true;
	
	private Gains mShortGains, mLongGains;
	private double initialSliderPosition;	// distance from center in inches
	private final double backupDistance = 10;	// distance in inches
	private final double pilotWaitTime = 2;	// time in seconds

	public CenterPegAutoMode(Alliance alliance, boolean backup) {
		mAlliance = alliance;
		mShortGains = Gains.steikShortDriveMotionMagicGains;
		mLongGains = Gains.steikLongDriveMotionMagicGains;

		mBackup = backup;
	}

	@Override
	public Routine getRoutine() {
		return mSequentialRoutine;
	}

	@Override
	public void prestart() {
		String log = "Starting Center Peg Auto Mode";
		Logger.getInstance().logRobotThread("Starting Center Peg Auto Mode");
		// Construct sequence of routines to run
		ArrayList<Routine> sequence = new ArrayList<>();
		// Straight drive distance to the center peg
		DriveSignal driveForward = DriveSignal.getNeutralSignal();
		double driveForwardSetpoint =
				((mAlliance == Alliance.BLUE) ? AutoDistances.kBlueCenterPegDistanceInches : AutoDistances.kRedCenterPegDistanceInches)
						* Constants.kDriveTicksPerInch;
		// Aegir: right +30
		// Vali: left +100
		driveForward.leftMotor.setMotionMagic(driveForwardSetpoint, mLongGains,
			Gains.kSteikLongDriveMotionMagicCruiseVelocity, Gains.kSteikLongDriveMotionMagicMaxAcceleration);
		driveForward.rightMotor.setMotionMagic(driveForwardSetpoint, mLongGains,
				Gains.kSteikLongDriveMotionMagicCruiseVelocity, Gains.kSteikLongDriveMotionMagicMaxAcceleration);
		
		initialSliderPosition = (mAlliance == Alliance.BLUE) ? -2.5 : 0;
		// Drive forward while moving slider to initial position
		ArrayList<Routine> initialSlide = new ArrayList<>();
		initialSlide.add(new CustomPositioningSliderRoutine(initialSliderPosition));
		initialSlide.add(new CANTalonRoutine(driveForward, true));
		sequence.add(new ParallelRoutine(initialSlide));
		sequence.add(new TimeoutRoutine(pilotWaitTime));
		
		if (mBackup) {
			double backup = (mAlliance == Alliance.BLUE) ? 0 : 5;
			sequence.add(getBackup(backup));		// Move slider slightly to the left
			sequence.add(new TimeoutRoutine(pilotWaitTime));
		}

		mSequentialRoutine = new SequentialRoutine(sequence);
		System.out.println(log);
	}
	@Override
	public String toString() {
		String name = (mAlliance == Alliance.BLUE) ? "BlueCenterPeg" : "RedCenterPeg";
		return name;
	}
	/*
	 * GET BACKUP
	 */
	private SequentialRoutine getBackup(double sliderPosition) {
		DriveSignal driveBackup = DriveSignal.getNeutralSignal();
		DriveSignal driveReturn = DriveSignal.getNeutralSignal();

		double driveBackupSetpoint = -backupDistance * Constants.kDriveTicksPerInch;
		driveBackup.leftMotor.setMotionMagic(driveBackupSetpoint, mShortGains, 
				Gains.kSteikShortDriveMotionMagicCruiseVelocity, Gains.kSteikShortDriveMotionMagicMaxAcceleration);
		driveBackup.rightMotor.setMotionMagic(driveBackupSetpoint, mShortGains, 
				Gains.kSteikShortDriveMotionMagicCruiseVelocity, Gains.kSteikShortDriveMotionMagicMaxAcceleration);

		// drive forward same distance as backup
		driveReturn.leftMotor.setMotionMagic(-driveBackupSetpoint+3*Constants.kDriveTicksPerInch, mShortGains, 
				Gains.kSteikShortDriveMotionMagicCruiseVelocity, Gains.kSteikShortDriveMotionMagicMaxAcceleration);
		driveReturn.rightMotor.setMotionMagic(-driveBackupSetpoint+3*Constants.kDriveTicksPerInch, mShortGains, 
				Gains.kSteikShortDriveMotionMagicCruiseVelocity, Gains.kSteikShortDriveMotionMagicMaxAcceleration);
		
		// Create a routine that drives back, then moves the slider while moving back forward
		ArrayList<Routine> sequence = new ArrayList<>();
		ArrayList<Routine> parallelSliding = new ArrayList<>();
		parallelSliding.add(new CANTalonRoutine(driveBackup, true));
		ArrayList<Routine> slideSequence = new ArrayList<>();
		slideSequence.add(new TimeoutRoutine(0.5));
		slideSequence.add(new CustomPositioningSliderRoutine(sliderPosition));
		parallelSliding.add(new SequentialRoutine(slideSequence));
		sequence.add(new ParallelRoutine(parallelSliding));
		sequence.add(new CANTalonRoutine(driveReturn, true));
		sequence.add(new TimeoutRoutine(pilotWaitTime));
		
		return new SequentialRoutine(sequence);
	}
}