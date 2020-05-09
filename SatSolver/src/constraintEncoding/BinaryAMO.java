package constraintEncoding;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.Literal;
import booleanFormula.Variables;
import solver.SolverDPLL;
import solver.SolverTimeoutException;
import tools.Tools;

public class BinaryAMO implements AMO {

	@Override
	public void addConstraint(int[] variables, CNF phi) throws AMOException, CNFException {
		//we create log(n) variables and add them to the formula
		int n = variables.length;
		int nb_new = (int) Math.ceil((Math.log((variables.length))/Math.log(2)));
		int var_or = phi.getVariables().getSize();
		phi.addVariables(nb_new);
		Literal[] literals = phi.getLiterals();
		int[] new_var_idx = new int[nb_new];
		for(int i = 0;i<nb_new;i++) {
			new_var_idx[i] = var_or +i;
		}
		
		for(int i = 0;i<n;i++) {
			for(int j = 0;j<nb_new;j++) {
				int val = (int) getBit(j,(long)i);
				Clause c = new Clause();
				c.setFormula(phi);
				int idx1 = variables[i];
				c.addLiteral(literals.length-1-idx1);
				int idx2 = new_var_idx[j];
				if(val == 1) {
					c.addLiteral(idx2);
				}else {
					c.addLiteral(literals.length-1-idx2);
				}
				phi.addClause(c);
			}
		}
		
		
	}

	@Override
	public void test(int n) throws CNFException, AMOException, SolverTimeoutException {
		CNF phi = new CNF(new Variables(n));
		int[] variables = new int[n];
		for(int i =0;i<n;i++) {
			variables[i] = i;
		}
		//we ensure every cell is assigned a number
		Clause c = new Clause();
		c.setFormula(phi);
		for(int i = 0;i<n;i++) {
			c.addLiteral(i);
			}
		phi.addClause(c);
		addConstraint(variables,phi);
		System.out.println(phi);
		SolverDPLL s = new SolverDPLL();
		s.solve(phi);
		Tools.printInterpration(s.getInterpretation());
	}
	
	/**
	 * returns the jth bit of n
	 * @param j
	 * @param n
	 * @return
	 */
	public static long getBit(int j, long n) {
		return ((n >>>  (j)) & 1);
	}

}
