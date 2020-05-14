package solver;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
/**
 * Heuristic studied : Prioritizing variable that cause the most conflicts.
 * We considered them priority if the number of conflict they cause is above some threshold
 * We do not sort them once they're above
 * @author Serge
 *
 */
public class SolverTwlHeuristics implements Solver {
	boolean solved = false;
	int[] interpretation;

	public boolean solve2(CNF phi,int timeout,int priority_thresh) throws CNFException, SolverTimeoutException {
		long start_time = System.nanoTime();
		int[] nb_conflicts = new int[phi.getVariables().getSize()];
		//we first get rid of preexisting unit clauses
		keepPropagating(phi); //this doesn't use any watched literal technique, it's just pre-processing
		if(phi.hasEmptyClause()) {
			System.out.println("trivially false");
			this.solved = false;
			return false;
		}
		
		phi.initWatchedLiterals();//since we got rid of unit clauses we're sure all active clauses are watched by 2 literals
		HashSet<Integer> VariablesLeft = new HashSet<Integer>(phi.getUnassigned());
		HashSet<Integer> PriorityVariables = new HashSet<Integer>();
		LinkedList<Integer> VariablesExplored = new LinkedList<Integer>();
		
		//initializing variables
		int var;
		int val;
		int lit_id;
		boolean conflict = false;
		boolean backtracking = false;
		LinkedList<Integer> LearnedStack = new LinkedList<Integer>();
		LinkedList<Integer> LearnedFinal = new LinkedList<Integer>();
		LinkedList<Integer> varToRemove = new LinkedList<Integer>();
		int cpt = 0;
		int nb_dec = 0;
		
		while(true) {
			long elapsed_time = (System.nanoTime()-start_time)/1_000_000_000;
			if(elapsed_time > timeout) {
				throw new SolverTimeoutException("Over " + timeout +" seconds");
			}
			if(!backtracking) {
				var = -1;
				if(VariablesLeft.size() == 0) {
					System.out.println("Picked from priority list " + cpt + " times");
					System.out.println("Total number of decision = " + nb_dec);
					System.out.println("percentage of prioritized decision = " + (cpt*100/nb_dec) +"%");
					this.solved = true;
					this.interpretation = phi.getInterpretation();
					return true;
				}else {
					nb_dec ++;
					boolean priority_found = false;
					if(PriorityVariables.size() > 0) {
						Iterator<Integer> it = PriorityVariables.iterator();
						while(it.hasNext()) {
							int temp_var = it.next();
							if(VariablesLeft.contains(temp_var)) {
								var = temp_var;
								VariablesLeft.remove(var);
								it.remove();
								priority_found = true;
								cpt++;
								break;
							}
						}
						if(!priority_found) {
							var = VariablesLeft.iterator().next();
							VariablesLeft.remove(var);
						}
					}else {
						var = VariablesLeft.iterator().next();
						VariablesLeft.remove(var);
					}	
				}
				
				val = 0;
				phi.getVariables().setVal(var, val);
				lit_id = var; //var is assigned to 0 so xvar is the negative literal
				conflict = false;
				LearnedStack = new LinkedList<Integer>();
				LearnedFinal = new LinkedList<Integer>();
				while(true) {//trying to learn new assignments with unit clauses
					LinkedList<Integer> TempLearned = unitPropagate(lit_id,phi,nb_conflicts);
					if(TempLearned.size() == 0) {
						//nothing learned but there might be previously learned lit to propagate
						try {
							lit_id = LearnedStack.removeFirst();
						}catch(NoSuchElementException e) {
							//in principle the first test on size should prevent us from executing this part
							break;
						}
					}else if(TempLearned.getLast() == -1) {
						//conflict found
						TempLearned.removeLast();
						LearnedFinal.addAll(TempLearned);
						conflict = true;
						break;
					}else {
						//learning new assignments with just learned 
						LearnedStack.addAll(TempLearned);
						LearnedFinal.addAll(TempLearned);
						try {
							lit_id = LearnedStack.removeFirst();
						}catch(NoSuchElementException e) {
							//in principle the first test on size should prevent us from executing this part
							break;
						}
					}
				}
				if(!conflict) {
					//before going back to decision we remove learned variables from the set of left variables to be picked
					//and we add them to the explored variables
					VariablesExplored.add(-1);
					VariablesExplored.add(var);
					for(int i : LearnedFinal) {
						int var_id = phi.getLiterals()[i].getId();
						VariablesLeft.remove(var_id);
						VariablesExplored.add(var_id);
					}
					continue;
				
				
				}else {
					//conflict found: trying the other value for var
					//first we undo all learned assignments
					int var_temp;
					for(int l : LearnedFinal) {
						var_temp = phi.getLiterals()[l].getId();
						phi.getVariables().setVal(var_temp, -1);
					}
					
					phi.getVariables().setVal(var, 1);
					lit_id = phi.getLiterals().length-1-var; //var is assigned to 1 so neg(xvar) is the negative literal
					conflict = false;
					LearnedStack = new LinkedList<Integer>();
					LearnedFinal = new LinkedList<Integer>();
					while(true) {//trying to learn new assignments with unit clauses
						LinkedList<Integer> TempLearned = unitPropagate(lit_id,phi,nb_conflicts);
						if(TempLearned.size() == 0) {
							//nothing learned but there might be previously learned lit to propagate
							try {
								lit_id = LearnedStack.removeFirst();
							}catch(NoSuchElementException e) {
								//in principle the first test on size should prevent us from executing this part
								break;
							}
						}else if(TempLearned.getLast() == -1) {
							//conflict found
							TempLearned.removeLast();
							LearnedFinal.addAll(TempLearned);
							conflict = true;
							break;
						}else {
							//learning new assignments with just learned 
							LearnedStack.addAll(TempLearned);
							LearnedFinal.addAll(TempLearned);
							try {
								lit_id = LearnedStack.removeFirst();
							}catch(NoSuchElementException e) {
								//in principle the first test on size should prevent us from executing this part
								break;
							}
						}
					}
					
					if(!conflict) {
						//no conflict on 2nd assignment: no need for checkpoint
						//but still need to add it to explored variables 
						VariablesExplored.add(var);
						for(int i : LearnedFinal) {
							int var_id = phi.getLiterals()[i].getId();
							VariablesLeft.remove(var_id);
							VariablesExplored.add(var_id);
						}
						continue;
					}else {
						//conflict and both values tried: we backtrack to another decision level
						for(int l : LearnedFinal) {
							var_temp = phi.getLiterals()[l].getId();
							phi.getVariables().setVal(var_temp, -1);
						}
						phi.getVariables().setVal(var, -1);
						nb_conflicts[var] += 1;
						if(nb_conflicts[var]>priority_thresh) {
							PriorityVariables.add(var);
						}
						VariablesLeft.add(var);
						backtracking = true;
						continue;
					}	
				}
			}else {
				//we are backtracking
				int var_temp;
				varToRemove = new LinkedList<Integer>();
				try {
					while((var_temp = VariablesExplored.removeLast())!= -1) {
						varToRemove.add(var_temp);
					}
				}catch(NoSuchElementException e) {
					//no checkpoint seen: no more place to backtrack to: UNSAT
					this.solved = false;
					return false;
				}
				var = varToRemove.removeLast();
				
				for(int i : varToRemove) {
					phi.getVariables().setVal(i, -1);
					VariablesLeft.add(i);
				}
				
				phi.getVariables().setVal(var, 1);
				lit_id = phi.getLiterals().length-1-var;
				conflict = false;
				LearnedStack = new LinkedList<Integer>();
				LearnedFinal = new LinkedList<Integer>();
				while(true) {//trying to learn new assignments with unit clauses
					LinkedList<Integer> TempLearned = unitPropagate(lit_id,phi,nb_conflicts);
					if(TempLearned.size() == 0) {
						//nothing learned but there might be previously learned lit to propagate
						try {
							lit_id = LearnedStack.removeFirst();
						}catch(NoSuchElementException e) {
							//in principle the first test on size should prevent us from executing this part
							break;
						}
					}else if(TempLearned.getLast() == -1) {
						//conflict found
						TempLearned.removeLast();
						LearnedFinal.addAll(TempLearned);
						conflict = true;
						break;
					}else {
						//learning new assignments with just learned 
						LearnedStack.addAll(TempLearned);
						LearnedFinal.addAll(TempLearned);
						try {
							lit_id = LearnedStack.removeFirst();
						}catch(NoSuchElementException e) {
							//in principle the first test on size should prevent us from executing this part
							break;
						}
					}
				}
				if(!conflict) {
					VariablesExplored.add(var);
					for(int i : LearnedFinal) {
						int var_id = phi.getLiterals()[i].getId();
						VariablesLeft.remove(var_id);
						VariablesExplored.add(var_id);
					}
					backtracking = false;
					continue;
				}else {
					//need to backtrack further
					for(int l : LearnedFinal) {
						var_temp = phi.getLiterals()[l].getId();
						phi.getVariables().setVal(var_temp, -1);
					}
					phi.getVariables().setVal(var, -1);
					backtracking = true;
					continue;
				}
				
				
			}
			
		}
		


	}
	
