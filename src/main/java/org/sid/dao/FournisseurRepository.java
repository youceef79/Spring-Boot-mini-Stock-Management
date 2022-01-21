package org.sid.dao;


import org.sid.entities.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long>{
	@Query("select f from Fournisseur f where f.nom =:x")
	public Fournisseur findByName(@Param("x") String four);
	
}
