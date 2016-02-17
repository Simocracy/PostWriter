package de.simocracy.postwriter.postexport;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

public class PWAbfrage extends JPanel {

	private static final long serialVersionUID = 7472675177325096765L;
	private JLabel lblBittePasswortEingeben;
	private JPasswordField pwdPwf;

	public PWAbfrage() {
		setLayout(new MigLayout("", "[grow]", "[][]"));
		
		lblBittePasswortEingeben = new JLabel("Bitte Passwort des SimForum eingeben:");
		add(lblBittePasswortEingeben, "cell 0 0");
		
		pwdPwf = new JPasswordField();
		add(pwdPwf, "cell 0 1,growx");
	}
	
	protected JPasswordField getPW() {
		return pwdPwf;
	}

}
