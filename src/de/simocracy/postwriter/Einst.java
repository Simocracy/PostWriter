package de.simocracy.postwriter;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.wikipedia.Wiki;

import de.simocracy.postwriter.datenhaltung.*;
import de.simocracy.postwriter.datumsrechner.DatumRechner;

public class Einst {
	
	// Variable Einstellungen
	private String nutzer = null;
	private int autoumbruch = 0;
	private int gbereich = 0;
	private int gthema = 0;
	private int gnormal = 0;
	private int gkurz = 0;
	private int gkurztext = 0;
	private int sheader = 0;
	private int sfooter = 0;
	private String wikiArtikel = null;
	private String verzeichnis = null;
	private boolean topo = false;
	private boolean postanlegen = false;
	private int sthemenvorlage = 0;
	private String formatierung = null;
	private boolean exportBaum = false;
	private String nopasteSeite = null;
	private boolean autoUpdate = false;
	private String kurzBereichName = null;
	
	// Feste Einstellungen
	final static public SimpleDateFormat ausgabeDatum = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	final static public SimpleDateFormat postDatum = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	final static public SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
	final static public String poliLink = "http://www.simforum.de/showthread.php?goto=newpost&t=166677";
	final static public String diskuLink = "http://www.simforum.de/showthread.php?goto=newpost&t=166676";
	final static public String wikiLink = "http://simocracy.de/Wikocracy:Portal";
	final static public String postenLink = "http://www.simforum.de/newreply.php?do=newreply&noquote=1&t=166677";
	final static public String pwrLink = "http://simocracy.de/PostWriter";
	final static public String datumsLink = "http://fluggs.simocracy.de/zeit/";
	final static public Image icon = Toolkit.getDefaultToolkit().getImage(
			Einst.class.getResource("/de/simocracy/postwriter/ressourcen/PWRIcon.png"));
	
	// Objekte, die ueberall gebraucht werden
	private DAOs dao;
	private DatumRechner dr;
	
	// Version des PostWriter
	final static public String version = "2.0.5";
	final static public int versionI = 205;
	
	// Version der Datenbank
	final static public String dbVersion = "2.0";
	final static public int dbVersionI = 20;
	
	/*
	 * Fehlerteil:
	 * Globale Ausgabevarianten:
	 * 	0: Konsole
	 * 	1: Schreibe in File logdatei
	 * 	2: Ausgabe fuer Entwicklungszwecke
	 * 
	 * Reihenfolge Fehlernummern:
	 * 	SQLiteVerbindung: 0x (1-5)
	 * 	Einstellungen: 0x (6-9)
	 *  DBUpdates (Pruefung): 1x (10-13)
	 * 	DBErstellen: 1x (14-15)
	 * 	DAOs (Einstellungen): 2x (20-26)
	 * 	DAOs: 3x, 4x (30-43)
	 * 	DAOs (PW): 5x (57-59)
	 *  DBUpdates (Ausfuehrung): 6x (60,65)
	 *  Hauptmenue (Datum): 6x (61)
	 *  Hauptmenue (Updateprüfung): 6x (62)
	 *  Updatepruefung: 6x (63-64)
	 *  DatumRechner: 6x (68-69)
	 *  PostExport: 7x (70-71,79)
	 *  CtP: 7x (72-78)
	 */
	
	/*
	 * Typennummern:
	 * 	0: Platzhalter
	 * 	1: Header
	 * 	2: Footer
	 * 	3: Standardtext
	 * 	4: Themenvorlage
	 */
	
	// Diagnoseeinstellungen
	private byte outputID;
	private File logdatei = null;
	private PrintWriter schreiben = null;
	private boolean entwicklung = false;
	
	/*
	 * Javaversion: 1.7
	 */
	
	/*
	 * Folgendes machen und in die Changelist:
	 * - mehrere themen mit gleichem namen - Im Postexporter einbauen
	 * - Post komplett selber schreiben
	 * - Mehr Fortschrittfenster
	 * - Fenster alle von Basisfenster erben
	 */
	
	////////////////////////////////////
	// Feste Textvariablen
	////////////////////////////////////
	
