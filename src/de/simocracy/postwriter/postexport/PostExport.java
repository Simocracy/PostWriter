package de.simocracy.postwriter.postexport;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.datumsrechner.DatumRechner;
import de.simocracy.postwriter.fachklassen.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JCheckBox;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PostExport extends Fenster {

	private static final long serialVersionUID = 655184367888019171L;
	private JPanel contentPane;
	protected Einst einst;
	protected Hauptmenue hm;
	
	protected ArrayList<Post> alPost;
	protected ArrayList<Bereich> alBereich;
	protected ArrayList<Thema> alThemen;
	protected ArrayList<Kurznachricht> alKurz;
	protected ArrayList<Header> alHeader;
	protected ArrayList<Footer> alFooter;
	
	protected Post post;
	protected Bereich bereich;
	protected Thema thema;
	protected Kurznachricht kurznachricht;
	protected Header header;
	protected Footer footer;

	private JLabel lblPost;
	private JLabel lblHeader;
	private JLabel lblFooter;
	private JLabel lblInhalt;
	private JComboBox<String> comboBoxPost;
	private JComboBox<String> comboBoxHeader;
	private JComboBox<String> comboBoxFooter;
	private JScrollPane scrollPane;
	private JPanel panelInfos;
	private JLabel lblInformationen;
	private JButton btnPosten;
	private JButton btnAlsTxt;
	private JButton btnSchlieen;
	private JTextArea txtrPostinhalt;
	private JTextArea txtrBereiche;
	private JTextArea txtrAnzahlbereiche;
	private JTextArea txtrThemen;
	private JTextArea txtrAnzahlthemen;
	private JTextArea txtrKurzn;
	private JTextArea txtrAnzahlkurzn;
	private JTextArea txtrPostdatum;
	private JTextArea txtrDatum;
	private JTextArea txtrAnzahlWrter;
	private JTextArea txtrWrter;
	private JLabel lblOptionen;
	private JCheckBox chckbxAutoumbruch;
	private JButton btnClicktopost;
	private JCheckBox chckbxTopAnf;
	private JTextArea txtrAnzahlZeichen;
	private JTextArea txtrZeichen;
	private JButton btnBaumansicht;
	private JButton btnNopaste;
	private JButton btnPostOeffnen;

	public PostExport(Einst einst, Hauptmenue hm) {
		super(700,730);
		this.einst = einst;
		this.hm = hm;
		
		setTitle("PostWriter - Postexporter");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[120:120:120,grow,fill][grow]", "[][][][][][][][][][grow][][][][][][][]"));
		
		lblPost = new JLabel("Post:");
		contentPane.add(lblPost, "cell 0 0");
		
		comboBoxPost = new JComboBox<String>();
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
		contentPane.add(comboBoxPost, "cell 1 0,growx");
		
		lblHeader = new JLabel("Header:");
		contentPane.add(lblHeader, "cell 0 1");
		
		comboBoxHeader = new JComboBox<String>();
		comboBoxHeader.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				int min = 0;
				int max = comboBoxHeader.getItemCount();
				int aktuell = comboBoxHeader.getSelectedIndex();
				int neu = aktuell + e.getWheelRotation();
				
				if(min <= neu && neu < max){
					comboBoxHeader.setSelectedIndex(neu);
				}
			}
		});
		contentPane.add(comboBoxHeader, "cell 1 1,growx");
		
		lblFooter = new JLabel("Footer:");
		contentPane.add(lblFooter, "cell 0 2");
		
		comboBoxFooter = new JComboBox<String>();
		comboBoxFooter.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				int min = 0;
				int max = comboBoxFooter.getItemCount();
				int aktuell = comboBoxFooter.getSelectedIndex();
				int neu = aktuell + e.getWheelRotation();
				
				if(min <= neu && neu < max){
					comboBoxFooter.setSelectedIndex(neu);
				}
			}
		});
		contentPane.add(comboBoxFooter, "cell 1 2,growx");
		
		lblInhalt = new JLabel("Vorschau:");
		contentPane.add(lblInhalt, "cell 0 3");
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 1 3 1 14,grow");
		
		txtrPostinhalt = new JTextArea();
		txtrPostinhalt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				txtrWrter.setText(Helfer.zahl(txtrPostinhalt));
				txtrZeichen.setText(String.valueOf(txtrPostinhalt.getText().length()));
			}
		});
		txtrPostinhalt.setWrapStyleWord(true);
		txtrPostinhalt.setLineWrap(true);
		scrollPane.setViewportView(txtrPostinhalt);
		
		lblInformationen = new JLabel("Informationen:");
		contentPane.add(lblInformationen, "cell 0 4");
		
		panelInfos = new JPanel();
		contentPane.add(panelInfos, "cell 0 5,aligny top");
		panelInfos.setLayout(new MigLayout("", "[10:10:10,grow][grow][grow]", "[][][][][][][][grow][grow]"));
		
		txtrBereiche = new JTextArea();
		txtrBereiche.setFocusable(false);
		txtrBereiche.setBackground(UIManager.getColor("Panel.background"));
		txtrBereiche.setEditable(false);
		txtrBereiche.setText("Bereiche:");
		panelInfos.add(txtrBereiche, "cell 0 0 2 1");
		
		txtrAnzahlbereiche = new JTextArea();
		txtrAnzahlbereiche.setBackground(UIManager.getColor("Panel.background"));
		txtrAnzahlbereiche.setEditable(false);
		txtrAnzahlbereiche.setFocusable(false);
		panelInfos.add(txtrAnzahlbereiche, "cell 2 0");
		
		txtrThemen = new JTextArea();
		txtrThemen.setEditable(false);
		txtrThemen.setFocusable(false);
		txtrThemen.setBackground(UIManager.getColor("Panel.background"));
		txtrThemen.setText("Themen:");
		panelInfos.add(txtrThemen, "cell 0 1 2 1");
		
		txtrAnzahlthemen = new JTextArea();
		txtrAnzahlthemen.setFocusable(false);
		txtrAnzahlthemen.setEditable(false);
		txtrAnzahlthemen.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrAnzahlthemen, "cell 2 1");
		
		txtrKurzn = new JTextArea();
		txtrKurzn.setEditable(false);
		txtrKurzn.setFocusable(false);
		txtrKurzn.setBackground(UIManager.getColor("Panel.background"));
		txtrKurzn.setText("Kurzn.:");
		panelInfos.add(txtrKurzn, "cell 0 2 2 1");
		
		txtrAnzahlkurzn = new JTextArea();
		txtrAnzahlkurzn.setFocusable(false);
		txtrAnzahlkurzn.setEditable(false);
		txtrAnzahlkurzn.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrAnzahlkurzn, "cell 2 2");
		
		txtrPostdatum = new JTextArea();
		txtrPostdatum.setText("Postdatum:");
		txtrPostdatum.setFocusable(false);
		txtrPostdatum.setEditable(false);
		txtrPostdatum.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrPostdatum, "cell 0 3 3 1");
		
		txtrDatum = new JTextArea();
		txtrDatum.setFocusable(false);
		txtrDatum.setEditable(false);
		txtrDatum.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrDatum, "cell 1 4 2 1");
		
		txtrAnzahlWrter = new JTextArea();
		txtrAnzahlWrter.setToolTipText("");
		txtrAnzahlWrter.setEditable(false);
		txtrAnzahlWrter.setFocusable(false);
		txtrAnzahlWrter.setBackground(UIManager.getColor("Panel.background"));
		txtrAnzahlWrter.setText("Anzahl Wörter:");
		panelInfos.add(txtrAnzahlWrter, "cell 0 5 3 1");
		
		txtrWrter = new JTextArea();
		txtrWrter.setToolTipText("Anzahl der Wörter des Posts (ungefährer Wert)");
		txtrWrter.setFocusable(false);
		txtrWrter.setEditable(false);
		txtrWrter.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrWrter, "cell 1 6 2 1");
		
		txtrAnzahlZeichen = new JTextArea();
		txtrAnzahlZeichen.setToolTipText("");
		txtrAnzahlZeichen.setText("Anzahl Zeichen:");
		txtrAnzahlZeichen.setFocusable(false);
		txtrAnzahlZeichen.setEditable(false);
		txtrAnzahlZeichen.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrAnzahlZeichen, "cell 0 7 3 1,grow");
		
		txtrZeichen = new JTextArea();
		txtrZeichen.setToolTipText("");
		txtrZeichen.setFocusable(false);
		txtrZeichen.setEditable(false);
		txtrZeichen.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrZeichen, "cell 1 8 2 1,grow");
		
		// Buttons mit ActionListener
		btnPosten = new JButton("Posten");
		btnPosten.setMnemonic('p');
		btnPosten.setToolTipText("<html>Kopiert den Post wie in der Vorschau in die Zwischenablage<br>" +
				"und öffnet das SimForum zum antworten im Politikthread.</html>");
		btnPosten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				posten();
			}
		});
		
		lblOptionen = new JLabel("Optionen:");
		contentPane.add(lblOptionen, "cell 0 6");
		
		chckbxAutoumbruch = new JCheckBox("Autoumbruch");
		chckbxAutoumbruch.setMnemonic('a');
		contentPane.add(chckbxAutoumbruch, "cell 0 7");
		
		// Checkbox Anfuehrungszeichen einstellen
		chckbxTopAnf = new JCheckBox("Top. Anf.");
		chckbxTopAnf.setMnemonic('o');
		contentPane.add(chckbxTopAnf, "cell 0 8");
		chckbxTopAnf.setToolTipText("Topografische Anführungszeichen");
		chckbxTopAnf.setSelected(einst.isTopo());
		
		btnClicktopost = new JButton("Click-to-Post");
		btnClicktopost.setMnemonic('c');
		btnClicktopost.setToolTipText("Postet den Post mit einem Mausklick im Politikthread");
		btnClicktopost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctp();
			}
		});
		
		btnPostOeffnen = new JButton("Post öffnen");
		btnPostOeffnen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				postOeffnen();
			}
		});
		btnPostOeffnen.setToolTipText("Postet den Post mit einem Mausklick im Politikthread");
		btnPostOeffnen.setMnemonic('ö');
		contentPane.add(btnPostOeffnen, "cell 0 10");
		contentPane.add(btnClicktopost, "cell 0 11");
		contentPane.add(btnPosten, "cell 0 12");
		
		btnAlsTxt = new JButton("In TXT");
		btnAlsTxt.setMnemonic('t');
		btnAlsTxt.setToolTipText("Post in Textdatei speichern");
		btnAlsTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txt();
			}
		});
		contentPane.add(btnAlsTxt, "cell 0 13");
		
		btnSchlieen = new JButton("Schließen");
		btnSchlieen.setMnemonic('s');
		btnSchlieen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				beenden();
			}
		});
		
		btnBaumansicht = new JButton("Baumansicht");
		btnBaumansicht.setMnemonic('b');
		btnBaumansicht.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				baumansicht();
			}
		});
		
		btnNopaste = new JButton("Nopaste");
		btnNopaste.setMnemonic('n');
		btnNopaste.setToolTipText("<html>Kopiert den aktuellen Post in die Zwischenablage<br>" +
				"und öffnet den in den Einstellungen eingestellte Nopaste-Dienst</html>");
		btnNopaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				nopaste();
			}
		});
		contentPane.add(btnNopaste, "cell 0 14");
		contentPane.add(btnBaumansicht, "cell 0 15");
		contentPane.add(btnSchlieen, "cell 0 16");

		// WindowListener zum Schliessen
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				beenden();
			}
		});
		
		// Checkbox Autoumbruch deaktivieren, falls keine Umbrueche in Einstellungen
		if(einst.getAutoumbruch() == 0){
			chckbxAutoumbruch.setEnabled(false);
			chckbxAutoumbruch.setSelected(false);
		}else{
			chckbxAutoumbruch.setSelected(true);
		}
		
		// Einlesen aus DB
		einlesen();
		
		// ItemListener der ComboBoxen
		comboBoxPost.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				auswahl();
			}
		});
		
		comboBoxHeader.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				auswahl();
			}
		});
		
		comboBoxFooter.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				auswahl();
			}
		});
		
		// ItemListener der Checkboxen
		chckbxAutoumbruch.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chckbxAutoumbruch.isSelected()){
					// Wenn markiert, Autoumbrueche einbauen
					umbrueche();
				}else{
					// Wenn nicht markiert, Post ohne Umbrueche neu zusammensetzen
					auswahl();
				}
				
				txtrPostinhalt.setCaretPosition(0);
			}
		});
		
		chckbxTopAnf.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				topo();
				txtrPostinhalt.setCaretPosition(0);
			}
		});
		
		setVisible(true);
	}
	
	// Post öffnen
	private void postOeffnen() {
		einst.oeffneLink(post.getUrl());
	}
	
	// In Poli posten
	private void posten(){
		einst.out("Post wird klassisch gepostet.");
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
				new StringSelection(getVorschau()), null);
		einst.oeffneLink(Einst.postenLink);
		new Dateneingabe(einst, hm, post, this);
	}
	
	// Auf Nopaste posten
	private void nopaste() {
		einst.out("Post wird auf Nopaste-Seite gepostet.");
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
				new StringSelection(getVorschau()), null);
		einst.oeffneLink(einst.getNopasteSeite());
	}
	
	// Click-to-Post funktion
	private void ctp(){
		einst.out("Post wird mit CtP gepostet.");
		int postlaengeMin = 150;
		if(getVorschau().length() < postlaengeMin) {
			JOptionPane.showMessageDialog(null, "Der zu postende Post ist zu Kurz." + Einst.nl +
					"Um den Post mit Click-to-Post posten zu können, muss er mindestens " + postlaengeMin +
					" Zeichen lang sein.", "Post zu kurz", JOptionPane.INFORMATION_MESSAGE);
			einst.out("Post zu kurz. Konnte nicht gepostet werden.");
		}else {
			final PostExport pe = this;
			// CtP in eigenem Thread ablaufen lassen
			new Thread(new Runnable() {
				@Override
				public void run() {
					pe.setVisible(false);
					new CtP(einst).posten(pe, post);
					pe.requestFocus(false);
					pe.setVisible(true);
				}
			}).start();
		}
	}
	
	// In TXT speichern
	private void txt(){
		einst.out("Exportiere Post in Textdatei.");
		
		// FileChooser erzeugen und starten
		JFileChooser fc = new JFileChooser(einst.getVerzeichnis());
		fc.setMultiSelectionEnabled(false);
		
		fc.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return "Textdateien (*.txt)";
			}
			
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
			}
		});
		
		// Rueckgaben
		int result = fc.showSaveDialog(null);
		
		if(result == JFileChooser.APPROVE_OPTION){
			// Datei holen
			File temp = fc.getSelectedFile();
			
			// Ausgabedatei initialisieren
			File ausgabe;

			// Dateiendung pruefen
			if(temp.getAbsolutePath().toString().endsWith(".txt")){
				// Wenn .txt dran ist
				ausgabe = temp;
			}else{
				// Wenn .txt nicht dran ist
				String dateiname = temp.getAbsolutePath().toString() + ".txt";
				ausgabe = new File(dateiname);
			}
			
			// Pruefen, ob Datei bereits existiert
			int schreiben = JOptionPane.YES_OPTION;
			if(ausgabe.exists()){
				// Wenn ja, Sicherheitsabfrage
				schreiben = JOptionPane.showConfirmDialog(null, "Die gewählte Datei existiert bereits." +
						Einst.nl + "Soll sie überschrieben werden?", "Datei existiert bereits",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

				// Aktionen
				switch (schreiben) {
				case JOptionPane.YES_OPTION:
					break;
				case JOptionPane.NO_OPTION:
					txt();
					break;
				case JOptionPane.CANCEL_OPTION:
					einst.out("Export abgebrochen");
					break;
				default:
					einst.outEx(70, "Fehler beim Export.");
					break;
				}
			}
			
			// Datei schreiben
			if(schreiben == JOptionPane.YES_OPTION){
				try {
					// Wirter erzeugen
					PrintWriter writer = new PrintWriter(new FileWriter(ausgabe, false),true);
					
					// In Datei schreiben
					writer.print(getVorschau());
					writer.close();
					
					einst.out("Export beendet.");
				} catch (Exception e) {
					einst.outEx(e, 71, "Post konnte nicht in Textdatei exportiert werden.");
				}
			}
		}
	
	}
	
	// Daten einlesen
	private void einlesen(){
		// ComboBoxen leeren
		comboBoxPost.removeAllItems();
		comboBoxHeader.removeAllItems();
		comboBoxFooter.removeAllItems();
		
		// Daten aus DAO holen
		alPost = einst.dao().sucheAllePostsMitArray();
		alBereich = einst.dao().sucheAlleBereiche();
		alHeader = einst.dao().sucheAlleHeader();
		alFooter = einst.dao().sucheAlleFooter();
		
		// Fuellen der ComboBoxPost
		for(int i = alPost.size()-1; i > 0; i--){
			if(alPost.get(i).getDatum().equals(""))
			comboBoxPost.addItem("ID: " + String.valueOf(alPost.get(i).getId()));
			else{
				comboBoxPost.addItem("ID: " + String.valueOf(alPost.get(i).getId()) +
						" - Datum: " + Helfer.datumLn(alPost.get(i).getDatum()));
			}
		}
		
		// Fuellen der ComboBoxHeader
		for(int i = 1; i < alHeader.size(); i++){
			comboBoxHeader.addItem(Helfer.varText(alHeader.get(i).getName()));
		}
		
		// Fuellen der ComboBoxFooter
		for(int i = 1; i < alFooter.size(); i++){
			comboBoxFooter.addItem(Helfer.varText(alFooter.get(i).getName()));
		}
		
		// Erstauswahl
		comboBoxHeader.setSelectedItem(alHeader.get(
				Helfer.sucheALHeaderHid(alHeader, einst.getSHeader())).getName());
		comboBoxFooter.setSelectedItem(alFooter.get(
				Helfer.sucheALFooterFid(alFooter, einst.getSFooter())).getName());
		
		auswahl();
	}

	// Auswahl eines Posts in der ComboBoxPost
	private void auswahl(){
		// Post aus Array holen
		if(comboBoxPost.getSelectedItem() != null){
			if(comboBoxPost.getSelectedItem().toString().contains(" - ")){
				String[] postInfo = comboBoxPost.getSelectedItem().toString().split(" - ");
				post = alPost.get(Helfer.sucheALPostPid(alPost,
						Integer.parseInt(postInfo[0].toString().replaceFirst("ID: ", ""))));
			}
			else{
				post = alPost.get(Helfer.sucheALPostPid(alPost,
						Integer.parseInt(comboBoxPost.getSelectedItem().toString().replaceFirst("ID: ", ""))));
			}
		
		// Themen und Kurznachrichten in eigenen Array speichern
		alThemen = post.getThemen();
		alKurz = post.getKurznachrichten();
		
		// Infos in panelInfos setzen, Bereichanzahl wird nach Zusammensetzen des Posts gesetzt
		txtrAnzahlthemen.setText(String.valueOf(post.getThemaSize()));
		txtrAnzahlkurzn.setText(String.valueOf(post.getKurznachrichtenSize()));
		
		if(post.getDatum().equals("")){
			// Wenn noch nicht gepostet
			txtrDatum.setText("-");
			btnPosten.setEnabled(true);
			btnClicktopost.setEnabled(true);
			btnPostOeffnen.setEnabled(false);
		}
		else{
			// Wenn bereits gepostet
			txtrDatum.setText(Helfer.datumL(post.getDatum()));
			btnPosten.setEnabled(false);
			btnClicktopost.setEnabled(false);
			btnPostOeffnen.setEnabled(true);
		}
		
		// Postformatierung holen
		txtrPostinhalt.setText(einst.getFormatierung());
		
		// Zeilenumbrüche einbauen
		txtrPostinhalt.setText(txtrPostinhalt.getText().replaceAll(Einst.vnl, Einst.nl));
		
		// Auswahl Header und in Inhalt einbauen
		if(comboBoxHeader.getSelectedItem() != null && txtrPostinhalt.getText().contains(Einst.vheader)){
			header = alHeader.get(Helfer.sucheALHeaderName(alHeader, Helfer
					.textVar(comboBoxHeader)));
			txtrPostinhalt.setText(txtrPostinhalt.getText().replaceAll(Einst.vheader, Helfer.varText(header.getInhalt())));
		}
		
		// Auswahl Footer und in Inhalt einbauen
		if(comboBoxFooter.getSelectedItem() != null && txtrPostinhalt.getText().contains(Einst.vfooter)){
			footer = alFooter.get(Helfer.sucheALFooterName(alFooter, Helfer
					.textVar(comboBoxFooter)));
			txtrPostinhalt.setText(txtrPostinhalt.getText()
					.replaceAll(Einst.vfooter, Helfer.varText(footer.getInhalt())));
		}
		
		// Bereiche und Themen des Posts in Inhalt einbauen
//		btkEinbau();
		try {
			btkEinbauNeu();
		} catch (Exception e) {
			einst.outEx(79, "Fehler beim Parsen des Posts." + Einst.nl +
					"Bitte die Postformatierung in den Einstellungen prüfen.");
		}
		
		// Anführungszeichen ersetzen
		txtrPostinhalt.setText(Helfer.varText(txtrPostinhalt.getText()));
		
		// Werte zählen
		txtrWrter.setText(Helfer.zahl(txtrPostinhalt.getText()));
		txtrZeichen.setText(String.valueOf(txtrPostinhalt.getText().length()));
		
		// Zeilenumbrueche setzen, wenn gewuenscht
		if(chckbxAutoumbruch.isSelected()){
			umbrueche();
		}

		// Anfuehrungszeichen setzen, wenn gewuenscht
		if(chckbxTopAnf.isSelected()){
			topo();
		}
		
		// Erste Zeilen anzeigen
		txtrPostinhalt.setCaretPosition(0);
		
		// Abschliessendes Woerterzaehlen
//		woerterZaehlen();
		
		}
	}
	
	// Neuer Bereich/Themen/Kurznachrichten-Einbau
	private void btkEinbauNeu() throws Exception {
		int anzahlBereiche = 0;
		
		// Bereichsbereich suchen
		String bereichSplitter[] = txtrPostinhalt.getText().split(Einst.vbereichStart);
		String bereichCode = bereichSplitter[1].split(Einst.vbereichEnde)[0];
		String bereichCodeVorThema = bereichCode.split(Einst.vthemaStart)[0];
		String bereichCodeNachThema = "";
		try {
			bereichCodeNachThema = bereichCode.split(Einst.vthemaEnde)[1];
		}catch(Exception e) {
			
		}
		txtrPostinhalt.setText(txtrPostinhalt.getText().replace(bereichCode, ""));
		
		String bereichtext = "";
		
		// Bereiche nach Wichtigkeit einbauen
		for(int w = 10; w >= 0; w--) {
			
			// Bereiche nacheinander abarbeiten
			for(int i = 0; i < alBereich.size(); i++) {
				
				// Bereich aus Array holen
				bereich = alBereich.get(i);
				
				// Bereich nur weiter abarbeiten, wenn Themen und Wichtigkeit passen
				if(w == bereich.getWkat() && Helfer.sucheALThemaBid(alThemen, i) != -1) {
					
					// Bereichszähler hochzählen
					anzahlBereiche++;
					
					// Themenbereich suchen
					String themenSplitter[] = bereichCode.split(Einst.vthemaStart);
					String themaCode = themenSplitter[1].split(Einst.vthemaEnde)[0];
					String themenText = "";
					
					// Prüfen, ob es eine Bereichsüberschrift gibt
					if(i != 0) {
						
						// Wenns eine Bereichüberschrift gibt diese einbauen
						bereichtext += bereichCodeVorThema.replaceAll(Einst.vbereichname, bereich.getName());
					}
					
					// Themenpriorität abarbeiten
					for (int x = 10; x >= 0; x--) {
						
						// Themen in Schleife abarbeiten
						for(int j = 0; j < alThemen.size(); j++) {
							
							// Thema aus Array holen
							thema = alThemen.get(j);
							
							// Thema nur weitermachen, wenn Wichtigkeit und Bereich passt
							if(x == thema.getWkat() && thema.getBid() == bereich.getId()) {
								
								// Thema in Rohcode einbauen
								themenText += themaCode.replaceAll(Einst.vthemenname, thema.getName())
										.replaceAll(Einst.vthementext, thema.getInhalt());
							}
						}
					}
					
					// Themen einbauen
					bereichtext += themenText + bereichCodeNachThema;
					
				}
			}
		}
		
		// Bereiche in Post einbauen
		txtrPostinhalt.setText(txtrPostinhalt.getText()
				.replaceAll(Einst.vbereichStart + Einst.vbereichEnde, bereichtext));
		
		// Kurznachrichtenbereich suchen
		String kurzSplitter[] = txtrPostinhalt.getText().split(Einst.vkurzBereichStart);
		String kurzCode = kurzSplitter[1].split(Einst.vkurzBereichEnde)[0];
		String kurzCodeVorThema = kurzCode.split(Einst.vkurzStart)[0];
		String kurzCodeThema = kurzCode.split(Einst.vkurzStart)[1];
		String kurzCodeNachThema = "";
		try {
			kurzCodeNachThema = kurzCodeThema.split(Einst.vkurzEnde)[1];
		}catch(Exception e) {
			
		}
		kurzCodeThema = kurzCodeThema.split(Einst.vkurzEnde)[0];
		txtrPostinhalt.setText(txtrPostinhalt.getText().replace(kurzCode, ""));
		String kurzText = "";
		
		// Kurznachrichten nur abarbeiten, wenn welche existieren
		if(alKurz.size() > 0) {
			
			// Überschrift einbauen
			kurzText = kurzCodeVorThema.replaceAll(Einst.vkurzUeber, einst.getKurzBereichName());
			
			// Kurzprio abarbeiten
			for(int x = 10; x >= 0; x--) {
				
				// Kurznachrichten in Schleife abarbeiten
				for(int j = 0; j < alKurz.size(); j++) {
					
					// Kurznachricht aus Array holen
					kurznachricht = alKurz.get(j);
					
					// Kurznachricht nur weitermachen, wenn alles passt
					if(x == kurznachricht.getWkat()) {
						
						// Kurznachricht in Rohcode einbauen
						kurzText += kurzCodeThema.replaceAll(Einst.vkurzText, kurznachricht.getInhalt());
					}
				}
			}
			
			
			// Rest vom Kurznachrichtenbereich einbauen
			kurzText += kurzCodeNachThema;
		}
		
		// Kurznachrichtenbereich in Post einbauen
		txtrPostinhalt.setText(txtrPostinhalt.getText()
				.replaceAll(Einst.vkurzBereichStart + Einst.vkurzBereichEnde, kurzText));
		
		// Textgrößen anpassen
		txtrPostinhalt.setText(txtrPostinhalt.getText()
				.replaceAll(Einst.vbUeberGroesse, String.valueOf(einst.getGbereich()))
				.replaceAll(Einst.vkUeberGroesse, String.valueOf(einst.getGKurzUeber()))
				.replaceAll(Einst.vtTextGroesse, String.valueOf(einst.getGnormal()))
				.replaceAll(Einst.vtUeberGroesse, String.valueOf(einst.getGthema()))
				.replaceAll(Einst.vkurzTextGroesse, String.valueOf(einst.getGKurzText())));
		
		// SY-Datum bei Beadarf berechnen
		if(txtrPostinhalt.getText().contains(Einst.vdatumSY)) {
			String datumRL = einst.getNeuesPostDatum();
			
			// Heutiges Datum in Variablen Speichern
			int tag = Integer.parseInt(datumRL.substring(0, 2));
			int mon = Integer.parseInt(datumRL.substring(3, 5));
			int jahr = Integer.parseInt(datumRL.substring(6, 10));
			int stu = Integer.parseInt(datumRL.substring(11, 13));
			int min = Integer.parseInt(datumRL.substring(14, 16));
			
			txtrPostinhalt.setText(txtrPostinhalt.getText().replaceAll(Einst.vdatumSY,
					DatumRechner.rLSyString(jahr, mon, tag, stu, min, einst)));
		}
		
		// RL-Datum bei Bedarf einfügen
		if(txtrPostinhalt.getText().contains(Einst.vdatumRL)) {
			String datum = einst.getNeuesPostDatum();
			txtrPostinhalt.setText(txtrPostinhalt.getText()
					.replaceAll(Einst.vdatumRL, datum.substring(0, datum.length()-6)));
		}
		
		// Kommentare entfernen
		int kommentarStart = txtrPostinhalt.getText().indexOf(Einst.vkommentarStart);
		int kommentarEnde = txtrPostinhalt.getText().indexOf(Einst.vkommentarEnde);
		do {
			String entfernen = txtrPostinhalt.getText().
					substring(kommentarStart, kommentarEnde+Einst.vkommentarEnde.length());
			txtrPostinhalt.setText(txtrPostinhalt.getText().replaceFirst(entfernen, ""));

			kommentarStart = txtrPostinhalt.getText().indexOf(Einst.vkommentarStart);
			kommentarEnde = txtrPostinhalt.getText().indexOf(Einst.vkommentarEnde);
		} while(kommentarStart != -1 && kommentarEnde != -1);
		
		// Anzahl Bereiche anzeigen
		txtrAnzahlbereiche.setText(String.valueOf(anzahlBereiche));
	}
	
	// Bereiche und Themen in den Post einbauen
