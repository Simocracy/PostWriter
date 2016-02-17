package de.simocracy.postwriter.datumsrechner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JComboBox;

import de.simocracy.postwriter.*;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Dimension;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import javax.swing.JCheckBox;
import java.awt.GridLayout;

public abstract class FrameDatumsRechner extends Fenster {

	private static final long serialVersionUID = -7995595126548169921L;
	private JPanel contentPane;
	protected Einst einst;
	protected JFrame hm;
	private ButtonGroup bg;
	private JLabel lblRichtung;
	protected JRadioButton rdbtnRlSy;
	protected JRadioButton rdbtnSyRl;
	private JLabel lblEingabedatum;
	protected JComboBox<Integer> comboBoxTag;
	protected JComboBox<Integer> comboBoxMonat;
	protected JComboBox<Integer> comboBoxJahr;
	protected JComboBox<Integer> comboBoxStunde;
	protected JComboBox<Integer> comboBoxMinute;
	private JTextArea txtrTag;
	private JTextArea txtrMonat;
	private JTextArea txtrJahr;
	private JTextArea txtrStunden;
	private JTextArea txtrMinute;
	private JLabel lblGltigeEingabeAb;
	private JTextArea txtrITag;
	private JTextArea txtrIMon;
	private JTextArea txtrIJahr;
	private JTextArea txtrIStunde;
	private JTextArea txtrIMin;
	private JLabel lblBerechnetesDatum;
	private JPanel panelButtons;
	private JButton btnBerechnen;
	protected JButton btnSchlieen;
	protected JLabel lblErgebnis;
	private JButton btnHeutigesDatum;
	protected JButton btnEinfgen;
	protected JPanel panelEinfuegen;
	protected JLabel lblEinfgeoptionen;
	protected JCheckBox chckbxMitSydatum;
	private ItemListener ilTag;
	private ItemListener ilMon;
	private ItemListener ilJahr;
	private ItemListener ilStu;
	private ItemListener ilMin;
	private String[] berW;
	private boolean darfBerechnen;
	private int monatszahlTage;
	protected JCheckBox chckbxMitUhrzeit;
	protected JCheckBox chckbxMitZielangabe;

