package de.simocracy.postwriter.datenhaltung;

import java.sql.*;

import de.simocracy.postwriter.Einst;

public class SQLiteVerbindung{

	private Connection dieVerbindung = null;
	private Statement stmtSQL = null;
	private Einst einst = null;
	
	protected SQLiteVerbindung(Einst einst) {
		super();
		this.einst = einst;
	}

	protected boolean oeffneDB(){
		boolean ok=true;
		try{
			Class.forName("org.sqlite.JDBC");
			dieVerbindung = DriverManager.getConnection("jdbc:sqlite:" + einst.getVerzeichnis() + "posts.sqlite");
			stmtSQL = dieVerbindung.createStatement();
		}
		catch (Exception err){
			ok = false;
			einst.outEx(err, 1, "Verbindung zur DB konnte nicht hergestellt werden.");
		}
		return ok;
	}

	protected boolean oeffneDB(String dbname){
		boolean ok=true;
		try{
			Class.forName("org.sqlite.JDBC");
			dieVerbindung = DriverManager.getConnection("jdbc:sqlite:" + einst.getVerzeichnis() + dbname);
			stmtSQL = dieVerbindung.createStatement();
		}
		catch (Exception err){
			ok = false;
			einst.outEx(err, 2, "Verbindung zur DB konnte nicht hergestellt werden.");
		}
		return ok;
	}
	
	protected boolean schliesseDB(){
		boolean ok = true;
		try{
			stmtSQL.close();
			dieVerbindung.close();
		}
		catch (Exception err){
			ok = false;
			einst.outEx(err, 3, "Verbindung zur Datenbank konnte nicht geschlossen werden.");
		}
		return ok;
	}
 
	protected boolean aendern(String pSQL){ 
		boolean ok = true;
		try{
			einst.out("Führe aus: " + pSQL);
			stmtSQL.executeUpdate(pSQL);
		}
		catch (SQLException err){
			ok = false;
			einst.outEx(err, 4, "Daten konnten nicht in Datenbank geschrieben werden.");
		}
		return ok;
	}
	
	protected ResultSet lesen(String pSQL){
		ResultSet rs;
		try{
			einst.out("Führe aus: " + pSQL);
			rs = stmtSQL.executeQuery(pSQL);
		}
		catch(SQLException err){
			rs = null;
			einst.outEx(err, 5, "Kann Datenbank nicht lesen.");
		}
		return rs;
	}
}