package de.simocracy.postwriter.frameEinstellungen;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.themaEditor.Postformatierung;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import java.awt.GridLayout;

import javax.swing.JTextPane;

public abstract class FrameEinstellungen extends Fenster {

	private static final long serialVersionUID = -8171280558753237637L;
	protected Einst einst;
	protected JButton btnAbbreichen;
	protected JButton btnSpeichern;
	protected Hauptmenue hm;
	protected JLabel lblNick;
	protected JTextField txtWikiartikel;
	protected JTextField txtNick;
	protected JButton btnEinstellungenZurcksetzen;
	protected JSlider sliderGBereich;
	protected JSlider sliderGThema;
	protected JSlider sliderGNormal;
	protected JSlider sliderAutoumbruch;
	protected JSlider sliderGKurzUeber;
	protected JSlider sliderGKurzText;
	protected JPasswordField pwdPw;
	protected JPasswordField pwdPwwh;
	protected JCheckBox chckbxTopo;
	protected JCheckBox chckbxNeuerPost;
	protected JButton btnArbeitsverzeichnisndern;
	protected JLabel lblSimForumPasswort;
	protected JButton btnPasswortAusDatenbank;
	protected JButton btnPasswortndern;
	protected JCheckBox chckbxExportBaum;
	protected JTextField txtNopasteseite;
	protected JCheckBox chckbxAutoUpdate;
	protected JTextField txtNamekurzbereich;
	
	private JPanel contentPane;
	private JLabel lblAutoumbruchNach;
	private JLabel lblGreberschriftenEines;
	private JLabel lblGreberschriftenEines_1;
	private JLabel lblGreNormalerText;
	private JLabel lblLinkZumWikiartikel;
	private JLabel lblPasswortWiederholen;
	private JTextField textFieldSliderUmbruch;
	private JTextField textFieldSliderGBereich;
	private JTextField textFieldSliderGThema;
	private JTextField textFieldSliderGNormal;
	private JTabbedPane tabbedPane;
	private JPanel pnlAllgemein;
	private JPanel pnlPosteinstellungen;
	private JLabel lblArbeitsverzeichnis;
	private JTextField txtArbeitsverzeichnis;
	private JPanel pnlPassworteinstellungen;
	private JPanel AllgemeinButtonPanel;
	private JPanel PasswortButtonsPanel;
	private JPanel PostEinstellungsButtonsPanel;
	private JButton btnPostformatierung;
	private JLabel lblNopasteseite;
	private JButton btnAufUpdatesPrfen;
	private JTextPane txtpnPasswortWirdVor;
	private JButton btnHilfeZurPostform;
	private JLabel lblGreKurznachrichtenberschrift;
	private JLabel lblGreKurznachrichtentext;
	private JLabel lblNameKurznachrichtenbereich;
	private JTextField textFieldSliderGKurzUeber;
	private JTextField textFieldSliderGKurzText;

