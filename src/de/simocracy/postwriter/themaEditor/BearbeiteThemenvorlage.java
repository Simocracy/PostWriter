package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.Einst;
import de.simocracy.postwriter.Helfer;
import de.simocracy.postwriter.fachklassen.Themenvorlage;
import de.simocracy.postwriter.verwaltung.Verwaltung;

public class BearbeiteThemenvorlage extends ThemenvorlageBearbeiten {

	private static final long serialVersionUID = -798891217827976208L;

	public BearbeiteThemenvorlage(Einst einst, ArrayList<Themenvorlage> alTVorlage,
			Verwaltung verw, Themenvorlage tvorlage) {
		super(einst, alTVorlage, verw);
		this.tvorlage = tvorlage;
		
		setTitle("PostWriter - Themeneditor - Themenvorlage bearbeiten");
		
		// Setze Themenvorlage zum bearbeiten
		txtThemenname.setText(Helfer.varText(tvorlage.getName()));
		textAreaThema.setText(Helfer.varText(tvorlage.getInhalt()));
		txtrErstelltDatum.setText(Helfer.datumL(tvorlage.getDatum()));
		txtrWoerterZahl.setText(Helfer.zahl(textAreaThema));
		txtrZeichenZahl.setText(String.valueOf(textAreaThema.getText().length()));
		textAreaThema.setCaretPosition(0);
	}
	
	// Speichern
	protected void speichern(){
		einst.out("Speichere geänderte Themenvorlage.");
		
		// Werfe aktuelle Themenvorlage aus Array fuer Suche
		alTVorlage.remove(Helfer.sucheALTVorlageName(alTVorlage, tvorlage.getName()));
		
		// Pruefe, ob Name bereits existiert
		if(Helfer.sucheALTVorlageName(alTVorlage, Helfer.textVar(txtThemenname)) != -1){
			einst.out("Name für Standardtext existiert bereits.");
			JOptionPane.showMessageDialog(null, "Der gewählte Name für die Themenvorlage existiert bereits und kann " +
					"daher nicht gespeichert werden.", "Themenvorlage existiert bereits", JOptionPane.ERROR_MESSAGE);
		}else{
			// Suche Themenvorlageninfos
			holeThemenvorlageInfosEdit();
			
			// Speichere Themenvorlage in DB
			einst.dao().aendereThemenvorlage(tvorlage);
			
			// Fenster beenden
			abbrechen();
		}
	}
}