//	private void btkEinbau(){
//		int anzahlBereiche = 0;
//		
//		// Wichtigkeit der Bereiche durchgehen
//		for(int w = 10; w >= 0; w--){
//			
//			// Bereiche in Schleife abarbeiten
//			for(int i = 0; i < alBereich.size(); i++){
//				
//				// Bereich aus Array holen
//				bereich = alBereich.get(i);
//				
//				// Nur abarbeiten, wenn es im Bereich Themen gibt und Wichtigkeit passt
//				if(w == bereich.getWkat() && Helfer.sucheALThemaBid(alThemen, i) != -1){
//					
//					// Ermittlung Anzahl Bereiche
//					anzahlBereiche++;
//					
//					// Pruefe bid zwecks Ueberschrift
//					if(i != 0){
//						
//						// Wenn es einen Bereich gibt Ueberschrift setzen
//						if(einst.getGbereich() != 2){
//							txtrPostinhalt.append("[B][SIZE=" + einst.getGbereich() + "]" +
//									bereich.getName().replaceAll("=APOSTROPH=", "'") +
//									"[/SIZE][/B]" + Einst.nl + Einst.nl);
//						}else{
//							txtrPostinhalt.append("[B]" + bereich.getName().replaceAll("=APOSTROPH=", "'") +
//									"[/B]" + Einst.nl + Einst.nl);
//						}
//					}
////					
//					// Wichtigkeit fuer Themen abarbeiten
//					for(int x = 10; x >= 0; x--){
//						
//						// Themen in Schleife abarbeiten
//						for(int j = 0; j < alThemen.size(); j++){
//							
//							// Thema aus Array holen
//							thema = alThemen.get(j);
//							
//							// Pruefen, ob Thema in Bereich enthalten und Wichtigkeit passt
//							if(x == thema.getWkat() && thema.getBid() == bereich.getId()){
//								
//								// Ueberschrift eines Themas setzen
//								if(einst.getGthema() != 2){
//									txtrPostinhalt.append("[B][SIZE=" + einst.getGthema() + "]" +
//											thema.getName().replaceAll("=APOSTROPH=", "'") +
//											"[/SIZE][/B]" + Einst.nl + Einst.nl);
//								}else{
//									txtrPostinhalt.append("[B]" + thema.getName().replaceAll("=APOSTROPH=", "'") +
//											"[/B]" + Einst.nl + Einst.nl);
//								}
//								
//								// Inhalt eines Themas setzen
//								if(einst.getGnormal() != 2){
//									txtrPostinhalt.append("[SIZE=" + einst.getGnormal() + "]" +
//											thema.getInhalt().replaceAll("=APOSTROPH=", "'") +
//											"[/SIZE]" + Einst.nl + Einst.nl);
//								}else{
//									txtrPostinhalt.append(thema.getInhalt().replaceAll("=APOSTROPH=", "'") +
//											Einst.nl + Einst.nl);
//								}
//							}
//						}
//					}
//					
//					// Nach Bereich eine Leerzeile
//					txtrPostinhalt.append(Einst.nl);
//				}
//				
//			}
//		}
//		
//		// Kurznachrichten nur Ararbeiten, wenn welche existieren
//		if(alKurz.size() != 0){
//			
//			// Ueberschrift setzen
//			if(einst.getGbereich() != 2){
//				txtrPostinhalt.append("[B][SIZE=" + einst.getGbereich() + "]Kurznachrichten[/SIZE][/B]" + Einst.nl);
//			}else{
//				txtrPostinhalt.append("[B]Kurznachrichten[/B]" + Einst.nl);
//			}
//			
//			// Auflistung starten
//			txtrPostinhalt.append("[LIST]");
//			
//			// evtl SIZE
//			if(einst.getGnormal() != 2){
//				txtrPostinhalt.append("[SIZE=" + einst.getGnormal() + "]");
//			}
//			
//			// neue Zeile fuer Kurznachrichten
//			txtrPostinhalt.append(Einst.nl);
//			
//			// Wichtigkeitsschleife fuer Kurznachrichten
//			for(int y = 10; y >= 0; y--){
//				
//				// Kurznachrichten in Schleife abbauen
//				for(int k = 0; k < alKurz.size(); k++){
//					
//					// Kurznachricht aus Array holen
//					kurznachricht = alKurz.get(k);
//					
//					// Nur einbauen, wenn Wichtigkeit passt
//					if(y == kurznachricht.getWkat()){
//
//						// Inhalt der Kurznachricht setzen
//						txtrPostinhalt.append("[*]" + kurznachricht.getInhalt().
//								replaceAll("=APOSTROPH=", "'") + Einst.nl);
//					}
//				}
//			}
//			
//			// evtl SIZE
//			if(einst.getGnormal() != 2){
//				txtrPostinhalt.append("[/SIZE]");
//			}
//			
//			// Auflistung beenden
//			txtrPostinhalt.append("[/LIST]");
//		}
//		
//		// Anzahl Bereiche anzeigen
//		txtrAnzahlbereiche.setText(String.valueOf(anzahlBereiche));
//		
//	}
	
	private void umbrueche(){
		// Hole Vorschautext
		StringBuilder sb = new StringBuilder(txtrPostinhalt.getText());
		
		// Erhoehe Kapazitaet vom StringBuilder
		sb.ensureCapacity(sb.capacity() + 4095);
		
		// Variablen initialisieren
		int zeilenlaenge = 0;
		boolean bbCodeAktiv = false;
		boolean imgAktiv = false;
		boolean urlAktiv = false;
		
		// Text Zeichen fuer Zeichen in Schleife durchgehen
		for(int i = 0; i < sb.length(); i++){
			// Pruefen, ob BB-Code beginnt
			if(sb.charAt(i) == '['){
				bbCodeAktiv = true;
			}
			
			// Pruefen, ob BB-Code endet
			if(i > 1 && sb.charAt(i-1) == ']'){
				bbCodeAktiv = false;
			}
			
			// Pruefen, ob BB-Code fuer Bild aktiv
			if(sb.charAt(i) == '[' && (sb.charAt(i+1) == 'I' || sb.charAt(i+1) == 'i') &&
					(sb.charAt(i+2) == 'M' || sb.charAt(i+2) == 'm') &&
					(sb.charAt(i+3) == 'G' || sb.charAt(i+3) == 'g') && sb.charAt(i+4) == ']'){
				imgAktiv = true;
			}
			
			// Pruefen, ob BB-Code fuer Bild endet
			if(i > 6 && sb.charAt(i-6) == '[' && sb.charAt(i-5) == '/' &&
					(sb.charAt(i-4) == 'I' || sb.charAt(i-4) == 'i') &&
					(sb.charAt(i-3) == 'M' || sb.charAt(i-3) == 'm') &&
					(sb.charAt(i-2) == 'G' || sb.charAt(i-2) == 'g') && sb.charAt(i-1) == ']'){
				imgAktiv = false;
			}
			
			// Pruefen, ob BB-Code fuer URL aktiv
			System.out.println(sb.charAt(i));
			if(sb.charAt(i) == '[' && (sb.charAt(i+1) == 'U' || sb.charAt(i+1) == 'u') &&
					(sb.charAt(i+2) == 'R' || sb.charAt(i+2) == 'r') &&
					(sb.charAt(i+3) == 'L' || sb.charAt(i+3) == 'l') &&
					(sb.charAt(i+4) == '=') && (sb.charAt(i+5) == '\"')){
				urlAktiv = true;
			}
			
			// Pruefen, ob BB-Code fuer Bild endet
			if(urlAktiv && i > 2 && sb.charAt(i-2) == '\"' && sb.charAt(i-1) == ']'){
				urlAktiv = false;
			}
			
			// Zeilenlaenge erhoehen, wenn kein bb-code aktiv ist
			if(!bbCodeAktiv && !imgAktiv && !urlAktiv){
				zeilenlaenge++;
			}
			
			// Wenn Zeilenlaenge ueber der Autoumbruchlaenge und Zeichen ein Leerzeichen
			if(zeilenlaenge >= einst.getAutoumbruch() && sb.charAt(i) == ' '){
				// Leerzeichen durch Zeilenumbruch ersetzen
				sb.setCharAt(i, '\n');
			}
			
			// Wenn aktuelles Zeichen ein Zeilenumbruch ist
			if(sb.charAt(i) == '\n'){
				
				// Zuruecksetzen der Zeilenlaenge
				zeilenlaenge = 0;
			}
		}
		
		// Vorschautext wieder einfuegen
		txtrPostinhalt.setText(sb.toString());
	}
	
	// Ein- und Ausbau von topografischen Anfuehrungszeichen
	private void topo(){
		if(chckbxTopAnf.isSelected()){
		// Einbau
			StringBuilder text = new StringBuilder(txtrPostinhalt.getText());
			int zeichenNr = 0;
			
			// Text Zeichen fuer Zeichen durchgehen
			for(int i = 0; i < text.length(); i++){
				if(text.charAt(i) == '"'){
					zeichenNr++;
					// Erstes Zeichen
					if(zeichenNr % 2 == 1) text.setCharAt(i, Einst.vTopoA);
					// Zweites Zeichen
					if(zeichenNr % 2 == 0) text.setCharAt(i, Einst.vTopoE);
				}
			}
			
			// Ersetzten Text in Vorschau einbauen
			txtrPostinhalt.setText(text.toString());
		}
		else{
		// Ausbau
			String text = txtrPostinhalt.getText();
			
			// Ersetzen
			text = text.replaceAll(String.valueOf(Einst.vTopoA), "\"");
			text = text.replaceAll(String.valueOf(Einst.vTopoA), "\"");
			
			// Text wieder in Vorschau einfuegen
			txtrPostinhalt.setText(text);
		}
	}
	
	// Inhalt aus Vorschau weitergeben
	public String getVorschau(){
		return txtrPostinhalt.getText();
	}
	
	// Baumansicht aufrufen
	private void baumansicht(){
		dispose();
		new PostExportBaum(einst, hm);
	}
	
	// Fenster schliessen
	private void beenden(){
		einst.out("Schließe Postexporter.");
		dispose();
		hm.setVisible(true);
	}
}
