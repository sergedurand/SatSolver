package sudoku;

import booleanFormula.*;
import java.util.ArrayList;
import java.util.Random;

public class TestSudoku {

	public static void main(String[] args) throws SudokuException, CNFException {
		// TODO Auto-generated method stub

		ArrayList<Sudoku> list_sudokus = SudokuTools.ListSudokuFromFile("puzzles.sdk.txt");
		System.out.println(list_sudokus.get(3).toString());
		
		CNF formule = new CNF();
		try {
			formule = SudokuTools.CNFfromSudoku(list_sudokus.get(3));
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		SudokuTools.DimacsFromCNF(formule, "sudoku4", null);
		
	
		
	}

}
