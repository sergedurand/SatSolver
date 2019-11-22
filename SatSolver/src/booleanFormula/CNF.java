package booleanFormula;

import java.util.ArrayList;

public class CNF {
	private ArrayList<Clause> clauses = new ArrayList<Clause>();

	
	public CNF() {
		super();
	}
	
	public CNF(ArrayList<Clause> clauses) {
		super();
		this.clauses = clauses;
	}

	public ArrayList<Clause> getClauses() {
		return clauses;
	}

	public void setClauses(ArrayList<Clause> clauses) {
		this.clauses = clauses;
	}
	
	public void addClause(Clause c) {
		this.clauses.add(c);
	}
	
	@Override
	public String toString() {
		String res = "[";
		for(int i = 0;i<this.clauses.size();i++) {
			res += clauses.get(i).toString();
			if(i!=this.clauses.size()-1) {
				res+= " ^ ";
			}
		}
		res+="]";
		
		return res;
	}
}
