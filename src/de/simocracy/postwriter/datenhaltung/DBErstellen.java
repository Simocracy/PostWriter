package de.simocracy.postwriter.datenhaltung;

import de.simocracy.postwriter.*;

public class DBErstellen extends FortschrittsFenster {

	private static final long serialVersionUID = -5913701594113146089L;
	private Einst einst;
	private SQLiteVerbindung verb;
	private String[] aktionErstellen;
	private String[] aktionReset;

	public DBErstellen(Einst einst) {
		super("Datenbankreset", "Bitte warten. Datenbank wird neu erstellt...",
				"Datenbankreset wird initialisiert...");
		this.einst = einst;
		this.verb = new SQLiteVerbindung(einst);
	}
	
	public void setResetStatus(int wert){
		ausfuehrungReset(wert);
		setFortschritt(wert+1);
	}
	
	// Datenbank erstellen
	public void erstelleDB() {
		erstelleSQL();
		try {
			verb.oeffneDB();
			for(int i = 0; i < aktionErstellen.length; i++){
				verb.aendern(aktionErstellen[i]);
			}
			verb.schliesseDB();
		} catch (Exception e) {
			verb.schliesseDB();
			einst.outEx(e, 14, "Datenbank konnte nicht erstellt werden. PostWriter wird beendet.");
			System.exit(0);
		}
	}
	
	private void ausfuehrungReset(int nummer){
		// Befehle einlesen
		if(nummer == 0){
			erstelleSQL();
		}
		
		// Befehle ausfuehren
		else{
			legeTextFest(nummer-1);
			try {
				verb.oeffneDB();
				verb.aendern(aktionReset[nummer-1]);
				verb.schliesseDB();
			} catch (Exception e) {
				verb.schliesseDB();
				einst.outEx(e, 15, "Reset konnte nicht ausgeführt werden. Datenbank muss " +
						"manuell gelöscht werden.\nPostWriter wird beendet.");
				System.exit(0);
			}
		}
	}
	
	private void legeTextFest(int nr){
		if(aktionReset[nr].startsWith("DROP TABLE")){
			setAktion("Lösche bestehende Datenbank...");
		}
		else if(aktionReset[nr].startsWith("CREATE TABLE IF NOT EXISTS haupttabelle") ||
				aktionReset[nr].startsWith("INSERT INTO haupttabelle")){
			setAktion("Lege neue Haupttabelle an...");
		}
		else if(aktionReset[nr].startsWith("CREATE TABLE")){
			setAktion("Erstelle neue Datenbank...");
		}
		else if(aktionReset[nr].startsWith("INSERT INTO")){
			setAktion("Füge Datensätze ein...");
		}
	}
	
