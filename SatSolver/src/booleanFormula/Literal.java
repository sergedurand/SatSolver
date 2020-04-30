package booleanFormula;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 
 * @author Serge
 *
 */
public class Literal {
	public static CNF formula; //this is the formula to which the literals belong. Used to access the valuation for all literals
	private int id; //this is the id of the variable of the literal, it is not unique: x1 and not(x1) have both id = 1
	private boolean isNeg = false;
	//contains the list of clauses that uses the literal
	//as clauses should have unique ID maybe a priority queue would be better for efficiency
	private PriorityBlockingQueue<Integer> clauses = new PriorityBlockingQueue<Integer>(); 
	
	public Literal () {
		super();
	}
	public Literal(int id, boolean isNeg) {
		super();
		this.id = id;
		this.isNeg = isNeg;
 	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	


	public boolean isNeg() {
		return isNeg;
	}


	public void setNeg(boolean isNeg) {
		this.isNeg = isNeg;
	}
	
	/**
	 * return the valuation of the literal
	 * @return
	 * @throws CNFException
	 */
	public int getVal() throws CNFException {
		if(Literal.formula == null) {
			throw new CNFException("this literal " + this.id +" isn't assigned to any formula");
		}
		int var_val =  Literal.formula.variables.getVal(this.id);
		if(var_val == -1) {return var_val;}
		if(isNeg) {return 1-var_val;}
		else return var_val;
	}
	
	public void addClause(int clause_ID) {
		if(!this.clauses.contains(clause_ID)) {
			this.clauses.add(clause_ID);
		}
		return;
	}
	
	public void removeClause(int clause_ID) {
		this.clauses.remove(clause_ID);
	}
	
	/**
	 * for proper display
	 */
	@Override
	public String toString() {
		if(this.isNeg) {
			return "\u00AC"+"x"+(id+1);
		}
		return "x"+(id+1);
	}
	
	public String sudToString() {
		//printing literal using https://www.lri.fr/~conchon/ENSPSaclay/project/A_SAT-based_Sudoku_solver.pdf conventions
		int i = id/81 +1;
		int j = (id/9)%9+1;
		int k = id-((i-1)*9+(j-1))*9 +1;
		String id1 = i+""+j+""+k;
		if(this.isNeg) {
			return "\u00AC"+"x"+(id1);
		}
		return "x"+(id1);
	}
	
	public double getFreq() {
		return (this.clauses.size()/(double)formula.getClauses().size());
	}
	
	public void printFreq() {
		System.out.printf(this.toString() + " frequency = %.2f %%",this.getFreq()*100);
	}
	
	/**
	 * return a random list (ArrayList) of length size of literals
	 * with consecutive IDs starting from id
	 * this is used to test printing or other functions, probably won't be used in SAT solver functions
	 * @param size
	 * @param id
	 * @return
	 */
	public static ArrayList<Literal> getRandomLiterals(int size, int id){
		ArrayList<Literal >res = new ArrayList<Literal>();
		Random r = new Random();
		for(int i =0;i<size;i++) {
			int neg = r.nextInt(2);
			res.add(new Literal(id,neg==0));
			id++;
		}
		
		return res;
	}
	public PriorityBlockingQueue<Integer> getClauses() {
		return clauses;
	}
	public void setClauses(PriorityBlockingQueue<Integer> clauses) {
		this.clauses = clauses;
	}
}
