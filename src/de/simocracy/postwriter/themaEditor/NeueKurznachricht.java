package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.*;

public class NeueKurznachricht extends KurznachrichtenBearbeiten {

	private static final long serialVersionUID = -966428264559734720L;

	public NeueKurznachricht(Einst einst, ArrayList<Post> alPost, ArrayList<Kurznachricht> alKurz,
			Verwaltung verw) {
		super(einst, alPost, alKurz, verw);
		
		setTitle("PostWriter - Themeneditor - Neue Kurznachricht");
		txtrWoerterZahl.setText("0");
		txtrZeichenZahl.setText("0");
	}
	
	// Speichern
	protected void speichern(){
		einst.out("Speichere neue Kurznachricht.");
		
		// Pruefe, ob Name bereits existiert
		if(Helfer.sucheALKurzInhalt(alKurz, Helfer.textVar(textAreaThema)) != -1){
			einst.out("Kurznachricht existiert bereits.");
			JOptionPane.showMessageDialog(null, "Die gewählte Kurznachricht existiert bereits und kann " +
					"daher nicht gespeichert werden.", "Kurznachricht existiert bereits", JOptionPane.ERROR_MESSAGE);
		}else{
			// Suche Kurznachrichteninfos
			holeKurzNInfosNeu();
			
			// Speichere Kurznachricht in DB
			einst.dao().erfasseKurznachricht(kurznachricht);
			
			// Fenster beenden
			abbrechen();
		}
	}
	
}
