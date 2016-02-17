package de.simocracy.postwriter.datenhaltung;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;

public class DBUpdates extends FortschrittsFenster {
	
	private static final long serialVersionUID = 3825103457380334272L;
	private Einst einst;
	private SQLiteVerbindung verb;
	private File db;
	private int dbVersionDB;
	private ArrayList<String> befehle1; // Befehle bis Update von 1.6 auf 2.0 Teil 1 (vor Konvertierung und PW)
	private ArrayList<String> befehle2; // Befehle ab Update von 1.6 auf 2.0 Teil 2 (nach Konvertierung und PW)
	private int anzahlBefehle1;
	private int anzahlBefehle2;
	private ArrayList<Integer> stand;
	private int vorKon;
	private boolean konv;
	private boolean pwInkl;
	private InitBild ib;
	
	public DBUpdates(Einst einst, InitBild ib){
		super("Datenbankupdate",
				"Bitte warten. Datenbank wird aktualisiert...", "Initialisiere Update...");
		this.einst = einst;
		this.verb = new SQLiteVerbindung(einst);
		this.ib = ib;
		
		db = new File(einst.getVerzeichnis() + "posts.sqlite");
	}
	
	public boolean existierenEinstellungen(){
		einst.out("Prüfe Datenbank.");
		
		// Pruefe, welche DB existiert
		int dbdb = 0;
		if(db.exists()) dbdb = 1;
		
		if(dbdb != 0){
			einst.out("Datenbank existiert.");
			
			// Pruefe DB-Version
			dbVersionDB = -1;
			holeDBVersion();
			einst.out("Datenbankversion: " + dbVersionDB);
			
			// Aktualisiere DB
			if(dbVersionDB != -1 && Einst.dbVersionI > dbVersionDB){
				
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {

						try {
							einst.out("Veraltete Datenbank. Aktualisiere Datenbank auf Version " +
									Einst.dbVersion + ".");
							ib.dispose();
							setVisible(true);
							
							// Oeffne DB
							verb.oeffneDB();
							
							sammleBefehle();

							// Lege Maximum der ProgressBar fest
							setMaximum(anzahlBefehle1 + anzahlBefehle2);
							
							einst.out("Führe Update aus.");
							// Anzeige initialisieren
							aktualisiereAnzeige(-1);
							
							// Update bis Teile von 2.0
							for (int i = 0; i < vorKon; i++){
								// Datenbank bearbeiten
								verb.aendern(befehle1.get(i));
								
								// Anzeige aktualisieren
								aktualisiereAnzeige(i);
							}
							// Verbindung zur DB schliessen, damit DAO ran kann
							verb.schliesseDB();
							
							// Posts konvertieren
							if(konv) konvPost20();
							
							// Passwort hier aktualisieren
							if(pwInkl) konvPW();
							
							// Verbindung neu starten
							verb.oeffneDB();
							
							// Update ab Teile von 2.0
							for (int i = 0; i < anzahlBefehle2; i++){
								// Datenbank bearbeiten
								verb.aendern(befehle2.get(i));
								
								// Anzeige aktualisieren
								aktualisiereAnzeige(anzahlBefehle1+i);
							}
							
							verb.schliesseDB();
							dispose();
							
						} catch (Exception e) {
							einst.outEx(e, 60, "Konnte Datenbank nicht updaten." + Einst.nl + "Datenbank könnte " +
									"beschädigt sein." + Einst.nl + "Bitte Gobo kontaktieren." + Einst.nl +
									"PostWriter wird beendet.");
							System.exit(1);
						}
						
					}
				});
				
				try {
					t.start();
					t.join();
				} catch (InterruptedException e) {
					einst.outEx(e, 65, "Konnte Datenbank nicht updaten." + Einst.nl + "PostWriter wird beendet.");
					System.exit(1);
				}
			}
			
		} else {
			// Wenn keine DB existiert
			einst.out("Datenbank existiert nicht. Erstelle Datenbank.");
			try {
				db.createNewFile();
				new DBErstellen(einst).erstelleDB();
			} catch (IOException e) {
				einst.outEx(e, 10, "Konnte Datenbank nicht erstellen." + Einst.nl +
						"PostWriter wird beendet.");
				System.exit(1);
			}

			return true;
		}
		
		// Hole Anzahl der Einstellungsdatensaetze
		einst.out("Prüfe, ob Einstellungen vorhanden sind.");
		ResultSet ergebnismenge = null;
		int anzahl = 0;
		String sql = "SELECT COUNT(*) FROM einstellungen;";
		verb.oeffneDB();
		ergebnismenge = verb.lesen(sql);
		try {
			anzahl = ergebnismenge.getInt(1);
		} catch (Exception e) {
			einst.outEx(e, 11, "Einstellungen konnten nicht gelesen werden.");
		}
		verb.schliesseDB();
		
		// Pruefe Anzahl der Einstellungsdatensaetze
		if(anzahl == 0){
			einst.out("Keine Einstellungen vorhanden.");
			return true;
		} else if(anzahl == 1){
			einst.out("Einstellungen vorhanden.");
			return false;
		} else {
			einst.outEx(12, "Datenbank muss geresettet werden.");
			return false;
		}
	}
	
	// Version der DB holen
	private void holeDBVersion(){
		ResultSet ergebnismenge = null;
		verb.oeffneDB();
		ergebnismenge = verb.lesen("SELECT * FROM haupttabelle;");
		try {
			dbVersionDB = (int) (ergebnismenge.getDouble(1)*10);
		} catch (Exception e) {
			einst.outEx(e, 13, "Einstellungen konnten nicht gelesen werden.");
		}
		verb.schliesseDB();
	}
	
	// Sammeln der Befehle, die ausgefuehrt werden sollen
	private void sammleBefehle() throws Exception{
		// Anzahl initialisieren
		anzahlBefehle1 = 0;
		befehle1 = new ArrayList<String>();
		anzahlBefehle2 = 0;
		befehle2 = new ArrayList<String>();
		vorKon = 0;
		stand = new ArrayList<Integer>();
		konv = false;
		pwInkl = false;
		
		// Update von v1.0 oder v1.3 auf 1.4
		if(dbVersionDB <= 13){
			befehle1.add("ALTER TABLE 'einstellungen' ADD COLUMN 'pw' VARCHAR(255);");
			befehle1.add("UPDATE haupttabelle SET version = '1.4';");
			stand.add(befehle1.size());
		}
		
		// Update von v1.4 auf 1.5
		if(dbVersionDB <= 14){
			befehle1.add("ALTER TABLE 'einstellungen' ADD COLUMN 'topo' VARCHAR(5) DEFAULT 'false';");
			befehle1.add("UPDATE 'einstellungen' SET topo = 'false';");
			befehle1.add("ALTER TABLE 'einstellungen' ADD COLUMN 'postanlegen' VARCHAR(5) " +
					"DEFAULT 'false';");
			befehle1.add("UPDATE 'einstellungen' SET postanlegen = 'false';");
			befehle1.add("UPDATE haupttabelle SET version = '1.5';");
			stand.add(befehle1.size());
		}
		
		// Update von v1.5 auf 1.6
		if(dbVersionDB <= 15){
			befehle1.add("ALTER TABLE 'bereich' ADD COLUMN 'wkat' INT DEFAULT 0;");
			befehle1.add("ALTER TABLE 'thema' ADD COLUMN 'wkat' INT DEFAULT 0;");
			befehle1.add("ALTER TABLE 'kurznachricht' ADD COLUMN 'wkat' INT DEFAULT 0;");
			befehle1.add("CREATE TABLE IF NOT EXISTS themenvorlage (tvid INT NOT NULL, " +
					"name VARCHAR(255) NOT NULL, inhalt VARCHAR(65532) NOT NULL, erstellt VARCHAR(16) NOT NULL);");
			befehle1.add("INSERT INTO 'themenvorlage' (tvid, name, inhalt, erstellt) " +
					"VALUES (0, '', '', '');");
			befehle1.add("INSERT INTO themenvorlage (tvid, name, inhalt, erstellt) " +
					"VALUES (1, 'Leeres Thema','<?text?>','" + einst.getNeuesPostDatum() + "');");
			befehle1.add("ALTER TABLE 'einstellungen' ADD COLUMN 'tvorlage' INT DEFAULT 1;");
			befehle1.add("UPDATE 'einstellungen' SET tvorlage = '1';");
			befehle1.add("UPDATE haupttabelle SET version = '1.6';");
			stand.add(befehle1.size());
		}
		
		// Update von v1.6 auf 2.0
		if(dbVersionDB <= 16){
			befehle1.add("ALTER TABLE 'einstellungen' ADD COLUMN 'formatierung' VARCHAR(65532);");
			befehle1.add("ALTER TABLE 'einstellungen' ADD COLUMN 'exportbaum' VARCHAR(5) DEFAULT 'false';");
			befehle1.add("ALTER TABLE 'einstellungen' ADD COLUMN 'nopaste' VARCHAR(1023) " +
					"DEFAULT 'http://nopaste.euirc.net/';");
			befehle1.add("ALTER TABLE 'einstellungen' ADD COLUMN 'autoupdate' VARCHAR(5) DEFAULT 'false';");
			befehle1.add("ALTER TABLE 'einstellungen' ADD COLUMN 'gkurz' INT NULL;");
			befehle1.add("ALTER TABLE 'einstellungen' ADD COLUMN 'gkurztext' INT NULL;");
			befehle1.add("ALTER TABLE 'einstellungen' ADD COLUMN 'kurzbereichname' " +
					"VARCHAR(2048) DEFAULT 'Kurznachrichten';");
			befehle1.add("UPDATE einstellungen SET formatierung = '" +
					DBErstellen.standardFormatierungPost + "';");
			befehle1.add("UPDATE einstellungen SET exportbaum = 'false';");
			befehle1.add("UPDATE einstellungen SET nopaste = 'http://nopaste.euirc.net/';");
			befehle1.add("UPDATE einstellungen SET autoupdate = 'true';");
			befehle1.add("UPDATE einstellungen SET gkurz = '3';");
			befehle1.add("UPDATE einstellungen SET gkurztext = '2';");
			befehle1.add("UPDATE einstellungen SET kurzbereichname = 'Kurznachrichten';");
			
			// Bereite int-Werte vor
			anzahlBefehle1 = befehle1.size();
			vorKon = anzahlBefehle1;
			konv = true;
			
			// Suche Anzahl
			// Themen
			ResultSet ergebnismenge = verb.lesen("SELECT COUNT(*) FROM thema;");
			anzahlBefehle1 += ergebnismenge.getInt(1);
			
			// Bereiche
			ergebnismenge = verb.lesen("SELECT COUNT(*) FROM bereich;");
			anzahlBefehle1 += ergebnismenge.getInt(1);
			
			// Kurznachrichten
			ergebnismenge = verb.lesen("SELECT COUNT(*) FROM kurznachricht;");
			anzahlBefehle1 += ergebnismenge.getInt(1);
			
			// Header
			ergebnismenge = verb.lesen("SELECT COUNT(*) FROM header;");
			anzahlBefehle1 += ergebnismenge.getInt(1);
			
			// Footer
			ergebnismenge = verb.lesen("SELECT COUNT(*) FROM footer;");
			anzahlBefehle1 += ergebnismenge.getInt(1);
			
			// Standardtexte
			ergebnismenge = verb.lesen("SELECT COUNT(*) FROM standardtext;");
			anzahlBefehle1 += ergebnismenge.getInt(1);
			
			// Themenvorlage nur ab Update von 1.5
			if(dbVersionDB == 16){
				ergebnismenge = verb.lesen("SELECT COUNT(*) FROM themenvorlage;");
				anzahlBefehle1 += ergebnismenge.getInt(1);
			}
			else{
				anzahlBefehle1++;
			}
			
			// Fehlerpuffer
//			anzahlBefehle1++;
			
			// Passwort
			ergebnismenge = verb.lesen("SELECT pw FROM einstellungen;");
			String pwV = ergebnismenge.getString("pw");
			if(pwV != null && !pwV.isEmpty()){
				pwInkl = true;
				anzahlBefehle1 = anzahlBefehle1+2;
			}
			
			// Nacharbeitung
			befehle2.add("UPDATE haupttabelle SET version = '2.0';");
			anzahlBefehle2 += befehle2.size();
			stand.add(anzahlBefehle1+befehle2.size());
		}
	}
	
	private void aktualisiereAnzeige(int i){
		// Wert der Anzeige aendern
		setFortschritt(getFortschritt()+1);
		
		switch (dbVersionDB) {
		case 10:
		case 13: 
			setAktion("Aktualisiere Datenbank auf Version 1.4...");
			if(i == stand.get(0)-1){
				stand.remove(0);
				dbVersionDB = 14;
				setAktion("Aktualisiere Datenbank auf Version 1.5...");
			}
			
			break;
		case 14: 
			setAktion("Aktualisiere Datenbank auf Version 1.5...");
			if(i == stand.get(0)-1){
				stand.remove(0);
				dbVersionDB = 15;
				setAktion("Aktualisiere Datenbank auf Version 1.6...");
			}
			
			break;
		case 15: 
			setAktion("Aktualisiere Datenbank auf Version 1.6...");
			if(i == stand.get(0)-1){
				stand.remove(0);
				dbVersionDB = 16;
				setAktion("Aktualisiere Datenbank auf Version 2.0...");
			}
			
			break;
		case 16: 
			setAktion("Aktualisiere Datenbank auf Version 2.0...");
			if(i == stand.get(0)-1){
				stand.remove(0);
				dbVersionDB = 20;
			}
			
			break;
		default:
			setAktion("Bitte warten...");
			break;
		}
	}
	
	// Posts konvertieren
	private void konvPost20() throws Exception{
		DAOs dao = einst.dao();
		
		// Themen
		ArrayList<Thema> alT = dao.sucheAlleThemen();
		for(int i = 0; i < alT.size(); i++){
			if(alT.get(i).getName().contains("=APOSTROPH=") || alT.get(i).getName().contains("\"") ||
					alT.get(i).getInhalt().contains("=APOSTROPH=") || alT.get(i).getInhalt().contains("\""))
			{
				alT.get(i).setName(anfuehrungsZeichenKonvert(alT.get(i).getName()));
				alT.get(i).setInhalt(anfuehrungsZeichenKonvert(alT.get(i).getInhalt()));
				
				dao.aendereThemaOhneObjekte(alT.get(i));
			}
			
			aktualisiereAnzeige(getFortschritt());
		}
		
		// Bereiche
		ArrayList<Bereich> alB = dao.sucheAlleBereiche();
		for(int i = 0; i < alB.size(); i++){
			if(alB.get(i).getName().contains("=APOSTROPH=") || alB.get(i).getName().contains("\""))
			{
				alB.get(i).setName(anfuehrungsZeichenKonvert(alB.get(i).getName()));
				
				dao.aendereBereich(alB.get(i));
			}
			
			aktualisiereAnzeige(getFortschritt());
		}
		
		// Kurznachrichten
		ArrayList<Kurznachricht> alK = dao.sucheAlleKurznachrichten();
		for(int i = 0; i < alK.size(); i++){
			if(alK.get(i).getInhalt().contains("=APOSTROPH=") || alK.get(i).getInhalt().contains("\""))
			{
				alK.get(i).setInhalt(anfuehrungsZeichenKonvert(alK.get(i).getInhalt()));
				
				dao.aendereKurznachrichtOhneObjekte(alK.get(i));
			}
			
			aktualisiereAnzeige(getFortschritt());
		}
		
		// Header
		ArrayList<Header> alH = dao.sucheAlleHeader();
		for(int i = 0; i < alH.size(); i++){
			if(alH.get(i).getName().contains("=APOSTROPH=") || alH.get(i).getName().contains("\"") ||
					alH.get(i).getInhalt().contains("=APOSTROPH=") || alH.get(i).getInhalt().contains("\""))
			{
				alH.get(i).setName(anfuehrungsZeichenKonvert(alH.get(i).getName()));
				alH.get(i).setInhalt(anfuehrungsZeichenKonvert(alH.get(i).getInhalt()));
				
				dao.aendereHeader(alH.get(i));
			}
			
			aktualisiereAnzeige(getFortschritt());
		}
		
		// Footer
		ArrayList<Footer> alF = dao.sucheAlleFooter();
		for(int i = 0; i < alF.size(); i++){
			if(alF.get(i).getName().contains("=APOSTROPH=") || alF.get(i).getName().contains("\"") ||
					alF.get(i).getInhalt().contains("=APOSTROPH=") || alF.get(i).getInhalt().contains("\""))
			{
				alF.get(i).setName(anfuehrungsZeichenKonvert(alF.get(i).getName()));
				alF.get(i).setInhalt(anfuehrungsZeichenKonvert(alF.get(i).getInhalt()));
				
				dao.aendereFooter(alF.get(i));
			}
			
			aktualisiereAnzeige(getFortschritt());
		}
		
		// Standardtexte
		ArrayList<Standardtext> alS = dao.sucheAlleStandardtexte();
		for(int i = 0; i < alS.size(); i++){
			if(alS.get(i).getName().contains("=APOSTROPH=") || alS.get(i).getName().contains("\"") ||
					alS.get(i).getInhalt().contains("=APOSTROPH=") || alS.get(i).getInhalt().contains("\"") ||
					alS.get(i).getInhalt().contains("<?var=") || alS.get(i).getInhalt().contains("=var?>"))
			{
				alS.get(i).setName(anfuehrungsZeichenKonvert(alS.get(i).getName()));
				alS.get(i).setInhalt(anfuehrungsZeichenKonvert(alS.get(i).getInhalt()));
				alS.get(i).setInhalt(alS.get(i).getInhalt().replaceAll("<\\?var=", Einst.vvarStart));
				alS.get(i).setInhalt(alS.get(i).getInhalt().replaceAll("=var\\?>", Einst.vvarEnde));
				
				dao.aendereStandardtext(alS.get(i));
			}
			
			aktualisiereAnzeige(getFortschritt());
		}
		
		// Themenvorlage
		ArrayList<Themenvorlage> alTV = dao.sucheAlleThemenvorlagen();
		for(int i = 0; i < alTV.size(); i++){
			if(alTV.get(i).getName().contains("=APOSTROPH=") || alTV.get(i).getName().contains("\"") ||
				alTV.get(i).getInhalt().contains("=APOSTROPH=") || alTV.get(i).getInhalt().contains("\"") ||
				alTV.get(i).getInhalt().contains("<?text?>"));
			{
				alTV.get(i).setName(anfuehrungsZeichenKonvert(alTV.get(i).getName()));
				alTV.get(i).setInhalt(anfuehrungsZeichenKonvert(alTV.get(i).getInhalt()));
				alTV.get(i).setInhalt(alTV.get(i).getInhalt().replaceAll("<\\?text\\?>", Einst.vthementext));
				
				dao.aendereThemenvorlage(alTV.get(i));
			}
			
			aktualisiereAnzeige(getFortschritt());
		}
	}
	
	// PW konvertieren
	private void konvPW() throws Exception{
		// Hole altes PW
		verb.oeffneDB();
		ResultSet ergebnismenge = verb.lesen("SELECT * FROM einstellungen;");
		String nutzer = ergebnismenge.getString("nutzer");
		String pwV = ergebnismenge.getString("pw");
		verb.schliesseDB();
		
		// Entschluesseln
		byte[] pwE = null;
		if(pwV != null){
			// Key generieren
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			DESKeySpec keySpec = new DESKeySpec("gasdjkfh89w4h6/()hjho/".getBytes());
			SecretKey key = keyFactory.generateSecret(keySpec);
			
			// Objekte selber erzeugen
			Cipher ent = Cipher.getInstance("DES");
			ent.init(Cipher.DECRYPT_MODE, key);
			
			// Entschlüsseln
			byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(pwV);
			pwE = ent.doFinal(dec);
		}
		aktualisiereAnzeige(getFortschritt());
		
		// PW speichern
		String md5 = Helfer.hashMD5(pwE);
		
		// Eigentlicher Speichervorgang
		verb.oeffneDB();
		verb.aendern("UPDATE einstellungen SET pw = '" + md5 + "' WHERE nutzer = '" + nutzer + "';");
		verb.schliesseDB();
		
		aktualisiereAnzeige(getFortschritt());
	}
	
	// Einfaches Aendern der Anfuehrungszeichen
	private String anfuehrungsZeichenKonvert(String text){
		text = text.replaceAll("=APOSTROPH=", Einst.vapostroph);
		text = text.replaceAll("\"", Einst.vanfuehrungszeichen);
		return text;
	}
}
