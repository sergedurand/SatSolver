package grecoLatin;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.Literal;
import booleanFormula.Variables;
import constraintEncoding.AMO;
import constraintEncoding.AMOException;
import solver.Solver;
import solver.SolverTimeoutException;

public class grecoLatin {
	int size;
	int[][][] grid;
	CNF phi;
	
	
	public grecoLatin(AMO a,int n) throws CNFException, AMOException {
		this.size = n;
		this.grid = new int[n][n][2];
		CNF res = new CNF();
		// n^3 variables for each square.
		Variables variables = new Variables(2*n*n*n + n*n*n*n);
		//from 0 to n^3 : encoding of first square
		//from n^3 to 2n^3 encoding of 2nd square
		//from 2n^3 to 2n^3 + n^4 = encoding of pairs : n^2 possible pairs for each cell
		res.setVariables(variables);
		res.initLiterals();
		//we ensure every cell is assigned 2 numbers
		for(int i = 0;i<n;i++) {
			for(int j = 0;j<n ;j++) {
				Clause c1 = new Clause();
				c1.setFormula(res);
				Clause c2 = new Clause();
				c2.setFormula(res);
				for(int k = 0;k<n;k++) {
					int lit_id = indicesToInt(i, j, k, n);
					c1.addLiteral(lit_id);
					res.getLiterals()[lit_id].addClause(c1.getId());
					res.getVariables().addClause(c1.getId(), lit_id);
					
				}
				res.addClause(c1);
			}
		}
		
		//2nd square
		for(int i = 0;i<n;i++) {
			for(int j = 0;j<n ;j++) {
				Clause c1 = new Clause();
				c1.setFormula(res);
				for(int k = 0;k<n;k++) {
					//first square
					int lit_id = n*n*n + indicesToInt(i, j, k, n);
					c1.addLiteral(lit_id);
					res.getLiterals()[lit_id].addClause(c1.getId());
					res.getVariables().addClause(c1.getId(), lit_id);
					
				}
				res.addClause(c1);
			}
		}
		
		//every row is a permutation
		for(int i = 0; i < n; i++) {
			for(int k = 0; k<n;k++) {
				int[] cur_row1 = new int[n];
				for(int j = 0;j<n ; j++) {
					cur_row1[j] = indicesToInt(i,j,k,n);
				}
				a.addConstraint(cur_row1, res);
				
			}
		}
		
		//2nd square
		for(int i = 0; i < n; i++) {
			for(int k = 0; k<n;k++) {
				int[] cur_row2 = new int[n];
				for(int j = 0;j<n ; j++) {
					cur_row2[j] = n*n*n + indicesToInt(i,j,k,n);
				}
				a.addConstraint(cur_row2, res);
				
			}
		}
		
		//every column is a permutation
		for(int j = 0;j<n;j++) {
			for(int k = 0;k<n;k++) {
				int[] cur_col1 = new int[n];
				for(int i = 0; i<n; i++) {
					cur_col1[i] = indicesToInt(i,j,k,n);
				}
				a.addConstraint(cur_col1, res);
			}
		}
		
		//2nd square
		for(int j = 0;j<n;j++) {
			for(int k = 0;k<n;k++) {
				int[] cur_col2 = new int[n];
				for(int i = 0; i<n; i++) {
					cur_col2[i] = n*n*n + indicesToInt(i,j,k,n);
				}
				a.addConstraint(cur_col2, res);
			}
		}
		
		
		for(int i = 0;i<n;i++) {
			for(int j = 0;j<n;j++) {
				for(int k1 = 0;k1<n;k1++) {
					for(int k2 = 0;k2<n;k2++) {
						//encoding coherence between single variables and pair variables:
						//A[i,j,k1] ^ B[i,j,k2] => C[i,j,k1,k2]
						Literal l1 = res.getLiterals()[res.getLiterals().length-1-indicesToInt(i, j, k1, n)];
						Literal l2 = res.getLiterals()[res.getLiterals().length-1-(n*n*n + indicesToInt(i, j, k2, n))];
						Literal l3 = res.getLiterals()[pairIndicesToInt(i, j, k1,k2, n)];
						Clause c = new Clause ();
						c.setFormula(res);
						l1.addClause(c.getId());
						l2.addClause(c.getId());
						l3.addClause(c.getId());
						res.getVariables().addClause(c.getId(), l1.getId());
						res.getVariables().addClause(c.getId(), l2.getId());
						res.getVariables().addClause(c.getId(), l3.getId());
						c.addLiteral(res.getLiterals().length-1-indicesToInt(i, j, k1, n));
						c.addLiteral(res.getLiterals().length-1-(n*n*n + indicesToInt(i, j, k2, n)));
						c.addLiteral(pairIndicesToInt(i, j, k1,k2, n));
						res.addClause(c);
					}
				}
			}
		}
		
		//encoding at most one pair using AMO generator
		for(int k1 = 0;k1<n;k1++) {
			for(int k2 = 0;k2<n;k2++) {
				int[] cur_pair = new int[n*n];
				for(int i = 0;i<n;i++) {
					for(int j = 0; j<n;j++) {
						cur_pair[i*n+j] = pairIndicesToInt(i, j, k1, k2, n);
					}
				}
				a.addConstraint(cur_pair, res);
			}
		}
		
		this.phi = res;
	}
	
	public void solveSquare(Solver s) throws SolverTimeoutException, CNFException {
		s.solve(this.phi,30);
		s.printRes();
		int[] assignment = s.getInterpretation();
		for(int i = 0; i < this.size*this.size*this.size ; i++) {
			if(assignment[i] == 1) {
				int x = i/(this.size*this.size)+1;
				int y = (i/this.size)%this.size+1;
				int k = i-((x-1)*this.size+(y-1))*this.size+1;
				this.grid[x-1][y-1][0] = k;
			}
			
			if(assignment[this.size*this.size*this.size + i] == 1) {
				int x = i/(this.size*this.size)+1;
				int y = (i/this.size)%this.size+1;
				int k = i-((x-1)*this.size+(y-1))*this.size+1;
				this.grid[x-1][y-1][1] = k;
			}
		}
	}
	
	public static int indicesToInt(int i, int j, int k, int size) {
		return i*size*size+j*size+k;
	}
	
	public static int pairIndicesToInt(int i, int j, int k1, int k2, int size) {
		return 2*size*size*size + i*size*size*size +j*size*size +k1*size + k2;
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
	
	@Override
	public String toString() {
		int width = this.size*3 + 2*this.size+1;
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
				res += " " + (char)((int)'A' + this.grid[i][j][0]) + (char)((int)'a' + this.grid[i][j][1]) + " ";
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
	
	public CNF getPhi() {
		return phi;
	}
	
	public void printStat() {
		phi.printStat();
	}

}
