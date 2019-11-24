package booleanFormula;

import java.util.ArrayList;
import java.util.Random;

public class Clause {
	
	//store the literals used in the clause. Shouldn't have fixed length (not sure if true actually) so we use arraylist
	private ArrayList<Literal> literals = new ArrayList<Literal>();
	private static int count = 0;
	private int id;
	public static CNF formula;
	
	
	public Clause(ArrayList<Literal> literals) {
		super();
		this.literals = literals;
		this.id = count++;
	}
	public ArrayList<Literal> getLiterals() {
		return literals;
	}
	public void setLiterals(ArrayList<Literal> literals) {
		this.literals = literals;
	}
	public int getId() {
		return id;
	}

	
	@Override
	public String toString() {
		String res = "(";
		for(int i = 0;i<literals.size();i++) {
			res += literals.get(i);
			if(i!=literals.size()-1) {
				res += " "+ "v"+" ";
			}
		}
		res += ")";
		return res;
	}
	
	public void addLit(Literal l) {
		this.literals.add(l);
	}
	
	//Only used for diverse tests.
	public static ArrayList<Clause> getRandomClauses(int nb_clauses,int max_nb_lit){
		
		ArrayList<Clause> res = new ArrayList<Clause>();
		int id_l = 0;
		Random r = new Random();
		for(int i = 0;i<nb_clauses;i++) {
			int size = r.nextInt(max_nb_lit)+1;
			res.add(new Clause(Literal.getRandomLiterals(size, id_l)));
			id_l += size;
		}
		
		return res;
	}
	
}
