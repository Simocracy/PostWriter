package de.simocracy.postwriter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.simocracy.postwriter.fachklassen.*;

public class Helfer {
	
	////////////////////////////////////
	// Allgemeine kleine Helfer
	////////////////////////////////////
	
	// Woerter zaehlen und String-Ausgabe
	public static String zahl(String text){
		return String.valueOf(zahlI(text));
	}
	
	public static String zahl(JTextArea textArea){
		return String.valueOf(zahlI(textArea));
	}
	
	public static String zahl(JTextField textField){
		return String.valueOf(zahlI(textField));
	}
	
	public static String zahl(JList<String> list){
		return String.valueOf(zahlI(list));
	}
	
	// Woerter zaehlen und int-Ausgabe
	public static int zahlI(String text){
		return text.replaceAll("\\s+", " ").split(" ").length;
	}

	public static int zahlI(JTextArea textArea){
		return textArea.getText().replaceAll("\\s+", " ").split(" ").length;
	}

	public static int zahlI(JTextField textField){
		return textField.getText().replaceAll("\\s+", " ").split(" ").length;
	}

	public static int zahlI(JList<String> list){
		return list.getSelectedValue().toString().replaceAll("\\s+", " ").split(" ").length;
	}
	
	// Datum einlesen
	public static String datumL(String datum){
		String[] d = datum.split(" ");
		return d[0] + Einst.nl + d[1] + " Uhr";
	}
	
	// Datum einlesen ohne Zeilenumbruch
	public static String datumLn(String datum){
		String[] d = datum.split(" ");
		return d[0] + ", " + d[1] + " Uhr";
	}
	
	// Datum speichern
	public static String datumS(String datum){
		String[] d = datum.split(Einst.nl);
		return d[0] + " " + d[1].substring(0,d[1].length()-4);
	}
	
	// PW mit MD5 hashen
	public static String hashMD5(JPasswordField jpf) throws Exception {
		String str = "";
		
		for(int i = 0; i < jpf.getPassword().length; i++) {
			str += jpf.getPassword()[i];
		}
		
		return hashMD5(str.getBytes());
	}
	
	public static String hashMD5(byte[] text) throws Exception {
		MessageDigest digester = MessageDigest.getInstance("MD5");
		digester.update(text);
		String md5 = new BigInteger(1, digester.digest()).toString(16);
		while(md5.length() < 32) {
			md5 = "0" + md5;
		}
	
		return md5;
	}
	
	// " und ' ersetzen
	/**
	 * @param text
	 * @return Text mit " und '
	 */
	public static String varText(String text){
		text = text.replaceAll(Einst.vanfuehrungszeichen, "\"");
		text = text.replaceAll(Einst.vapostroph, "'");
		
		return text;
	}
	
	/**
	 * @param text
	 * @return Text mit Variablen
	 */
	public static String textVar(String text){
		text = text.replaceAll("\"", Einst.vanfuehrungszeichen);
		text = text.replaceAll("'", Einst.vapostroph);
		
		return text;
	}
	
	/**
	 * @param textField
	 * @return Text mit Variablen
	 */
	public static String textVar(JTextField textField){
		String text = textVar(textField.getText());
		
		return text;
	}
	
	/**
	 * @param textArea
	 * @return Text mit Variablen
	 */
	public static String textVar(JTextArea textArea){
		String text = textVar(textArea.getText());
		
		return text;
	}
	
	/**
	 * @param comboBox
	 * @return Text mit Variablen
	 */
	public static String textVar(JComboBox<?> comboBox){
		String text = textVar(comboBox.getSelectedItem().toString());
		
		return text;
	}
	
	/**
	 * @param list
	 * @return Text mit Variablen
	 */
	public static String textVar(JList<?> list){
		String text = textVar(list.getSelectedValue().toString());
		
		return text;
	}
	
	////////////////////////////////////
	// ArrayList Suche
	////////////////////////////////////
	
	// ArrayList Post durchsuchen
	public static int sucheALPostPid(ArrayList<Post> al, int ziel){
		int a = -1;
		int ergebnis;
		
		do{
			a++;
			
			if(a==al.size()){
				// Abbruch der Suche
				ergebnis = ziel;
				a = -1;
			}else{
				// Suche
				ergebnis = al.get(a).getId();
			}
		}
		while(ziel != ergebnis);
		
		return a;
	}
	
	// ArrayList Bereich durchsuchen
	public static int sucheALBereichName(ArrayList<Bereich> al, String ziel){
		int a = -1;
		String ergebnis;
		
		do{
			a++;
			
			if(a==al.size()){
				// Abbruch der Suche
				ergebnis = ziel;
				a = -1;
			}else{
				// Suche
				ergebnis = al.get(a).getName();
			}
		}
		while(!ziel.equals(ergebnis));
		
		return a;
	}
	
