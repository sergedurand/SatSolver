package solver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import tools.DimacsParser;

public class DPLLOptiJar {

	public static void main(String[] args) throws CNFException, SolverTimeoutException{
		// TODO Auto-generated method stub
		String filename = args[0];
		String destname = args[1];
		SolverDPLLOptimized s = new SolverDPLLOptimized();
		CNF phi = DimacsParser.CNFfromDIMACS2(filename);
		boolean res = s.solve3(phi, Integer.MAX_VALUE);
		if(res) {
			System.out.println("SAT");
		}else {
			System.out.println("UNSAT");
		}
		
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter(destname, true));
			writer.append(' ');
			writer.append(s.saveModel());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
