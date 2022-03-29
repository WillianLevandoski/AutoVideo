package com.AutoVideo;

public class Frase {
	
	Integer id;
	String narador;
	String senteca;
	
	
	public Frase(String senteca) {
		this.senteca = senteca.trim();
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNarador() {
		return narador;
	}
	public void setNarador(String narador) {
		this.narador = narador;
	}
	public String getSenteca() {
		return senteca;
	}
	public void setSenteca(String senteca) {
		this.senteca = senteca;
	}



	
	
	
	

}
