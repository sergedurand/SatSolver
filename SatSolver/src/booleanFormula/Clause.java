package booleanFormula;

import java.util.ArrayList;
import java.util.Random;

public class Clause {
	
	//store the literals used in the clause. Shouldn't have fixed length (not sure if true actually) so we use arraylist
	private ArrayList<Integer> literals = new ArrayList<Integer>();
	private static int count = 0;
	private int id;
	public static CNF formula;
	
	public Clause() {
		super();
		this.id = count++;
	}
	public Clause(ArrayList<Integer> literals) {
		super();
		this.literals = literals;
		this.id = count++;
	}
	public ArrayList<Integer> getLiterals() {
		return literals;
	}
	public void setLiterals(ArrayList<Integer> literals) {
		this.literals = literals;
	}
	public int getId() {
		return id;
	}
	
	public void addLiteral(int lit_id) {
		this.literals.add(lit_id);
	}

	public static void resetCounter() {
		count = 0;
	}
	@Override
	public String toString() {
		String res = "(";
		for(int i = 0;i<literals.size();i++) {
			int l_id = literals.get(i);
			res += formula.literals[l_id].toString();
			if(i!=literals.size()-1) {
				res += " "+ "v"+" ";
			}
		}
		res += ")";
		return res;
	}
	
	public String sudToString() {
		String res = "(";
		for(int i = 0;i<literals.size();i++) {
			int l_id = literals.get(i);
			res += formula.literals[l_id].sudToString();
			if(i!=literals.size()-1) {
				res += " "+ "v"+" ";
			}
		}
		res += ")";
		return res;
	}
	

	
}
