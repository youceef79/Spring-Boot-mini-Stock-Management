package org.sid.metier;

import java.util.Date;
import java.util.Optional;

import org.sid.dao.EntreeRepository;
import org.sid.dao.ProduitRepository;
import org.sid.entities.Entree;
import org.sid.entities.Fournisseur;
import org.sid.entities.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class IStockImplementation implements IStockMetier{
	@Autowired
	private ProduitRepository pRep;
	@Autowired
	private EntreeRepository eRep;
	
	public void updateProduit(Entree entree){
		 Optional<Produit> p = pRep.findById(entree.getProduit().getId());
		double priU = p.get().prixU();
		p.get().setQuantite(p.get().getQuantite()+entree.getQuantite());
		p.get().updateFields(priU);
	    pRep.save(p.get());
		}
    public void updateEntreeFromAjout(Produit produit , Fournisseur four){
 	      Entree nvEntree = new Entree(new Date(),produit,four, produit.getQuantite());
		  eRep.save(nvEntree);
	  }  
    
}
