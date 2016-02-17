package de.simocracy.postwriter.frameEinstellungen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.Einst;
import de.simocracy.postwriter.Hauptmenue;
import de.simocracy.postwriter.Helfer;
import de.simocracy.postwriter.themaEditor.Postformatierung;
import de.simocracy.postwriter.verwaltung.PostEditor;

public class EinstellungenErststart extends FrameEinstellungen {

	private static final long serialVersionUID = -9109148207874694035L;
	private Postformatierung pf;

	public EinstellungenErststart(Einst einst, Hauptmenue hm) {
		super(einst, hm);
		
		einst.out("Öffne Einstellungsfenster.");
		
		// Einstellungen fuer Erststart
		btnAbbreichen.setEnabled(false);
		btnEinstellungenZurcksetzen.setEnabled(false);
		btnArbeitsverzeichnisndern.setEnabled(false);
		lblSimForumPasswort.setText("SimForum-Passwort für CtP:");
		pwdPw.setToolTipText("<html>Passwort für die CtP-Funktion eingeben.<br>" +
				"Muss zur Verwendung von CtP nicht in Datenbank gespeichert werden</html>");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		btnPasswortAusDatenbank.setEnabled(false);
		btnPasswortndern.setEnabled(false);
		btnPasswortndern.setToolTipText("Passwort wird mit klick auf \"Speichern\" " +
				"automatisch gespeichert.");
		
		// Anzeigen fuer Erststart
		txtNick.setText("Nick des Nutzers");
		sliderAutoumbruch.setValue(70);
		sliderGBereich.setValue(3);
		sliderGThema.setValue(2);
		sliderGNormal.setValue(2);
		sliderGKurzUeber.setValue(3);
		sliderGKurzText.setValue(2);
		txtWikiartikel.setText(Einst.wikiLink);
		chckbxNeuerPost.setSelected(true);
		txtNopasteseite.setText("http://nopaste.info/");
		chckbxAutoUpdate.setSelected(true);
		txtNamekurzbereich.setText("Kurznachrichten");
		
		// Speicherbutton bei Erststart
		btnSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Pruefe, ob Nick leer ist
				if(txtNick.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Der Nick des Nutzer muss eingegeben werden.",
							"Bitte Nick eingeben", JOptionPane.ERROR_MESSAGE);
				} else{
					// Speichern
					erstSpeichern();
				}
			}
		});
		
		// Anzeige bei Erststart
		JOptionPane.showMessageDialog(null, "Um den PostWriter verwenden zu können, müssen zuerst " +
				"ein paar Einstellungen festgelegt werden." + Einst.nl + "Bitte legen Sie die " +
				"Einstellungen nun fest und klicken Sie danach auf Speichern.",
				"Willkommen!", JOptionPane.INFORMATION_MESSAGE);
		
		// Einstellungsfenster für Erststart sichtbar machen
		setVisible(true);
	}
	
	// Start der Formatierungseinstellung aendern
	protected void postformatierung(){
		setVisible(false);
		pf = new Postformatierung(einst, this, true);
	}
	
	// Aktion beim Speichern
	private void erstSpeichern(){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				einst.erstStart(txtNick.getText());
				if(speicherPW()){
					einst.speicherEinstellungen(sliderAutoumbruch.getValue(), sliderGBereich.getValue(),
							sliderGThema.getValue(), sliderGNormal.getValue(), txtWikiartikel.getText(),
							chckbxTopo.isSelected(), chckbxNeuerPost.isSelected(), chckbxExportBaum.isSelected(),
							txtNopasteseite.getText(), chckbxAutoUpdate.isSelected(), txtNick.getText(),
							sliderGKurzUeber.getValue(), sliderGKurzText.getValue(), txtNamekurzbereich.getText());
					if(pf != null) {
						einst.speicherFormatierung(pf.getFormatierung().replaceAll(Einst.nl, ""));
						pf.dispose();
					}
					einst.out("Schließe Einstellungsfenster.");
					new PostEditor(einst, null).postErstellen();
					hm.startePW();
					einst.out("Vorbereitungen abgeschlossen. Starte ProstWriter.");
				}
			}
		});
		
		t.start();
		dispose();
	}

	// Passwort speichern
	private boolean speicherPW(){
		// Nur speichern, wenn Eingabefeld nicht leer
		if(pwdPw.getPassword().length > 0){
			// PWs hashen
			String pw1 = "";
			String pw2 = "!";
			try {
				pw1 = Helfer.hashMD5(pwdPw);
				pw2 = Helfer.hashMD5(pwdPwwh);
			} catch (Exception e) {
			}
			
			// Pruefen, ob PWs richtig sind
			if(pw1.equals(pw2)){
				// Wenn Kontrolle stimmt, an Hasher und Speicherung weitergeben
				einst.dao().speicherPW(pw1);
				return true;
			}
			else{
				// Wenn Kontrolle falsch ist
				JOptionPane.showMessageDialog(null, "Die eingegeben Passwörter stimmen nicht überein.",
						"Passwörter stimmen nicht überein", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		else{
			// Bei leerem Eingabefeld
			return true;
		}
	}
}
