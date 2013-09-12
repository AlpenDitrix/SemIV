package ru.math.spbu.avapir.matrix_multiplication.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import ru.math.spbu.avapir.matrix_multiplication.Matrix;

public class E_MatrixTest {

	public double[][] a;
	public Matrix A;
	public Matrix B;
	public Matrix C;

	void testPrintArray() {
		
		System.out.println(Matrix._2dArraytoString(a));
	}

	void testPrintMatrixes() {
		System.out.println(A);
		System.out.println();
		System.out.println(B);
	}

	public void prepare(int size) {
		a = new double[size][size];
		// testPrintArrays();
		for (int i = 0; i < size; i++) {
			a[i][i] = 1;
		}
		// testPrintArrays();
		A = new Matrix(a);
		B = new Matrix(a);
		C = new Matrix(a);
		// a[1][1] = 4;
		// A.getField()[0][0] = 5;
		// testPrintMatrixes();
	}

	@Test
	public void test() {
		//check empty matrix multiplication
		int i = 0;
		prepare(i++);
		try {
			C.compareTo(A.times(B));
			fail();
		} catch (IllegalArgumentException e) {
			if (!e.getMessage().equals(
					"Unable to multiply zero-dimesion matrix")) {
				fail();
			}
		}

		for (; i < 2049; i *= 2) {
			// System.out.println(i);
			prepare(i);
			assertTrue(C.compareTo(A.times(B)));
		}
	}

}
