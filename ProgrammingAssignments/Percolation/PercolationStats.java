/* Dane Osborne 6/30/2015
 * This class analyzes the execution
 * of the Percolation class.
 *
 * Complilation: javac PercolationStats.java
 * Execution:    java PercolationStats.java 200 100
*/


public class PercolationStats {
   
   private int N;              // variable to store the number of sites
                          
   private int T;              // variable to store the number of experiments
                               // performed
                          
   private double[] results;   // integer array to store the results of each
                               // experiment
   
   // Constructor performs T computational experiments on an N-by-N grid
   public PercolationStats(int N, int T) {
      if (N <= 0 || T <= 0) 
         throw new IllegalArgumentException("The dimension of the grid and experiments must be"
                           +"greater than 0.");
      this.N = N;
      this.T = T;
      results = new double[T];
      
      for (int cnt = 0; cnt < T; cnt++) {
         results[cnt] = run();
      } //end for loop
   } //end Percolation constructor
                          
   // sample mean of percolation threshold
   public double mean() {
      double mean = StdStats.mean(results);
      return mean;
   }
   
   // sample standard deviation of percolation threshold
   public double stddev() {
      double stddev = StdStats.stddev(results);
      return stddev;
   }
   
   public double confidenceLo() {
      double confidenceLo = mean() - 1.96 *  stddev() / Math.sqrt(T);
      return confidenceLo;
   }
   
   
   public double confidenceHi() {
      double confidenceHi = mean() + 1.96 *  stddev() / Math.sqrt(T);
      return confidenceHi;
   }
   
   // private method to return a random integer
   private int random(int i) {
      int randomNumber = StdRandom.uniform(i) + 1;
      return randomNumber;
   }
   
   private double run() {
      Percolation percolationObject = new Percolation(N);
      double openSitesCount = 0;
      while (!percolationObject.percolates()) {
         int i = random(N);
         int j = random(N);
         if (!percolationObject.isOpen(i, j)) {
            percolationObject.open(i, j);
            openSitesCount++;
         }
      } // end while loop
         
      return openSitesCount / (N * N);
   }
   
   // main method to test Percolation Stats
   public static void main(String[] args) {
      int N = 0;
      int T = 0;
      
      if (args.length > 0) {
         try {
            N = Integer.parseInt(args[0]);
            T = Integer.parseInt(args[1]);
         } catch (NumberFormatException e) {
            System.err.print("Number format exception");
         }
      }
      PercolationStats perc = new PercolationStats(N, T);
      StdOut.println("mean                 = " + perc.mean());
      StdOut.println("stddev               = " + perc.stddev());
      StdOut.println("95% confidence level = " + perc.confidenceLo()
                                         + " " + perc.confidenceHi());
   } // end main
  
}