	private void erstelleSQL(){
		
		// SQL-Befehle erstellen
		String[] sqlErstellen = {
				"CREATE TABLE IF NOT EXISTS haupttabelle (version VARCHAR(3) NOT NULL);",
				"INSERT INTO haupttabelle (version) VALUES ('" + Einst.dbVersion + "');",
				
				"CREATE TABLE IF NOT EXISTS post (pid INT NOT NULL, datum VARCHAR(16), url VARCHAR(255));",
				"CREATE TABLE IF NOT EXISTS bereich (bid INT NOT NULL,name VARCHAR(255) NOT NULL, " +
						"wkat INT DEFAULT 0);",
				"CREATE TABLE IF NOT EXISTS thema (tid INT NOT NULL, name VARCHAR(255) NOT NULL, inhalt " +
						"VARCHAR(65532) NOT NULL, erstellt VARCHAR(16) NOT NULL, bid INT NOT NULL," +
						"pid INT NOT NULL, wkat INT DEFAULT 0);",
				"CREATE TABLE IF NOT EXISTS kurznachricht (kid INT NOT NULL,inhalt VARCHAR(1023) NOT NULL," +
						" erstellt VARCHAR(16) NOT NULL, pid INT NOT NULL, wkat INT DEFAULT 0);",
				"CREATE TABLE IF NOT EXISTS header (hid INT NOT NULL, name VARCHAR(255) NOT NULL, inhalt " +
						"VARCHAR(65532) NOT NULL, erstellt VARCHAR(16) NOT NULL)",
				"CREATE TABLE IF NOT EXISTS footer (fid INT NOT NULL, name VARCHAR(255) NOT NULL, inhalt " +
						"VARCHAR(65532) NOT NULL, erstellt VARCHAR(16) NOT NULL)",
				"CREATE TABLE IF NOT EXISTS standardtext (sid INT NOT NULL, name VARCHAR(255) NOT NULL, inhalt " +
						"VARCHAR(65532) NOT NULL, erstellt VARCHAR(16) NOT NULL);",
				"CREATE TABLE IF NOT EXISTS themenvorlage (tvid INT NOT NULL, name VARCHAR(255) NOT NULL, inhalt " +
						"VARCHAR(65532) NOT NULL, erstellt VARCHAR(16) NOT NULL);",
				
				"CREATE TABLE IF NOT EXISTS einstellungen (nutzer VARCHAR(255) NOT NULL, autoumbruch INT NULL, " +
						"gbereich INT NULL, gthema INT NULL, gnormal INT NULL, header INT DEFAULT 0, " +
						"footer INT DEFAULT 0, wikiartikel VARCHAR(255) NULL, pw VARCHAR(32) NULL, " +
						"topo VARCHAR(5) DEFAULT 'false', postanlegen VARCHAR(5) DEFAULT 'false', " +
						"tvorlage INT DEFAULT 1, formatierung VARCHAR(65532) DEFAULT '" + standardFormatierungPost +
						"', exportbaum VARCHAR(5) DEFAULT 'false', nopaste VARCHAR(1023) DEFAULT " +
						"'http://nopaste.euirc.net/', autoupdate VARCHAR(5) DEFAULT 'false', gkurz INT NULL," +
						"gkurztext INT NULL, kurzbereichname VARCHAR(2048) DEFAULT 'Kurznachrichten');",
				
				"INSERT INTO post (pid, datum, url) VALUES (0, '', '');",
				"INSERT INTO bereich (bid, name) VALUES (0, '');",
				"INSERT INTO thema (tid, name, inhalt, erstellt, bid, pid) VALUES (0, '', '', '', 0, 0);",
				"INSERT INTO kurznachricht (kid, inhalt, erstellt, pid) " +
						"VALUES (0, 'Bayern muss das Tripple gewinnen!', '', 0);",
				"INSERT INTO header (hid, name, inhalt, erstellt) VALUES (0, '', '', '');",
				"INSERT INTO footer (fid, name, inhalt, erstellt) VALUES (0, '', '', '');",
				"INSERT INTO standardtext (sid, name, inhalt, erstellt) VALUES (0, '', '', '');",
				"INSERT INTO themenvorlage (tvid, name, inhalt, erstellt) VALUES (0, '', '', '');",
				
				"INSERT INTO standardtext (sid, name, inhalt, erstellt) VALUES (1, 'Begrüßung neuer Staaten', " +
						"'Wir begrüßen die neuen Staaten herzlich in der Staatengemeinschaft und bieten einen " +
						"Botschaftenaustausch an, um erste diplomatische Beziehungen zu eröffnen.', '" +
						einst.getNeuesPostDatum() + "');",
				"INSERT INTO themenvorlage (tvid, name, inhalt, erstellt) VALUES(1, 'Leeres Thema', '<?text?>', '" +
						einst.getNeuesPostDatum() + "');"
		};
		
		// SQL-Befehle loeschen
		String[] sqlLoeschen = {
				"DROP TABLE IF EXISTS post;",
				"DROP TABLE IF EXISTS bereich;",
				"DROP TABLE IF EXISTS thema;",
				"DROP TABLE IF EXISTS kurznachricht;",
				"DROP TABLE IF EXISTS einstellungen;",
				"DROP TABLE IF EXISTS standardtext",
				"DROP TABLE IF EXISTS header",
				"DROP TABLE IF EXISTS footer",
				"DROP TABLE IF EXISTS themenvorlage",
				"DROP TABLE IF EXISTS haupttabelle;",
		};
		
		// Reset in Array
		aktionReset = new String[sqlLoeschen.length+sqlErstellen.length];
		int c;
		for(c = 0; c < sqlLoeschen.length; c++){
			aktionReset[c] = sqlLoeschen[c];
		}
		
		// Erstellen in Array
		aktionErstellen = new String[sqlErstellen.length];
		for(int i = 0; i < sqlErstellen.length; i++){
			aktionErstellen[i] = sqlErstellen[i];
			aktionReset[c+i] = sqlErstellen[i];
		}
		
		// Maximum fuer ProgressBar einstellen
		setMaximum(aktionReset.length);
	}
	
	public int getAktionenResetSize(){
		return aktionReset.length;
	}
	
	// Inhalt der Standardformatierung
	public static final String standardFormatierungPost = Einst.vheader + Einst.vnl +
		Einst.vnl +
		Einst.vbereichStart + "[B][SIZE=" + Einst.vbUeberGroesse + "]" +
							Einst.vbereichname + "[/SIZE][/B]" + Einst.vnl +
		Einst.vnl +
		Einst.vthemaStart + "[B][SIZE=" + Einst.vtUeberGroesse + "]" + Einst.vthemenname + "[/SIZE][/B]" + Einst.vnl +
		Einst.vnl +
		"[SIZE=" + Einst.vtTextGroesse + "]" + Einst.vthementext + "[/SIZE]" + Einst.vnl +
		Einst.vnl +
		Einst.vthemaEnde + Einst.vbereichEnde + Einst.vkurzBereichStart + Einst.vnl +
		Einst.vnl +
		"[B][SIZE=" + Einst.vkUeberGroesse + "]" +
							Einst.vkurzUeber + "[/SIZE][/B]" + Einst.vnl +
		"[SIZE=" + Einst.vkurzTextGroesse + "][LIST]" + Einst.vnl +
		Einst.vkurzStart + "[*]" + Einst.vkurzText + Einst.vnl + Einst.vkurzEnde +
		"[/LIST][/SIZE]" + Einst.vnl +
		Einst.vnl + Einst.vkurzBereichEnde +
		Einst.vfooter;
}
