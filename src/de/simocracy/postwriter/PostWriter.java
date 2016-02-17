package de.simocracy.postwriter;

import de.simocracy.postwriter.datenhaltung.DBUpdates;

public class PostWriter {

	/**
	 * @author Gerald Siegert
	 * @author gobo77
	 */
	public static void main(String[] args) {
		InitBild ib = new InitBild();
		ib.setVisible(true);
		
		// Debug-Output festlegen
		Byte b;
		if(args.length > 0) {
			b = Byte.parseByte(args[0]);
		}
		else {
			b = 0;
		}
		
		Einst einst = new Einst(b);
		
		// Programm starten
		einst.out("Initialisiere PostWriter " + Einst.version + ".");
		einst.out("Arbeitsverzeichnis: " + einst.getVerzeichnis());
		Hauptmenue hm = new Hauptmenue(einst, ib);
		
		if(new DBUpdates(einst, ib).existierenEinstellungen()){
			einst.out("Erststart des Programms.");
			hm.erststart();
		} else {
			einst.out("Datenbank mit Einstellungen existiert. Starte PostWriter.");
			hm.startePW();
		}
	}
}
