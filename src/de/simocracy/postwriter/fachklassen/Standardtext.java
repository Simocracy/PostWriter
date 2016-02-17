package de.simocracy.postwriter.fachklassen;

public class Standardtext extends Vorlage {
	private final static byte typ = 3;
	
	public Standardtext(int id, String name, String inhalt, String datum) {
		super(id, name, inhalt, datum, typ);
	}

	public Standardtext() {
		super(typ);
	}

}
