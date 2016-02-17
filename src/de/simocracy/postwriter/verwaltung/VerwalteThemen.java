package de.simocracy.postwriter.verwaltung;

import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.themaEditor.*;

public class VerwalteThemen extends PostVerwaltung {

	private static final long serialVersionUID = -4733563649970082680L;
	private Thema thema;
	private ArrayList<Bereich> alBereich;
	private ArrayList<Thema> alThema;
	private ItemListener ilFiltern;
	private JTextArea txtrInhalt;
	private JTextArea txtrBereich;
	private JTextArea txtrBereichname;
	private ArrayList<Themenvorlage> alTVorlage;

	public VerwalteThemen(Einst einst, Hauptmenue hm) {
		super(einst, hm);
		beendenText = "Themenverwaltung";
		setTitle("Post Writer - " + beendenText);
		
		// Button fuer Neues aus einer Vorlage
		btnNeuausVorl.setToolTipText("Aus einer Vorlage ein neues Thema erstellen");
		btnNeuausVorl.setEnabled(true);
		
		// Themenvorlagen aus Datenbank lesen
		alTVorlage = einst.dao().sucheAlleThemenvorlagen();
		
		// Platzhalter aus alTVorlage werfen
		alTVorlage.remove(0);
		
		// TextArea fuer Inhalt einbauen
		txtrInhalt = new JTextArea();
		txtrInhalt.setLineWrap(true);
		txtrInhalt.setWrapStyleWord(true);
		scrollPane.setViewportView(txtrInhalt);
		txtrInhalt.setEditable(false);
		txtrInhalt.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		
		// Bereiche und Themen aus DB holen
		alBereich = einst.dao().sucheAlleBereicheMitArrays();
		alThema = einst.dao().sucheAlleThemenMitObjekten();
		
		// Bereiche auffuellen
		bereichEinbau();
		
		// Themen auffuellen
		themenAuflisten();
		
		// ItemListener zum Filtern
		ilFiltern = new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				comboBoxName.removeItemListener(ilAuswahl);
				themenAuflisten();
				comboBoxName.addItemListener(ilAuswahl);
			}
		};
		
		erstThema();
		
		// ItemListener Einbau
		comboBoxPost.addItemListener(ilFiltern);
		comboBoxBereich.addItemListener(ilFiltern);
		comboBoxWichtigkeit.addItemListener(ilFiltern);
		comboBoxName.addItemListener(ilAuswahl);
	}
	
	// Neues Thema erstellen
	protected void erstellen(){
		einst.out("Erstelle neues Thema.");
		setVisible(false);
		new NeuesThema(einst, alPost, alBereich, alThema, this,
				Helfer.varText(alTVorlage.get(einst.getSThemenVorlage()-1).getInhalt()));
	}
	
	// Neues Thema aus Vorlage erstellen
	protected void erstellen2(){
		// ArrayList konvertieren
		ArrayList<String> tvorlageAl = new ArrayList<String>();
		for(int i = 0; i < alTVorlage.size(); i++){
			tvorlageAl.add(Helfer.varText(alTVorlage.get(i).getName()));
		}
		Object[] tvorlagenArray = tvorlageAl.toArray();
		
		// Abfragen, welche Vorlage verwendet werden soll
		Object wahlO = JOptionPane.showInputDialog(null, "Themenvorlage auswählen:", "Neues Thema",
				JOptionPane.PLAIN_MESSAGE, null, tvorlagenArray, tvorlagenArray[0]);
		
		// Nur weiterarbeiten, wenn kein abbruch
		if(wahlO != null){
			einst.out("Erstelle neues Thema aus Vorlage.");
			
			// Suche Vorlage zum weitergeben
			int index = Helfer.sucheALTVorlageName(alTVorlage, Helfer.textVar(wahlO.toString()));
			
			// ThemenEditor starten
			setVisible(false);
			new NeuesThema(einst, alPost, alBereich, alThema, this,
					Helfer.varText(alTVorlage.get(index).getInhalt()));
		}
	}
	
	// Bestehendes Thema bearbeiten
	protected void bearbeiten(){
		einst.out("Bearbeite Thema.");
		setVisible(false);
		new BearbeiteThema(einst, alPost, alBereich, alThema, this, thema);
	}
	
	// Bestehendes Thema loeschen
	protected void loeschen(){
		einst.out("Lösche Thema.");
		
		// Setze Thema
		thema = alThema.get(Helfer.sucheALThemaName(alThema, Helfer.textVar(comboBoxName)));
		
		// Pruefe, ob bereits gepostet
		if(!thema.getPost().getDatum().equals("")){
			einst.out("Thema wurde bereits gepostet.");
			JOptionPane.showMessageDialog(null, "Das gewählte Thema wurde bereits gepostet" + Einst.nl +
					"und kann daher nicht gelöscht werden.", "Thema breits gepostet", JOptionPane.ERROR_MESSAGE);
		}
		
		// Pruefe, ob wirklich geloescht werden soll
		else{
			int i = JOptionPane.showConfirmDialog(null, "Soll das Thema wirklich gelöscht werden?",
					"Löschen bestätigen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			// Wenn geloescht werden soll
			if(i == JOptionPane.YES_OPTION){
				// Loesche Thema
				einst.dao().loescheThema(thema.getId());
				
				// Loesche Thema aus Array
				einst.out("Löschen abgeschlossen. Aktualisiere IDs.");
				alThema.remove(thema.getId());
				
				// Aktualisiere Datenbank
				for(int j = thema.getId(); j < alThema.size(); j++){
					// Aktualisiere Themen
					einst.dao().aendereThemaTid(alThema.get(j).getId(), j);
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
	
	// Bereiche auffuellen
	private void bereichEinbau(){
		// Leere bisherige Liste
		comboBoxBereich.removeAllItems();
		
		// Neue Liste einbauen
		comboBoxBereich.addItem("Alle anzeigen");
		for(int i = 1; i < alBereich.size(); i++){
			comboBoxBereich.addItem(Helfer.varText(alBereich.get(i).getName()));
		}
		comboBoxBereich.addItem(alBereich.get(0).getName());
	}

	// Thema filtern
	private void themenAuflisten(){
		// Indizes sammeln
		int postIndex = comboBoxPost.getSelectedIndex();
		int bereichIndex = comboBoxBereich.getSelectedIndex();
		int wichtigkeitIndex = comboBoxWichtigkeit.getSelectedIndex();
		
		// Alte Liste entfernen
		comboBoxName.removeAllItems();
		
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
		
		// bid ermitteln
		int bid = 0;
		if(!comboBoxBereich.getSelectedItem().equals("Alle anzeigen")){
			bid = alBereich.get(Helfer.sucheALBereichName(
					alBereich, Helfer.textVar(comboBoxBereich))).getId();
		}
		
		// Wichtigkeit ermitteln
		int wk = 0;
		if(!comboBoxWichtigkeit.getSelectedItem().equals("Alle anzeigen")){
			wk = Integer.parseInt(comboBoxWichtigkeit.getSelectedItem().toString());
		}
		
		// Wenn nach nichts gesucht wird
		if (postIndex == 0 && bereichIndex == 0 && wichtigkeitIndex == 0){
			// ComboBoxName normal fuellen
			for(int i = 1; i < alThema.size(); i++){
				// Nur Themen aus noch nicht geposteten Posts anzeigen
				if(alThema.get(i).getPost().getDatum().equals("")){
					comboBoxName.addItem(Helfer.textVar(alThema.get(i).getName()));
				}
				else{
					// Wenn Post bereits gepostet
					alThema.remove(i);
				}
			}
		}
		
		// Wenn nur nach post gesucht wird
		else if(postIndex != 0 && bereichIndex == 0 && wichtigkeitIndex == 0){
			
			// Baue Themen ein
			for(int i = 1; i < alThema.size(); i++){
				
				// Filtern
				if(alThema.get(i).getPid() == pid){
					comboBoxName.addItem(Helfer.textVar(alThema.get(i).getName()));
				}
			}
		}
		
		// Wenn nur nach bereich gesucht wird
		else if(postIndex == 0 && bereichIndex != 0 && wichtigkeitIndex == 0){
			
			// Baue Themen ein
			for(int i = 1; i < alThema.size(); i++){
				
				//Filtern
				if(alThema.get(i).getBid() == bid){
					comboBoxName.addItem(Helfer.textVar(alThema.get(i).getName()));
				}
			}
		}
		
		// Wenn nur nach wichtigkeit gesucht wird
		else if(postIndex == 0 && bereichIndex == 0 && wichtigkeitIndex != 0){
			
			// Baue Themen ein
			for(int i = 1; i < alThema.size(); i++){
				
				//Filtern
				if(alThema.get(i).getWkat() == wk){
					comboBoxName.addItem(Helfer.textVar(alThema.get(i).getName()));
				}
			}
		}
		
		// Wenn nach post und bereich gesucht wird
		else if(postIndex != 0 && bereichIndex != 0 && wichtigkeitIndex == 0){
			
			// Baue Themen ein
			for(int i = 1; i < alThema.size(); i++){
				
				// Filtern
				if(alThema.get(i).getPid() == pid && alThema.get(i).getBid() == bid){
					comboBoxName.addItem(Helfer.textVar(alThema.get(i).getName()));
				}
			}
		}
		
		// Wenn nach post und wichtigkeit gesucht wird
		else if(postIndex != 0 && bereichIndex == 0 && wichtigkeitIndex != 0){
			
			// Baue Themen ein
			for(int i = 1; i < alThema.size(); i++){
				
				// Filtern
				if(alThema.get(i).getPid() == pid && alThema.get(i).getWkat() == wk){
					comboBoxName.addItem(Helfer.textVar(alThema.get(i).getName()));
				}
			}
		}
		
		// Wenn nach bereich und wichtigkeit gesucht wird
		else if(postIndex == 0 && bereichIndex != 0 && wichtigkeitIndex != 0){
			
			// Baue Themen ein
			for(int i = 1; i < alThema.size(); i++){
				
				//Filtern
				if(alThema.get(i).getBid() == bid && alThema.get(i).getWkat() == wk){
					comboBoxName.addItem(Helfer.textVar(alThema.get(i).getName()));
				}
			}
		}
		
		// Wenn nach allem gesucht wird
		else{
			
			// Baue Themen ein
			for(int i = 1; i < alThema.size(); i++){
				
				// Filtern
				if(alThema.get(i).getPid() == pid && alThema.get(i).getBid() == bid &&
						alThema.get(i).getWkat() == wk){
					comboBoxName.addItem(Helfer.textVar(alThema.get(i).getName()));
				}
			}
		}
		
		// Auswahl erstes Thema
		erstThema();
	}
	
	// Erstes Thema auswaehlen
	private void erstThema(){
		// Wenn es Themen gibt
		if(comboBoxName.getItemCount() > 0){
			comboBoxName.setSelectedIndex(0);
			auswahl();
		}
		
		// Wenn es keine Themen gibt
		else{
			txtrPostid.setText("");
			txtrBereichname.setText("");
			txtrInhalt.setText("");
			txtrErstelltDatum.setText("-");
			txtrWoerter.setText("-");
			txtrZeichen.setText("-");
		}
	}
	
	// Normale Themenauswahl
	protected void auswahl(){
		// Setze thema
		thema = alThema.get(Helfer.sucheALThemaName(alThema, Helfer.textVar(comboBoxName)));
		
		// Baue Zeug aus thema in Anzeige ein
		txtrBereichname.setText(Helfer.varText(thema.getBereich().getName()));
		txtrInhalt.setText(Helfer.varText(thema.getInhalt()));
		txtrErstelltDatum.setText(Helfer.datumL(thema.getErstellt()));
		txtrWoerter.setText(Helfer.zahl(txtrInhalt));
		txtrZeichen.setText(String.valueOf(txtrInhalt.getText().length()));
		txtrInhalt.setCaretPosition(0);
		
		// Anzeige fuer Post
		if(thema.getPid() == 0){
			txtrPostid.setText("Zw.-Speicher");
		}else{
			txtrPostid.setText(String.valueOf(thema.getPid()));
		}
	}
	
	// Anweisung zum Einlesen aus DB
	public void einlesen(){
		// Bereiche, Themen und -vorlagen aus DB holen
		alBereich = einst.dao().sucheAlleBereicheMitArrays();
		alThema = einst.dao().sucheAlleThemenMitObjekten();
		
		// ItemListener rauswerfen
		comboBoxPost.removeItemListener(ilFiltern);
		comboBoxBereich.removeItemListener(ilFiltern);
		comboBoxWichtigkeit.removeItemListener(ilFiltern);
		comboBoxName.removeItemListener(ilAuswahl);
		
		// Posts einlesen
		postEinlesen();
		
		// Bereiche einlesen
		bereichEinbau();
		
		// Themen auffuellen
		themenAuflisten();
		
		// ItemListener neu einbauen
		comboBoxPost.addItemListener(ilFiltern);
		comboBoxBereich.addItemListener(ilFiltern);
		comboBoxWichtigkeit.addItemListener(ilFiltern);
		comboBoxName.addItemListener(ilAuswahl);
	}
	
	// PanelInfos definieren
	protected void panelInfos(){
		panelInfos = new JPanel();
		contentPane.add(panelInfos, "cell 0 5,grow");
		panelInfos.setLayout(new MigLayout("", "[10:n:10][][grow]", "[][][][][][][][][]"));
		
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
		
		txtrBereich = new JTextArea();
		txtrBereich.setFocusable(false);
		txtrBereich.setEditable(false);
		txtrBereich.setBackground(UIManager.getColor("Panel.background"));
		txtrBereich.setText("Bereich:");
		panelInfos.add(txtrBereich, "cell 0 1 3 1,grow");
		
		txtrBereichname = new JTextArea();
		txtrBereichname.setBackground(UIManager.getColor("Panel.background"));
		txtrBereichname.setEditable(false);
		txtrBereichname.setFocusable(false);
		panelInfos.add(txtrBereichname, "cell 1 2 2 1,grow");
		
		txtrErstellt = new JTextArea();
		txtrErstellt.setBackground(UIManager.getColor("Panel.background"));
		txtrErstellt.setEditable(false);
		txtrErstellt.setFocusable(false);
		txtrErstellt.setText("Erstellt:");
		panelInfos.add(txtrErstellt, "cell 0 3 3 1,grow");
		
		txtrErstelltDatum = new JTextArea();
		txtrErstelltDatum.setFocusable(false);
		txtrErstelltDatum.setEditable(false);
		txtrErstelltDatum.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrErstelltDatum, "cell 1 4 2 1,grow");
		
		txtrAnzahl = new JTextArea();
		txtrAnzahl.setText("Anzahl Wörter:");
		txtrAnzahl.setFocusable(false);
		txtrAnzahl.setEditable(false);
		txtrAnzahl.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrAnzahl, "cell 0 5 3 1,grow");
		
		txtrWoerter = new JTextArea();
		txtrWoerter.setFocusable(false);
		txtrWoerter.setEditable(false);
		txtrWoerter.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrWoerter, "cell 1 6 2 1,grow");
		
		txtrAnzahlZeichen = new JTextArea();
		txtrAnzahlZeichen.setText("Anzahl Zeichen:");
		txtrAnzahlZeichen.setFocusable(false);
		txtrAnzahlZeichen.setEditable(false);
		txtrAnzahlZeichen.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrAnzahlZeichen, "cell 0 7 3 1,grow");
		
		txtrZeichen = new JTextArea();
		txtrZeichen.setFocusable(false);
		txtrZeichen.setEditable(false);
		txtrZeichen.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrZeichen, "cell 1 8 2 1,grow");
	}
}
