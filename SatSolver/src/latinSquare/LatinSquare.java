package latinSquare;

import java.util.ArrayList;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.Literal;
import booleanFormula.Variables;
import solver.Solver;
import solver.SolverTimeoutException;
import tools.Tools;

public class LatinSquare {
	
	private CNF phi;
	private int size;
	private int[][] grid;
	
	public LatinSquare(int n) throws CNFException {
		this.size = n;
		this.grid = new int[n][n];
		CNF res = new CNF();
		Variables variables = new Variables(n*n*n);
		res.setVariables(variables);
		res.initLiterals();
		//first clauses for unicity.
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j ++) {
				int[] row = new int[n*n];
				int[] column = new int[n*n];
				for(int j2 = 0 ; j2 < n; j2++) {
					for(int k =0;k<n;k++) {
						row[j2*n+k] = indicesToInt(i, j2, k, n);
					}
				}
				for(int i2 = 0; i2 <n ; i2++) {
					for(int k = 0; k<n ; k++) {
						column[i2*n+k] = indicesToInt(i2, j, k, n);
					}
				}
				int[] rowcolumun = new int[row.length+column.length];
				System.arraycopy(row, 0, rowcolumun, 0, row.length);
				System.arraycopy(column, 0, rowcolumun, row.length, column.length);
				unicityClause(rowcolumun, res);
			}
		}
		
		this.phi = res;
	}
	
	
	
	/**
	 * add the clauses satisfied iff exactly only variable among 
	 * the variables is assigned to true
	 * @param variables
	 * @param unique_var
	 * @return
	 * @throws CNFException 
	 */
	public static void unicityClause(int[] variables, CNF phi) throws CNFException {
		Literal[] lit = phi.getLiterals();
		Clause one_true = new Clause();
		one_true.setFormula(phi);
		for(int i = 0;i < variables.length ; i++) {
			//this ensures at least one is true
			int var = variables[i];
			if(lit[var] == null) {
				lit[var] = new Literal(var,false);
			}
			one_true.addLiteral(var);
			lit[var].addClause(one_true.getId());
			phi.getVariables().addClause(one_true.getId(), var);
		}
		phi.addClause(one_true);
		
		for(int i = 0;i<variables.length-1;i++) {
			for(int j = i+1;j <variables.length;j++) {
				Clause c = new Clause();
				c.setFormula(phi);
				int id_neg1 = lit.length-1-variables[i];
				int id_neg2 = lit.length-1-variables[j];
				if(lit[id_neg1] == null) {
					lit[id_neg1] = new Literal(variables[i],true);
				}
				if(lit[id_neg2] == null) {
					lit[id_neg2] = new Literal(variables[j],true);
				}
				c.addLiteral(id_neg1);
				c.addLiteral(id_neg2);
				lit[id_neg1].addClause(c.getId());
				lit[id_neg2].addClause(c.getId());
				phi.getVariables().addClause(c.getId(), variables[i]);
				phi.getVariables().addClause(c.getId(), variables[j]);
				phi.addClause(c);
			}
		}	
	}
	
	//3D indices to unique int
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
		s.solve(this.phi);
		int[] assignment = s.getInterpretation();
		for(int i = 0; i < assignment.length ; i++) {
			if(assignment[i] == 1) {
				int x = i/(this.size*this.size);
				int y = (i/this.size)%this.size;
				int k = i-(x*this.size+y)*this.size+1;
				this.grid[x][y] = k;
			}
		}
	}
	
	@Override
	public String toString() {
		String res = "";
		int n = this.size;
		for(int i = 0; i <n ; i++) {
			res+= "-";
		}
		res += "\n";
		for(int i = 0; i<n;i++) {
			for(int j = 0;j<n; j++) {
				String val = " * ";
				if(j == 0) {
					res += "|";
				}
				for(int k = 0;k<n;k++) {
					if(phi.getInterpretation()[indicesToInt(i, j, k, n)] == 1) {
						val = " "+k+1 +" ";
						break;
					}
				}
				res += val;
				res += "|";
			}
			res += "\n";
			for(int i1 = 0; i1 <n ; i1++) {
				res+= "-";
			}
			res += "\n";
		}
		
		return res;
	}
	
	public String toString2() {
		String res = "";
		int n = this.size;
		for(int i = 0; i <n ; i++) {
			res+= "-";
		}
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
			for(int i1 = 0; i1 <n ; i1++) {
				res+= "-";
			}
			res += "\n";
		}
		
		return res;
	}
	public CNF getPhi() {
		return phi;
	}
	
}
