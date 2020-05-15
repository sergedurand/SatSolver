package booleanFormula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class CNF {
	//we chose a HashMap for faster access to specific clauses (clauses ID != index...)
	private HashMap<Integer,Clause> clauses = new HashMap<Integer,Clause>(); 
	private Variables variables; //index is the number of the variable. Value : 0 if valuation to false, 1 to true, -1 if no valuation
	private Literal[] literals; //literal xi is stored in literals[i] and literal not(xi) is stored in literal[literals.length-i]
	private Literal[] watched_literals;
	private ClausesState clauses_state;
	
	public CNF() {
		super();
		this.clauses_state = new ClausesState();
	}
	
	public CNF(int[] variables) throws CNFException {
		this.variables = new Variables(variables);
		this.initLiterals();
	}
	
	public CNF(HashMap<Integer,Clause> clauses, int[] variables) {
		super();
		this.clauses = clauses;
		this.variables = new Variables(variables);
		this.clauses_state = new ClausesState(this);
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
		this.clauses_state.activateClause(c.getId());
	}
		
	
	public void setLiterals(Literal[] literals) {
		this.literals = literals;
	}
	public Variables getVariables() {
		return variables;
	}
	
	/**
	 * This also reset all literals.
	 * THis shouldn't be used to update the interpretation
	 * @param variables
	 * @throws CNFException 
	 */
	public void setVariables(Variables variables) throws CNFException {
		this.variables = variables;
		this.initLiterals();
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
		for(int cl_id : this.clauses_state.getActiveClauses()) {
			
			res+= this.clauses.get(cl_id).toString();
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
		this.clauses_state.deactivateClause(clause_ID);
	}
	
	public Set<Integer> getClauseID(){
		return this.clauses.keySet();
	}
	
	public Set<Integer> getActiveClauses(){
		return this.clauses_state.getActiveClauses();
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
		for(int id : this.clauses_state.getActiveClauses()) {
			Clause c = this.clauses.get(id);
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
		for(int cl_id : this.clauses_state.getActiveClauses()) {
			Clause c = this.clauses.get(cl_id);
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
	 * add n new variables to the formula and its corresponding 2*n literals
	 * @param n
	 */
	public void addVariables(int n) {
		int[] new_variables = new int[n];
		for(int i= 0; i < n; i++) {
			new_variables[i] = -1;
		}
		
		int size_or = this.getVariables().getSize();
		int[] res = new int[size_or+n];
		System.arraycopy(this.getVariables().getVariables(), 0, res, 0,size_or);
		System.arraycopy(new_variables, 0, res, size_or, n);
		this.getVariables().setVariables(res);
		Set<Integer>[] updated_clauses = new HashSet[size_or+n];
		System.arraycopy(this.getVariables().getClauses(), 0, updated_clauses, 0, size_or);
		for(int i = 0;i<n;i++) {
			updated_clauses[i+size_or] = new HashSet<Integer>();
		}
		
		this.getVariables().setClauses(updated_clauses);
		Literal[] new_literals = new Literal[n*2];
		for(int i = 0;i<n;i++) {
			int lit_pos = i;
			int lit_neg = n*2-1-i;
			new_literals[lit_pos] = new Literal(lit_pos,false);
			new_literals[lit_neg] = new Literal(lit_neg,true);
			new_literals[lit_pos].setId(i+size_or);
			new_literals[lit_neg].setId(i+size_or);
			new_literals[lit_pos].setFormula(this);
			new_literals[lit_neg].setFormula(this);
		}
		
		
		Literal[] lit_pos = new Literal[n];
		System.arraycopy(new_literals, 0, lit_pos, 0, n);
		Literal[] lit_neg = new Literal[n];
		System.arraycopy(new_literals, n, lit_neg, 0, n);
		
		Literal[] final_lit = new Literal[this.getLiterals().length+n*2];
		System.arraycopy(this.getLiterals(),0,final_lit,0,size_or);
		System.arraycopy(this.getLiterals(), size_or, final_lit, final_lit.length-size_or, size_or);

		
		//we have to update the shifting in every clauses...
		for(HashMap.Entry<Integer,Clause> e : this.clauses.entrySet()) {
			Clause c = e.getValue();
			LinkedHashSet<Integer> temp = new LinkedHashSet<Integer>();
			Iterator<Integer> it = c.getLiterals().iterator();
			while(it.hasNext()) {
				int lit_id = it.next();
				if(lit_id < size_or) {
					temp.add(lit_id);
				}else {
					temp.add(lit_id+2*n);
				}
			}
			c.setLiterals(temp);
		}
		
		//finally we can add the new literals
		System.arraycopy(lit_pos, 0, final_lit, size_or, n);
		System.arraycopy(lit_neg, 0, final_lit, size_or+n, n);
		this.literals = final_lit;
	}

	public ClausesState getClauses_state() {
		return clauses_state;
	}

	public void setClauses_state(ClausesState clauses_state) {
		this.clauses_state = clauses_state;
	}
	
	public void deactivateClause(int cl_id) throws CNFException {
		this.clauses_state.deactivateClause(cl_id);
	}
	
	public void activateClause(int cl_id) throws CNFException {
		this.clauses_state.activateClause(cl_id);
	}
	
	public void deactivateSetClause(Set<Integer> clauses) throws CNFException {
		this.clauses_state.deactivateSetClauses(clauses);
	}
	
	public void activateSetClause(Set<Integer> clauses) throws CNFException {
		this.clauses_state.activateSetClauses(clauses);
	}
	
	/**
	 * reactivate all clauses used by var
	 * @param var
	 * @throws CNFException 
	 */
	public void reactivateSetClause(int var) throws CNFException {
		this.getVariables().setVal(var, -1);
		for(int cl_id : this.variables.getClauses()[var]) {
			//we only reactivate the clause if unassigning var leads to the clause being undetermined (still potentially sat)
			if(this.getClauses().get(cl_id).clauseSituation()==-1) {
				this.activateClause(cl_id);
			}
		}
		//but we reactivate the literal in all the clauses
		for(int cl_id : this.variables.getClauses()[var]) {
			if(this.getClauses().get(cl_id).hasLit(var)) {
				this.getClauses().get(cl_id).reactivateLiteral(var);
			}else{
				this.getClauses().get(cl_id).reactivateLiteral(this.getLiterals().length-1-var);
			}
		}
	}
	
	public void initWatchedLiterals() {
		this.watched_literals = new Literal[this.getLiterals().length];
		for(int i = 0;i<this.getLiterals().length;i++) {
			this.watched_literals[i] = new Literal(this.getLiterals()[i].getId(),this.getLiterals()[i].isNeg());
			this.watched_literals[i].setFormula(this);
			this.watched_literals[i].setClauses(new HashSet<Integer>());
		}
		
		for(int cl_id : this.clauses_state.getActiveClauses()) {
			Iterator<Integer> it = this.clauses.get(cl_id).getLiterals().iterator();
			int l1 = it.next();
			int l2 = it.next();
			int[] watches = {l1,l2};
			this.clauses.get(cl_id).setWatches(watches);
			this.watched_literals[l1].addClause(cl_id);
			this.watched_literals[l2].addClause(cl_id);
		}
	}
	
	public int[] getWatchedLiteral(int cl_id) {
		int[] res = new int[2];
		int i = 0;
		for(int lit_id : this.clauses.get(cl_id).getLiterals()) {//getting the clause is constant
			if(i>1) {break;}
			if(this.watched_literals[lit_id].getClauses().contains(cl_id)) {//checking the literal watches the clause is constant
				res[i] = lit_id;
				i++;
			}
		}
		return res; 
	}
	
	public String printWatchedLiterals() {
		String res ="";
		for(int cl_id : this.clauses_state.getActiveClauses()) {
			int[] watched = getWatchedLiteral(cl_id);
			res += "Clause : " + this.clauses.get(cl_id).toString() + 
					" is being watched by " + this.getLiterals()[watched[0]].toString()
					+" and " + this.getLiterals()[watched[1]].toString();
			res += "\n";
		}
		return res;
	}

	public Literal[] getWatched_literals() {
		return watched_literals;
	}

	public void setWatched_literals(Literal[] watched_literals) {
		this.watched_literals = watched_literals;
	}
	
}
