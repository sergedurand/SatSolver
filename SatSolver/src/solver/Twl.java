package solver;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;

public class Twl implements Solver {
	boolean solved = false;
	int[] interpretation;

	@Override
	public boolean solve(CNF phi,int timeout) throws CNFException, SolverTimeoutException {
		long start_time = System.nanoTime();
		
		//we first get rid of preexisting unit clauses
		keepPropagating(phi); 
		if(phi.hasEmptyClause()) {
			System.out.println("trivially false");
			this.solved = false;
			return false;
		}
		
		phi.initWatchedLiterals();//since we got rid of unit clauses we're sure all active clauses are watched by 2 literals
		HashSet<Integer> VariablesLeft = new HashSet<Integer>(phi.getUnassigned());
		LinkedList<Integer> VariablesExplored = new LinkedList<Integer>();
		
		int var;
		int val;
		boolean conflict;
		while(true) {
			try {
				var = VariablesLeft.iterator().next();
				VariablesLeft.remove(var);
			}catch(NoSuchElementException e){
				this.solved = true;
				this.interpretation = phi.getInterpretation();
				return true;
			}
			
			val = 0;
			phi.getVariables().setVal(var, val);
			VariablesExplored.add(-1);
			VariablesExplored.add(var);
			int lit_id = var;
			boolean backtracking = false;
			LinkedList<Integer> Learned = new LinkedList<Integer>();
			while(true) {
				LinkedList<Integer> TempLearned = unitPropagate(lit_id,phi,VariablesExplored);
				if(TempLearned.size()==0) {
					//nothing learned, no conflict
					break;
				}
				if(TempLearned.getLast() == -1) {
					TempLearned.removeLast();
					Learned.addAll(TempLearned);
					backtracking = true;
					break;
				}else {
					lit_id = Learned.removeFirst();
				}
			}
			if(backtracking) {
				
			}
			
			
		}
		


	}
	
	public LinkedList<Integer> unitPropagate(int lit_id,CNF phi,LinkedList<Integer> VariablesExplored) throws CNFException {
		//we update variables explored with the learned variable
		//we return the literals made unsat by the learning
		LinkedList<Integer> res = new LinkedList<Integer>();
		for(int cl_id : phi.getWatched_literals()[lit_id].getClauses()) {
			Clause c = phi.getClauses().get(cl_id);
			for(int l2 : c.getLiterals()) {
				if(l2 == lit_id) {continue;}
				if(((phi.getLiterals()[l2].getVal()!=0)) && !phi.getWatched_literals()[l2].getClauses().contains(cl_id)){
							//we found another unassigned literal that is not watched
							phi.getWatched_literals()[l2].getClauses().add(cl_id);
							phi.getWatched_literals()[lit_id].removeClause(cl_id);
							break; //moving to next clause
						}else if (phi.getWatched_literals()[l2].getClauses().contains(cl_id)) {
							//we found the other watched literal
							if(phi.getWatched_literals()[l2].getVal() == 1) {
								break; //moving to next clause
							}else if(phi.getWatched_literals()[l2].getVal() == -1) {
								//we are in a unit clause and can satisfy the second watched literal
								VariablesExplored.add(phi.getLiterals()[l2].getId());
								if(l2>= phi.getVariables().getSize()) {
									res.add(phi.getLiterals()[l2].getId()); 
									phi.getVariables().setVal(phi.getLiterals()[l2].getId(), 0);
								}else {
									res.add(phi.getLiterals().length-1-l2);
									phi.getVariables().setVal(phi.getLiterals()[l2].getId(), 1);
								}	
							}else {
								//-1 will signal a conflict was found
								res.add(-1);
								return res;
							}
						}
			}
		}
		
		return res;
	}
	
	public void deactivateSatClausesTwl(int var_id,CNF phi) throws CNFException {
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
	
	public LinkedList<Integer> keepPropagating(CNF phi) throws CNFException{
		LinkedList<Integer> var_learned = new LinkedList<Integer>();
		boolean empty = false;
		int[] assigned_var = {-1};
		while(true) {
			assigned_var = unitPropagation(phi);
			if(assigned_var[0] == -1) {
				break;
			}
			var_learned.add(assigned_var[0]);
			phi.getVariables().setVal(assigned_var[0], assigned_var[1]);
			deactivateSatClauses(assigned_var[0], phi);
			empty = phi.hasEmptyClause();
			if(empty) {
				break;
			}
		}
		return var_learned;
	}
	
	public LinkedList<Integer> keepPropagatingTwl(CNF phi,int lit_id) throws CNFException{
		LinkedList<Integer> var_learned = new LinkedList<Integer>();
		boolean empty = false;
		int[] assigned_var = {-1};
		while(true) {
			assigned_var = unitPropagation(phi);
			if(assigned_var[0] == -1) {
				break;
			}
			var_learned.add(assigned_var[0]);
			phi.getVariables().setVal(assigned_var[0], assigned_var[1]);
			deactivateSatClauses(assigned_var[0], phi);
			empty = phi.hasEmptyClause();
			if(empty) {
				break;
			}
		}
		return var_learned;
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
