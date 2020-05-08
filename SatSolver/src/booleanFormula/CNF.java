package booleanFormula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CNF {
	//we chose a HashMap for faster access to specific clauses (clauses ID != index...)
	private HashMap<Integer,Clause> clauses = new HashMap<Integer,Clause>(); 
	private Variables variables; //index is the number of the variable. Value : 0 if valuation to false, 1 to true, -1 if no valuation
	private Literal[] literals; //literal xi is stored in literals[i] and literal not(xi) is stored in literal[literals.length-i]
	public CNF() {
		super();
	}
	
	public CNF(int[] variables) throws CNFException {
		this.variables = new Variables(variables);
		this.initLiterals();
	}
	
	public CNF(HashMap<Integer,Clause> clauses, int[] variables) {
		super();
		this.clauses = clauses;
		this.variables = new Variables(variables);
	}
	
	public CNF(Variables variables) throws CNFException {
		super();
		this.variables = variables;
		this.initLiterals();
	}

	public HashMap<Integer,Clause> getClauses() {
		return clauses;
	}

	public void setClauses(HashMap<Integer,Clause> clauses) {
		this.clauses = clauses;
	}
	
	public void addClause(Clause c) {
		this.clauses.put(c.getId(),c);
	}
		
	
	public void setLiterals(Literal[] literals) {
		this.literals = literals;
	}
	public Variables getVariables() {
		return variables;
	}

	public void setVariables(Variables variables) {
		this.variables = variables;
	}
	
	/**
	 * print the formula in the style associated with grid-like problem (e.g. sudoku or latin square)
	 * assumes the grid is square and the values are from 1 to size
	 * for instance x145 is the literal satisfied if there is a 5 in row 1 column 4.
	 * @param size of the grid
	 * @return
	 */
	public String gridToString(int size) {
		String res = "[";
		for(HashMap.Entry<Integer,Clause> e : this.clauses.entrySet()) {
			res+= e.getValue().gridToString(size);
			res+= " ^ ";
		}
		res+="]";
		res = res.substring(0,res.lastIndexOf("^")-1) + res.substring(res.lastIndexOf("^")+2);
		
		return res;
	}

	@Override
	public String toString() {
		String res = "[";
		for(HashMap.Entry<Integer,Clause> e : this.clauses.entrySet()) {
			res+= e.getValue().toString();
			res+= " ^ ";
		}
		res+="]";
		res = res.substring(0,res.lastIndexOf("^")-1) + res.substring(res.lastIndexOf("^")+2);
		
		return res;
	}
	
	/**
	 * Check if the formula is valid with current variable assignment
	 * throws an exception if there are unassigned variables
	 * @return
	 * @throws CNFException 
	 */
	public boolean isValid() throws CNFException {
		for(HashMap.Entry<Integer,Clause> e : this.clauses.entrySet()) {
			if(e.getValue().isValid()) {
				return false;
			}
		}
		return true;
	}
	
	
	public void printStat() {
		System.out.println("Number of clauses = " + this.clauses.size());
		System.out.println("Number of variables = " + this.getVariables().getSize());
		int nb_lit = 0;
		for(int i = 0;i<literals.length;i++) {
			if(literals[i] != null) {nb_lit++;}
		}
		System.out.println("Number of unique literals = " +nb_lit);
		nb_lit = 0;
		for(HashMap.Entry<Integer,Clause> e : this.getClauses().entrySet()) {
			Clause c = e.getValue();
			nb_lit += c.getLiterals().size();
		}
		System.out.println("Number of total literals = " +nb_lit);

	}

	public Literal[] getLiterals() {
		return literals;
	}
	
	/**
	 * returns the index of the literal
	 * @param lit_id
	 * @return
	 */
	public int getSpecificLiteral(int lit_id) {
		for(int i = 0;i<this.literals.length;i++) {
			if(this.literals[i] == null) {
				continue;
			}
			if(this.literals[i].getId() == lit_id) {
				return i;
			}
		}
		return -1;
	}
	
	
	public void removeClause(int clause_ID) throws CNFException {
		this.clauses.remove(clause_ID);
		this.variables.removeClauseFromAll(clause_ID);
		for(Literal l : this.literals) {
			if(l != null) {
				l.removeClause(clause_ID);
			}
		}
	}
	
	public ArrayList<Integer> getClauseID(){
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(HashMap.Entry<Integer,Clause> e : this.clauses.entrySet()) {
			res.add(e.getKey());	
		}
		
		return res;
	}
	
	/**
	 * return 1 if the formula is satisfied
	 * return 0 if it is unsatisfiable
	 * return -1 if it is potentially satisfiable
	 * @return
	 * @throws CNFException
	 */
	public int satSituation() throws CNFException {
		int res = 1;
		for(HashMap.Entry<Integer,Clause> e : this.clauses.entrySet()) {
			Clause c = e.getValue();
			if(c.isEmpty() || c.clauseSituation() == 0) { //one unsatisfiable clause found
				res=0;
				break;
			}
			if(c.clauseSituation() == -1) {
				res=-1; //one potentially satisfied clause
				break;
			}
		}
		return res; //res is 1 if and only if all clauses are sat
	}
	
	public int[] getInterpretation() {
		return this.getVariables().getInterpretation();
	}
	
	public LinkedList<Integer> getUnassigned() throws CNFException{
		return this.variables.getUnassigned();
	}
	//TODO : list of stats on literals and variables7
	

	public boolean hasEmptyClause() {
		for(HashMap.Entry<Integer,Clause> e : this.clauses.entrySet()) {
			Clause c = e.getValue();
			if(c.isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	public void printClauses(int var_id) {
		for(int clause_id: this.getVariables().getClauses()[var_id]) {
			System.out.println(this.getClauses().get(clause_id).toString());
		}
	}
	
	/**
	 * Instanciate all the literals of the variables so to have no null literal
	 * Returns an error if variables haven't been instanciated
	 * Should only be used when created a new formula
	 * @throws CNFException 
	 */
	public void initLiterals() throws CNFException {
		
		if(this.variables == null) {
			throw new CNFException("Variables have not been instanciated");
		}
		Literal[] lit = new Literal[this.getVariables().getSize()*2];
		for(int i = 0; i < this.getVariables().getSize();i++) {
			int pos = i;
			int neg = lit.length-1-i;
			lit[pos] = new Literal(i,false);
			lit[neg] = new Literal(i,true);
			lit[pos].setFormula(this);
			lit[neg].setFormula(this);
		}
		
		this.literals = lit;
		
	}
	
	
	/**
	 * Remove all duplicates
	 */
}