	// ArrayList Thema durchsuchen
	public static int sucheALThemaName(ArrayList<Thema> al, String ziel){
		int a = -1;
		String ergebnis;
		
		do{
			a++;
			
			if(a==al.size()){
				// Abbruch der Suche
				ergebnis = ziel;
				a = -1;
			}else{
				// Suche
				ergebnis = al.get(a).getName();
			}
		}
		while(!ziel.equals(ergebnis));
		
		return a;
	}
	
	public static int sucheALThemaBid(ArrayList<Thema> al, int ziel){
		int a = -1;
		int ergebnis;
		
		do{
			a++;
			
			if(a==al.size()){
				// Abbruch der Suche
				ergebnis = ziel;
				a = -1;
			}else{
				// Suche
				ergebnis = al.get(a).getBid();
			}
		}
		while(ziel != ergebnis);
		
		return a;
	}
	
	// ArrayList Kurznachrichten durchsuchen
	public static int sucheALKurzInhalt(ArrayList<Kurznachricht> al, String ziel){
		int a = -1;
		String ergebnis;
		
		do{
			a++;
			
			if(a==al.size()){
				// Abbruch der Suche
				ergebnis = ziel;
				a = -1;
			}else{
				// Suche
				ergebnis = al.get(a).getInhalt();
			}
		}
		while(!ziel.equals(ergebnis));
		
		return a;
	}
	
	public static int sucheALKurzPid(ArrayList<Kurznachricht> al, int ziel){
		int a = -1;
		int ergebnis;
		
		do{
			a++;
			
			if(a==al.size()){
				// Abbruch der Suche
				ergebnis = ziel;
				a = -1;
			}else{
				// Suche
				ergebnis = al.get(a).getPid();
			}
		}
		while(ziel != ergebnis);
		
		return a;
	}
	
	// ArrayList Header durchsuchen
	public static int sucheALHeaderName(ArrayList<Header> al, String ziel){
		int a = -1;
		String ergebnis;
		
		do{
			a++;
			
			if(a==al.size()){
				// Abbruch der Suche
				ergebnis = ziel;
				a = -1;
			}else{
				// Suche
				ergebnis = al.get(a).getName();
			}
		}
		while(!ziel.equals(ergebnis));
		
		return a;
	}
	
	public static int sucheALHeaderHid(ArrayList<Header> al, int ziel){
		int a = -1;
		int ergebnis;
		
		do{
			a++;
			
			if(a==al.size()){
				// Abbruch der Suche
				ergebnis = ziel;
				a = -1;
			}else{
				// Suche
				ergebnis = al.get(a).getId();
			}
		}
		while(ziel != ergebnis);
		
		return a;
	}
	
	// ArrayList Footer durchsuchen
	public static int sucheALFooterName(ArrayList<Footer> al, String ziel){
		int a = -1;
		String ergebnis;
		
		do{
			a++;
			
			if(a==al.size()){
				// Abbruch der Suche
				ergebnis = ziel;
				a = -1;
			}else{
				// Suche
				ergebnis = al.get(a).getName();
			}
		}
		while(!ziel.equals(ergebnis));
		
		return a;
	}

	public static int sucheALFooterFid(ArrayList<Footer> al, int ziel){
		int a = -1;
		int ergebnis;
		
		do{
			a++;
			
			if(a==al.size()){
				// Abbruch der Suche
				ergebnis = ziel;
				a = -1;
			}else{
				// Suche
				ergebnis = al.get(a).getId();
			}
		}
		while(ziel != ergebnis);
		
		return a;
	}
	
	// ArrayList Standardtext durchsuchen
	public static int sucheALStandardtextName(ArrayList<Standardtext> al, String ziel){
		int a = -1;
		String ergebnis;
		
		do{
			a++;
			
			if(a==al.size()){
				// Abbruch der Suche
				ergebnis = ziel;
				a = -1;
			}else{
				// Suche
				ergebnis = al.get(a).getName();
			}
		}
		while(!ziel.equals(ergebnis));
		
		return a;
	}
	
	// ArrayList Themenvorlage durchsuchen
	public static int sucheALTVorlageName(ArrayList<Themenvorlage> al, String ziel){
		int a = -1;
		String ergebnis;
		
		do{
			a++;
			
			if(a==al.size()){
				// Abbruch der Suche
				ergebnis = ziel;
				a = -1;
			}else{
				// Suche
				ergebnis = al.get(a).getName();
			}
		}
		while(!ziel.equals(ergebnis));
		
		return a;
	}
	
}
