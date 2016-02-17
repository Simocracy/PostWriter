package de.simocracy.postwriter;

import javax.swing.JFrame;

public class Fenster extends JFrame {

	private static final long serialVersionUID = 4350614876117760301L;
	
	public Fenster(int hoehe, int breite) {
		int bildHoehe = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		int bildBreite = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;

		setBounds((bildBreite-breite)/2, (bildHoehe-hoehe)/2, breite, hoehe);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setIconImage(Einst.icon);
	}

}
