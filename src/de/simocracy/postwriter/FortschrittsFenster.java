package de.simocracy.postwriter;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

public class FortschrittsFenster extends Fenster {

	private static final long serialVersionUID = -3658270323548870823L;
	private JLabel lblNachricht;
	private JProgressBar progressBar;
	private JLabel lblAktuelleAktion;
	private JTextPane txtpnAktion;

	public FortschrittsFenster(String titel, String nachricht, String initAktion) {
		super(140, 300);
		
		setResizable(false);
		setTitle(titel);
		getContentPane().setLayout(new MigLayout("", "[294px,grow]", "[][grow][][]"));
		
		lblNachricht = new JLabel(nachricht);
		getContentPane().add(lblNachricht, "cell 0 0,growx");
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		getContentPane().add(progressBar, "cell 0 1,growx,aligny center");
		
		lblAktuelleAktion = new JLabel("Aktuelle Aktion:");
		getContentPane().add(lblAktuelleAktion, "cell 0 2,growx");
		
		txtpnAktion = new JTextPane();
		txtpnAktion.setFocusable(false);
		txtpnAktion.setEditable(false);
		txtpnAktion.setOpaque(false);
		txtpnAktion.setText(initAktion);
		getContentPane().add(txtpnAktion, "cell 0 3,growx");
		
//		paint(getGraphics());
	}
	
	/**
	 * Legt Text der aktuellen Aktion fest
	 * @param text
	 */
	public void setAktion(String text) {
		txtpnAktion.setText(text);
//		paint(getGraphics());
	}
	
	/**
	 * Gibt den aktuellen Fortschrittswert zurück
	 * @return
	 */
	public int getFortschritt() {
		return progressBar.getValue();
	}
	
	/**
	 * Legt Fortschrittswert auf einen bestimmten Wert fest
	 * @param i
	 */
	public void setFortschritt(int i) {
		progressBar.setValue(i);
//		paint(getGraphics());
//		repaint();
	}
	
	/**
	 * Erhöht Fortschrittswert um 1
	 */
	public void setFortschritt() {
		progressBar.setValue(progressBar.getValue() + 1);
	}
	
	/**
	 * Legt Aktionstext und Fortschrittswert fest
	 * @param text
	 * @param i
	 */
	public void setFortschrit(String text, int i) {
		setAktion(text);
		setFortschritt(i);
	}
	
	/**
	 * Legt maximalen Fortschrittswert (100 % Fortschritt) fest
	 * @param i
	 */
	public void setMaximum(int i) {
		progressBar.setMaximum(i);
	}
}
