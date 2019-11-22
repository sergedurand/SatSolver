package sudoku;

import java.util.ArrayList;
import java.util.Random;

public class TestSudoku {

	public static void main(String[] args) throws SudokuException {
		// TODO Auto-generated method stub

		ArrayList<Sudoku> list_sudokus = Tools.ListSudokuFromFile("puzzles.sdk.txt");
		
		System.out.println(list_sudokus.get(100));
		
	}

}
