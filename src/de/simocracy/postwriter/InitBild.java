package de.simocracy.postwriter;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JWindow;

public class InitBild extends JWindow {

	private static final long serialVersionUID = 8636880570476868902L;
	
	public InitBild() {
		int bildHoehe = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		int bildBreite = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int hoehe = 74;
		int breite = 210;
		setBounds((bildBreite-breite)/2, (bildHoehe-hoehe)/2, breite, hoehe);
		
		getContentPane().setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("");
			lblNewLabel.setBounds(5, 5, 200, 64);
			getContentPane().add(lblNewLabel);
			lblNewLabel.setIcon(new ImageIcon(getClass().getResource(
					"/de/simocracy/postwriter/ressourcen/PWRLogo.png")));
		}
	}
}