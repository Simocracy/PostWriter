package de.simocracy.postwriter.themaEditor;

import de.simocracy.postwriter.Einst;
import de.simocracy.postwriter.datenhaltung.DBErstellen;
import de.simocracy.postwriter.frameEinstellungen.FrameEinstellungen;

public class Postformatierung extends EinstellungenThemenEditor {

	private static final long serialVersionUID = 8628901959164012253L;
	private Einst einst;
	private FrameEinstellungen feinst;
	private boolean erststart;
	
	public Postformatierung(Einst einst, FrameEinstellungen feinst, boolean erststart) {
		super(einst, null);
		this.erststart = erststart;
		
		this.einst = einst;
		this.feinst = feinst;
		btnVariable.setEnabled(true);
		
		// Komponenten vorbereiten
		contentPane.remove(panelInfos);
		contentPane.add(panelFormatierungen, "cell 3 9 1 12,growy");
		txtThemenname.setEnabled(false);
		btnVariable.setEnabled(false);
		setTitle("PostWriter - Postformatierung");
		einst.out("Starte Themeneditor");
		
		// Bestehender Text einbauen
		if(!erststart) {
			textAreaThema.setText(einst.getFormatierung().replaceAll(Einst.vnl, Einst.vnl + Einst.nl));
		}
		else {
			textAreaThema.setText(DBErstellen.standardFormatierungPost.replaceAll(Einst.vnl, Einst.vnl + Einst.nl));
		}
	}
	
	// Speichern
	protected void speichern(boolean schließen){
		if(!erststart) {
			einst.speicherFormatierung(textAreaThema.getText().replaceAll(Einst.nl, ""));
		}

		abbrechen();
	}
	
	// Rueckgabe der Eingabe fuer Erststart
	public String getFormatierung() {
		return textAreaThema.getText();
	}
	
	// Editor beenden
	protected void abbrechen(){
		einst.out("Beende Themeneditor.");
		setVisible(false);
		feinst.setVisible(true);
	}
}
