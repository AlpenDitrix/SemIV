package ru.math.spbu.avapir.matrix_multiplication.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Test;

import ru.math.spbu.avapir.matrix_multiplication.Matrix;

public class MatrixMultiplicationTest {

	public final int[] N = { 64, 256, 1024, 2048 };
	public double[][] a;
	public double[][] b;
	public double[][] result;
	int first = 0;
	int second = -1;

	private void fillWithRandomDbuble() {
		Random r = new Random();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				a[i][j] = r.nextDouble();
				b[j][i] = r.nextDouble();
			}
		}
	}

	@Test
	public void test() {
		long start = System.currentTimeMillis();
		while (first < N.length) {
			if (second < N.length - 1) {
				second++;
			} else {
				second = -1;
				first++;
				continue;
			}
			// System.out.println(N[first] + "x" + N[second] + " * " + N[second]
			// + "x" + N[first]);
			// System.out.println((first) * 4 + (second) + " " + N[first] + "x"
			// + N[second]);
			long time = System.currentTimeMillis();
			a = new double[N[first]][N[second]];
			b = new double[N[second]][N[first]];
			// fillWithInts();
			fillWithRandomDbuble();
			// fillDefined7();
			Matrix A = new Matrix(a);
			Matrix B = new Matrix(b);

			Matrix ResultSequential = A.timesSequential(B);
			System.out.println("Time elapsed: "
					+ (System.currentTimeMillis() - time) + "ms");
			time = System.currentTimeMillis();
			Matrix ResultParallel = A.timesParallel(B);
			System.out.print("Time elapsed: "
					+ (System.currentTimeMillis() - time) + "ms");
			// MatrixMultiplicator.printMatrix(Matrix_1);
			// System.out.println();
			// MatrixMultiplicator.printMatrix(Matrix_2);Õ
			try {
				assertEquals(ResultParallel, ResultSequential);
			} catch (AssertionError e) {
				System.out.println(A);
				System.out.println(B);
				System.out.println(ResultParallel);
				fail();
			}

			System.out.println();
		}

		System.out.println("\nTotal: " + (System.currentTimeMillis() - start));
	}

}