	// Allgemeine Variablen
	final static public String vthementext = "<text>";
	final static public String vthemenname = "<themaName>";
	final static public String vbereichname = "<bereichName>";
	final static public String vbUeberGroesse = "<buSize>";
	final static public String vtUeberGroesse = "<tuSize>";
	final static public String vtTextGroesse = "<ttSize>";
	final static public String vkUeberGroesse = "<kuSize>";
	final static public String vkurzTextGroesse = "<ktSize>";
	final static public String vkurzUeber = "<uKurz>";
	final static public String vkurzText = "<kurzText>";
	final static public String vheader = "<header>";
	final static public String vfooter = "<footer>";
	final static public String vnl = "<newline>";
	final static public String vdatumSY = "<dateSY>";
	final static public String vdatumRL = "<dateRL>";
	final static public String vbereichStart = "<bereich>";
	final static public String vbereichEnde = "</bereich>";
	final static public String vthemaStart = "<thema>";
	final static public String vthemaEnde = "</thema>";
	final static public String vkurzStart = "<kurznachricht>";
	final static public String vkurzEnde = "</kurznachricht>";
	final static public String vkurzBereichStart = "<kurzBereich>";
	final static public String vkurzBereichEnde = "</kurzBereich>";
	
	final static public String vkommentarStart = "<kom>";
	final static public String vkommentarEnde = "</kom>";
	
	// Interne Variablen
	final static public String vapostroph = "<apo>"; // Alt: =APOSTROPH=
	final static public String vanfuehrungszeichen = "<anf>";
	final static public String vvarStart = "<var>";
	final static public String vvarEnde = "</var>"; // Alt: =var?>
	final static public char vTopoA = '„';
	final static public char vTopoE = '“';
	final static public String index = "INDEX";
	
	// Systemvars
//	final static private String nl = System.getProperty("line.separator"); // Neue Zeile ueber Systemcode
	final static public String nl = "\n"; // Neue Zeile ueber Java-Standardcode (Linux-Variante)
	final static public String fs = System.getProperty("file.separator");
	
	////////////////////////////////////
	// Constructor
	////////////////////////////////////
	
	public Einst(byte outputID) {
		this.dao = new DAOs(this);
		this.outputID = outputID;
		
		// Ermittle Verzeichnis
		ermittleVZ();
		
		// Logdatei bei Bedarf erstellen
		if(outputID == 1){
			erstelleLogDatei();
		}
		
		// Entwicklungseinstellungen
		if(outputID == 2){
			verzeichnis = "";
			this.outputID = 0;
			entwicklung = true;
		}
	}
	
	////////////////////////////////////
	// Methoden ab hier
	////////////////////////////////////
	
	// Suche nach Updates
	// Suche nach aktuelleren Version
	public void versionSuche(boolean endfenster) {
		Thread updatesuche = new Thread(new updatecheck(endfenster));
		updatesuche.start();
	}
	
	// Datenbank resetten
	public void resetDB(){
		out("Resette Datenbank.");
		DBErstellen rs = new DBErstellen(this);

		rs.setVisible(true);
		int j = -1;
		do{
			rs.setResetStatus(++j);
		}
		while(j<rs.getAktionenResetSize());
		
		rs.dispose();
	}
	
	// Ausfuehrung für Erststart
	public void erstStart(String nutzer){
		this.nutzer = nutzer;
		dao.legeNutzerAn(nutzer);
	}
	
	// Hole alle Einstellungen aus Datenbank in den RAM
	public void holeEinstellungen(){
		out("Hole Einstellungen aus Datenbank.");
		ArrayList<String> al = dao.holeEinstellungen();
		
		nutzer = al.get(0);
		autoumbruch = Integer.parseInt(al.get(1));
		gbereich = Integer.parseInt(al.get(2));
		gthema = Integer.parseInt(al.get(3));
		gnormal = Integer.parseInt(al.get(4));
		sheader = Integer.parseInt(al.get(5));
		sfooter = Integer.parseInt(al.get(6));
		wikiArtikel = al.get(7);
		topo = Boolean.parseBoolean(al.get(8));
		postanlegen = Boolean.parseBoolean(al.get(9));
		sthemenvorlage = Integer.parseInt(al.get(10));
		formatierung = al.get(11);
		exportBaum = Boolean.parseBoolean(al.get(12));
		nopasteSeite = al.get(13);
		autoUpdate = Boolean.parseBoolean(al.get(14));
		gkurz = Integer.parseInt(al.get(15));
		gkurztext = Integer.parseInt(al.get(16));
		kurzBereichName = al.get(17);
	}
	
