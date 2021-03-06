package eigenvector;

import ru.math.spbu.avapir.matrix_multiplication.Matrix;

@SuppressWarnings("unused")
public class Main {

	static Matrix E;
	static Matrix input1;
	static Matrix input2;
	static Matrix multiplier1;
	static Matrix multiplier2;

	static int N = 12;

	static {
		double[] input11 = { 0.75, 1, 0.75, 0.5, 0, 0.75, 0.25, 0, 0.25, 0.75,
				0.75, 0.25, 0.5, 1, 0.75, 0.75 };
		double[] input12 = { 0.5, 0.25, 0.5, 0.75, 0.75, 0.5, 0.75, 1, 0.5,
				0.25, 0.5, 0.75, 0.25, 0, 0.25, 0.5 };

		double[] input21 = { 0.75, 1.25, 1, 0.75, 0.25, 0.75, 0.5, 0.25, 0.5,
				1, 0.75, 0.5, 0.75, 1.25, 1, 0.75 };
		double[] input22 = { 0.75, 0.5, 0.75, 1, 1, 0.75, 1, 1.25, 0.75, 0.5,
				0.75, 1, 0.5, 0.25, 0.5, 0.75 };

		double[] input41 = { 1, 1.5, 1.25, 1, 0.5, 1, 0.75, 0.5, 0.75, 1.25, 1,
				0.75, 1, 1.5, 1.25, 1 };
		double[] input42 = { 1, 0.75, 1, 1.25, 1.25, 1, 1, 1.5, 1, 0.75, 1,
				1.25, 0.75, 0.5, 0.75, 1 };

		double[] input51 = { 1, 2, 1.5, 1, 0, 1, 0.5, 0, 0.5, 1.5, 1, 0.5, 1,
				2, 1.5, 1 };
		double[] input52 = { 1, 0.5, 1, 1.5, 1.5, 1, 1.5, 2, 1, 0.5, 1, 1.5,
				0.5, 0, 0.5, 1 };

		double[] input61 = { 10.75, 13.25, 12, 10.75, 8.25, 10.75, 9.5, 8.25,
				9.5, 12, 10.75, 9.5, 10.75, 13.25, 12, 10.75 };
		double[] input62 = { 10.75, 9.5, 10.75, 12, 12, 10.75, 12, 13.25,
				10.75, 9.5, 10.75, 12, 9.5, 8.25, 9.5, 10.75 };

		double[] input71 = { 1, 1.1, 1.05, 1, 0.5, 1, 0.95, 0.9, 0.95, 1.05, 1,
				0.95, 1, 1.1, 1.05, 1 };
		double[] input72 = { 1, 0.95, 1, 1.05, 1.05, 1, 1, 1.1, 1, 0.95, 1,
				1.05, 0.95, 0.9, 0.9, 1 };

		double[] input81 = { 1, 1.002, 1.001, 1, 0.998, 1, 0.999, 0.998, 0.999,
				1.001, 1, 0.999, 1, 1.002, 1.001, 1 };
		double[] input82 = { 1, 0.999, 1, 1.001, 1.001, 1, 1, 1.002, 1, 0.999,
				1, 1.001, 0.999, 0.998, 0.999, 1 };

		input1 = new Matrix(input11, 4, 4);
		input1.putOnDiagonal(1.5);
		
		double[] e = { 1, 1, 1, 1 };
		E = new Matrix(e, 4, 1);
	}

	public static void main(String[] args) {
		System.out.println(E);
		System.out.println(input1);
//		System.out.println(input2);

		doWork();
	}

	private static void doWork() {
		multiplier1 = input1.timesSequential(E);
		multiplier1.normalize();
//		multiplier2 = input2.timesSequential(E);
//		multiplier2.normalize();

		multiplyAndNormalize(N);

	}

	private static void multiplyAndNormalize(int N) {
		System.out.println("Iterations count: " + N + "\n");
		for (int i = 0; i < N; i++) {

			multiplier1 = input1.times(multiplier1);
//			multiplier2 = input2.times(multiplier2);
			multiplier1.normalize();
//			multiplier2.normalize();
		}
		System.out.println(multiplier1);
		System.out.println("First:\n" + multiplier1.roundAfterDot(3) + "\n");
//		System.out.println(multiplier2);
//		System.out.println("Second:\n" + multiplier2.roundAfterDot(3) + "\n");

	}

}
