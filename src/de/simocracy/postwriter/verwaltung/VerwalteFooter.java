package de.simocracy.postwriter.verwaltung;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.themaEditor.*;

public class VerwalteFooter extends EinstellungenVerwaltung {

	private static final long serialVersionUID = -3945304758642837750L;
	private Footer footer;
	private ArrayList<Footer> alFooter;

	public VerwalteFooter(Einst einst, Hauptmenue hm) {
		super(einst, hm);
		beendenText = "Footerverwaltung";
		setTitle("Post Writer - " + beendenText);
		
		// ComboBoxBereich anzeige
		comboBoxBereich.addItem("Footer");
		
		// Footer aus DB holen
		alFooter = einst.dao().sucheAlleFooter();
		
		// ComboBoxName auffuellen
		footerAuflisten();
		
		// Auswahl des ersten Footers
		erstAuswahl();
		
		// Einbau des ItemListeners
		comboBoxName.addItemListener(ilAuswahl);
	}
	
	// Neuer Footer erstellen
	protected void erstellen(){
		einst.out("Erstelle neuen Footer.");
		setVisible(false);
		new NeuerFooter(einst, alFooter, this);
	}
	
	// Footer bearbeiten
	protected void bearbeiten(){
		einst.out("Bearbeite Footer.");
		setVisible(false);
		new BearbeiteFooter(einst, alFooter, this, footer);
	}
	
	// Footer loeschen
	protected void loeschen(){
		einst.out("Lösche Footer.");
		
		// Pruefe, ob wirklich geloescht werden soll
		int i = JOptionPane.showConfirmDialog(null, "Soll der Footer wirklich gelöscht werden?",
				"Löschen bestätigen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		// Wenn geloescht werden soll
		if(i == JOptionPane.YES_OPTION){
			// Loesche Footer
			einst.dao().loescheFooter(footer.getId());
			
			// Loesche Footer aus Array
			einst.out("Löschen abgeschlossen. Aktualisiere IDs.");
			alFooter.remove(footer.getId());
			
			// Aktualisiere Datenbank
			for(int j = footer.getId(); j < alFooter.size(); j++){
				// Aktualisiere Footer
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
	
	// Footer als Standard festlegen
	protected void standard(){
		einst.out("Lege neuen Standardfooter fest.");
		
		einst.speicherSFooter(footer.getId());
		btnStandard.setEnabled(false);
	}
	
	// Footer auflisten
	private void footerAuflisten(){
		// Alte Liste entfernen
		comboBoxName.removeAllItems();
		
		// Box fuellen
		for(int i = 1; i < alFooter.size(); i++){
			comboBoxName.addItem(Helfer.varText(alFooter.get(i).getName()));
		}
		
		// Erstauswahl
		erstAuswahl();
	}
	
	// Erstauswahl eines Footer
	private void erstAuswahl(){
		// Wenn es Footer gibt
		if(comboBoxName.getItemCount() > 0){
			// Standardfooter auswaehlen
			comboBoxName.setSelectedItem(Helfer.varText(alFooter.get(Helfer.sucheALFooterFid(alFooter,
					einst.getSFooter())).getName()));
			auswahl();
		}
		
		// Wenn es keine Footer gibt
		else{
			txtrInhalt.setText("");
			txtrErstelltDatum.setText("-");
			txtrWoerter.setText("-");
			txtrZeichen.setText("-");
		}
	}
	
	// Auswahl eines Footer
	protected void auswahl(){
		// Setze header
		footer = alFooter.get(Helfer.sucheALFooterName(alFooter, Helfer.textVar(comboBoxName)));
		
		// Baue Zeug aus footer in Anzeige ein
		txtrInhalt.setText(Helfer.varText(footer.getInhalt()));
		txtrErstelltDatum.setText(Helfer.datumL(footer.getDatum()));
		txtrWoerter.setText(Helfer.zahl(txtrInhalt));
		txtrZeichen.setText(String.valueOf(txtrInhalt.getText().length()));
		
		// Standardbutton einstellen
		if(footer.getId() == einst.getSFooter()){
		// Wenn Footer bereits Standardfooter
			btnStandard.setToolTipText("Der aktuell gewählte Footer ist bereits als Standard festgelegt");
			btnStandard.setEnabled(false);
		}else{
		// Wenn Footer nicht Standardfooter
			btnStandard.setToolTipText("Aktuell gewählten Footer als Standard festlegen");
			btnStandard.setEnabled(true);
		}
	}
	
	// Neueinlesen der Footer
	public void einlesen(){
		// Footer aus DB holen
		alFooter = einst.dao().sucheAlleFooter();
		
		// ItemListener entfernen
		comboBoxName.removeItemListener(ilAuswahl);
		
		// Footer auffuellen
		footerAuflisten();
		
		// ItemListener neu einbauen
		comboBoxName.addItemListener(ilAuswahl);
	}
}
