package org.sid.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;
@Entity
public class Fournisseur implements Serializable{
	public Long getId() {
		return id;
	}
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotEmpty
	@Size(min=4,max=20)
	private String nom;
	@NotEmpty
	private String adresse;
	@NotEmpty
	private String tel;
	@OneToMany(mappedBy = "fournisseur",fetch = FetchType.LAZY)
	private Collection<Entree> entrees;
	public Fournisseur(@Size(min = 4, max = 20) String nom, String adresse, String tel) {
		super();
		this.nom = nom;
		this.adresse = adresse;
		this.tel = tel;
	}
	public Fournisseur() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
