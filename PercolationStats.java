import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.lang.Math;

public class PercolationStats
{
    private double[] totalMean;
    private double percolationFraction;
    private Percolation trial;
    private int column;
    private int row;
    private int counter;
    
    public PercolationStats(int n, int trials)
    {
        if(n  <= 0 || trials <= 0)
        {
            throw new IllegalArgumentException();
        }
        
        trial = new Percolation(n);
        totalMean = new double[trials];
        for(int i = 0; i<trials; i++)
        {
            counter = 0;
            while(!trial.percolates())
            {
                int selectedLoc = StdRandom.uniform(1,n*n); // select a random location
                
                if(selectedLoc%5 == 0) // we convert the selected value to a row,column value
                {
                    column = 5;
                    row = ((selectedLoc-column)/5)+1;
                }
                else
                {
                    column = selectedLoc%5;
                    row = ((selectedLoc-column)/5)+1;
                }
                
                //System.out.println("selectedLoc: " + selectedLoc + ", row: " + row + ", col: " + column);
                
                if(!(trial.isOpen(row,column) || trial.isFull(row,column))) // we check if the spot is not open and not full
                {
                    counter++;
                    trial.open(row,column);
                }
            }
            System.out.println("percolated counter: " + counter);
            trial = new Percolation(n);
            percolationFraction = counter/(double)(n*n); // calculate the current experiments percolation fraction
            totalMean[i] = (percolationFraction/trials); // calculate the current total mean.
        }
    }
    
    public double mean()
    {
        return StdStats.mean(totalMean);
    }
    
    public double stddev()
    {
        return StdStats.stddev(totalMean);
    }
    
    public double confidenceLo()
    {
        return (mean() - (1.96*stddev())/Math.sqrt(totalMean.length));
    }
    
    public double confidenceHi()
    {
        return (mean() + (1.96*stddev())/Math.sqrt(totalMean.length));
    }
    
    public static void main(String[] args)
    {
        PercolationStats test = new PercolationStats(5, 5);
        System.out.println(test.mean());
        System.out.println(test.stddev());
        System.out.println(test.confidenceLo());
        System.out.println(test.confidenceHi());
    }
    
}