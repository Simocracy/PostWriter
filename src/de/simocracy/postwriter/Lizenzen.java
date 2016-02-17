package de.simocracy.postwriter;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Lizenzen extends JDialog {

	private static final long serialVersionUID = 3203263130336416651L;
	private JPanel contentPane;
	private JLabel lblLizenzDesXerial;
	private JLabel lblHomepage;
	private JLabel lblInternetseite;
	private JScrollPane scrollPane;
	private JTextPane txtrLizenzarea;
	private JButton btnSchlieen;
	private JLabel lblAutor;

	public Lizenzen(final Einst einst, String name, String autor, String url, String dateiname) {
		int bildHoehe = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		int bildBreite = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int hoehe = 600;
		int breite = 530;

		setTitle("PostWriter - Lizenzangaben");
		setResizable(false);
		setModal(true);
		setBounds((bildBreite-breite)/2, (bildHoehe-hoehe)/2, breite, hoehe);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Einst.icon);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[][][][grow][]"));
		
		lblLizenzDesXerial = new JLabel("Lizenzangaben von: " + name);
		contentPane.add(lblLizenzDesXerial, "cell 0 0");
		
		lblAutor = new JLabel("Autor: " + autor);
		contentPane.add(lblAutor, "cell 0 1");
		
		lblHomepage = new JLabel("Homepage:");
		contentPane.add(lblHomepage, "flowx,cell 0 2");
		
		lblInternetseite = new JLabel(url);
		lblInternetseite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				einst.oeffneLink(lblInternetseite.getText());
			}
		});
		contentPane.add(lblInternetseite, "cell 0 2");
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 0 3,grow");
		
		txtrLizenzarea = new JTextPane();
		txtrLizenzarea.setEditable(false);
		txtrLizenzarea.setFocusable(false);
		scrollPane.setViewportView(txtrLizenzarea);

		StringBuilder sb = new StringBuilder();

		try {
			InputStream in = getClass().getResourceAsStream(
				"/de/simocracy/postwriter/ressourcen/" + dateiname + ".txt");
		
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String read;
			
			read = br.readLine();
			while(read != null) {
			    sb.append(read + "\n");
			    read = br.readLine();
			}
			txtrLizenzarea.setText(sb.toString());
		} catch (Exception e) {
			txtrLizenzarea.setText("Lizenzdatei konnte nicht gelesen werden.");
		}
		txtrLizenzarea.setCaretPosition(0);
		
		btnSchlieen = new JButton("Schließen");
		btnSchlieen.setMnemonic('s');
		btnSchlieen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		contentPane.add(btnSchlieen, "cell 0 4,alignx center");
		
		setVisible(true);
	}

}
