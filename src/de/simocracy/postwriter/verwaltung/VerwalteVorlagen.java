package de.simocracy.postwriter.verwaltung;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.themaEditor.*;

public class VerwalteVorlagen extends EinstellungenVerwaltung {

	private static final long serialVersionUID = 7727217796829677428L;
	private Standardtext standardtext;
	private ArrayList<Standardtext> alStandardtext;
	private Themenvorlage tvorlage;
	private ArrayList<Themenvorlage> alTVorlage;
	private boolean isStand;

	public VerwalteVorlagen(Einst einst, Hauptmenue hm) {
		super(einst, hm);
		beendenText = "Vorlagenverwaltung";
		setTitle("Post Writer - " + beendenText);
		
		// Neu-Buttons einstellen
		btnNeu.setText("Neuer Stand.");
		btnNeu.setToolTipText("Neuer Standardtext erstellen");
		btnNeu.setMnemonic('t');
		btnNeuausVorl.setText("Neue Thvor.");
		btnNeuausVorl.setToolTipText("Neue Themenvorlage erstellen");
		btnNeuausVorl.setMnemonic('v');
		
		// ComboBoxBereich einstellen
		comboBoxBereich.addItem("Standardtexte und Themenvorlagen");
		comboBoxBereich.addItem("Standardtexte");
		comboBoxBereich.addItem("Themenvorlagen");
		comboBoxBereich.setEnabled(true);
		comboBoxBereich.setSelectedIndex(0);
		lblBereich.setText("Typ");
		
		// ItemListener fuer ComboBoxBereich
		comboBoxBereich.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				comboBoxName.removeItemListener(ilAuswahl);
				vorlagenFiltern();
				comboBoxName.addItemListener(ilAuswahl);
			}
		});
		
		// Vorlagen einlesen
		einlesen();
	}
	
	// Neuer Standardtext erstellen
	protected void erstellen(){
		new NeuerStandardtext(einst, alStandardtext, this);
	}
	
	// Neue Themenvorlage erstellen
	protected void erstellen2(){
		new NeueThemenvorlage(einst, alTVorlage, this);
	}
	
	// Standardtext bearbeiten
	protected void bearbeiten(){
		// Pruefe, was bearbeitet werden soll

		if(isStand){
			// Standardtext bearbeiten
			einst.out("Bearbeite Standardtext.");
			setVisible(false);
			new BearbeiteStandardtext(einst, alStandardtext, this, standardtext);
		}

		else if(!isStand){
			// Themenvorlage bearbeiten
			einst.out("Bearbeite Themenvorlage.");
			setVisible(false);
			new BearbeiteThemenvorlage(einst, alTVorlage, this, tvorlage);
		}
	}
	
	// Vorlage loeschen
	protected void loeschen(){
		
		// Pruefe, was geloescht werden soll
		if(isStand){
			// Loeschen von Standardtext
			einst.out("Lösche Standardtext.");
			
			// Pruefe, ob wirklich geloescht werden soll
			int i = JOptionPane.showConfirmDialog(null, "Soll der Standardtext wirklich gelöscht werden?",
					"Löschen bestätigen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			// Wenn geloescht werden soll
			if(i == JOptionPane.YES_OPTION){
				// Loesche Standardtext
				einst.dao().loescheStandardtext(standardtext.getId());
				
				// Loesche Standardtext aus Array
				einst.out("Löschen abgeschlossen. Aktualisiere IDs.");
				alStandardtext.remove(standardtext.getId());
				
				// Aktualisiere Datenbank
				for(int j = standardtext.getId(); j < alStandardtext.size(); j++){
					// Aktualisiere Standardtexte in Datenbank
					einst.dao().aendereStandardtextSid(alStandardtext.get(j).getId(), j);
				}
				
				// Neueinlesen der Infos
				einlesen();
			}
			
			// Wenn nicht geloescht werden soll
			else{
				einst.out("Löschen abgebrochen.");
			}
		}
		
		else if(!isStand){
			// Loeschen von Themenvorlage
			einst.out("Lösche Themenvorlage.");
			
			// Pruefe, ob wirklich geloescht werden soll
			int i = JOptionPane.showConfirmDialog(null, "Soll die Themenvorlage wirklich gelöscht werden?",
					"Löschen bestätigen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			// Wenn geloescht werden soll
			if(i == JOptionPane.YES_OPTION){
				// Loesche Themenvorlage
				einst.dao().loescheThemenvorlage(tvorlage.getId());
				
				// Loesche Standardtext aus Array
				einst.out("Löschen abgeschlossen. Aktualisiere IDs.");
				alTVorlage.remove(tvorlage.getId());
				
				// Aktualisiere Datenbank
				for(int j = tvorlage.getId(); j < alTVorlage.size(); j++){
					// Aktualisiere Themenvorlagen in Datenbank
					einst.dao().aendereThemenvorlageTVid(alTVorlage.get(j).getId(), j);
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
	
	// Vorlagen auflisten
	private void vorlagenFiltern(){
		// Alte Liste entfernen
		comboBoxName.removeAllItems();
		
		// Standardtexte bei Bedarf einfuegen
		if(comboBoxBereich.getSelectedItem().toString().contains("Standardtexte")){
			for(int i = 1; i < alStandardtext.size(); i++){
				comboBoxName.addItem(Helfer.varText(alStandardtext.get(i).getName()));
			}
		}
		
		// Themenvorlagen bei Bedarf einfuegen
		if(comboBoxBereich.getSelectedItem().toString().contains("Themenvorlagen")){
			for(int i = 1; i < alTVorlage.size(); i++){
				comboBoxName.addItem(Helfer.varText(alTVorlage.get(i).getName()));
			}
		}
		
		// Erstauswahl
		erstAuswahl();
	}
	
	// Erstauswahl einer Vorlage
	private void erstAuswahl(){
		// Wenn es Vorlagen gibt
		if(comboBoxName.getItemCount() > 0){
			comboBoxName.setSelectedIndex(0);
			auswahl();
		}
		
		// Wenn es keine Vorlagen gibt
		else{
			txtrInhalt.setText("");
			txtrErstelltDatum.setText("-");
			txtrWoerter.setText("-");
			txtrZeichen.setText("-");
		}
	}
	
	// Vorlage als Standard festlegen
	protected void standard(){
		einst.out("Lege neue Standard-Themenvorlage fest.");
		
		einst.speicherSThemenVorlage(tvorlage.getId());
		btnStandard.setEnabled(false);
	}
	
	// Auswahl einer Vorlage
	protected void auswahl(){
		// Pruefe welcher Typ die Auswahl ist
		if(Helfer.sucheALStandardtextName(alStandardtext, Helfer.textVar(comboBoxName)) != -1 &&
				alStandardtext.get(Helfer.sucheALStandardtextName(alStandardtext, 
				Helfer.textVar(comboBoxName))).getTyp() == 3){
			isStand = true;
		}
		else if(Helfer.sucheALTVorlageName(alTVorlage, Helfer.textVar(comboBoxName)) != -1 &&
				alTVorlage.get(Helfer.sucheALTVorlageName(alTVorlage, 
						Helfer.textVar(comboBoxName))).getTyp() == 4){
			isStand = false;
		}
		
		// Falls Vorlage ein Standardtext
		if(isStand){
			
			// Setze Standardtext
			standardtext = alStandardtext.get(Helfer.sucheALStandardtextName(alStandardtext,
					Helfer.textVar(comboBoxName)));

			// Baue Infos aus Standardtext in Anzeige ein
			txtrInhalt.setText(Helfer.varText(standardtext.getInhalt()));
			txtrErstelltDatum.setText(Helfer.datumL(standardtext.getDatum()));
			txtrWoerter.setText(Helfer.zahl(txtrInhalt));
			txtrZeichen.setText(String.valueOf(txtrInhalt.getText().length()));
			txtrInhalt.setCaretPosition(0);
			
			// Standardbutton deaktivieren
			btnStandard.setEnabled(false);
		}
		
		// Falls Vorlage eine Themenvorlage
		else if(!isStand){
			
			// Setze Themenvorlage
			tvorlage = alTVorlage.get(Helfer.sucheALTVorlageName(alTVorlage, Helfer.textVar(comboBoxName)));

			// Baue Infos aus Themenvorlage in Anzeige ein
			txtrInhalt.setText(Helfer.varText(tvorlage.getInhalt()));
			txtrErstelltDatum.setText(Helfer.datumL(tvorlage.getDatum()));
			txtrWoerter.setText(Helfer.zahl(txtrInhalt));
			txtrZeichen.setText(String.valueOf(txtrInhalt.getText().length()));
			
			// Standardbutton einstellen
			if(tvorlage.getId() == einst.getSThemenVorlage()){
			// Wenn Themenvorlage bereits Standard
				btnStandard.setToolTipText("Die aktuell gewählte Themenvorlage ist bereits als Standard festgelegt");
				btnStandard.setEnabled(false);
			}else{
			// Wenn Themenvorlage nicht Standard
				btnStandard.setToolTipText("Aktuell gewählte Themenvorlage als Standard festlegen, welche\n" +
						"automatisch verwendet wird, wenn ein neues Thema über \"Neu\" erstellt wird");
				btnStandard.setEnabled(true);
			}
		}
	}
	
	// Neueinlesen der Standardtexte
	public void einlesen(){
		// Vorlagen aus DB holen
		alStandardtext = einst.dao().sucheAlleStandardtexte();
		alTVorlage = einst.dao().sucheAlleThemenvorlagen();
		
		// ItemListener entfernen
		comboBoxName.removeItemListener(ilAuswahl);
		
		// ComboBoxName auffuellen
		vorlagenFiltern();
		
		// Einbau des ItemListeners
		comboBoxName.addItemListener(ilAuswahl);
		
		// Standardbutton deaktivieren
		btnStandard.setEnabled(false);
	}
}
