package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.*;

public abstract class KurznachrichtenBearbeiten extends PostsThemaEditor {	
	
	private static final long serialVersionUID = -4910232099813030099L;
	protected Kurznachricht kurznachricht;
	protected ArrayList<Kurznachricht> alKurz;

	public KurznachrichtenBearbeiten(Einst einst, ArrayList<Post> alPost,
			ArrayList<Kurznachricht> alKurz, Verwaltung verw) {
		super(einst, alPost, verw);
		this.alKurz = alKurz;
		
		// ComboBoxBreich deaktivieren
		comboBoxBereich.addItem("Kurznachrichten");
		comboBoxBereich.setSelectedItem("Kurznachrichten");
		
		// Textfeld Name deaktivieren
		txtThemenname.setEnabled(false);
	}
	
	// Infos zum Speichern von neuer Kurznachricht holen
	protected void holeKurzNInfosNeu(){
		// Suche neue kid
		int kid = alKurz.size();
		
		// Bestimmte pid
		int pid = bestimmtePid();
		
		// Suche Post
		Post post = alPost.get(Helfer.sucheALPostPid(alPost, pid));
		
		// Infos in Kurznachricht
		kurznachricht = new Kurznachricht(einst);
		kurznachricht.setId(kid);
		kurznachricht.setInhalt(Helfer.textVar(textAreaThema));
		kurznachricht.setPost(post);
		kurznachricht.setWkat(Integer.parseInt(comboBoxWichtigkeit.getSelectedItem().toString()));
		
		// DatumErstellt
		kurznachricht.setErstellt(einst.getNeuesPostDatum());
	}

	// Infos zum Speichern von editierter Kurznachricht holen
	protected void holeKurzNInfosEdit(){
		// Pruefe Post
		int pid = bestimmtePid();
		if(kurznachricht.getPid() != pid){
			kurznachricht.setPost(alPost.get(Helfer.sucheALPostPid(alPost, pid)));
		}
		
		// Pruefe Wichtigkeit
		int wk = Integer.parseInt(comboBoxWichtigkeit.getSelectedItem().toString());
		if(kurznachricht.getWkat() != wk){
			kurznachricht.setWkat(wk);
		}
		
		// Aendere Inhalt
		kurznachricht.setInhalt(Helfer.textVar(textAreaThema));
	}
}
