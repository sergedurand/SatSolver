package latinSquare;

import java.util.ArrayList;
import java.util.HashMap;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.Variables;
import constraintEncoding.AMOException;
import constraintEncoding.BinaryAMO;
import constraintEncoding.NaiveAMO;
import constraintEncoding.SequentialAMO;
import solver.SolverDPLL;
import solver.SolverTimeoutException;
import tools.ModelParser;
import tools.Tools;

public class TestLatin {

	public static void main(String[] args) throws CNFException, SolverTimeoutException, AMOException {
		// TODO Auto-generated method stub
		SolverDPLL s = new SolverDPLL();
		int size = 5;
		LatinSquare sq = new LatinSquare(10);
		int[] res = ModelParser.ModelToInterpretation("/home/serge/Documents/ProjetAI/sat20skel/outputs/latin-10.model");
		sq.getPhi().getVariables().setVariables(res);
		sq.updateGrid();
		System.out.println(sq);

		
	}

}
