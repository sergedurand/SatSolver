package sudoku;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.Literal;
import booleanFormula.Variables;

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

	
	/**
	 * Load the dimacs file into a CNF
	 * @param filename
	 * @return
	 * @throws CNFException
	 */
	public static CNF CNFfromDIMACS(String filename) throws CNFException {
		CNF res = new CNF();
		Clause.formula = res;
		Literal.formula = res;
		int nb_var = 0;
		Literal[] literals = null;
		try(BufferedReader br = new BufferedReader((new FileReader(filename)))) {
			String line;
			while((line = br.readLine())!=null) {
				if(line.charAt(0) == 'c') { //skipping comments
					continue;
				}
				if(line.charAt(0)== 'p') {//initializing from first line
					String[] tab = line.split(" +");
					nb_var = Integer.parseInt(tab[2]);
					Variables variables = new Variables(nb_var);
					res.setVariables(variables);
					literals = new Literal[nb_var*2];
				}
				else { //reading the clauses
					String[] tab = line.split(" +");
					Clause cl = new Clause();
					for(int i = 0;i<tab.length-1;i++) {//browsing the literals
						int var = Math.abs(Integer.parseInt(tab[i]))-1;
						int id_lit = Integer.parseInt(tab[i]);
						Literal l = new Literal();
						if(id_lit<0) {
							id_lit = literals.length+id_lit;
							l.setNeg(true);
							if(literals[id_lit]==null) {
								literals[id_lit] = l;
								literals[id_lit].setId(var);
							}
							
						}else {
							l.setNeg(false);
							id_lit = id_lit-1;
							if(literals[id_lit]==null) {
								literals[id_lit] = l;
								literals[id_lit].setId(var);
							}
						}
						
						literals[id_lit].addClause(cl.getId());
						res.variables.addClause(cl.getId(), var);
						cl.addLiteral(id_lit);	
					}
					res.addClause(cl);
				}
				
				
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		res.literals =literals;

		return res;

	}
	
	/**
	 * save a CNF as a Dimacs file. Comments should be properly formatted or null
	 * @param formule
	 * @param filename
	 * @param comments
	 */
	public static void DimacsFromCNF(CNF formule, String filename, String comments) {
		String dimacs = "";
		String path = "./"+filename+".cnf";
		if(comments != null) {dimacs += comments;} //we assume comments are already in proper format
		String first_line = "p cnf " + formule.getVariables().getSize() + " " + formule.getClauses().size() +"\n";
		dimacs += first_line;
		for(Clause c : formule.getClauses()) {
			String line = "";
			for(Integer i : c.getLiterals()) {
				if(formule.literals[i].isNeg()) {
					line += (-formule.literals[i].getId()-1) + " ";
				}else {
					line += (formule.literals[i].getId()+1) +" ";
				}
			}
			line += "0\n";
			dimacs += line;
		}
		
		try (FileWriter writer = new FileWriter(path); BufferedWriter bw = new BufferedWriter(writer)){
			bw.write(dimacs);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
