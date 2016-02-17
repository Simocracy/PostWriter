package de.simocracy.postwriter.fachklassen;

public class Themenvorlage extends Vorlage {
	private final static byte typ = 4;
	
	public Themenvorlage(int id, String name, String inhalt, String datum) {
		super(id, name, inhalt, datum, typ);
	}

	public Themenvorlage() {
		super(typ);
	}
}
