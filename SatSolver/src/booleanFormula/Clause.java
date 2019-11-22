package booleanFormula;

import java.util.ArrayList;
import java.util.Random;

public class Clause {
	private ArrayList<Literal> literals = new ArrayList<Literal>();
	private int id;
	
	
	public Clause(ArrayList<Literal> literals, int id) {
		super();
		this.literals = literals;
		this.id = id;
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
	public void setId(int id) {
		this.id = id;
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
	
	public static ArrayList<Clause> getRandomClauses(int nb_clauses,int max_nb_lit,int id){
		ArrayList<Clause> res = new ArrayList<Clause>();
		int id_l = 0;
		Random r = new Random();
		for(int i = 0;i<nb_clauses;i++) {
			int size = r.nextInt(max_nb_lit)+1;
			res.add(new Clause(Literal.getRandomLiterals(size, id_l),id));
			id++;
			id_l += size;
		}
		
		return res;
	}
	
}
