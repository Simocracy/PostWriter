package de.simocracy.postwriter.datenhaltung;

import java.sql.*;
import java.util.ArrayList;

import de.simocracy.postwriter.Einst;
import de.simocracy.postwriter.fachklassen.*;

public class DAOs{
	
	private SQLiteVerbindung meineDBVerbindung = null;
	private Einst einst = null;
	
	public DAOs(Einst einst){
		this.einst = einst;
		meineDBVerbindung = new SQLiteVerbindung(einst);
	}
	
	public DAOs(Einst einst, SQLiteVerbindung verb){
		this.einst = einst;
		meineDBVerbindung = verb;
	}
	////////////////////////////////////
	// Schema: Erfassen, aendern, suchen, loeschen, auflisten
	////////////////////////////////////
	
	////////////////////////////////////
	// Posts
	////////////////////////////////////
	
	public void erfassePost(Post p){
		if(p.getDatum() == null){
			p.setDatum("");
		}
		if(p.getUrl() == null){
			p.setUrl("");
		}
		String sql = "INSERT INTO post (pid, datum, url) " +
				"VALUES (" + p.getId() + ", '" + p.getDatum() + "', '" + p.getUrl() + "');";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aenderePost(Post p){
		if(p.getDatum() == null){
			p.setDatum("");
		}
		if(p.getUrl() == null){
			p.setUrl("");
		}
		String sql = "UPDATE post SET datum = '" + p.getDatum() + "'," +
				"url = '"+ p.getUrl() + "' WHERE pid = " + p.getId() + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aenderePostPid(int idAlt, int idNeu){
		String sql = "UPDATE post SET pid = " + idNeu + " WHERE pid = " + idAlt + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public Post suchePost(int id){ 
		Post p = new Post();
		ResultSet ergebnismenge=null;
		String sql  = "SELECT * FROM post WHERE pid = " + id + ";";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			p.setId(ergebnismenge.getInt("pid"));
			p.setDatum(ergebnismenge.getString("datum"));
			p.setUrl(ergebnismenge.getString("url"));
		} catch (SQLException e) {
			einst.outEx(e, 30, "Fehler bei der Suche nach Posts.");
		}
		meineDBVerbindung.schliesseDB();
		return p;
	}
	
	public void loeschePost(int id){
		String sql  = "DELETE FROM post WHERE pid = " + id + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public ArrayList<Post> sucheAllePostsMitArray(){ 
		Post p;
		ResultSet ergebnismenge=null;
		ArrayList<Post> pl = new ArrayList<Post>();
		String sql  = "SELECT * FROM post;";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				p = new Post();
				p.setId(ergebnismenge.getInt("pid"));
				p.setDatum(ergebnismenge.getString("datum"));
				p.setUrl(ergebnismenge.getString("url"));
				p.setThemen(sucheThemenFiltern(String.valueOf(p.getId()), null));
				p.setKurznachrichten(sucheKurznachrichtenFiltern(p.getId()));
				pl.add(p);
			}
		} catch (SQLException e) {
			einst.outEx(e, 31, "Fehler bei der Suche nach Posts.");
		}
		meineDBVerbindung.schliesseDB();
		return pl;
	}
	
	public ArrayList<Post> sucheAllePostsMitKurz(){ 
		Post p;
		ResultSet ergebnismenge=null;
		ArrayList<Post> pl = new ArrayList<Post>();
		String sql  = "SELECT * FROM post;";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				p = new Post();
				p.setId(ergebnismenge.getInt("pid"));
				p.setDatum(ergebnismenge.getString("datum"));
				p.setUrl(ergebnismenge.getString("url"));
				p.setKurznachrichten(sucheKurznachrichtenFiltern(p.getId()));
				pl.add(p);
			}
		} catch (SQLException e) {
			einst.outEx(e, 43, "Fehler bei der Suche nach Posts.");
		}
		meineDBVerbindung.schliesseDB();
		return pl;
	}
	
	////////////////////////////////////
	// Bereiche
	////////////////////////////////////
	
	public void erfasseBereich(Bereich b){
		String sql = "INSERT INTO bereich (bid, name, wkat) VALUES (" + b.getId() + ", '" + b.getName() + "', " +
				b.getWkat() + ");";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereBereich(Bereich b){
		String sql = "UPDATE bereich SET name = '" + b.getName() + "', wkat = " + b.getWkat() + 
				" WHERE bid = " + b.getId() + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereBereichBid(int idAlt, int idNeu){
		String sql = "UPDATE bereich SET bid = " + idNeu + " WHERE bid = " + idAlt + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public Bereich sucheBereich(int id){ 
		Bereich b = new Bereich();
		ResultSet ergebnismenge=null;
		String sql  = "SELECT * FROM bereich WHERE bid = " + id + ";";
		meineDBVerbindung.oeffneDB();            
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			b.setId(ergebnismenge.getInt("bid"));
			b.setName(ergebnismenge.getString("name"));
			b.setWkat(ergebnismenge.getInt("wkat"));
		} catch (SQLException e) {
			einst.outEx(e, 32, "Fehler bei der Suche nach Bereich.");
		}
		meineDBVerbindung.schliesseDB();
		return b;
	}
	
//	public Bereich sucheBereich(String name){ 
//		Bereich b = new Bereich();
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT * FROM bereich";
//		if(name != null && !name.equals("null")){
//			sql += " WHERE name = '" + name + "'";
//		}
//		
//		sql += ";";
//		meineDBVerbindung.oeffneDB();            
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			b.setId(ergebnismenge.getInt("bid"));
//			b.setName(ergebnismenge.getString("name"));
//		} catch (SQLException e) {
//			einst.outEx(e, 10, "Fehler bei der Suche nach Bereich.");
//		}
//		meineDBVerbindung.schliesseDB();
//		return b;
//	}
	
//	public int sucheBereichMenge(String name){ 
//		int b = 0;
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT COUNT(*) FROM bereich WHERE name = '" + name + "';";
//		meineDBVerbindung.oeffneDB();            
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			b = ergebnismenge.getInt(1);
//		} catch (SQLException e) {
//			einst.outEx(e, 11, "Fehler bei der Suche nach Bereich.");
//		}
//		meineDBVerbindung.schliesseDB();
//		return b;
//	}
	
	public void loescheBereich(int id){
		String sql  = "DELETE FROM bereich WHERE bid = " + id + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
//	public void loescheBereich(String name){
//		String sql  = "DELETE FROM bereich WHERE name = " + name + ";";
//		meineDBVerbindung.oeffneDB();
//		meineDBVerbindung.aendern(sql);
//		meineDBVerbindung.schliesseDB();
//	}
	
	public ArrayList<Bereich> sucheAlleBereiche(){ 
		Bereich b;
		ResultSet ergebnismenge=null;
		ArrayList<Bereich> bl = new ArrayList<Bereich>();
		String sql  = "SELECT * FROM bereich;";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				b = new Bereich();
				b.setId(ergebnismenge.getInt("bid"));
				b.setName(ergebnismenge.getString("name"));
				b.setWkat(ergebnismenge.getInt("wkat"));
				bl.add(b);
			}
		} catch (SQLException e) {
			einst.outEx(e, 33, "Fehler bei der Suche nach Bereich.");
		}
		meineDBVerbindung.schliesseDB();
		return bl;
	}
	
	public ArrayList<Bereich> sucheAlleBereicheMitArrays(){ 
		Bereich b;
		ResultSet ergebnismenge=null;
		ArrayList<Bereich> bl = new ArrayList<Bereich>();
		String sql  = "SELECT * FROM bereich;";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				b = new Bereich();
				b.setId(ergebnismenge.getInt("bid"));
				b.setName(ergebnismenge.getString("name"));
				b.setWkat(ergebnismenge.getInt("wkat"));
				ArrayList<Thema> alT = sucheThemenFiltern(null, String.valueOf(b.getId()));
				b.setThemen(alT);
				bl.add(b);
			}
		} catch (SQLException e) {
			einst.outEx(e, 34, "Fehler bei der Suche nach Bereich.");
		}
		meineDBVerbindung.schliesseDB();
		return bl;
	}
	
	////////////////////////////////////
	// Themen
	////////////////////////////////////
	
	public void erfasseThema(Thema t){
		String sql = "INSERT INTO thema (tid, name, inhalt, erstellt, bid, pid, wkat) " +
				"VALUES (" + t.getId() + ", '" + t.getName() +"', '" + t.getInhalt() + "', '" + t.getErstellt() +
				"', " + t.getBereich().getId() + ", " + t.getPost().getId() + ", " + t.getWkat() + ");";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereThema(Thema t){
		String sql = "UPDATE thema SET name = '" + t.getName() + "', inhalt = '" + t.getInhalt() +
				"', erstellt = '" + t.getErstellt() + "', bid = " + t.getBereich().getId() + ", pid = " +
				t.getPost().getId() + ", wkat = " + t.getWkat() + " WHERE tid = " + t.getId() + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereThemaOhneObjekte(Thema t){
		String sql = "UPDATE thema SET name = '" + t.getName() + "', inhalt = '" + t.getInhalt() +
				"', erstellt = '" + t.getErstellt() + "', bid = " + t.getBid() + ", pid = " +
				t.getPid() + ", wkat = " + t.getWkat() + " WHERE tid = " + t.getId() + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereThemaBid(int idAlt, int idNeu){
		String sql = "UPDATE thema SET bid = " + idNeu + " WHERE bid = " + idAlt + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereThemaPid(int idAlt, int idNeu){
		String sql = "UPDATE thema SET pid = " + idNeu + " WHERE pid = " + idAlt + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereThemaTid(int idAlt, int idNeu){
		String sql = "UPDATE thema SET tid = " + idNeu + " WHERE tid = " + idAlt + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
//	public Thema sucheThema(int tid){
//		Thema t = new Thema(einst);
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT * FROM thema WHERE tid = " + tid + ";";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			t.setId(ergebnismenge.getInt("tid"));
//			t.setName(ergebnismenge.getString("name"));
//			t.setInhalt(ergebnismenge.getString("inhalt"));
//			t.setErstellt(ergebnismenge.getString("erstellt"));
//			t.setPid(ergebnismenge.getInt("pid"));
//			t.setBid(ergebnismenge.getInt("bid"));
//		} catch (SQLException e) {
//			einst.outEx(e, 13, "Fehler bei der Suche nach Themen.");
//		}
//		meineDBVerbindung.schliesseDB();
//		
//		t.setBereich(sucheBereich(t.getBid()));
//		t.setPost(suchePost(t.getPid()));
//		return t;
//	}
	
//	public Thema sucheThema(String name){
//		Thema t = new Thema(einst);
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT * FROM thema WHERE name = '" + name + "';";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			t.setId(ergebnismenge.getInt("tid"));
//			t.setName(ergebnismenge.getString("name"));
//			t.setInhalt(ergebnismenge.getString("inhalt"));
//			t.setErstellt(ergebnismenge.getString("erstellt"));
//			t.setPid(ergebnismenge.getInt("pid"));
//			t.setBid(ergebnismenge.getInt("bid"));
//		} catch (SQLException e) {
//			einst.outEx(e, 14, "Fehler bei der Suche nach Themen.");
//		}
//		meineDBVerbindung.schliesseDB();
//		
//		t.setBereich(sucheBereich(t.getBid()));
//		t.setPost(suchePost(t.getPid()));
//		return t;
//	}
	
//	public int sucheThemaMengeName(String name){
//		int t = 0;
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT COUNT(*) FROM thema WHERE name = '" + name + "';";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			t = ergebnismenge.getInt(1);
//		} catch (SQLException e) {
//			einst.outEx(e, 15, "Fehler bei der Suche nach Themen.");
//		}
//		meineDBVerbindung.schliesseDB();
//		
//		return t;
//	}
	
//	public int sucheThemaMengeBid(int bid){
//		int t =0;
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT COUNT(*) FROM thema WHERE bid = " + bid + ";";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			t = ergebnismenge.getInt(1);
//		} catch (SQLException e) {
//			einst.outEx(e, 16, "Fehler bei der Suche nach Themen.");
//		}
//		meineDBVerbindung.schliesseDB();
//		return t;
//	}
	
	public void loescheThema(int id){
		String sql  = "DELETE FROM thema WHERE tid = '" + id + "';";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
//	public void loescheThema(String name){
//		String sql  = "DELETE FROM thema WHERE name = '" + name + "';";
//		meineDBVerbindung.oeffneDB();
//		meineDBVerbindung.aendern(sql);
//		meineDBVerbindung.schliesseDB();
//	}
	
	public ArrayList<Thema> sucheAlleThemen(){ 
		Thema t;
		ResultSet ergebnismenge = null;
		ArrayList<Thema> tl = new ArrayList<Thema>();
		String sql  = "SELECT * FROM thema;";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				t = new Thema(einst);
				t.setId(ergebnismenge.getInt("tid"));
				t.setName(ergebnismenge.getString("name"));
				t.setInhalt(ergebnismenge.getString("inhalt"));
				t.setErstellt(ergebnismenge.getString("erstellt"));
				t.setPid(ergebnismenge.getInt("pid"));
				t.setBid(ergebnismenge.getInt("bid"));
				t.setWkat(ergebnismenge.getInt("wkat"));
				tl.add(t);
			}
		} catch (SQLException e) {
			einst.outEx(e, 17, "Fehler bei der Suche nach Themen.");
		}
		meineDBVerbindung.schliesseDB();
		return tl;
	}
	
	public ArrayList<Thema> sucheAlleThemenMitObjekten(){ 
		Thema t;
		ResultSet ergebnismenge = null;
		ArrayList<Thema> tl = new ArrayList<Thema>();
		String sql  = "SELECT * FROM thema;";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				t = new Thema(einst);
				t.setId(ergebnismenge.getInt("tid"));
				t.setName(ergebnismenge.getString("name"));
				t.setInhalt(ergebnismenge.getString("inhalt"));
				t.setErstellt(ergebnismenge.getString("erstellt"));
				t.setPid(ergebnismenge.getInt("pid"));
				t.setBid(ergebnismenge.getInt("bid"));
				t.setWkat(ergebnismenge.getInt("wkat"));
				t.setBereich(sucheBereich(t.getBid()));
				t.setPost(suchePost(t.getPid()));
				tl.add(t);
			}
		} catch (SQLException e) {
			einst.outEx(e, 35, "Fehler bei der Suche nach Themen.");
		}
		meineDBVerbindung.schliesseDB();
		
		return tl;
	}
	
	public ArrayList<Thema> sucheThemenFiltern(String postID, String bereichID){
		Thema t;
		ResultSet ergebnismenge = null;
		ArrayList<Thema> tl = new ArrayList<Thema>();
		String sql = "SELECT * FROM thema WHERE ";
		
		// Auswahl, nach was gesucht werden soll
		if(postID != null && bereichID == null){
			int pid = Integer.parseInt(postID);
			sql += "pid = " + pid;
		}
		else if (postID == null && bereichID != null){
			int bid = Integer.parseInt(bereichID);
			sql += "bid = " + bid;
		}
		else if (postID != null && bereichID != null){
			int pid = Integer.parseInt(postID);
			int bid = Integer.parseInt(bereichID);
			sql += "pid = " + pid + " AND bid = " + bid;
		}
		else{
			einst.outEx(11, "Fehler bei der Suche nach Themen.");
		}
		
		sql += ";";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				t = new Thema(einst);
				t.setId(ergebnismenge.getInt("tid"));
				t.setName(ergebnismenge.getString("name"));
				t.setInhalt(ergebnismenge.getString("inhalt"));
				t.setErstellt(ergebnismenge.getString("erstellt"));
				t.setPid(ergebnismenge.getInt("pid"));
				t.setBid(ergebnismenge.getInt("bid"));
				t.setWkat(ergebnismenge.getInt("wkat"));
				tl.add(t);
			}
		} catch (SQLException e) {
			einst.outEx(e, 36, "Fehler bei der Suche nach Themen.");
		}
		meineDBVerbindung.schliesseDB();
		return tl;
	}
	
	////////////////////////////////////
	// Kurznachrichten
	////////////////////////////////////
	
	public void erfasseKurznachricht(Kurznachricht k){
		String sql = "INSERT INTO kurznachricht (kid, inhalt, erstellt, pid, wkat) VALUES (" + k.getId() + ", '" +
				k.getInhalt() +"', '" + k.getErstellt() + "', " + k.getPost().getId() + ", " + k.getWkat() + ");";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereKurznachricht(Kurznachricht k){
		String sql = "UPDATE kurznachricht SET inhalt = '" + k.getInhalt() + "', erstellt = '" + k.getErstellt() +
				"', pid = " + k.getPost().getId() + ", wkat = " + k.getWkat() + " WHERE kid = " + k.getId() + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereKurznachrichtOhneObjekte(Kurznachricht k){
		String sql = "UPDATE kurznachricht SET inhalt = '" + k.getInhalt() + "', erstellt = '" + k.getErstellt() +
				"', pid = " + k.getPid() + ", wkat = " + k.getWkat() + " WHERE kid = " + k.getId() + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereKurznachrichtKid(int idAlt, int idNeu){
		String sql = "UPDATE kurznachricht SET kid = " + idNeu + " WHERE kid = " + idAlt + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereKurznachrichtPid(int idAlt, int idNeu){
		String sql = "UPDATE kurznachricht SET pid = " + idNeu + " WHERE pid = " + idAlt + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
//	public Kurznachricht sucheKurznachricht(int id){ 
//		Kurznachricht k = new Kurznachricht(einst);
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT * FROM kurznachricht WHERE kid = " + id + ";";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			k.setInhalt(ergebnismenge.getString("inhalt"));
//			k.setErstellt(ergebnismenge.getString("erstellt"));
//			k.setPid(ergebnismenge.getInt("pid"));
//		} catch (SQLException e) {
//			einst.outEx(e, 20, "Fehler bei der Suche nach Kurznachrichten.");
//		}
//		meineDBVerbindung.schliesseDB();
//		return k;
//	}
	
//	public Kurznachricht sucheKurznachricht(String inhalt){ 
//		Kurznachricht k = new Kurznachricht(einst);
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT * FROM kurznachricht WHERE inhalt = '" + inhalt + "';";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			k.setId(ergebnismenge.getInt("kid"));
//			k.setInhalt(ergebnismenge.getString("inhalt"));
//			k.setErstellt(ergebnismenge.getString("erstellt"));
//			k.setPid(ergebnismenge.getInt("pid"));
//		} catch (SQLException e) {
//			einst.outEx(e, 21, "Fehler bei der Suche nach Kurznachrichten.");
//		}
//		meineDBVerbindung.schliesseDB();
//		return k;
//	}
	
//	public int sucheKurzNMenge(String inhalt){ 
//		int k = 0;
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT COUNT(*) FROM kurznachricht WHERE inhalt = '" + inhalt + "';";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			k = ergebnismenge.getInt(1);
//		} catch (SQLException e) {
//			einst.outEx(e, 22, "Fehler bei der Suche nach Kurznachrichten.");
//		}
//		meineDBVerbindung.schliesseDB();
//		return k;
//	}
	
//	public int sucheKurznachrichtMenge(int pid){
//		int t =0;
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT COUNT(*) FROM kurznachricht WHERE pid = " + pid + ";";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			t = ergebnismenge.getInt(1);
//		} catch (SQLException e) {
//			einst.outEx(e, 23, "Fehler bei der Suche nach Kurznachrichten.");
//		}
//		meineDBVerbindung.schliesseDB();
//		return t;
//	}
	
	public void loescheKurznachricht(int id){
		String sql  = "DELETE FROM kurznachricht WHERE kid = " + id + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
//	public void loescheKurznachricht(String inhalt){
//		String sql  = "DELETE FROM kurznachricht WHERE inhalt = " + inhalt + ";";
//		meineDBVerbindung.oeffneDB();
//		meineDBVerbindung.aendern(sql);
//		meineDBVerbindung.schliesseDB();
//	}
	
	public ArrayList<Kurznachricht> sucheAlleKurznachrichten(){ 
		Kurznachricht k;
		ResultSet ergebnismenge=null;
		ArrayList<Kurznachricht> kl = new ArrayList<Kurznachricht>();
		String sql  = "SELECT * FROM kurznachricht;";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				k = new Kurznachricht(einst);
				k.setId(ergebnismenge.getInt("kid"));
				k.setInhalt(ergebnismenge.getString("inhalt"));
				k.setErstellt(ergebnismenge.getString("erstellt"));
				k.setPid(ergebnismenge.getInt("pid"));
				k.setWkat(ergebnismenge.getInt("wkat"));
				kl.add(k);
			}
		} catch (SQLException e) {
			einst.outEx(e, 24, "Fehler bei der Suche nach Kurznachrichten.");
		}
		meineDBVerbindung.schliesseDB();
		return kl;
	}
	
	public ArrayList<Kurznachricht> sucheAlleKurznachrichtenMitObjekten(){ 
		Kurznachricht k;
		ResultSet ergebnismenge=null;
		ArrayList<Kurznachricht> kl = new ArrayList<Kurznachricht>();
		String sql  = "SELECT * FROM kurznachricht;";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				k = new Kurznachricht(einst);
				k.setId(ergebnismenge.getInt("kid"));
				k.setInhalt(ergebnismenge.getString("inhalt"));
				k.setErstellt(ergebnismenge.getString("erstellt"));
				k.setPid(ergebnismenge.getInt("pid"));
				k.setWkat(ergebnismenge.getInt("wkat"));
				k.setPost(suchePost(k.getPid()));
				kl.add(k);
			}
		} catch (SQLException e) {
			einst.outEx(e, 37, "Fehler bei der Suche nach Kurznachrichten.");
		}
		meineDBVerbindung.schliesseDB();
		return kl;
	}
	
	public ArrayList<Kurznachricht> sucheKurznachrichtenFiltern(int pid){ 
		Kurznachricht k;
		ResultSet ergebnismenge=null;
		ArrayList<Kurznachricht> kl = new ArrayList<Kurznachricht>();
		String sql  = "SELECT * FROM kurznachricht WHERE pid = " + pid + ";";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				k = new Kurznachricht(einst);
				k.setInhalt(ergebnismenge.getString("inhalt"));
				k.setErstellt(ergebnismenge.getString("erstellt"));
				k.setPid(ergebnismenge.getInt("pid"));
				k.setWkat(ergebnismenge.getInt("wkat"));
				kl.add(k);
			}
		} catch (SQLException e) {
			einst.outEx(e, 38, "Fehler bei der Suche nach Kurznachrichten.");
		}
		meineDBVerbindung.schliesseDB();
		return kl;
	}
	
	////////////////////////////////////
	// Header
	////////////////////////////////////
	
	public void erfasseHeader(Header h){
		String sql = "INSERT INTO header (hid, name, inhalt, erstellt) " +
				"VALUES (" + h.getId() + ", '" + h.getName() + "', '" + h.getInhalt() + "', '" + h.getDatum() + "');";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereHeader(Header h){
		String sql = "UPDATE header SET name = '" + h.getName() + "'," +
				"inhalt = '"+ h.getInhalt() + "', erstellt = '" + h.getDatum() + "' WHERE hid = " + h.getId() + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereHeaderHid(int idAlt, int idNeu){
		String sql = "UPDATE header SET hid = " + idNeu + " WHERE hid = " + idAlt + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
//	public Header sucheHeader(int id){ 
//		Header h = new Header();
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT * FROM header WHERE hid = " + id + ";";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			h.setId(ergebnismenge.getInt("hid"));
//			h.setName(ergebnismenge.getString("name"));
//			h.setInhalt(ergebnismenge.getString("inhalt"));
//			h.setDatum(ergebnismenge.getString("datum"));
//		} catch (SQLException e) {
//			einst.outEx(e, 5, "Fehler bei der Suche nach Header.");
//		}
//		meineDBVerbindung.schliesseDB();
//		return h;
//	}
	
//	public Header sucheHeader(String name){ 
//		Header h = new Header();
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT * FROM header WHERE name = " + name + ";";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			h.setId(ergebnismenge.getInt("hid"));
//			h.setName(ergebnismenge.getString("name"));
//			h.setInhalt(ergebnismenge.getString("inhalt"));
//			h.setDatum(ergebnismenge.getString("datum"));
//		} catch (SQLException e) {
//			einst.outEx(e, 5, "Fehler bei der Suche nach Header.");
//		}
//		meineDBVerbindung.schliesseDB();
//		return h;
//	}
	
	public void loescheHeader(int id){
		String sql  = "DELETE FROM header WHERE hid = " + id + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public ArrayList<Header> sucheAlleHeader(){ 
		Header h;
		ResultSet ergebnismenge=null;
		ArrayList<Header> hl = new ArrayList<Header>();
		String sql  = "SELECT * FROM header;";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				h = new Header();
				h.setId(ergebnismenge.getInt("hid"));
				h.setName(ergebnismenge.getString("name"));
				h.setInhalt(ergebnismenge.getString("inhalt"));
				h.setDatum(ergebnismenge.getString("erstellt"));
				hl.add(h);
			}
		} catch (SQLException e) {
			einst.outEx(e, 39, "Fehler bei der Suche nach Header.");
		}
		meineDBVerbindung.schliesseDB();
		return hl;
	}
	
	////////////////////////////////////
	// Footer
	////////////////////////////////////
	
	public void erfasseFooter(Footer f){
		String sql = "INSERT INTO footer (fid, name, inhalt, erstellt) " +
				"VALUES (" + f.getId() + ", '" + f.getName() + "', '" + f.getInhalt() + "', '" + f.getDatum() + "');";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereFooter(Footer f){
		String sql = "UPDATE footer SET name = '" + f.getName() + "'," +
				"inhalt = '"+ f.getInhalt() + "', erstellt = '" + f.getDatum() + "' WHERE fid = " + f.getId() + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereFooterFid(int idAlt, int idNeu){
		String sql = "UPDATE footer SET fid = " + idNeu + " WHERE fid = " + idAlt + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
//	public Footer sucheFooter(int id){ 
//		Footer f = new Footer();
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT * FROM header WHERE fid = " + id + ";";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			f.setId(ergebnismenge.getInt("fid"));
//			f.setName(ergebnismenge.getString("name"));
//			f.setInhalt(ergebnismenge.getString("inhalt"));
//			f.setDatum(ergebnismenge.getString("datum"));
//		} catch (SQLException e) {
//			einst.outEx(e, 5, "Fehler bei der Suche nach Footer.");
//		}
//		meineDBVerbindung.schliesseDB();
//		return f;
//	}
	
//	public Footer sucheFooter(String name){ 
//		Footer f = new Footer();
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT * FROM footer WHERE name = " + name + ";";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			f.setId(ergebnismenge.getInt("fid"));
//			f.setName(ergebnismenge.getString("name"));
//			f.setInhalt(ergebnismenge.getString("inhalt"));
//			f.setDatum(ergebnismenge.getString("datum"));
//		} catch (SQLException e) {
//			einst.outEx(e, 5, "Fehler bei der Suche nach Footer.");
//		}
//		meineDBVerbindung.schliesseDB();
//		return f;
//	}
	
	public void loescheFooter(int id){
		String sql  = "DELETE FROM footer WHERE fid = " + id + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public ArrayList<Footer> sucheAlleFooter(){ 
		Footer f;
		ResultSet ergebnismenge=null;
		ArrayList<Footer> fl = new ArrayList<Footer>();
		String sql  = "SELECT * FROM footer;";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				f = new Footer();
				f.setId(ergebnismenge.getInt("fid"));
				f.setName(ergebnismenge.getString("name"));
				f.setInhalt(ergebnismenge.getString("inhalt"));
				f.setDatum(ergebnismenge.getString("erstellt"));
				fl.add(f);
			}
		} catch (SQLException e) {
			einst.outEx(e, 40, "Fehler bei der Suche nach Footer.");
		}
		meineDBVerbindung.schliesseDB();
		return fl;
	}
	
	////////////////////////////////////
	// Standardtexte
	////////////////////////////////////
	
	public void erfasseStandardtext(Standardtext s){
		String sql = "INSERT INTO standardtext (sid, name, inhalt, erstellt) " +
				"VALUES (" + s.getId() + ", '" + s.getName() + "', '" + s.getInhalt() + "', '" + s.getDatum() + "');";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereStandardtext(Standardtext s){
		String sql = "UPDATE standardtext SET name = '" + s.getName() + "'," +
				"inhalt = '"+ s.getInhalt() + "', erstellt = '" + s.getDatum() + "' WHERE sid = " + s.getId() + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereStandardtextSid(int idAlt, int idNeu){
		String sql = "UPDATE standardtext SET sid = " + idNeu + " WHERE sid = " + idAlt + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
//	public Standardtext sucheStandardtext(int id){ 
//		Standardtext s = new Standardtext();
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT * FROM standardtext WHERE sid = " + id + ";";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			s.setId(ergebnismenge.getInt("sid"));
//			s.setName(ergebnismenge.getString("name"));
//			s.setInhalt(ergebnismenge.getString("inhalt"));
//			s.setDatum(ergebnismenge.getString("datum"));
//		} catch (SQLException e) {
//			einst.outEx(e, 5, "Fehler bei der Suche nach Standardtext.");
//		}
//		meineDBVerbindung.schliesseDB();
//		return s;
//	}
	
//	public Standardtext sucheStandardtext(String name){ 
//		Standardtext s = new Standardtext();
//		ResultSet ergebnismenge=null;
//		String sql  = "SELECT * FROM standardtext WHERE name = " + name + ";";
//		meineDBVerbindung.oeffneDB();
//		ergebnismenge = meineDBVerbindung.lesen(sql);
//		try {
//			s.setId(ergebnismenge.getInt("sid"));
//			s.setName(ergebnismenge.getString("name"));
//			s.setInhalt(ergebnismenge.getString("inhalt"));
//			s.setDatum(ergebnismenge.getString("datum"));
//		} catch (SQLException e) {
//			einst.outEx(e, 5, "Fehler bei der Suche nach Standardtext.");
//		}
//		meineDBVerbindung.schliesseDB();
//		return s;
//	}
	
	public void loescheStandardtext(int id){
		String sql  = "DELETE FROM standardtext WHERE sid = " + id + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public ArrayList<Standardtext> sucheAlleStandardtexte(){ 
		Standardtext s;
		ResultSet ergebnismenge=null;
		ArrayList<Standardtext> sl = new ArrayList<Standardtext>();
		String sql  = "SELECT * FROM standardtext;";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				s = new Standardtext();
				s.setId(ergebnismenge.getInt("sid"));
				s.setName(ergebnismenge.getString("name"));
				s.setInhalt(ergebnismenge.getString("inhalt"));
				s.setDatum(ergebnismenge.getString("erstellt"));
				sl.add(s);
			}
		} catch (SQLException e) {
			einst.outEx(e, 41, "Fehler bei der Suche nach Standardtext.");
		}
		meineDBVerbindung.schliesseDB();
		return sl;
	}
	
	////////////////////////////////////
	// Themenvorlagen
	////////////////////////////////////
	
	public void erfasseThemenvorlage(Themenvorlage tv){
		String sql = "INSERT INTO themenvorlage (tvid, name, inhalt, erstellt) " +
				"VALUES (" + tv.getId() + ", '" + tv.getName() + "', '" + tv.getInhalt() +
				"', '" + tv.getDatum() + "');";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereThemenvorlage(Themenvorlage tv){
		String sql = "UPDATE themenvorlage SET name = '" + tv.getName() + "'," +
				"inhalt = '"+ tv.getInhalt() + "', erstellt = '" + tv.getDatum() +
				"' WHERE tvid = " + tv.getId() + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void aendereThemenvorlageTVid(int idAlt, int idNeu){
		String sql = "UPDATE themenvorlage SET tvid = " + idNeu + " WHERE tvid = " + idAlt + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public void loescheThemenvorlage(int id){
		String sql  = "DELETE FROM themenvorlage WHERE tvid = " + id + ";";
		meineDBVerbindung.oeffneDB();
		meineDBVerbindung.aendern(sql);
		meineDBVerbindung.schliesseDB();
	}
	
	public ArrayList<Themenvorlage> sucheAlleThemenvorlagen(){ 
		Themenvorlage tv;
		ResultSet ergebnismenge=null;
		ArrayList<Themenvorlage> tvl = new ArrayList<Themenvorlage>();
		String sql  = "SELECT * FROM themenvorlage;";
		meineDBVerbindung.oeffneDB();
		ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			while(ergebnismenge.next()){
				tv = new Themenvorlage();
				tv.setId(ergebnismenge.getInt("tvid"));
				tv.setName(ergebnismenge.getString("name"));
				tv.setInhalt(ergebnismenge.getString("inhalt"));
				tv.setDatum(ergebnismenge.getString("erstellt"));
				tvl.add(tv);
			}
		} catch (SQLException e) {
			einst.outEx(e, 42, "Fehler bei der Suche nach Themenvorlage.");
		}
		meineDBVerbindung.schliesseDB();
		return tvl;
	}
	
	////////////////////////////////////
	// Einstellungen
	////////////////////////////////////
	
	public void legeNutzerAn(String name){
		String sqlI = "INSERT INTO einstellungen (nutzer) VALUES ('" + name + "');";
		String sqlC = "SELECT COUNT(*) FROM einstellungen;";
		meineDBVerbindung.oeffneDB();
		try{
			if(meineDBVerbindung.lesen(sqlC).getInt(1) == 0){
				meineDBVerbindung.aendern(sqlI);
			}
			else{
				meineDBVerbindung.aendern("UPDATE einstellungen SET nutzer = '" + name + "' " +
						"WHERE nutzer = '" + holeEinstellungen().get(0) + "';");
			}
		} catch (Exception e) {
			einst.outEx(e, 20, "Fehler beim Erstellen des Nutzers.");
		}
		meineDBVerbindung.schliesseDB();
	}
	
	public ArrayList<String> holeEinstellungen(){
		ArrayList<String> al = new ArrayList<String>();
		ResultSet ergebnismenge = null;
		meineDBVerbindung.oeffneDB();            
		ergebnismenge = meineDBVerbindung.lesen("SELECT * FROM einstellungen;");
		try {
			al.add(ergebnismenge.getString("nutzer"));
			al.add(ergebnismenge.getString("autoumbruch"));
			al.add(ergebnismenge.getString("gbereich"));
			al.add(ergebnismenge.getString("gthema"));
			al.add(ergebnismenge.getString("gnormal"));
			al.add(ergebnismenge.getString("header"));
			al.add(ergebnismenge.getString("footer"));
			al.add(ergebnismenge.getString("wikiartikel"));
			al.add(ergebnismenge.getString("topo"));
			al.add(ergebnismenge.getString("postanlegen"));
			al.add(ergebnismenge.getString("tvorlage"));
			al.add(ergebnismenge.getString("formatierung"));
			al.add(ergebnismenge.getString("exportbaum"));
			al.add(ergebnismenge.getString("nopaste"));
			al.add(ergebnismenge.getString("autoupdate"));
			al.add(ergebnismenge.getString("gkurz"));
			al.add(ergebnismenge.getString("gkurztext"));
			al.add(ergebnismenge.getString("kurzbereichname"));
		} catch (SQLException e) {
			einst.outEx(e, 21, "Einstellungen konnten nicht gelesen werden.");
		}
		meineDBVerbindung.schliesseDB();
		return al;
	}
	
	public void speichereEinstellungen(int autoumbruch, int gbereich, int gthema, int gnormal,
			String wikiArtikel, boolean topo, boolean postanlegen, boolean exportBaum, String nopasteSeite,
			boolean autoUpdate, String alterNutzer, int gkurz, int gkurztext, String kurzbereichname){
		String sql = "UPDATE einstellungen SET autoumbruch = " + autoumbruch + ", gbereich = " + gbereich +
				", gthema = " + gthema + ", gnormal = " + gnormal + ", wikiartikel = '" + wikiArtikel + "'" +
				", topo = '" + topo + "', postanlegen = '" + postanlegen + "', exportbaum = '" +
				exportBaum + "', nopaste = '" + nopasteSeite + "', autoupdate = '" + autoUpdate + "', " +
				"nutzer = '" + einst.getNutzer() + "', gkurz = '" + gkurz + "', gkurztext = '" + gkurztext +
				"', kurzbereichname = '" + kurzbereichname + "' WHERE nutzer = '" + alterNutzer + "';";
		meineDBVerbindung.oeffneDB();
		try {
			meineDBVerbindung.aendern(sql);
		} catch (Exception e) {
			einst.outEx(e, 22, "Einstellungen konnten nicht gespeichert werden.");
		}
		meineDBVerbindung.schliesseDB();
	}
	
	public void speicherSHeader(String nutzer, int sheader){
		String sql = "UPDATE einstellungen SET header = " + sheader + " WHERE nutzer = '" + nutzer + "';";
		meineDBVerbindung.oeffneDB();
		try {
			meineDBVerbindung.aendern(sql);
		} catch (Exception e) {
			einst.outEx(e, 23, "Standardheader konnten nicht gespeichert werden.");
		}
		meineDBVerbindung.schliesseDB();
	}
	
	public void speicherSFooter(String nutzer, int sfooter){
		String sql = "UPDATE einstellungen SET footer = " + sfooter + " WHERE nutzer = '" + nutzer + "';";
		meineDBVerbindung.oeffneDB();
		try {
			meineDBVerbindung.aendern(sql);
		} catch (Exception e) {
			einst.outEx(e, 24, "Standardfooter konnten nicht gespeichert werden.");
		}
		meineDBVerbindung.schliesseDB();
	}
	
	public void speicherSThemenVorlage(String nutzer, int sthemenvorlage){
		String sql = "UPDATE einstellungen SET tvorlage = " + sthemenvorlage + " WHERE nutzer = '" + nutzer + "';";
		meineDBVerbindung.oeffneDB();
		try {
			meineDBVerbindung.aendern(sql);
		} catch (Exception e) {
			einst.outEx(e, 25, "Standard-Themenvorlage konnten nicht gespeichert werden.");
		}
		meineDBVerbindung.schliesseDB();
	}
	
	public void speicherFormatierung(String nutzer, String form){
		String sql = "UPDATE einstellungen SET formatierung = '" + form + "' WHERE nutzer = '" + nutzer + "';";
		meineDBVerbindung.oeffneDB();
		try {
			meineDBVerbindung.aendern(sql);
		} catch (Exception e) {
			einst.outEx(e, 26, "Postformat konnten nicht gespeichert werden.");
		}
		meineDBVerbindung.schliesseDB();
	}
	
	public void speicherPW(String pw){
		// Eigentlicher Speichervorgang
		String sql = "UPDATE einstellungen SET pw = '" + pw + "' WHERE nutzer = '" + einst.getNutzer() + "';";
		meineDBVerbindung.oeffneDB();
		try {
			meineDBVerbindung.aendern(sql);
		} catch (Exception e) {
			einst.outEx(e, 57, "Passwort konnte nicht in Datenbank gespeichert werden.");
		}
		meineDBVerbindung.schliesseDB();
	}
	
	public String holePW(){
		// Holen des PW aus DB
		String pw = "";
		String sql = "SELECT pw FROM einstellungen WHERE nutzer = '" + einst.getNutzer() + "';";
		meineDBVerbindung.oeffneDB();
		ResultSet ergebnismenge = meineDBVerbindung.lesen(sql);
		try {
			pw = ergebnismenge.getString("pw");
		} catch (Exception e) {
			einst.outEx(e, 58, "Passwort konnte nicht aus Datenbank gelesen werden.");
		}
		meineDBVerbindung.schliesseDB();
		
		// Rueckgabe
		return pw;
	}
	
	public void loeschePW(){
		// Leeres Passwort in DB einfuegen
		String sql = "UPDATE einstellungen SET pw = '' WHERE nutzer = '" + einst.getNutzer() + "';";
		meineDBVerbindung.oeffneDB();
		try {
			meineDBVerbindung.aendern(sql);
		} catch (Exception e) {
			einst.outEx(e, 59, "Passwort konnte nicht aus Datenbank entfernt werden.");
		}
		meineDBVerbindung.schliesseDB();
	}
}
