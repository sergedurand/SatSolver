package booleanFormula;

import java.util.ArrayList;
import java.util.Random;

public class Literal {
	private int id;
	private boolean isNeg = false;
	private ArrayList<Clause> clauses = new ArrayList<Clause>();
	
	
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
	
	@Override
	public String toString() {
		if(this.isNeg) {
			return "¬x"+id;
		}
		return "x"+id;
	}

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
