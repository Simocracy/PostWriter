package de.simocracy.postwriter.themaEditor;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.verwaltung.*;

public abstract class EinstellungenThemenEditor extends ThemaEditor {

	private static final long serialVersionUID = 1168622018444791579L;

	public EinstellungenThemenEditor(Einst einst, Verwaltung verw) {
		super(einst, verw);
		
		// Komponenten deaktivieren
		comboBoxPost.setEnabled(false);
		btnStandardtext.setEnabled(false);
		comboBoxWichtigkeit.setEnabled(false);
		btnOfftopic.setEnabled(false);
	}

}
