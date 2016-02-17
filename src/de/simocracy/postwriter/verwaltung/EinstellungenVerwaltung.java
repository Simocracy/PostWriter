package de.simocracy.postwriter.verwaltung;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;

import de.simocracy.postwriter.*;

public abstract class EinstellungenVerwaltung extends Verwaltung {

	private static final long serialVersionUID = 1487099495672293467L;
	protected JTextArea txtrInhalt;

	public EinstellungenVerwaltung(Einst einst, Hauptmenue hm) {
		super(einst, hm);
		
		// Deaktiviere ComboBoxen
		comboBoxPost.setEnabled(false);
		comboBoxBereich.setEnabled(false);
		comboBoxWichtigkeit.setEnabled(false);
		
		// Alternativ-Neu-Button aktivieren
		btnNeuausVorl.setEnabled(true);
		
		// TextArea fuer Inhalt einbauen
		txtrInhalt = new JTextArea();
		txtrInhalt.setLineWrap(true);
		txtrInhalt.setWrapStyleWord(true);
		txtrInhalt.setEditable(false);
		txtrInhalt.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		scrollPane.setViewportView(txtrInhalt);
		
		// Button fuer Standard
		btnStandard = new JButton("Als Standard");
		btnStandard.setMnemonic('s');
		contentPane.add(this.btnStandard, "cell 0 6");
		
		// ActionListener fuer Standardbutton
		btnStandard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				standard();
			}
		});
	}
	
	// Aktion bei Standard-Setzen
	protected void standard(){
		// Zum Ueberschreiben in Unterklassen
	}
}
