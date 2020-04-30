package sudoku;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.Literal;
import booleanFormula.Variables;

public class SudokuTools {
	public SudokuTools() {
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
					line = line.trim().replaceAll(" +", " "); // clean all spaces
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
		for(HashMap.Entry<Integer,Clause> e : formule.getClauses().entrySet()) {
			Clause c = e.getValue();
			String line = "";
			for(Integer i : c.getLiterals()) {
				String lit_id = formule.literals[i].sudToString();
				String res = "";
				if(formule.literals[i].isNeg()) {
					res += "-" + lit_id.charAt(2) + lit_id.charAt(3) + lit_id.charAt(4);
				}else {
					res += lit_id.charAt(1) + lit_id.charAt(2) + lit_id.charAt(3);
				}
				res += " ";
				line += res;

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
	
	/**
	 * return a CNF from a Sudoku
	 * See details of encoding here :
	 * https://www.lri.fr/~conchon/ENSPSaclay/project/A_SAT-based_Sudoku_solver.pdf
	 * @param s
	 * @return
	 * @throws CNFException 
	 */
	public static CNF CNFfromSudoku(Sudoku s) throws CNFException {
		Variables variables = new Variables(729);
		Clause.resetCounter(); //in case another formula was created before
		//convention : variables[0:9] contains the 9 possible value for cell [0][0]
		//variable [0:81] contains the 9 possible values for each cell in the first line
		//etc.
		CNF res = new CNF();

		Literal[] literals = new Literal[729*2];
		//we initiate valuation from Sudoku grid
		HashMap<Integer,Clause> clauses = new HashMap<Integer,Clause>();
		int[][] grid = s.getGrid();
		for(int i = 0;i<grid.length;i++) {
			for(int j = 0;j<grid.length;j++) {
				if(!s.isEmpty(i, j)) {
					for(int k = 0;k<9;k++) {
						//k is between 0 and 8 but the values of sudoku grid are between 1 and 9
						if(k +1 == grid[i][j]) {variables.setVal((i*9+j)*9+k,1);} 
						else {variables.setVal((i*9+j)*9+k,0);}					
					}
				}
			}
		}
		
		
		//unit clauses:
//		for(int i = 0;i<9;i++) {
//			for(int j = 0;j<9;j++) {
//				if(grid[i][j] == 0) {continue;}
//				for(int k = 0;k<9;k++) {
//					Clause c = new Clause();
//					int index = (i*9+j)*9+k;
//					if(variables.getVal(index)==1) {
//						literals[index] = new Literal(index,false);
//						literals[index].addClause(c.getId());
//						c.addLiteral(index);
//					}
//					else {
//						index = literals.length-index-1;
//						literals[index] = new Literal(index,false);
//						literals[index].addClause(c.getId());
//						c.addLiteral(index);
//					}
//					clauses.add(c);
//				}
//			}
//		}
		
		//we create 81 clauses: each cell must have a value between 1 and 9
		int nb_definedness = 0;
		// = definedness clauses
		for(int i = 0;i<grid.length;i++){
			for(int j = 0;j<grid.length;j++) {
				// if(grid[i][j] != 0) {continue ;}
				Clause c = new Clause();
				for(int k =0;k<grid.length;k++) {
					int index = (i*9+j)*9+k;
					literals[index] = new Literal((i*9+j)*9+k,false);
					literals[index].addClause(c.getId());
					variables.addClause(c.getId(), index);
					c.addLiteral(index);
				}
				clauses.put(c.getId(),c);
			}
		}
		nb_definedness = clauses.size();
		
		System.out.println("Definedness clauses = "+nb_definedness);
		int nb_uniq = 0;
		// uniqueness clauses
		for(int i = 0; i<9;i++) {
			for(int j = 0;j<9;j++) {
				// if(grid[i][j] != 0) {continue ;}
				for(int k = 0;k<9;k++) {
					int index1 = (i*9+j)*9+k;
					for(int k2 = k+1;k2<9;k2++) {
						Clause c = new Clause();
						int index2 = (i*9+j)*9+k2;
						//we know both literals are negative
						int index1_literal = literals.length-index1-1;
						int index2_literal = literals.length-index2-1;
						literals[index1_literal] = new Literal(index1,true);
						literals[index2_literal] = new Literal(index2,true);
						c.addLiteral(index1_literal);
						c.addLiteral(index2_literal);
						literals[index1_literal].addClause(c.getId());
						literals[index2_literal].addClause(c.getId());
						variables.addClause(c.getId(), index1);
						variables.addClause(c.getId(), index2);
						clauses.put(c.getId(),c);
						nb_uniq++;
					}
				}
			}
		}
		
		System.out.println("Unique clauses = " + nb_uniq);
		//validity clauses for lines
		
		int nb_l = 0;
		for(int i1 = 0;i1<9;i1++) { //for each line
			for(int i=0;i<9;i++) {
				for(int j = i+1;j<9;j++) {
					for(int d = 0;d<9;d++) {
						Clause c = new Clause();
						//literals are negative : xi != d = not(xi=d)
						int index1 = (i1*9+i)*9+d;
						int index2 = (i1*9+j)*9+d;
						int index1_literal = literals.length-index1-1;
						int index2_literal = literals.length-index2-1;
						literals[index1_literal] = new Literal(index1,true);
						literals[index2_literal] = new Literal(index2,true);
						c.addLiteral(index1_literal);
						c.addLiteral(index2_literal);
						variables.addClause(c.getId(), index1);
						variables.addClause(c.getId(), index2);
						clauses.put(c.getId(),c);	
						nb_l++;
					}
				}
				
			}
		}
		
		System.out.println("Validity on lines : "+nb_l);
		
		int nb_c = 0;
		
		//validity for columns
		for(int j1 = 0;j1<9;j1++) { 
			for(int i=0;i<9;i++) {
				for(int j = i+1;j<9;j++) {
					for(int d = 0;d<9;d++) {
						Clause c = new Clause();
						//literals are negative : xi != d = not(xi=d)
						int index1 = (i*9+j1)*9+d;
						int index2 = (j*9+j1)*9+d;
						int index1_literal = literals.length-index1-1;
						int index2_literal = literals.length-index2-1;
						literals[index1_literal] = new Literal(index1,true);
						literals[index2_literal] = new Literal(index2,true);
						c.addLiteral(index1_literal);
						c.addLiteral(index2_literal);
						variables.addClause(c.getId(), index1);
						variables.addClause(c.getId(), index2);
						clauses.put(c.getId(),c);	
						nb_c ++;
					}
				}
				
			}
		}
		
		System.out.println("validity on columns " + nb_c);
		
		int nb_s = 0;
		
		//validity for cell blocks
		for(int x = 0;x<3;x++) {
			for(int y=0;y<3;y++) {
				for(int k = 0;k<9;k++) {
					for(int k2=k+1;k2<9;k2++) {
						int i1 = (x+k)/3;
						int j1 = (y*3) + k%3;
						int i2 = (x+k2)/3;
						int j2 = (y*3) + k2%3;
						for(int d = 0;d<9;d++) {
							Clause c = new Clause();
							int index1 = (i1*9+j1)*9+d;
							int index2 = (i2*9+j2)*9+d;
							int index1_literal = literals.length-index1-1;
							int index2_literal = literals.length-index2-1;
							literals[index1_literal] = new Literal(index1,true);
							literals[index2_literal] = new Literal(index2,true);
							c.addLiteral(index1_literal);
							c.addLiteral(index2_literal);
							variables.addClause(c.getId(), index1);
							variables.addClause(c.getId(), index2);
							clauses.put(c.getId(),c);	
							nb_s ++;
						}
					}
				}
			}
		}
		
		System.out.println("Validity on squares " + nb_s);
		System.out.println("total = " + (nb_s+nb_c+nb_l+nb_uniq+nb_definedness));
		System.out.println("total clause = "+clauses.size());
		res.setClauses(clauses);
		res.setVariables(variables);
		res.setLiterals(literals);
		Literal.formula = res;
		Clause.formula = res;
		
		return res;
		
	}
	
	
	/**
	 * return a Sudoku grid form a CNF formula 
	 * (only uses variables valuation)
	 * @param formule
	 * @return
	 * @throws SudokuException
	 * @throws CNFException
	 */
	public static Sudoku SudokuFromCNF(CNF formule) throws SudokuException, CNFException {
		if(formule.variables.getSize() != 729) {
			throw new SudokuException("The formula doesn't have proper size. It should have 729 variables");
		}
		
		int[] grid = new int[81];
		for(int i = 0;i<9;i++) {
			for(int j = 0;j<9;j++) {
				for(int k = 0; k<9;k++) {
					if(formule.getVariables().getVal((i*9+j)*9+k) == 1) {
						grid[i*9+j] = k+1;
					}
				}
			}
		}
		
		return new Sudoku(grid);
	
	}
	
	

}
