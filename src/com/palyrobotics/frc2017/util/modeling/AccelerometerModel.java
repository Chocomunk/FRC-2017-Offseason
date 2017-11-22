package com.palyrobotics.frc2017.util.modeling;

import com.palyrobotics.frc2017.config.Constants;
import com.palyrobotics.frc2017.robot.HardwareAdapter;
import org.la4j.Matrix;

/**
 * Created by EricLiu on 11/21/17.
 */
public class AccelerometerModel {
    private KalmanFilter filter;

    private double timeStep = Constants.kNormalLoopsDt;

    public AccelerometerModel() {
        Matrix X0 = Matrix.from1DArray(4, 1, new double[]{0, 0, 0, 1});
        Matrix F = Matrix.from2DArray(new double[][]{{1.0, timeStep, 0.5*Math.pow(timeStep, 2), 0}, {0, 1, timeStep, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}});
        Matrix P0 = Matrix.from2DArray(new double[][]{{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}});
        //Guessed
        Matrix Q = Matrix.from2DArray(new double[][]{{0.9, 0, 0, 0}, {0, 0.9, 0, 0}, {0, 0, 0.9, 0}, {0, 0, 0, 1}});

        Matrix B = Matrix.from1DArray(3, 1, new double[]{0.5*Math.pow(timeStep, 2), timeStep, 1.0});

        //Starts at 1, changes later in order to keep adding a constant to measured accel
        //INCOMPLETE - MEASURE STUFF
        Matrix H = Matrix.from2DArray(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});

        //Guessed
        Matrix R = Matrix.from2DArray(new double[][]{{0.7, 0, 0}, {0, 0.7, 0}, {0, 0, 0.7}});

        filter = new KalmanFilter(X0, F, P0, Q, B, H, R);
    }

    public void step() {
        filter.update();
        //STILL NEED TO GET PREDICTED ACCEL FROM THROTTLE FROM MOTOR SIMULATOR
    }
}
