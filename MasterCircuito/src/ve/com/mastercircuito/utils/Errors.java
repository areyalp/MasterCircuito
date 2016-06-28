package ve.com.mastercircuito.utils;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Errors {
	
	private ArrayList<String> errList;
	
	public Errors() {
		errList = new ArrayList<String>();
	}
	
	public void add(String errorMessage) {
		this.errList.add(errorMessage);
	}
	
	public boolean isEmpty() {
		return this.errList.isEmpty();
	}
	
	public void dump() {
		if(this.errList.size() > 0) {
			for(String m : this.errList) {
				JOptionPane.showMessageDialog(null, m);
			}
		} else {
			JOptionPane.showMessageDialog(null, "No hay errores");
			System.out.println("No errors found");
		}
	}
	
	public ArrayList<String> getErrors() {
		return this.errList;
	}
	
}
