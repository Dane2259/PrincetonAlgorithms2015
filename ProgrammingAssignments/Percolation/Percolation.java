/******************************************************************** 
 * Dane Osborne 6/30/2015
 * Compilation:  javac Percolation.java
 * Execution:    java Percolation input20.txt
 * Dependencies: WeightedQuickUnionUF.java, StdIn.java, StdOut.java
*********************************************************************/
 
/* The Percolation class creates a two dimensional
 * grid with all sites initially blocked.  The class
 * reads from the input file the number of rows and
 * columns, N, and then repeatedly opens sites read
 * in from the text file (row i, column j).  If a site in
 * the first row is connected to a site in the bottom
 * row then there is a path from the top row to the
 * bottom, and therefore the system percolates.
*/

public class Percolation {
   private int rows;            // Variable for number of rows (N).
   
   private int columns;         // Variable for number of columns (N).
   
   private int numberOfSites;   // Variable for number of sites (N squared).
   
   private boolean[] siteOpen;  // An array to track open sites.
   
   private int seed;            // Variable for place in array
                                // of selected site in 2D grid.
                                
   private int adjacentSeed;    // Variable for site next to the
                                // chosen site.
  
   private int N;               // Dimension of the grid
   
   private WeightedQuickUnionUF wQU;
   
   /* Constructor to create a Percolation instance
    * Input is the number of rows and columns.
    * @param N the number of rows and columns.
    * @throws IllegalArgumentException unless N > 0.
    */
   public Percolation(int N) {
      if (N <= 0) throw 
         new IllegalArgumentException("The number of rows and columns "
                                               + "must be greater than 0");
      this.N = N;
      setRows(N);
      setColumns(N);
      setDimen(getRows() * getColumns());
      siteOpen = new boolean[numberOfSites];
      setAdjacentSeed(0);
      setSeed(0);
      
      wQU = new WeightedQuickUnionUF(numberOfSites + 2);
   } 
   
   /* Method to determine if a site is open or not.
    * @param i the row of the test site.
    * @param j the column of the test site.
    * @return if the test site is open (true) or closed (false)
    */
   public boolean isOpen(int i, int j) {
      validateIndices(i, j);
      return siteOpen[xyTo1D(i, j)];
   } 
   
   /* Method to open chosen site and determine if cells adjacent
    * to chosen site are also open.  If so, they should be 
    * connected using the weightedQuickUnion object.
    * @param i row of site to be opened.
    * @param j column of site to be opened.
    */
   public void open(int i, int j) {
      validateIndices(i, j);
      setSeed(xyTo1D(i, j));
      siteOpen[getSeed()] = true;
      
      // cell above
      if (i > 1 && isOpen(i - 1, j)) {
         setAdjacentSeed(xyTo1D(i - 1, j));
         wQU.union(getSeed(), getAdjacentSeed());
      }
      // cell below
      if (i < getRows() && isOpen(i + 1, j)) {
         setAdjacentSeed(xyTo1D(i + 1, j));
         wQU.union(getSeed(), getAdjacentSeed());
      }
      
      // cell to the right
      if (j < getColumns() && isOpen(i, j + 1)) {
         setAdjacentSeed(xyTo1D(i, j + 1));
         wQU.union(getSeed(), getAdjacentSeed());
      }
      
      // cell to the left
      if (j > 1 && isOpen(i, j - 1)) {
         setAdjacentSeed(xyTo1D(i, j - 1));
         wQU.union(getSeed(), getAdjacentSeed());
      }
      
      // if cell is in the first row connect it to the 
      // virtual site above the first row.
      if (i == 1) wQU.union(getDimen(), getSeed());
      
      // if cell to be opened is in the last row connect
      // it to the virtual site below the last row.
      if (i == getRows()) wQU.union(getDimen() + 1, getSeed());
      
   } 
   
   /* Method to determine if the chosen site is connected to 
    * the first row.
    * @param  i row of chosen site.
    * @param  j column of chosen site
    * @return true if site is connected to first row, false
    *         if not connected to first row.
    */
   public boolean isFull(int i, int j) {
      validateIndices(i, j);
      if (isOpen(i, j)) {
         setSeed(xyTo1D(i, j));
         if (wQU.connected(getDimen(), getSeed()))
            return true;
      }
      return false;
   }
   
   /* Method to determine if the system percolates (there
    * exists a path of open sites from the first row to the
    * bottom row).
    * @return true if the system percolates, false if it 
    *         does not.
    */
   public boolean percolates() {
      if (wQU.connected(getDimen(), getDimen() + 1))
         return true;
      return false;
   }
   
   // private accessor methods
   private int getRows() {
      return rows;
   }
   
   private int getColumns() {
      return columns;
   }
   
   private int getDimen() {
      return numberOfSites;
   }
   
   private int getAdjacentSeed() {
      return adjacentSeed;
   }
   
   private int getSeed() {
      return seed;
   }
   
   private void setRows(int i) {
      rows = i;
   }
   
   private void setColumns(int i) {
      columns = i;
   }
   
   private void setDimen(int i) {
      numberOfSites = i;
   }
   
   private void setAdjacentSeed(int i) {
      adjacentSeed = i;
   }
   
   private void setSeed(int i) {
      seed = i;
   }
   
   /* Method to convert the 2D position of the site to
    * it's corresponding 1D position in the array.
    * @param i row of site.
    * @param j column of site.
    * @return index of 2D site in the 1D array
    */
   private int xyTo1D(int i, int j) {
      int arrayPosition = N * (i - 1) + (j - 1);
      return arrayPosition;
   }
   
   
   /* Method to determine that the site chosen is a valid 
    * site or it does exist.
    * @param i row of site to validate.
    * @param j column of site to validate.
    * @throws IndexOutOfBoundsException unless row and
    *         column of site is between 1 and N.
    */
   private void validateIndices(int i, int j) {
      if (i < 1 || i > N) throw
         new IndexOutOfBoundsException("row index i out of bounds");
      if (j < 1 || j > N) throw
         new IndexOutOfBoundsException("column index j out of bounds");
   }
   
   /* Reads in the number of rows and columns (N).  Then, opens sites
    * read in from text file.  For example, 2 2 opens site in the 
    * second row and second column.  Finally, prints to standard
    * output if the system percolates.
    */
   public static void main(String[] args) {
      // Read in file
      In in = new In(args[0]);
      
      // Set rows and columns of 2D grid
      int N = in.readInt();
      
      // Create N X N Percolation object
      Percolation perc = new Percolation(N);
      
      // repeatedly open sites read from text file
      while (!in.isEmpty()) {
         int i = in.readInt();
         int j = in.readInt();
         perc.open(i, j);
      }
      
      // Does the system percolate?
      StdOut.print("It is " + perc.percolates() + " that the system"
                    + " percolates");
   }
}