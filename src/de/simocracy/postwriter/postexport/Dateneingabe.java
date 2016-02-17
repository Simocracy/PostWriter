package de.simocracy.postwriter.postexport;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.PostEditor;
import net.miginfocom.swing.MigLayout;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;

public class Dateneingabe extends JDialog {
	
	static final long serialVersionUID = -3251125554900003719L;
	private JPanel contentPane;
	private Einst einst;
	private Post post;
	private Hauptmenue hm;
	private PostExport pe;
	private JTextArea txtrDerAktuellePost;
	private JLabel lblUrl;
	private JTextField txtUrleingabe;
	private JPanel panelButtons;
	private JButton btnInZwischenablageKopieren;
	private JButton btnSpeichern;
	private JButton btnAbbrechen;

	public Dateneingabe(Einst einst, Hauptmenue hm, Post post, PostExport pe) {
		this.einst = einst;
		this.hm = hm;
		this.post = post;
		this.pe = pe;
		
		setTitle("PostWriter - Postexporter");
		int bildHoehe = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		int bildBreite = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int hoehe = 300;
		int breite = 400;

		setBounds((bildBreite-breite)/2, (bildHoehe-hoehe)/2, breite, hoehe);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setIconImage(Einst.icon);
		setModal(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][grow,fill]", "[][][]"));
		
		txtrDerAktuellePost = new JTextArea();
		txtrDerAktuellePost.setBackground(UIManager.getColor("Panel.background"));
		txtrDerAktuellePost.setFocusable(false);
		txtrDerAktuellePost.setEditable(false);
		txtrDerAktuellePost.setWrapStyleWord(true);
		txtrDerAktuellePost.setLineWrap(true);
		txtrDerAktuellePost.setText("Der aktuelle Post befindet sich in dem Zustand, wie er in der " +
				"Vorschau ist, nun in der Zwischenablage. Im SimForum muss daher der Post nur noch " +
				"eingefügt werden." + Einst.nl + Einst.nl + "Nachdem der Post gepostet wurde, " +
				"kann man hier die URL des Posts einfügen, um den Post im Forum schnell zu erreichen." +
				Einst.nl + Einst.nl + "Ein Klick auf den Button \"In Zwischenablage kopieren\" " +
				"kopiert den Post nochmal in die Zwischenablage.");
		contentPane.add(txtrDerAktuellePost, "cell 0 0 2 1,growx");
		
		lblUrl = new JLabel("URL:");
		contentPane.add(lblUrl, "cell 0 1");
		
		txtUrleingabe = new JTextField();
		contentPane.add(txtUrleingabe, "cell 1 1");
		txtUrleingabe.setColumns(10);
		
		panelButtons = new JPanel();
		contentPane.add(panelButtons, "cell 0 2 2 1,alignx center");
		panelButtons.setLayout(new MigLayout("", "[grow,fill]", "[][]"));
		
		btnInZwischenablageKopieren = new JButton("In Zwischenablage kopieren");
		btnInZwischenablageKopieren.setMnemonic('z');
		btnInZwischenablageKopieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				zwischenablage();
			}
		});
		panelButtons.add(btnInZwischenablageKopieren, "cell 0 0");
		
		btnSpeichern = new JButton("Als gepostet speichern");
		btnSpeichern.setMnemonic('s');
		btnSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				speichern();
			}
		});
		panelButtons.add(btnSpeichern, "flowx,cell 0 1");
		
		btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.setMnemonic('a');
		btnAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abbrechen();
			}
		});
		panelButtons.add(btnAbbrechen, "cell 0 1");

		// WindowListener zum Schliessen
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				abbrechen();
			}
		});
		
		setVisible(true);
	}
	
	// Post nochmal in Zwischenablge kopieren
	private void zwischenablage(){
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
				new StringSelection(pe.getVorschau()), null);
	}
	
	// Post als gepostet speichern
	private void speichern(){
		// Infos von Post vervollstaendigen
		post.setDatum(einst.getNeuesPostDatum());
		post.setUrl(txtUrleingabe.getText());
		
		// Post in DB speichern
		einst.dao().aenderePost(post);
		
		// Bei Bedarf neuen Post anlegen
		if(einst.isPostanlegen()){
			new PostEditor(einst, null).postErstellen();
		}
		
		// Fenster schliessen
		dispose();
		pe.dispose();
		hm.setVisible(true);
		einst.out("Post gepostet.");
	}
	
	// Posten abbrechen
	private void abbrechen(){
		dispose();
		einst.out("Posten abgebrochen.");
	}

}
