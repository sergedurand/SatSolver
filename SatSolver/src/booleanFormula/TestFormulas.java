package booleanFormula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import solver.Solver;
import solver.SolverNaive;
import sudoku.SudokuTools;
import tools.Tools;

public class TestFormulas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// trying the naive solver (and import function).
		try {
			CNF phi = SudokuTools.CNFfromDIMACS("quinn.cnf.txt");
			System.out.println(phi.toString());
			Tools.printInterpration(phi.getVariables().getInterpretation());
			SolverNaive s = new SolverNaive(phi);
			int[] interpretation = s.naiveBacktrack();
			Tools.printInterpration(interpretation);
			Tools.printInterpration(phi.getVariables().getInterpretation());
			
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	
	}	

}
