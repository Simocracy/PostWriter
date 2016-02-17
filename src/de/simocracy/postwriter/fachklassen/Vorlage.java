package de.simocracy.postwriter.fachklassen;

public abstract class Vorlage {
	private int id = 0;
	private String name = null;
	private String inhalt = null;
	private String datum = null;
	private byte typ = 0;
	
	public Vorlage(int id, String name, String inhalt, String datum, byte typ) {
		this.id = id;
		this.name = name;
		this.inhalt = inhalt;
		this.datum = datum;
		this.typ = typ;
	}

	public Vorlage(byte typ) {
		this.typ = typ;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInhalt() {
		return inhalt;
	}

	public void setInhalt(String inhalt) {
		this.inhalt = inhalt;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}
	
	public byte getTyp(){
		return typ;
	}
}
