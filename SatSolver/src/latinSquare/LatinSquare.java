package latinSquare;

import java.util.ArrayList;

import booleanFormula.CNF;
import booleanFormula.Clause;
import booleanFormula.Literal;

public class LatinSquare {
	
	private CNF phi;
	
	public LatinSquare(int n) {
		CNF res = new CNF();
		int[][][] var = new int[n][n][n];
		
		//first clauses for unicity.
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j ++) {
				for(int k = 0;k<n;k++) {
					
				}
			}
		}
	}
	
	private ArrayList<Clause> unicityClause(int i, int j, int k, int n) {
		ArrayList<Clause> res = new ArrayList<Clause>();
		for(int x = 0; x < n; x++) {
			if(x == k) {continue;};
			Clause c = new Clause();
			Literal l = new Literal();
			
			
		}
	}

}
