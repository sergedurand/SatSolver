package booleanFormula;

import java.util.HashSet;
import java.util.Set;

public class ClausesState {
	
	private Set<Integer> activeClauses = new HashSet<Integer>();
	private Set<Integer> unactiveClauses = new HashSet<Integer>();
	
	
	public ClausesState() {
		this.activeClauses = new HashSet<Integer>();
		this.unactiveClauses = new HashSet<Integer>();
	}
	public ClausesState(HashSet<Integer> usedClauses, HashSet<Integer> removedClauses) {
		super();
		this.activeClauses = usedClauses;
		this.unactiveClauses = removedClauses;
	}
	
	public ClausesState(CNF phi) {
		this.activeClauses = phi.getClauses().keySet();
	}
	public Set<Integer> getActiveClauses() {
		return activeClauses;
	}
	public void setActiveClauses(HashSet<Integer> usedClauses) {
		this.activeClauses = usedClauses;
	}
	public Set<Integer> getUnactiveClauses() {
		return unactiveClauses;
	}
	public void setUnactiveClauses(HashSet<Integer> removedClauses) {
		this.unactiveClauses = removedClauses;
	}
	
	public void deactivateClause(int cl_id) {
		this.activeClauses.remove(cl_id);
		this.unactiveClauses.add(cl_id);
	}
	
	public void activateClause(int cl_id) {
		this.unactiveClauses.remove(cl_id);
		this.activeClauses.add(cl_id);
	}
	
	public void deactivateSetClauses(Set<Integer> clauses) {
		this.activeClauses.removeAll(clauses);
		this.unactiveClauses.addAll(clauses);
	}
	
	public void activateSetClauses(Set<Integer> clauses) {
		this.activeClauses.addAll(clauses);
		this.unactiveClauses.removeAll(clauses);
	}
}
