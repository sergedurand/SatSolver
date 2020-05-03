package booleanFormula;

import java.util.ArrayList;

import tools.Tools;

public class ListFormulas {
	private ArrayList<CNF> formulas = new ArrayList<CNF>();
	public ListFormulas() {
		this.formulas = new ArrayList<CNF>();
	}
	public ListFormulas(ArrayList<CNF> formulas) {
		this.formulas = formulas;
		// TODO Auto-generated constructor stub
	}
	public ArrayList<CNF> getFormulas() {
		return formulas;
	}
	public void setFormulas(ArrayList<CNF> formulas) {
		this.formulas = formulas;
	}
	
	public void addPhi(CNF phi) {
		this.formulas.add(phi);
	}
	public CNF getPhi(int index) {
		return this.formulas.get(index);
	}
	
	public int getSize() {
		return this.formulas.size();
	}
	
	public ListFormulas clone(){
		ListFormulas res = new ListFormulas();
		for(int i = 0; i<this.getSize();i++) {
			try {
				CNF phi = Tools.cloneFormula(this.getPhi(i));
				res.addPhi(phi);
			} catch (CNFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return res;
	}
}
