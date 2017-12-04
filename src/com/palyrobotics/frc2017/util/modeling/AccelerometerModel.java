package com.palyrobotics.frc2017.util.modeling;

import com.palyrobotics.frc2017.config.Commands;
import com.palyrobotics.frc2017.config.Constants;
import com.palyrobotics.frc2017.robot.Robot;

import static com.palyrobotics.frc2017.config.Constants.kRobotMass;
import static com.palyrobotics.frc2017.config.Constants.kWheelRadius;

/**
 * Created by EricLiu on 11/21/17.
 */
public class AccelerometerModel {
    private KalmanFilter filter;

    private MotorSimulator transmission;

    private double timeStep = Constants.kNormalLoopsDt;

    public AccelerometerModel() {
        double qf = Math.pow(0.05, 3);

        Matrix X0 = new Matrix(4, 1, new double[]{0, 0, 0, 1});

        Matrix F = new Matrix(new double[][]{{1, timeStep, 0.5*Math.pow(timeStep, 2), 0}, {0, 1, timeStep, 0}, {0, 0, 0, 0}, {0, 0, 0, 1}});

        Matrix P0 =  new Matrix(new double[][]{{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}});

        Matrix Q = new Matrix(new double[][]{{Math.pow(timeStep, 5)*qf/5.0, Math.pow(timeStep, 4)*qf/4.0 ,Math.pow(timeStep, 3)*qf/3.0 , 0}, {Math.pow(timeStep, 4)*qf/4.0, Math.pow(timeStep, 3)*qf/3.0 ,Math.pow(timeStep, 2)*qf/2.0 , 0}, {Math.pow(timeStep, 3)*qf/3.0,Math.pow(timeStep, 2)*qf/2.0 ,timeStep*qf , 0}, {0, 0, 0, 0}});

        Matrix B = new Matrix(4, 1, new double[]{0, 0, 1, 0});

        Matrix H = new Matrix(new double[][]{{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, -1.09}, {0, 0, 0, 1}});

        Matrix R = new Matrix(new double[][]{{0.01372820103, 0, 0, 0}, {0, 0.01372820103, 0, 0}, {0, 0, 0.01372820103, 0}, {0, 0, 0, 0}});

        filter = new KalmanFilter(X0, F, P0, Q, B, H, R);

        //Assumed 80% efficiency
        transmission = MotorSimulator.makeTransmission(MotorSimulator.makeCIM(), 6, 5, 0.8);

    }

    public void step(Commands commands) {

        //used to sketchily fill out z
        Matrix H = filter.getH();
        Matrix X = filter.getX();

        Matrix zFiller = H.multiply(X);

//        Matrix z = new Matrix(4, 1, new double[]{Robot.getRobotState().drivePose.leftEnc/(12.0*Constants.kDriveSpeedUnitConversion), Robot.getRobotState().drivePose.leftSpeed/(12.0*Constants.kDriveSpeedUnitConversion), Robot.getRobotState().drivePose.forwardAccel, 1});

        Matrix z = new Matrix(4, 1, new double[]{zFiller.get(0, 0)[0], zFiller.get(1, 0)[0], Robot.getRobotState().drivePose.forwardAccel, 1});

        transmission.step(-commands.leftStickInput.y * 12.0, kRobotMass*kWheelRadius*kWheelRadius, 0, timeStep);

        filter.update(z, new Matrix(1, 1, new double[]{transmission.getAcceleration()}));
    }

    public double getPos() {
        return filter.getX().get(0, 0)[0];
    }

    public double getVel() {
        return filter.getX().get(1, 0)[0];
    }

    public double getAccel() {
        return filter.getX().get(2, 0)[0];
    }
}
