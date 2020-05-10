package latinSquare;

import java.util.ArrayList;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.Literal;
import booleanFormula.Variables;
import constraintEncoding.AMO;
import constraintEncoding.AMOException;
import solver.Solver;
import solver.SolverTimeoutException;
import tools.Tools;

public class LatinSquare {
	
	private CNF phi;
	private int size;
	private int[][] grid;
	
	/**
	 * Construct a CNF encoding the existence of a latin square of size n
	 * uses naive pairwise clauses for the unicity in each row and column
	 * @param n
	 * @throws CNFException
	 */
	public LatinSquare(int n) throws CNFException {
		this.size = n;
		this.grid = new int[n][n];
		CNF res = new CNF();
		Variables variables = new Variables(n*n*n);
		res.setVariables(variables);
		res.initLiterals();
		//we ensure every cell is assigned a number
		for(int i = 0;i<n;i++) {
			for(int j = 0;j<n ;j++) {
				Clause c = new Clause();
				c.setFormula(res);
				for(int k = 0;k<n;k++) {
					int lit_id = indicesToInt(i, j, k, n);
					c.addLiteral(lit_id);
					res.getLiterals()[lit_id].addClause(c.getId());
					res.getVariables().addClause(c.getId(), lit_id);
				}
				res.addClause(c);
			}
		}
		
		//every row is a permutation
		for(int i = 0; i < n; i++) {
			for(int k = 0; k<n;k++) {
				for(int j1 = 0; j1<n-1 ;j1++) {
					for(int j2 = j1+1;j2<n;j2++) {
						Clause c = new Clause();
						c.setFormula(res);
						int idx1 = indicesToInt(i, j1, k, n);
						int idx2 = indicesToInt(i, j2, k, n);
						c.addLiteral(res.getLiterals().length-1-idx1);
						res.getLiterals()[res.getLiterals().length-1-idx1].addClause(c.getId());
						res.getVariables().addClause(c.getId(), idx1);
						c.addLiteral(res.getLiterals().length-1-idx2);
						res.getLiterals()[res.getLiterals().length-1-idx2].addClause(c.getId());
						res.getVariables().addClause(c.getId(), idx2);
						res.addClause(c);
					}	
				}			
			}
		}
		
		//every column is a permutation
		for(int j = 0;j<n;j++) {
			for(int k = 0;k<n;k++) {
				for(int i1 = 0; i1 <n-1;i1 ++){
					for(int i2 = i1+1; i2<n ; i2++) {
						Clause c = new Clause();
						c.setFormula(res);
						int idx1 = indicesToInt(i1, j, k, n);
						int idx2 = indicesToInt(i2, j, k, n);
						c.addLiteral(res.getLiterals().length-1-idx1);
						res.getLiterals()[res.getLiterals().length-1-idx1].addClause(c.getId());
						res.getVariables().addClause(c.getId(), idx1);
						c.addLiteral(res.getLiterals().length-1-idx2);
						res.getLiterals()[res.getLiterals().length-1-idx2].addClause(c.getId());
						res.getVariables().addClause(c.getId(), idx2);
						res.addClause(c);
					}
				}
			}
		}
		this.phi = res;
	}
	
	public static int indicesToInt(int i, int j, int k, int size) {
		return i*size*size+j*size+k;
	}
	
	public static int[] intToIndices(int id, int size){
		int i = id/(size*size) +1;
		int j = (id/size)%size+1;
		int k = id-((i-1)*size+(j-1))*size +1;
		int[] res = {i,j,k};
		return res;
	}
	
	public static String indicesToString(int[] indices) {
		int i = indices[0];
		int j = indices [1];
		int k = indices [2];
		String res = "";
		return res + i + "" + j + "" + k;
	}
	
	public void solveSquare(Solver s) throws SolverTimeoutException, CNFException {
		s.solve(this.phi,10);
		int[] assignment = s.getInterpretation();
		for(int i = 0; i < this.size*this.size*this.size ; i++) {
			if(assignment[i] == 1) {
				int x = i/(this.size*this.size)+1;
				int y = (i/this.size)%this.size+1;
				int k = i-((x-1)*this.size+(y-1))*this.size+1;
				this.grid[x-1][y-1] = k;
			}
		}
	}
	
	/**
	 * this constructor use an at most one clause generator
	 * @param a
	 * @param n
	 * @throws CNFException 
	 * @throws AMOException 
	 */
	public LatinSquare(AMO a, int n) throws CNFException, AMOException {
		this.size = n;
		this.grid = new int[n][n];
		CNF res = new CNF();
		Variables variables = new Variables(n*n*n);
		res.setVariables(variables);
		res.initLiterals();
		//we ensure every cell is assigned a number
		for(int i = 0;i<n;i++) {
			for(int j = 0;j<n ;j++) {
				Clause c = new Clause();
				c.setFormula(res);
				for(int k = 0;k<n;k++) {
					int lit_id = indicesToInt(i, j, k, n);
					c.addLiteral(lit_id);
					res.getLiterals()[lit_id].addClause(c.getId());
					res.getVariables().addClause(c.getId(), lit_id);
				}
				res.addClause(c);
			}
		}
		
		//every row is a permutation
		for(int i = 0; i < n; i++) {
			for(int k = 0; k<n;k++) {
				int[] cur_row = new int[n];
				for(int j = 0;j<n ; j++) {
					cur_row[j] = indicesToInt(i,j,k,n);
				}
				a.addConstraint(cur_row, res);			
			}
		}
		
		//every column is a permutation
		for(int j = 0;j<n;j++) {
			for(int k = 0;k<n;k++) {
				int[] cur_col = new int[n];
				for(int i = 0; i<n; i++) {
					cur_col[i] = indicesToInt(i,j,k,n);
				}
				a.addConstraint(cur_col, res);
			}
		}
		this.phi = res;
	}
	
	public void updateGrid() {
		int[] assignment = phi.getInterpretation();
		for(int i = 0; i < this.size*this.size*this.size ; i++) {
			if(assignment[i] == 1) {
				int x = i/(this.size*this.size)+1;
				int y = (i/this.size)%this.size+1;
				int k = i-((x-1)*this.size+(y-1))*this.size+1;
				this.grid[x-1][y-1] = k;
			}
		}
	}
	
	@Override
	public String toString() {
		int width = this.size*3 + this.size+1;
		String res = "";
		for(int i = 0;i<width;i++) {
			res+="-";
		}
		int n = this.size;
		res += "\n";
		for(int i = 0; i<n;i++) {
			for(int j = 0;j<n; j++) {
				if(j == 0) {
					res += "|";
				}
				res += " " + this.grid[i][j] + " ";
				res += "|";

			}
			res += "\n";
			for(int i1 = 0;i1<width;i1++) {
				res+="-";
			}
			res += "\n";
		}
		
		return res;
	}
	
	/**
	 * Set the value in coordinates (i,j) to k
	 * @param i
	 * @param j
	 * @param k
	 * @throws CNFException 
	 */
	public void setVal(int i, int j,int k) throws CNFException {
		this.grid[i][j] = k+1;
		int var = indicesToInt(i, j, k, this.size);
		this.phi.getVariables().setVal(var, 1);
	}
	public CNF getPhi() {
		return phi;
	}
	
	public void printStat() {
		phi.printStat();
	}
	
}
