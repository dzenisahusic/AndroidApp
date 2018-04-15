package com.comtrade.edit2014sales;

public class Artikal {
	private long id;
	private int barkod; 
	private String naziv; 
	private float cijena;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getBarkod() {
		return barkod;
	}
	public void setBarkod(int barkod) {
		this.barkod = barkod;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public float getCijena() {
		return cijena;
	}
	public void setCijena(float cijena) {
		this.cijena = cijena;
	}
	
	@Override
	  public String toString() {
	    return naziv;
	  }
}
