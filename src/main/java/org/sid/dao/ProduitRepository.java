package org.sid.dao;


import org.sid.entities.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProduitRepository extends JpaRepository<Produit, Long>{
    @Query("select p from Produit p where p.categorie.id =:x")
	public Page<Produit> listProds(@Param("x") Long cat, Pageable pageable);
    
    @Query("select p from Produit p where p.designation =:y")
	public Produit findByDes(@Param("y") String prod);
}
