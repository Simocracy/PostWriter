package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.Themenvorlage;
import de.simocracy.postwriter.verwaltung.Verwaltung;

public class NeueThemenvorlage extends ThemenvorlageBearbeiten {

	private static final long serialVersionUID = 4833868997639719891L;

	public NeueThemenvorlage(Einst einst,
			ArrayList<Themenvorlage> alTVorlage, Verwaltung verw) {
		super(einst, alTVorlage, verw);
		
		setTitle("PostWriter - Themeneditor - Neue Themenvorlage");
		txtrWoerterZahl.setText("0");
		txtrZeichenZahl.setText("0");
	}
	
	// Speichern
	protected void speichern(){
		einst.out("Speichere neue Themenvorlage.");
		
		// Pruefe, ob Name bereits existiert
		if(Helfer.sucheALTVorlageName(alTVorlage, Helfer.textVar(txtThemenname)) != -1){
			einst.out("Name für Standardtext existiert bereits.");
			JOptionPane.showMessageDialog(null, "Der gewählte Name für die Themenvorlage existiert bereits und kann " +
					"daher nicht gespeichert werden.", "Themenvorlage existiert bereits", JOptionPane.ERROR_MESSAGE);
		}else{
			// Suche Themenvorlageninfos
			holeThemenvorlageInfosNeu();
			
			// Speichere Themenvorlage in DB
			einst.dao().erfasseThemenvorlage(tvorlage);
			
			// Fenster beenden
			abbrechen();
		}
	}
}
