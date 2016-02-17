package de.simocracy.postwriter.verwaltung;

import java.util.ArrayList;

import javax.swing.JTextArea;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;

public class PostVerwaltung extends Verwaltung {

	private static final long serialVersionUID = 6319063175571510314L;
	protected ArrayList<Post> alPost;
	protected Post post;
	protected JTextArea txtrPostid;
	protected JTextArea txtrPost;
	protected String postLeertext;

	public PostVerwaltung(Einst einst, Hauptmenue hm) {
		super(einst, hm);
		
		postLeertext = "Zeige alle nichtgepostete an";
		btnNeuausVorl.setMnemonic('v');
		btnNeu.setMnemonic('n');
		
		postEinlesen();
		
		// ComboBoxWichtigkeit fuellen
		comboBoxWichtigkeit.addItem("Alle anzeigen");
		for(int i = 0; i <= 10; i++){
			comboBoxWichtigkeit.addItem(String.valueOf(i));
		}
	}
	
	// Posts fuellen
	protected void postEinlesen(){
		// Alte Liste leeren
		comboBoxPost.removeAllItems();
		
		// Neue Liste einlesen
		alPost = einst.dao().sucheAllePostsMitArray();
		comboBoxPost.addItem(postLeertext);
		comboBoxPost.addItem("Zwischenspeicher");
		for(int i = alPost.size()-1; i > 0; i--){
			// Nur noch nicht gepostete Posts einbauen
			if(alPost.get(i).getDatum().equals("")){
				comboBoxPost.addItem("ID: " + alPost.get(i).getId());
			}
			else{
				// Gepostete Posts rauswerfen
				alPost.remove(i);
			}
		}
		
		// Neuesten Post als Vorauswahl treffen
		if(comboBoxPost.getItemCount() > 2){
			comboBoxPost.setSelectedIndex(2);
		}
	}
}
