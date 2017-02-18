
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	
	private int trials;
	private double []stat;
	
	// perform trials independent experiments on an n-by-n grid
	public PercolationStats(int n, int trials){
		if(n <=0 || trials <= 0){
			throw new java.lang.IllegalArgumentException();
		}
		
		this.trials = trials;
		
		stat = new double[trials];
		for(int i=0;i< trials; i++){
			int open = 0;
			
			Percolation p = new Percolation(n);
			while(!p.percolates()){
				int row = StdRandom.uniform(1, n+1);
				int col = StdRandom.uniform(1, n+1);
				
				if(!p.isOpen(row, col)){
					open ++ ;
					p.open(row, col);
				}
			}
			stat[i] = open*1.0 /(n*n);
			
		}
		
	}

	// sample mean of percolation threshold
	public double mean(){
		return StdStats.mean(stat);
		
	}

	// sample standard deviation of percolation threshold
	public double stddev(){
		return StdStats.stddev(stat);
	}

	// low  endpoint of 95% confidence interval
	public double confidenceLo(){
		return mean() - 1.96*stddev()/Math.sqrt(this.trials);
		
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi(){
	  return mean() + 1.96*stddev()/Math.sqrt(this.trials);	
	}

	public static void main(String[] args) {
		if(args.length != 2){
			throw new java.lang.IllegalArgumentException();
		}
		
		int n = Integer.valueOf( args[0]);
		int trials = Integer.valueOf( args[1]);
		
		PercolationStats p = new PercolationStats(n, trials);
		StdOut.println("mean                    = " + p.mean() );
		StdOut.println("stddev                  = " + p.stddev());
		StdOut.println("95% confidence interval = " + p.confidenceLo() + ", " +  p.confidenceHi());
		
	}

}
