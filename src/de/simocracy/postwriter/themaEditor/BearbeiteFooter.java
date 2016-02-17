package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.Verwaltung;

public class BearbeiteFooter extends FooterBearbeiten {

	private static final long serialVersionUID = 7947592074768771744L;

	public BearbeiteFooter(Einst einst, ArrayList<Footer> alFooter, Verwaltung verw, Footer footer) {
		super(einst, alFooter, verw);
		this.footer = footer;
		
		setTitle("PostWriter - Themeneditor - Footer bearbeiten");
		
		// Setze Footer zum bearbeiten
		txtThemenname.setText(Helfer.varText(footer.getName()));
		textAreaThema.setText(Helfer.varText(footer.getInhalt()));
		txtrErstelltDatum.setText(Helfer.datumL(footer.getDatum()));
		txtrWoerterZahl.setText(Helfer.zahl(textAreaThema));
		txtrZeichenZahl.setText(String.valueOf(textAreaThema.getText().length()));
		textAreaThema.setCaretPosition(0);
	}
	
	// Speichern
	protected void speichern(){
		einst.out("Speichere geänderten Footer.");
		
		// Werfe aktuellen Footer aus Array fuer Suche
		alFooter.remove(Helfer.sucheALFooterName(alFooter, footer.getName()));
		
		// Pruefe, ob Name bereits existiert
		if(Helfer.sucheALFooterName(alFooter, Helfer.textVar(txtThemenname)) != -1){
			einst.out("Name für Footer existiert bereits.");
			JOptionPane.showMessageDialog(null, "Der gewählte Name für den Footer existiert bereits und kann " +
					"daher nicht gespeichert werden.", "Footer existiert bereits", JOptionPane.ERROR_MESSAGE);
		}else{
			// Suche Footerinfos
			holeFooterInfosEdit();
			
			// Speichere Footer in DB
			einst.dao().aendereFooter(footer);
			
			// Fenster beenden
			abbrechen();
		}
	}
}
