package com.dynamic.sse;

public class Matrix {

	private final int M;             
    private final int N;             
    private final double[][] data; 

    // create M-by-N matrix of 0's
    public Matrix(int M, int N) 
    {
        this.M = M;
        this.N = N;
        data = new double[M][N];
    }
    public static Matrix random(int M, int N) {
        Matrix A = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[i][j] = 0;
        return A;
    }
	
	public void show() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                System.out.println(i+ "th row " + data[i][j] +" " + j + "th column");
        }
    }
}
