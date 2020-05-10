package tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.Literal;
import booleanFormula.Variables;

public class DimacsParser {
	
	/**
	 * Load the dimacs file into a CNF
	 * @param filename
	 * @return
	 * @throws CNFException
	 */
	public static CNF CNFfromDIMACS2(String filename) throws CNFException {
		CNF res = new CNF();
		int nb_var = 0;
		Literal[] literals = null;
		try(BufferedReader br = new BufferedReader((new FileReader(filename)))) {
			String line = "";
			int c;
			char car;
			while((line = br.readLine())!=null) {
				if(line.charAt(0) == 'c') { //skipping comments
					continue;
				}
				else if(line.charAt(0)== 'p') {//initializing from first line
					String[] tab = line.split(" +");
					nb_var = Integer.parseInt(tab[2]);
					Variables variables = new Variables(nb_var);
					res.setVariables(variables);
					res.initLiterals();
					literals = res.getLiterals();
				}else {
					break; //due to some weird CNF formats we move to caracter reading
				}
			}
			if(line.length()>=2 && (line.charAt(line.length()-1) == '0' && line.charAt(line.length()-2) == ' ')) {
				//reading a clause
				line = line.trim().replaceAll(" +", " ").replaceAll("\t", " ").replaceAll("\n"," "); // clean all spaces and potential tab
				String[] tab = line.split(" +");
				Clause cl = new Clause();
				cl.setFormula(res);
				int last_index = tab.length-2;
				if(line.charAt(line.length()-1) != '0') {//some files don't have a 0 at the end of the line but on the next line
					last_index = tab.length-1; //in this case we have to go all the way
				}
				for(int i = 0;i<=last_index;i++) {//browsing the literals
					int var = Math.abs(Integer.parseInt(tab[i]))-1;
					int id_lit = Integer.parseInt(tab[i]);
					Literal l = new Literal();
					l.setFormula(res);
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
					res.getVariables().addClause(cl.getId(), var);
					cl.addLiteral(id_lit);
				}
				res.addClause(cl);
				line = "";
			}
			while((c = br.read()) != -1) {
				car = (char)c;
				line += car;
				if(line.charAt(0) == '%') { //some dimacs file had more at the end...
					break;
				}
				if(line.length()>=2 && (line.charAt(line.length()-1) == '0' && line.charAt(line.length()-2) == ' ')) {
					//reading a clause
					line = line.trim().replaceAll(" +", " ").replaceAll("\t", " ").replaceAll("\\r\\n|\\r|\\n"," "); // clean all spaces and potential tab
					String[] tab = line.split(" +");
					Clause cl = new Clause();
					cl.setFormula(res);
					int last_index = tab.length-2;
					if(line.charAt(line.length()-1) != '0') {//some files don't have a 0 at the end of the line but on the next line
						last_index = tab.length-1; //in this case we have to go all the way
					}
					for(int i = 0;i<=last_index;i++) {//browsing the literals
						int var = Math.abs(Integer.parseInt(tab[i]))-1;
						int id_lit = Integer.parseInt(tab[i]);
						Literal l = new Literal();
						l.setFormula(res);
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
						res.getVariables().addClause(cl.getId(), var);
						cl.addLiteral(id_lit);
					}
					res.addClause(cl);
					line = "";
					continue;
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		res.setLiterals(literals);
		return res;

	}

}
