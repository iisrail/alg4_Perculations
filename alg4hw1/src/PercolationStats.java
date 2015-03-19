/**
 * This class enables to perform a series of computational experiments
 * and eventually calculates statistic and performance data
 * 
 * @author Igal Israilevich
 */
public class PercolationStats {
 double[] fractions;
 int T;
 int N;
 int WN;

 // perform T independent experiments on an N-by-N grid
 public PercolationStats(int N, int T) {
     if (N <= 0 || T <= 0) {
         throw new IllegalArgumentException("not valid input arguments");
     }
     this.T = T;
     this.N = N;
     this.WN = N * N;
     fractions = new double[T];
 }

 private void test() {
     for (int i = 0; i < T; i++) {
         Percolation perc = new Percolation(N);
         
         int[] randomMatrix = new int[N * N];
         initRandomArray(randomMatrix);
         // printArray(randomMatrix);
         int time = 0;
         int matrix, col, row;
         while (!perc.percolates()) {
             // get random site
             matrix = randomMatrix[time];
             row = (matrix - 1) / N + 1;
             col = matrix % N + 1;
             perc.open(row, col);
             time++;
         }
         fractions[i] = (double) time / WN;
     }
 }

 /**
  * sample mean of percolation threshold
  */
 public double mean() {
     double sum = 0.0;
     for (double fraction : fractions)
         sum += fraction;
     return sum / T;
 }

 /**
  * sample standard deviation of percolation threshold
  */
 public double stddev() {
     return Math.sqrt(getVariance());
 }

 private double getVariance() {
     double mean = mean();
     double temp = 0;
     for (double fraction : fractions)
         temp += (mean - fraction) * (mean - fraction);
     return temp / (T - 1);
 }

 /**
  * low endpoint of 95% confidence interval
  */
 public double confidenceLo() {
     double mean = mean();
     double stdv = stddev();
     
     return mean - ((1.96 * stdv) / Math.sqrt(T));
 }

 /**
  * high endpoint of 95% confidence interval
  */
 public double confidenceHi() {
     double mean = mean();
     double stdv = stddev();
     
     return mean + ((1.96 * stdv) / Math.sqrt(T));
 }

 private void initRandomArray(int[] arr) {
     int len = arr.length;
     for (int i = 0; i < len; i++) {
         arr[i] = i + 1;
     }
     for (int i = 0; i < len; i++) {
         int rand = StdRandom.uniform(0, len);
         int t = arr[i];
         arr[i] = arr[rand];
         arr[rand] = t;
     }
 }

 private static void printArray(int[] arr) {
     System.out.println("arr length = " + arr.length);
     for (int i = 0; i < arr.length; i++) {
         System.out.println(arr[i]);
     }
 }

 
 public static void main(String[] args) {
     Stopwatch stopwatch = new Stopwatch();
     int N = Integer.parseInt(args[0]);
     int T = Integer.parseInt(args[1]);
     PercolationStats stats = new PercolationStats(N, T);
     stats.test();
     double seconds = stopwatch.elapsedTime();
     System.out.println("PercolationStats N = " +N+" T = "+T);
     System.out.println("mean\t\t\t = " + stats.mean());
     System.out.println("stddev\t\t\t = " + stats.stddev());
     System.out.println("95% confidence interval = " + stats.confidenceLo()
                            + ", " + stats.confidenceHi());
     System.out.println("takes time " + seconds + " seconds");
 }
}
