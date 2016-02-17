package de.simocracy.postwriter.frameEinstellungen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import de.simocracy.postwriter.Einst;
import de.simocracy.postwriter.Hauptmenue;

public class EinstellungenNormal extends FrameEinstellungen {

	private static final long serialVersionUID = 7988290119665342372L;

	public EinstellungenNormal(Einst einst, Hauptmenue hm) {
		super(einst, hm);
		
		// Einstellungen für normalen Betrieb
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		// Speicherbutton bei normalem Start
		this.btnSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				speichern();
			}
		});
		
		// Fenster schliessen bei normalem Start
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				abbruch();
			}
		});
		
		einbau();
		
		setVisible(true);
	}
	
	// Aktuelle Einstellungen einbauen
	private void einbau(){
		txtNick.setText(einst.getNutzer());
		sliderAutoumbruch.setValue(einst.getAutoumbruch());
		sliderGBereich.setValue(einst.getGbereich());
		sliderGThema.setValue(einst.getGthema());
		sliderGNormal.setValue(einst.getGnormal());
		sliderGKurzUeber.setValue(einst.getGKurzUeber());
		sliderGKurzText.setValue(einst.getGKurzText());
		txtWikiartikel.setText(einst.getWikiArtikel());
		chckbxTopo.setSelected(einst.isTopo());
		chckbxNeuerPost.setSelected(einst.isPostanlegen());
		chckbxExportBaum.setSelected(einst.isExportBaum());
		txtNopasteseite.setText(einst.getNopasteSeite());
		chckbxAutoUpdate.setSelected(einst.isAutoUpdate());
		txtNamekurzbereich.setText(einst.getKurzBereichName());
	}
	
	// Aktion beim speichern
	private void speichern(){
		einst.speicherEinstellungen(sliderAutoumbruch.getValue(), sliderGBereich.getValue(),
				sliderGThema.getValue(), sliderGNormal.getValue(), txtWikiartikel.getText(),
				chckbxTopo.isSelected(), chckbxNeuerPost.isSelected(), chckbxExportBaum.isSelected(),
				txtNopasteseite.getText(), chckbxAutoUpdate.isSelected(), txtNick.getText(),
				sliderGKurzUeber.getValue(), sliderGKurzText.getValue(), txtNamekurzbereich.getText());
		abbruch();
	}
}
