package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.Einst;
import de.simocracy.postwriter.Helfer;
import de.simocracy.postwriter.fachklassen.Kurznachricht;
import de.simocracy.postwriter.fachklassen.Post;
import de.simocracy.postwriter.verwaltung.Verwaltung;

public class BearbeiteKurznachricht extends KurznachrichtenBearbeiten {

	private static final long serialVersionUID = 5034686986612846662L;

	public BearbeiteKurznachricht(Einst einst,  ArrayList<Post> alPost,
			ArrayList<Kurznachricht> alKurz, Verwaltung verw, Kurznachricht kurznachricht) {
		super(einst, alPost, alKurz, verw);
		this.kurznachricht = kurznachricht;
		
		setTitle("PostWriter - Themeneditor - Kurznachricht bearbeiten");
		
		// Setze Kurznachricht zum bearbeiten
		comboBoxPost.setSelectedItem("ID: " + kurznachricht.getPid());
		comboBoxWichtigkeit.setSelectedItem(kurznachricht.getWkat());
		textAreaThema.setText(Helfer.varText(kurznachricht.getInhalt()));
		txtrErstelltDatum.setText(Helfer.datumL(kurznachricht.getErstellt()));
		txtrWoerterZahl.setText(Helfer.zahl(textAreaThema));
		txtrZeichenZahl.setText(String.valueOf(textAreaThema.getText().length()));
		textAreaThema.setCaretPosition(0);
	}
	
	// Speichern
	protected void speichern(){
		einst.out("Speichere geänderte Kurznachricht.");
		
		// Werfe aktuelle Kurznachricht aus Array fuer Suche
		alKurz.remove(Helfer.sucheALKurzInhalt(alKurz, kurznachricht.getInhalt()));
		
		// Pruefe, ob Kurznachricht bereits existiert
		if(Helfer.sucheALKurzInhalt(alKurz, Helfer.textVar(textAreaThema)) != -1){
			einst.out("Kurznachricht existiert bereits.");
			JOptionPane.showMessageDialog(null, "Die gewählte Kurznachricht existiert bereits und kann " +
					"daher nicht gespeichert werden.", "Kurznachricht existiert bereits", JOptionPane.ERROR_MESSAGE);
		}else{
			// Suche Themeninfos
			holeKurzNInfosEdit();
			
			// Speichere thema in DB
			einst.dao().aendereKurznachricht(kurznachricht);
			
			// Fenster beenden
			abbrechen();
		}
	}

}
