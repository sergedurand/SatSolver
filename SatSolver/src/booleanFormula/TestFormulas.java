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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// trying the naive solver (and import function).
		List<String> benchmark = Tools.listFiles("C:\\Users\\Serge\\Documents\\Maths\\M1 JH ENS\\Logical aspect of AI\\SatSolver\\SatSolver\\data\\benchmark\\uuf50-218");
		SolverNaive s1 = new SolverNaive();
		SolverDPLL s2 = new SolverDPLL();
		ArrayList<CNF> bench_formulas = new ArrayList<CNF>();
		
		try {
//			int i = 0;
//			for(String filename : benchmark) {
//				CNF phi = SudokuTools.CNFfromDIMACS(filename);
//				bench_formulas.add(phi);
//				i++;
//			}
			
			
//			CNF phi2 = SudokuTools.CNFfromDIMACS(".\\data\\benchmark\\uuf50-218\\uuf50-01.cnf");
//			Literal l = phi2.getLiterals()[16];
//			System.out.println(l.toString());
//			s2.solve(phi2);
//			s2.printRes();

//			for(CNF phi : bench_formulas) {
//				s1.solve(phi);
//				s1.printRes();
//			}
			s2 = new SolverDPLL();
			CNF phi = SudokuTools.CNFfromDIMACS("wikiDLL.txt");
			System.out.println("test");
			phi.printStat();
			s2.solve(phi);
			s2.printRes();
//			long startTime = System.nanoTime();
//			for(CNF phi : bench_formulas) {
//				s1.solve(phi);
//			}
//			
//			long elapsedTime = System.nanoTime() - startTime;
//			System.out.println("Temps total d'execution pour solver naif sur uf20-91 = " + elapsedTime/1000000 + " ms");
//			
//			startTime = System.nanoTime();
//			for(CNF phi : bench_formulas) {
//				s2.solve(phi);
//				//s2.printRes();
//			}
//			
//			elapsedTime = System.nanoTime() - startTime;
//			System.out.println("Temps total d'execution pour solver DPLL sur uf20-91 = " + elapsedTime/1000000 + " ms");
			
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	
	}	

}
