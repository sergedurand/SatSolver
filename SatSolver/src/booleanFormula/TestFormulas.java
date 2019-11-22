package booleanFormula;

import java.util.ArrayList;
import java.util.Random;

public class TestFormulas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<Clause> clauses = Clause.getRandomClauses(3, 4, 0);
		
		for(Clause c: clauses) {
			System.out.println(c.toString());
		}
		
		CNF phi = new CNF(clauses);
		String line = "-1 5 3 4 0";
		String[] tab = line.split(" +");
		for(int i = 0;i<tab.length;i++) {
			System.out.println(Integer.parseInt(tab[i]));
		}
		
		ArrayList<Literal> literals = new ArrayList<Literal>(20);
		System.out.println(literals.get(5) == null);
		
	}
		

}
