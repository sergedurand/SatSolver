package booleanFormula;

import java.util.ArrayList;
import java.util.Random;

import solver.SolverNaive;
import sudoku.Tools;

public class TestFormulas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			CNF phi = Tools.CNFfromDIMACS("quinn.cnf.txt");
			System.out.println(phi.toString());
			System.out.println(phi.getVariables().getSize());
			SolverNaive solver = new SolverNaive();
			solver.solve(phi);
			
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
	}	

}
