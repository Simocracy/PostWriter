package de.simocracy.postwriter.fachklassen;

import de.simocracy.postwriter.Einst;

public class Kurznachricht {
	private int id = 0;
	private String inhalt = null;
	private String erstellt = null;
	private Post post = null;
	private int pid = 0;
	private Einst einst = null;
	private int wkat = 0;
	
	public Kurznachricht(int id, String inhalt, Einst einst) {
		super();
		this.id = id;
		this.inhalt = inhalt;
		this.einst = einst;
	}
	
	public Kurznachricht(int id, String inhalt, String erstellt, Einst einst) {
		super();
		this.id = id;
		this.inhalt = inhalt;
		this.erstellt = erstellt;
		this.einst = einst;
	}

	public Kurznachricht(Einst einst) {
		super();
		this.einst = einst;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInhalt() {
		return inhalt;
	}

	public void setInhalt(String inhalt) {
		this.inhalt = inhalt;
	}

	public String getErstellt() {
		return erstellt;
	}

	public void setErstellt(String erstellt) {
		this.erstellt = erstellt;
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
	
	public void suchePost(){
		post = einst.dao().suchePost(pid);
	}

	public int getWkat() {
		return wkat;
	}

	public void setWkat(int wkat) {
		this.wkat = wkat;
	}
}
