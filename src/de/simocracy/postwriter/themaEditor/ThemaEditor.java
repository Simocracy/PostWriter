package de.simocracy.postwriter.themaEditor;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.UIManager;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.datumsrechner.FrameDRThemen;
import de.simocracy.postwriter.fachklassen.Standardtext;
import de.simocracy.postwriter.verwaltung.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.List;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public abstract class ThemaEditor extends Fenster {
	
	private static final long serialVersionUID = -3474939242694169904L;
	protected Einst einst;
	protected JComboBox<String> comboBoxBereich;
	protected JTextField txtThemenname;
	protected JTextArea textAreaThema;
	protected JComboBox<String> comboBoxPost;
	protected JTextArea txtrErstelltDatum;
	protected JTextArea txtrWoerterZahl;
	protected JButton btnStandardtext;
	protected Verwaltung verw;
	protected JButton btnVariable;
	protected JTextArea txtrZeichenZahl;
	protected JComboBox<String> comboBoxWichtigkeit;
	protected JLabel lblBereich;
	protected JButton btnOfftopic;
	protected JLabel lblPostformatierungen;
	protected List listFormatierungen;
	protected JPanel panelFormatierungen;
	protected JPanel panelInfos;
	protected JPanel contentPane;
	
	private JLabel lblName;
	private JScrollPane scrollPane;
	private JButton btnFett;
	private JButton btnKursiv;
	private JButton btnUnterstrichen;
	private JButton btnGre;
	private JButton btnLink;
	private JButton btnBild;
	private JButton btnListe;
	private JButton btnListenpunkt;
	private JButton btnEinschubLinks;
	private JButton btnZitat;
	private JButton btnSpeichern;
	private JButton btnAbbrechen;
	private JLabel lblFormatierungen;
	private JLabel lblInformationen;
	private JLabel lblPost;
	private JButton btnPoli;
	private JButton btnZumDisku;
	private JButton btnZumWiki;
	private JButton btnZumWikiartikel;
	private JTextArea txtrErstellt;
	private JTextArea txtrAnzahlWrter;
	private JLabel lblEinfgen;
	private JButton btnDatumsrechner;
	private JTextArea txtrAnzahlZeichen;
	private JLabel lblWichtigkeit;

	public ThemaEditor(Einst einst, Verwaltung verw) {
		super(650,800);
		this.einst = einst;
		this.verw = verw;
		
		setTitle("PostWriter - Themeneditor");
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new MigLayout("", "[fill][grow][grow][fill]", "[][][][][][][][][][][][][][][][][][][grow][][]"));
		
		this.lblPost = new JLabel("Post:");
		this.contentPane.add(this.lblPost, "cell 0 0,alignx trailing,aligny center");
		
		this.comboBoxPost = new JComboBox<String>();
		comboBoxPost.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				int min = 0;
				int max = comboBoxPost.getItemCount();
				int aktuell = comboBoxPost.getSelectedIndex();
				int neu = aktuell + e.getWheelRotation();
				
				if(min <= neu && neu < max){
					comboBoxPost.setSelectedIndex(neu);
				}
			}
		});
		this.contentPane.add(this.comboBoxPost, "cell 1 0,growx,aligny center");
		
		lblWichtigkeit = new JLabel("Priorität:");
		contentPane.add(lblWichtigkeit, "flowx,cell 2 0 2 1");
		
		this.lblBereich = new JLabel("Bereich:");
		this.contentPane.add(this.lblBereich, "cell 0 1,alignx trailing,aligny center");
		
		this.comboBoxBereich = new JComboBox<String>();
		comboBoxBereich.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				int min = 0;
				int max = comboBoxBereich.getItemCount();
				int aktuell = comboBoxBereich.getSelectedIndex();
				int neu = aktuell + e.getWheelRotation();
				
				if(min <= neu && neu < max){
					comboBoxBereich.setSelectedIndex(neu);
				}
			}
		});
		this.comboBoxBereich.setEnabled(false);
		this.contentPane.add(this.comboBoxBereich, "cell 1 1 3 1,growx,aligny center");
		
		this.lblName = new JLabel("Name:");
		this.contentPane.add(this.lblName, "cell 0 2,alignx trailing,aligny center");
		
		this.txtThemenname = new JTextField();
		this.contentPane.add(this.txtThemenname, "cell 1 2 3 1,growx,aligny center");
		this.txtThemenname.setColumns(10);
		
		this.scrollPane = new JScrollPane();
		this.contentPane.add(this.scrollPane, "cell 1 3 2 18,grow");
		
		this.textAreaThema = new JTextArea();
		this.textAreaThema.setWrapStyleWord(true);
		this.textAreaThema.setLineWrap(true);
		this.scrollPane.setViewportView(this.textAreaThema);
		
		this.btnUnterstrichen = new JButton("Unterstrichen");
		btnUnterstrichen.setMnemonic('u');
		this.btnUnterstrichen.setToolTipText("[U][/U] einfügen");
		this.btnUnterstrichen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bbU();
			}
		});
		
		this.lblInformationen = new JLabel("Informationen:");
		this.contentPane.add(this.lblInformationen, "cell 3 3");
		
		this.btnPoli = new JButton("Zum Poli");
		this.btnPoli.setToolTipText("Politikthread im Browser öffnen");
		this.btnPoli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iPoli();
			}
		});
		this.contentPane.add(this.btnPoli, "cell 3 4");
		
		this.btnZumDisku = new JButton("Zum Disku");
		this.btnZumDisku.setToolTipText("Diskussionsthread im Browser öffnen");
		this.btnZumDisku.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iDisku();
			}
		});
		this.contentPane.add(this.btnZumDisku, "cell 3 5");
		this.contentPane.add(this.btnUnterstrichen, "cell 0 6");
		
		this.btnGre = new JButton("Größe");
		btnGre.setMnemonic('g');
		this.btnGre.setToolTipText("[SIZE][/[SIZE]] einfügen");
		this.btnGre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bbSize();
			}
		});
		
		this.btnZumWiki = new JButton("Zum Wiki");
		this.btnZumWiki.setToolTipText("Wikocracy-Portal im Browser öffnen");
		this.btnZumWiki.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iWiki();
			}
		});
		this.contentPane.add(this.btnZumWiki, "cell 3 6");
		this.contentPane.add(this.btnGre, "cell 0 7");
		
		this.btnEinschubLinks = new JButton("Einschub");
		btnEinschubLinks.setMnemonic('e');
		this.btnEinschubLinks.setToolTipText("[INDENT][/INDENT] einfügen");
		this.btnEinschubLinks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bbIndent();
			}
		});
		
		this.btnZumWikiartikel = new JButton("Zum Wikiartikel");
		this.btnZumWikiartikel.setToolTipText("Wikiartikel im Browser öffnen");
		this.contentPane.add(this.btnZumWikiartikel, "cell 3 7,aligny top");
		
		// Button zum Wikiartikel
		einst.setALWikiArtikel(btnZumWikiartikel);
		this.contentPane.add(this.btnEinschubLinks, "cell 0 8");
		
		btnDatumsrechner = new JButton("Datumsrechner");
		btnDatumsrechner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				datum();
			}
		});
		contentPane.add(btnDatumsrechner, "cell 3 8");
		
		this.lblEinfgen = new JLabel("Einfügen:");
		this.contentPane.add(this.lblEinfgen, "cell 0 9");
		
		this.btnStandardtext = new JButton("Standardtext");
		btnStandardtext.setMnemonic('t');
		btnStandardtext.setToolTipText("Standardtext einfügen");
		this.btnStandardtext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				standardtextEinbauen();
			}
		});
		
		this.panelInfos = new JPanel();
		contentPane.add(panelInfos, "cell 3 9 1 12,grow");
		this.panelInfos.setLayout(new MigLayout("", "[10:n:10,grow][grow]", "[][][][][][top]"));
		
		this.txtrErstellt = new JTextArea();
		this.txtrErstellt.setText("Erstellt:");
		this.txtrErstellt.setFocusable(false);
		this.txtrErstellt.setEditable(false);
		this.txtrErstellt.setBackground(UIManager.getColor("Button.background"));
		this.panelInfos.add(this.txtrErstellt, "cell 0 0 2 1,grow");
		
		this.txtrErstelltDatum = new JTextArea();
		this.txtrErstelltDatum.setText("-");
		this.txtrErstelltDatum.setFocusable(false);
		this.txtrErstelltDatum.setEditable(false);
		this.txtrErstelltDatum.setBackground(UIManager.getColor("Button.background"));
		this.panelInfos.add(this.txtrErstelltDatum, "cell 1 1,grow");
		
		this.txtrAnzahlWrter = new JTextArea();
		this.txtrAnzahlWrter.setText("Anzahl Wörter:");
		this.txtrAnzahlWrter.setFocusable(false);
		this.txtrAnzahlWrter.setEditable(false);
		this.txtrAnzahlWrter.setBackground(UIManager.getColor("Button.background"));
		this.panelInfos.add(this.txtrAnzahlWrter, "cell 0 2 2 1,grow");
		
		this.txtrWoerterZahl = new JTextArea();
		this.txtrWoerterZahl.setText("-");
		this.txtrWoerterZahl.setFocusable(false);
		this.txtrWoerterZahl.setEditable(false);
		this.txtrWoerterZahl.setBackground(UIManager.getColor("Button.background"));
		this.panelInfos.add(this.txtrWoerterZahl, "cell 1 3,grow");
		
		txtrAnzahlZeichen = new JTextArea();
		txtrAnzahlZeichen.setText("Anzahl Zeichen:");
		txtrAnzahlZeichen.setFocusable(false);
		txtrAnzahlZeichen.setEditable(false);
		txtrAnzahlZeichen.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrAnzahlZeichen, "cell 0 4 2 1,grow");
		
		txtrZeichenZahl = new JTextArea();
		txtrZeichenZahl.setText("-");
		txtrZeichenZahl.setFocusable(false);
		txtrZeichenZahl.setEditable(false);
		txtrZeichenZahl.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrZeichenZahl, "cell 1 5,grow");
		this.contentPane.add(this.btnStandardtext, "flowy,cell 0 10");
		
		this.btnZitat = new JButton("Zitat");
		btnZitat.setMnemonic('z');
		this.btnZitat.setToolTipText("[QUOTE][/QUOTE] einfügen");
		this.btnZitat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bbQuote();
			}
		});
		
		this.btnListenpunkt = new JButton("Listenpunkt");
		btnListenpunkt.setMnemonic('p');
		this.btnListenpunkt.setToolTipText("[*] einfügen");
		this.btnListenpunkt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bbStern();
			}
		});
		
		this.btnListe = new JButton("Liste");
		btnListe.setMnemonic('i');
		this.btnListe.setToolTipText("[LIST][/LIST] einfügen");
		this.btnListe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bbList();
			}
		});
		
		this.btnBild = new JButton("Bild");
		btnBild.setMnemonic('b');
		this.btnBild.setToolTipText("[IMG][/IMG] einfügen");
		this.btnBild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bbImg();
			}
		});
		
		this.btnLink = new JButton("Link");
		btnLink.setMnemonic('l');
		this.btnLink.setToolTipText("[URL][/URL] einfügen");
		this.btnLink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bbUrl();
			}
		});
		this.contentPane.add(this.btnLink, "cell 0 11");
		this.contentPane.add(this.btnBild, "cell 0 12");
		this.contentPane.add(this.btnListe, "cell 0 13");
		this.contentPane.add(this.btnListenpunkt, "cell 0 14");
		
		panelFormatierungen = new JPanel();
