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
		CNF phi = SudokuTools.CNFfromDIMACS("C:\\Users\\Serge\\Documents\\Maths"
				+ "\\M1 JH ENS\\Logical aspect of AI\\SatSolver"
				+ "\\SatSolver\\data\\benchmark\\uf20\\uf20-01.cnf");
		phi.printStat();
		s.solveRec(phi);
		s.printRes();
		
		
	}
}
