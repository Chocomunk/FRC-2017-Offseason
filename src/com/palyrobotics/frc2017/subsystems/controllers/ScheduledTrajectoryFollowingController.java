package com.palyrobotics.frc2017.subsystems.controllers;

import java.util.logging.Level;

import com.palyrobotics.frc2017.config.Constants;
import com.palyrobotics.frc2017.config.Gains;
import com.palyrobotics.frc2017.config.RobotState;
import com.palyrobotics.frc2017.config.dashboard.DashboardManager;
import com.palyrobotics.frc2017.robot.team254.lib.util.ChezyMath;
import com.palyrobotics.frc2017.robot.team254.lib.util.SynchronousPID;
import com.palyrobotics.frc2017.robot.team254.lib.util.interp.InterpolatingDouble;
import com.palyrobotics.frc2017.robot.team254.lib.util.interp.InterpolatingGains;
import com.palyrobotics.frc2017.robot.team254.lib.util.interp.InterpolatingTreeMap;
import com.palyrobotics.frc2017.subsystems.Drive;
import com.palyrobotics.frc2017.util.Pose;
import com.palyrobotics.frc2017.util.archive.DriveSignal;
import com.palyrobotics.frc2017.util.archive.team254.trajectory.LegacyScheduleTrajectoryFollower;
import com.palyrobotics.frc2017.util.archive.team254.trajectory.LegacyTrajectoryFollower;
import com.palyrobotics.frc2017.util.logger.Logger;
import com.team254.lib.trajectory.Path;

/**
 * Created by Robbie.
 */
public class ScheduledTrajectoryFollowingController implements Drive.DriveController {
	private LegacyScheduleTrajectoryFollower mLeftFollower = new LegacyScheduleTrajectoryFollower("left");
	private LegacyScheduleTrajectoryFollower mRightFollower = new LegacyScheduleTrajectoryFollower("right");

	private boolean mGyroCorrection;
	private boolean mIllegalPath;

	private SynchronousPID headingPID;
	
	private InterpolatingGains gains;

	public ScheduledTrajectoryFollowingController(Path path, InterpolatingGains gains, boolean correctUsingGyro, boolean inverted) {
		this.gains = gains;
		headingPID = new SynchronousPID(gains.turnP, 0, gains.turnD);
		headingPID.setOutputRange(-0.15, 0.15);
		headingPID.setSetpoint(0);

		// set trajectory gains
		mLeftFollower.configure(gains.getP(new InterpolatingDouble(0.0)), 0, gains.getD(new InterpolatingDouble(0.0)),
				gains.getV(new InterpolatingDouble(0.0)), gains.getA(new InterpolatingDouble(0.0)));
		mRightFollower.configure(gains.getP(new InterpolatingDouble(0.0)), 0, gains.getD(new InterpolatingDouble(0.0)),
				gains.getV(new InterpolatingDouble(0.0)), gains.getA(new InterpolatingDouble(0.0)));

		// set goals and paths
		if (path == null) {
			mIllegalPath = true;
			Logger.getInstance().logSubsystemThread(Level.SEVERE, "No path!");
			return;
		} else {
			mIllegalPath = false;
		}
		if (inverted) {
			path.getRightWheelTrajectory().setInvertedY(inverted);
			path.getLeftWheelTrajectory().setInvertedY(inverted);
		}
		mRightFollower.setTrajectory(path.getRightWheelTrajectory());
		mLeftFollower.setTrajectory(path.getLeftWheelTrajectory());
		
		mGyroCorrection = correctUsingGyro;
	}

	@Override
	public DriveSignal update(RobotState state) {
		if (mIllegalPath) {
			return DriveSignal.getNeutralSignal();
		}
		if (onTarget()) {
			return DriveSignal.getNeutralSignal();
		}
		DriveSignal driveSignal = DriveSignal.getNeutralSignal();
		
		InterpolatingDouble leftCSpeed = new InterpolatingDouble(mLeftFollower.getSpeed());
		InterpolatingDouble rightCSpeed = new InterpolatingDouble(mRightFollower.getSpeed());

		mLeftFollower.configure(gains.getP(leftCSpeed), 0, gains.getD(leftCSpeed),
				gains.getV(leftCSpeed), gains.getA(leftCSpeed));
		mRightFollower.configure(gains.getP(rightCSpeed), 0, gains.getD(rightCSpeed),
				gains.getV(rightCSpeed), gains.getA(rightCSpeed));
		
		double leftPower = mLeftFollower.calculate(state.drivePose.leftEnc/Constants.kDriveTicksPerInch/12);
		double rightPower = mRightFollower.calculate(state.drivePose.rightEnc/Constants.kDriveTicksPerInch/12);
		
		double gyroError = 0;

		if (!mGyroCorrection) {
			driveSignal.leftMotor.setPercentVBus(leftPower);
			driveSignal.rightMotor.setPercentVBus(rightPower);
		} else {
			gyroError = ChezyMath.getDifferenceInAngleRadians(Math.toRadians(state.drivePose.heading), mLeftFollower.getHeading());
			gyroError = Math.toDegrees(gyroError);
			double gyroCorrection = headingPID.calculate(gyroError);
			Logger.getInstance().logSubsystemThread(Level.FINEST, "Gyro correction", gyroCorrection);
			driveSignal.leftMotor.setVoltage((leftPower+gyroCorrection)*12);
			driveSignal.rightMotor.setVoltage((rightPower-gyroCorrection)*12);
		}
		
		DashboardManager.getInstance().updateCANTable(mLeftFollower.getCanTableString() + ", " + mRightFollower.getCanTableString() + ", " + gyroError);
		
		return driveSignal;
		
		
	}

	@Override
	public Pose getSetpoint() {
		// TODO: what to return?
		return new Pose(0,0,0,0,0,0,0,0);
	}

	@Override
	public boolean onTarget() {
		return !mIllegalPath | mLeftFollower.isFinishedTrajectory() && mRightFollower.isFinishedTrajectory();
	}
}
