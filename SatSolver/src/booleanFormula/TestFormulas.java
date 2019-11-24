package booleanFormula;

import java.util.ArrayList;
import java.util.Random;

public class TestFormulas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		
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
	}
		

}
