package solver;

import java.util.ArrayList;
import java.util.List;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.ListFormulas;
import booleanFormula.Literal;
import sudoku.SudokuTools;
import tools.Tools;

public class TestSolverDPLL {
	public static void main(String[] args) throws CNFException, SolverTimeoutException {
		SolverDPLL s = new SolverDPLL();
		String path = "C:\\Users\\Serge\\Documents\\Maths\\M1 JH ENS\\Projet Logique\\Projet\\SatSolver\\benchmark";
		List<String> benchmark = Tools.listFiles(path);
		ArrayList<CNF> bench_formulas = new ArrayList<CNF>();

		try {
			for(String filename : benchmark) {
				System.out.println(filename);
				CNF phi = SudokuTools.CNFfromDIMACS(filename);
				bench_formulas.add(phi);
			}
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long start_time = System.nanoTime();
		long clone_time = 0;
		int cpt = 0;
		for(CNF phi : bench_formulas) {
			try {
				boolean res = s.solveRec(phi);
				if(res) {
					System.out.println("SAT");
				}else {
					System.out.println("UNSAT");
				}
			}catch(CNFException e){
				e.printStackTrace();
			}catch(SolverTimeoutException e){
				cpt ++;
				phi.printStat();
			}
		}
		long elapsed_time = System.nanoTime()-start_time-clone_time;
		System.out.println("temps total = " + elapsed_time/1000000 + " ms");
		System.out.println("Nombre de formule abandonnée : " + cpt);
	}

}
