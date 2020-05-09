package constraintEncoding;

import java.util.ArrayList;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.Literal;
import booleanFormula.Variables;
import solver.SolverDPLL;
import solver.SolverTimeoutException;
import tools.Tools;
/**
 * this class implements the "naive" pairwise encoding for AMO constraint
 * @author serge
 *
 */
public class NaiveAMO implements AMO {

	@Override
	public void addConstraint(int[] variables,CNF phi) throws AMOException, CNFException{
		// TODO Auto-generated method stub
		int n = variables.length;
		ArrayList<Clause> clauses = new ArrayList<Clause>();
		Literal[] literals = phi.getLiterals();
		Variables phi_var = phi.getVariables();
		for(int i = 0;i<n-1;i++) {
			for(int j = i+1;j<n;j++) {
				Clause c = new Clause();
				int idx1 = variables[i];
				int idx2 = variables[j];
				if(idx1 >= phi_var.getSize()) {
					throw new AMOException("Variable " + idx1 +" not in the variables of the formula");
				}
				if(idx2 >= phi_var.getSize()) {
					throw new AMOException("Variable " + idx2 +" not in the variables of the formula");
				}
				c.setFormula(phi);
				c.addLiteral(literals.length-1-idx1);
				c.addLiteral(literals.length-1-idx2);
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
		addConstraint(variables,phi);
		SolverDPLL s = new SolverDPLL();
		System.out.println(phi);
		s.solve(phi);
		Tools.printInterpration(s.getInterpretation());
	}

}
