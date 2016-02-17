package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.Standardtext;
import de.simocracy.postwriter.verwaltung.Verwaltung;

public class BearbeiteStandardtext extends StandardtextBearbeiten {
	
	private static final long serialVersionUID = -5405576796986401649L;

	public BearbeiteStandardtext(Einst einst, ArrayList<Standardtext> alStandardtext,
			Verwaltung verw, Standardtext standardtext) {
		super(einst, alStandardtext, verw);
		this.standardtext = standardtext;
		
		setTitle("PostWriter - Themeneditor - Standardtext bearbeiten");
		
		// Setze Standardtext zum bearbeiten
		txtThemenname.setText(Helfer.varText(standardtext.getName()));
		textAreaThema.setText(Helfer.varText(standardtext.getInhalt()));
		txtrErstelltDatum.setText(Helfer.datumL(standardtext.getDatum()));
		txtrWoerterZahl.setText(Helfer.zahl(textAreaThema));
		txtrZeichenZahl.setText(String.valueOf(textAreaThema.getText().length()));
		textAreaThema.setCaretPosition(0);
	}
	
	// Speichern
	protected void speichern(){
		einst.out("Speichere geänderten Standardtext.");
		
		// Werfe aktuellen Standardtext aus Array fuer Suche
		alStandardtext.remove(Helfer.sucheALStandardtextName(alStandardtext, standardtext.getName()));
		
		// Pruefe, ob Name bereits existiert
		if(Helfer.sucheALStandardtextName(alStandardtext, Helfer.textVar(txtThemenname)) != -1){
			einst.out("Name für Standardtext existiert bereits.");
			JOptionPane.showMessageDialog(null, "Der gewählte Name für den Standardtext existiert bereits und kann " +
					"daher nicht gespeichert werden.", "Standardtext existiert bereits", JOptionPane.ERROR_MESSAGE);
		}else{
			// Suche Standardtextinfos
			holeStandardtextInfosEdit();
			
			// Speichere Standardtext in DB
			einst.dao().aendereStandardtext(standardtext);
			
			// Fenster beenden
			abbrechen();
		}
	}
}
