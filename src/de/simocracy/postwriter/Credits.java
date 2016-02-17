package de.simocracy.postwriter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import java.awt.Cursor;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

public class Credits extends JDialog {

	private static final long serialVersionUID = -8779517476976725728L;
	private JPanel contentPane;
	private JLabel lblSimocracyPostwriter;
	private JLabel lblVersion;
	private JLabel lblProgrammierer;
	private JLabel lblWichtigeLinks;
	private JLabel lblPolitikthread;
	private JLabel lblDiskussionsthread;
	private JLabel lblWikocracyportal;
	private JLabel lblWikiartikelDesPostwriters;
	private JLabel lblKontakt;
	private JButton btnSchlieen;
	private JTextArea textAreaPoli;
	private JTextArea textAreaDisku;
	private JTextArea textAreaWikiPortal;
	private JTextArea textAreaWikiPW;
	private JTextArea textAreaVersion;
	private JTextArea textAreaProgrammierer;
	private JButton btnPostwriterArtikelIm;
	private JTextArea txtrImSimocracydiskussionsthreadIm;
	private JLabel lblIcongrafiker;
	private JTextArea textAreaIconGrafiker;
	private JLabel lblIcon;
	private JLabel lblDatumsrechner;
	private JTextArea txtrFormelBasierendAuf;
	private JTextArea textAreaDatumsLink;
	private JLabel lblVersionDerDatenbank;
	private JTextArea textAreaVersionDB;
	private JButton btnLizenzXerialSqlite;
	private JPanel panelButtons;
	private JButton btnLizenzWikijava;

