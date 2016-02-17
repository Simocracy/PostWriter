package de.simocracy.postwriter.postexport;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Properties;
import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;
import de.simocracy.postwriter.fachklassen.*;
import de.simocracy.postwriter.verwaltung.PostEditor;

public class CtP extends FortschrittsFenster {

	private static final long serialVersionUID = 1801551685282627626L;
	private Einst einst;

	public CtP(final Einst einst) {
		super("Click-to-Post", "Bitte warten. Post wird gepostet...",
				"Initialisiere Click-to-Post...");
		this.einst = einst;
		setMaximum(77);
		setVisible(true);
	}
	
	protected void posten(PostExport pe, Post post) {
		// URL-Baukasten
		String sfUrl = "http://www.simforum.de/";
		String newreply = "newreply.php?";
		String sessionID = "";
		String sessionIDFull = "";
		
		// Poli
		String pid = "4411660";
		String tid = "166677";
		
		// Disku
//		String pid = "4410921";
//		String tid = "166676";
		
		// Poliantwort:
		// http://www.simforum.de/newreply.php?s=f5ece8befe9a119082829e1277539b7c&do=newreply&noquote=1&p=4412648
		// Mindestens 10 Zeichen
		// Diskuantwort:
		// http://www.simforum.de/newreply.php?s=1f92acf283fcff9e7430ce41ea8a46b1&do=newreply&noquote=1&p=4410921
		
		// Ständig verwendete Objekte
		boolean keinFehler = true;
		URL sf;
		HttpURLConnection simforum = null;
		BufferedWriter up;
		BufferedReader down;
		
		// Einloggen
		try {
			setAktion("Login: Suche Login-Daten...");
			
			// Passwort holen
			String btr = einst.dao().holePW();
			
			setFortschritt(3);
			
			// Falls Passwort nicht in DB, abfragen
			if(btr == null || btr.length() == 0){
			PWAbfrage pwf = new PWAbfrage();
			
			JOptionPane.showMessageDialog(null,
							pwf,
					"Passwort", JOptionPane.QUESTION_MESSAGE);
				
				// PW hashen
				btr = Helfer.hashMD5(pwf.getPW());
			}
			
			setFortschritt(5);
			
			// POST-Daten vorbereiten
			Properties postDaten = new Properties();
			postDaten.setProperty("vb_login_username", einst.getNutzer());
			postDaten.setProperty("vb_login_password", "");
			postDaten.setProperty("vb_login_md5password_utf", btr);
			postDaten.setProperty("vb_login_md5password", btr);
			postDaten.setProperty("securitytoken", "guest");
			postDaten.setProperty("s", "");
			postDaten.setProperty("do", "login");
			String httpPOST = genHttpPOST(postDaten);
			
			setFortschritt(7);
			
			// Verbindung vorbereiten
			sf = new URL(sfUrl + "login.php?do=login");
			simforum = (HttpURLConnection) sf.openConnection();
			simforum.setDoInput(true);
			simforum.setDoOutput(true);
			simforum.setUseCaches(false);
			
			setFortschritt(10);
			setAktion("Login: Sende Daten...");
			
			// Daten senden
			up = new BufferedWriter(new OutputStreamWriter(simforum.getOutputStream()));
			up.write(httpPOST);
			up.flush();
			up.close();
			
			setFortschritt(13);
			setAktion("Login: Warte auf Antwort...");
			
			// Daten empfangen
			down = new BufferedReader(new InputStreamReader(simforum.getInputStream()));
			
			// SessionID suchen
			String loginErgebnis = "";
			boolean loginErfolgreich = false;
			for(String line; (line = down.readLine()) != null; )
			{
				loginErgebnis += line;
				if(line.contains("Falls dein Browser dich nicht automatisch weiterleitet, klicke bitte hier.")) {
					sessionID = line.split("\"")[3].split("s=")[1];
					sessionIDFull = "s=" + sessionID + "&";
					loginErfolgreich = true;
					setFortschritt(20);
				}
			}
			down.close();
			
			setFortschritt(23);
			
			// Bei Fehlern diese schmeißen
			if(!loginErfolgreich) {
				throw new Exception("Falscher Benutzername oder falsches Kennwort.");
			}
			
			if(loginErgebnis.contains("Gib zur Anmeldung deinen Benutzernamen und " +
							"dein Kennwort in die dafür vorgesehenen Textfelder ein")) {
				throw new Exception("Allgemeiner Fehler.");
			}
			
			if(sessionID.isEmpty()) {
				throw new Exception("Session-ID konnte nicht gefunden werden.");
			}
		}
		catch(IOException e) {
			keinFehler = false;
			einst.outEx(e, 72, "Fehler beim Login.");
		}
		catch (Exception e) {
			keinFehler = false;
			if(e.getMessage().contains("Falscher Benutzername oder falsches Kennwort.")) {
				einst.outEx(e, "Fehler beim Login.");
			}
			else if(e.getMessage().contains("Allgemeiner Fehler.")){
				einst.outEx(e, 73, "Fehler beim Login.");
			}
			else {
				einst.outEx(e, 74, "Fehler beim Login.");
			}
		}
		
		// Formulardaten
		String securitytoken = "";
		String doPOST = "";
		String specifiedpost = "";
		String posthash = "";
		String poststarttime = "";
		String loggedinuser = "";
		String multiquoteempty = "";
		String sbutton = "";
		
		if(keinFehler) {
			try {
				setAktion("Posten: Fordere Formulardaten an...");
				
				// Verbindung vorbereiten
				sf = new URL(sfUrl + newreply + sessionIDFull + "do=newreply&noquote=1&p=" + pid);
				simforum = (HttpURLConnection) sf.openConnection();
				
				simforum.setUseCaches(false);
				
				setFortschritt(25);
				setAktion("Posten: Warte auf Formulardaten...");
				
				// Daten empfangen
				down = new BufferedReader(new InputStreamReader(simforum.getInputStream()));
				
				// Formulardaten suchen
				boolean sec = true;
				boolean doingPost = true;
				boolean specpost = true;
				boolean hash = true;
				boolean start = true;
				boolean logged = true;
				boolean multi = true;
				boolean sbut = true;

				setFortschritt(27);
				
				for(String line; (line = down.readLine()) != null; )
				{
					if(line.startsWith("\t\t<input type=\"hidden\" name=\"securitytoken\" ")) {
						securitytoken = line.split("\"")[5];
						sec = false;
						setFortschritt(35);
					}
					if(line.startsWith("\t\t<input type=\"hidden\" name=\"do\" ")) {
						doPOST = line.split("\"")[5];
						doingPost = false;
						setFortschritt(36);
					}
					if(line.startsWith("\t\t<input type=\"hidden\" name=\"specifiedpost\" ")) {
						specifiedpost = line.split("\"")[5];
						specpost = false;
						setFortschritt(37);
					}
					if(line.startsWith("\t\t<input type=\"hidden\" name=\"posthash\" ")) {
						posthash = line.split("\"")[5];
						hash = false;
						setFortschritt(38);
					}
					if(line.startsWith("\t\t<input type=\"hidden\" name=\"poststarttime\" ")) {
						poststarttime = line.split("\"")[5];
						start = false;
						setFortschritt(39);
					}
					if(line.startsWith("\t\t<input type=\"hidden\" name=\"loggedinuser\" ")) {
						loggedinuser = line.split("\"")[5];
						logged = false;
						setFortschritt(40);
					}
					if(line.startsWith("\t\t<input type=\"hidden\" name=\"multiquoteempty\" ")) {
						multiquoteempty = line.split("\"")[7];
						multi = false;
						setFortschritt(41);
					}
					if(line.startsWith("\t\t<input type=\"submit\" class=\"button\" name=\"sbutton\" ")) {
						sbutton = line.split("\"")[9];
						sbut = false;
						setFortschritt(42);
					}
				}
				
				if(sec || doingPost || specpost || hash || start || logged || multi || sbut) {
					throw new Exception("Konnte Formulardaten nicht lesen.");
				}
				
				setFortschritt(47);
				
			} catch (IOException e) {
				keinFehler = false;
				einst.outEx(e, 75, "Fehler beim Abruf der Formulardaten.");
			} catch (Exception e) {
				keinFehler = false;
				einst.outEx(e, 76, "Fehler beim Abruf der Formulardaten.");
			}
		}
		
		if(keinFehler) {
			try {
				setAktion("Posten: Bereite Senden des Posts vor...");
				
				// Posttext fürs Forum konvertieren
				String postinhalt = pe.getVorschau();
				postinhalt = postinhalt.replaceAll("\r", "<br>");
				postinhalt = postinhalt.replaceAll("\n", "<br>");
				postinhalt = postinhalt.replaceAll("\r\n", "<br>");
				postinhalt = postinhalt.replaceAll("\\s{2,}", " ");
//				postinhalt = postinhalt.replaceAll("\\s", "+");
//				postinhalt = new String(postinhalt.getBytes(Charset.defaultCharset()), "CP1252");
				
				/* Testdaten:
Mike Kohl

Öhm, was "s0ll" ich %$tun? Ich tu   mal  mehr... ()der „auch“ nicht?!

[B][SIZE=1]President of the Union of North American States[/SIZE][/B]
				oder
Mike Kohl
„auch“

[B][SIZE=1]President %$of the Union of North, American States[/SIZE][/B]

[B][SIZE=1]President of the   . Union ?! North American States[/SIZE][/B]

[B][SIZE=1]President 0f the Union of N()rth 'American' States[/SIZE][/B]

[B][SIZE=1]Pre§ident of the Union of North AmericÄn States[/SIZE][/B]
				 */
				
				setFortschritt(52);
				
				// POST-Daten festlegen
				Properties postDaten = new Properties();
				postDaten.setProperty("wysiwyg", "1");
				postDaten.setProperty("title", "");
				postDaten.setProperty("t", tid);
				postDaten.setProperty("specifiedpost", specifiedpost);
				postDaten.setProperty("signature", "1");
				postDaten.setProperty("securitytoken", securitytoken);
				postDaten.setProperty("sbutton", sbutton);
				postDaten.setProperty("s", sessionID);
				postDaten.setProperty("poststarttime", poststarttime);
				postDaten.setProperty("posthash", posthash);
				postDaten.setProperty("parseurl", "1");
				postDaten.setProperty("p", pid);
				postDaten.setProperty("multiquoteempty", multiquoteempty);
				postDaten.setProperty("message", postinhalt);
				postDaten.setProperty("loggedinuser", loggedinuser);
				postDaten.setProperty("iconid", "0");
				postDaten.setProperty("emailupdate", "9999");
				postDaten.setProperty("do", doPOST);
				String httpPOST = genHttpPOST(postDaten);
//				httpPOST += "&message=" + postinhalt;
				System.out.println(httpPOST);
				
				setFortschritt(55);
				
				// Verbindung vorbereiten
				sf = new URL(sfUrl + newreply + "do=postreply&t=" + tid);
				simforum = (HttpURLConnection) sf.openConnection();
				simforum.setDoInput(true);
				simforum.setDoOutput(true);
				simforum.setUseCaches(false);
				simforum.setInstanceFollowRedirects(true);
				
				setFortschritt(58);
				setAktion("Posten: Sende Post...");
				
				// Daten senden
				String postDatum = einst.getNeuesPostDatum();
				up = new BufferedWriter(new OutputStreamWriter(simforum.getOutputStream()));
				up.write(httpPOST);
				up.flush();
				up.close();
				
				setFortschritt(65);
				setAktion("Posten: Empfange Post-URL...");
				
				// Daten empfangen
				down = new BufferedReader(new InputStreamReader(simforum.getInputStream()));
				
				for(String line; (line = down.readLine()) != null; )
				{
					einst.out(line);
				}
				
				setFortschritt(73);
				
				if(simforum.getURL().toExternalForm().equals(sfUrl + newreply + "do=postreply&t=" + tid)) {
					throw new Exception("Post konnte nicht gesendet werden.");
				}
				
				// Post-URL einstellen
				URL psf = simforum.getURL();
//				String postURL = sfUrl + "showthread.php?p=" + simforum.getURL().getRef().substring("post".length());
				String postURL = psf.getProtocol() + "://" + psf.getHost() + 
						psf.getPath() + "?" + psf.getQuery().substring(sessionIDFull.length());
				
//				StringBuilder postURL = new StringBuilder(simforum.getHeaderField("Location"));
//				postURL.delete(sfUrl.length() + "showthread.php?".length(),
//						sfUrl.length() + "showthread.php?".length() + sessionIDFull.length());
				
				setFortschritt(75);
				setAktion("Posten: Speichere Post-URL...");
				
				post.setDatum(postDatum);
				post.setUrl(postURL);
				einst.dao().aenderePost(post);
				
				setFortschritt(77);
				setAktion("Posten: Post gesendet.");
			} catch (IOException e) {
				keinFehler = false;
				einst.outEx(e, 77, "Fehler beim Posten.");
			} catch (Exception e) {
				keinFehler = false;
				einst.outEx(e, 78, "Fehler beim Posten.");
			}
		}
		
		if(keinFehler) {
			einst.out("Post wurde gepostet.");
			Object[] text = {"Der Post wurde erfolgreich im Politikhread gepostet."};
			Object[] stringArray = {"OK","Post öffnen"};
			int i = JOptionPane.showOptionDialog(null, text, "Passwort eingeben", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, stringArray, text);
			
			// Post bei Bedarf oeffnen
			if(i == JOptionPane.NO_OPTION){
				einst.oeffneLink(post.getUrl());
			}
			
			// Bei Bedarf neuen Post anlegen
			if(einst.isPostanlegen()){
				new PostEditor(einst, null).postErstellen();
			}
		}
		
		dispose();
		
//		// Alte Version mit Jython
//		System.setProperty("python.cachedir", System.getProperty("java.io.tmpdir"));
//		
//		// Daten sammeln
//		String username = einst.getNutzer();
//		String password = einst.dao().holePW();
//		int status = -1;
//		String meldung = "";
//		
//		// Dateiname der temp-Datei
//		String dateiname = System.getProperty("java.io.tmpdir") + "PostWriterTempPost.tmp";
//		
//		// Post in temp-Datei schreiben
//		File tempDatei = new File(dateiname);
//		PrintWriter writer;
//		
//		try {
//			String postinhalt = new String(pe.getVorschau().getBytes(Charset.defaultCharset()), "CP1252");
//			tempDatei.createNewFile();
//			writer = new PrintWriter(new FileWriter(tempDatei, false), true);
//			writer.print(postinhalt);
//			writer.close();
//		} catch (Exception e) {
//			einst.outEx(e, 66, "Fehler bei der Postübergabe.");
//		}
//		
//		// Falls Passwort nicht in DB, abfragen
//		if(password.isEmpty()){
//			JPasswordField pwf = new JPasswordField();
//			
//			JOptionPane.showMessageDialog(null, "Bitte Passwort des SimForum eingeben:\n" + pwf,
//					"Passwort", JOptionPane.QUESTION_MESSAGE);
//			
//			password = pwf.getPassword().toString();
//			
//			pwf = null;
//		}
//
//		// Interpreter erstellen
//		PythonInterpreter py = new PythonInterpreter();
//		
//		// ImputStream fuer Script erstellen
//		InputStream is = getClass().getResourceAsStream("/de/g77/sy/postwriter/plugins/flpostscript.py");
//		
//		try {
//			// Variablen setten
//			py.set("py_username", new PyString(username));
//			py.set("py_password", new PyString(password));
//			py.set("py_postpath", new PyString(dateiname));
//			py.set("py_sep", new PyString(Einst.nl));
//			
//			// Script ausfuehren
//			py.execfile(is);
//
//			// Daten rausholen
//			status = Integer.parseInt(py.get("py_polipost_status").toString());
//			meldung = py.get("py_polipost_output").toString();
//		} catch (Exception e) {
//			einst.outEx(e, 67, "Fehler beim posten.");
//		}
//		
//		// Sensible Daten null setzen
//		py = null;
//		password = null;
//		System.gc();
//
//		// Abschliesende Aktionen ausfuehren
//		if(tempDatei.exists()){
//			tempDatei.deleteOnExit();
//		}
//		dispose();
//		
//		// Pruefen, ob Post gepostet
//		if(status == 0){
//			// Wenn post gepostet
//			post.setDatum(einst.getNeuesPostDatum());
//			post.setUrl(meldung);
//			dao.aenderePost(post);
//			einst.out("Post wurde gepostet.");
////			JOptionPane.showMessageDialog(null, "Post wurde erfolgreich im Politikhread gepostet.",
////					"Post gepostet", JOptionPane.INFORMATION_MESSAGE);
//			Object[] text = {"Der Post wurde erfolgreich im Politikhread gepostet."};
//			Object[] stringArray = {"OK","Post öffnen"};
//			int i = JOptionPane.showOptionDialog(null, text, "Passwort eingeben", JOptionPane.YES_NO_OPTION,
//					JOptionPane.INFORMATION_MESSAGE, null, stringArray, text);
//			
//			// Post bei Bedarf oeffnen
//			if(i == JOptionPane.NO_OPTION){
//				einst.oeffneLink(meldung);
//			}
//			
//			// Bei Bedarf neuen Post anlegen
//			if(einst.isPostanlegen()){
//				new PostEditor(einst, null).postErstellen();
//			}
//			
//			// Postexporter beenden
//			pe.dispose();
//			einst.out("Post gepostet.");
//		}
//		else if(status == 1){
//			// Wenn Fehler
//			einst.outEx(68, meldung);
//		}
//		else if(status != -1){
//			// Unbekannter Fehler
//			einst.outEx(69, "Ein unbekannter Fehler ist aufgetreten.");
//		}
	}
	
	// Properties-Objekt in String für http-POST konvertieren
	private String genHttpPOST(Properties props) throws IOException {
		StringBuilder sb = new StringBuilder();
		Enumeration<?> namen = props.propertyNames();
		
		// Properties durchgehen
		while(namen.hasMoreElements()) {
			String name = (String) namen.nextElement();
			String wert = props.getProperty(name);
			sb.append(URLEncoder.encode(name, "CP1252") + "=" + URLEncoder.encode(wert, "CP1252"));
//			sb.append(URLEncoder.encode(name, "UTF-8") + "=" + URLEncoder.encode(wert, "UTF-8"));
			if(namen.hasMoreElements()) {
				sb.append("&");
			}
		}
		
		return sb.toString();
	}
}
