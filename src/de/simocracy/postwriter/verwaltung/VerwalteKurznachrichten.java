package de.simocracy.postwriter.verwaltung;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.themaEditor.*;

public class VerwalteKurznachrichten extends PostVerwaltung {

	private static final long serialVersionUID = -1366151485476363051L;
	private Kurznachricht kurz;
	private ArrayList<Kurznachricht> alKurz;
	private boolean aktiv;
	
	private JList<String> listKurzN;
	private DefaultListModel<String> listModelKurzN;
	private ItemListener ilFiltern;
	private MouseListener mlAuswahl;

	public VerwalteKurznachrichten(Einst einst, Hauptmenue hm) {
		super(einst, hm);
		beendenText = "Kurznachrichtenverwaltung";
		setTitle("Post Writer - " + beendenText);
		
		// Liste fuer Kurznachrichten einbauen
		listKurzN = new JList<String>();
		scrollPane.setViewportView(listKurzN);
		
		// listModelKurzN erstellen und einbauen
		listModelKurzN = new DefaultListModel<String>();
		listKurzN.setModel(listModelKurzN);
		
		// ComboBoxBereich deaktivieren
		comboBoxBereich.addItem("Kurznachrichten");
		comboBoxBereich.setSelectedIndex(0);
		comboBoxBereich.setEnabled(false);
		
		// ComboBoxName deaktivieren
		comboBoxName.setEnabled(false);
		
		// Kurznachrichten aus DB holen
		alKurz = einst.dao().sucheAlleKurznachrichtenMitObjekten();

		// MouseListener definieren (vor Auflistung, da dort aktiviert)
		mlAuswahl = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				infosAnzeigen();
				
				// Aktiv fuer Buttons setzen
				aktiv = true;
				
				// Bei Doppelklick bearbeiten
				if(e.getClickCount() > 1){
					bearbeiten();
				}
			}
		};
		
		// Kurznachrichten anzeigen
		kurzNauflisten();
		
		// ItemListener definieren
		ilFiltern = new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				kurzNauflisten();
			}
		};
		
		// Listener einbauen
		comboBoxPost.addItemListener(ilFiltern);
		comboBoxWichtigkeit.addItemListener(ilFiltern);
