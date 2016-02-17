package de.simocracy.postwriter.fachklassen;

import de.simocracy.postwriter.Einst;

public class Thema {
	private int id = 0;
	private String name = null;
	private String inhalt = null;
	private String erstellt = null;
	private Bereich bereich = null;
	private Post post = null;
	private int pid = 0;
	private int bid = 0;
	private Einst einst = null;
	private int wkat = 0;
	
	public Thema(int id, String name, String inhalt, Einst einst) {
		super();
		this.id = id;
		this.name = name;
		this.inhalt = inhalt;
		this.einst = einst;
	}
	
	public Thema(int id, String name, String inhalt, String erstellt, Einst einst) {
		super();
		this.id = id;
		this.name = name;
		this.inhalt = inhalt;
		this.erstellt = erstellt;
		this.einst = einst;
	}

	public Thema(Einst einst) {
		super();
		this.einst = einst;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInhalt() {
		return inhalt;
	}

	public void setErstellt(String erstellt) {
		this.erstellt = erstellt;
	}

	public String getErstellt() {
		return erstellt;
	}

	public void setInhalt(String inhalt) {
		this.inhalt = inhalt;
	}

	public Bereich getBereich() {
		return bereich;
	}

	public void setBereich(Bereich bereich) {
		this.bereich = bereich;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}
	
	public void suchePost(){
		post = einst.dao().suchePost(pid);
	}
	
	public void sucheBereich(){
		bereich = einst.dao().sucheBereich(bid);
	}

	public int getWkat() {
		return wkat;
	}

	public void setWkat(int wkat) {
		this.wkat = wkat;
	}
}
