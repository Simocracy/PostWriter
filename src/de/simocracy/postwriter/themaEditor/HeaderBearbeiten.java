package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.*;

public abstract class HeaderBearbeiten extends EinstellungenThemenEditor {
	
	private static final long serialVersionUID = 8838431941091447155L;
	protected Header header;
	protected ArrayList<Header> alHeader;

	public HeaderBearbeiten(Einst einst, ArrayList<Header> alHeader, Verwaltung verw) {
		super(einst, verw);
		this.alHeader = alHeader;
		
		contentPane.remove(panelFormatierungen);
	}
	
	// Infos zum Speichern von neuem Header holen
	protected void holeHeaderInfosNeu(){
		// Suche neue hid
		int hid = alHeader.size();
		
		// Infos in Header
		header = new Header();
		header.setId(hid);
		header.setName(Helfer.textVar(txtThemenname));
		header.setInhalt(Helfer.textVar(textAreaThema));
		
		// DatumErstellt
		header.setDatum(einst.getNeuesPostDatum());
	}

	// Infos zum Speichern von editiertem Header holen
	protected void holeHeaderInfosEdit(){
		
		// Aendere Name und Inhalt
		header.setName(Helfer.textVar(txtThemenname));
		header.setInhalt(Helfer.textVar(textAreaThema));
	}
	
}