//		listKurzN.addMouseListener(mlAuswahl);
	}
	
	// Neue Kurznachricht erstellen
	protected void erstellen(){
		einst.out("Erstelle neue Kurznachricht.");
		setVisible(false);
		new NeueKurznachricht(einst, alPost, alKurz, this);
	}
	
	// Bestehende Kurznachricht bearbeiten
	protected void bearbeiten(){
		// Nur falls aktiv ThemenEditor starten
		if(aktiv){
			einst.out("Bearbeite Kurznachricht.");
			setVisible(false);
			new BearbeiteKurznachricht(einst, alPost, alKurz, this, kurz);
		}
		
		// Fehlermeldung, wenn nicht aktiv
		else{
			JOptionPane.showMessageDialog(null, "Bitte eine Kurznachricht auswählen.",
					"Keine Kurznachricht ausgewählt", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// Bestehende Kurznachricht loeschen
	protected void loeschen(){
		// Nur falls aktiv ThemenEditor loeschen
		if(aktiv){
			einst.out("Loesche Kurznachricht.");
			
			// Setze Kurznachricht
			kurz = alKurz.get(Helfer.sucheALKurzInhalt(alKurz, Helfer.textVar(listKurzN)));
			
			// Pruefe, ob bereits gepostet
			if(!kurz.getPost().getDatum().equals("")){
				einst.out("Kurznachricht wurde bereits gepostet.");
				JOptionPane.showMessageDialog(null, "Die gewählte Kurznachricht wurde bereits gepostet" + Einst.nl +
						"und kann daher nicht gelöscht werden.", "Kurznachricht breits gepostet",
						JOptionPane.ERROR_MESSAGE);
			}

			// Pruefe, ob wirklich geloescht werden soll
			else{
				int i = JOptionPane.showConfirmDialog(null, "Soll die Kurznachricht wirklich gelöscht werden?",
						"Löschen bestätigen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				
				// Wenn geloescht werden soll
				if(i == 0){
					// Loesche Thema
					einst.dao().loescheKurznachricht(kurz.getId());
					
					// Loesche Thema aus Array
					einst.out("Löschen abgeschlossen. Aktualisiere IDs.");
					alKurz.remove(kurz.getId());
					
					// Aktualisiere Datenbank
					for(int j = kurz.getId(); j < alKurz.size(); j++){
						// Aktualisiere Kurznachrichten
						einst.dao().aendereKurznachrichtKid(alKurz.get(j).getId(), j);
					}
					
					// Neueinlesen der Infos
					einlesen();
				}
				
				// Wenn nicht geloescht werden soll
				else{
					einst.out("Löschen abgebrochen.");
				}
			}
		}
		
		// Fehlermeldung, wenn nicht aktiv
		else{
			JOptionPane.showMessageDialog(null, "Bitte eine Kurznachricht auswählen.",
					"Keine Kurznachricht ausgewählt", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// Kurznachrichten filtern
	protected void kurzNauflisten(){
		// PostIndex setzen
		int postIndex = comboBoxPost.getSelectedIndex();
		int wichtigkeitIndex = comboBoxWichtigkeit.getSelectedIndex();
		
		// Alte Liste entfernen
		listModelKurzN.removeAllElements();
		
		// MouseListener deaktivieren
		listKurzN.removeMouseListener(mlAuswahl);
		
		// pid ermitteln
		int pid = 0;
		if(!comboBoxPost.getSelectedItem().equals(postLeertext)){
			if(comboBoxPost.getSelectedItem().equals("Zwischenspeicher")){
				pid = 0;
			}
			else{
				pid = Integer.parseInt(comboBoxPost.getItemAt(postIndex).toString().replaceFirst("ID: ", ""));
			}
		}
		
		// Wichtigkeit ermitteln
		int wk = 0;
		if(!comboBoxWichtigkeit.getSelectedItem().equals("Alle anzeigen")){
			wk = Integer.parseInt(comboBoxWichtigkeit.getSelectedItem().toString());
		}
		
		// Wenn nach nichts gesucht wird
		if (postIndex == 0 && wichtigkeitIndex == 0){
			// listModelKurzN normal fuellen
			for(int i = 1; i < alKurz.size(); i++){
				// Nur Kurznachrichten aus noch nicht geposteten Posts anzeigen
				if(alKurz.get(i).getPost().getDatum().equals("")){
					listModelKurzN.addElement(Helfer.varText(alKurz.get(i).getInhalt()));
				}
				else{
					// Wenn Post bereits gepostet
					alKurz.remove(i);
				}
			}
		}
		
		// Wenn nach post gesucht wird
		else if(postIndex != 0 && wichtigkeitIndex == 0){
			
			// Baue Kurznachrichten ein
			for(int i = 0; i < alKurz.size(); i++){
				
				// Filtern
				if(alKurz.get(i).getPid() == pid && alKurz.get(i).getId() != 0){
					listModelKurzN.addElement(Helfer.varText(alKurz.get(i).getInhalt()));
				}
			}
		}
		
		// Wenn nach wichtigkeit gesucht wird
		else if(postIndex == 0 && wichtigkeitIndex != 0){
			
			// Baue Kurznachrichten ein
			for(int i = 0; i < alKurz.size(); i++){
				
				// Filtern
				if(alKurz.get(i).getWkat() == wk && alKurz.get(i).getId() != 0){
					listModelKurzN.addElement(Helfer.varText(alKurz.get(i).getInhalt()));
				}
			}
		}
		
		// Wenn nach allem gesucht wird
		else if(postIndex != 0 && wichtigkeitIndex != 0){
			
			// Baue Themen ein
			for(int i = 1; i < alKurz.size(); i++){
				
				// Filtern
				if(alKurz.get(i).getPid() == pid && alKurz.get(i).getWkat() == wk && alKurz.get(i).getId() != 0){
					listModelKurzN.addElement(Helfer.varText(alKurz.get(i).getInhalt()));
				}
			}
		}
		
		// Falls keine Kurznachrichten
		if(listModelKurzN.size() == 0){
		}
		
		// Falls Kurznachrichten gefunden wurden
		else{
			// MouseListener aktivieren
			listKurzN.addMouseListener(mlAuswahl);
		}
		
		// Anzeige im PanelInfo setzen
		txtrPostid.setText("-");
		txtrErstelltDatum.setText("-");
		txtrWoerter.setText("-");
		txtrZeichen.setText("-");
		
		// Aktiv fuer Buttons setzen
		aktiv = false;
	}
	
	// Infos anzeigen
	private void infosAnzeigen(){
		// Kurznachricht aus Array holen
		kurz = alKurz.get(Helfer.sucheALKurzInhalt(alKurz, Helfer.textVar(listKurzN)));
		
		// Kurznachricht in panelInfos einbauen
		txtrPostid.setText(String.valueOf(kurz.getPid()));
		txtrErstelltDatum.setText(Helfer.datumL(kurz.getErstellt()));
		txtrWoerter.setText(Helfer.zahl(listKurzN));
		txtrZeichen.setText(String.valueOf(listKurzN.getSelectedValue().toString().length()));
	}
	
	// Daten neu aus DB einlesen
	public void einlesen(){
		// Kurznachrichten aus DB holen
		alKurz = einst.dao().sucheAlleKurznachrichtenMitObjekten();
		
		// Listener rauswerfen
		comboBoxPost.removeItemListener(ilFiltern);
		
		// Posts einlesen
		postEinlesen();
		
		// Kurznachrichten neu Auflisten
		kurzNauflisten();
		
		// Listener neu einbauen
		comboBoxPost.addItemListener(ilFiltern);
	}
	
	// PanelInfos definieren
	protected void panelInfos(){
		panelInfos = new JPanel();
		contentPane.add(panelInfos, "cell 0 5,grow");
		panelInfos.setLayout(new MigLayout("", "[10:n:10][][grow]", "[][][][][][][]"));
		
		txtrPost = new JTextArea();
		txtrPost.setBackground(UIManager.getColor("Panel.background"));
		txtrPost.setFocusable(false);
		txtrPost.setEditable(false);
		txtrPost.setText("Post:");
		panelInfos.add(txtrPost, "cell 0 0 2 1,grow");
		
		txtrPostid = new JTextArea();
		txtrPostid.setFocusable(false);
		txtrPostid.setEditable(false);
		txtrPostid.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrPostid, "cell 2 0,grow");
		
		txtrErstellt = new JTextArea();
		txtrErstellt.setBackground(UIManager.getColor("Panel.background"));
		txtrErstellt.setEditable(false);
		txtrErstellt.setFocusable(false);
		txtrErstellt.setText("Erstellt:");
		panelInfos.add(txtrErstellt, "cell 0 1 3 1,grow");
		
		txtrErstelltDatum = new JTextArea();
		txtrErstelltDatum.setFocusable(false);
		txtrErstelltDatum.setEditable(false);
		txtrErstelltDatum.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrErstelltDatum, "cell 1 2 2 1,grow");
		
		txtrAnzahl = new JTextArea();
		txtrAnzahl.setText("Anzahl Wörter:");
		txtrAnzahl.setFocusable(false);
		txtrAnzahl.setEditable(false);
		txtrAnzahl.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrAnzahl, "cell 0 3 3 1,grow");
		
		txtrWoerter = new JTextArea();
		txtrWoerter.setFocusable(false);
		txtrWoerter.setEditable(false);
		txtrWoerter.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrWoerter, "cell 1 4 2 1,grow");
		
		txtrAnzahlZeichen = new JTextArea();
		txtrAnzahlZeichen.setText("Anzahl Zeichen:");
		txtrAnzahlZeichen.setFocusable(false);
		txtrAnzahlZeichen.setEditable(false);
		txtrAnzahlZeichen.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrAnzahlZeichen, "cell 0 5 3 1,grow");
		
		txtrZeichen = new JTextArea();
		txtrZeichen.setFocusable(false);
		txtrZeichen.setEditable(false);
		txtrZeichen.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrZeichen, "cell 1 6 2 1,grow");
	}
}
