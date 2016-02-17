package de.simocracy.postwriter.datumsrechner;

import javax.swing.JFrame;

import de.simocracy.postwriter.*;

public class FrameDRHm extends FrameDatumsRechner {

	private static final long serialVersionUID = -6434846331392326415L;

	public FrameDRHm(Einst einst, JFrame hm) {
		super(einst, hm);
		
		// Besondere Einstellungen

		lblEinfgeoptionen.setEnabled(false);
		btnEinfgen.setEnabled(false);
		chckbxMitSydatum.setEnabled(false);
		chckbxMitUhrzeit.setEnabled(false);
		panelEinfuegen.setEnabled(false);
	}

}
