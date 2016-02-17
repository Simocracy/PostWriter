package de.simocracy.postwriter.verwaltung;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.themaEditor.*;

public class VerwalteHeader extends EinstellungenVerwaltung {

	private static final long serialVersionUID = 9216783271169299084L;
	private Header header;
	private ArrayList<Header> alHeader;

	public VerwalteHeader(Einst einst, Hauptmenue hm) {
		super(einst, hm);
		beendenText = "Headerverwaltung";
		setTitle("Post Writer - " + beendenText);
		
		// ComboBoxBereich anzeige
		comboBoxBereich.addItem("Header");
		
		// Header aus DB holen
		alHeader = einst.dao().sucheAlleHeader();
		
		// ComboBoxName auffuellen
		headerAuflisten();
		
		// Auswahl des ersten Headers
		erstAuswahl();
		
		// Einbau des ItemListeners
		comboBoxName.addItemListener(ilAuswahl);
	}
	
	// Neuer Header erstellen
	protected void erstellen(){
		einst.out("Erstelle neuen Header.");
		setVisible(false);
		new NeuerHeader(einst, alHeader, this);
	}
	
	// Header bearbeiten
	protected void bearbeiten(){
		einst.out("Bearbeite Header.");
		setVisible(false);
		new BearbeiteHeader(einst, alHeader, this, header);
	}
	
	// Header loeschen
	protected void loeschen(){
		einst.out("Lösche Header.");
		
		// Setze Thema
		header = alHeader.get(Helfer.sucheALHeaderName(alHeader, Helfer.textVar(comboBoxName)));
		
		// Pruefe, ob wirklich geloescht werden soll
		int i = JOptionPane.showConfirmDialog(null, "Soll der Header wirklich gelöscht werden?",
				"Löschen bestätigen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		// Wenn geloescht werden soll
		if(i == JOptionPane.YES_OPTION){
			// Loesche Header
			einst.dao().loescheHeader(header.getId());
			
			// Loesche Header aus Array
			einst.out("Löschen abgeschlossen. Aktualisiere IDs.");
			alHeader.remove(header.getId());
			
			// Aktualisiere Datenbank
			for(int j = header.getId(); j < alHeader.size(); j++){
				// Aktualisiere Header
				einst.dao().aendereHeaderHid(alHeader.get(j).getId(), j);
			}
			
			// Neueinlesen der Infos
			einlesen();
		}
		
		// Wenn nicht geloescht werden soll
		else{
			einst.out("Löschen abgebrochen.");
		}
	}
	
	// Header als Standard festlegen
	protected void standard(){
		einst.out("Lege neuen Standardheader fest.");
		
		einst.speicherSHeader(header.getId());
		btnStandard.setEnabled(false);
	}
	
	// Header auflisten
	private void headerAuflisten(){
		// Alte Liste entfernen
		comboBoxName.removeAllItems();
		
		// Box fuellen
		for(int i = 1; i < alHeader.size(); i++){
			comboBoxName.addItem(Helfer.varText(alHeader.get(i).getName()));
		}
		
		// Erstauswahl
		erstAuswahl();
	}
	
	// Erstauswahl eines Headers
	private void erstAuswahl(){
		// Wenn es Header gibt
		if(comboBoxName.getItemCount() > 0){
			comboBoxName.setSelectedItem(Helfer.varText(alHeader.get(Helfer.sucheALHeaderHid(alHeader,
					einst.getSHeader())).getName()));
			auswahl();
		}
		
		// Wenn es keine Themen gibt
		else{
			txtrInhalt.setText("");
			txtrErstelltDatum.setText("-");
			txtrWoerter.setText("-");
			txtrZeichen.setText("-");
		}
	}
	
	// Auswahl eines Headers
	protected void auswahl(){
		// Setze header
		header = alHeader.get(Helfer.sucheALHeaderName(alHeader, Helfer.textVar(comboBoxName)));
		
		// Baue Zeug aus thema in Anzeige ein
		txtrInhalt.setText(Helfer.varText(header.getInhalt()));
		txtrErstelltDatum.setText(Helfer.datumL(header.getDatum()));
		txtrWoerter.setText(Helfer.zahl(txtrInhalt));
		txtrZeichen.setText(String.valueOf(txtrInhalt.getText().length()));
		
		// Standardbutton einstellen
		if(header.getId() == einst.getSHeader()){
		// Wenn Footer bereits Standardfooter
			btnStandard.setToolTipText("Der aktuell gewählte Header ist bereits als Standard festgelegt");
			btnStandard.setEnabled(false);
		}else{
		// Wenn Footer nicht Standardfooter
			btnStandard.setToolTipText("Aktuell gewählten Header als Standard festlegen");
			btnStandard.setEnabled(true);
		}
	}
	
	// Neueinlesen der Header
	public void einlesen(){
		// Header aus DB holen
		alHeader = einst.dao().sucheAlleHeader();
		
		// ItemListener entfernen
		comboBoxName.removeItemListener(ilAuswahl);
		
		// Header auffuellen
		headerAuflisten();
		
		// ItemListener neu einbauen
		comboBoxName.addItemListener(ilAuswahl);
	}
}
