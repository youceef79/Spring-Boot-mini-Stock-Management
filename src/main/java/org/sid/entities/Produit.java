package org.sid.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;

@SuppressWarnings("serial")
@Entity
public class Produit implements Serializable{
	
	@Column(nullable = true, length = 64)
    private String image;

	@Id @GeneratedValue
	private Long id;
	
	@NotEmpty
	@Size(min=4,max=50)
	private String designation;
	
	
	//@DecimalMax("10000.0") @DecimalMin("0.0") @NotNull @NumberFormat
	private double prix_achat_Ht;

	private double prix_vente_Ht;
	
	private double prix_vente_ttc;
	
	public Long getId() {
		return id;
	}
	public double getPrix_vente_Ht() {
		return prix_vente_Ht;
	}
	public void setPrix_vente_Ht(double prix_vente_Ht) {
		this.prix_vente_Ht = prix_vente_Ht;
	}
	public double getPrix_vente_ttc() {
		return prix_vente_ttc;
	}
	public void setPrix_vente_ttc(double prix_vente_ttc) {
		this.prix_vente_ttc = prix_vente_ttc;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getImage() {
		return image;
	}
	
	
	@Transient
    public String getImagePath() {
        if (image == null) return null;
         
        return "/app-images/" + image;
    }
	
	private long quantite;
	
	@ManyToOne
	@JoinColumn(name="cat_id")
	private Categorie categorie;
    @OneToMany(mappedBy = "produit",fetch = FetchType.LAZY)
	private Collection<Entree> entries;
    @OneToMany(mappedBy = "produit",fetch = FetchType.LAZY)
	private Collection<Sortie> sorties;

    @NotNull
    private double marge;

    @NotNull
    @NumberFormat
    private double taux;
	
	public double getMarge() {
		return marge;
	}
	public void setMarge(double marge) {
		this.marge = marge;
	}
	public Double prixU() {
		return prix_achat_Ht/quantite;
	}
	public void updateFields(double unt) {
		this.prix_achat_Ht = quantite *unt;
		this.prix_vente_Ht = prix_achat_Ht + prix_achat_Ht*(marge/100);
		this.prix_vente_ttc = prix_vente_Ht + prix_vente_Ht*(taux/100);
	}
	public double getTaux() {
		return taux;
	}
	public void setTaux(double taux) {
		this.taux = taux;
	}
	public Collection<Entree> getEntries() {
		return entries;
	}
	public void setEntries(Collection<Entree> entries) {
		this.entries = entries;
	}
	public Collection<Sortie> getSorties() {
		return sorties;
	}
	public void setSorties(Collection<Sortie> sorties) {
		this.sorties = sorties;
	}
	
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public double getPrix_achat_Ht() {
		return prix_achat_Ht;
	}
	public void setPrix_achat_Ht(double prix) {
		this.prix_achat_Ht = prix;
	}
	public long getQuantite() {
		return quantite;
	}
	public void setQuantite(long quantite) {
		this.quantite = quantite;
	}
	public Produit() {
		super();
	}
	
	public Produit(@Size(min = 4, max = 15) String designation,double prix_achat_Ht, double prix_vente_Ht,
			double prix_vente_ttc, long quantite, Categorie categorie, float marge, float taux) {
		super();
		this.designation = designation;
		this.prix_achat_Ht = prix_achat_Ht;
		this.prix_vente_Ht = prix_vente_Ht;
		this.prix_vente_ttc = prix_vente_ttc;
		this.quantite = quantite;
		this.categorie = categorie;
		this.marge = marge;
		this.taux = taux;
	}
	public Categorie getCategorie() {
		return categorie;
	}
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}
}
