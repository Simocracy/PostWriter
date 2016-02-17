package de.simocracy.postwriter.postexport;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import java.awt.datatransfer.StringSelection;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.awt.Cursor;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PostExportBaum extends Fenster {

	private static final long serialVersionUID = 655184367888019171L;
	private JPanel contentPane;
	private Einst einst;
	private Hauptmenue hm;
	
	private ArrayList<Post> alPost;
	private ArrayList<Bereich> alBereich;
	private ArrayList<Thema> alThema;
	private String postURL;
	
	private JPanel panelInfos;
	private JLabel lblInformationen;
	private JButton btnPosten;
	private JButton btnAlsTxt;
	private JButton btnSchlieen;
	private JTextPane txtrBereiche;
	private JTextPane txtrAnzahlbereiche;
	private JTextPane txtrThemen;
	private JTextPane txtrAnzahlthemen;
	private JTextPane txtrKurzn;
	private JTextPane txtrAnzahlkurzn;
	private JTextPane txtrPostdatum;
	private JTextPane txtrDatum;
	private JTextPane txtrAnzahlWrter;
	private JTextPane txtrWrter;
	private JLabel lblOptionen;
	private JCheckBox chckbxAutoumbruch;
	private JButton btnClicktopost;
	private JCheckBox chckbxTopAnf;
	private JTextPane txtrAnzahlZeichen;
	private JTextPane txtrZeichen;
	private JButton btnEditoransicht;
	private JTree tree;
	private JPanel panelInhalt;
	private JScrollPane scrollPaneTree;
	private JScrollPane scrollPane;
	private JTextPane txtrText;
	private JLabel lblThemenname;
	private JLabel lblSucheNach;
	private JTextField txtSuchbegriff;
	private JButton btnNopaste;
	private boolean kurzGefunden;
	private JButton btnPostOeffnen;

	public PostExportBaum(Einst einst, Hauptmenue hm) {
		super(700,730);
		this.einst = einst;
		this.hm = hm;
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[120:120:120,grow,fill][grow]", "[][][][][][][grow][][][][][][][]"));
		
		lblSucheNach = new JLabel("Suche nach:");
		contentPane.add(lblSucheNach, "cell 0 0");
		
		txtSuchbegriff = new JTextField();
		txtSuchbegriff.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						suchen();
					}
				}).start();
			}
		});
		contentPane.add(txtSuchbegriff, "cell 1 0,growx");
		txtSuchbegriff.setColumns(10);
		
		lblInformationen = new JLabel("Informationen:");
		contentPane.add(lblInformationen, "cell 0 1");
		
		panelInhalt = new JPanel();
		contentPane.add(panelInhalt, "cell 1 1 1 13,grow");
		panelInhalt.setLayout(new GridLayout(2, 1, 0, 0));
		
		scrollPaneTree = new JScrollPane();
		panelInhalt.add(scrollPaneTree);
		
		tree = new JTree();
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				try {
					themenauswahl(e);
				} catch (Exception e2) {
				}
			}
		});
		scrollPaneTree.setViewportView(tree);
		
		tree.setToggleClickCount(1);
		tree.setRootVisible(false);
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setViewportBorder(UIManager.getBorder("ScrollPane.border"));
		panelInhalt.add(scrollPane);
		
		txtrText = new JTextPane();
		txtrText.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		txtrText.setEditable(false);
		scrollPane.setViewportView(txtrText);
		
		lblThemenname = new JLabel("");
		scrollPane.setColumnHeaderView(lblThemenname);
		
		panelInfos = new JPanel();
		contentPane.add(panelInfos, "cell 0 2,aligny top");
		panelInfos.setLayout(new MigLayout("", "[10:10:10,grow][grow][grow]", "[][][][][][][][grow][grow]"));
		
		txtrBereiche = new JTextPane();
		txtrBereiche.setEnabled(false);
		txtrBereiche.setFocusable(false);
		txtrBereiche.setBackground(UIManager.getColor("Panel.background"));
		txtrBereiche.setEditable(false);
		txtrBereiche.setText("Bereiche:");
		panelInfos.add(txtrBereiche, "cell 0 0 2 1");
		
		txtrAnzahlbereiche = new JTextPane();
		txtrAnzahlbereiche.setEnabled(false);
		txtrAnzahlbereiche.setBackground(UIManager.getColor("Panel.background"));
		txtrAnzahlbereiche.setEditable(false);
		txtrAnzahlbereiche.setFocusable(false);
		panelInfos.add(txtrAnzahlbereiche, "cell 2 0");
		
		txtrThemen = new JTextPane();
		txtrThemen.setEnabled(false);
		txtrThemen.setEditable(false);
		txtrThemen.setFocusable(false);
		txtrThemen.setBackground(UIManager.getColor("Panel.background"));
		txtrThemen.setText("Themen:");
		panelInfos.add(txtrThemen, "cell 0 1 2 1");
		
		txtrAnzahlthemen = new JTextPane();
		txtrAnzahlthemen.setEnabled(false);
		txtrAnzahlthemen.setFocusable(false);
		txtrAnzahlthemen.setEditable(false);
		txtrAnzahlthemen.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrAnzahlthemen, "cell 2 1");
		
		txtrKurzn = new JTextPane();
		txtrKurzn.setEnabled(false);
		txtrKurzn.setEditable(false);
		txtrKurzn.setFocusable(false);
		txtrKurzn.setBackground(UIManager.getColor("Panel.background"));
		txtrKurzn.setText("Kurzn.:");
		panelInfos.add(txtrKurzn, "cell 0 2 2 1");
		
		txtrAnzahlkurzn = new JTextPane();
		txtrAnzahlkurzn.setEnabled(false);
		txtrAnzahlkurzn.setFocusable(false);
		txtrAnzahlkurzn.setEditable(false);
		txtrAnzahlkurzn.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrAnzahlkurzn, "cell 2 2");
		
		txtrPostdatum = new JTextPane();
		txtrPostdatum.setText("Postdatum:");
		txtrPostdatum.setFocusable(false);
		txtrPostdatum.setEditable(false);
		txtrPostdatum.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrPostdatum, "cell 0 3 3 1");
		
		txtrDatum = new JTextPane();
		txtrDatum.setFocusable(false);
		txtrDatum.setEditable(false);
		txtrDatum.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrDatum, "cell 1 4 2 1");
		
		txtrAnzahlWrter = new JTextPane();
		txtrAnzahlWrter.setToolTipText("");
		txtrAnzahlWrter.setEditable(false);
		txtrAnzahlWrter.setFocusable(false);
		txtrAnzahlWrter.setBackground(UIManager.getColor("Panel.background"));
		txtrAnzahlWrter.setText("Anzahl Wörter:");
		panelInfos.add(txtrAnzahlWrter, "cell 0 5 3 1");
		
		txtrWrter = new JTextPane();
		txtrWrter.setToolTipText("Anzahl der Wörter des Posts (ungefährer Wert)");
		txtrWrter.setFocusable(false);
		txtrWrter.setEditable(false);
		txtrWrter.setBackground(UIManager.getColor("Panel.background"));
		panelInfos.add(txtrWrter, "cell 1 6 2 1");
		
		txtrAnzahlZeichen = new JTextPane();
		txtrAnzahlZeichen.setToolTipText("");
		txtrAnzahlZeichen.setText("Anzahl Zeichen:");
		txtrAnzahlZeichen.setFocusable(false);
		txtrAnzahlZeichen.setEditable(false);
		txtrAnzahlZeichen.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrAnzahlZeichen, "cell 0 7 3 1,grow");
		
		txtrZeichen = new JTextPane();
		txtrZeichen.setToolTipText("");
		txtrZeichen.setFocusable(false);
		txtrZeichen.setEditable(false);
		txtrZeichen.setBackground(UIManager.getColor("Button.background"));
		panelInfos.add(txtrZeichen, "cell 1 8 2 1,grow");
		
		btnPosten = new JButton("Posten");
		btnPosten.setMnemonic('p');
		btnPosten.setEnabled(false);
		btnPosten.setToolTipText("Posten oder Exportieren nur in Editoransicht möglich");
		
		lblOptionen = new JLabel("Optionen:");
		contentPane.add(lblOptionen, "cell 0 3");
		
		chckbxAutoumbruch = new JCheckBox("Autoumbruch");
		chckbxAutoumbruch.setMnemonic('a');
		chckbxAutoumbruch.setEnabled(false);
		contentPane.add(chckbxAutoumbruch, "cell 0 4");
		
		chckbxTopAnf = new JCheckBox("Top. Anf.");
		chckbxTopAnf.setMnemonic('o');
		contentPane.add(chckbxTopAnf, "cell 0 5");
		chckbxTopAnf.setToolTipText("Topografische Anführungszeichen");
		chckbxTopAnf.setSelected(einst.isTopo());
		
		btnPostOeffnen = new JButton("Post öffnen");
		btnPostOeffnen.setEnabled(false);
		btnPostOeffnen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				postOeffnen();
			}
		});
		btnPostOeffnen.setToolTipText("Postet den Post mit einem Mausklick im Politikthread");
		btnPostOeffnen.setMnemonic('ö');
		contentPane.add(btnPostOeffnen, "cell 0 7");
		
		btnClicktopost = new JButton("Click-to-Post");
		btnClicktopost.setMnemonic('c');
		btnClicktopost.setEnabled(false);
		btnClicktopost.setToolTipText("Posten oder Exportieren nur in Editoransicht möglich");
		contentPane.add(btnClicktopost, "cell 0 8");
		contentPane.add(btnPosten, "cell 0 9");
		
		btnAlsTxt = new JButton("In TXT");
		btnAlsTxt.setMnemonic('t');
		btnAlsTxt.setEnabled(false);
		btnAlsTxt.setToolTipText("Posten oder Exportieren nur in Editoransicht möglich");
		contentPane.add(btnAlsTxt, "cell 0 10");
		
		btnSchlieen = new JButton("Schließen");
		btnSchlieen.setMnemonic('s');
		btnSchlieen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				beenden();
			}
		});
		
		btnEditoransicht = new JButton("Editoransicht");
		btnEditoransicht.setMnemonic('e');
		btnEditoransicht.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editoransicht();
			}
		});
		
		btnNopaste = new JButton("Nopaste");
		btnNopaste.setMnemonic('n');
		btnNopaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				nopaste();
			}
		});
		btnNopaste.setToolTipText("<html>Kopiert den aktuellen Post in die Zwischenablage<br>und öffnet den in den Einstellungen eingestellte Nopaste-Dienst</html>");
		contentPane.add(btnNopaste, "cell 0 11");
		contentPane.add(btnEditoransicht, "cell 0 12");
		contentPane.add(btnSchlieen, "cell 0 13");

		// WindowListener zum Schliessen
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				beenden();
			}
		});
		
		// Checkbox Autoumbruch deaktivieren, falls keine Umbrueche in Einstellungen
		if(einst.getAutoumbruch() == 0){
			chckbxAutoumbruch.setSelected(false);
		}else{
			chckbxAutoumbruch.setSelected(true);
		}
		
		// ItemListener fuer Topo-Anfuehrungszeichen
		chckbxTopAnf.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				topo();
			}
		});

		// Posts und das Zeug aus DB holen
		alPost = einst.dao().sucheAllePostsMitArray();
		alBereich = einst.dao().sucheAlleBereiche();
		alThema = einst.dao().sucheAlleThemen();
		
		// Einlesen aus DB
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				einlesen("");
			}
		}).start();
		
		setVisible(true);
	}
	
	// Post öffnen
	private void postOeffnen() {
		einst.oeffneLink(postURL);
	}
	
	// Daten einlesen
	private void einlesen(final String begriff){
		// Fuellen vom Postbaum
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("JTree") {
				private static final long serialVersionUID = 1907351604833342654L;{
					
					DefaultMutableTreeNode posts;
					DefaultMutableTreeNode bereiche;
					int anzahl;
					boolean gefunden;
					
//					node_1 = new DefaultMutableTreeNode("colors");
//						node_1.add(new DefaultMutableTreeNode("blue"));
//						node_1.add(new DefaultMutableTreeNode("violet"));
//						node_1.add(new DefaultMutableTreeNode("red"));
//						node_1.add(new DefaultMutableTreeNode("yellow"));
//					add(node_1);
					
					for(int i = 1; i < alPost.size(); i++) {
						// Postinfo auslesen
						gefunden = false;
						
						if(alPost.get(i).getDatum().equals("")) {
							posts = new DefaultMutableTreeNode("ID: " +
									String.valueOf(alPost.get(i).getId()));
						}
						else{
							posts = new DefaultMutableTreeNode("ID: " + String.valueOf(
									alPost.get(i).getId()) + " - Datum: " +
									Helfer.datumLn(alPost.get(i).getDatum()));
						}
						
						final int postID = i;
						kurzGefunden = false;
						// Text in Kurznachrichten suchen und in eigenem Thread starten
						Thread threadKurzSuche = new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(alPost.get(postID).getKurznachrichten() != null &&
										alPost.get(postID).getKurznachrichtenSize() > 0) {
									for(int u = 0; u < alPost.get(postID).getKurznachrichtenSize(); u++) {
										if(alPost.get(postID).getKurznachricht(u).
												getInhalt().toLowerCase().contains(begriff)) {
											kurzGefunden = true;
										}
									}
								}
							}
						});
						threadKurzSuche.start();
						
						// Post in Baum setzen
						add(posts);
						
						// Wichtigkeit der Bereiche durchgehen
						for(int wb = 10; wb >= 0; wb--) {
							
							// Einzelne Bereiche in Schleife abarbeiten
							for(int b = 0; b < alBereich.size(); b++) {
								
								// Nur abarbeiten, wenn Anzahl Themen > 0 und Wichtigkeit passen
								if(wb == alBereich.get(b).getWkat() &&
										alPost.get(i).getThemen() != null &&
										alPost.get(i).getThemaSize() > 0) {
									
									// Pruefe bid zwecks Ueberschrift
									if(b != 0) {
										
										// Wenns Bereich gibt, Ueberschrift lesen
										bereiche = new DefaultMutableTreeNode(
												Helfer.varText(alBereich.get(b).getName()));
										
										// Themen einbauen
										anzahl = einbauThemen(bereiche, alBereich.get(b), i, begriff);
										
										// Bereich in Baum einsetzen
										if(anzahl > 0) {
											posts.add(bereiche);
											gefunden = true;
										}
									}
									
									// Falls bid == 0
									else {
										
										// Themen einbauen
										anzahl = einbauThemen(posts, alBereich.get(b), i, begriff);
										
										if(anzahl > 0) {
											gefunden = true;
										}
									}
								}
							}
						}
						
						// Kurznachrichten nur Abarbeiten, wenns welche fuer den Post gibt
						if(alPost.get(i).getKurznachrichten() != null &&
								alPost.get(i).getKurznachrichtenSize() > 0) {
							
							// Ueberschrift einbauen
							bereiche = new DefaultMutableTreeNode("Kurznachrichten");
							
							// Prüfen, ob kurznachrichten gefunden wurden und bei bedarf einbauen
							try {
								threadKurzSuche.join();
							} catch (Exception e) {
								// TODO: handle exception
								kurzGefunden=true;
							}
							
							if(kurzGefunden) {
								posts.add(bereiche);
							}
						}
						// Kurznachrichten Ende
						
						// Prüfen, ob Post eingebaut werden soll
						
						if(!gefunden && !kurzGefunden) {
							remove(posts);
						}
					}
					// Post Ende
				}
			}
		));
	}
	
	// Themeneinbau in Baum
	private int einbauThemen(DefaultMutableTreeNode bauelement, Bereich b, int pid, String begriff) {
		DefaultMutableTreeNode themen;
//		DefaultMutableTreeNode text;
		int anzahl = 0;
		
		// Wichtigkeit fuer Themen abarbeiten
		for(int wt = 10; wt >= 0; wt--) {
			
			// Themen in Schleife abarbeiten
			for(int t = 0; t < alPost.get(pid).getThemaSize(); t++) {
				
				// Nur abarbeiten, wenn Bereich und Wichtigkeit passen
				if(wt == alPost.get(pid).getThema(t).getWkat() &&
						alPost.get(pid).getThema(t).getBid() == b.getId()){
					
					// Ueberschrift auslesen
					themen = new DefaultMutableTreeNode(Helfer.varText(alPost.get(pid).getThema(t).getName()));
					
					// Text auslesen
//					text = new DefaultMutableTreeNode(ersetzen(alPost.get(pid).getThema(t).getInhalt()));
					
					// Text und Inhalt in Baum einsetzen, wenn begrifff enthalten
					String name = alPost.get(pid).getThema(t).getName().toLowerCase();
					String inhalt = alPost.get(pid).getThema(t).getInhalt().toLowerCase();
					if(name.contains(begriff) ||
							inhalt.contains(begriff)) {
						bauelement.add(themen);
						
						// Zaehler hochzaehlen
						anzahl++;
					}
//					themen.add(text);
					
				}
			}
		}
		
		// Anzahl Themen zurueckgeben
		return anzahl;
	}
	
	// Thementext in unterem TextArea anzeigen
	private void themenauswahl(TreeSelectionEvent e) throws Exception {
		// Suche Themenname
		String themenName = Helfer.varText(
				e.
				getNewLeadSelectionPath().
				getLastPathComponent().
				toString());
		
		// Auswahl zwischen Themen und Kurznachrichten
		if(!themenName.equals("Kurznachrichten")) {
			
			// Bearbeitung Themen
			
			// TID suchen
			int tid = Helfer.sucheALThemaName(alThema, themenName);
			
			// Weitermachen nur wenn ein Thema
			if(tid > 0) {
				
				// Thema suchen und vorbereiten
				String text = bbCodeRaus(Helfer.varText(alThema.get(tid).getInhalt()));
				
				// Text setzen
				lblThemenname.setText(themenName);
				txtrText.setText(text);
				txtrText.setCaretPosition(0);
				
				// Topo Anfuehrungszeichen setzen
				if(chckbxTopAnf.isSelected()) {
					topo();
				}
				
				// Einstellen der Infos
				txtrWrter.setText(Helfer.zahl(text));
				txtrZeichen.setText(String.valueOf(text.length()));
				txtrDatum.setText(Helfer.datumL(alPost.get(alThema.get(tid).getPid()).getDatum()));
				postURL = alPost.get(alThema.get(tid).getPid()).getUrl();
				btnPostOeffnen.setEnabled(true);
			}
		}
		
		else {
			
			// Bearbeitung Kurznachrichten
			
			// Pid suchen
			int pid = Integer.parseInt(e.getNewLeadSelectionPath().getPathComponent(1).toString().
					split(" - ")[0].replaceFirst("ID: ", ""));
			
			// Kurznachrichten nacheinander in String einbauen
			String text = "";
			for(int i = 0; i < alPost.get(pid).getKurznachrichtenSize()-1; i++) {
				text = text + "• " + Helfer.varText(alPost.get(pid).getKurznachricht(i).getInhalt()) + "\n";
			}
			
			// Letzte Kurznachricht einbauen
			text = text + "• " + Helfer.varText(alPost.get(pid).getKurznachricht(
					alPost.get(pid).getKurznachrichtenSize()-1).getInhalt());
			
			// Kurznachrichten in Anzeige einbauen
			lblThemenname.setText("Kurznachrichten");
			txtrText.setText(text);
			txtrText.setCaretPosition(0);
			
			// Einstellen der Infos
			txtrWrter.setText(Helfer.zahl(text));
			txtrZeichen.setText(String.valueOf(text.length()));
			txtrDatum.setText(Helfer.datumL(alPost.get(pid).getDatum()));
			postURL = alPost.get(pid).getUrl();
			btnPostOeffnen.setEnabled(true);
		}
	}
	
	// Ein- und Ausbau von topografischen Anfuehrungszeichen
	private void topo(){
		if(chckbxTopAnf.isSelected()){
		// Einbau
			StringBuilder text = new StringBuilder(txtrText.getText());
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
			txtrText.setText(text.toString());
		}
		else{
		// Ausbau
			String text = txtrText.getText();
			
			// Ersetzen
			text = text.replaceAll(String.valueOf(Einst.vTopoA), "\"");
			text = text.replaceAll(String.valueOf(Einst.vTopoE), "\"");
			
			// Text wieder in Vorschau einfuegen
			txtrText.setText(text);
		}
	}
	
	// BB-Codes rauswerfen
	private String bbCodeRaus(String text){
		// Vorbereitungen
		StringBuilder sb = new StringBuilder(text);
		boolean hatBBGanz = false;
		int bbStart = 0;
		int bbEnde = 0;
		
		// Text zeichen fuer Zeichen von hinten abarbeiten
		for(int i = sb.length() - 1; i >= 0; i--) {
			
			// Pruefen, ob BB-Code endet
			if(sb.charAt(i) == ']') {
				// Wenn BB-Code endet
				bbEnde = i;
			}
			
			// Pruefen, ob BB-Code beginnt
			if(sb.charAt(i) == '[') {
				// Wenn BB-Code beginnt
				hatBBGanz = true;
				bbStart = i;
			}
			
			// Wenn BB-Code ganz ist, loeschen
			if(hatBBGanz) {
				// Eigentlicher Loeschvorgang
				sb.delete(bbStart, bbEnde+1);
				
				// Variable zuruecksetzen
				hatBBGanz = false;
			}
		}
		
		// StringBuilder sb zurueck zu String konvertieren
		text = sb.toString();
		
		return text;
	}
	
	// Posts nach Suchbegriff durchsuchen
	private void suchen(){
		// Suchbegriff einstellen
		String suchbegriff = Helfer.varText(txtSuchbegriff.getText());
		suchbegriff = suchbegriff.toLowerCase();
		
		einlesen(suchbegriff);
		
	}
	
	// Auf Nopaste posten
	private void nopaste() {
		einst.out("Post wird auf Nopaste-Seite gepostet.");
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
				new StringSelection(txtrText.getText()), null);
		einst.oeffneLink(einst.getNopasteSeite());
	}
	
	// Editoransicht aufrufen
	private void editoransicht(){
		dispose();
		new PostExport(einst, hm);
	}
	
	// Fenster schliessen
	private void beenden(){
		einst.out("Schließe Postexporter.");
		dispose();
		hm.setVisible(true);
	}
}
