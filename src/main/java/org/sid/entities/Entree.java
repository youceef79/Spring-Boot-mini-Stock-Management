package org.sid.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Entree implements Serializable{
	@Id @GeneratedValue
    private long id;	
	private Date date;
	@ManyToOne
	@JoinColumn(name="prod_id")
	private Produit produit;
	private Long quantite;
	
	private double prix;
	
	public double getPrix() {
		return prix;
	}
	
	public void setPrix(double prix) {
		this.prix = prix;
	}
	
	public Long getQuantite() {
		return quantite;
	}
	public void setQuantite(Long quantite) {
		this.quantite = quantite;
	}
	public Fournisseur getFournisseur() {
		return fournisseur;
	}
	public void setFournisseur(Fournisseur fournisseur) {
		this.fournisseur = fournisseur;
	}
	@ManyToOne
	@JoinColumn(name="four_id")
	private Fournisseur fournisseur;
	public Entree(Date date , Produit produit, Fournisseur fournisseur , Long quantite) {
		super();
		this.date = date;
		this.produit = produit;
		this.fournisseur = fournisseur;
		this.quantite = quantite;
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Entree() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}