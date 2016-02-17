package de.simocracy.postwriter;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JButton;

import de.simocracy.postwriter.datumsrechner.*;
import de.simocracy.postwriter.frameEinstellungen.*;
import de.simocracy.postwriter.postexport.*;
import de.simocracy.postwriter.verwaltung.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import java.awt.GridLayout;

public class Hauptmenue extends Fenster {

	private static final long serialVersionUID = 5611670268615366516L;
	private JPanel contentPane;
	private Einst einst;
	private InitBild ib;
	private JLabel lblThemenBearbeiten;
	private JButton btnThemaNeu;
	private JLabel lblPostBearbeiten;
	private JButton btnBereiche;
	private JButton btnPosten;
	private JButton btnPostwriterBeenden;
	private JButton btnCredits;
	private JButton btnWikiartikel;
	private JButton btnEinstellungen;
	private JLabel lblWichtigeLinks;
	private JButton btnPolitikthread;
	private JButton btnAnmeldungsUndDiskuthread;
	private JButton btnWikocracy;
	private JButton btnThemaEditieren;
	private JButton btnPosts;
	private JButton btnHeaderFooter;
	private JButton btnStandardtexte;
	private JLabel lblExportieren;
	private JButton btnDatumsrechner;
	private JLabel lblAktuellesSydatum;
	private JTextPane txtpnSytext;
	private JPanel panel;
	private JLabel lblHauptmenpwrb;
	
