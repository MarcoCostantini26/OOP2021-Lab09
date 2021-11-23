package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix{
    
    private final int n;
    
    public MultiThreadedSumMatrix(final int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        this.n = n;
    }
    
    private static class Worker extends Thread{
       private final double[][] matrix;
       private final int startPos;
       private final int nElement;
       private long result;
       /**
        * Build new worker
        * 
        * @param list list of element to sum
        * @param startPos starting position
        * @param nElement number of element 
        */
       public Worker(final double[][] matrix, final int startPos, final int nElement) {
        super();
        this.matrix = Arrays.copyOf(matrix, matrix.length);
        this.startPos = startPos;
        this.nElement = nElement;
       }
       @Override
       public void run() {
           System.out.println("Working from position " + startPos + " to position " + (startPos + nElement - 1));
           for (int i = startPos; i < matrix.length && i < startPos + nElement; i++) {
               for(int j = 0; j < matrix[i].length; j++) {
                   result += matrix[i][j];
               }
           }
       }
       

       public long getResult() {
           return this.result;
       }
    }

    @Override
    public double sum(final double[][] matrix) {
        final int size = matrix.length % n + matrix.length / n;
        /*
         * Build a list of workers
         */
        final List<Worker> workers = new ArrayList<>(n);
        for (int start = 0; start < matrix.length; start += size) {
            workers.add(new Worker(matrix, start, size));
        }
        /*
         * Start them
         */
        for (final Worker w: workers) {
            w.start();
        }
        long sum = 0;
        for (final Worker w: workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }

}
