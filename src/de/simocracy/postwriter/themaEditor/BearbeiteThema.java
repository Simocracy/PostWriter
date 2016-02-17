package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.*;

public class BearbeiteThema extends ThemaBearbeiten {

	private static final long serialVersionUID = 7359735281612797715L;

	public BearbeiteThema(Einst einst, ArrayList<Post> alPost,
			ArrayList<Bereich> alBereich, ArrayList<Thema> alThema, Verwaltung verw, Thema thema) {
		super(einst, alPost, alBereich, alThema, verw);
		this.thema = thema;
		
		setTitle("PostWriter - Themeneditor - Thema bearbeiten");
		
		// Setze Thema zum bearbeiten
		comboBoxPost.setSelectedItem("ID: " + thema.getPid());
		comboBoxBereich.setSelectedItem(thema.getBereich().getName());
		comboBoxWichtigkeit.setSelectedItem(thema.getWkat());
		txtThemenname.setText(Helfer.varText(thema.getName()));
		textAreaThema.setText(Helfer.varText(thema.getInhalt()));
		txtrErstelltDatum.setText(Helfer.datumL(thema.getErstellt()));
		txtrWoerterZahl.setText(Helfer.zahl(textAreaThema));
		txtrZeichenZahl.setText(String.valueOf(textAreaThema.getText().length()));
		textAreaThema.setCaretPosition(0);
	}
	
	// Speichern
	protected void speichern(){
		einst.out("Speichere geändertes Thema.");
		
		// Werfe aktuelles Thema aus Array fuer Suche
		alThema.remove(Helfer.sucheALThemaName(alThema, thema.getName()));
		
		
//		// Pruefe, ob Name bereits existiert
		if(Helfer.sucheALThemaName(alThema, Helfer.textVar(txtThemenname)) != -1){
			einst.out("Name für Thema existiert bereits.");
			JOptionPane.showMessageDialog(null, "Der gewählte Name für das Thema existiert bereits und kann " +
					"daher nicht gespeichert werden.\nDer Name des Themas kann beim Postexport manuell" +
					"korrigiert werden.", "Themenname existiert bereits", JOptionPane.ERROR_MESSAGE);
			
		} else {
			// Suche Themeninfos
			holeThemenInfosEdit();
			
			// Speichere thema in DB
			einst.dao().aendereThema(thema);
			
			// Fenster beenden
			abbrechen();
		}
	}
}
