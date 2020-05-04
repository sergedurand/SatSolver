package booleanFormula;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import solver.Solver;
import solver.SolverDPLL;
import solver.SolverNaive;
import sudoku.SudokuTools;
import tools.Tools;

public class TestFormulas {

	public static void main(String[] args) throws CNFException {
		// TODO Auto-generated method stub
		CNF phi = SudokuTools.CNFfromDIMACS("C:\\Users\\Serge\\Documents\\Maths\\M1 JH ENS"
				+ "\\Projet Logique\\Projet\\SatSolver\\benchmark\\ordering1.cnf");
		System.out.println(phi);
		
		phi.getClauses().get(2).removeLiteral(phi.getLiterals().length-1);
		System.out.println(phi);
		
	
	}	

}
