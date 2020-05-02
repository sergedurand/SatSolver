package booleanFormula;

import java.util.Comparator;

public class ComparatorVariable implements Comparator<PairVariableFormula> {

	public ComparatorVariable() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(PairVariableFormula p1, PairVariableFormula p2) {
		// TODO Auto-generated method stub
		float x = p1.getFormula().getVariables().getClauses()[p1.getVariable()].size()/p1.getFormula().getClauses().size();
		float y = p2.getFormula().getVariables().getClauses()[p2.getVariable()].size()/p2.getFormula().getClauses().size();
		if(x==y) {
			return 0;
		}else if(x < y) {
			return -1;
		}
		return 1;
	}

}
