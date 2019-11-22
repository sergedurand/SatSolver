package sudoku;

/**
 * @author Serge
 *
 *This class handles Sudoku: display, reading from a file, converting to a SAT-problem
 */
public class Sudoku {
	
	private int[][] grid = new int[9][9];
	private int[][][] squares = new int[9][3][3]; //redundant with the grid but helpful to test validity
	
	
	
	/**
	 * classic constructor
	 * @param grid
	 * @param squares
	 */
	public Sudoku(int[][] grid, int[][][] squares) {
		super();
		this.grid = grid;
		this.squares = squares;
	}


	/**
	 * Constructor from an array of integer. This only test size of input.
	 * Doesn't test if input is a valid Sudoku.
	 * Builds the 9x9 grid and 9 3x3 matrices for the squares
	 * @param list_int
	 * @throws SudokuException
	 */
	public Sudoku(int[] list_int) throws SudokuException{
		if(list_int.length != 81) {throw new SudokuException("the size of input is incorrect. It should be 81");}
		for(int i = 0;i<81;i++) {
			int x = i/9;
			int y = i%9;
			this.grid[x][y] = list_int[i];
			
			//filling the squares
			if(x<3) {
				if(y<3) {
					this.squares[0][x][y] = list_int[i];
				}
				else if(y<6) {
					y = y-3;
					this.squares[1][x][y] = list_int[i];
				}
				else {
					y = y-6;
					this.squares[2][x][y] = list_int[i];
				}
			}else if(x<6) {
				x = x-3;
				if(y<3) {
					this.squares[3][x][y] = list_int[i];
				}
				else if(y<6) {
					y = y-3;
					this.squares[4][x][y] = list_int[i];
				}
				else {
					y = y-6;
					this.squares[5][x][y] = list_int[i];
				}
			}
			else {
				x = x-6;
				if(y<3) {
					this.squares[6][x][y] = list_int[i];
				}
				else if(y<6) {
					y = y-3;
					this.squares[7][x][y] = list_int[i];
				}
				else {
					y = y-6;
					this.squares[8][x][y] = list_int[i];
				}
			}
			
		}
	}
	
	

	public int[][] getGrid() {
		return grid;
	}


	public void setGrid(int[][] grid) {
		this.grid = grid;
	}


	public int[][][] getSquares() {
		return squares;
	}


	public void setSquares(int[][][] squares) {
		this.squares = squares;
	}


	/**
	 * Display the Sudoku as a grid:
	 * ------------- 
	 *|728|233|308|
	 *|038|722|300|
	 *|424|705|240|
	 *------------- 
	 *|423|321|141|
	 *|434|304|181|
	 *|083|620|458|
	 *------------- 
	 *|648|274|452|
	 *|475|484|302|
	 *|240|430|555|
	 *------------- 
	 */
	@Override
	public String toString() {
		
		String res = "------------- \n";
		for(int i=0;i<9;i++) {
			res += "|";
			for(int j = 0;j<9;j++) {
				res+= grid[i][j];
				if((j+1)%3 == 0) {res += "|";}
			}
			res +="\n";
			if((i+1)%3 == 0){res += "------------- \n";}
		}
		
		
		return res;
	}
	
	/**
	 * Return true if the cell at [x][y] is empty. 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isEmpty(int x,int y) {
		return (this.grid[x][y] == 0);
	}
	
	/**
	 * subroutine for checking validity of full grid
	 * @param line
	 * @return
	 */
	private boolean lineIsValid(int[] line) {
		int total = 0;
		for(int i = 0;i<9;i++) {
			if(line[i] == 0) {return false;}
			total += line[i];
		}
		return (total == 45);
	}
	
	/**
	 * subroutine for checking validity of full grid
	 * @param square
	 * @return
	 */
	private boolean squareIsValid(int[][] square) {
		int total = 0;
		for(int i =0;i<3;i++) {
			for(int j= 0;j<3;j++) {
				if(square[i][j] == 0) {return false;}
				total += square[i][j];
			}
		}
		return(total == 45);
	}
	
	/**
	 * subroutine for checking validity of full grid
	 * @param c
	 * @param matrix
	 * @return
	 */
	private boolean columnIsValid(int c, int[][] matrix) {
		int total = 0;
		for(int i = 0;i<9;i++) {
			if(matrix[i][c] == 0) {return false;}
			total += matrix[i][c];
		}
		
		return (total == 45);
	}
	
	/**
	 * return true if the grid is a full valid Sudoku
	 * @return
	 */
	public boolean isValid() {
		
		for(int i = 0;i<9;i++) {
			if(!lineIsValid(grid[i])) {return false;}
			if(!squareIsValid(squares[i])) {return false;}
			if(!columnIsValid(i, this.grid)) {return false;}
		}
		
		return true;
	}
	
	


		
}
