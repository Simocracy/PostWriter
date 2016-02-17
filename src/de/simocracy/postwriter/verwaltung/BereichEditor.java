package de.simocracy.postwriter.verwaltung;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;

import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class BereichEditor extends Fenster {

	private static final long serialVersionUID = -1812702866462506343L;
	private JPanel contentPane;
	private Einst einst;
	private Hauptmenue hm;
	private Bereich bereich;
	private ArrayList<Bereich> alBereich;
	private JLabel lblBestehenderBereich;
	private JLabel lblName;
	private JComboBox<String> comboBoxBestBereich;
	private JTextField txtName;
	private JPanel panel;
	private JButton btnNeu;
	private JButton btnndern;
	private JButton btnAbbrechen;
	private JButton btnLschen;
	private JLabel lblThemen;
	private JTextArea txtrAnzahlthemen;
	private ItemListener il;
	private JLabel lblWichtigkeit;
	private JComboBox<String> comboBoxWichtigkeit;

	public BereichEditor(Einst einst, Hauptmenue hm) {
		super(210,500);
		this.einst = einst;
		this.hm = hm;
		
		setTitle("PostWriter - Bereicheditor");
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new MigLayout("", "[][grow]", "[][][][grow][]"));
		
		this.lblBestehenderBereich = new JLabel("Bereich:");
		this.contentPane.add(this.lblBestehenderBereich, "cell 0 0");
		
		this.comboBoxBestBereich = new JComboBox<String>();
		comboBoxBestBereich.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				int min = 0;
				int max = comboBoxBestBereich.getItemCount();
				int aktuell = comboBoxBestBereich.getSelectedIndex();
				int neu = aktuell + arg0.getWheelRotation();
				
				if(min <= neu && neu < max){
					comboBoxBestBereich.setSelectedIndex(neu);
				}
			}
		});
		this.comboBoxBestBereich.setToolTipText("Auswahl eines bestehenden Bereichs");
		
		this.contentPane.add(this.comboBoxBestBereich, "cell 1 0,grow");
		
		lblWichtigkeit = new JLabel("Priorität:");
		contentPane.add(lblWichtigkeit, "cell 0 1");
		
		comboBoxWichtigkeit = new JComboBox<String>();
		comboBoxWichtigkeit.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				int min = 0;
				int max = comboBoxWichtigkeit.getItemCount();
				int aktuell = comboBoxWichtigkeit.getSelectedIndex();
				int neu = aktuell + arg0.getWheelRotation();
				
				if(min <= neu && neu < max){
					comboBoxWichtigkeit.setSelectedIndex(neu);
				}
			}
		});
		comboBoxWichtigkeit.setMaximumRowCount(11);
		for(int i = 0; i <= 10; i++){
			comboBoxWichtigkeit.addItem(String.valueOf(i));
		}
		contentPane.add(comboBoxWichtigkeit, "cell 1 1,growx");
		
		this.lblName = new JLabel("Name:");
		this.contentPane.add(this.lblName, "cell 0 2");
		
		this.txtName = new JTextField();
		this.txtName.setToolTipText("Name des Bereiches");
		
		this.contentPane.add(this.txtName, "cell 1 2,grow");
		this.txtName.setColumns(10);
		
		this.lblThemen = new JLabel("Themen:");
		this.contentPane.add(this.lblThemen, "cell 0 3");
		
		this.txtrAnzahlthemen = new JTextArea();
		this.txtrAnzahlthemen.setFocusable(false);
		this.txtrAnzahlthemen.setEditable(false);
		this.txtrAnzahlthemen.setBackground(UIManager.getColor("Panel.background"));
		this.contentPane.add(this.txtrAnzahlthemen, "cell 1 3,growx,aligny center");
		
		this.panel = new JPanel();
		this.contentPane.add(this.panel, "cell 0 4 2 1,grow");
		this.panel.setLayout(new MigLayout("", "[grow,fill]", "[]"));
		
		this.btnNeu = new JButton("Neu");
		btnNeu.setMnemonic('n');
		this.btnNeu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				neuerBereich();
			}
		});
		this.panel.add(this.btnNeu, "flowx,cell 0 0,alignx center");
		
		this.btnndern = new JButton("Ändern");
		btnndern.setMnemonic('ä');
		this.btnndern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editBereich();
			}
		});
		this.panel.add(this.btnndern, "cell 0 0,alignx center");
		
		this.btnAbbrechen = new JButton("Schließen");
		btnAbbrechen.setMnemonic('s');
		this.btnAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				beenden();
			}
		});
		
		this.btnLschen = new JButton("Löschen");
		btnLschen.setMnemonic('l');
		this.btnLschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				entferneBereich();
			}
		});
		this.panel.add(this.btnLschen, "cell 0 0");
		this.panel.add(this.btnAbbrechen, "cell 0 0,alignx center");
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				beenden();
			}
		});

		// Inhalt der Combobox holen
		comboBoxEinlesen();
		
		// ItemListener einbauen
		comboBoxBestBereich.addItemListener(il = new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				bereichAuswahl();
			}
		});
		
		setVisible(true);
	}
	
	// Einstellungen für ComboBox festlegen
	private void comboBoxEinlesen(){
		// ItemListener rauswerfen
		comboBoxBestBereich.removeItemListener(il);
		
		// Alter Inhalt der Box rauswerfen
		comboBoxBestBereich.removeAllItems();
		
		// Neuer Inhalt einlesen
		alBereich = einst.dao().sucheAlleBereicheMitArrays();
		for(int i = 1; i<alBereich.size();i++){
			comboBoxBestBereich.addItem(Helfer.varText(alBereich.get(i).getName()));
		}
		comboBoxBestBereich.addItem(alBereich.get(0).getName());
		
		// ItemListener einbauen
		comboBoxBestBereich.addItemListener(il);
		
		// Erstbereich einstellen
		setzeErstbereich();
	}

	// Aktionen beim auswaehlen
	private void bereichAuswahl(){
		// Hole Infos von Bereich
		String bereichname = Helfer.textVar(comboBoxBestBereich);
		bereich = alBereich.get(Helfer.sucheALBereichName(alBereich, bereichname));
		int bereichMenge = bereich.getThemenSize();
		
		// Platzhalterthema raushauen
		if(bereichname.equals("")){
			bereichMenge -= 1;
		}
		
		// Setze Infos von Bereich
		txtName.setText(bereichname);
		txtrAnzahlthemen.setText(String.valueOf(bereichMenge));
		comboBoxWichtigkeit.setSelectedItem(bereich.getWkat());
	}
	
	// Aktion beim Anlegen
	private void neuerBereich(){
		if (pruefen()){
			einst.out("Erstelle neuen Bereich.");
			// Fasse Infos in neuem Bereichobjekt zusammen
			bereich = new Bereich(alBereich.size(), Helfer.textVar(txtName));
			bereich.setWkat(Integer.parseInt(comboBoxWichtigkeit.getSelectedItem().toString()));
			
			// Fugue neuen Bereich in DB ein
			einst.dao().erfasseBereich(bereich);
			
			// Aktualisiere Einstellungen der Combobox
			comboBoxEinlesen();
		}
	}

	// Aktion beim Aendern
	private void editBereich(){
		if (pruefen()){
			einst.out("Ändere Bereich.");
			
			// Fasse Infos in neuem Bereichobjekt zusammen
			bereich = new Bereich(alBereich.get(Helfer.sucheALBereichName(alBereich,
					Helfer.textVar(comboBoxBestBereich))).getId(), Helfer.textVar(txtName));
			bereich.setWkat(Integer.parseInt(comboBoxWichtigkeit.getSelectedItem().toString()));
			
			// Schreibe neuen Namen in DB
			einst.dao().aendereBereich(bereich);
			
			// Aktualisiere Einstellungen der Combobox
			comboBoxEinlesen();
		}
	}

	// Aktion beim Schließen
	private void beenden(){
		einst.out("Schließe Bereicheditor.");
		dispose();
		hm.setVisible(true);
	}

	// Aktion beim Loeschen
	private void entferneBereich(){
		einst.out("Starte Löschvorgang eines Bereiches.");

		// Pruefe, ob Platzhalterbereich geloescht werden soll
		if(comboBoxBestBereich.getSelectedItem().equals("")){
			einst.out("Platzhalterbereich kann nicht gelöscht werden.");
			JOptionPane.showMessageDialog(null, "Der Platzhalterbereich kann nicht gelöscht werden.",
					"Platzhalterbereich",JOptionPane.ERROR_MESSAGE);
		}
		
		// Pruefe, ob Themen dem Bereich zugeordnet sind
		else if(bereich.getThemenSize()>0){
			einst.out("Themen sind dem Bereich zugeordnet.");
			JOptionPane.showMessageDialog(null, "Dem ausgewähltem Bereich sind " + bereich.getThemenSize() +
					" Themen zugeordnet." + Einst.nl + "Solange dem Bereich Themen zugeordnet sind, kann " +
					"er nicht gelöscht werden." + Einst.nl + "Sollte ein Post bereits gepostet sein mit " +
					"diesem Bereich, kann er ebenfalls" + Einst.nl + "nicht gelöscht werden, da diese " +
					"Themen nicht mehr editiert werden können.",
					"Themen sind dem Bereich zugeordnet",JOptionPane.ERROR_MESSAGE);
		}
		
		// Falls kein Thema zugeordnet ist
		else {
			einst.dao().loescheBereich(bereich.getId());
			
			// Aktualisiere bei Themen und Bereiche die bid
			einst.out("Löschen abgeschlossen. Aktualisiere Bereich-IDs.");
			alBereich.remove(Helfer.sucheALBereichName(alBereich, bereich.getName()));
			
			// bearbeite Bereiche
			for (int i = bereich.getId(); i < alBereich.size(); i++){
				einst.dao().aendereBereichBid(alBereich.get(i).getId(), i);
				
				// Bearbeite Themen
				for(int j = 0; j < alBereich.get(i).getThemenSize(); j++){
					einst.dao().aendereThemaBid(alBereich.get(i).getThema(j).getBid(), i);
				}
			}
			
			// Endausgaben
			einst.out("Aktualisierung der IDs abgeschlossen.");
			comboBoxEinlesen();
		}
	}
	
	// Erster Bereich auswaehlen
	private void setzeErstbereich(){
		// Setze ausgewaehlter Bereich
		comboBoxBestBereich.setSelectedIndex(0);
		
		// Setze Bereichinfos
		bereichAuswahl();
	}

	// Pruefungen bei Erstellen und Aendern
	private boolean pruefen(){
		
		// Pruefe, ob Name leer ist, wenn Ja, dann Error
		einst.out("Prüfe, ob für Bereich ein Name eingegeben wurde und ob er bereits existiert.");
		if(txtName.getText().equals("")){
			einst.out("Kein Bereichname eingegeben.");
			JOptionPane.showMessageDialog(null, "Bitte einen Namen für den Bereich eingeben. Ein " +
					"Platzhalterbereich ohne" + Einst.nl + "Name existiert bereits und kann im " +
					"Posteditor ausgewählt werden.", "Doppelter Bereich", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		// Pruefe, ob Name schon existiert und Name geaendert werden soll, wenn Ja, dann Error
		else if(Helfer.sucheALBereichName(alBereich, Helfer.textVar(txtName)) != -1 &&
				!Helfer.textVar(txtName).equals(Helfer.textVar(comboBoxBestBereich))){
			einst.out("Gewählter Bereichname existiert bereits.");
			JOptionPane.showMessageDialog(null, "Ein Bereich mit dem Namen \"" + txtName.getText() +
					"\" exisitert bereits." + Einst.nl + "Bitte einen anderen Namen wählen.", "Doppelter Bereich",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		// Wenn Pruefungen erfolgreich
		else {
			return true;
		}
	}
}
