package sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Tools {
	public Tools() {
		super();
	}
	
	/**
	 * Convert a String into a list of digits
	 * @param s
	 * @return
	 */
	public static int[] StringToInt(String s) {
		int[] list_int = new int[s.length()];
		
		for(int i = 0;i<s.length();i++) {
			list_int[i] = Character.getNumericValue(s.charAt(i));
		}
		
		return list_int;
	}
	
	
	/**
	 * Read a file that contains a list of sudoku problems
	 * stored as lines of 81 characters
	 * @param filename
	 * @return
	 * @throws SudokuException
	 */
	public static ArrayList<Sudoku> ListSudokuFromFile(String filename) throws SudokuException{
		ArrayList<Sudoku> res = new ArrayList<Sudoku>();
		
		try(BufferedReader br = new BufferedReader((new FileReader(filename)))){
			
			String line;
			while((line = br.readLine()) != null) {
				res.add(new Sudoku(StringToInt(line)));
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}

}
