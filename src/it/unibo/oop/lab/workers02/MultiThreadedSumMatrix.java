package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unibo.oop.lab.workers01.MultiThreadedListSumClassic.Worker;

public class MultiThreadedSumMatrix implements SumMatrix{
    
    private final int n;
    
    public MultiThreadedSumMatrix(final int n) {
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
       public Worker(final double[][] matrix, final int startPos, final int nElement, final long result) {
        super();
        this.matrix = Arrays.copyOf(matrix, matrix.length);
        this.startPos = startPos;
        this.nElement = nElement;
        this.result = result;
       }
       @Override
       public void run() {
           System.out.println("Working from position " + startPos + " to position " + (startPos + nElement - 1));
           for (int i = startPos; i < matrix.length && i < startPos + nElement; i++) {
               for(int j = i; j < matrix[i].length; j++) {
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
        final List<Worker> workers = new ArrayList<>(n);
        return 0;
    }

}