	public FrameEinstellungen(final Einst einst, Hauptmenue hm) {
		super(435,500);
		setVisible(true);
		this.einst = einst;
		this.hm = hm;
		
		setTitle("PostWriter - Einstellungen");
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new MigLayout("", "[grow][grow]", "[grow,center][]"));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, "cell 0 0 2 1,grow");
		
		pnlAllgemein = new JPanel();
		pnlAllgemein.setBorder(new EmptyBorder(5, 5, 5, 5));
		tabbedPane.addTab("Allgemein", null, pnlAllgemein, null);
		tabbedPane.setEnabledAt(0, true);
		pnlAllgemein.setLayout(new MigLayout("", "[][10:10:10][grow,fill]", "[][][][][][][][][]"));
		
		this.lblNick = new JLabel("Nick:");
		pnlAllgemein.add(lblNick, "cell 0 0");
		
		this.txtNick = new JTextField();
		pnlAllgemein.add(txtNick, "cell 2 0,alignx center");
		this.txtNick.setToolTipText("<html>Nick des Nutzer zu Verwaltungszwecken.<br>Der Nick kann " +
				"nachträglich NICHT geändert werden!</html>");
		this.txtNick.setColumns(10);
		
		this.lblLinkZumWikiartikel = new JLabel("Link zum Wikiartikel:");
		pnlAllgemein.add(lblLinkZumWikiartikel, "cell 0 1");
		
		this.txtWikiartikel = new JTextField();
		pnlAllgemein.add(txtWikiartikel, "cell 2 1,growx");
		this.txtWikiartikel.setToolTipText("Link zum Wikiartikel. Kann auch zu einem bestimmten " +
				"Bereich verlinkt werden.");
		this.txtWikiartikel.setColumns(10);
		
		lblNopasteseite = new JLabel("Nopaste-Seite:");
		pnlAllgemein.add(lblNopasteseite, "cell 0 2");
		
		txtNopasteseite = new JTextField();
		pnlAllgemein.add(txtNopasteseite, "cell 2 2,growx");
		txtNopasteseite.setColumns(10);
		
		chckbxNeuerPost = new JCheckBox("Nach erfolgeichem Posten automatisch neuen Post anlegen");
		chckbxNeuerPost.setMnemonic('p');
		pnlAllgemein.add(chckbxNeuerPost, "cell 0 3 3 1");
		chckbxNeuerPost.setToolTipText("Automatisch einen neuen Post in der Datenbank anlegen, " +
				"wenn das Posten erfolgreich war");
		
		chckbxExportBaum = new JCheckBox("Posts im Postexport-Fenster in der Baumansicht anzeigen");
		chckbxExportBaum.setMnemonic('b');
		chckbxExportBaum.setToolTipText("<html>Zeit Export- und Archiv-Fenster standardmäßig in<br>" +
				"der Baumansicht und nicht in der Editoransicht</html>");
		pnlAllgemein.add(chckbxExportBaum, "cell 0 4 3 1");
		
		chckbxAutoUpdate = new JCheckBox("Beim Start automatisch nach Updates suchen");
		chckbxAutoUpdate.setMnemonic('s');
		chckbxAutoUpdate.setToolTipText("Beim Start des PostWriter automatisch nach einer " +
				"neuen Version suchen");
		pnlAllgemein.add(chckbxAutoUpdate, "cell 0 5 3 1");
		
		lblArbeitsverzeichnis = new JLabel("Aktuelles Arbeitsverzeichnis:");
		pnlAllgemein.add(lblArbeitsverzeichnis, "cell 0 6 3 1");
		
		txtArbeitsverzeichnis = new JTextField();
		txtArbeitsverzeichnis.setToolTipText("Aktuelles Arbeitsverzeichnis, in dem die Datenbank gespeichert ist");
		txtArbeitsverzeichnis.setEditable(false);
		txtArbeitsverzeichnis.setText(einst.getVerzeichnis());
		pnlAllgemein.add(txtArbeitsverzeichnis, "cell 0 7 3 1,growx");
		txtArbeitsverzeichnis.setColumns(10);
		
		AllgemeinButtonPanel = new JPanel();
		pnlAllgemein.add(AllgemeinButtonPanel, "cell 0 8 3 1,grow");
		AllgemeinButtonPanel.setLayout(new GridLayout(0, 2, 4, 4));
		
		btnArbeitsverzeichnisndern = new JButton("Arbeitsverzeichnis ändern");
		btnArbeitsverzeichnisndern.setMnemonic('v');
		AllgemeinButtonPanel.add(btnArbeitsverzeichnisndern);
		
		this.btnEinstellungenZurcksetzen = new JButton("Datenbank zurücksetzen");
		btnEinstellungenZurcksetzen.setMnemonic('z');
		AllgemeinButtonPanel.add(btnEinstellungenZurcksetzen);
		
		btnAufUpdatesPrfen = new JButton("Auf Updates prüfen");
		btnAufUpdatesPrfen.setMnemonic('u');
		btnAufUpdatesPrfen.setToolTipText("Manuell nach einer neuen Version des PostWriter suchen");
		btnAufUpdatesPrfen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				einst.versionSuche(true);
			}
		});
		AllgemeinButtonPanel.add(btnAufUpdatesPrfen);
		this.btnEinstellungenZurcksetzen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetDB();
			}
		});
		btnArbeitsverzeichnisndern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				neuesVerzeichnis();
			}
		});
		
		pnlPassworteinstellungen = new JPanel();
		tabbedPane.addTab("Passwort", null, pnlPassworteinstellungen, "");
		tabbedPane.setEnabledAt(1, true);
		pnlPassworteinstellungen.setLayout(new MigLayout("", "[50px:n,grow][10:10:10][50px:47%,grow,fill]", "[][][][]"));
		
		lblSimForumPasswort = new JLabel("SimForum-Passwort für CtP ändern:");
		pnlPassworteinstellungen.add(lblSimForumPasswort, "cell 0 0");
		
		pwdPw = new JPasswordField();
		pnlPassworteinstellungen.add(pwdPw, "cell 2 0");
		pwdPw.setToolTipText("Eingabe nur, wenn Passwort geändert werden soll");
		
		lblPasswortWiederholen = new JLabel("Passwort wiederholen:");
		pnlPassworteinstellungen.add(lblPasswortWiederholen, "cell 0 1");
		
		pwdPwwh = new JPasswordField();
		pnlPassworteinstellungen.add(pwdPwwh, "cell 2 1");
		
		txtpnPasswortWirdVor = new JTextPane();
		txtpnPasswortWirdVor.setOpaque(false);
		txtpnPasswortWirdVor.setText("Hinweis: Das Passwort wird vor der Speicherung mit dem gleichen\n" +
				"Verfahren verschlüsselt, den das SimForum verwendet.");
		pnlPassworteinstellungen.add(txtpnPasswortWirdVor, "cell 0 2 3 1,grow");
		
		PasswortButtonsPanel = new JPanel();
		pnlPassworteinstellungen.add(PasswortButtonsPanel, "cell 0 3 3 1,grow");
		PasswortButtonsPanel.setLayout(new GridLayout(1, 2, 4, 4));
		
		btnPasswortndern = new JButton("Passwort ändern");
		btnPasswortndern.setMnemonic('ä');
		PasswortButtonsPanel.add(btnPasswortndern);
		
		btnPasswortAusDatenbank = new JButton("Passwort löschen");
		btnPasswortAusDatenbank.setMnemonic('l');
		PasswortButtonsPanel.add(btnPasswortAusDatenbank);
		btnPasswortAusDatenbank.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pwloeschen();
			}
		});
		btnPasswortAusDatenbank.setToolTipText("Bestehendes Passwort aus Datenbank entfernen");
		btnPasswortndern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pwaendern();
			}
		});
		
		pnlPosteinstellungen = new JPanel();
		pnlPosteinstellungen.setBorder(new EmptyBorder(5, 5, 5, 5));
		tabbedPane.addTab("Posteinstellungen", null, pnlPosteinstellungen, null);
		pnlPosteinstellungen.setLayout(new MigLayout("", "[grow][10:10:10][grow,fill][25:25:25,grow]", "[][][][][][][][][]"));
		
		this.lblAutoumbruchNach = new JLabel("Autoumbruch nach ... Zeichen:");
		pnlPosteinstellungen.add(lblAutoumbruchNach, "cell 0 0");
		
		this.sliderAutoumbruch = new JSlider(0, 200);
		pnlPosteinstellungen.add(sliderAutoumbruch, "cell 2 0,alignx center");
		sliderAutoumbruch.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				sliderAutoumbruch.setValue(sliderAutoumbruch.getValue()-arg0.getWheelRotation());
			}
		});
		this.sliderAutoumbruch.setToolTipText("Zum deaktivieren auf 0 setzen");
		this.sliderAutoumbruch.setPaintLabels(true);
		this.sliderAutoumbruch.setSnapToTicks(true);
		this.sliderAutoumbruch.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				textFieldSliderUmbruch.setText(String.valueOf(sliderAutoumbruch.getValue()));
			}
		});
		this.sliderAutoumbruch.setMinorTickSpacing(1);
		this.sliderAutoumbruch.setMajorTickSpacing(40);
		
		textFieldSliderUmbruch = new JTextField();
		pnlPosteinstellungen.add(textFieldSliderUmbruch, "cell 3 0");
		textFieldSliderUmbruch.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				sliderAutoumbruch.setValue(sliderAutoumbruch.getValue()-e.getWheelRotation());
			}
		});
		textFieldSliderUmbruch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				sliderAutoumbruch.setValue(Integer.parseInt(textFieldSliderUmbruch.getText()));
			}
		});
		textFieldSliderUmbruch.setText("0");
		textFieldSliderUmbruch.setColumns(10);
		
		this.lblGreberschriftenEines = new JLabel("Größe Überschriften eines Bereichs:");
		pnlPosteinstellungen.add(lblGreberschriftenEines, "cell 0 1");
		
		this.sliderGBereich = new JSlider(1,7);
		pnlPosteinstellungen.add(sliderGBereich, "cell 2 1");
		sliderGBereich.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				sliderGBereich.setValue(sliderGBereich.getValue()-e.getWheelRotation());
			}
		});
		this.sliderGBereich.setPaintLabels(true);
		this.sliderGBereich.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				textFieldSliderGBereich.setText(String.valueOf(sliderGBereich.getValue()));
			}
		});
		this.sliderGBereich.setSnapToTicks(true);
		this.sliderGBereich.setRequestFocusEnabled(false);
		this.sliderGBereich.setMinorTickSpacing(1);
		this.sliderGBereich.setMajorTickSpacing(1);
		
		textFieldSliderGBereich = new JTextField();
		pnlPosteinstellungen.add(textFieldSliderGBereich, "cell 3 1");
		textFieldSliderGBereich.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				sliderGBereich.setValue(sliderGBereich.getValue()-e.getWheelRotation());
			}
		});
		textFieldSliderGBereich.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				sliderGBereich.setValue(Integer.parseInt(textFieldSliderGBereich.getText()));
			}
		});
		textFieldSliderGBereich.setText("0");
		textFieldSliderGBereich.setColumns(10);
		
		this.lblGreberschriftenEines_1 = new JLabel("Größe Überschriften eines Themas:");
		pnlPosteinstellungen.add(lblGreberschriftenEines_1, "cell 0 2");
		
		this.sliderGThema = new JSlider(1,7);
		pnlPosteinstellungen.add(sliderGThema, "cell 2 2");
		sliderGThema.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				sliderGThema.setValue(sliderGThema.getValue()-e.getWheelRotation());
			}
		});
		this.sliderGThema.setPaintLabels(true);
		this.sliderGThema.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				textFieldSliderGThema.setText(String.valueOf(sliderGThema.getValue()));
			}
		});
		this.sliderGThema.setSnapToTicks(true);
		this.sliderGThema.setRequestFocusEnabled(false);
		this.sliderGThema.setMinorTickSpacing(1);
		this.sliderGThema.setMajorTickSpacing(1);
		
		textFieldSliderGThema = new JTextField();
		pnlPosteinstellungen.add(textFieldSliderGThema, "cell 3 2");
		textFieldSliderGThema.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				sliderGThema.setValue(sliderGThema.getValue()-e.getWheelRotation());
			}
		});
		textFieldSliderGThema.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				sliderGThema.setValue(Integer.parseInt(textFieldSliderGThema.getText()));
			}
		});
		textFieldSliderGThema.setText("0");
		textFieldSliderGThema.setColumns(10);
		
		this.lblGreNormalerText = new JLabel("Größe normaler Text:");
		pnlPosteinstellungen.add(lblGreNormalerText, "cell 0 3");
		
		this.sliderGNormal = new JSlider(1, 7);
		pnlPosteinstellungen.add(sliderGNormal, "cell 2 3");
		sliderGNormal.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				sliderGNormal.setValue(sliderGNormal.getValue()-e.getWheelRotation());
			}
		});
		this.sliderGNormal.setPaintLabels(true);
		this.sliderGNormal.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				textFieldSliderGNormal.setText(String.valueOf(sliderGNormal.getValue()));
			}
		});
		this.sliderGNormal.setSnapToTicks(true);
		this.sliderGNormal.setRequestFocusEnabled(false);
		this.sliderGNormal.setMinorTickSpacing(1);
		this.sliderGNormal.setMajorTickSpacing(1);
		
		textFieldSliderGNormal = new JTextField();
		pnlPosteinstellungen.add(textFieldSliderGNormal, "cell 3 3");
		textFieldSliderGNormal.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				sliderGNormal.setValue(sliderGNormal.getValue()-e.getWheelRotation());
			}
		});
		textFieldSliderGNormal.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				sliderGNormal.setValue(Integer.parseInt(textFieldSliderGNormal.getText()));
			}
		});
		textFieldSliderGNormal.setText("0");
		textFieldSliderGNormal.setColumns(10);
		
		lblGreKurznachrichtenberschrift = new JLabel("Größe Kurznachrichtenüberschrift:");
		pnlPosteinstellungen.add(lblGreKurznachrichtenberschrift, "cell 0 4");
		
		sliderGKurzUeber = new JSlider(1, 7);
		sliderGKurzUeber.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				sliderGKurzUeber.setValue(sliderGKurzUeber.getValue()-e.getWheelRotation());
			}
		});
		sliderGKurzUeber.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				textFieldSliderGKurzUeber.setText(String.valueOf(sliderGKurzUeber.getValue()));
			}
		});
		sliderGKurzUeber.setMajorTickSpacing(1);
		sliderGKurzUeber.setMinorTickSpacing(1);
		sliderGKurzUeber.setPaintLabels(true);
		sliderGKurzUeber.setRequestFocusEnabled(false);
		sliderGKurzUeber.setSnapToTicks(true);
		sliderGKurzUeber.setValue(4);
		pnlPosteinstellungen.add(sliderGKurzUeber, "cell 2 4");
		
		textFieldSliderGKurzUeber = new JTextField();
		textFieldSliderGKurzUeber.setText("0");
		textFieldSliderGKurzUeber.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				sliderGKurzUeber.setValue(sliderGKurzUeber.getValue()-e.getWheelRotation());
			}
		});
		textFieldSliderGKurzUeber.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				sliderGKurzUeber.setValue(Integer.parseInt(textFieldSliderGKurzUeber.getText()));
			}
		});
		pnlPosteinstellungen.add(textFieldSliderGKurzUeber, "cell 3 4,growx");
		textFieldSliderGKurzUeber.setColumns(10);
		
		lblGreKurznachrichtentext = new JLabel("Größe Kurznachrichtentext:");
		pnlPosteinstellungen.add(lblGreKurznachrichtentext, "cell 0 5");
		
		sliderGKurzText = new JSlider(1, 7);
		sliderGKurzText.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				sliderGKurzText.setValue(sliderGKurzText.getValue()-e.getWheelRotation());
			}
		});
		sliderGKurzText.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				textFieldSliderGKurzText.setText(String.valueOf(sliderGKurzText.getValue()));
			}
		});
		sliderGKurzText.setMajorTickSpacing(1);
		sliderGKurzText.setMinorTickSpacing(1);
		sliderGKurzText.setRequestFocusEnabled(false);
		sliderGKurzText.setSnapToTicks(true);
		sliderGKurzText.setPaintLabels(true);
		sliderGKurzText.setValue(4);
		pnlPosteinstellungen.add(sliderGKurzText, "cell 2 5");
		
		textFieldSliderGKurzText = new JTextField();
		textFieldSliderGKurzText.setText("0");
		textFieldSliderGKurzText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				sliderGKurzText.setValue(Integer.parseInt(textFieldSliderGKurzText.getText()));
			}
		});
		textFieldSliderGKurzText.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				sliderGKurzText.setValue(sliderGKurzText.getValue()-e.getWheelRotation());
			}
		});
		pnlPosteinstellungen.add(textFieldSliderGKurzText, "cell 3 5,growx");
		textFieldSliderGKurzText.setColumns(10);
		
		lblNameKurznachrichtenbereich = new JLabel("Name Kurznachrichtenbereich:");
		pnlPosteinstellungen.add(lblNameKurznachrichtenbereich, "cell 0 6");
		
		txtNamekurzbereich = new JTextField();
		pnlPosteinstellungen.add(txtNamekurzbereich, "cell 2 6 2 1,growx");
		txtNamekurzbereich.setColumns(10);
		
		chckbxTopo = new JCheckBox("Standardmäßig topografische Anführungszeichen verwenden");
		chckbxTopo.setMnemonic('t');
		pnlPosteinstellungen.add(chckbxTopo, "cell 0 7 4 1");
		chckbxTopo.setToolTipText("„ “ statt \" \" verwenden. Anführungszeichen " +
				"werden erst beim Postexport ersetzt.");
		
		PostEinstellungsButtonsPanel = new JPanel();
		pnlPosteinstellungen.add(PostEinstellungsButtonsPanel, "cell 0 8 4 1,grow");
		PostEinstellungsButtonsPanel.setLayout(new GridLayout(0, 2, 4, 0));
		
		btnPostformatierung = new JButton("Postformatierung");
		btnPostformatierung.setMnemonic('p');
		btnPostformatierung.setToolTipText("Voreinstellung für Formatierung des ganzen Posts ändern");
		btnPostformatierung.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				postformatierung(); 
			}
		});
		PostEinstellungsButtonsPanel.add(btnPostformatierung);
		
		btnHilfeZurPostform = new JButton("Hilfe zur Postform.");
		btnHilfeZurPostform.setMnemonic('H');
		btnHilfeZurPostform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				einst.oeffneLink(Einst.pwrLink + "#Postformatierung");
			}
		});
		PostEinstellungsButtonsPanel.add(btnHilfeZurPostform);
		
		this.btnSpeichern = new JButton("Speichern");
		btnSpeichern.setMnemonic('s');
		contentPane.add(btnSpeichern, "cell 0 1,growx");
		
		this.btnAbbreichen = new JButton("Abbrechen");
		btnAbbreichen.setMnemonic('a');
		contentPane.add(btnAbbreichen, "cell 1 1,growx");
		this.btnAbbreichen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abbruch();
			}
		});
	}
	
	// Aktion bei Klick auf Abbrechen
	protected void abbruch(){
		einst.out("Schließe Einstellungsfenster.");
		dispose();
		hm.setVisible(true);
	}
	
	// Ablauf bei Reset
	private void resetDB(){
		einst.out("Datenbank soll geresettet werden.");
		int wahl1 = JOptionPane.NO_OPTION;
		int wahl2 = JOptionPane.NO_OPTION;
		
		wahl1 = JOptionPane.showConfirmDialog(null, "Durch Zurücksetzen der Einstellungen wird die " +
				"Datenbank, in der alle Daten gespeichert werden, gelöscht" + Einst.nl + "und neu erstellt. " +
				"Dadurch gehen alle Daten wie Posts, Bereiche und Themen unwiderruflich verloren!" + Einst.nl + 
				"Sollte es Probleme beim Lesen oder Schreiben der Datenbank geben, wird es empfohlen, sich " + 
				"an Gobo77" + Einst.nl + "zu wenden, um die Daten sichern zu können." + Einst.nl + Einst.nl +
				"Sind Sie sich sicher, dass Sie die Einstellungen zurücksetzen wollen?", "Sind Sie sicher?",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if(wahl1 == JOptionPane.YES_OPTION){
			einst.out("Sicherheitsabfrage 1 mit JA bestätigt.");
			wahl2 = JOptionPane.showConfirmDialog(null, "Sind Sie sich wirklich sicher, alle Daten " +
					"zu löschen?" + Einst.nl + "Diese Aktion kann nicht rückgängig gemacht werden!", "Sind Sie " +
					"wirklich sicher?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(wahl2 == JOptionPane.YES_OPTION){
				einst.out("Sicherheitsabfrage 2 mit JA bestätigt.");
				
				// In eigenem Thread starten
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						dispose();
						
						einst.resetDB();
						
						JOptionPane.showMessageDialog(null, "Einstellungen wurden zurückgesetzt. " +
								"Der PostWriter wird nun beendet.", "Datenbank gelöscht",
								JOptionPane.INFORMATION_MESSAGE);
						einst.out("PostWriter wird nun beendet.");
						
						System.exit(0);
					}
				}).start();
				
			}
		}
		
		if(wahl1 == JOptionPane.NO_OPTION || wahl2 == JOptionPane.NO_OPTION){
			einst.out("Reset der Datenbank abgebrochen.");
		}
	}
	
	// Verzeichnis aendern
	private void neuesVerzeichnis(){
		einst.out("Ändere Arbeitsverzeichnis.");
		JFileChooser fc = new JFileChooser(einst.getVerzeichnis());
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int result = fc.showDialog(null, "Verzeichnis verwenden");
		
		if(result == JFileChooser.APPROVE_OPTION){
			einst.speicherVerzeichnis(fc.getSelectedFile(), false);
		}
	}
	
	// Passwort aus DB löschen
	private void pwloeschen() {
		einst.dao().loeschePW();
	}
	
	// Passwort ändern
	private void pwaendern() {
		try {
			if(!speicherPW()){
				// Wenn aenderung abgebrochen wurde
				JOptionPane.showMessageDialog(null, "Das Passwort wurde nicht geändert.",
						"Passwort ändern", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Passwort speichern
	private boolean speicherPW() throws Exception{
		// Nur speichern, wenn Eingabefeld nicht leer
		if(pwdPw.getPassword().length > 0){
			// PWs hashen
			String pw1 = Helfer.hashMD5(pwdPw);
			String pw2 = Helfer.hashMD5(pwdPwwh);
			
			// Pruefen, ob PWs richtig sind
			if(pw1.equals(pw2)){
				// Altes PW holen
				String aPW = einst.dao().holePW();
				
				// Nach altem PW prüfen
				if(aPW == null || aPW.isEmpty()){
				// Wenn altes PW leer ist, sofort speichern
				einst.dao().speicherPW(pw1);
				JOptionPane.showMessageDialog(null, "Das Passwort wurde erfolgreich geändert.",
						"Passwort ändern", JOptionPane.INFORMATION_MESSAGE);
				return true;
				
				}else{
					// Wenn altes PW nicht leer ist, nach altem PW fragen
					JPasswordField pwf = new JPasswordField();
					Object[] text = {"Bitte das bestehende Passwort eingeben, um das Passwort zu ändern.\n", pwf};
					Object[] stringArray = {"OK","Abbrechen"};
					int i = JOptionPane.showOptionDialog(null, text, "Passwort eingeben", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, stringArray, text);
					
					// Pruefen, ob Abbruch
					if(i == JOptionPane.YES_OPTION){
						// Kein Abbruch
						String eingegeben = Helfer.hashMD5(pwf);
						
						// Pruefen, ob altes Passwort stimmt
						if(aPW.equals(eingegeben)){
							// Wenn Eingabe stimmt
							einst.dao().speicherPW(pw1);
							
							JOptionPane.showMessageDialog(null, "Das Passwort wurde erfolgreich geändert.",
									"Passwort ändern", JOptionPane.INFORMATION_MESSAGE);
							return true;
						}
						else{
							// Wenn Eingabe nicht stimmt
							JOptionPane.showMessageDialog(null, "Engabe des alten Passwort stimmt nicht mit der " +
									"Datenbank überein.", "Passwörter stimmen nicht überein",
									JOptionPane.ERROR_MESSAGE);
							return true;
						}
					}
					else{
						// Bei Abbruch
						return false;
					}
				}
			}
			else{
				// Wenn Kontrolle falsch ist
				JOptionPane.showMessageDialog(null, "Die eingegeben Passwörter stimmen nicht überein.",
						"Passwörter stimmen nicht überein", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		else{
			// Bei leerem Eingabefeld
			return true;
		}
	}
	
	protected void postformatierung(){
		setVisible(false);
		new Postformatierung(einst, this, false);
	}
}
