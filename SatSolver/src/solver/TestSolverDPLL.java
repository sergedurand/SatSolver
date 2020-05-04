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
		CNF phi = SudokuTools.CNFfromDIMACS("C:\\Users\\Serge\\Documents\\Maths\\M1 JH ENS"
				+ "\\Projet Logique\\Projet\\SatSolver\\benchmark\\rand3_1.cnf");
		phi.printStat();
		System.out.println(phi);
		phi.getVariables().setVal(4, 1);
		phi = s.removeSatClauses(4, phi);
		System.out.println(phi);
		System.out.println(s.unitPropagation(phi)[0]);
		phi.getVariables().setVal(8, 1);
		phi = s.removeSatClauses(8, phi);
		System.out.println(phi);
		phi.getVariables().setVal(5, 1);
		phi = s.removeSatClauses(5, phi);
		System.out.println(phi);
		int[] res = s.unitPropagation(phi);
		System.out.println("var = " + res[0] + ", val = " + res[1]);
	}
}
