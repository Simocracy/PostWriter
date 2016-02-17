package de.simocracy.postwriter.themaEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.Themenvorlage;
import de.simocracy.postwriter.verwaltung.Verwaltung;

public abstract class ThemenvorlageBearbeiten extends EinstellungenThemenEditor {

	private static final long serialVersionUID = 6448425533076672918L;
	protected Themenvorlage tvorlage;
	protected ArrayList<Themenvorlage> alTVorlage;

	public ThemenvorlageBearbeiten(Einst einst, ArrayList<Themenvorlage> alTVorlage, Verwaltung verw) {
		super(einst, verw);
		this.alTVorlage = alTVorlage;
		
		contentPane.remove(panelFormatierungen);
		
		// Button fuer Variablen
		btnVariable.setEnabled(true);
		btnVariable.setText("Thementext");
		btnVariable.setToolTipText("Stelle festlegen, an der später der Cursor " +
				"standardmäßig steht, um ein neues Thema zu schreiben");
		btnVariable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int pos1 = textAreaThema.getCaretPosition();
				String input = Einst.vthementext;
				textAreaThema.insert(input, pos1);
				textAreaThema.requestFocus();
				textAreaThema.setCaretPosition(pos1+input.length());
			}
		});
	}
	
	// Infos zum Speichern von neuer Themenvorlage holen
	protected void holeThemenvorlageInfosNeu(){
		// Suche neue sid
		int sid = alTVorlage.size();
		
		// Infos in Standardtext
		tvorlage = new Themenvorlage();
		tvorlage.setId(sid);
		tvorlage.setName(Helfer.textVar(txtThemenname));
		tvorlage.setInhalt(Helfer.textVar(textAreaThema));
		
		// DatumErstellt
		tvorlage.setDatum(einst.getNeuesPostDatum());
	}

	// Infos zum Speichern von editierter Themenvorlage holen
	protected void holeThemenvorlageInfosEdit(){
		
		// Aendere Name und Inhalt
		tvorlage.setName(Helfer.textVar(txtThemenname));
		tvorlage.setInhalt(Helfer.textVar(textAreaThema));
	}

}