//		contentPane.add(panelFormatierungen, "cell 3 9 1 12,growy");
		panelFormatierungen.setLayout(new MigLayout("", "[129px,grow]", "[64px,grow]"));
		
		lblPostformatierungen = new JLabel("Postformatierungen:");
		panelFormatierungen.add(lblPostformatierungen, "flowy,cell 0 0,growx");
				
		listFormatierungen = new List();
		listFormatierungen.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				formatEinfuegen(e);
			}
		});
		panelFormatierungen.add(listFormatierungen, "cell 0 0,grow");
		listFormatierungen.add("Neue Zeile");
		listFormatierungen.add("Header");
		listFormatierungen.add("Bereichstart");
		listFormatierungen.add("Bereichende");
		listFormatierungen.add("Bereichname");
		listFormatierungen.add("Themenstart");
		listFormatierungen.add("Themenende");
		listFormatierungen.add("Themenname");
		listFormatierungen.add("Thementext");
		listFormatierungen.add("Kurzn.-Ber.-Start");
		listFormatierungen.add("Kurzn.-Ber.-Ende");
		listFormatierungen.add("Kurzn.-Überschr.");
		listFormatierungen.add("Kurzn.-Start");
		listFormatierungen.add("Kurzn.-Ende");
		listFormatierungen.add("Kurzn.-Text");
		listFormatierungen.add("Größe Bereichüber.");
		listFormatierungen.add("Größe Themenüber.");
		listFormatierungen.add("Größe Text");
		listFormatierungen.add("Größe Kurzn.-Über.");
		listFormatierungen.add("Größe Kurzn.-Text");
		listFormatierungen.add("Footer");
		listFormatierungen.add("Kommentar-Start");
		listFormatierungen.add("Kommentar-Ende");
		listFormatierungen.add("SY-Datum");
		listFormatierungen.add("RL-Datum");
		this.contentPane.add(this.btnZitat, "cell 0 15");
		
		this.lblFormatierungen = new JLabel("Formatierungen:");
		this.contentPane.add(this.lblFormatierungen, "cell 0 3");
		
		
		this.btnKursiv = new JButton("Kursiv");
		btnKursiv.setMnemonic('k');
		this.btnKursiv.setToolTipText("[I][/I] einfügen");
		this.btnKursiv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bbI();
			}
		});
		this.contentPane.add(this.btnKursiv, "cell 0 5");
		
		this.btnFett = new JButton("Fett");
		btnFett.setMnemonic('f');
		this.btnFett.setToolTipText("[B][/B] einfügen");
		this.btnFett.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bbB();
			}
		});
		this.contentPane.add(this.btnFett, "cell 0 4,growx");
		
		this.btnSpeichern = new JButton("Speichern");
		btnSpeichern.setMnemonic('s');
		this.btnSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				speichern(true);
			}
		});
		
		btnOfftopic = new JButton("Off-Topic");
		btnOfftopic.setMnemonic('o');
		btnOfftopic.setToolTipText("Off-Topic-Bereich einfügen");
		btnOfftopic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				offTopic();
			}
		});
		contentPane.add(btnOfftopic, "cell 0 16");
		
		btnVariable = new JButton("Variable");
		btnVariable.setMnemonic('v');
		btnVariable.setToolTipText("Variable einfügen, die im Post einfach ersetzt werden kann");
		btnVariable.setEnabled(false);
		contentPane.add(btnVariable, "cell 0 17");
		this.contentPane.add(this.btnSpeichern, "cell 0 19,growx");
		
		this.btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.setMnemonic('a');
		this.btnAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abbrechen();
			}
		});
		this.contentPane.add(this.btnAbbrechen, "cell 0 20,growx");
		
		comboBoxWichtigkeit = new JComboBox<String>();
		comboBoxWichtigkeit.setMaximumRowCount(11);
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
		contentPane.add(comboBoxWichtigkeit, "cell 2 0 2 1,growx");
		
		// Aktionen beim Schliessen vom Fenster
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				abbrechen();
			}
		});
		
		// Woerterzaehlmaschine
		this.textAreaThema.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent arg0) {
				woerterZaehlen();
				txtrZeichenZahl.setText(String.valueOf(textAreaThema.getText().length()));
			}
		});
		
		setVisible(true);
	}

	////////////////////////////////////
	// Methoden Formatierungen
	////////////////////////////////////
	
	private void bbB(){
		int pos1 = textAreaThema.getSelectionStart();
		int pos2 = textAreaThema.getSelectionEnd();
		textAreaThema.insert("[B]", pos1);
		textAreaThema.insert("[/B]", pos2+3);
//		StringBuilder sb = new StringBuilder(textAreaThema.getText());
//		sb.insert(pos1, "[B]");
//		sb.insert(pos2+3, "[/B]");
//		textAreaThema.setText(sb.toString());
		textAreaThema.requestFocus();
		if(pos1 == pos2){
			textAreaThema.setCaretPosition(pos1+3);
		}
		else{
			textAreaThema.setCaretPosition(pos2+7);
		}
	}
	
	private void bbI(){
		int pos1 = textAreaThema.getSelectionStart();
		int pos2 = textAreaThema.getSelectionEnd();
		textAreaThema.insert("[I]", pos1);
		textAreaThema.insert("[/I]", pos2+3);
		textAreaThema.requestFocus();
		if(pos1 == pos2){
			textAreaThema.setCaretPosition(pos1+3);
		}
		else{
			textAreaThema.setCaretPosition(pos2+7);
		}
	}
	
	private void bbU(){
		int pos1 = textAreaThema.getSelectionStart();
		int pos2 = textAreaThema.getSelectionEnd();
		textAreaThema.insert("[U]", pos1);
		textAreaThema.insert("[/U]", pos2+3);
		textAreaThema.requestFocus();
		if(pos1 == pos2){
			textAreaThema.setCaretPosition(pos1+3);
		}
		else{
			textAreaThema.setCaretPosition(pos2+7);
		}
	}
	
	private void bbSize(){
		// Infos sammeln
		int pos1 = textAreaThema.getSelectionStart();
		int pos2 = textAreaThema.getSelectionEnd();
		Object[] a = {1,2,3,4,5,6,7};
		int b = einst.getGnormal();
		Object sizeO = null;
		
		// Groesse abfragen
		sizeO = JOptionPane.showInputDialog(null, "Bitte Größe Festlegen:",
				"Größe ändern", JOptionPane.PLAIN_MESSAGE, null, a, a[b-1]);
		
		// Ausfuehrung, falls sizeO nicht leer ist
		if(sizeO != null){
			// Groesse einbauen
			String sizeS = sizeO.toString();
			textAreaThema.insert("[SIZE=" + sizeS + "][/SIZE]", pos1);
			textAreaThema.requestFocus();
			if(pos1 == pos2){
				textAreaThema.setCaretPosition(pos1+8);
			}
			else{
				textAreaThema.setCaretPosition(pos2+15);
			}
		} else {
			textAreaThema.requestFocus();
			textAreaThema.setCaretPosition(pos1);
		}
	}
	
	private void bbIndent(){
		int pos1 = textAreaThema.getSelectionStart();
		int pos2 = textAreaThema.getSelectionEnd();
		int nlL = Einst.nl.length();
		textAreaThema.insert(Einst.nl + "[INDENT]" + Einst.nl, pos1);
		textAreaThema.insert(Einst.nl + "[/INDENT]" + Einst.nl, pos2+8+2*nlL);
		textAreaThema.requestFocus();
		if(pos1 == pos2){
			textAreaThema.setCaretPosition(pos1+8+2*nlL);
		}
		else{
			textAreaThema.setCaretPosition(pos2+17+4*nlL);
		}
	}
	
	////////////////////////////////////
	// Methoden Einfuegen
	////////////////////////////////////
	
	private void standardtextEinbauen(){
		// Initialiasierung Standardtext und Holen der Cursorposition
		int pos1 = textAreaThema.getCaretPosition();
		
		// Auswahlfenster
		StandardtextEinfuegen se = new StandardtextEinfuegen(einst);
		
		// Text holen
		Standardtext stand = se.verwenden();
		
		// Text einbauen, wenn verfuegbar
		if(stand != null){
			// Pruefen, ob Variablen
			if(stand.getInhalt().contains(Einst.vvarStart) &&
					stand.getInhalt().contains(Einst.vvarEnde)){
				// Alten text aus Textfeld holen
				String alterText = textAreaThema.getText();
				
				// Vorschau für neuen Gesamttext einbauen
				textAreaThema.append(stand.getInhalt());
				
				// Inhalt in StringBuilder werfen
				StringBuilder sb = new StringBuilder(65532);
				
				// Text vor erster Variable in sb einbauen
				String[] aufgeteilt = stand.getInhalt().split("\\" + Einst.vvarStart);
				String textVorVar = aufgeteilt[0];
				sb.append(textVorVar);
				
				// Variablen in Schleife abarbeiten
				for(int i = 1; i < aufgeteilt.length; i++){
					String text = aufgeteilt[i];
					
					// Pruefe, wann Variablenamen zuende
					int j = -1;
					do{
						j++;
					}while(!text.substring(j, j+6).equals("\\" + Einst.vvarEnde));
					String variableName = text.substring(0, j);
					
					// Inhalt der Variable abfragen
					String inhalt = JOptionPane.showInputDialog(null, "Text für Variable \"" + variableName +
							"\" eingeben:", "Variable füllen", JOptionPane.QUESTION_MESSAGE);
					
					// Inhalt in sb einfuegen
					sb.append(inhalt);
					
					// Restlichen Text ohne Variablenende einfuegen
					sb.append(text.substring(j+Einst.vvarEnde.length()));
					
					// Abbruch
					if(inhalt == null){
						i = aufgeteilt.length-1;
						sb = new StringBuilder();
					}
				}
				
				// sb in textArea einfuegen
				String einfuegen = sb.toString();
				textAreaThema.setText(alterText);
				textAreaThema.insert(einfuegen, pos1);
				textAreaThema.requestFocus();
				textAreaThema.setCaretPosition(pos1 + einfuegen.length());
				
			}
			else{
				// Wenn keine Variablen
				textAreaThema.insert(Helfer.varText(stand.getInhalt()), pos1);
				textAreaThema.requestFocus();
				textAreaThema.setCaretPosition(pos1 + Helfer.varText(stand.getInhalt()).length());
			}
			
		}
	}
	
	private void bbUrl(){
		// Infos sammeln
		int pos1 = textAreaThema.getSelectionStart();
		int pos2 = textAreaThema.getSelectionEnd();
		Object urlO = null;
		
		// URL abfragen
		urlO = JOptionPane.showInputDialog(null, "Bitte URL einfügen:", "URL einfügen",
				JOptionPane.PLAIN_MESSAGE);
		
		// Ausfuehrung, falls urlO nicht leer ist
		if(urlO != null){
			// Groesse einbauen
			String urlS = urlO.toString();
			int urlL = urlS.length();
			textAreaThema.insert("[URL=\"" + urlS + "\"]", pos1);
			textAreaThema.insert("[/URL]", pos2+8+urlL);
			textAreaThema.requestFocus();
			if(pos1 == pos2){
				textAreaThema.setCaretPosition(pos2+8+urlL);
			}
			else{
				textAreaThema.setCaretPosition(pos2+8+6+urlL);
			}
		} else {
			textAreaThema.requestFocus();
			textAreaThema.setCaretPosition(pos1);
		}
	}
	
	private void bbImg(){
		int pos1 = textAreaThema.getSelectionStart();
		int pos2 = textAreaThema.getSelectionEnd();
		textAreaThema.insert("[IMG]", pos1);
		textAreaThema.insert("[/IMG]", pos2+5);
		textAreaThema.requestFocus();
		if(pos1 == pos2){
			textAreaThema.setCaretPosition(pos1+5);
		}
		else{
			textAreaThema.setCaretPosition(pos2+11);
		}
	}
	
	private void bbList(){
		int pos1 = textAreaThema.getCaretPosition();
		textAreaThema.insert("[LIST]" + Einst.nl + "[*]" + Einst.nl + "[/LIST]", pos1);
		textAreaThema.requestFocus();
		textAreaThema.setCaretPosition(pos1+10);
	}
	
	private void bbStern(){
		int pos1 = textAreaThema.getCaretPosition();
		textAreaThema.insert("[*]", pos1);
		textAreaThema.requestFocus();
		textAreaThema.setCaretPosition(pos1+3);
	}
	
	private void bbQuote(){
		// Infos sammeln
		int pos1 = textAreaThema.getCaretPosition();
		Object quoteO = null;
		
		// Sprecher abfragen
		quoteO = JOptionPane.showInputDialog(null, "Zitat von:", "Zitat einfügen",
				JOptionPane.PLAIN_MESSAGE);
		
		// Ausfuehrung, falls quoteO nicht leer ist
		if(quoteO != null){
			// Groesse einbauen
			String quoteS = quoteO.toString();
			int quoteL = quoteS.length();
			textAreaThema.insert("[QUOTE="+ quoteS + "]" + Einst.nl + Einst.nl + "[/QUOTE]", pos1);
			textAreaThema.requestFocus();
			textAreaThema.setCaretPosition(pos1+9+quoteL);
		} else {
			textAreaThema.requestFocus();
			textAreaThema.setCaretPosition(pos1);
		}
	}
	
	private void offTopic(){
		// Infos sammeln
		int pos1 = textAreaThema.getCaretPosition();
		Object textO = null;
		
		// Sprecher abfragen
		textO = JOptionPane.showInputDialog(null, "Off-Topc-Information:", "Off Topic",
				JOptionPane.PLAIN_MESSAGE);
		
		// Ausfuehrung, falls quoteO nicht leer ist
		if(textO != null){
			// Groesse einbauen
			String textS = textO.toString();
			textAreaThema.insert("[SIZE=1](Off: " + textS + ")[/SIZE]", pos1);
			textAreaThema.requestFocus();
			textAreaThema.setCaretPosition(pos1 + "[SIZE=1](Off: ".length());
		} else {
			textAreaThema.requestFocus();
			textAreaThema.setCaretPosition(pos1);
		}
	}
	
	// Formatierungen
	protected void formatEinfuegen(ItemEvent e){
		// Aktuelle Cursorposition suchen
		int pos = textAreaThema.getCaretPosition();

		// Textcodes in de.g77.sy.postwriter.Einstellungen
//		listFormatierungen.add("Neue Zeile");
//		listFormatierungen.add("Header");
//		listFormatierungen.add("Bereichstart");
//		listFormatierungen.add("Bereichende");
//		listFormatierungen.add("Bereichname");
//		listFormatierungen.add("Themenstart");
//		listFormatierungen.add("Themenende");
//		listFormatierungen.add("Themenname");
//		listFormatierungen.add("Thementext");
//		listFormatierungen.add("Kurzn.-Ber.-Start");
//		listFormatierungen.add("Kurzn.-Ber.-Ende");
//		listFormatierungen.add("Kurzn.-Überschr.");
//		listFormatierungen.add("Kurznachrichtenstart");
//		listFormatierungen.add("Kurznachrichtenende");
//		listFormatierungen.add("Kurznachrichtentext");
//		listFormatierungen.add("Größe Bereichüberschr.");
//		listFormatierungen.add("Größe Themenüberschr.");
//		listFormatierungen.add("Größe Text");
//		listFormatierungen.add("Größe Kurzn.-Überschr.");
//		listFormatierungen.add("Größe Kurzn.-Text");
//		listFormatierungen.add("Footer");
//		listFormatierungen.add("SY-Datum");
//		listFormatierungen.add("RL-Datum");
		
		// Auswahl, was eingefuegt werden soll
		String insert;
		switch (Integer.parseInt(e.getItem().toString())) {
		case 0:
			// Neue Zeile
			insert = Einst.vnl + Einst.nl;
			break;
		case 1:
			// Header
			insert = Einst.vheader;
			break;
		case 2:
			// Bereichstart
			insert = Einst.vbereichStart;
			break;
		case 3:
			// Bereichende
			insert = Einst.vbereichEnde;
			break;
		case 4:
			// Bereichname
			insert = Einst.vbereichname;
			break;
		case 5:
			// Themenstart
			insert = Einst.vthemaStart;
			break;
		case 6:
			// Themenende
			insert = Einst.vthemaEnde;
			break;
		case 7:
			// Themenname
			insert = Einst.vthemenname;
			break;
		case 8:
			// Thementext
			insert = Einst.vthementext;
			break;
		case 9:
			// Kurznachrichtenbereichstart
			insert = Einst.vkurzBereichStart;
			break;
		case 10:
			// Kurznachrichtenbereichende
			insert = Einst.vkurzBereichEnde;
			break;
		case 11:
			// Kurznachrichtenüberschrift
			insert = Einst.vkurzUeber;
			break;
		case 12:
			// Kurznachrichtenstart
			insert = Einst.vkurzStart;
			break;
		case 13:
			// Kurznachrichtenende
			insert = Einst.vkurzEnde;
			break;
		case 14:
			// Kurznachrichtentext
			insert = Einst.vkurzText;
			break;
		case 15:
			// Groeße Bereich
			insert = Einst.vbUeberGroesse;
			break;
		case 16:
			// Groeße Thema
			insert = Einst.vtUeberGroesse;
			break;
		case 17:
			// Groeße Text
			insert = Einst.vtTextGroesse;
			break;
		case 18:
			// Groeße Kurzn. Ueber
			insert = Einst.vkUeberGroesse;
			break;
		case 19:
			// Groeße Kurzn. Text
			insert = Einst.vkurzTextGroesse;
			break;
		case 20:
			// Footer
			insert = Einst.vfooter;
			break;
		case 21:
			// Kommentarstart
			insert = Einst.vkommentarStart;
			break;
		case 22:
			// Kommentarende
			insert = Einst.vkommentarEnde;
			break;
		case 23:
			// SY-Datum
			insert = Einst.vdatumSY;
			break;
		case 24:
			// RL-Datum
			insert = Einst.vdatumRL;
			break;
		default:
			insert = "";
			break;
		}
		
		// Einfuegen
		textAreaThema.insert(insert, pos);
		textAreaThema.requestFocus();
		textAreaThema.setCaretPosition(pos+insert.length());
	}
	
	////////////////////////////////////
	// Methoden Infos
	////////////////////////////////////
	
	private void iPoli(){
		einst.oeffnePoli();
	}
	
	private void iDisku(){
		einst.oeffneDisku();
	}
	
	private void iWiki(){
		einst.oeffneWiki();
	}
	
	private void datum(){
		new FrameDRThemen(einst, this);
	}
	
	public void gebeDatum(FrameDRThemen fd) {
		int pos1 = textAreaThema.getCaretPosition();
		
		String datum = fd.getDatum();
		
		// Text einbauen, wenn verfuegbar
		if(datum != null){
			textAreaThema.insert(datum, pos1);
			
			textAreaThema.requestFocus();
			textAreaThema.setCaretPosition(pos1 + datum.length());
		}
	}
	
	private void woerterZaehlen(){
		txtrWoerterZahl.setText(Helfer.zahl(textAreaThema));
	}
	
	////////////////////////////////////
	// Methoden Buttons unten
	////////////////////////////////////
	
	protected void speichern(boolean schließen){
		speichern();
		// Speicherbutton zum Ueberschreiben in Unterklassen
	}
	
	protected void speichern(){
		// Speicherbutton zum Ueberschreiben in Unterklassen
	}
	
	// Editor beenden
	protected void abbrechen(){
		einst.out("Beende Themeneditor.");
		verw.einlesen();
		dispose();
		verw.setVisible(true);
	}
	
	////////////////////////////////////
	// Aufbau Themeneditor
	////////////////////////////////////
	/*
	 * ThemenEditor.java
	 * 	PostsThemaEditor.java
	 * 		ThemaBearbeiten.java
	 * 			NeuesThema.java
	 * 			BearbeiteThema.java
	 * 		KurznachrichtBearbeiten.java
	 * 			NeueKurznachricht.java
	 * 			BearbeiteKurznachricht.java
	 * 	EinstellungenThemenEditor.java
	 * 		HeaderBearbeiten.java
	 * 			NeuerHeader.java
	 * 			BearbeiteHeader.java
	 * 		FooterBearbeiten.java
	 * 			NeuerFooter.java
	 * 			BearbeiteFooter.java
	 * 		StandardtextBearbeiten.java
	 * 			NeuerStandardtext.java
	 * 			BearbeiteStandardtext.java
	 * 		ThemenvorlageBearbeiten.java
	 * 			NeueThemenvorlage.java
	 * 			BearbeiteThemenvorlage.java
	 * 		Postformatierung.java
	 */
	
}