	// Speichere Einstellungen in Datenbank
	public void speicherEinstellungen(int autoumbruch, int gbereich, int gthema, int gnormal, String wikiArtikel,
			boolean topo, boolean postanlegen, boolean exportBaum, String nopasteSeite, boolean autoUpdate,
			String nutzer, int gkurz, int gkurztext, String kurzBereichName){
		out("Speichere Einstellungen.");
		this.autoumbruch = autoumbruch;
		this.gbereich = gbereich;
		this.gthema = gthema;
		this.gnormal = gnormal;
		this.wikiArtikel = wikiArtikel;
		this.topo = topo;
		this.postanlegen = postanlegen;
		this.exportBaum = exportBaum;
		this.nopasteSeite = nopasteSeite;
		this.autoUpdate = autoUpdate;
		this.gkurz = gkurz;
		this.gkurztext = gkurztext;
		this.kurzBereichName = kurzBereichName;
		
		String alterNutzer = this.nutzer;
		this.nutzer = nutzer;
		
		dao.speichereEinstellungen(autoumbruch, gbereich, gthema, gnormal, wikiArtikel, topo,
				postanlegen, exportBaum, nopasteSeite, autoUpdate, alterNutzer, gkurz, gkurztext, kurzBereichName);
	}
	
	// Speicher Standardheader
	public void speicherSHeader(int sheader){
		this.sheader = sheader;

		dao.speicherSHeader(nutzer, sheader);
	}
	
	// Speicher Standardfooter
	public void speicherSFooter(int sfooter){
		this.sfooter = sfooter;

		dao.speicherSFooter(nutzer, sfooter);
	}
	
	// Speicher Standardvorlage
	public void speicherSThemenVorlage(int sthemenvorlage){
		this.sthemenvorlage = sthemenvorlage;

		dao.speicherSThemenVorlage(nutzer, sthemenvorlage);
	}
	
	// Speicher Format
	public void speicherFormatierung(String formatierung){
		this.formatierung = formatierung;

		dao.speicherFormatierung(nutzer, formatierung);
	}
	
	// Arbeitsverzeichnis speichern
	@SuppressWarnings("resource")
	public void speicherVerzeichnis(File vz, boolean erstStart){
		File verzeichnisDatei = new File(System.getProperty("user.home") + fs + ".PostWriter" + fs + ".verzeichnis");
		FileChannel fcIn = null;
		FileChannel fcOut = null;
		
		try {
			// Alte Dateien einlesen
			File datenbank = null;
			if(!erstStart){
				datenbank = new File(verzeichnis + "posts.sqlite");
			}
			
			// Verzeichnis aendern
			PrintWriter writer = new PrintWriter(new FileWriter(verzeichnisDatei, false), true);
			writer.write(vz.getAbsolutePath());
			writer.close();
			
			verzeichnis = vz.getAbsolutePath() + fs;
			
			// Alte Dateien verschieben
			if(!erstStart){
				File datenbankNeu = new File(verzeichnis + "posts.sqlite");
				
				// Datenbank verschieben
				fcIn = new FileInputStream(datenbank).getChannel();
				fcOut = new FileOutputStream(datenbankNeu).getChannel();

				fcIn.transferTo(0, fcIn.size(), fcOut);
				
				fcIn.close();
				fcOut.close();
				
				// Alte Dateien loeschen
				datenbank.delete();
				
				out("Neues Arbeitsverzeichnis: " + verzeichnis);
			}
			
		} catch (Exception e) {
			outEx(e, 6, "Konnte Verzeichnis nicht speichern.");
		}
	}
	
	// Datum ermitteln
	private static Date getDatum(){
		return new Date();
	}
	
	// Link oeffnen
	public void oeffneLink(String link){
		try {
			Desktop.getDesktop().browse(new URI(link));
		} catch (Exception e1) {
			outEx(e1, 7, "Konnte Link nicht öffnen: " + link);
		} 
	}
	
	////////////////////////////////////
	// Links oeffnen
	////////////////////////////////////

	
	public void oeffnePoli(){
		out("Öffne Politikthread.");
		oeffneLink(poliLink);
	}
	
	public void oeffneDisku(){
		out("Öffne Diskussionsthread.");
		oeffneLink(diskuLink);
	}
	
	public void oeffneWiki(){
		out("Öffne Wikocracy.");
		oeffneLink(wikiLink);
	}

