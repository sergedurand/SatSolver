package solver;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Variables;

public class SolverDPLL implements Solver {
	public boolean solved;
	public int[] interpretation;
	public CNF formula;
	
	public SolverDPLL() {
		this.solved = false;
		// TODO Auto-generated constructor stub
	}
	
	public SolverDPLL(CNF formula) {
		this.formula = formula;
		this.interpretation = this.formula.getVariables().getInterpretation();
	}
	
	/**
	 * does one iteration of unit propagation with current interpretation
	 */
	private void unitPropagation() {
		
	}
	@Override
	public Variables solve(CNF formula) throws CNFException {
		// TODO Auto-generated method stub
		return null;
	}

}
