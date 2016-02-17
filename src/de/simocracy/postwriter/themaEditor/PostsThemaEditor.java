package de.simocracy.postwriter.themaEditor;

import java.util.ArrayList;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.*;

public abstract class PostsThemaEditor extends ThemaEditor {

	private static final long serialVersionUID = -6685335411024502345L;
	protected ArrayList<Post> alPost;

	public PostsThemaEditor(Einst einst, ArrayList<Post> alPost, Verwaltung verw) {
		super(einst, verw);
		this.alPost = alPost;
		
		// ComboBoxPost fuellen
		for(int i = alPost.size()-1; i > 0; i--){
			comboBoxPost.addItem("ID: " + alPost.get(i).getId());
		}
		comboBoxPost.addItem("Zwischenspeicher");

		// ComboBoxWichtigkeit fuellen
		for(int i = 0; i <= 10; i++){
			comboBoxWichtigkeit.addItem(String.valueOf(i));
		}
		
		// Komponenten deaktivieren
		contentPane.remove(panelFormatierungen);
	}
	
	// Bestimmte pid
	protected int bestimmtePid(){
		if(comboBoxPost.getSelectedItem().equals("Zwischenspeicher")){
			return 0;
		}else{
			return Integer.parseInt(comboBoxPost.getSelectedItem().toString().replaceFirst("ID: ", ""));
		}
	}
}
