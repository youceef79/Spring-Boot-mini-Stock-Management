package org.sid.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Sortie implements Serializable{
	@Id @GeneratedValue
    private Long id;	
	private Date date;
	
	private double prix;
	
	@ManyToOne
	@JoinColumn(name="prod_id")
	private Produit produit;
	
	@ManyToOne
	@JoinColumn(name="client_id")
	private Client client;
	
	private Long quantiteEx;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public Long getQuantiteEx() {
		return quantiteEx;
	}
	public void setQuantiteEx(Long quantiteEx) {
		this.quantiteEx = quantiteEx;
	}
	public Sortie(Date date, Produit produit) {
		super();
		this.date = date;
		this.produit = produit;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Produit getProduit() {
		return produit;
	}
	public void setProduit(Produit produit) {
		this.produit = produit;
	}
	public Sortie() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}