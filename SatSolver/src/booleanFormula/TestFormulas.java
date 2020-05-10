package booleanFormula;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import solver.Solver;
import solver.SolverDPLL;
import solver.SolverNaive;
import sudoku.SudokuTools;
import tools.Tools;

public class TestFormulas {

	public static void main(String[] args) throws CNFException {
		// TODO Auto-generated method stub
		CNF phi = Tools.CNFfromDIMACS("p 3 3 \n -1 2 0\n 1 3 0\n 3 -2 0\n");
		LinkedList<Integer> l1 = new LinkedList<Integer>();
		for(int i = 0; i < 5; i++) {
			l1.add(i);
		}
		l1.addFirst(-1);
		l1.addFirst(2);
		for(int i : l1) {
			System.out.println(i);
		}
		LinkedList<Integer> varToRemove = new LinkedList<Integer>();
		int var_temp;
		while((var_temp = l1.removeLast())!= -1) {
			varToRemove.add(var_temp);
		}
		for(int i : varToRemove) {
			System.err.println(i);
		}
		int var = varToRemove.removeLast();
		
		System.out.println(var);
		for(int i : varToRemove) {
			System.out.println(i);
		}
	
	}	

}
