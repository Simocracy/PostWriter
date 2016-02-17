package de.simocracy.postwriter.themaEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.*;

public abstract class StandardtextBearbeiten extends EinstellungenThemenEditor {
	
	private static final long serialVersionUID = 6587490210117915390L;
	protected Standardtext standardtext;
	protected ArrayList<Standardtext> alStandardtext;

	public StandardtextBearbeiten(Einst einst, ArrayList<Standardtext> alStandardtext, Verwaltung verw) {
		super(einst, verw);
		this.alStandardtext = alStandardtext;
		contentPane.remove(panelFormatierungen);
		
		// Button fuer Variablen
		btnVariable.setEnabled(true);
		btnVariable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int pos1 = textAreaThema.getCaretPosition();
				String variableName = JOptionPane.showInputDialog(null, "Bitte Name für die neue Variable eingeben:",
						"Neue Variable", JOptionPane.QUESTION_MESSAGE);
				
				// Weitere Verarbeitung nur, wenn ein Name eingegeben wurde
				if(variableName != null){
					String input = Einst.vvarStart + variableName + Einst.vvarEnde;
					textAreaThema.insert(input, pos1);
					textAreaThema.requestFocus();
					textAreaThema.setCaretPosition(pos1+input.length());
				}
			}
		});
	}
	
	// Infos zum Speichern von neuem Standardtext holen
	protected void holeStandardtextInfosNeu(){
		// Suche neue sid
		int sid = alStandardtext.size();
		
		// Infos in Standardtext
		standardtext = new Standardtext();
		standardtext.setId(sid);
		standardtext.setName(Helfer.textVar(txtThemenname));
		standardtext.setInhalt(Helfer.textVar(textAreaThema));
		
		// DatumErstellt
		standardtext.setDatum(einst.getNeuesPostDatum());
	}

	// Infos zum Speichern von editiertem Standardtext holen
	protected void holeStandardtextInfosEdit(){
		
		// Aendere Name und Inhalt
		standardtext.setName(Helfer.textVar(txtThemenname));
		standardtext.setInhalt(Helfer.textVar(textAreaThema));
	}
	
}
