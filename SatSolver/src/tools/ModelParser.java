package tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import booleanFormula.Literal;
import booleanFormula.Variables;

public class ModelParser {
	
	public static int[] ModelToInterpretation(String filename) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		try(BufferedReader br = new BufferedReader((new FileReader(filename)))) {
			String line = "";
			while((line = br.readLine())!=null) {
				if("SAT".equals(line) || "UNSAT".equals(line)) {
					continue;
				}
				line = line.trim().replaceAll(" +", " ").replaceAll("\t", " ").replaceAll("\n"," "); // clean all spaces and potential tab
				String[] tab = line.split(" +");
				for(int i = 0; i<tab.length;i++) {
					res.add(Integer.parseInt(tab[i]));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int[] interpretation = new int[res.size()];
		for(int i : res) {
			if(i==0) {continue;}
			if(i>0) {
				interpretation[i-1] = 1;
			}else {
				i = Math.abs(i);
				interpretation[i-1] = 0;
			}
		}
		return interpretation;
		
	}
}
