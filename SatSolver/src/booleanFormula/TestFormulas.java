package booleanFormula;

import java.util.ArrayList;
import java.util.Random;

import sudoku.Tools;

public class TestFormulas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		try {
			CNF formule = Tools.CNFfromDIMACS("petite_formule.cnf");
			System.out.println(formule.toString());
		} catch (CNFException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int var = 0;
		System.out.println(var);
		int[] tab = new int[3];
		tab[0] = 1;tab[1] = 1;tab[2] = 1;
		Variables variables = new Variables(tab);
		CNF phi = new CNF(variables);
		Literal.formula = phi;
		Literal l = new Literal(1,false);
		Literal l2 = new Literal(0,true);
		try {
			System.out.println("valuation de l : "+l.getVal());
			System.out.println("valuation de l2 : " + l2.getVal());
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			phi.variables.setVal(0, 0);
			phi.variables.setVal(1, 0);
			System.out.println("valuation de l : "+l.getVal());
			System.out.println("valuation de l2 : " + l2.getVal());
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Literal[] lit = new Literal[9];
		System.out.println("taille lit : "+lit.length);
	}
		

}
