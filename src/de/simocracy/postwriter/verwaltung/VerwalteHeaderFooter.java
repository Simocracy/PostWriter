package de.simocracy.postwriter.verwaltung;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.Einst;
import de.simocracy.postwriter.Hauptmenue;
import de.simocracy.postwriter.Helfer;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.themaEditor.*;

public class VerwalteHeaderFooter extends EinstellungenVerwaltung {

	private static final long serialVersionUID = 4574902719204544612L;
	private Header header;
	private ArrayList<Header> alHeader;
	private Footer footer;
	private ArrayList<Footer> alFooter;
	private boolean isHeader;

	public VerwalteHeaderFooter(Einst einst, Hauptmenue hm) {
		super(einst, hm);
		beendenText = "Header- und Footerverwaltung";
		setTitle("Post Writer - " + beendenText);
		
		// Neu-Buttons einstellen
		btnNeu.setText("Neuer Header");
		btnNeu.setMnemonic('h');
		btnNeuausVorl.setText("Neuer Footer");
		btnNeuausVorl.setMnemonic('f');
		
		// ComboBoxBereich einstellen
		comboBoxBereich.addItem("Header und Footer");
		comboBoxBereich.addItem("Header");
		comboBoxBereich.addItem("Footer");
		comboBoxBereich.setEnabled(true);
		comboBoxBereich.setSelectedIndex(0);
		lblBereich.setText("Typ");
		
		// ItemListener fuer ComboBoxBereich
		comboBoxBereich.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				comboBoxName.removeItemListener(ilAuswahl);
				headerFooterFiltern();
				comboBoxName.addItemListener(ilAuswahl);
			}
		});
		
		// Header und Footer einlesen
		einlesen();
	}
	
	// Neuer Header erstellen
	protected void erstellen(){
		new NeuerHeader(einst, alHeader, this);
	}
	
	// Neue Footer erstellen
	protected void erstellen2(){
		new NeuerFooter(einst, alFooter, this);
	}
	
	// Header und Footer bearbeiten
	protected void bearbeiten(){
		// Pruefe, was bearbeitet werden soll

		if(isHeader){
			einst.out("Bearbeite Header.");
			setVisible(false);
			new BearbeiteHeader(einst, alHeader, this, header);
		}

		else if(!isHeader){
			einst.out("Bearbeite Footer.");
			setVisible(false);
			new BearbeiteFooter(einst, alFooter, this, footer);
		}
	}
	
	// Vorlage loeschen
	protected void loeschen(){
		
		// Pruefe, was geloescht werden soll
		if(isHeader){
			// Loeschen von Header
			einst.out("Lösche Header.");
			
			// Pruefe, ob wirklich geloescht werden soll
			int i = JOptionPane.showConfirmDialog(null, "Soll der Header wirklich gelöscht werden?",
					"Löschen bestätigen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			// Wenn geloescht werden soll
			if(i == JOptionPane.YES_OPTION){
				// Loesche Standardtext
				einst.dao().loescheStandardtext(header.getId());
				
				// Loesche Standardtext aus Array
				einst.out("Löschen abgeschlossen. Aktualisiere IDs.");
				alHeader.remove(header.getId());
				
				// Aktualisiere Datenbank
				for(int j = header.getId(); j < alHeader.size(); j++){
					// Aktualisiere Standardtexte in Datenbank
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
		
		else if(!isHeader){
			// Loeschen von Footer
			einst.out("Lösche Footer.");
			
			// Pruefe, ob wirklich geloescht werden soll
			int i = JOptionPane.showConfirmDialog(null, "Soll der Footer wirklich gelöscht werden?",
					"Löschen bestätigen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			// Wenn geloescht werden soll
			if(i == JOptionPane.YES_OPTION){
				// Loesche Themenvorlage
				einst.dao().loescheFooter(footer.getId());
				
				// Loesche Standardtext aus Array
				einst.out("Löschen abgeschlossen. Aktualisiere IDs.");
				alFooter.remove(footer.getId());
				
				// Aktualisiere Datenbank
				for(int j = footer.getId(); j < alFooter.size(); j++){
					// Aktualisiere Themenvorlagen in Datenbank
					einst.dao().aendereFooterFid(alFooter.get(j).getId(), j);
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
	
	// Header und Footer auflisten
	private void headerFooterFiltern(){
		// Alte Liste entfernen
		comboBoxName.removeAllItems();
		
		// Header bei Bedarf einfuegen
		if(comboBoxBereich.getSelectedItem().toString().contains("Header")){
			for(int i = 1; i < alHeader.size(); i++){
				comboBoxName.addItem(Helfer.varText(alHeader.get(i).getName()));
			}
		}
		
		// Footer bei Bedarf einfuegen
		if(comboBoxBereich.getSelectedItem().toString().contains("Footer")){
			for(int i = 1; i < alFooter.size(); i++){
				comboBoxName.addItem(Helfer.varText(alFooter.get(i).getName()));
			}
		}
		
		// Erstauswahl
		erstAuswahl();
	}
	
	// Erstauswahl eines Header oder Footer
	private void erstAuswahl(){
		// Wenn es Header und Footer gibt
		if(comboBoxName.getItemCount() > 0){
			comboBoxName.setSelectedIndex(0);
			auswahl();
		}
		
		// Wenn es keine Header oder Footer gibt
		else{
			txtrInhalt.setText("");
			txtrErstelltDatum.setText("-");
			txtrWoerter.setText("-");
			txtrZeichen.setText("-");
		}
	}
	
	// Header oder Footer als Standard festlegen
	protected void standard(){
		// Pruefe, was als Standard gesetzt werden soll
		if(isHeader){
			einst.out("Lege neuen Standard-Header fest.");
			
			einst.speicherSHeader(header.getId());
			btnStandard.setEnabled(false);
		}
		
		else if(!isHeader){
			einst.out("Lege neuen Standard-Footer fest.");
			
			einst.speicherSFooter(footer.getId());
			btnStandard.setEnabled(false);
		}
	}
	
	// Auswahl eines Header oder Footer
	protected void auswahl(){
		// Pruefe welcher Typ die Auswahl ist
		if(Helfer.sucheALHeaderName(alHeader, Helfer.textVar(comboBoxName)) != -1 &&
				alHeader.get(Helfer.sucheALHeaderName(alHeader, Helfer.textVar(comboBoxName))).getTyp() == 1){
			isHeader = true;
		}
		else if(Helfer.sucheALFooterName(alFooter, Helfer.textVar(comboBoxName)) != -1 &&
				alFooter.get(Helfer.sucheALFooterName(alFooter, Helfer.textVar(comboBoxName))).getTyp() == 2){
			isHeader = false;
		}
		
		// Falls Header
		if(isHeader){
			
			// Setze Header
			header = alHeader.get(Helfer.sucheALHeaderName(alHeader, Helfer.textVar(comboBoxName)));

			// Baue Infos aus Header in Anzeige ein
			txtrInhalt.setText(Helfer.varText(header.getInhalt()));
			txtrErstelltDatum.setText(Helfer.datumL(header.getDatum()));
			txtrWoerter.setText(Helfer.zahl(txtrInhalt));
			txtrZeichen.setText(String.valueOf(txtrInhalt.getText().length()));
			txtrInhalt.setCaretPosition(0);
			
			// Standardbutton einstellen
			if(header.getId() == einst.getSHeader()){
			// Wenn Header bereits Standard
				btnStandard.setToolTipText("Der aktuell gewählte Header ist bereits als Standard festgelegt");
				btnStandard.setEnabled(false);
			}else{
			// Wenn Header nicht Standard
				btnStandard.setToolTipText("Aktuell gewählten Header als Standard festlegen,\n" +
						"welcher automatisch beim Postexport eingefügt wird");
				btnStandard.setEnabled(true);
			}
		}
		
		// Falls Footer
		else if(!isHeader){
			
			// Setze Footer
			footer = alFooter.get(Helfer.sucheALFooterName(alFooter, Helfer.textVar(comboBoxName)));

			// Baue Infos aus Footer in Anzeige ein
			txtrInhalt.setText(Helfer.varText(footer.getInhalt()));
			txtrErstelltDatum.setText(Helfer.datumL(footer.getDatum()));
			txtrWoerter.setText(Helfer.zahl(txtrInhalt));
			txtrZeichen.setText(String.valueOf(txtrInhalt.getText().length()));
			txtrInhalt.setCaretPosition(0);
			
			// Standardbutton einstellen
			if(footer.getId() == einst.getSFooter()){
			// Wenn Footer bereits Standard
				btnStandard.setToolTipText("Der aktuell gewählte Footer ist bereits als Standard festgelegt");
				btnStandard.setEnabled(false);
			}else{
			// Wenn Themenvorlage nicht Standard
				btnStandard.setToolTipText("Aktuell gewählten Footer als Standard festlegen,\n" +
						"welcher automatisch beim Postexport eingefügt wird");
				btnStandard.setEnabled(true);
			}
		}
	}
	
	// Neueinlesen der Header und Footer
	public void einlesen(){
		// Header und Footer aus DB holen
		alHeader = einst.dao().sucheAlleHeader();
		alFooter = einst.dao().sucheAlleFooter();
		
		// ItemListener entfernen
		comboBoxName.removeItemListener(ilAuswahl);
		
		// ComboBoxName auffuellen
		headerFooterFiltern();
		
		// Einbau des ItemListeners
		comboBoxName.addItemListener(ilAuswahl);
		
		// Standardbutton deaktivieren
		btnStandard.setEnabled(false);
	}
}
