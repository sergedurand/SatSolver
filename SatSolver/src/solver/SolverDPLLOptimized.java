package solver;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;

public class SolverDPLLOptimized implements Solver {
	private int[] interpretation;
	boolean solved = false;

	public SolverDPLLOptimized(){
		super();
	}
	
	public SolverDPLLOptimized(CNF phi) {
		this();
	}
	
	
	@Override
	public boolean solve(CNF phi,int timeout) throws CNFException, SolverTimeoutException {
		long start_time = System.nanoTime();
		LinkedList<Integer> VariablesLeft = phi.getUnassigned();
		
		//some preprocessing: in the case the interpretation has already assigned values
		//update the formula to add corresponding unit clause
		for(int i = 0;i<phi.getVariables().getSize();i++) {
			if(phi.getVariables().getVal(i)==0) {
				Clause c = new Clause();
				c.setFormula(phi);
				c.addLiteral(phi.getLiterals().length-1-i);
				phi.addClause(c);
				phi.getLiterals()[phi.getLiterals().length-1-i].addClause(c.getId());
				phi.getVariables().addClause(c.getId(), i);
			}else if(phi.getVariables().getVal(i)==1) {
				Clause c = new Clause();
				c.setFormula(phi);
				c.addLiteral(i);
				phi.getLiterals()[i].addClause(c.getId());
				phi.getVariables().addClause(c.getId(), i);
				phi.addClause(c);
			}
		}
		
		// stack to be used when backtracking
		LinkedList<Integer> VariablesExplored = new LinkedList<Integer>();
		int var = -1;
		boolean backtracking = false;
		long elapsed_time;
		while(true) {
			elapsed_time = (System.nanoTime()-start_time)/1_000_000_000;
			if(elapsed_time > timeout) {
				throw new SolverTimeoutException("Over "+ timeout + " seconds");
			}
			int val;
			int[] assigned_var = {0};
			boolean empty = false;
			if(!backtracking) {
				//case of a fresh variable: start from val = 0
				try {
					var = VariablesLeft.pop();
				}catch(NoSuchElementException e) {
					//we have no more variables left and haven't found a conflict, it's SAT
					//in the weird case the formula is empty with no variable:
					if(phi.hasEmptyClause()) {
						this.solved = false;
						return false;
					}
					this.solved = true;
					this.interpretation = phi.getInterpretation();
					return true;
				}
				//we have a variable to check.
				val = 0;
				phi.getVariables().setVal(var, val);
				//if it fails we'll look for the other branch: the assignment to 1
				VariablesExplored.add(-1);
				VariablesExplored.add(var);
				//we run unit propagation until impossible;
				assigned_var = new int[1];
				assigned_var[0] = -2;
				empty = false;
				while(true) {
					elapsed_time = (System.nanoTime()-start_time)/1_000_000_000;
					if(elapsed_time > timeout) {
						throw new SolverTimeoutException("Over " + timeout +" seconds");
					}
					deactivateSatClauses(var, phi);
					empty = phi.hasEmptyClause();
					if(empty) {
						break;
					}
					assigned_var = unitPropagation(phi);
					if(assigned_var[0] == -1) {//we have found no new assignment due to pure literal propagation
						break;
					}
					VariablesExplored.add(assigned_var[0]);
					phi.getVariables().setVal(assigned_var[0], assigned_var[1]);
					deactivateSatClauses(assigned_var[0], phi);

					empty = phi.hasEmptyClause();
					if(empty) {
						break;
					}
				}
				if(!empty) {
					//we found no empty clause. We can keep looking with the next variable.
					VariablesLeft = phi.getUnassigned();
					backtracking = false;
					continue;
				}else {
					//conflict found, we try the other assignment
					LinkedList<Integer> varToRemove = new LinkedList<Integer>();
					int var_temp;
					while((var_temp = VariablesExplored.removeLast())!= -1) {
						varToRemove.add(var_temp);
					}
					var = varToRemove.removeLast();
					for(int i : varToRemove) {
						phi.reactivateSetClause(i);
					}
					val = 1;
					//this time we don't add the check symbol -1: we might need to backtrack way before
					VariablesExplored.add(var);
					phi.reactivateSetClause(var);
					phi.getVariables().setVal(var, val);
					assigned_var[0] = 0;
					deactivateSatClauses(var,phi);
					empty = phi.hasEmptyClause();

//					while(true) {
//						deactivateSatClauses(var, phi);
//						empty = phi.hasEmptyClause();
//						if(empty) {
//							break;
//						}
//						assigned_var = unitPropagation(phi);
//						if(assigned_var[0] == -1) {
//							break;
//						}
//						VariablesExplored.add(assigned_var[0]);
//						phi.getVariables().setVal(assigned_var[0], assigned_var[1]);
//						deactivateSatClauses(assigned_var[0], phi);
//						empty = phi.hasEmptyClause();
//						if(empty) {
//							break;
//						}
					if(!empty) {
						//we can continue, no backtracking
						VariablesLeft = phi.getUnassigned();
						backtracking = false;
						continue;
					}else {
						//we have an empty clause on second assignement: need to backtrack
						backtracking = true;
						continue;
					}
				}
				
			}else { //we are backtracking
				LinkedList<Integer> varToRemove = new LinkedList<Integer>();
				int var_temp;
				try {
					while((var_temp = VariablesExplored.removeLast())!= -1) {
						varToRemove.add(var_temp);
					}
				}catch(NoSuchElementException e) {
					//no checkpoint found: can't backtrack further: UNSAT
					this.solved = false;
					return false;
				}
				var = varToRemove.removeLast();
				for(int i : varToRemove) {
					phi.reactivateSetClause(i);
				}
				phi.reactivateSetClause(var);
				val = 1;
				phi.getVariables().setVal(var, val);
				//no need to get a specific val, when backtracking we know we have only assignment 1 left.
				//no need to add to explored variables: we won't backtrack again to here
				assigned_var[0] = 0;
				deactivateSatClauses(var,phi);
				empty = phi.hasEmptyClause();
//				while(true) {
//					deactivateSatClauses(var, phi);
//					empty = phi.hasEmptyClause();
//					if(empty) {
//						break;
//					}
//					assigned_var = unitPropagation(phi);
//					if(assigned_var[0] == -1) {
//						break;
//					}
//					//still need to add the assignments gained by unit propagation:
//					//they'll need to be undone if we backtrack
//					VariablesExplored.add(assigned_var[0]);
//					phi.getVariables().setVal(assigned_var[0], assigned_var[1]);
//					deactivateSatClauses(assigned_var[0], phi);
//					empty = phi.hasEmptyClause();
//					if(empty) {
//						break;
//					}
//				}
				if(!empty) {
					//we can continue 
					VariablesLeft = phi.getUnassigned();
					backtracking = false;
					continue;
				}else {
					//we have an empty clause on second assignement we have to try to backtrack further
					backtracking = true;
					continue;
				}
			}
		}
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
		if(this.solved) {
			System.out.println("SAT");
		}else {
			System.out.println("UNSAT");
		}
	}

	@Override
	public void updateSolver(CNF formula) {
		this.interpretation = formula.getVariables().getInterpretation();
		this.solved = false;
	}
	
	@Override
	public boolean solveRec(CNF phi) throws CNFException, SolverTimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] getInterpretation() {
		return interpretation;
	}

}
