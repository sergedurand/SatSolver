package booleanFormula;

import java.util.ArrayList;
import java.util.Random;

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
	private ArrayList<Integer> clauses = new ArrayList<Integer>(); 
	
	
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
		if(this.formula == null) {
			throw new CNFException("this literal " + this.id +" isn't assigned to any formula");
		}
		int var_val =  this.formula.variables.getVal(this.id);
		if(var_val == -1) {return var_val;}
		if(isNeg) {return 1-var_val;}
		else return var_val;
	}
	
	/**
	 * for proper display
	 */
	@Override
	public String toString() {
		if(this.isNeg) {
			return "¬x"+id;
		}
		return "x"+id;
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
}
