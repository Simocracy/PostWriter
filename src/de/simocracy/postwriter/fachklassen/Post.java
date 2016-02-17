package de.simocracy.postwriter.fachklassen;

import java.util.ArrayList;

public class Post {
	private int id = 0;
	private String datum = null;
	private String url = null;
	private ArrayList<Bereich> bereiche = null;
	private ArrayList<Thema> themen = null;
	private ArrayList<Kurznachricht> kurznachrichten = null;
	
	public Post() {
		super();
		bereiche = new ArrayList<Bereich>();
		kurznachrichten = new ArrayList<Kurznachricht>();
	}
	
	public Bereich getBereich(int i) {
		return bereiche.get(i);
	}
	
	public void addBereich(Bereich t){
		bereiche.add(t);
	}
	
	public int getBereicheSize(){
		return bereiche.size();
	}
	
	public Thema getThema(int i) {
		return themen.get(i);
	}
	
	public void addThema(Thema t){
		themen.add(t);
	}
	
	public int getThemaSize(){
		return themen.size();
	}
	
	public Kurznachricht getKurznachricht(int i) {
		return kurznachrichten.get(i);
	}
	
	public void addKurznachricht(Kurznachricht t){
		kurznachrichten.add(t);
	}
	
	public int getKurznachrichtenSize(){
		return kurznachrichten.size();
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public ArrayList<Kurznachricht> getKurznachrichten() {
		return kurznachrichten;
	}

	public void setKurznachrichten(ArrayList<Kurznachricht> kurznachrichten) {
		this.kurznachrichten = kurznachrichten;
	}
}
