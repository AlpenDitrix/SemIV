package ru.math.spbu.avapir.matrix_multiplication;

import java.awt.event.KeyEvent;
import java.util.Random;

public class BorderFinder {

	private static final int FIRST_ROUND = (int) Math.pow(10, 5);
	private static final double SECOND_ROUND = Math.pow(10, 4);

	static final Random r = new Random();
	private static final int RUNS = 10;
	static double[][] a, b;
	static Matrix A, B;
	static int N;

	public static void main(String[] a) {
		preburn();
		burn();
	}

	private static void burn() {
		for (int n = 60; n < 110; n +=1
				) {

			prepare(n);
			runFewTimes(n);
		}
	}

	private static void runFewTimes(int n) {
		String s = "";
		long timeParallel = 0;
		long timeSequential = 0;
		long start;
		
		for (int i = 0; i < RUNS; i++) {
			start = System.nanoTime();
			A.timesParallel(B);
			timeParallel += System.nanoTime() - start;
			
			if(A.getField()[0][0]==0){
				s = new 
						String();
			}

			start = System.nanoTime();
			A.timesSequential(B);
			timeSequential += System.nanoTime() - start;
			
			if(A.getField()[0][0]==0){
				s = new String();
			}
		}
		
//		System.out.println(timeSequential/RUNS);
//		System.out.println(timeParallel/RUNS);
		if(timeSequential<timeParallel){
//			System.out.println("seq");
			System.err.println(n);
		} else {
//			System.out.println("par");
			System.out.println(n);
		}
//		outTime(timeSequential/RUNS);
//		outTime(timeParallel/RUNS);
//		if (timeParallel > timeSequential) {
//			System.out.println("S " + n * n + " : " + outTime(timeSequential)
//					+ "<" + outTime(timeParallel));
//		} else {
//			System.err.println("P " + n * n + " : " + outTime(timeParallel)
//					+ "<" + outTime(timeSequential));
//		}
		System.out.println(s);
	}

	private static void prepare(int N) {
		a = new double[N][N];
		b = new double[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				a[i][j] = r.nextDouble();
				b[i][j] = r.nextDouble();
			}
			A = new Matrix(a);
			B = new Matrix(b);
		}
	}

	private static void preburn() {
		for (int i = 0; i < 10; i++) {
			prepare(100);
			A.timesParallel(B);
			A.timesSequential(B);
		}

	}

}
