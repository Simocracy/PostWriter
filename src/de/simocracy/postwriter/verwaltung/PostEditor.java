package de.simocracy.postwriter.verwaltung;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class PostEditor extends Fenster {

	private static final long serialVersionUID = 5707801616860298763L;
	private JPanel contentPane;
	private Einst einst;
	private Hauptmenue hm;
	private ArrayList<Post> alPost;
	private Post post;
	private MouseAdapter mlURL;
	private ItemListener ilAuswahl;
	private JLabel lblPostId;
	private JComboBox<String> comboBoxPosts;
	private JLabel lblGepostetAm;
	private JLabel lblUrl;
	private JPanel panel;
	private JButton btnNeu;
	private JButton btnLschen;
	private JButton btnSchlieen;
	private JLabel lblAnzahlThemen;
	private JLabel lblAnzahlKurznachrichten;
	private JTextArea txtrThemen;
	private JTextArea txtrKurzN;
	private JTextArea textAreaGepostet;
	private JTextArea textAreaURL;
	
	public PostEditor(Einst einst, Hauptmenue hm) {
		super(190,600);
		this.einst = einst;
		this.hm = hm;
		
		setTitle("PostWriter - Postverwaltung");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][grow][][grow]", "[][grow][grow][grow][grow]"));
		
		lblPostId = new JLabel("Post ID:");
		contentPane.add(lblPostId, "cell 0 0");
		
		lblGepostetAm = new JLabel("Gepostet am:");
		contentPane.add(lblGepostetAm, "cell 0 1");
		
		textAreaGepostet = new JTextArea();
		textAreaGepostet.setToolTipText("Datum, wann der Post gepostet wurde");
		textAreaGepostet.setFocusable(false);
		textAreaGepostet.setEditable(false);
		textAreaGepostet.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(textAreaGepostet, "cell 1 1 3 1,grow");
		
		lblUrl = new JLabel("URL:");
		contentPane.add(lblUrl, "cell 0 2");
		
		textAreaURL = new JTextArea();
		textAreaURL.setToolTipText("URL des Posts im Politikthread");
		textAreaURL.setFocusable(false);
		textAreaURL.setEditable(false);
		textAreaURL.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(textAreaURL, "cell 1 2 3 1,grow");
		
		lblAnzahlThemen = new JLabel("Anzahl Themen:");
		contentPane.add(lblAnzahlThemen, "cell 0 3");
		
		txtrThemen = new JTextArea();
		txtrThemen.setToolTipText("");
		txtrThemen.setFocusable(false);
		txtrThemen.setEditable(false);
		txtrThemen.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(txtrThemen, "cell 1 3,grow");
		
		lblAnzahlKurznachrichten = new JLabel("Anzahl Kurznachrichten:");
		contentPane.add(lblAnzahlKurznachrichten, "cell 2 3");
		
		txtrKurzN = new JTextArea();
		txtrKurzN.setToolTipText("");
		txtrKurzN.setFocusable(false);
		txtrKurzN.setEditable(false);
		txtrKurzN.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(txtrKurzN, "cell 3 3,grow");
		
		comboBoxPosts = new JComboBox<String>();
		comboBoxPosts.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				int min = 0;
				int max = comboBoxPosts.getItemCount();
				int aktuell = comboBoxPosts.getSelectedIndex();
				int neu = aktuell + arg0.getWheelRotation();
				
				if(min <= neu && neu < max){
					comboBoxPosts.setSelectedIndex(neu);
				}
			}
		});
		contentPane.add(comboBoxPosts, "cell 1 0 3 1,growx");
		
		panel = new JPanel();
		contentPane.add(panel, "cell 0 4 4 1,grow");
		panel.setLayout(new MigLayout("", "[grow,fill]", "[]"));
		
		btnNeu = new JButton("Neu");
		btnNeu.setMnemonic('n');
		btnNeu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				postErstellen();
			}
		});
		panel.add(btnNeu, "flowx,cell 0 0");
		
		btnLschen = new JButton("Löschen");
		btnLschen.setMnemonic('l');
		btnLschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				postLoeschen();
			}
		});
		panel.add(btnLschen, "cell 0 0");
		
		btnSchlieen = new JButton("Schließen");
		btnSchlieen.setMnemonic('s');
		btnSchlieen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				beenden();
			}
		});
		panel.add(btnSchlieen, "cell 0 0");
		
		// Aktionen beim Schliessen vom Fenster
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				beenden();
			}
		});
		
		// Lege MouseListener zum oeffnen des Links fest
		mlURL = new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				linkOeffnen();
			}
		};
		
		// Lege ItemListener bei Auswahl fest
		ilAuswahl = new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(comboBoxPosts.getSelectedIndex()!=0 &&
						!comboBoxPosts.getSelectedItem().equals("Keine Posts verfügbar")){
					postAuswahl(Integer.parseInt(comboBoxPosts.getSelectedItem().
							toString().replaceFirst("ID: ", "")));
				}
				else{
					postAuswahl(0);
				}
			}
		};
		
		// Inhalt der ComboBox einlesen
		inhaltEinlesen();
		
		// Setze ItemListener fuer ComboBox
		comboBoxPosts.addItemListener(ilAuswahl);
		
		if(hm != null){
			setVisible(true);
		}
	}
	
	// Postlink oeffnen
	private void linkOeffnen(){
		einst.oeffneLink(post.getUrl());
	}
	
	// Combobox Inhalt einlesen
	private void inhaltEinlesen(){
		// Alter Inhalt rauswerfen
		comboBoxPosts.removeAllItems();
		
		// Neuer Inhalt einlesen
		comboBoxPosts.addItem("Zwischenspeicher");
		alPost = einst.dao().sucheAllePostsMitArray();
		for(int i = alPost.size()-1; i > 0; i--){
			comboBoxPosts.addItem("ID: " + String.valueOf(alPost.get(i).getId()));
		}
		
		// Erstauswahl
		if(alPost.size()>1){
			comboBoxPosts.setSelectedIndex(1);
			postAuswahl(Integer.parseInt(comboBoxPosts.getSelectedItem().toString().replaceFirst("ID: ", "")));
		}
		else{
			comboBoxPosts.addItem("Keine Posts verfügbar");
			comboBoxPosts.setSelectedItem("Keine Posts verfügbar");
		}
	}

	// Aktion bei Postauswahl
	private void postAuswahl(int pid){
		post = alPost.get(Helfer.sucheALPostPid(alPost, pid));
		
		// Setze Datum
		if(pid == 0){
			// Beim Ziwschenspeicher
			textAreaGepostet.setText("");
		}
		else{
			// Normaler Post
			if(post.getDatum().equals("")){
				textAreaGepostet.setText("Post wurde noch nicht gepostet");
			}else{
				textAreaGepostet.setText(Helfer.datumLn(post.getDatum()));
			}
		}
	
		// Setze URL
		if(pid == 0){
			// Beim Ziwschenspeicher
			textAreaURL.setText("");
			textAreaURL.removeMouseListener(mlURL);
		}
		else{
			// Normaler Post
			if(post.getUrl().equals("")){
				textAreaURL.setText("Post wurde noch nicht gepostet");
				textAreaURL.removeMouseListener(mlURL);
			}else{
				textAreaURL.setText(post.getUrl());
				textAreaURL.addMouseListener(mlURL);
			}
		}
		
		// Setze Groessenangaben
		if(pid != 0){
			txtrThemen.setText(String.valueOf(post.getThemaSize()));
			txtrKurzN.setText(String.valueOf(post.getKurznachrichtenSize()));
		}
		else{
			txtrThemen.setText(String.valueOf(post.getThemaSize()-1));
			txtrKurzN.setText(String.valueOf(post.getKurznachrichtenSize()-1));
		}
	}

	// Post erstellen
	public void postErstellen(){
		// Infos fuer neuen Post suchen
		post = new Post();
		post.setId(alPost.size());
		
		// Post speichern
		einst.dao().erfassePost(post);
		
		// Posts neu einlesen
		comboBoxPosts.removeItemListener(ilAuswahl);
		inhaltEinlesen();
		comboBoxPosts.addItemListener(ilAuswahl);
	}
	
	// Post loeschen
	private void postLoeschen(){
		// Pruefe, ob es posts gibt
		if(comboBoxPosts.getSelectedItem().equals("Keine Posts verfügbar")){
			JOptionPane.showMessageDialog(null, "Keine Posts zum Löschen in der Datenbank.",
					"Keine Posts verfügbar", JOptionPane.ERROR_MESSAGE);
		}
		else{
			// Suche Post in DB
			int pid = Integer.parseInt(comboBoxPosts.getSelectedItem().toString().replaceFirst("ID: ", ""));
			post = alPost.get(Helfer.sucheALPostPid(alPost, pid));
			
			// Pruefe, ob post bereits gepostet wurde
			if(!post.getDatum().equals("")){
				JOptionPane.showMessageDialog(null, "Der ausgewählte Post wurde bereits im Politikthread " +
						"gepostet und kann daher nicht mehr gelöscht werden.",
						"Post bereits gepostet", JOptionPane.ERROR_MESSAGE);
			}
			
			// Pruefe, ob Themen oder Kurznachrichten existeiren, wenn Ja, dann Error
			else if(post.getThemaSize() != 0 && post.getKurznachrichtenSize() != 0){
				JOptionPane.showMessageDialog(null, "Der ausgewählte Post enthält Themen und/oder Kurznachrichten" +
						"und kann daher nicht gelöscht werden.", "Post enthält Text", JOptionPane.ERROR_MESSAGE);
			}
			
			// loeschen
			else{
				einst.dao().loeschePost(pid);
				einst.out("Post löschen abgeschlossen. Aktualisiere IDs.");
				
				// Werfe alten Post aus dem Array
				alPost.remove(Helfer.sucheALPostPid(alPost, pid));

				// bearbeite Posts
				for (int i = post.getId(); i < alPost.size(); i++){
					einst.dao().aenderePostPid(alPost.get(i).getId(), i);
					
					// Bearbeite Themen
					for(int j = 0; j < alPost.get(i).getThemaSize(); j++){
						einst.dao().aendereThemaPid(alPost.get(i).getThema(j).getPid(), i);
					}
					
					// Bearbeite Kurznachrichten
					for(int k = 0; k < alPost.get(i).getKurznachrichtenSize(); k++){
						einst.dao().aendereKurznachrichtPid(alPost.get(i).getKurznachricht(k).getPid(), i);
					}
				}
				
				einst.out("Aktualisierung der IDs abgeschlossen.");
				
				// Posts neu einlesen
				inhaltEinlesen();
			}
		}
	}
	
	// Aktion beim Schließen
	private void beenden(){
		einst.out("Schließe Posteditor.");
		dispose();
		hm.setVisible(true);
	}

}
