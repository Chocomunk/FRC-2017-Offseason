package com.palyrobotics.frc2017.util.modeling;

import com.palyrobotics.frc2017.config.Commands;
import com.palyrobotics.frc2017.config.Constants;
import com.palyrobotics.frc2017.robot.HardwareAdapter;
import com.palyrobotics.frc2017.robot.Robot;
import org.la4j.Matrix;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Size;

import static com.palyrobotics.frc2017.config.Constants.kRobotMass;
import static com.palyrobotics.frc2017.config.Constants.kWheelRadius;

/**
 * Created by EricLiu on 11/21/17.
 */
public class AccelerometerModel {
//    private KalmanFilter filter;

    private MotorSimulator transmission;

    private double timeStep = Constants.kNormalLoopsDt;

    public AccelerometerModel() {
        MatOfDouble X0 = new MatOfDouble();
        X0.create(4, 1, CvType.CV_32F);
        X0.put(0, 0, 0);
        X0.put(1, 0, 0);
        X0.put(2, 0, 0);
        X0.put(3, 0, 1);

        MatOfDouble F = new MatOfDouble();
        F.create(4, 4, CvType.CV_32F);
        F.put(0, 0, 1.0);
        F.put(0, 1, timeStep);
        F.put(0, 2, 0.5*Math.pow(timeStep, 2));
        F.put(0, 3, 0);

        F.put(1, 0, 0);
        F.put(1, 1, 1);
        F.put(1, 2, timeStep);
        F.put(1, 3, 0);

        F.put(2, 0, 0);
        F.put(2, 1, 0);
        F.put(2, 2, 0);
        F.put(2, 3, 0);

        F.put(3, 0, 0);
        F.put(3, 1, 0);
        F.put(3, 2, 0);
        F.put(3, 3, 1);

        MatOfDouble P0 = new MatOfDouble();
        P0.create(4, 4, CvType.CV_32F);
        P0.put(0, 0, 1.0);
        P0.put(0, 1, 0);
        P0.put(0, 2, 0);
        P0.put(0, 3, 0);

        P0.put(1, 0, 0);
        P0.put(1, 1, 1);
        P0.put(1, 2, 0);
        P0.put(1, 3, 0);

        P0.put(2, 0, 0);
        P0.put(2, 1, 0);
        P0.put(2, 2, 1);
        P0.put(2, 3, 0);

        P0.put(3, 0, 0);
        P0.put(3, 1, 0);
        P0.put(3, 2, 0);
        P0.put(3, 3, 1);

        MatOfDouble Q = new MatOfDouble();
        Q.create(4, 4, CvType.CV_32F);
        Q.put(0, 0, 0);
        Q.put(0, 1, 0);
        Q.put(0, 2, 0);
        Q.put(0, 3, 0);

        Q.put(1, 0, 0);
        Q.put(1, 1, 0);
        Q.put(1, 2, 0);
        Q.put(1, 3, 0);

        Q.put(2, 0, 0);
        Q.put(2, 1, 0);
        Q.put(2, 2, 0);
        Q.put(2, 3, 0);

        Q.put(3, 0, 0);
        Q.put(3, 1, 0);
        Q.put(3, 2, 0);
        Q.put(3, 3, 0);

        MatOfDouble B = new MatOfDouble();
        B.create(4, 1, CvType.CV_32F);
        B.put(0, 0, 0);
        B.put(1, 0, 0);
        B.put(2, 0, 1);
        B.put(3, 0, 0);

        MatOfDouble H = new MatOfDouble();
        H.create(4, 4, CvType.CV_32F);
        H.put(0, 0, 1);
        H.put(0, 1, 0);
        H.put(0, 2, 0);
        H.put(0, 3, 0);

        H.put(1, 0, 0);
        H.put(1, 1, 1);
        H.put(1, 2, 0);
        H.put(1, 3, 0);

        H.put(2, 0, 0);
        H.put(2, 1, 0);
        H.put(2, 2, 1);
        H.put(2, 3, -1.12);

        H.put(3, 0, 0);
        H.put(3, 1, 0);
        H.put(3, 2, 0);
        H.put(3, 3, 1);

        MatOfDouble R = new MatOfDouble();
        R.create(3, 3, CvType.CV_32F);
        R.put(0, 0, 0);
        R.put(0, 1, 0);
        R.put(0, 2, 0);
        R.put(0, 3, 0);

        R.put(1, 0, 0);
        R.put(1, 1, 1);
        R.put(1, 2, 0);
        R.put(1, 3, 0);

        R.put(2, 0, 0);
        R.put(2, 1, 0);
        R.put(2, 2, 0);
        R.put(2, 3, 0);

        R.put(3, 0, 0);
        R.put(3, 1, 0);
        R.put(3, 2, 0);
        R.put(3, 3, 0);

//        filter = new KalmanFilter(X0, F, P0, Q, B, H, R);

        //Assumed 80% efficiency
        transmission = MotorSimulator.makeTransmission(MotorSimulator.makeCIM(), 6, 5, 0.8);

    }

    public void step(Commands commands) {

        //used to sketchily fill out z
//        Mat H = filter.getH();
//        Mat X = filter.getX();

//        Matrix zFiller = H.multiply(X);

//        Matrix z = Matrix.from1DArray(4, 1, new double[]{zFiller.get(0, 0), zFiller.get(1, 0), Robot.getRobotState().drivePose.forwardAccel, 1});

        transmission.step(-commands.leftStickInput.y, kRobotMass*kWheelRadius*kWheelRadius, 0, timeStep);


//        filter.update(z, Matrix.from1DArray(1, 1, new double[] {transmission.getAcceleration()}));
    }
}
