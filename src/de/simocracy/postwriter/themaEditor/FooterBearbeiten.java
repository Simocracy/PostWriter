package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.*;

public abstract class FooterBearbeiten extends EinstellungenThemenEditor {

	private static final long serialVersionUID = -4238608915361521146L;
	protected Footer footer;
	protected ArrayList<Footer> alFooter;

	public FooterBearbeiten(Einst einst, ArrayList<Footer> alFooter, Verwaltung verw) {
		super(einst, verw);
		this.alFooter = alFooter;
		
		contentPane.remove(panelFormatierungen);
	}
	
	// Infos zum Speichern von neuem Footer holen
	protected void holeFooterInfosNeu(){
		// Suche neue fid
		int fid = alFooter.size();
		
		// Infos in Footer
		footer = new Footer();
		footer.setId(fid);
		footer.setName(Helfer.textVar(txtThemenname));
		footer.setInhalt(Helfer.textVar(textAreaThema));
		
		// DatumErstellt
		footer.setDatum(einst.getNeuesPostDatum());
	}

	// Infos zum Speichern von editiertem Footer holen
	protected void holeFooterInfosEdit(){
		
		// Aendere Name und Inhalt
		footer.setName(Helfer.textVar(txtThemenname));
		footer.setInhalt(Helfer.textVar(textAreaThema));
	}
	
}
