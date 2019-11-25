package sudoku;

import booleanFormula.*;
import java.util.ArrayList;
import java.util.Random;

public class TestSudoku {

	public static void main(String[] args) throws SudokuException {
		// TODO Auto-generated method stub

		ArrayList<Sudoku> list_sudokus = Tools.ListSudokuFromFile("puzzles.sdk.txt");
		System.out.println(list_sudokus.get(45).toString());
		
		CNF formule = new CNF();
		try {
			formule = Tools.CNFfromSudoku(list_sudokus.get(3));
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Tools.DimacsFromCNF(formule, "sudoku4", null);

		System.out.println(formule.getClauses().get(10000));
	}

}
