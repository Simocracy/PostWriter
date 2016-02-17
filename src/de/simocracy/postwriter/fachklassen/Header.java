package de.simocracy.postwriter.fachklassen;

public class Header extends HeaderFooter{
	private final static byte typ = 1;
	
	public Header(int id, String name, String inhalt, String datum) {
		super(id, name, inhalt, datum, typ);
	}

	public Header() {
		super(typ);
	}

}
