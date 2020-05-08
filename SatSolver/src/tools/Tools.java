package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	 * print an interpretation
	 * @param interpretation
	 */
	public static void printInterpration(int[] interpretation) {
		String res = "";
		for(int i = 0;i<interpretation.length;i++) {
			res += "x"+(i+1)+" = " + interpretation[i];
			if(i<interpretation.length-1) {
				res += ", ";
			}
		}
		System.out.println(res);
	}
	
	
	public static List<String> listFiles(String folder_path){
		List<String> res = new ArrayList<String>();
		try (Stream<Path> walk = Files.walk(Paths.get(folder_path))) {

			res = walk.map(x -> x.toString())
					.filter(f -> f.endsWith(".cnf")).collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	/**
	 * create a Dimacs style string from a CNF. Comments should be properly formatted or null
	 * used for formula deep copy
	 * @param formule
	 * @param filename
	 * @param comments
	 */
	public static String StringDimacsFromCNF(CNF formule,String comments) {
		String dimacs = "";
		if(comments != null) {dimacs += comments;} //we assume comments are already in proper format
		String first_line = "p cnf " + formule.getVariables().getSize() + " " + formule.getClauses().size() +"\n";
		dimacs += first_line;
		for(HashMap.Entry<Integer,Clause> e : formule.getClauses().entrySet()) {
			Clause c = e.getValue();
			String line = "";
			for(Integer i : c.getLiterals()) {
				int lit_id = formule.getLiterals()[i].getId() + 1;
				String res = "";
				if(formule.getLiterals()[i].isNeg()) {
					res += "-" + lit_id;
				}else {
					res += lit_id;
				}
				res += " ";
				line += res;

			}
			line += "0\n";
			dimacs += line;
		}
		
		return dimacs;
		
	}
	
	
	/**
	 * Create a formula from a dimacs style string
	 * @param dimacs
	 * @return
	 * @throws CNFException
	 */
	public static CNF CNFfromDIMACS(String dimacs) throws CNFException {
		CNF res = new CNF();
		int nb_var = 0;
		Literal[] literals = null;
		try(BufferedReader br = new BufferedReader((new StringReader(dimacs)))) {
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
					if(line.charAt(0) == '%') { //some dimacs file had more at the end...
						break;
					}
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
						literals[id_lit].setFormula(res);
						res.getVariables().addClause(cl.getId(), var);
						cl.addLiteral(id_lit);
					}
					cl.setFormula(res);
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
		
		res.setLiterals(literals);

		return res;

	}
	
	
	public static CNF cleanClone(CNF phi) throws CNFException {
		CNF res = new CNF();
		Variables variables = phi.getVariables().clone();
		res.setVariables(variables);
		Literal[] literals = new Literal[variables.getSize()*2];
		for(int i = 0;i<variables.getSize();i++) {
			Literal l_pos = new Literal();
			l_pos.setId(i);
			l_pos.setFormula(res);
			l_pos.setNeg(false);
			literals[i] = l_pos;
			
			Literal l_neg = new Literal();
			l_neg.setId(i);
			l_neg.setFormula(res);
			l_neg.setNeg(true);
			literals[literals.length-1-i] = l_neg;
		}
		res.setLiterals(literals);
		
		HashMap<Integer,Clause> clauses = new HashMap<Integer,Clause>();
		for(HashMap.Entry<Integer,Clause> e : phi.getClauses().entrySet()) {
			Clause c = e.getValue().clone();
			c.setFormula(res);
			clauses.put(c.getId(), c);
		}
		res.setClauses(clauses);
		
		for(HashMap.Entry<Integer,Clause> e : clauses.entrySet()) {
			Clause c = e.getValue();
			for(int i = 0;i<variables.getSize();i++) {
				if(c.hasVar(i)) {
					variables.addClause(c.getId(), i);
				}
			}
			for(int i = 0;i<res.getLiterals().length;i++) {
				if(c.hasLit(i)) {
					res.getLiterals()[i].addClause(c.getId());
				}
			}
		}
		
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
				String lit_id = Integer.toString(formule.getLiterals()[i].getId());
				String res = "";
				if(formule.getLiterals()[i].isNeg()) {
					res += "-" + lit_id;
				}else {
					res += lit_id;
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

}