	public Hauptmenue(Einst einst, InitBild ib) {
		super(430,430);
		this.einst = einst;
		this.ib = ib;
		
		setTitle("Simocracy PostWriter 2.0");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[215,grow,fill][215,grow,fill]", "[grow,fill][grow,fill][fill][grow,fill][grow,fill][grow,fill][grow,fill][fill][grow,fill][grow,fill][fill][grow,fill][10:n,fill][grow,fill]"));
		
		btnEinstellungen = new JButton("Einstellungen");
		btnEinstellungen.setMnemonic('s');
		btnEinstellungen.setToolTipText("Einstellungsfenster öffnen");
		btnEinstellungen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oeffneEinstellungsfenster();
			}
		});
		
		panel = new JPanel();
		contentPane.add(panel, "cell 0 0 1 2,grow");
		panel.setLayout(new GridLayout(3, 0, 0, 0));
		
		lblHauptmenpwrb = new JLabel("Hauptmenü");
		panel.add(lblHauptmenpwrb);
		
		lblAktuellesSydatum = new JLabel();
		panel.add(lblAktuellesSydatum);
		lblAktuellesSydatum.setText("Aktuelles SY-Datum:");
		
		txtpnSytext = new JTextPane();
		panel.add(txtpnSytext);
		txtpnSytext.setText("Berechne SY-Datum...");
		txtpnSytext.setBackground(UIManager.getColor("Panel.background"));
		txtpnSytext.setFocusable(false);
		txtpnSytext.setEditable(false);
		contentPane.add(btnEinstellungen, "cell 1 0");
		
		btnDatumsrechner = new JButton("Datumsrechner");
		btnDatumsrechner.setMnemonic('d');
		btnDatumsrechner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				oeffneDatumsrechner();
			}
		});
		contentPane.add(btnDatumsrechner, "cell 1 1");
		
		lblWichtigeLinks = new JLabel("Wichtige Internetlinks");
		contentPane.add(lblWichtigeLinks, "cell 0 2");
		
		btnPolitikthread = new JButton("Politikthread");
		btnPolitikthread.setMnemonic('o');
		btnPolitikthread.setToolTipText("Politikthread im Browser öffnen");
		btnPolitikthread.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oeffnePoli();
			}
		});
		contentPane.add(btnPolitikthread, "cell 0 3");
		
		btnAnmeldungsUndDiskuthread = new JButton("Diskussionsthread");
		btnAnmeldungsUndDiskuthread.setMnemonic('u');
		btnAnmeldungsUndDiskuthread.setToolTipText("Dikussionsthread im Browser öffnen");
		btnAnmeldungsUndDiskuthread.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oeffneDisku();
			}
		});
		contentPane.add(btnAnmeldungsUndDiskuthread, "cell 1 3");
		
		btnWikocracy = new JButton("Wikocracy");
		btnWikocracy.setMnemonic('w');
		btnWikocracy.setToolTipText("Wikocracy-Portal im Browser öffnen");
		btnWikocracy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oeffneWiki();
			}
		});
		contentPane.add(btnWikocracy, "cell 0 4");
		
		btnWikiartikel = new JButton("Wikiartikel");
		btnWikiartikel.setMnemonic('a');
		contentPane.add(btnWikiartikel, "cell 1 4");
		
		lblThemenBearbeiten = new JLabel("Posten");
		contentPane.add(lblThemenBearbeiten, "cell 0 5");
		
		btnThemaNeu = new JButton("Themen");
		btnThemaNeu.setMnemonic('t');
		btnThemaNeu.setToolTipText("Themenverwaltung öffnen");
		btnThemaNeu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				oeffneThemen();
			}
		});
		contentPane.add(btnThemaNeu, "cell 0 6");
		
		btnThemaEditieren = new JButton("Kurznachrichten");
		btnThemaEditieren.setMnemonic('k');
		btnThemaEditieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oeffneKurzN();
			}
		});
		btnThemaEditieren.setToolTipText("Kurznachrichten verwalten");
		contentPane.add(btnThemaEditieren, "cell 1 6");
		
		btnBereiche = new JButton("Bereiche");
		btnBereiche.setMnemonic('b');
		btnBereiche.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oeffneBereiche();
			}
		});
		
		btnStandardtexte = new JButton("Vorlagen");
		btnStandardtexte.setMnemonic('v');
		btnStandardtexte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				oeffneStandardT();
			}
		});
		contentPane.add(btnStandardtexte, "cell 0 8");
		
		btnHeaderFooter = new JButton("Header und Footer");
		btnHeaderFooter.setMnemonic('h');
		btnHeaderFooter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oeffneHeader();
			}
		});
		contentPane.add(btnHeaderFooter, "cell 1 8");
		btnBereiche.setToolTipText("Bereicheditor öffnen");
		contentPane.add(btnBereiche, "cell 0 9");
		
		btnPosten = new JButton("Export und Archiv");
		btnPosten.setMnemonic('e');
		btnPosten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oeffnePosten();
			}
		});
		
		btnPosts = new JButton("Posts");
		btnPosts.setMnemonic('p');
		btnPosts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oeffnePosts();
			}
		});
		btnPosts.setToolTipText("Posteditor öffnen");
		contentPane.add(btnPosts, "cell 1 9");
		
		lblExportieren = new JLabel("Posts exportieren");
		contentPane.add(lblExportieren, "cell 0 10 2 1");
		btnPosten.setToolTipText("Im Politikthread posten");
		contentPane.add(btnPosten, "cell 0 11 2 1");
		
		btnPostwriterBeenden = new JButton("PostWriter beenden");
		btnPostwriterBeenden.setMnemonic('b');
		btnPostwriterBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				beenden();
			}
		});
		contentPane.add(btnPostwriterBeenden, "cell 0 13");
		
		btnCredits = new JButton("Credits");
		btnCredits.setMnemonic('c');
		btnCredits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oeffneCredits();
			}
		});
		btnCredits.setToolTipText("Credits anzeigen");
		contentPane.add(btnCredits, "cell 1 13");
		
		lblPostBearbeiten = new JLabel("Verwalten");
		contentPane.add(lblPostBearbeiten, "cell 0 7");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				beenden();
			}
		});
	}
	
	////////////////////////////////////
	// Methoden für die Buttons
	////////////////////////////////////
	
	private void oeffneEinstellungsfenster(){
		einst.out("Öffne Einstellungsfenster.");
		setVisible(false);
		new EinstellungenNormal(einst, this);
	}
	
	private void oeffneDatumsrechner(){
		einst.out("Öffne Datumsrechner.");
		setVisible(false);
		new FrameDRHm(einst, this);
	}
	
	public void oeffnePoli(){
		einst.out("Öffne Politikthread.");
		einst.oeffneLink(Einst.poliLink);
	}
	
	public void oeffneDisku(){
		einst.out("Öffne Diskussionsthread.");
		einst.oeffneLink(Einst.diskuLink);
	}
	
	public void oeffneWiki(){
		einst.out("Öffne Wikocracy.");
		einst.oeffneLink(Einst.wikiLink);
	}
	
	private void oeffneWikiArtikel(){
		einst.out("Öffne Wikiartikel.");
		einst.oeffneLink(einst.getWikiArtikel());
	}
	
	private void oeffneThemen(){
		einst.out("Öffne Themenverwaltung.");
		setVisible(false);
		new VerwalteThemen(einst, this);
	}
	
	private void oeffneKurzN(){
		einst.out("Öffne Kurznachrichtenverwaltung.");
		setVisible(false);
		new VerwalteKurznachrichten(einst, this);
	}
	
	private void oeffneStandardT(){
		einst.out("Öffne Vorlagenverwaltung.");
		setVisible(false);
		new VerwalteVorlagen(einst, this);
	}
	
	private void oeffneHeader(){
		einst.out("Öffne Header- und Footerverwaltung.");
		setVisible(false);
		new VerwalteHeaderFooter(einst, this);
	}
	
	private void oeffneBereiche(){
		einst.out("Öffne Bereicheditor.");
		setVisible(false);
		new BereichEditor(einst, this);
	}
	
	private void oeffnePosts(){
		einst.out("Öffne Posteditor.");
		setVisible(false);
		new PostEditor(einst, this);
	}
	
	private void oeffnePosten(){
		einst.out("Öffne Postexporter.");
		setVisible(false);
		if(einst.isExportBaum()){
			// Baumansicht oeffnen
			new PostExportBaum(einst, this);
		}
		else {
			// Normaler Export oeffnen
			new PostExport(einst, this);
		}
	}
	
	private void beenden(){
		einst.out("Beende PostWriter.");
		System.exit(0);
	}
	
	private void oeffneCredits(){
		einst.out("Öffne Creditsfenster.");
		new Credits(einst).setVisible(true);
	}
	
	////////////////////////////////////
	// Sonstige Methoden
	////////////////////////////////////
	
	// Anzeige fuer aktuelles Datum
	private void datumsberechnung(){
		txtpnSytext.setText("Berechne SY-Datum...");
		String datumRL = einst.getNeuesPostDatum();
		try {
			// Parsen
			int tag = Integer.parseInt(datumRL.substring(0, 2));
			int mon = Integer.parseInt(datumRL.substring(3, 5));
			int jahr = Integer.parseInt(datumRL.substring(6, 10));
			int stu = Integer.parseInt(datumRL.substring(11, 13));
			int min = Integer.parseInt(datumRL.substring(14, 16));
			
			// Berechnen
			txtpnSytext.setText(DatumRechner.rLSyString(tag, mon, jahr, stu, min, einst));
		} catch (Exception e) {
			einst.outEx(e, 60, "Konnte Datum nicht berechnen.");
		}
	}
	
	// Setze ActionListener fuer Wikiartikel-Button
	public void setALWikiArtikel(JButton button){
		if(einst.getWikiArtikel() == null || einst.getWikiArtikel().equals("")){
			button.setEnabled(false);
			button.setToolTipText("Kein Link Wikiartikel verfügbar.");
		} else {
			button.setToolTipText("Wikiartikel im Browser öffnen");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					oeffneWikiArtikel();
				}
			});
		}
	}
	
	// Ausfuehrung bei Erststart
	protected void erststart(){
		if(ib.isVisible()) ib.dispose();
		new EinstellungenErststart(einst, this);
	}
	
	// Ausfuehrung bei normalem Start
	public void startePW(){
		einst.holeEinstellungen();
		timerUpdate();
		datumsrechnen();
		setALWikiArtikel(btnWikiartikel);
		ib.dispose();
		setVisible(true);
	}
	
	// Regelmaessige Ausfuehrung der Datumsberechnug
	private void datumsrechnen(){
		try {
			Timer timer = new Timer();
			timer.schedule(
				new TimerTask() {
					public void run(){
						datumsberechnung();
					}
				}, 1000, 3000);
		} catch (Exception e) {
			einst.outEx(e, 61, "Konnte Datum nicht berechnen.");
		}
	}
	
	// Auf Updates pruefen
	private void timerUpdate(){
		// Nur ausfuehren, wenn nicht im Entwicklungsmodus
		if(!einst.isEntwicklung() && einst.isAutoUpdate()){
			try {
				Timer timer = new Timer();
				timer.schedule(
					new TimerTask() {
						public void run(){
							einst.versionSuche(false);
						}
					}, 50);
			} catch (Exception e) {
				einst.outEx(e, 62, "Konnte nicht auf Update überprüfen.");
			}
		}
	}
}
