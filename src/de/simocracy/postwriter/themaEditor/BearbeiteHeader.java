package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.*;

public class BearbeiteHeader extends HeaderBearbeiten {

	private static final long serialVersionUID = 977148273827240265L;

	public BearbeiteHeader(Einst einst, ArrayList<Header> alHeader, Verwaltung verw, Header header) {
		super(einst, alHeader, verw);
		this.header = header;
		
		setTitle("PostWriter - Themeneditor - Header bearbeiten");
		
		// Setze Header zum bearbeiten
		txtThemenname.setText(Helfer.varText(header.getName()));
		textAreaThema.setText(Helfer.varText(header.getInhalt()));
		txtrErstelltDatum.setText(Helfer.datumL(header.getDatum()));
		txtrWoerterZahl.setText(Helfer.zahl(textAreaThema));
		txtrZeichenZahl.setText(String.valueOf(textAreaThema.getText().length()));
		textAreaThema.setCaretPosition(0);
	}
	
	// Speichern
	protected void speichern(){
		einst.out("Speichere geänderten Header.");
		
		// Werfe aktuellen Header aus Array fuer Suche
		alHeader.remove(Helfer.sucheALHeaderName(alHeader, header.getName()));
		
		// Pruefe, ob Name bereits existiert
		if(Helfer.sucheALHeaderName(alHeader, Helfer.textVar(txtThemenname)) != -1){
			einst.out("Name für Header existiert bereits.");
			JOptionPane.showMessageDialog(null, "Der gewählte Name für den Header existiert bereits und kann " +
					"daher nicht gespeichert werden.", "Header existiert bereits", JOptionPane.ERROR_MESSAGE);
		}else{
			// Suche Headerinfos
			holeHeaderInfosEdit();
			
			// Speichere Header in DB
			einst.dao().aendereHeader(header);
			
			// Fenster beenden
			abbrechen();
		}
	}
}