	public Credits(final Einst einst) {
		int bildHoehe = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		int bildBreite = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int hoehe = 570;
		int breite = 450;

		setTitle("PostWriter - Credits");
		setResizable(false);
		setModal(true);
		setBounds((bildBreite-breite)/2, (bildHoehe-hoehe)/2, breite, hoehe);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Einst.icon);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new MigLayout("", "[20:20:20][20:20:20,grow][grow][]", "[][][][][][][][][][][][][][][][][][][][][][][][]"));
		
		this.lblSimocracyPostwriter = new JLabel("Simocracy PostWriter");
		this.contentPane.add(this.lblSimocracyPostwriter, "cell 0 0 3 1");
		
		this.lblVersion = new JLabel("Version:");
		this.contentPane.add(this.lblVersion, "cell 0 1 3 1");
		
		this.textAreaVersion = new JTextArea();
		this.textAreaVersion.setText(Einst.version);
		this.textAreaVersion.setLineWrap(true);
		this.textAreaVersion.setFocusable(false);
		this.textAreaVersion.setEditable(false);
		this.textAreaVersion.setBackground(UIManager.getColor("Panel.background"));
		this.contentPane.add(this.textAreaVersion, "cell 1 2 2 1,growx");
		
		lblVersionDerDatenbank = new JLabel("Version der Datenbank:");
		contentPane.add(lblVersionDerDatenbank, "cell 0 3 3 1");
		
		textAreaVersionDB = new JTextArea();
		textAreaVersionDB.setText(Einst.dbVersion);
		textAreaVersionDB.setLineWrap(true);
		textAreaVersionDB.setFocusable(false);
		textAreaVersionDB.setEditable(false);
		textAreaVersionDB.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(textAreaVersionDB, "cell 1 4 2 1,grow");
		
		panelButtons = new JPanel();
		contentPane.add(panelButtons, "cell 3 4 1 6,grow");
		panelButtons.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		this.btnPostwriterArtikelIm = new JButton("PostWriter-Artikel im Wiki");
		btnPostwriterArtikelIm.setMnemonic('p');
		panelButtons.add(btnPostwriterArtikelIm, "1, 1");
		
		btnLizenzXerialSqlite = new JButton("Lizenz Xerial SQLite-JDBC");
		btnLizenzXerialSqlite.setMnemonic('s');
		btnLizenzXerialSqlite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Lizenzen(einst, "Xerial SQLite-JDBC", "Taro L. Saito",
						"https://bitbucket.org/xerial/sqlite-jdbc", "Apache20");
			}
		});
		panelButtons.add(btnLizenzXerialSqlite, "1, 3");
		
		btnLizenzWikijava = new JButton("Lizenz wiki-java");
		btnLizenzWikijava.setMnemonic('w');
		btnLizenzWikijava.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Lizenzen(einst, "wiki-java", "MER-C", 
						"http://code.google.com/p/wiki-java/", "gpl-3.0");
			}
		});
		panelButtons.add(btnLizenzWikijava, "1, 5");
		this.btnPostwriterArtikelIm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				einst.out("Öffne PostWriter-Artikel im Wiki");
				einst.oeffneLink(Einst.pwrLink);
			}
		});
		
		this.lblProgrammierer = new JLabel("Programmierung:");
		this.contentPane.add(this.lblProgrammierer, "cell 0 5 3 1");
		
		this.lblIcon = new JLabel("");
		this.lblIcon.setIcon(new ImageIcon(getClass().getResource(
				"/de/simocracy/postwriter/ressourcen/PWRLogo.png")));
		this.contentPane.add(this.lblIcon, "cell 3 0 1 4,alignx center");
		
		this.textAreaProgrammierer = new JTextArea();
		this.textAreaProgrammierer.setText("gobo77");
		this.textAreaProgrammierer.setLineWrap(true);
		this.textAreaProgrammierer.setFocusable(false);
		this.textAreaProgrammierer.setEditable(false);
		this.textAreaProgrammierer.setBackground(UIManager.getColor("Panel.background"));
		this.contentPane.add(this.textAreaProgrammierer, "cell 1 6 2 1,growx");
		
		this.lblIcongrafiker = new JLabel("Grafiken:");
		this.contentPane.add(this.lblIcongrafiker, "cell 0 7 3 1");
		
		this.textAreaIconGrafiker = new JTextArea();
		this.textAreaIconGrafiker.setText("pschraeer");
		this.textAreaIconGrafiker.setLineWrap(true);
		this.textAreaIconGrafiker.setFocusable(false);
		this.textAreaIconGrafiker.setEditable(false);
		this.textAreaIconGrafiker.setBackground(UIManager.getColor("Button.background"));
		this.contentPane.add(this.textAreaIconGrafiker, "cell 1 8 2 1,grow");
		
		lblDatumsrechner = new JLabel("Datumsrechner:");
		contentPane.add(lblDatumsrechner, "cell 0 9 3 1");
		
		txtrFormelBasierendAuf = new JTextArea();
		txtrFormelBasierendAuf.setBackground(UIManager.getColor("Panel.background"));
		txtrFormelBasierendAuf.setFocusable(false);
		txtrFormelBasierendAuf.setEditable(false);
		txtrFormelBasierendAuf.setText("Formel basierend auf Webscript von Fluggs:");
		contentPane.add(txtrFormelBasierendAuf, "cell 1 10 3 1,growx");
		
		textAreaDatumsLink = new JTextArea();
		textAreaDatumsLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		textAreaDatumsLink.setText(Einst.datumsLink);
		textAreaDatumsLink.setFocusable(false);
		textAreaDatumsLink.setEditable(false);
		textAreaDatumsLink.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(textAreaDatumsLink, "cell 1 11 3 1,growx");
		
		this.lblWichtigeLinks = new JLabel("Intigrierte Internetadressen:");
		this.contentPane.add(this.lblWichtigeLinks, "cell 0 12 4 1");
		
		this.lblPolitikthread = new JLabel("Politikthread:");
		this.contentPane.add(this.lblPolitikthread, "cell 1 13 3 1");
		
		this.textAreaPoli = new JTextArea();
		textAreaPoli.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.textAreaPoli.setBackground(UIManager.getColor("Panel.background"));
		this.textAreaPoli.setFocusable(false);
		this.textAreaPoli.setEditable(false);
		this.textAreaPoli.setLineWrap(true);
		this.textAreaPoli.setText(Einst.poliLink);
		this.contentPane.add(this.textAreaPoli, "cell 2 14 2 1,growx");
		
		this.lblDiskussionsthread = new JLabel("Diskussionsthread:");
		this.contentPane.add(this.lblDiskussionsthread, "cell 1 15 3 1");
		
		this.textAreaDisku = new JTextArea();
		textAreaDisku.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.textAreaDisku.setLineWrap(true);
		this.textAreaDisku.setFocusable(false);
		this.textAreaDisku.setEditable(false);
		this.textAreaDisku.setText(Einst.diskuLink);
		this.textAreaDisku.setBackground(UIManager.getColor("Panel.background"));
		this.contentPane.add(this.textAreaDisku, "cell 2 16 2 1,growx");
		
		this.lblWikocracyportal = new JLabel("Wikocracy-Portal:");
		this.contentPane.add(this.lblWikocracyportal, "cell 1 17 3 1");
		
		this.textAreaWikiPortal = new JTextArea();
		textAreaWikiPortal.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.textAreaWikiPortal.setLineWrap(true);
		this.textAreaWikiPortal.setFocusable(false);
		this.textAreaWikiPortal.setEditable(false);
		this.textAreaWikiPortal.setText(Einst.wikiLink);
		this.textAreaWikiPortal.setBackground(UIManager.getColor("Panel.background"));
		this.contentPane.add(this.textAreaWikiPortal, "cell 2 18 2 1,growx");
		
		this.lblWikiartikelDesPostwriters = new JLabel("Wikiartikel des PostWriters:");
		this.contentPane.add(this.lblWikiartikelDesPostwriters, "cell 1 19 3 1");
		
		this.textAreaWikiPW = new JTextArea();
		textAreaWikiPW.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.textAreaWikiPW.setLineWrap(true);
		this.textAreaWikiPW.setFocusable(false);
		this.textAreaWikiPW.setEditable(false);
		this.textAreaWikiPW.setText(Einst.pwrLink);
		this.textAreaWikiPW.setBackground(UIManager.getColor("Panel.background"));
		this.contentPane.add(this.textAreaWikiPW, "cell 2 20 2 1,growx");
		
		this.lblKontakt = new JLabel("Kontaktmöglichkeiten:");
		this.contentPane.add(this.lblKontakt, "cell 0 21 4 1");
		
		this.btnSchlieen = new JButton("Schließen");
		btnSchlieen.setMnemonic('s');
		this.btnSchlieen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				einst.out("Schließe Creditsfenster.");
			}
		});
		
		this.txtrImSimocracydiskussionsthreadIm = new JTextArea();
		this.txtrImSimocracydiskussionsthreadIm.setText("Im Simocracy-Diskussionsthread im SimForum, " +
				"im IRC-Channel #Simocracy auf irc.euirc.net oder auf der Diskussionsseite des " +
				"PostWriters im Wikocracy.");
		this.txtrImSimocracydiskussionsthreadIm.setWrapStyleWord(true);
		this.txtrImSimocracydiskussionsthreadIm.setLineWrap(true);
		this.txtrImSimocracydiskussionsthreadIm.setEditable(false);
		this.txtrImSimocracydiskussionsthreadIm.setFocusable(false);
		this.txtrImSimocracydiskussionsthreadIm.setBackground(UIManager.getColor("Panel.background"));
		this.contentPane.add(this.txtrImSimocracydiskussionsthreadIm, "cell 1 22 3 1,grow");
		this.contentPane.add(this.btnSchlieen, "cell 0 23 4 1,alignx center");
		

		// Lege MouseListener zum oeffnen der Links fest
		textAreaPoli.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				einst.oeffneLink(Einst.poliLink);
			}
		});
		textAreaDisku.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				einst.oeffneLink(Einst.diskuLink);
			}
		});
		textAreaWikiPortal.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				einst.oeffneLink(Einst.wikiLink);
			}
		});
		textAreaWikiPW.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				einst.oeffneLink(Einst.pwrLink);
			}
		});
		textAreaDatumsLink.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				einst.oeffneLink(Einst.datumsLink);
			}
		});
	}

}
