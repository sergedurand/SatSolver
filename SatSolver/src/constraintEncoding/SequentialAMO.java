package constraintEncoding;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.Literal;
import booleanFormula.Variables;
import solver.SolverDPLL;
import solver.SolverTimeoutException;
import tools.Tools;

public class SequentialAMO implements AMO {

	@Override
	public void addConstraint(int[] variables, CNF phi) throws AMOException, CNFException {
		int n = variables.length;
		int nb_new = n-1;
		phi.addVariables(nb_new);
		int var_or = phi.getVariables().getSize();
		Literal[] literals = phi.getLiterals();
		int[] new_var_idx = new int[nb_new];
		for(int i = 0;i<nb_new;i++) {
			new_var_idx[i] = var_or +i;
		}
		//we start with X1 and S1
		Clause c1 = new Clause();
		c1.setFormula(phi);
		c1.addLiteral(literals.length-1-variables[0]);
		c1.addLiteral(new_var_idx[0]);
		phi.addClause(c1);

		//then Xn and Sn-1
		Clause c2 = new Clause();
		c2.setFormula(phi);
		c2.addLiteral(literals.length-1-variables[n-1]);
		c2.addLiteral(literals.length-1-new_var_idx[new_var_idx.length-1]);
		phi.addClause(c2);
		
		//then all clauses for 1<i<n:
		for(int i = 1;i<n-1;i++) {
			Clause cl1 = new Clause();
			cl1.setFormula(phi);
			cl1.addLiteral(literals.length-1-variables[i]);
			cl1.addLiteral(new_var_idx[i]);
			phi.addClause(cl1);
			Clause cl2 = new Clause();
			cl2.setFormula(phi);
			cl2.addLiteral(literals.length-1-new_var_idx[i-1]);
			cl2.addLiteral(new_var_idx[i]);
			phi.addClause(cl2);
			Clause cl3 = new Clause();
			cl3.setFormula(phi);
			cl3.addLiteral(literals.length-1-variables[i]);
			cl3.addLiteral(literals.length-1-new_var_idx[i-1]);
			phi.addClause(cl3);
		}
	}

	@Override
	public void test(int n) throws CNFException, AMOException, SolverTimeoutException {
		CNF phi = new CNF(new Variables(n));
		int[] variables = new int[n];
		for(int i =0;i<n;i++) {
			variables[i] = i;
		}
		phi.printStat();
		addConstraint(variables,phi);
		phi.printStat();
		System.out.println(phi);
		SolverDPLL s = new SolverDPLL();
		s.solve(phi);
		Tools.printInterpration(s.getInterpretation());
	}

}
