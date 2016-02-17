package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.*;

public class NeuerHeader extends HeaderBearbeiten {
	
	private static final long serialVersionUID = -3420635940633863479L;

	public NeuerHeader(Einst einst, ArrayList<Header> alHeader, Verwaltung verw) {
		super(einst, alHeader, verw);
		
		setTitle("PostWriter - Themeneditor - Neuer Header");
		txtrWoerterZahl.setText("0");
		txtrZeichenZahl.setText("0");
	}
	
	// Speichern
	protected void speichern(){
		einst.out("Speichere neuen Header.");
		
		// Pruefe, ob Name bereits existiert
		if(Helfer.sucheALHeaderName(alHeader, Helfer.textVar(txtThemenname)) != -1){
			einst.out("Name für Header existiert bereits.");
			JOptionPane.showMessageDialog(null, "Der gewählte Name für den Header existiert bereits und kann " +
					"daher nicht gespeichert werden.", "Header existiert bereits", JOptionPane.ERROR_MESSAGE);
		}else{
			// Suche Headerinfos
			holeHeaderInfosNeu();
			
			// Speichere Header in DB
			einst.dao().erfasseHeader(header);
			
			// Fenster beenden
			abbrechen();
		}
	}
}
