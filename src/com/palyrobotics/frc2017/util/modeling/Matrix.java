package com.palyrobotics.frc2017.util.modeling;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Range;

/**
 * Wrapper class for {@link org.opencv.core.Mat}.
 * Supports operation chaining, more intuitive constructors and toString()
 *
 * Created by EricLiu on 11/30/17.
 */
public class Matrix extends Mat {

    /**
     * Construct a Matrix with the specified Mat.
     *
     * @param mat the Mat used to construct this Matrix
     */
    public Matrix(Mat mat) {
        super(mat, new Range(0, (int) (mat.size().height)), new Range(0, (int) (mat.size().width)));
    }

    /**
     * Construct a row or column Matrix using the values given in the array.
     * One and only one of rows or columns should have the value 1.
     *
     * @param rows the number of rows in this matrix
     * @param columns the number of columns in this matrix
     * @param values the set of values to be placed in this matrix
     */
    public Matrix(int rows, int columns, double[] values) {
        super(rows, columns, CvType.CV_64F);
        if(rows == 1) {
            for(int i = 0; i < values.length; i++) {
                this.put(0, i, values[i]);
            }
        } else if(columns == 1) {
            for(int i = 0; i < values.length; i++) {
                this.put(i, 0, values[i]);
            }
        } else {
            System.err.println("Invalid dimensions");
        }
    }

    /**
     * Construct a Matrix from the given 2-D array of doubles.
     *
     * values[] contains rows
     * values[][] contains column entries
     *
     * @param values
     */
    public Matrix(double[][] values) {
        super(values.length, values[0].length, CvType.CV_64F);
        for(int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                this.put(i, j, values[i][j]);
            }
        }
    }

    /**
     * Adds x to this Matrix.
     *
     * @param x the Matrix to be added
     * @return this + x
     */
    public Matrix add(Matrix x) {
        Mat output = new Mat((int) this.size().height, (int) this.size().width, CvType.CV_64F);
        Core.addWeighted(this, 1, x, 1, 0, output);
        return new Matrix(output);
    }

    /**
     * Subtracts x from this Matrix
     *
     * @param x the Matrix to be subtracted
     * @return this - x
     */
    public Matrix subtract(Matrix x) {
        Mat output = new Mat((int) this.size().height, (int) this.size().width, CvType.CV_64F);
        Core.addWeighted(this, 1, x, -1, 0, output);
        return new Matrix(output);
    }

    /**
     * Inverts this Matrix
     *
     * @return this^(-1)
     */
    public Matrix inverse() {
        return new Matrix(this.inv());
    }

    /**
     * Multiplies this Matrix by x
     *
     * @param x the Matrix to be multiplied
     * @return this * x
     */
    public Matrix multiply(Matrix x) {
        Mat output = new Mat((int) this.size().height, (int) x.size().width, CvType.CV_64F);
        Core.gemm(this, x, 1, Mat.zeros(output.size(), CvType.CV_64F), 0, output, 0);
        return new Matrix(output);
    }

    /**
     * Transposes this Matrix
     *
     * @return this^T
     */
    public Matrix transpose() {
        return new Matrix(this.t());
    }

    @Override
    public String toString() {
        String string = "{";
        for(int i = 0; i < this.rows(); i++) {
            string = string + "\n";
            for(int j = 0; j < this.cols(); j++) {
                string = string + this.get(i, j)[0] + " ";
            }
        }
        string = string + "\n" + "}";
        return string;
    }
}
