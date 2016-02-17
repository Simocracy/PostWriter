package de.simocracy.postwriter.themaEditor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class StandardtextEinfuegen extends JDialog {

	private static final long serialVersionUID = 3309130232280398535L;
	private JPanel contentPane;
	private ArrayList<Standardtext> alStand;
	private Standardtext stand;
	private Einst einst;
	private boolean beenden;
	private JLabel lblName;
	private JLabel lblInhalt;
	private JComboBox<String> comboBoxName;
	private JScrollPane scrollPane;
	private JTextArea txtrInhalt;
	private JPanel panelInfos;
	private JTextArea txtrErstellt;
	private JTextArea txtrErstelltDatum;
	private JTextArea txtrAnzahl;
	private JTextArea txtrWoerter;
	private JLabel lblInformationen;
	private JButton btnEinfgen;
	private JButton btnSchlieen;
	private JTextArea txtrAnzahlZeichen;
	private JTextArea txtrZeichen;

	public StandardtextEinfuegen(Einst einst) {
		beenden = false;
		this.einst = einst;
		
		int bildHoehe = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		int bildBreite = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int hoehe = 350;
		int breite = 500;
		setBounds((bildBreite-breite)/2, (bildHoehe-hoehe)/2, breite, hoehe);
		
		setModal(true);
		setIconImage(Einst.icon);
		setTitle("PostWriter - Standardtext einfügen");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[fill][grow]", "[][][][grow][][]"));
		
		lblName = new JLabel("Name:");
		contentPane.add(lblName, "cell 0 0");
		
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
		contentPane.add(comboBoxName, "cell 1 0,growx");
		
		lblInhalt = new JLabel("Inhalt:");
		contentPane.add(lblInhalt, "cell 0 1");
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 1 1 1 5,grow");
		
		txtrInhalt = new JTextArea();
		txtrInhalt.setLineWrap(true);
		txtrInhalt.setWrapStyleWord(true);
		scrollPane.setViewportView(txtrInhalt);
		
		lblInformationen = new JLabel("Informationen:");
		contentPane.add(lblInformationen, "cell 0 2");
		
		panelInfos = new JPanel();
		contentPane.add(panelInfos, "cell 0 3,grow");
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
		
		btnEinfgen = new JButton("Einfügen");
		btnEinfgen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(btnEinfgen, "cell 0 4");
		
		btnSchlieen = new JButton("Schließen");
		btnSchlieen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				beenden();
			}
		});
		contentPane.add(btnSchlieen, "cell 0 5");
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				beenden();
			}
		});

		comboBoxName.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				auswahl();
			}
		});
		holeStandardtexte();
		
		setVisible(true);
		
	}
	
	// Standardtexte holen
	private void holeStandardtexte(){
		// Array fuellen
		alStand = einst.dao().sucheAlleStandardtexte();
		
		if(alStand.size() > 1){
		// Wenn Standardnachrichten in DB
			
			// ComboBoxName fuellen
			for(int i = 1; i < alStand.size(); i++){
				comboBoxName.addItem(Helfer.varText(alStand.get(i).getName()));
			}
			
			// Erster Standardtext selektieren
			comboBoxName.setSelectedIndex(0);
			
			// Auswahl dafuer
			auswahl();
		}
		else{
		// Aktionen, falls keine Standardnachrichten verfuegbar
			dispose();
			JOptionPane.showMessageDialog(null, "Es sind keine Standardtexte in der Datenbank verfügbar.",
					"Keine Standardtexte", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	// Auswahl eines Standardtextes
	private void auswahl(){
		// Suche in Array
		stand = alStand.get(Helfer.sucheALStandardtextName(alStand, Helfer.textVar(comboBoxName)));
		
		// Einbau der Infos
		txtrInhalt.setText(Helfer.varText(stand.getInhalt()));
		txtrErstelltDatum.setText(stand.getDatum());
		txtrWoerter.setText(Helfer.zahl(txtrInhalt));
		txtrZeichen.setText(String.valueOf(txtrInhalt.getText().length()));
		txtrInhalt.setCaretPosition(0);
	}
	
	// Text in Thema einfuegen
	protected Standardtext verwenden(){
		// Pruefung, ob was ausgegeben werden soll
		if(beenden){
			return null;
		}
		else{
			return stand;
		}
	}
	
	// Fenster schließen
	private void beenden(){
		beenden = true;
		dispose();
	}
	
}
