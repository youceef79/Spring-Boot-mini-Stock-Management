package org.sid.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Categorie implements Serializable{
	@Id @GeneratedValue
    private Long id;
	
	private String nom;
	public Long getId() {
		return id;
	}
	@OneToMany(mappedBy = "categorie",fetch = FetchType.LAZY)
	private Collection<Produit> produits;
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Collection<Produit> getProduits() {
		return produits;
	}
	public void setProduits(Collection<Produit> produits) {
		this.produits = produits;
	}
	public Categorie(String nom) {
		super();
		this.nom = nom;
	}
	public Categorie() {
		super();
		// TODO Auto-generated constructor stub
	}
 
}