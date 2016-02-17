package de.simocracy.postwriter.fachklassen;

import java.util.ArrayList;

public class Bereich {
	private int id = 0;
	private ArrayList<Thema> themen = null;
	private String name = null;
	private int wkat = 0;
	
	public Bereich(int id, String name) {
		super();
		this.id = id;
		themen = new ArrayList<Thema>();
		this.name = name;
	}

	public Bereich() {
		super();
		themen = new ArrayList<Thema>();
	}

	public Thema getThema(int i) {
		return themen.get(i);
	}
	
	public void addThema(Thema t){
		themen.add(t);
	}
	
	public int getThemenSize(){
		return themen.size();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Thema> getThemen() {
		return themen;
	}

	public void setThemen(ArrayList<Thema> themen) {
		this.themen = themen;
	}

	public int getWkat() {
		return wkat;
	}

	public void setWkat(int wkat) {
		this.wkat = wkat;
	}
}