	public void setALWikiArtikel(JButton button){
		if(getWikiArtikel() == null || getWikiArtikel().equals("")){
			button.setEnabled(false);
			button.setToolTipText("Kein Link Wikiartikel verfügbar.");
		} else {
			button.setToolTipText("Wikiartikel im Browser öffnen");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					out("Öffne Wikiartikel.");
					oeffneLink(getWikiArtikel());
				}
			});
		}
	}
	
	////////////////////////////////////
	// Methoden, um einzelne Einstellungen im Programm auszulesen
	////////////////////////////////////
	
	public String getNutzer() {
		return nutzer;
	}
	
	public int getAutoumbruch() {
		return autoumbruch;
	}

	public int getGbereich() {
		return gbereich;
	}

	public int getGthema() {
		return gthema;
	}

	public int getGnormal() {
		return gnormal;
	}

	public int getGKurzUeber() {
		return gkurz;
	}

	public int getGKurzText() {
		return gkurztext;
	}

	public String getWikiArtikel() {
		return wikiArtikel;
	}
	
	public boolean isTopo() {
		return topo;
	}

	public boolean isPostanlegen() {
		return postanlegen;
	}

	public boolean isExportBaum() {
		return exportBaum;
	}

	public String getNeuesPostDatum(){
		return postDatum.format(getDatum());
	}
	
	public byte getOutputID(){
		return outputID;
	}
	
	public String getVerzeichnis(){
		return verzeichnis;
	}
	
	public DAOs dao(){
		return dao;
	}
	
	public DatumRechner dr(){
		return dr;
	}
	
	public int getSHeader(){
		return sheader;
	}
	
	public int getSFooter(){
		return sfooter;
	}
	
	public boolean isEntwicklung(){
		return entwicklung;
	}
	
	public int getSThemenVorlage(){
		return sthemenvorlage;
	}
	
	public String getFormatierung(){
		return formatierung;
	}
	
	public String getNopasteSeite() {
		return nopasteSeite;
	}
	
	public boolean isAutoUpdate(){
		return autoUpdate;
	}
	
	public String getKurzBereichName() {
		return kurzBereichName;
	}
	
	////////////////////////////////////
	// Fehlerteil
	////////////////////////////////////
	
	private void ermittleVZ(){
		// Erstelle Dateiobjekt mit VZ
		File verzeichnisDatei = new File(System.getProperty("user.home") + fs + ".PostWriter" + fs + ".verzeichnis");
		
		// Pruefe, ob Datei existiert
		if(verzeichnisDatei.exists()){
			// Wenn verzeichnisDatei exisitert, Verzeichnis auslesen
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(verzeichnisDatei));
				verzeichnis = reader.readLine() + fs;
				reader.close();
			} catch (Exception e) {
				outEx(e, 8, "Konnte Verzeichnis nicht finden.");
			}
		}
		else{
			// Wenn Datei nicht exisitert, Verzeichnis abfragen
			try {
				// Verzeichnis Anlegen
				new File(verzeichnisDatei.getParent()).mkdirs();
				
				// Auswahl des neuen Verzeichnisses
				JOptionPane.showMessageDialog(null, "Bitte das Arbeitsverzeichnis festlegen.\nIm " +
						"Arbeitsverzeichnis wird die Datenbank mit allen Posts gespeichert.",
						"Installation", JOptionPane.INFORMATION_MESSAGE);
				
				JFileChooser fc = new JFileChooser(verzeichnisDatei.getParent());
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int result = fc.showDialog(null, "Verzeichnis verwenden");
				
				if(result == JFileChooser.CANCEL_OPTION){
					JOptionPane.showMessageDialog(null, "Bitte ein Verzeichnis auswählen,\nin dem die " +
							"Datenbank gespeichert werden soll.", "Arbeitsverzeichnis auswählen",
							JOptionPane.WARNING_MESSAGE);
					ermittleVZ();
				}
				
				// Datei mit inhalt erstellen
				verzeichnisDatei.createNewFile();
				
				speicherVerzeichnis(fc.getSelectedFile(), true);
			} catch (Exception e) {
				outEx(e, 9, "Konnte Verzeichnis nicht speichern.");
			}
		}
	}
	
	private void erstelleLogDatei(){
		logdatei = new File(getVerzeichnis() + "logs" + System.getProperty("file.separator") +
				datum.format(getDatum()) + ".log");
		try {
			if(!logdatei.exists()){
				new File(logdatei.getParent()).mkdirs();
				logdatei.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void schreibeDatum(){
		try {
			schreiben = new PrintWriter(new FileWriter(logdatei, true),true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		schreiben.print(ausgabeDatum.format(getDatum()) + ": ");
	}
	
	// Normale Ausgabe
	public void out(String ausgabe){
		switch (this.outputID) {
		case 0:
			System.out.println(ausgabeDatum.format(getDatum()) + ": " + ausgabe);
			break;
		case 1:
			schreibeDatum();
			schreiben.println(ausgabe);
			schreiben.close();
			break;
		}
	}
	
	private void out(Exception ausgabe){
		switch (this.outputID) {
		case 0:
			ausgabe.printStackTrace();
			break;
		case 1:
			schreibeDatum();
			ausgabe.printStackTrace(schreiben);
			schreiben.close();
			break;
		}
	}
	
	// Fehler mit Meldung
	public void outEx(int code, String name){
		out("Fehler: " + name + " (Code " + code + "):");
		JOptionPane.showMessageDialog(null, "Es ist ein Fehler aufgetreten:" + nl + name +
				nl + "Fehlercode: " + code, "Fehler", JOptionPane.WARNING_MESSAGE);
	}
	
	public void outEx(Exception err, int code, String name){
		out("Fehler: " + name + " (Code " + code + "):");
		out(err);
		JOptionPane.showMessageDialog(null, "Es ist ein Fehler aufgetreten:" + nl + name +
				nl + "Fehlercode: " + code + nl + "Fehlerbeschreibung: " + err.getMessage(),
				"Fehler", JOptionPane.WARNING_MESSAGE);
	}
	
	public void outEx(Exception err, String name){
		out("Fehler: " + name + ":");
		out(err);
		JOptionPane.showMessageDialog(null, "Es ist ein Fehler aufgetreten:" + nl + name +
				nl + "Fehlerbeschreibung: " + err.getMessage(),
				"Fehler", JOptionPane.WARNING_MESSAGE);
	}
	
	// Klasse für Updatecheck, um diesen in eigenem Thread auszuführen
	private class updatecheck implements Runnable{
		public updatecheck(boolean endfenster) {
			this.endfenster = endfenster;
		}
		
		private boolean endfenster;
		
		public void run() {
			out("Starte Versionscheck.");
			String seitentext = "";
			boolean fehler = false;
			
			try {
				// Wikizugriff
				
				Wiki wiki = new Wiki("simocracy.de", "");
				wiki.setUsingCompressedRequests(false);
				wiki.setUserAgent("PostWriter Version Checker");
				
				
				
				seitentext = wiki.getPageText("Hauptseite");
				
				wiki.logout();
						
			} catch (Exception e) {
				outEx(63, "Fehler bei Prüfung auf Updates." + nl +
						"Besteht eine Internetverbindung?");
				fehler = true;
			}
			
			try {
				// Bearbeitung
				
				String gefunden[] = seitentext.split("\n");
				
				String versionsString[] = new String[3];
				boolean javaGefunden = false;
				for(int i = 0; i < gefunden.length; i++) {
					if(gefunden[i].startsWith("PWR-Java")) {
						versionsString = gefunden[i].split(";");
						javaGefunden = true;
					}
				}
				
				if(!javaGefunden) {
					throw new Exception("Keine Versionsinformationen für die Java-Version gefunden.");
				}
				
				String[] gefundeneVersion = versionsString[1].split("\\.");
				int neueVersion = Integer.parseInt(gefundeneVersion[0] + gefundeneVersion[1] + gefundeneVersion[2]);
				out("Gefundene Version: " + versionsString[1]);
				
				if(neueVersion > versionI) {
					// Ausgabe, dass neue Version gefunden wurde
					Object[] optionen = {"OK", "Download", "Wiki"};
					
					int variante = JOptionPane.showOptionDialog(null, "Es wurde eine neue Version des PostWriter " +
							"gefunden.\nEs wird empfohlen, diese zu installieren.\nNeue Version: " + versionsString[1] +
							"\nInstallierte Version: " + version, "Neue Version gefunden",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, optionen, null);
					
					// Pruefen, was getan werden soll
					if(variante == JOptionPane.NO_OPTION){
						// Wenn Download geoeffnet werden soll
						oeffneLink(versionsString[2]);
					}
					else if(variante == JOptionPane.CANCEL_OPTION) {
						// Wenn Wiki geoeffnet werden soll
						oeffneLink(pwrLink);
					}
				}
				else {
					// Wenn keine neue Version gefunden wurde und das Fenster angezeigt werden soll
					if(endfenster) {
						JOptionPane.showMessageDialog(null, "Es wurde keine neue Version des PostWriter gefunden.",
								"Updatesuche", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			} catch(Exception e) {
				if(!fehler) {
					outEx(e, 64, "Fehler bei Prüfung auf Updates.");
				}
			}
			
			out("Versionscheck beendet.");
		}
	}
}
