package de.simocracy.postwriter.datumsrechner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.themaEditor.ThemaEditor;

public class FrameDRThemen extends FrameDatumsRechner {

	private static final long serialVersionUID = 5650942511090613709L;
	private boolean beenden;
	private boolean einfuegen;
	private ThemaEditor te;

	public FrameDRThemen(Einst einst, ThemaEditor te) {
		super(einst, null);
		beenden = false;
		einfuegen = false;
		this.te = te;
		
		// Einstellungen treffen
		btnEinfgen.setToolTipText("Berechnet und fügt das Datum in Thema oder Kurznachricht ein");
		

		btnEinfgen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				einfuegen();
			}
		});
	}
	
	// Aktion beim Einfuegen des berechneten Datums
	private void einfuegen(){
		berechnen();
		einfuegen = true;
		setVisible(false);
		te.gebeDatum(this);
	}
	
	public String getDatum(){
		if(beenden){
			return null;
		}
		else if(!einfuegen){
			return null;
		}
		else{
			// Ergebnislabel aufsplitten
			String[] ergebnisteile = lblErgebnis.getText().split(" ");
			
			// Format: Datum - Zeit - Zielangabe - "(" - Quelldatum - Quellzeit - Quellangabe - ")"
			String[] erg = {ergebnisteile[0], "", "", "", "", "", "", ""};
			
			// Mit Quelldatum
			if(chckbxMitSydatum.isSelected()) {
				erg[3] = " (";
				
				erg[4] = comboBoxTag.getSelectedItem().toString() + "." +
						comboBoxMonat.getSelectedItem().toString() + "." +
						comboBoxJahr.getSelectedItem().toString();
				
				if(chckbxMitUhrzeit.isSelected()) {
					erg[5] = " " + comboBoxStunde.getSelectedItem().toString() + ":" +
							comboBoxMinute.getSelectedItem().toString() + " Uhr";
				}
				
				if(rdbtnRlSy.isSelected()) erg[6] = " RL";
				if(rdbtnSyRl.isSelected()) erg[6] = " SY";
				
				erg[7] = ")";
			}
			
			// Uhrzeit
			if(chckbxMitUhrzeit.isSelected()) {
				erg[1] = " " + ergebnisteile[1] + " Uhr";
			}
			
			// Zielrichtung
			if(chckbxMitZielangabe.isSelected()) {
				if(rdbtnRlSy.isSelected()) erg[2] = " SY";
				if(rdbtnSyRl.isSelected()) erg[2] = " RL";
			}
			
			// Rückgabe des Datums
			String raushaun = "";
			for(int i = 0; i < erg.length; i++) {
				raushaun += erg[i];
			}
			
			return raushaun;
		}
	}

	// Aktion beim Beenden
	protected void beenden(){
		beenden = true;
		dispose();
	}
}
