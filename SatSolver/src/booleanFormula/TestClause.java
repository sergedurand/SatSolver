package booleanFormula;

import solver.SolverTimeoutException;

public class TestClause {
	public static void main(String[] args) throws CNFException, SolverTimeoutException {
		CNF res = new CNF();
		Clause c = new Clause();
		c.setFormula(res);
		int[] variables = {-1,-1,-1};
		Variables var = new Variables(variables);
		res.setVariables(var);
		Literal l = new Literal();
		l.setFormula(res);
		l.setId(1);
		l.setNeg(false);
		Literal[] literals = new Literal[var.getSize()*2];
		literals[1] = l;
		res.setLiterals(literals);
		c.addLiteral(l.getId());
		res.addClause(c);
		for(int i = 0; i<c.getLiterals().size();i++) {
			System.out.println(c.getLiterals().get(i));
		}
		Clause c2 = c.clone();
		c2.setFormula(res);
		System.out.println(c2);

	}

}
