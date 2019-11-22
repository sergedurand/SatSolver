package sudoku;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import booleanFormula.CNF;
import booleanFormula.Clause;
import booleanFormula.Literal;

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

	
	public static CNF CNFfromDIMACS(String filename) {
		CNF res = new CNF();
		int id_clause = 0;
		int nb_var = 0;
		Literal[] literals;
		try(BufferedReader br = new BufferedReader((new FileReader(filename)))) {
			String line;
			while((line = br.readLine())!=null) {
				if(line.charAt(0) == 'c') { //skipping comments
					continue;
				}
				if(line.charAt(0)== 'p') {
					String[] tab = line.split(" +");
					nb_var = Integer.parseInt(tab[2]);
					literals = new Literal[nb_var*2];
				}
				else { //reading the clauses
					String[] tab = line.split(" +");
					for(int i = 0;i<tab.length;i++) {
						int id_lit = Integer.parseInt(tab[i]);
						if(id_lit<0) {
							
						}
					}
					Clause cl = new Clause();
				}
				
				
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
