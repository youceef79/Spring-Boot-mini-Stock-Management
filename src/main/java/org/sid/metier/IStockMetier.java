package org.sid.metier;

import org.sid.dao.EntreeRepository;
import org.sid.entities.Entree;
import org.sid.entities.Fournisseur;
import org.sid.entities.Produit;

public interface IStockMetier {
     
	public void updateEntreeFromAjout(Produit produit , Fournisseur f);
	public void updateProduit(Entree entree);
}
