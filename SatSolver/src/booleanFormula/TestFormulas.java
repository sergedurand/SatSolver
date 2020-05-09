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
		CNF phi = Tools.CNFfromDIMACS("p 3 3 \n -1 2 0\n 1 3 0\n 3 -2 0\n");
		System.out.println(phi);
		phi.printStat();
		for(int i = 0;i<phi.getLiterals().length;i++) {
			System.out.println(phi.getLiterals()[i]);
		}
		
		phi.addVariables(2);
		phi.printStat();
		
		for(int i = 0;i<phi.getLiterals().length;i++) {
			System.out.println(phi.getLiterals()[i]);
		}
		
	
	}	

}
