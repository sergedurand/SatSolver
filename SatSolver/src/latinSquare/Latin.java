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
		String dest_folder;
		if(args.length < 2) {
			dest_folder = "./inputs";
		}else{
			dest_folder = args[1];
		}
		
		NaiveAMO a = new NaiveAMO();
		LatinSquare sq = new LatinSquare(a,N);
		String file_name = "latin_"+N;
		Tools.DimacsFromCNF(sq.getPhi(), dest_folder, file_name, "");
	}
}
