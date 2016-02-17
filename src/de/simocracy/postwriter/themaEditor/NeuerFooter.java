package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.*;

public class NeuerFooter extends FooterBearbeiten {

	private static final long serialVersionUID = 8182648708567220317L;

	public NeuerFooter(Einst einst, ArrayList<Footer> alFooter, Verwaltung verw) {
		super(einst, alFooter, verw);
		
		setTitle("PostWriter - Themeneditor - Neuer Footer");
		txtrWoerterZahl.setText("0");
		txtrZeichenZahl.setText("0");
	}
	
	// Speichern
	protected void speichern(){
		einst.out("Speichere neuen Footer.");
		
		// Pruefe, ob Name bereits existiert
		if(Helfer.sucheALFooterName(alFooter, Helfer.textVar(txtThemenname)) != -1){
			einst.out("Name für Footer existiert bereits.");
			JOptionPane.showMessageDialog(null, "Der gewählte Name für den Footer existiert bereits und kann " +
					"daher nicht gespeichert werden.", "Footer existiert bereits", JOptionPane.ERROR_MESSAGE);
		}else{
			// Suche Footernifos
			holeFooterInfosNeu();
			
			// Speichere Footer in DB
			einst.dao().erfasseFooter(footer);
			
			// Fenster beenden
			abbrechen();
		}
	}
}
