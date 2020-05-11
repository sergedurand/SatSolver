package tools;

import java.util.ArrayList;
import java.util.List;

import booleanFormula.CNF;
import booleanFormula.CNFException;

public class ToolsTest {

	public static void main(String[] args) throws CNFException {
		// TODO Auto-generated method stub
		String line = "396	-398	0\n"; 
		System.err.println(line);
		line = line.trim().replaceAll(" +", " ").replaceAll("\t", " ").replaceAll("\\r\\n|\\r|\\n"," ");
		System.err.println(line);
		String[] tab = line.split(" +");
		for(int i = 0;i<tab.length;i++) {
			System.err.println(tab[i]);
		}
		
	}

}
