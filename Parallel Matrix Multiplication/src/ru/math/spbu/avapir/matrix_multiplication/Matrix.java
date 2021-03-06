package ru.math.spbu.avapir.matrix_multiplication;

import java.util.Arrays;

import javax.management.RuntimeErrorException;

/**
 * 
 * Typical matrix representation
 * 
 * @author Alpen Ditrix
 * 
 */
public class Matrix {

	private void checkRow(int r) {
		if (r < 0 || r >= N) {
			throw new RuntimeException("Wrong row number");
		}
	}

	private void checkColumn(int c) {
		if (c < 0 || c >= M) {
			throw new RuntimeException("Wrong column number");
		}
	}

	public Matrix getSubmatrixFromHead(int endRow, int endColumn) {
		return getSubmatrix(0, 0, endRow, endColumn);
	}

	public Matrix getSubmatrixToTail(int beginRow, int beginColumn) {
		return getSubmatrix(beginRow, beginColumn, N, M);
	}

	public Matrix getSubmatrix(int beginRow, int beginColumn, int endRow,
			int endColumn) {
		checkRow(beginRow);
		checkColumn(beginColumn);
		checkRow(endRow);
		checkColumn(endColumn);
		int n = endRow - beginRow + 1;
		int m = endColumn - beginColumn + 1;
		double[][] fieldd = new double[n][m];
		for (int i = 0; i < n; i++) {
			System.arraycopy(this.field[i+beginRow], beginColumn, fieldd[i], 0, m);
		}
		return new Matrix(fieldd);
	}

	public static Matrix getEyeMatrix(int dimension) {
		Matrix E = new Matrix(dimension);
		E.putOnDiagonal(1);
		return E;
	}
	
	public void putOnDiagonal(double d) {
		if (M != N) {
			throw new RuntimeException(
					"Can't put on diagonal for non-squared matrix");
		}

		for (int i = 0; i < N; i++) {
			field[i][i] = d;
		}
	}

	/**
	 * Executes for each thread whenever matrixes multiplication requested<br>
	 * Each one acquire this {@link Matrix}(as caller object), Matrix to
	 * multiply, array, where to put results of computation and borders of data,
	 * which must be computed by this thread. - *
	 * 
	 * @author Alpen Ditrix
	 */
	private final class MultiplicationRunner implements Runnable {
		private final double[][] resultArray;
		private double[] col_B_j;
		private double[] row_A_i;
		double s;
		private final Matrix B;
		int min;
		int max;
		int resultHeight;
		int resultWidth;

		/**
		 * Creates new {@link Runnable} which will be executed<BR>
		 * <b>This one will work on columns
		 * 
		 * @param min
		 *            from this column
		 * @param max
		 *            to this
		 * @param res
		 *            array, where to put results
		 * @param b
		 *            matrix to multiply
		 */
		private MultiplicationRunner(int min, int max, double[][] res, Matrix b) {
			this.resultArray = res;
			this.B = b;
			this.min = min;
			this.max = max;
			resultHeight = getRowsCount();
			resultWidth = B.getColumnsCount();
		}

		/**
		 * Multiplies chosen columns on all rows of {@link #B}
		 */
		@Override
		public void run() {
			for (int j = min; j < max; j++) {
				// ������ ��� �� ��������������� B
				// long time = System.nanoTime();
				// col_B_j = B.getRow(j);
				col_B_j = B.getColumn(j);
				// nanoTime += System.nanoTime() - time;
				for (int i = 0; i < resultHeight; i++) {
					row_A_i = getRow(i);
					s = 0;
					for (int k = 0; k < MIN(col_B_j.length, row_A_i.length); k++) {
						s += row_A_i[k] * col_B_j[k];
						// System.out.print(col_B_j[k] + "*" + row_A_i[k] +
						// " + ");
					}
					// System.out.println("|" + s);
					if (i < resultHeight && j < resultWidth) {
						resultArray[i][j] = s;
					}
				}
			}
		}
	}

	public void normalize() {
		if (M > 1) {
			throw new RuntimeException("Can not normalize non-vector matrix");
		}
		double normalizator = 0;
		for (int i = 0; i < N; i++) {
			normalizator += field[i][0];
		}
		for (int i = 0; i < N; i++) {
			field[i][0] /= normalizator;
		}
	}

	@Override
	public Object clone() {
		return new Matrix(this);
	}

