
public class MatrixMultiply {
	public static double[][] multiply(double[][] A, double[][] B) {
		if((A.length == 0 || A.length != A[0].length) ||
		   (B.length == 0 || B.length != B[0].length) ||
		   (A.length != B.length))
			throw new IllegalArgumentException("Dimensions don't match");
		
		int n = A.length;
		
		double[][] result = new double[n][n];
		
		for(int i = 0; i < n; i++)
			for(int k = 0; k < n; k++)
				for(int j = 0; j < n; j++)
					result[i][j] += A[i][k] * B[k][j];
		
		return result;
	}
	
	public static void transpose(double[][] A){
		double temp;
		for(int i = 0; i < A.length; i++)
			for(int j = i + 1; j < A.length; j++) {
				temp = A[i][j];
				A[i][j] = A[j][i];
				A[j][i] = temp;
			}
	}
	
	public static void main(String[] args) {
		final int N = 1024;
		final double[][] A = new double[N][N], B = new double[N][N], C = new double[N][N];
		long start, stop;
		double transposeTime;
		
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				A[i][j] = B[i][j] = i * N + j;
		
		System.out.println("Data prepared");
		
		start = System.nanoTime();
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				for(int k = 0; k < N; k++)
					C[i][j] += A[i][k] * B[k][j];
		stop = System.nanoTime();
		
		System.out.println("Non-transposed; cycles i-j-k " + (stop - start) / 1e9);
		
		start = System.nanoTime();
		for(int i = 0; i < N; i++)
			for(int k = 0; k < N; k++)
				for(int j = 0; j < N; j++)
					C[i][j] += A[i][k] * B[k][j];
		stop = System.nanoTime();
		
		System.out.println("Non-transposed; cycles i-k-j " + (stop - start) / 1e9);
		
		start = System.nanoTime();
		transpose(A);
		stop = System.nanoTime();
		transposeTime = (stop - start) / 1e9;
		
		start = System.nanoTime();
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				for(int k = 0; k < N; k++)
					C[i][j] += A[k][i] * B[k][j];
		stop = System.nanoTime();
		
		System.out.println( "A transposed; cycles i-j-k " + (stop - start) / 1e9 + 
							" + " + transposeTime + " for transpose = " +
							(transposeTime + (stop - start) / 1e9 )
		);
		
		start = System.nanoTime();
		transpose(B);
		stop = System.nanoTime();
		transposeTime = (stop - start) / 1e9;
		
		start = System.nanoTime();
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				for(int k = 0; k < N; k++)
					C[i][j] += A[i][k] * B[j][k];
		stop = System.nanoTime();
		
		System.out.println( "B transposed; cycles i-j-k " + (stop - start) / 1e9 + 
							" + " + transposeTime + " for transpose = " +
							(transposeTime + (stop - start) / 1e9 )
		);
		
		start = System.nanoTime();
		transpose(B);
		transpose(A);
		stop = System.nanoTime();
		transposeTime = (stop - start) / 1e9;
		
		start = System.nanoTime();
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				for(int k = 0; k < N; k++)
					C[i][j] += A[k][i] * B[j][k];
		stop = System.nanoTime();
		
		System.out.println( "Both transposed; cycles i-j-k " + (stop - start) / 1e9 + 
							" + " + transposeTime + " for transpose = " +
							(transposeTime + (stop - start) / 1e9 )
		);
	}
}
