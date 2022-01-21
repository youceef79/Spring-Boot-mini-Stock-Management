package org.sid.dao;


import org.sid.entities.Categorie;
import org.sid.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CategorieRepository extends JpaRepository<Categorie, Long>{
    @Query("select c from Categorie c where c.nom =:x")
	public Categorie findByName(@Param("x") String cat);
	
}
