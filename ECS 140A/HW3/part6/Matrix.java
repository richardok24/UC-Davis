
class Matrix extends Sequence{
	private int rowsize;
	private int colsize;
	private Sequence row;
	// constructor for creating a matrix of specific number of rows and columns
	public Matrix(int rowsize, int colsize) {
		this.rowsize = rowsize;
		this.colsize = colsize;
		row = new Sequence();
		for(int j=0;j<rowsize;j++) {
			Sequence col = new Sequence();
			row.add(col,j);
		}
		for(int j =0;j<rowsize;j++) {
			for(int k=0;k<colsize;k++)
				((Sequence) row.index(j)).add(new MyInteger(0),k);
		}
		
	}
	
	public void Set(int row, int col, int value) {
		// set the value of an element
		Sequence therow = (Sequence) this.row.index(row);
		((MyInteger) therow.index(col)).Set(value);
	}
	public int Get(int row, int col) {
		// get the value of an element
		if(row>rowsize || col > colsize) return -1;
		Sequence colSq = (Sequence) this.row.index(row);
		return ((MyInteger) colSq.index(col)).Get();
		//return -1 means cant find it;
	}
	
	public Matrix Sum(Matrix mat) {
		// return the sum of two matrices: mat & this
		if(rowsize != mat.rowsize || colsize != mat.colsize) {
	  		System.out.println("Matrix dimensions incompatible for Sum");
	  		System.exit(1);
	  	}
		Matrix newMat = new Matrix(rowsize,colsize);
		for(int j =0;j<rowsize;j++) {
			for(int k=0;k<colsize;k++) {
				int sum = this.Get(j, k)+mat.Get(j, k);
				newMat.Set(j,k,sum);
			}
		}
		return newMat;
	}
	public Matrix Product(Matrix mat) {
		// return the product of two matrices: mat & this
		if(mat.rowsize != colsize) {
	  		System.out.println("Matrix dimensions incompatible for product");
	  		System.exit(1);
	  	}
		Matrix newMat = new Matrix(rowsize,mat.colsize);
		for(int j =0;j<newMat.rowsize;j++) {
			for(int k=0;k<newMat.colsize;k++) {
				int prod = 0;
				for(int i=0;i<mat.rowsize;i++) {
					prod +=this.Get(j, i)*mat.Get(i, k);
				}
				
				newMat.Set(j,k,prod);
			}
		}
		return newMat;
	}
	public void Print() {
		//  print the elements of matrix
		for(int i=0;i<rowsize;i++) {
			row.index(i).Print();
			System.out.println();
		}
	}
}
