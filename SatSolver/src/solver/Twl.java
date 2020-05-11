package solver;

import java.util.Set;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;

public class Twl implements Solver {

	@Override
	public boolean solve(CNF formula, int timeout) throws CNFException, SolverTimeoutException {
			return false;
	}
	
	public void deactivateSatClauses(int var_id,CNF phi) throws CNFException {
		Set<Integer> clauses_id = phi.getActiveClauses();
		int lit_pos;
		int lit_neg;
		//we get the index of the positive and negative literal according to the variable valuation
		if(phi.getInterpretation()[var_id] == 1) {
			lit_pos = var_id;
			lit_neg = phi.getLiterals().length-1-var_id;
		}else if(phi.getInterpretation()[var_id] == 0) {
			lit_pos = phi.getLiterals().length-1-var_id;
			lit_neg = var_id;
		}else {
			throw new CNFException("Trying to remove clauses with an unassigned variable : " + var_id); 
		}
		
		//every clauses containing literal[lit_pos] is satisfied, we can remove them
		phi.deactivateSetClause(phi.getLiterals()[lit_pos].getClauses());
		
		//we remove every negative literals
		clauses_id = phi.getActiveClauses();
		for(int id : clauses_id) {
			phi.getClauses().get(id).deactivateLiteral(lit_neg);
		}
	}
	
	/**
	 * Check if there are any unit clauses. 
	 * If there are returns variable ID and assignment
	 * If there aren't returns [-1];
	 * @param phi
	 * @return
	 */
	public int[] unitPropagation(CNF phi) {
		int[] res = {-1};
		Set<Integer> clauses_id = phi.getActiveClauses();
		for(int id : clauses_id) {
			Clause c = phi.getClauses().get(id);
			if(c.isUnit()) {
				int lit_id = c.getLiterals().iterator().next();
				int var_id = phi.getLiterals()[lit_id].getId();
				int val = 1;
				if(phi.getLiterals()[lit_id].isNeg()) {
					val = 0;
				}
				res = new int[2];
				res[0] = var_id;
				res[1] = val;
				return res;
			}
		}
		return res;	
	}

	@Override
	public void printRes() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSolver(CNF Formula) {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getInterpretation() {
		// TODO Auto-generated method stub
		return null;
	}

}
