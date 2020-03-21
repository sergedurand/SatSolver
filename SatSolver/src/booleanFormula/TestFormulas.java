package booleanFormula;

import java.util.ArrayList;
import java.util.Random;

import sudoku.Tools;

public class TestFormulas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			CNF phi = Tools.CNFfromDIMACS("petite_formule.cnf");
			System.out.println(phi.toString());
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
		

}
