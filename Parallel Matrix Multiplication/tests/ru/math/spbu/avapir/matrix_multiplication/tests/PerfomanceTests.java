package ru.math.spbu.avapir.matrix_multiplication.tests;

import java.util.Random;

import ru.math.spbu.avapir.matrix_multiplication.Matrix;

public class PerfomanceTests {

	static Random r = new Random();

	private static final int RUN = 10;
	static int PRERUN_COUNT = 2;

	static int[] N = { 100, 300, 500, 1000, 1500, 2000, 3000, 50, 150, 250,
			500, 750, 1000, 1500, 200, 600, 1000, 2000, 3000, 4000, 6000 };
	static int[] M = { 100, 300, 500, 1000, 1500, 2000, 3000, 200, 600, 1000,
			2000, 3000, 4000, 6000, 50, 150, 250, 500, 750, 1000, 1500 };

	static Matrix A;
	static Matrix B;
	static double[][] a;
	static double[][] b;

	static int currentRun;

	/**
	 * DNIWE EBANOE
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		for (int iterator = 0; iterator < N.length; iterator++) {
			currentRun = iterator;
			prerunSeq();
			runSeq();
			prerunPar();
			runPar();
		}
		// System.out.println(Matrix.nanoTime/(RUN+PRERUN_COUNT));
	}

	private static void runSeq() {
		long totalTime = 0;
		for (int i = 0; i < RUN; i++) {
			prepare();
			long start = System.nanoTime();
			A.timesSequential(B);
			totalTime += System.nanoTime() - start;
			// System.out.println(System.currentTimeMillis() - start);
		}
		System.out.println("Sequential-time for [" + N[currentRun] + "x"
				+ M[currentRun] + "] * [" + M[currentRun] + "x" + N[currentRun]
				+ "] is " + totalTime / RUN);
	}

	private static void prerunSeq() {
		for (int i = 0; i < PRERUN_COUNT; i++) {
			prepare();
			// long time = System.currentTimeMillis();
			A.timesSequential(B);
			// System.out.println(":" + (System.currentTimeMillis() - time));
		}
	}

	private static void runPar() {
		long totalTime = 0;
		for (int i = 0; i < RUN; i++) {
			prepare();
			long start = System.nanoTime();
			A.timesParallel(B);
			totalTime += System.nanoTime() - start;
			// System.out.println(System.currentTimeMillis() - start);
		}
		System.out.println("Parallel-time for [" + N[currentRun] + "x"
				+ M[currentRun] + "] * [" + M[currentRun] + "x" + N[currentRun]
				+ "] is " + totalTime / RUN);
	}

	private static void prerunPar() {
		for (int i = 0; i < PRERUN_COUNT; i++) {
			prepare();
			A.timesParallel(B);
			// System.out.println(PRERUN_COUNT - i);
		}
	}

	private static void prepare() {
		a = new double[N[currentRun]][M[currentRun]];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				a[i][j] = r.nextDouble();
			}
		}
		b = new double[M[currentRun]][N[currentRun]];
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				b[i][j] = r.nextDouble();
			}
		}
		A = new Matrix(a);
		B = new Matrix(b);
	}

}
