package de.simocracy.postwriter.verwaltung;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.UIManager;

import de.simocracy.postwriter.*;

import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public abstract class Verwaltung extends Fenster {

	private static final long serialVersionUID = -7980220986057925858L;
	protected Einst einst;
	protected JScrollPane scrollPane;
	protected JComboBox<String> comboBoxPost;
	protected JComboBox<String> comboBoxBereich;
	protected JComboBox<String> comboBoxName;
	protected JTextArea txtrErstelltDatum;
	protected JTextArea txtrWoerter;
	protected Hauptmenue hm;
	protected String beendenText;
	protected JPanel panelInfos;
	protected JPanel contentPane;
	protected JTextArea txtrAnzahl;
	protected JTextArea txtrErstellt;
	protected ItemListener ilAuswahl;
	protected JButton btnStandard;
	protected JTextArea txtrAnzahlZeichen;
	protected JTextArea txtrZeichen;
	protected JComboBox<String> comboBoxWichtigkeit;
	protected JButton btnNeuausVorl;
	protected JLabel lblBereich;
	protected JButton btnNeu;
	
	private JLabel lblPost;
	private JLabel lblThemenname;
	private JLabel lblThemeninhalt;
	private JButton btnThemaAuswhlen;
	private JButton btnAbbrechen;
	private JLabel lblInformationen;
	private JButton btnLschen;
	private JLabel lblWichtigkeit;

	public Verwaltung(Einst einst, Hauptmenue hm) {
		super(550,650);
		this.einst = einst;
		this.hm = hm;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[115:115:115,fill][grow][grow]",
				"[][][][][][grow][][][][][][]"));
		
		lblPost = new JLabel("Post:");
		contentPane.add(lblPost, "cell 0 0,alignx trailing");
		
		comboBoxPost = new JComboBox<String>();
		comboBoxPost.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				int min = 0;
				int max = comboBoxPost.getItemCount();
				int aktuell = comboBoxPost.getSelectedIndex();
				int neu = aktuell + arg0.getWheelRotation();
				
				if(min <= neu && neu < max){
					comboBoxPost.setSelectedIndex(neu);
				}
			}
		});
		contentPane.add(comboBoxPost, "cell 1 0,growx");
		
		lblWichtigkeit = new JLabel("Priorität:");
		contentPane.add(lblWichtigkeit, "flowx,cell 2 0");
		
		lblBereich = new JLabel("Bereich:");
		contentPane.add(lblBereich, "cell 0 1,alignx trailing");
		
		comboBoxBereich = new JComboBox<String>();
		comboBoxBereich.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				int min = 0;
				int max = comboBoxBereich.getItemCount();
				int aktuell = comboBoxBereich.getSelectedIndex();
				int neu = aktuell + arg0.getWheelRotation();
				
				if(min <= neu && neu < max){
					comboBoxBereich.setSelectedIndex(neu);
				}
			}
		});
		contentPane.add(comboBoxBereich, "cell 1 1 2 1,growx");
		
		lblThemenname = new JLabel("Name:");
		contentPane.add(lblThemenname, "cell 0 2,alignx trailing");
		
		comboBoxName = new JComboBox<String>();
		comboBoxName.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				int min = 0;
				int max = comboBoxName.getItemCount();
				int aktuell = comboBoxName.getSelectedIndex();
				int neu = aktuell + e.getWheelRotation();
				
				if(min <= neu && neu < max){
					comboBoxName.setSelectedIndex(neu);
				}
			}
		});
		contentPane.add(comboBoxName, "cell 1 2 2 1,growx");
		
		lblThemeninhalt = new JLabel("Inhalt:");
		contentPane.add(lblThemeninhalt, "cell 0 3");
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 1 3 2 9,grow");
		
		lblInformationen = new JLabel("Informationen:");
		contentPane.add(lblInformationen, "cell 0 4");
		
		panelInfos();
		
		btnNeu = new JButton("Neu");
		btnNeu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				erstellen();
			}
		});
		contentPane.add(btnNeu, "flowy,cell 0 7");
		
		btnAbbrechen = new JButton("Schließen");
		btnAbbrechen.setMnemonic('s');
		btnAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abbrechen();
			}
		});
		
		btnNeuausVorl = new JButton("Neu (aus Vorl.)");
		btnNeuausVorl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				erstellen2();
			}
		});
		btnNeuausVorl.setEnabled(false);
		contentPane.add(btnNeuausVorl, "cell 0 8");
		
		btnThemaAuswhlen = new JButton("Bearbeiten");
		btnThemaAuswhlen.setMnemonic('b');
		btnThemaAuswhlen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bearbeiten();
			}
		});
		contentPane.add(btnThemaAuswhlen, "cell 0 9");
		
		btnLschen = new JButton("Löschen");
		btnLschen.setMnemonic('l');
		btnLschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loeschen();
			}
		});
		contentPane.add(btnLschen, "cell 0 10");
		contentPane.add(btnAbbrechen, "cell 0 11");
		
		comboBoxWichtigkeit = new JComboBox<String>();
		comboBoxWichtigkeit.setMaximumRowCount(12);
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
		contentPane.add(comboBoxWichtigkeit, "cell 2 0,growx");
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				abbrechen();
			}
		});
		
		// ItemListener zum Anzeigen
		ilAuswahl = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				auswahl();
			}
		};
		
		setVisible(true);
	}

	////////////////////////////////////
	// Methoden fuer Buttons zum ueberschreiben
	////////////////////////////////////
	
	protected void erstellen(){
		// Button fuer neues
	}
	
	protected void erstellen2(){
		// Button fuer neues 2
	}
	
	protected void bearbeiten(){
		// Button zum bearbeiten
	}
	
	protected void loeschen(){
		// Button zum loeschen
	}
	
	protected void abbrechen(){
		einst.out("Schließe " + beendenText + ".");
		dispose();
		hm.setVisible(true);
	}
	
	protected void auswahl(){
		// Aktion beim auswaehlen bei comboBoxName
	}
	
	public void einlesen(){
		// Aktion zum neuen Einlesen aus DB zum Ueberschreiben in Unterklassen
	}

	////////////////////////////////////
	// Sonstige Methoden
	////////////////////////////////////
	
	// PanelInfos definieren
	protected void panelInfos(){
		// Wird bei Themen und Kurznachrichten ueberschrieben
		panelInfos = new JPanel();
		contentPane.add(panelInfos, "cell 0 5,grow");
		panelInfos.setLayout(new MigLayout("", "[10:n:10,grow][grow]", "[][][][][][]"));
		
		txtrErstellt = new JTextArea();
		txtrErstellt.setBackground(UIManager.getColor("Panel.background"));
		txtrErstellt.setEditable(false);
		txtrErstellt.setFocusable(false);
		txtrErstellt.setText("Erstellt:");
		panelInfos.add(txtrErstellt, "cell 0 0 2 1,grow");
		
		txtrErstelltDatum = new JTextArea();
		txtrErstelltDatum.setFocusable(false);
		txtrErstelltDatum.setEditable(false);
		txtrErstelltDatum.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrErstelltDatum, "cell 1 1,grow");
		
		txtrAnzahl = new JTextArea();
		txtrAnzahl.setText("Anzahl Wörter:");
		txtrAnzahl.setFocusable(false);
		txtrAnzahl.setEditable(false);
		txtrAnzahl.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrAnzahl, "cell 0 2 2 1,grow");
		
		txtrWoerter = new JTextArea();
		txtrWoerter.setFocusable(false);
		txtrWoerter.setEditable(false);
		txtrWoerter.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrWoerter, "cell 1 3,grow");
		
		txtrAnzahlZeichen = new JTextArea();
		txtrAnzahlZeichen.setText("Anzahl Zeichen:");
		txtrAnzahlZeichen.setFocusable(false);
		txtrAnzahlZeichen.setEditable(false);
		txtrAnzahlZeichen.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrAnzahlZeichen, "cell 0 4 2 1,grow");
		
		txtrZeichen = new JTextArea();
		txtrZeichen.setFocusable(false);
		txtrZeichen.setEditable(false);
		txtrZeichen.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrZeichen, "cell 1 5,grow");
	}

	////////////////////////////////////
	// Aufbau Verwaltungen
	////////////////////////////////////
	/*
	 * Verwaltung.java
	 * 	PostVerwaltung.java
	 * 		VerwalteThemen.java
	 * 		VerwalteKurznachrichten.java
	 * 	EinstellungenVerwaltung.java
	 * 		VerwalteHeaderFooter.java
	 * 		VerwalteVorlagen.java
	 */

}
