package com.palyrobotics.frc2017.util.modeling;

import com.palyrobotics.frc2017.config.Commands;
import com.palyrobotics.frc2017.config.Constants;
import com.palyrobotics.frc2017.robot.HardwareAdapter;
import com.palyrobotics.frc2017.robot.Robot;
import org.la4j.Matrix;

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
        Matrix X0 = Matrix.from1DArray(4, 1, new double[]{0, 0, 0, 1});
        Matrix F = Matrix.from2DArray(new double[][]{{1.0, timeStep, 0.5*Math.pow(timeStep, 2), 0}, {0, 1, timeStep, 0}, {0, 0, 0, 0}, {0, 0, 0, 1}});
        Matrix P0 = Matrix.from2DArray(new double[][]{{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}});
        //Guessed
        Matrix Q = Matrix.from2DArray(new double[][]{{0.9, 0, 0, 0}, {0, 0.9, 0, 0}, {0, 0, 0.9, 0}, {0, 0, 0, 1}});

        Matrix B = Matrix.from1DArray(4, 1, new double[]{0, 0, 1, 0});

        Matrix H = Matrix.from2DArray(new double[][]{{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, -1.12}, {0, 0, 0, 1}});

        //Guessed
        Matrix R = Matrix.from2DArray(new double[][]{{0.7, 0, 0}, {0, 0.7, 0}, {0, 0, 0.7}});

        filter = new KalmanFilter(X0, F, P0, Q, B, H, R);

        //Assumed 80% efficiency
        transmission = MotorSimulator.makeTransmission(MotorSimulator.makeCIM(), 6, 5, 0.8);

    }

    public void step(Commands commands) {

        //used to sketchily fill out z
        Matrix H = filter.getH();
        Matrix X = filter.getX();

        Matrix zFiller = H.multiply(X);

        Matrix z = Matrix.from1DArray(4, 1, new double[]{zFiller.get(0, 0), zFiller.get(1, 0), Robot.getRobotState().drivePose.forwardAccel, 1});

        transmission.step(-commands.leftStickInput.y, kRobotMass*kWheelRadius*kWheelRadius, 0, timeStep);


        filter.update(z, Matrix.from1DArray(1, 1, new double[] {transmission.getAcceleration()}));
    }
}