	public void addToThisToAll(double that) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				field[i][j] += that;
			}
		}
	}

	//
	// public Matrix addToAll(int that){
	//
	// }

	public void roundThisAfterDot(int digits) {
		int mult = 1;
		for (int i = 0; i < digits; i++) {
			mult *= 10;
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				field[i][j] = ((double) Math.round((field[i][j] * mult)))
						/ mult;
			}
		}
	}

	public Matrix roundAfterDot(int digits) {
		double[][] f = ((Matrix) this.clone()).getField();

		int mult = 1;
		for (int i = 0; i < digits; i++) {
			mult *= 10;
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				f[i][j] = ((double) Math.round((f[i][j] * mult))) / mult;
			}
		}
		return new Matrix(f);
	}

	// public static long nanoTime = 0;

	/**
	 * Checks that dimensions of specified matrixes are correct to multiply them
	 * 
	 * @param A
	 * @param B
	 */
	private static void checkDimensions(Matrix A, Matrix B) {
		// if (!A.isPerfectWith(B)) {
		// throw new IllegalArgumentException("Dimensions do not match");
		// }

		if (A.isZeroMatrix() || B.isZeroMatrix()) {
			throw new IllegalArgumentException(
					"Unable to multiply zero-dimesion matrix");
		}
	}

	public static void main(String[] args) {
		double[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
		double[][] b = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
		Matrix A = new Matrix(a);
		Matrix B = new Matrix(b);
		System.out.println(A.timesSequential(B));
		System.out.println(A.timesParallel(B));
		// System.out.println(B.transpose().timesParallel(A.transpose())
		// .transpose());
	}

	/**
	 * Rows amount
	 */
	private int N;

	/**
	 * Columns amount
	 */
	private int M;

	/**
	 * Data storage
	 */
	private double[][] field;

	/**
	 * Stores amount of hardware CPU cores
	 */
	private static final int AVAILABLE_CORES = 4;// Runtime.getRuntime().availableProcessors();

	/**
	 * Creates matrix from packed array. To transform it correctly, method must
	 * receive dimensions of new matrix
	 * 
	 * @param f
	 *            packed array
	 * @param n
	 *            amount of rows of new matrix
	 * @param m
	 *            amount of columns of new matrix
	 */
	public Matrix(double[] f, int n, int m) {
		N = n;
		M = m;
		field = new double[N][M];
		for (int i = 0; i < n * m; i++) {
			field[i / M][i % M] = f[i];
		}
	}

	/**
	 * Creates matrix straight from 2D data array
	 * 
	 * @param f
	 */
	public Matrix(double[][] f) {
		try {
			field = new double[f.length][f[0].length];
			for (int i = 0; i < f.length; i++) {
				field[i] = f[i].clone();
				N = f.length;
				M = f[0].length;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("It seems null matrix was created");
			N = M = 0;
		}

	}

	/**
	 * Creates new empty matrix with its dimensions
	 * 
	 * @param n
	 * @param m
	 */
	public Matrix(int n, int m) {
		N = n;
		M = m;
		field = new double[N][M];
	}

	public Matrix(int dim) {
		new Matrix(dim, dim);
	}

	public Matrix(Matrix b) {
		this.field = new double[b.N][b.M];
		this.N = b.N;
		this.M = b.M;
		System.arraycopy(b.field, 0, this.field, 0, b.field.length);
	}

	/**
	 * @param m
	 *            matrix to compare
	 * @return is all values of cells of both matrixes are similar
	 */
	public boolean compareTo(Matrix m) {
		double[][] a = field;
		double[][] b = m.getField();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (a[i][j] != b[i][j]) {
					System.out.println(a[i][j] + " != " + b[i][j]);
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Matrix) {
			Matrix M = (Matrix) o;
			return compareTo(M);
		} else {
			System.out.println("Is not instance of Matrix");
			return false;
		}
	}

	/**
	 * @param j
	 * @return column packed in array specified by its index
	 */
	public double[] getColumn(int j) {
		double[] out = new double[N];
		for (int k = 0; k < N; k++) {
			out[k] = field[k][j];
		}
		return out;
	}

	/**
	 * @return field of matrix
	 */
	public double[][] getField() {
		return field;
	}

	/**
	 * @return amount of rows
	 */
	public int getRowsCount() {
		return N;
	}

	/**
	 * @return {@link #getRowsCount()}*{@link #getColumnsCount()}
	 */
	public long getMultipliedSize() {
		return N * M;
	}

	/**
	 * @param i
	 * @return row packed in array specified by its index
	 */
	public double[] getRow(int i) {
		return field[i];
	}

	/**
	 * @return amount of columns
	 */
	public int getColumnsCount() {
		return M;
	}

	/**
	 * Implements comparing of amount rows of one matrix and amount of columns
	 * of another one and vice versa
	 * 
	 * @param B
	 * @return is this matrix is perfect to another
	 */
	public boolean isPerfectWith(Matrix B) {
		// System.out.println(getWidth());
		// System.out.println(B.getHeight());
		return getColumnsCount() <= B.getRowsCount();
	}

	/**
	 * Checks if it is a zero-matrix
	 * 
	 * @return {@li#getRowsCount()}{@code ==0 || }{@link #getColumnsCount()}
	 *         {@code ==0}
	 */
	public boolean isZeroMatrix() {
		return N == 0 || M == 0;
	}

	private int MIN(int a, int b) {
		return a > b ? b : a;
	}

	/**
	 * Prepares array of threads for parallel multiplication<br>
	 * Each thread receives part of matrix to compute
	 * 
	 * @param B
	 *            matrix to multiply on this
	 * @param resultArray
	 *            where to put computed data
	 * @return threads packed in array
	 */
	private Thread[] prepareThreads(final Matrix B, final double[][] resultArray) {
		final int[] threadDataBorders = new int[AVAILABLE_CORES + 1];
		for (int i = 0; i < AVAILABLE_CORES + 1; i++) {
			threadDataBorders[i] = Math.round(((float) B.getColumnsCount()
					/ (float) AVAILABLE_CORES * i));
			// System.out.println(threadDataBorders[i] + " = " + B.getWidth()
			// + " / " + AVAILABLE_CORES + " * " + i);
		}

		Thread[] threads = new Thread[AVAILABLE_CORES];

		for (int i = 0; i < threadDataBorders.length - 1; i++) {
			threads[i] = new Thread(new MultiplicationRunner(
					threadDataBorders[i], threadDataBorders[i + 1],
					resultArray, B));
		}
		return threads;
	}

	/**
	 * Multiplies this matrix on specified.<br>
	 * Method will try to choose optimal algorithm
	 * 
	 * @param B
	 *            matrix to multiply on this
	 * @return result matrix
	 */
	public Matrix times(Matrix B) {
		// if (getMultipliedSize() > 25000 && AVAILABLE_CORES > 1) {
		return timesParallel(B);
		// } else {
		// return timesSequential(B);
		// }
	}

	/**
	 * Multiplies this matrix on specified one using few threads
	 * 
	 * @param B
	 *            matrix to multiply on this
	 * @return multiplication result
	 * @throws InterruptedException
	 *             throws whenever some thread had been interrupted
	 */
	public Matrix timesParallel(Matrix B) {
		// nanoTime=0;
		checkDimensions(this, B);
		int resultHeight = getRowsCount();
		int resultWidth = B.getColumnsCount();
		final double[][] resultArray = new double[resultHeight][resultWidth];
		Thread[] threads = prepareThreads(B, resultArray);

		// B.transpose();
		for (Thread t : threads) {
			t.start();
		}
		try {
			for (Thread t : threads) {
				t.join();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// B.transpose();
		return new Matrix(resultArray);
	}

	/**
	 * Multiplies this matrix on specified one using one thread
	 * 
	 * @param B
	 *            matrix to multiply on this
	 * @return result matrix
	 */
	public Matrix timesSequential(Matrix B) {
		checkDimensions(this, B);

		int resultHeight = this.getRowsCount();
		int resultWidth = B.getColumnsCount();
		final double[][] resultArray = new double[resultHeight][resultWidth];

		Matrix tmp = B;
		B = transpose(B);
		for (int j = 0; j < resultWidth; j++) {
			double[] col_B_j = B.getRow(j);
			// double[] col_B_j = B.getColumn(j);
			for (int i = 0; i < resultHeight; i++) {
				double[] row_A_i = getRow(i);
				double s = 0;
				for (int k = 0; k < MIN(col_B_j.length, row_A_i.length); k++) {
					s += row_A_i[k] * col_B_j[k];
					// System.out.print(col_B_j[k] + "*" + row_A_i[k] + " + ");
				}
				// System.out.println("|" + s);
				resultArray[i][j] = s;
			}
		}
		B = tmp;

		return new Matrix(resultArray);
	}

	public static double[] getRow(Matrix M, int i, int width) {
		double[] out = new double[width];
		System.arraycopy(M.getRow(i), 0, out, 0, M.getColumnsCount());
		return out;
	}

	public static double[] getColumn(Matrix M, int i, int height) {
		double[] out = new double[height];
		System.arraycopy(M.getColumn(i), 0, out, 0, M.getRowsCount());
		return out;
	}

	/**
	 * Packs all rows into one array
	 * 
	 * @return packed matrix
	 */
	public double[] toArray() {
		double[] d = new double[N * M];
		for (int i = 0; i < N * M; i++) {
			d[i] = field[i / M][i % M];
		}
		return d;
	}

	public static String _2dArraytoString(double[][] arr) {
		StringBuilder sb = new StringBuilder();
		StringBuilder border = new StringBuilder();
		for (int i = 0; i < arr[0].length; i++) {
			border.append('_');
		}
		border.append('\n');
		sb.append(border);
		for (int i = 0; i < arr.length; i++) {
			sb.append(Arrays.toString(arr[i]));
			sb.append('\n');
		}
		sb.append(border);
		return sb.toString();

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		StringBuilder border = new StringBuilder();
		for (int i = 0; i < M; i++) {
			border.append('_');
		}
		border.append('\n');
		sb.append(border);
		for (int i = 0; i < N; i++) {
			sb.append(Arrays.toString(field[i]));
			sb.append('\n');
		}
		sb.append(border);
		return sb.toString();
	}

	/**
	 * Turns all the rows into columns and vice versa
	 * 
	 * @return
	 */
	public static Matrix transpose(Matrix M) {
		// long time = System.currentTimeMillis();
		// long time = System.nanoTime();
		double[][] a = new double[M.field[0].length][M.field.length];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++) {
				a[i][j] = M.field[j][i];
			}
		// nanoTime += System.nanoTime() - time;
		// System.out.println("   transposed for "+(System.currentTimeMillis()-time));
		return new Matrix(a);
	}

	public double getDeterminant(){
		//TODO
		return 0;
	}
	
	public double getTrace(){
		
	}
	
}