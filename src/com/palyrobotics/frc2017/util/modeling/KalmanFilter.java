package com.palyrobotics.frc2017.util.modeling;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;

/**
 * Created by EricLiu on 11/20/17.
 */
public class KalmanFilter {

    public enum INFO {SENSOR, INPUT}
    /**
     * Modeling Matrices
     */

    //Modeled State
    private Matrix X;

    //Previous State -> Current State transformation matrix
    private Matrix F;

    //Modeled State Covariance
    private Matrix P;
    //Covariance due to unknown causes
    private Matrix Q;

    //Control matrix used in conjunction with input for the model
    private Matrix B;

    /**
     * Sensor Matrices
     */

    //Actual State -> Sensor Reading State transformation matrix
    private Matrix H;

    //Covariance of sensor reading
    private Matrix R;

    /**
     *
     * @param X0 Initial modeled state
     * @param F Previous modeled state -> Current modeled state transformation matrix
     * @param P0 Initial covariance of modeled state
     * @param Q Covariance due to unknown causes
     * @param B Control matrix used in conjunction with input for the model
     * @param H Actual State -> Sensor Reading State transformation matrix
     * @param R Covariance of sensor reading
     */
    public KalmanFilter(Matrix X0, Matrix F, Matrix P0, Matrix Q, Matrix B, Matrix H, Matrix R) {
        this.X = X0;
        this.P = P0;
        this.F = F;
        this.B = B;
        this.H = H;
        this.R = R;
        this.Q = Q;
    }

    /**
     * Update given only starting conditions
     *
     * @return {State, Covariance of State}
     */
    public Matrix[] update() {

        X = F.multiply(X);
        P = F.multiply(P).multiply(F.transpose()).add(Q);

        return new Matrix[] {X, P};
    }

    /**
     * Update given starting conditions and either sensor readings or input
     *
     * @param x
     * @param type
     * @return {State, Covariance of State}
     */
    public Matrix[] update(Matrix x, INFO type) {
        Matrix finalX;
        Matrix finalP;

        switch (type) {
            case SENSOR:
                X = F.multiply(X);
                P = F.multiply(P).multiply(F.transpose()).add(Q);


                Matrix K = P.multiply(H.transpose()).multiply((R.add(H.multiply(P).multiply(H.transpose()))).inverse());

                finalX = X.add(K.multiply(x.subtract(H.multiply(X))));
                finalP = P.subtract(K.multiply(H).multiply(P));

                break;
            case INPUT:
                X = F.multiply(X).add(B.multiply(x));
                P = F.multiply(P).multiply(F.transpose()).add(Q);

                finalX = X;
                finalP = P;

                break;
            default:
                return null;
        }

        return new Matrix[] {finalX, finalP};
    }

    /**
     * Update given starting conditions, sensor readings, and input
     *
     * @param z sensor readings
     * @param u input
     * @return {State, Covariance of State}
     */
    public Matrix[] update(Matrix z, Matrix u) {
        X = F.multiply(X).add(B.multiply(u));
        P = F.multiply(P).multiply(F.transpose()).add(Q);

        Matrix K = P.multiply(H.transpose()).multiply((R.add(H.multiply(P).multiply(H.transpose()))).inverse());

        Matrix finalX = X.add(K.multiply(z.subtract(H.multiply(X))));
        Matrix finalP = P.subtract(K.multiply(H).multiply(P));

        return new Matrix[] {finalX, finalP};
    }

    public Matrix getX() {
        return this.X;
    }

    public Matrix getP() {
        return this.P;
    }

    public Matrix getF() {
        return this.F;
    }

    public Matrix getQ() {
        return this.Q;
    }

    public Matrix getB() {
        return this.B;
    }

    public Matrix getH() {
        return this.H;
    }

    public Matrix getR() {
        return this.R;
    }
}