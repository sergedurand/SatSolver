package tools;

import java.util.ArrayList;
import java.util.List;

import booleanFormula.CNF;
import booleanFormula.CNFException;

public class ToolsTest {

	public static void main(String[] args) throws CNFException {
		// TODO Auto-generated method stub
		CNF phi = DimacsParser.CNFfromDIMACS2("./SAT/par8-1-c.cnf");
		String path = "./UNSAT";
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
		
	}

}
