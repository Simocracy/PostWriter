package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.Standardtext;
import de.simocracy.postwriter.verwaltung.*;

public class NeuerStandardtext extends StandardtextBearbeiten {

	private static final long serialVersionUID = 8059784501284289171L;

	public NeuerStandardtext(Einst einst, ArrayList<Standardtext> alStandardtext, Verwaltung verw) {
		super(einst, alStandardtext, verw);
		
		setTitle("PostWriter - Themeneditor - Neuer Standardtext");
		txtrWoerterZahl.setText("0");
		txtrZeichenZahl.setText("0");
	}
	
	// Speichern
	protected void speichern(){
		einst.out("Speichere neuen Standardtext.");
		
		// Pruefe, ob Name bereits existiert
		if(Helfer.sucheALStandardtextName(alStandardtext, Helfer.textVar(txtThemenname)) != -1){
			einst.out("Name für Standardtext existiert bereits.");
			JOptionPane.showMessageDialog(null, "Der gewählte Name für den Standardtext existiert bereits und kann " +
					"daher nicht gespeichert werden.", "Standardtext existiert bereits", JOptionPane.ERROR_MESSAGE);
		}else{
			// Suche Standardtextinfos
			holeStandardtextInfosNeu();
			
			// Speichere Standardtext in DB
			einst.dao().erfasseStandardtext(standardtext);
			
			// Fenster beenden
			abbrechen();
		}
	}
}
