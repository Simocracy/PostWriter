package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.*;


public abstract class ThemaBearbeiten extends PostsThemaEditor {	
	
	private static final long serialVersionUID = 2827504453023636669L;
	protected Thema thema;
	protected ArrayList<Bereich> alBereich;
	protected ArrayList<Thema> alThema;

	public ThemaBearbeiten(Einst einst, ArrayList<Post> alPost,
			ArrayList<Bereich> alBereich, ArrayList<Thema> alThema, Verwaltung verw) {
		super(einst, alPost, verw);
		this.alBereich = alBereich;
		this.alThema = alThema;
		
		// ComboBoxBreich fuellen
		for(int j = 1; j < alBereich.size(); j++){
			comboBoxBereich.addItem(Helfer.varText(alBereich.get(j).getName()));
		}
		comboBoxBereich.addItem(alBereich.get(0).getName());
		comboBoxBereich.setEnabled(true);
	}

	// Infos zum Speichern von neuem Thema holen
	protected void holeThemenInfosNeu(){
		// Suche neue tid
		int tid = alThema.size();
		
		// Bestimmte pid
		int pid = bestimmtePid();
		
		// Suche Objekte
		Bereich bereich = alBereich.get(Helfer.sucheALBereichName(alBereich, Helfer.textVar(comboBoxBereich)));
		Post post = alPost.get(Helfer.sucheALPostPid(alPost, pid));
		
		// Infos in Thema
		thema = new Thema(einst);
		thema.setId(tid);
		thema.setName(Helfer.textVar(txtThemenname));
		thema.setInhalt(Helfer.textVar(textAreaThema));
		thema.setBereich(bereich);
		thema.setPost(post);
		thema.setWkat(Integer.parseInt(comboBoxWichtigkeit.getSelectedItem().toString()));
		
		// DatumErstellt
		thema.setErstellt(einst.getNeuesPostDatum());
		
		// Wenn Name schon exisitiert Index an Namen hinzufügen
//		int index = Helfer.sucheALThemaName(alThema, Helfer.textVar(txtThemenname));
//		if(Helfer.sucheALThemaName(alThema, Helfer.textVar(txtThemenname)) > 0){
//			System.out.println("Thema existiert");
//			thema.setName(thema.getName() + Einst.index + index);
//		}
	}

	// Infos zum Speichern von editiertem Thema holen
	protected void holeThemenInfosEdit(){
		// Pruefe Post
		int pid = bestimmtePid();
		if(thema.getPid() != pid){
			thema.setPost(alPost.get(Helfer.sucheALPostPid(alPost, pid)));
		}
		
		// Pruefe Bereich
		String bName = Helfer.textVar(comboBoxBereich);
		if(!thema.getBereich().getName().equals(bName)){
			thema.setBereich(alBereich.get(Helfer.sucheALBereichName(alBereich, bName)));
		}
		
		// Pruefe Wichtigkeit
		int wk = Integer.parseInt(comboBoxWichtigkeit.getSelectedItem().toString());
		if(thema.getWkat() != wk){
			thema.setWkat(wk);
		}
		
		// Aendere Name und Inhalt
		thema.setName(Helfer.textVar(txtThemenname));
		thema.setInhalt(Helfer.textVar(textAreaThema));
		
		// Wenn Name schon exisitiert Index an Namen hinzufügen
//		int index = Helfer.sucheALThemaName(alThema, Helfer.textVar(txtThemenname));
//		if(Helfer.sucheALThemaName(alThema, Helfer.textVar(txtThemenname)) > 1){
//			thema.setName(thema.getName() + Einst.index + index);
//		}
	}
	
}