	public FrameDatumsRechner(Einst einst, JFrame hm) {
		super(340,500);
		setTitle("PostWriter - Datumsrechner");
		this.einst = einst;
		this.hm = hm;
		darfBerechnen = false;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][grow,fill][grow,fill][grow,fill][grow,fill][grow,fill]",
				"[][][fill][][][][][]"));
		
		lblRichtung = new JLabel("Richtung:");
		contentPane.add(lblRichtung, "cell 0 0");
		
		rdbtnRlSy = new JRadioButton("RL → SY");
		rdbtnRlSy.setSelected(true);
		rdbtnRlSy.setToolTipText("SY-Datum aus RL-Datum errechnen");
		contentPane.add(rdbtnRlSy, "flowy,cell 1 0 2 1");
		
		bg = new ButtonGroup();
		bg.add(rdbtnRlSy);
		
		btnHeutigesDatum = new JButton("Heutiges RL-Datum");
		btnHeutigesDatum.setMnemonic('h');
		btnHeutigesDatum.setToolTipText("Heutiges RL-Datum als Eingabedatum setzen");
		contentPane.add(btnHeutigesDatum, "cell 3 0 3 2,growy");
		
		rdbtnSyRl = new JRadioButton("SY → RL");
		rdbtnSyRl.setToolTipText("RL-Datum aus SY-Datum errechnen");
		contentPane.add(rdbtnSyRl, "cell 1 1 2 1");
		bg.add(rdbtnSyRl);
		
		lblEingabedatum = new JLabel("Eingabedatum:");
		contentPane.add(lblEingabedatum, "cell 0 2");
		
		txtrTag = new JTextArea();
		txtrTag.setEditable(false);
		txtrTag.setFocusable(false);
		txtrTag.setBackground(UIManager.getColor("Panel.background"));
		txtrTag.setText("Tag:");
		contentPane.add(txtrTag, "cell 1 2");
		
		txtrMonat = new JTextArea();
		txtrMonat.setText("Monat:");
		txtrMonat.setFocusable(false);
		txtrMonat.setEditable(false);
		txtrMonat.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(txtrMonat, "cell 2 2");
		
		txtrJahr = new JTextArea();
		txtrJahr.setText("Jahr:");
		txtrJahr.setFocusable(false);
		txtrJahr.setEditable(false);
		txtrJahr.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(txtrJahr, "cell 3 2");
		
		txtrStunden = new JTextArea();
		txtrStunden.setText("Stunde:");
		txtrStunden.setFocusable(false);
		txtrStunden.setEditable(false);
		txtrStunden.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(txtrStunden, "cell 4 2");
		
		txtrMinute = new JTextArea();
		txtrMinute.setText("Minute:");
		txtrMinute.setFocusable(false);
		txtrMinute.setEditable(false);
		txtrMinute.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(txtrMinute, "cell 5 2");
		
		comboBoxTag = new JComboBox<Integer>();
		comboBoxTag.setMaximumSize(new Dimension(64, 32767));
		comboBoxTag.setEditable(true);
		comboBoxTag.setMaximumRowCount(10);
		contentPane.add(comboBoxTag, "cell 1 3");
		
		comboBoxMonat = new JComboBox<Integer>();
		comboBoxMonat.setMaximumSize(new Dimension(64, 32767));
		comboBoxMonat.setEditable(true);
		comboBoxMonat.setMaximumRowCount(10);
		contentPane.add(comboBoxMonat, "cell 2 3");
		
		comboBoxJahr = new JComboBox<Integer>();
		comboBoxJahr.setMaximumSize(new Dimension(64, 32767));
		comboBoxJahr.setEditable(true);
		comboBoxJahr.setMaximumRowCount(10);
		contentPane.add(comboBoxJahr, "cell 3 3");
		
		comboBoxStunde = new JComboBox<Integer>();
		comboBoxStunde.setMaximumSize(new Dimension(64, 32767));
		comboBoxStunde.setEditable(true);
		comboBoxStunde.setMaximumRowCount(10);
		contentPane.add(comboBoxStunde, "cell 4 3");
		
		comboBoxMinute = new JComboBox<Integer>();
		comboBoxMinute.setMaximumSize(new Dimension(64, 32767));
		comboBoxMinute.setEditable(true);
		comboBoxMinute.setMaximumRowCount(10);
		contentPane.add(comboBoxMinute, "cell 5 3");
		
		lblGltigeEingabeAb = new JLabel("Gültige Eingabe ab:");
		contentPane.add(lblGltigeEingabeAb, "cell 0 4");
		
		txtrITag = new JTextArea();
		txtrITag.setFocusable(false);
		txtrITag.setEditable(false);
		txtrITag.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(txtrITag, "cell 1 4,alignx center");
		
		txtrIMon = new JTextArea();
		txtrIMon.setFocusable(false);
		txtrIMon.setEditable(false);
		txtrIMon.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(txtrIMon, "cell 2 4,alignx center");
		
		txtrIJahr = new JTextArea();
		txtrIJahr.setFocusable(false);
		txtrIJahr.setEditable(false);
		txtrIJahr.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(txtrIJahr, "cell 3 4,alignx center");
		
		txtrIStunde = new JTextArea();
		txtrIStunde.setFocusable(false);
		txtrIStunde.setEditable(false);
		txtrIStunde.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(txtrIStunde, "cell 4 4,alignx center");
		
		txtrIMin = new JTextArea();
		txtrIMin.setFocusable(false);
		txtrIMin.setEditable(false);
		txtrIMin.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(txtrIMin, "cell 5 4,alignx center");
		
		lblBerechnetesDatum = new JLabel("Berechnetes Datum:");
		contentPane.add(lblBerechnetesDatum, "cell 0 5");
		
		lblErgebnis = new JLabel("");
		contentPane.add(lblErgebnis, "cell 1 5 5 1");
		
		panelButtons = new JPanel();
		contentPane.add(panelButtons, "cell 0 6 6 1,grow");
		
		btnBerechnen = new JButton("Berechnen");
		btnBerechnen.setMnemonic('b');
		btnBerechnen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				berechnen();
			}
		});
		panelButtons.add(btnBerechnen);
		
		btnSchlieen = new JButton("Schließen");
		btnSchlieen.setMnemonic('s');
		btnSchlieen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				beenden();
			}
		});
		
		btnEinfgen = new JButton("Einfügen");
		btnEinfgen.setMnemonic('e');
		panelButtons.add(btnEinfgen);
		panelButtons.add(btnSchlieen);
		
		panelEinfuegen = new JPanel();
		contentPane.add(panelEinfuegen, "cell 0 7 6 1,grow");
		panelEinfuegen.setLayout(new GridLayout(0, 1, 0, 0));
		
		lblEinfgeoptionen = new JLabel("Einfügeoptionen:");
		panelEinfuegen.add(lblEinfgeoptionen);
		
		chckbxMitSydatum = new JCheckBox("Mit Ausgangsdatum");
		chckbxMitSydatum.setToolTipText("Wenn markiert, wird das eingegebene Datum mit eingefügt");
		chckbxMitSydatum.setMnemonic('a');
		panelEinfuegen.add(chckbxMitSydatum);
		
		chckbxMitZielangabe = new JCheckBox("Mit Zielangabe");
		chckbxMitZielangabe.setToolTipText("Wenn markiert wird die Angabe, ob das berechnete Datum das RL- " +
				"oder SY-Datum ist, eingefügt");
		chckbxMitZielangabe.setMnemonic('z');
		panelEinfuegen.add(chckbxMitZielangabe);
		
		chckbxMitUhrzeit = new JCheckBox("Mit Uhrzeit");
		chckbxMitUhrzeit.setToolTipText("Wenn markiert, werden die Uhrzeiten bei Ausgangs- und berechneten " +
				"Datum eingefügt");
		chckbxMitUhrzeit.setMnemonic('u');
		panelEinfuegen.add(chckbxMitUhrzeit);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				beenden();
			}
		});
		
		// ItemListener fuer Eingabehinweis
		eingabeHinweis();
		rdbtnRlSy.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				eingabeHinweis();
			}
		});
		
		// Heutiges Datum einbauen
		btnHeutigesDatum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				heute();
			}
		});
		
		// MouseWheelListener
		comboBoxTag.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				// Berechnung des neuen Wertes
				int alt = Integer.parseInt(comboBoxTag.getSelectedItem().toString());
				int neu = alt - arg0.getWheelRotation();
				
				if(neu > monatszahlTage) neu -= monatszahlTage;
				if(neu < 1) neu += monatszahlTage;
				comboBoxTag.setSelectedItem(neu);
			}
		});
		comboBoxMonat.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				// Berechnung des neuen Wertes
				int alt = Integer.parseInt(comboBoxMonat.getSelectedItem().toString());
				int neu = alt - arg0.getWheelRotation();
				
				if(neu > 12) neu -= 12;
				if(neu < 1) neu += 12;
				comboBoxMonat.setSelectedItem(neu);
				
				fuelleTag();
			}
		});
		comboBoxJahr.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				// Berechnung des neuen Wertes
				int alt = Integer.parseInt(comboBoxJahr.getSelectedItem().toString());
				int neu = alt - arg0.getWheelRotation();
				
				// Eintragen des neuen Wertes nur bei zulaessigem Bereich
				if(neu > 2007){
					comboBoxJahr.setSelectedItem(neu);
				}
			}
		});
		comboBoxStunde.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				// Berechnung des neuen Wertes
				int alt = Integer.parseInt(comboBoxStunde.getSelectedItem().toString());
				int neu = alt - arg0.getWheelRotation();
				
				if(neu > 23) neu -= 24;
				if(neu < 0) neu += 24;
				comboBoxStunde.setSelectedItem(neu);
			}
		});
		comboBoxMinute.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				// Berechnung des neuen Wertes
				int alt = Integer.parseInt(comboBoxMinute.getSelectedItem().toString());
				int neu = alt - arg0.getWheelRotation();
				
				if(neu > 59) neu -= 60;
				if(neu < 0) neu += 60;
				comboBoxMinute.setSelectedItem(neu);
			}
		});
		
		monatszahlTage = 31;
		
		// Fuellen der ComboBoxen
		fuellen();
		heute();

		// ItemListener für Sofortberechnung
		ilTag = new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				berechnen();
			}
		};
		ilMon = new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				fuelleTag();
				berechnen();
			}
		};
		ilJahr = new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				fuelleTag();
				berechnen();
			}
		};
		ilStu = new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				berechnen();
			}
		};
		ilMin = new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				berechnen();
			}
		};
		
		ilEinbau();
		
		darfBerechnen = true;
		
		berechnen();
		
		setVisible(true);
	}
	
	// ItemListener einbauen
	private void ilEinbau() {
		comboBoxTag.addItemListener(ilTag);
		comboBoxMonat.addItemListener(ilMon);
		comboBoxJahr.addItemListener(ilJahr);
		comboBoxStunde.addItemListener(ilStu);
		comboBoxMinute.addItemListener(ilMin);
	}
	
	// ItemListener ausbauen
	private void ilAusbau() {
		comboBoxTag.removeItemListener(ilTag);
		comboBoxMonat.removeItemListener(ilMon);
		comboBoxJahr.removeItemListener(ilJahr);
		comboBoxStunde.removeItemListener(ilStu);
		comboBoxMinute.removeItemListener(ilMin);
	}
	
	// Fuellung der ComboBoxen
	private void fuellen(){
		// ComboBoxMonat
		for(int i = 1; i < 13; i++){
			comboBoxMonat.addItem(i);
		}
		
		// comboBoxJahr
		for(int i = 2008; i < 2100; i++){
			comboBoxJahr.addItem(i);
		}
		
		// comboBoxStunde
		for(int i = 0; i < 24; i++){
			comboBoxStunde.addItem(i);
		}
		
		// comboBoxMinute
		for(int i = 0; i < 60; i++){
			comboBoxMinute.addItem(i);
		}
		
		fuelleTag();
	}
	
	// Füllen der Tagesbox
	private void fuelleTag() {
		ilAusbau();
		int alterTag = 1;
		if(comboBoxTag.getSelectedItem() != null) {
			alterTag = Integer.parseInt(comboBoxTag.getSelectedItem().toString());
		}
		comboBoxTag.removeAllItems();
		
		// Tagesanzahl festlegen
		switch (Integer.parseInt(comboBoxMonat.getSelectedItem().toString())) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			monatszahlTage = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			monatszahlTage = 30;
			break;
		case 2:
			monatszahlTage = DatumRechner.schaltjahrFeb(Integer.parseInt(comboBoxJahr.getSelectedItem().toString()));
			break;
		}
		
		// Tage einbauen
		for(int i = 1; i <= monatszahlTage; i++) {
			comboBoxTag.addItem(i);
		}
		
		ilEinbau();
		
		// Alter Tag festlegen
		if(alterTag > monatszahlTage) {
			comboBoxTag.setSelectedItem(monatszahlTage);
		}
		else {
			comboBoxTag.setSelectedItem(alterTag);
		}
		
	}
	
	// Setze Hinweise der fruehsten erlaubten Eingabe
	private void eingabeHinweis(){
//		ilAusbau();
		
		// Pruefung, welche Richtung
		if(rdbtnRlSy.isSelected()){
			// Setze Hinweise fuer RL>SY
			txtrITag.setText("1");
			txtrIMon.setText("10");
			txtrIJahr.setText("2008");
			txtrIStunde.setText("0");
			txtrIMin.setText("0");
		}else{
			// Setze Hinweise fuer RL>SY
			txtrITag.setText("1");
			txtrIMon.setText("1");
			txtrIJahr.setText("2020");
			txtrIStunde.setText("-");
			txtrIMin.setText("-");
			
			if(Integer.parseInt(comboBoxJahr.getSelectedItem().toString()) < 2020) {
				comboBoxJahr.setSelectedItem("2020");
			}
		}
		
		// Setze altes berechnetes Datum als Ausgangswerte
		if(berW != null) {
//			comboBoxTag.setSelectedItem(berechneteWerte[0]);
//			comboBoxMonat.setSelectedItem(berechneteWerte[1]);
//			comboBoxJahr.setSelectedItem(berechneteWerte[2]);
//			comboBoxStunde.setSelectedItem(berechneteWerte[3]);
//			comboBoxMinute.setSelectedItem(berechneteWerte[4]);
		}
		
//		ilEinbau();
		
		lblErgebnis.setText("");
		
//		berechnen();
	}
	
	// Heutiges Datum in ComboBoxen einbauen
	private void heute(){
//		while(comboBoxTag.getItemListeners().length < 0) {
			ilAusbau();
//		}
		String datumRL = einst.getNeuesPostDatum();
		
		// Heutiges Datum in Variablen Speichern
		String tag = datumRL.substring(0, 2);
		String mon = datumRL.substring(3, 5);
		String jahr = datumRL.substring(6, 10);
		String stu = datumRL.substring(11, 13);
		String min = datumRL.substring(14, 16);
		
		// Datum einbauen
		comboBoxJahr.setSelectedItem(jahr);
		comboBoxMonat.setSelectedItem(mon);
		comboBoxTag.setSelectedItem(tag);
		comboBoxStunde.setSelectedItem(stu);
		comboBoxMinute.setSelectedItem(min);
		
		rdbtnRlSy.setSelected(true);
		
		fuelleTag();
		
		berechnen();
	}
	
	// Berechnung des Datums
	protected void berechnen(){
		// Auswahl der Richtung
		if(rdbtnRlSy.isSelected() && darfBerechnen){
			// RL>SY
			berechneHin();
		}
		else if(darfBerechnen){
			// SY>RL
			berechneZuruck();
		}
	}
	
	// Berechnung RL>SY
	private void berechneHin(){
		// Initialisierung der Variablen
		int iJahr = 0;
		int iMon = 0;
		int iTag = 0;
		int iStu = 0;
		int iMin = 0;
		int pJahr = 2008;
		int pMon = 9;
		
		// Initialisierung der Pruefvariablen
		boolean sindZahlen = false;
		boolean zuFrueh = true;
		boolean tagesAngabe = false;
		
		// Parsen der Eingaben und Error, wenn Fehler
		try {
			String s = comboBoxJahr.getSelectedItem().toString();
			iJahr = Integer.parseInt(s);
			iMon = Integer.parseInt(comboBoxMonat.getSelectedItem().toString());
			iTag = Integer.parseInt(comboBoxTag.getSelectedItem().toString());
			iStu = Integer.parseInt(comboBoxStunde.getSelectedItem().toString());
			iMin = Integer.parseInt(comboBoxMinute.getSelectedItem().toString());
			sindZahlen = true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Fehler beim Einlesen der Eingaben.",
					"Fehler bei Eingabe", JOptionPane.ERROR_MESSAGE);
			sindZahlen = false;
		}

		// Weitere Ausfuehrung nur, wenn parsen funktioniert hat
		if(sindZahlen){
			// Pruefungen, ob Angaben zulaessig
			if(iJahr < pJahr){
				// Input kleiner als Minumum, Fehler ausgeben
				JOptionPane.showMessageDialog(null, "Das gewählte Datum liegt vor dem frühstmöglichsten " +
						"Datum" + Einst.nl + "und kann daher nicht umgerechnet werden.",
						"Fehler bei Eingabe", JOptionPane.ERROR_MESSAGE);
				zuFrueh = true;
			}
			else if(iJahr == pJahr){
				// Input ist RL-Jahr 2008
				if(iMon < pMon){
					// Bei zu fruehem Monat Fehler
					JOptionPane.showMessageDialog(null, "Das gewählte Datum liegt vor dem " +
							"frühstmöglichsten Datum" + Einst.nl + "und kann daher nicht " +
							"umgerechnet werden.", "Fehler bei Eingabe", JOptionPane.ERROR_MESSAGE);
					zuFrueh = true;
				}
				else{
					// Passende Angabe
					zuFrueh = false;
				}
			}
			else{
				// Passene Angabe
				zuFrueh = false;
			}
		}
		
		// Monatspruefungen, ob Tagesangaben passen
		if(!zuFrueh){
			switch (iMon) {
			case 2: // Februar
				// Pruefe, ob Schaltjahr
				int febTage = DatumRechner.schaltjahrFeb(iJahr);
				if(iTag <= febTage) tagesAngabe = true;
				break;
			case 4: // April
			case 6: // Juni
			case 9: // September
			case 11: // November
				if(iTag < 31) tagesAngabe = true;
				break;
			default: // Monate mit 31 Tagen
				tagesAngabe = true;
				break;
			}
			
			// Wenn alles passt, berechnen lassen und ausgeben
			if(tagesAngabe){
				// Berechnung
				berW = DatumRechner.rlSy(iTag, iMon, iJahr, iStu, iMin, einst);
				
				// Ausgabe
				lblErgebnis.setText(berW[0] + "." + berW[1] + "." +
						berW[2] + " " + berW[3] + ":" + berW[4] + " Uhr");
				
				// Rückrechnung
				rueck(iTag, iMon, iJahr, iStu, iMin, true);
			}
			else{
				JOptionPane.showMessageDialog(null, "Den gewählten Tag gibt es im gewählten Monat nicht.",
						"Fehler bei Eingabe", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	// Berechnung SY>RL
	private void berechneZuruck(){
		// Initialisierung der Variablen
		
		int iJahr = 0;
		int iMon = 0;
		int iTag = 0;
		int pJahr = 2020;
		int iStu = 0;
		int iMin = 0;
		
		// Initialisierung der Pruefvariablen
		boolean sindZahlen = false;
		boolean tagesAngabe = false;
		
		// Parsen der Eingaben und Error, wenn Fehler
		try {
			iJahr = Integer.parseInt(comboBoxJahr.getSelectedItem().toString());
			iMon = Integer.parseInt(comboBoxMonat.getSelectedItem().toString());
			iTag = Integer.parseInt(comboBoxTag.getSelectedItem().toString());
			iStu = Integer.parseInt(comboBoxStunde.getSelectedItem().toString());
			iMin = Integer.parseInt(comboBoxMinute.getSelectedItem().toString());
			sindZahlen = true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Fehler beim Einlesen der Eingaben.",
					"Fehler bei Eingabe", JOptionPane.ERROR_MESSAGE);
			sindZahlen = false;
		}

		// Weitere Ausfuehrung nur, wenn parsen funktioniert hat
		if(sindZahlen){
			// Pruefungen, ob Angaben zulaessig
			if(iJahr < pJahr){
				// Input kleiner als Minumum, Fehler ausgeben
				JOptionPane.showMessageDialog(null, "Das gewählte Datum liegt vor dem " +
						"frühstmöglichsten Datum" + Einst.nl + "und kann daher nicht umgerechnet " +
						"werden.", "Fehler bei Eingabe", JOptionPane.ERROR_MESSAGE);
			}
			else{
				// Monatspruefungen, ob Tagesangaben passen
				switch (iMon) {
				case 2: // Februar
					// Pruefe, ob Schaltjahr
					int febTage = DatumRechner.schaltjahrFeb(iJahr);
					if(iTag <= febTage) tagesAngabe = true;
					break;
				case 4: // April
				case 6: // Juni
				case 9: // September
				case 11: // November
					if(iTag < 31) tagesAngabe = true;
					break;
				default: // Monate mit 31 Tagen
					tagesAngabe = true;
					break;
				}
				
				// Wenn alles passt, berechnen lassen und ausgeben
				if(tagesAngabe){
					// Berechnung
					berW = DatumRechner.syRl(iTag, iMon, iJahr, iStu, iMin, einst);
					
					// Ausgabe
					lblErgebnis.setText(berW[0] + "." + berW[1] + "." +
							berW[2] + " " + berW[3] + ":" + berW[4] + " Uhr");
					
					// Rückrechnung
					rueck(iTag, iMon, iJahr, iStu, iMin, false);
				}
				else{
					JOptionPane.showMessageDialog(null, "Den gewählten Tag gibt es im gewählten Monat nicht.",
							"Fehler bei Eingabe", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	// Rueckberechnung
	private void rueck(int iTag, int iMon, int iJahr, int iStu, int iMin, boolean syRL) {
		String[] rueckWerte = new String[5];
		if(syRL) rueckWerte = DatumRechner.syRl(berW[0], berW[1], berW[2], berW[3], berW[4], einst);
		if(!syRL) rueckWerte = DatumRechner.rlSy(berW[0], berW[1], berW[2], berW[3], berW[4], einst);
		
		int rwTag = Integer.parseInt(rueckWerte[0]);
		int rwMon = Integer.parseInt(rueckWerte[1]);
		int rwJahr = Integer.parseInt(rueckWerte[2]);
		int rwStu = Integer.parseInt(rueckWerte[3]);
		int rwMin = Integer.parseInt(rueckWerte[4]);
		
		if(rwTag == iTag && rwMon == iMon && rwJahr == iJahr && rwStu == iStu &&
				((rwMin + 13) > iMin && (rwMin - 13) < iMin)) {
			lblErgebnis.setText(lblErgebnis.getText() + " (Zurückberechnung erfolgreich.)");
		}else {
			lblErgebnis.setText(lblErgebnis.getText() + " (Ergebnis könnte falsch sein.)");
		}
	}
	
	// Aktion beim Beenden
	protected void beenden(){
		einst.out("Beende Datumsrechner.");
		dispose();
		hm.setVisible(true);
	}

}
