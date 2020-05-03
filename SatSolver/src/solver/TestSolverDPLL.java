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
		
		List<String> benchmark = Tools.listFiles("C:\\Users\\Serge\\Documents\\Maths\\M1 JH ENS\\Logical aspect of AI\\SatSolver\\test_files_1\\temp");
		SolverDPLL s2 = new SolverDPLL();
		ListFormulas bench_formulas = new ListFormulas();
		CNF phi1 = SudokuTools.CNFfromDIMACS("C:\\Users\\Serge\\Documents\\Maths\\M1 JH ENS\\Logical aspect of AI\\SatSolver\\test_files_1\\test1.cnf");
		System.out.println(phi1.toString());
		System.out.println(s2.solveRec(phi1));
		
		for(int i = 0;i<benchmark.size();i++) {
			try {
				CNF phi = SudokuTools.CNFfromDIMACS(benchmark.get(i));
//				CNF phi2 = Tools.cloneFormula(phi);
//				String savename = "test_" + i + "_copy";
//				SudokuTools.DimacsFromCNF(phi2, savename,"");
				bench_formulas.addPhi(phi);
				System.out.println(phi.toString());
				System.out.println(bench_formulas.getPhi(i));
			}catch(CNFException e) {
				e.printStackTrace();
			}
		}
		
		ListFormulas bench2 = bench_formulas.clone();
		for(CNF phi : bench2.getFormulas()) {
			CNF phi2 = Tools.cloneFormula(phi);
			System.out.println(phi.toString());
		}
	
		
		
		
	}
}