	public LinkedList<Integer> unitPropagate(int lit_id,CNF phi, int[] nb_conflicts) throws CNFException {
		//we update variables explored with the learned variable
		//we return the literals made unsat by the learning
		LinkedList<Integer> res = new LinkedList<Integer>();
		Iterator<Integer> it_cl = phi.getWatched_literals()[lit_id].getClauses().iterator();
		while(it_cl.hasNext()) {
			int cl_id = it_cl.next();
			Clause c = phi.getClauses().get(cl_id);
			for(int l2 : c.getLiterals()) {
				if(l2 == lit_id) {continue;}
				if (phi.getWatched_literals()[l2].getClauses().contains(cl_id)) {
					//we found the other watched literal
					if(phi.getWatched_literals()[l2].getVal() == 1) {
						break; //moving to next clause
					}else {
						//looking for a non false non watch literal
						Iterator<Integer> it_lit = c.getLiterals().iterator();
						boolean found = false;
						while(it_lit.hasNext()) {
							int l3 = it_lit.next();
							if((l3 != l2) && (l3 != lit_id) && phi.getLiterals()[l3].getVal() != 0) {
								//found another watch
								it_cl.remove();
								phi.getWatched_literals()[l3].addClause(cl_id);
								found = true;
								break;
							}
						}
						if(found) {
							//we managed to find another watch
							break; //move to next clause
						}else {
							//we are in a unit clause
							if(phi.getWatched_literals()[l2].getVal() == -1) {
								if(l2>= phi.getVariables().getSize()) {
									res.add(phi.getLiterals()[l2].getId()); 
									phi.getVariables().setVal(phi.getLiterals()[l2].getId(), 0);
								}else {
									res.add(phi.getLiterals().length-1-l2);
									phi.getVariables().setVal(phi.getLiterals()[l2].getId(), 1);
								}
								//the other watch was unassigned, we can move on to next clause
								break;
							}else {
								nb_conflicts[phi.getLiterals()[lit_id].getId()] += 1;
								res.add(-1);
								return res;	
							}
						}				
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

	@Override
	public void printRes() {
		// TODO Auto-generated method stub

	}


	@Override
	public int[] getInterpretation() {
		return this.interpretation;
	}
	
	@Override
	/**
	 * With HashSet VariablesLeft
	 */
	public boolean solve(CNF phi,int timeout) throws CNFException, SolverTimeoutException {
		long start_time = System.nanoTime();
		int[] nb_conflicts = new int[phi.getVariables().getSize()];
		//we first get rid of preexisting unit clauses
		keepPropagating(phi); //this doesn't use any watched literal technique, it's just pre-processing
		if(phi.hasEmptyClause()) {
			System.out.println("trivially false");
			this.solved = false;
			return false;
		}
		
		int priority_thresh = phi.getClauses().size()/23;
		phi.initWatchedLiterals();//since we got rid of unit clauses we're sure all active clauses are watched by 2 literals
		LinkedList<Integer> temp = phi.getUnassigned();
		Collections.shuffle(temp);
		HashSet<Integer> VariablesLeft = new HashSet<Integer>(temp);
		HashSet<Integer> PriorityVariables = new HashSet<Integer>();
		LinkedList<Integer> VariablesExplored = new LinkedList<Integer>();
		
		//initializing variables
		int var;
		int val;
		int lit_id;
		boolean conflict = false;
		boolean backtracking = false;
		LinkedList<Integer> LearnedStack = new LinkedList<Integer>();
		LinkedList<Integer> LearnedFinal = new LinkedList<Integer>();
		LinkedList<Integer> varToRemove = new LinkedList<Integer>();
		int cpt = 0;
		int nb_dec = 0;
		
		while(true) {
			long elapsed_time = (System.nanoTime()-start_time)/1_000_000_000;
			if(elapsed_time > timeout) {
				throw new SolverTimeoutException("Over " + timeout +" seconds");
			}
			if(!backtracking) {
				var = -1;
				if(VariablesLeft.size() == 0) {
//					System.out.println("Picked from priority list " + cpt + " times");
//					System.out.println("Total number of decision = " + nb_dec);
//					System.out.println("percentage of prioritized decision = " + (cpt*100/nb_dec) +"%");
					this.solved = true;
					this.interpretation = phi.getInterpretation();
					return true;
				}else {
					nb_dec ++;
					boolean priority_found = false;
					if(PriorityVariables.size() > 0) {
						Iterator<Integer> it = PriorityVariables.iterator();
						while(it.hasNext()) {
							int temp_var = it.next();
							if(VariablesLeft.contains(temp_var)) {
								var = temp_var;
								VariablesLeft.remove(var);
								it.remove();
								priority_found = true;
								cpt++;
								break;
							}
						}
						if(!priority_found) {
							var = VariablesLeft.iterator().next();
							VariablesLeft.remove(var);
						}
					}else {
						var = VariablesLeft.iterator().next();
						VariablesLeft.remove(var);
					}	
				}
				
				val = 0;
				phi.getVariables().setVal(var, val);
				lit_id = var; //var is assigned to 0 so xvar is the negative literal
				conflict = false;
				LearnedStack = new LinkedList<Integer>();
				LearnedFinal = new LinkedList<Integer>();
				while(true) {//trying to learn new assignments with unit clauses
					LinkedList<Integer> TempLearned = unitPropagate(lit_id,phi,nb_conflicts);
					if(TempLearned.size() == 0) {
						//nothing learned but there might be previously learned lit to propagate
						try {
							lit_id = LearnedStack.removeFirst();
						}catch(NoSuchElementException e) {
							//in principle the first test on size should prevent us from executing this part
							break;
						}
					}else if(TempLearned.getLast() == -1) {
						//conflict found
						TempLearned.removeLast();
						LearnedFinal.addAll(TempLearned);
						conflict = true;
						break;
					}else {
						//learning new assignments with just learned 
						LearnedStack.addAll(TempLearned);
						LearnedFinal.addAll(TempLearned);
						try {
							lit_id = LearnedStack.removeFirst();
						}catch(NoSuchElementException e) {
							//in principle the first test on size should prevent us from executing this part
							break;
						}
					}
				}
				if(!conflict) {
					//before going back to decision we remove learned variables from the set of left variables to be picked
					//and we add them to the explored variables
					VariablesExplored.add(-1);
					VariablesExplored.add(var);
					for(int i : LearnedFinal) {
						int var_id = phi.getLiterals()[i].getId();
						VariablesLeft.remove(var_id);
						VariablesExplored.add(var_id);
					}
					continue;
				
				
				}else {
					//conflict found: trying the other value for var
					//first we undo all learned assignments
					int var_temp;
					for(int l : LearnedFinal) {
						var_temp = phi.getLiterals()[l].getId();
						phi.getVariables().setVal(var_temp, -1);
					}
					
					phi.getVariables().setVal(var, 1);
					lit_id = phi.getLiterals().length-1-var; //var is assigned to 1 so neg(xvar) is the negative literal
					conflict = false;
					LearnedStack = new LinkedList<Integer>();
					LearnedFinal = new LinkedList<Integer>();
					while(true) {//trying to learn new assignments with unit clauses
						LinkedList<Integer> TempLearned = unitPropagate(lit_id,phi,nb_conflicts);
						if(TempLearned.size() == 0) {
							//nothing learned but there might be previously learned lit to propagate
							try {
								lit_id = LearnedStack.removeFirst();
							}catch(NoSuchElementException e) {
								//in principle the first test on size should prevent us from executing this part
								break;
							}
						}else if(TempLearned.getLast() == -1) {
							//conflict found
							TempLearned.removeLast();
							LearnedFinal.addAll(TempLearned);
							conflict = true;
							break;
						}else {
							//learning new assignments with just learned 
							LearnedStack.addAll(TempLearned);
							LearnedFinal.addAll(TempLearned);
							try {
								lit_id = LearnedStack.removeFirst();
							}catch(NoSuchElementException e) {
								//in principle the first test on size should prevent us from executing this part
								break;
							}
						}
					}
					
					if(!conflict) {
						//no conflict on 2nd assignment: no need for checkpoint
						//but still need to add it to explored variables 
						VariablesExplored.add(var);
						for(int i : LearnedFinal) {
							int var_id = phi.getLiterals()[i].getId();
							VariablesLeft.remove(var_id);
							VariablesExplored.add(var_id);
						}
						continue;
					}else {
						//conflict and both values tried: we backtrack to another decision level
						for(int l : LearnedFinal) {
							var_temp = phi.getLiterals()[l].getId();
							phi.getVariables().setVal(var_temp, -1);
						}
						phi.getVariables().setVal(var, -1);
						nb_conflicts[var] += 1;
						if(nb_conflicts[var]>priority_thresh) {
							PriorityVariables.add(var);
						}
						VariablesLeft.add(var);
						backtracking = true;
						continue;
					}	
				}
			}else {
				//we are backtracking
				int var_temp;
				varToRemove = new LinkedList<Integer>();
				try {
					while((var_temp = VariablesExplored.removeLast())!= -1) {
						varToRemove.add(var_temp);
					}
				}catch(NoSuchElementException e) {
					//no checkpoint seen: no more place to backtrack to: UNSAT
					this.solved = false;
					return false;
				}
				var = varToRemove.removeLast();
				
				for(int i : varToRemove) {
					phi.getVariables().setVal(i, -1);
					VariablesLeft.add(i);
				}
				
				phi.getVariables().setVal(var, 1);
				lit_id = phi.getLiterals().length-1-var;
				conflict = false;
				LearnedStack = new LinkedList<Integer>();
				LearnedFinal = new LinkedList<Integer>();
				while(true) {//trying to learn new assignments with unit clauses
					LinkedList<Integer> TempLearned = unitPropagate(lit_id,phi,nb_conflicts);
					if(TempLearned.size() == 0) {
						//nothing learned but there might be previously learned lit to propagate
						try {
							lit_id = LearnedStack.removeFirst();
						}catch(NoSuchElementException e) {
							//in principle the first test on size should prevent us from executing this part
							break;
						}
					}else if(TempLearned.getLast() == -1) {
						//conflict found
						TempLearned.removeLast();
						LearnedFinal.addAll(TempLearned);
						conflict = true;
						break;
					}else {
						//learning new assignments with just learned 
						LearnedStack.addAll(TempLearned);
						LearnedFinal.addAll(TempLearned);
						try {
							lit_id = LearnedStack.removeFirst();
						}catch(NoSuchElementException e) {
							//in principle the first test on size should prevent us from executing this part
							break;
						}
					}
				}
				if(!conflict) {
					VariablesExplored.add(var);
					for(int i : LearnedFinal) {
						int var_id = phi.getLiterals()[i].getId();
						VariablesLeft.remove(var_id);
						VariablesExplored.add(var_id);
					}
					backtracking = false;
					continue;
				}else {
					//need to backtrack further
					for(int l : LearnedFinal) {
						var_temp = phi.getLiterals()[l].getId();
						phi.getVariables().setVal(var_temp, -1);
					}
					phi.getVariables().setVal(var, -1);
					backtracking = true;
					continue;
				}			
			}	
		}	
	}
	
	public String saveModel() {
		String res = "";
		if(this.getInterpretation() == null || this.getInterpretation().length == 0) {
			res = "no model";
			return res;
		}else{
			for(int i = 0;i<this.getInterpretation().length;i++) {
				if(this.getInterpretation()[i] ==0) {
					res+= "-" + (i+1) +" ";
				}else {
					res+= "" + (i+1)+ " ";
				}
			}
		}
		res += "0";
		return res;
	}
}
