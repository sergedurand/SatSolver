package booleanFormula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import solver.SolverNaive;
import sudoku.Tools;

public class TestFormulas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// trying the naive solver (and import function).
		try {
			CNF phi = Tools.CNFfromDIMACS("quinn.cnf.txt");
			System.out.println(phi.toString());
			
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String s = "[(x1 v x2) ^ (x3) ^ ]";
		System.out.println(s);
		String s2 = s.substring(0,s.lastIndexOf("^")-1) + s.substring(s.lastIndexOf("^")+2);
		System.out.println(s2);
		
		
	
	}	

}
