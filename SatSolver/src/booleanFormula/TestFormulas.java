package booleanFormula;

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
		List<String> benchmark = Tools.listFiles("C:\\Users\\Serge\\Documents\\Maths\\M1 JH ENS\\Logical aspect of AI\\SatSolver\\SatSolver\\data\\benchmark\\uf20");
		SolverNaive s1 = new SolverNaive();
		ArrayList<CNF> bench_formulas = new ArrayList<CNF>();
		
//		try {
//			for(String filename : benchmark) {
//				CNF phi = SudokuTools.CNFfromDIMACS(filename);
//				bench_formulas.add(phi);
//			}
//			
//			long startTime = System.nanoTime();
//			for(CNF phi : bench_formulas) {
//				s1.solve(phi);
//			}
//			
//			long elapsedTime = System.nanoTime() - startTime;
//			System.out.println("Temps total d'execution pour solver naif sur uf20-91 = " + elapsedTime/1000000 + " ms");
//		
//		} catch (CNFException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		CNF phi;
		SolverDPLL s2 = new SolverDPLL();
		try {
			phi = SudokuTools.CNFfromDIMACS("petite_formule.cnf");
			CNF phi2 = Tools.cloneFormula(phi);
			System.out.println(phi.toString());
			System.out.println(phi2.toString());
			Tools.printInterpration(phi.getVariables().getInterpretation());
			phi.getVariables().setVal(0, 1);
			Tools.printInterpration(phi.getVariables().getInterpretation());
			s2.updateSolver(phi);
			s2.unitProb(0);
			System.out.println(phi.toString());
			System.out.println(phi2.toString());

			

		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}	

}
