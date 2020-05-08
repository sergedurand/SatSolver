package solver;

import java.util.ArrayList;
import java.util.List;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.ListFormulas;
import booleanFormula.Literal;
import sudoku.SudokuTools;
import tools.DimacsParser;
import tools.Tools;

public class TestSolverDPLL {
	public static void main(String[] args) throws CNFException, SolverTimeoutException {
		SolverDPLL s = new SolverDPLL();
		System.out.println(System.getProperty("line.separator"));
		CNF phi = DimacsParser.CNFfromDIMACS2("C:\\Users\\Serge\\Documents\\Maths\\M1 JH ENS\\Projet Logique\\Projet\\SatSolver\\SAT\\par8-1-c.cnf");
		
		String path = "C:\\Users\\Serge\\Documents\\Maths\\M1 JH ENS\\Projet Logique\\Projet\\SatSolver\\SAT";
		List<String> benchmark = Tools.listFiles(path);
		ArrayList<CNF> bench_formulas = new ArrayList<CNF>();

		try {
			for(String filename : benchmark) {
				System.out.println(filename);
				CNF phi2 = DimacsParser.CNFfromDIMACS2(filename);
				bench_formulas.add(phi);
			}
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long start_time = System.nanoTime();
		long clone_time = 0;
		int cpt = 0;
		for(CNF phi2 : bench_formulas) {
			try {
				boolean res = s.solve(phi);
				if(res) {
					System.out.println("SAT");
				}else {
					System.out.println("UNSAT");
				}
			}catch(CNFException e){
				e.printStackTrace();
			}catch(SolverTimeoutException e){
				cpt ++;
			}
		}
		long elapsed_time = System.nanoTime()-start_time-clone_time;
		System.out.println("temps total = " + elapsed_time/1000000 + " ms");
		System.out.println("Nombre de formule abandonn�e : " + cpt);

	}

}
