/**
 * The class is a data type for Perculation system
 * 
 * @author Igal Israilevich
 */
public class Percolation {

 int[][] arrStatus;
 WeightedQuickUnionUF uf;
 //QuickFindUF uf;
 int sideSize;
 int topRoot;
 int[] bottomRootsArr;
 int bottomRootLow;
 int bottomRootHigh;

 // create N-by-N grid, with all sites blocked
 // blocked = 0
 // open = 1

 public Percolation(int N) {
  arrStatus = new int[N][N];
  int UF_N = N * N + N + 1;
  uf = new WeightedQuickUnionUF(UF_N);
  // uf = new QuickFindUF(UF_N);
  sideSize = N;
  bottomRootsArr = new int[N];
  bottomRootLow = N*N;
  bottomRootHigh = bottomRootLow + N;
  int j = 0;
  for( int i = bottomRootLow; i<bottomRootHigh;i++){   
      bottomRootsArr[j++] = uf.find(i);
  }
  
  topRoot = UF_N - 1;
 }

 public void open(int i, int j) {
     openPrivate(i - 1, j - 1);
 }

 /**
  * open site (row i, column j) if it is not open already
  */
 public void openPrivate(int i, int j) {
     arrStatus[i][j] = 1;
     unit(i, j);
 }

 public boolean isOpen(int i, int j) {
  return isOpenPrivate(i - 1, j - 1);
 }

 // is site (row i, column j) open?
 private boolean isOpenPrivate(int i, int j) {
     return arrStatus[i][j] == 1;
 }

 public boolean isFull(int i, int j) {
     return isFullPrivate(i - 1, j - 1);
 }

 // is site (row i, column j) full?
 private boolean isFullPrivate(int i, int j) {
     int ufValue = uf.find(findIntoUF(i, j)); 
     return ufValue == getUfTopValue() && isOpenPrivate(i, j);
 }

 // does the system percolate?
 public boolean percolates() {
  for(int i = 0; i < bottomRootsArr.length;i++){
      if(uf.find(bottomRootsArr[i]) == getUfTopValue()){
          return true;
      }
  }
  return false;
 }

 private void unit(int i, int j) {
  // find arrStatus place into UF
  int ufPlace = findIntoUF(i, j);
  // check upper and bottom
  checkRowLimits(i,j,ufPlace);
  // check left adjacent neighbor
  validateAndUnit(i, j - 1, ufPlace);
  // check right adjacent neighbor
  validateAndUnit(i, j + 1, ufPlace);
  // check upper
  validateAndUnit(i + 1, j, ufPlace);
  // check down
  validateAndUnit(i - 1, j, ufPlace);
  
 }

 private void validateAndUnit(int i, int j, int ufPlace) {
     if (validateBounds(i, j) && isOpenPrivate(i, j)) {
         uf.union(ufPlace, findIntoUF(i, j));
     }

 }
  
 private void checkRowLimits(int i, int j, int ufPlace){
     if(i == 0){
         uf.union(ufPlace, topRoot);
     }
     else if(i == sideSize - 1){
         int bottomRoot = findIntoUF(i+1, j);
         uf.union(ufPlace, uf.find(bottomRoot));
     }
 }

 private int findIntoUF(int i, int j) {
     return i * sideSize + j;
 }

 private boolean validateBounds(int i, int j) {
     if (i < 0 || i > sideSize - 1) {
         return false;
     }
     if (j < 0 || j > sideSize - 1) {
         return false;
     }
     return true;
 }

 private int getUfTopValue() {
     return uf.find(topRoot);
 }

}
