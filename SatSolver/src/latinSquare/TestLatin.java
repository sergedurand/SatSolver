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
import tools.Tools;

public class TestLatin {

	public static void main(String[] args) throws CNFException, SolverTimeoutException, AMOException {
		// TODO Auto-generated method stub
		SolverDPLL s = new SolverDPLL();
		int size = 5;
		NaiveAMO a1 = new NaiveAMO();
		BinaryAMO a2 = new BinaryAMO();
		SequentialAMO a3 = new SequentialAMO();
		for(int i = 2;i<15;i++) {
			System.out.println("TAILLE = " +i);
			System.out.println("NAIVE:");
			LatinSquare sq2 = new LatinSquare(a1,i);
			sq2.printStat();
			String title1 = "lsq_naive_" + i;
			Tools.DimacsFromCNF(sq2.getPhi(), title1, "");
			System.out.println("BINARY:");
			sq2 = new LatinSquare(a2,i);
			sq2.printStat();
			String title2 = "lsq_binary_" + i;
			Tools.DimacsFromCNF(sq2.getPhi(), title2, "");
			System.out.println("SEQUENTIAL");
			sq2 = new LatinSquare(a3,i);
			sq2.printStat();
			String title3 = "lsq_sequential_" + i;
			Tools.DimacsFromCNF(sq2.getPhi(), title3, "");
		}
		
	}

}
