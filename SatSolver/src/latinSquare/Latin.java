package latinSquare;

import booleanFormula.CNFException;
import constraintEncoding.AMOException;
import constraintEncoding.NaiveAMO;
import tools.Tools;

public class Latin {
	public static void main(String[] args) throws CNFException, AMOException {
		int N;
		if(args.length==0) {
			N = 10;
			System.out.println("No size input: default size for test is 10");
		}else {
			N = Integer.parseInt(args[0]);
		}
		NaiveAMO a = new NaiveAMO();
		LatinSquare sq = new LatinSquare(a,N);
		String file_name = "latin_"+N;
		Tools.DimacsFromCNF(sq.getPhi(),file_name, "");
	}
}
