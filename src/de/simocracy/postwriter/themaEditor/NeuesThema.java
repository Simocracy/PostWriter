package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.*;

public class NeuesThema extends ThemaBearbeiten {

	private static final long serialVersionUID = -4828054652707450223L;

	public NeuesThema(Einst einst, ArrayList<Post> alPost, ArrayList<Bereich> alBereich,
			ArrayList<Thema> alThema, Verwaltung verw, String tvorlage) {
		super(einst, alPost, alBereich, alThema, verw);
		
		setTitle("PostWriter - Themeneditor - Neues Thema");
		txtrWoerterZahl.setText("0");
		txtrZeichenZahl.setText("0");
		
		// Vorlagenbearbeitung nur bei Bedarf
		if(tvorlage != null){
			StringBuilder sb = new StringBuilder(tvorlage);
			// Suche Position fuer Cursor
			int pos = 0;
			try {
				while(!sb.subSequence(pos, pos + Einst.vthementext.length()).equals(Einst.vthementext)){
					pos++;
				}
				
				// Cursorvariable loeschen
				sb.delete(pos, pos + Einst.vthementext.length());
			} catch (Exception e) {
//				JOptionPane.showMessageDialog(null, "Keine Position für den Cursor gefunden.",
//						"Keine Curserposition", JOptionPane.INFORMATION_MESSAGE);
				pos = 0;
			}
			
			// Vorlage einbauen
			textAreaThema.setText(sb.toString());
			
			// Cursorposition bestimmen
			textAreaThema.requestFocus();
			textAreaThema.setCaretPosition(pos);
		}
	}
	
	// Speichern
	protected void speichern(){
		einst.out("Speichere neues Thema.");
		
		// Pruefe, ob Name bereits existiert
		if(Helfer.sucheALThemaName(alThema, Helfer.textVar(txtThemenname)) != -1){
			einst.out("Name für Thema existiert bereits.");
			JOptionPane.showMessageDialog(null, "Der gewählte Name für das Thema existiert bereits und kann " +
					"daher nicht gespeichert werden.\nDer Name des Themas kann beim Postexport manuell" +
					"korrigiert werden.", "Themenname existiert bereits", JOptionPane.ERROR_MESSAGE);

		} else {

			// Suche Themeninfos
			holeThemenInfosNeu();
			
			// Speichere thema in DB
			einst.dao().erfasseThema(thema);
			
			// Fenster beenden
			abbrechen();
		}
		
	}
}
