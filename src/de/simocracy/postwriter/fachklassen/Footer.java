package de.simocracy.postwriter.fachklassen;

public class Footer extends HeaderFooter{
	private final static byte typ = 2;
	
	public Footer(int id, String name, String inhalt, String datum) {
		super(id, name, inhalt, datum, typ);
	}

	public Footer() {
		super(typ);
	}

}
