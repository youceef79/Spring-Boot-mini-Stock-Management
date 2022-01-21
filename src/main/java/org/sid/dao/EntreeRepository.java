package org.sid.dao;

import org.sid.entities.Entree;
import org.sid.entities.Sortie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EntreeRepository extends JpaRepository<Entree, Long>{
	@Query("select e from Entree e where e.produit.id =:x")
	public Entree findByProduct(@Param("x") Long id);
	
	@Query("select e from Entree e where e.produit.categorie.id =:c")
	public Page<Entree> listEntrees(@Param("c") Long cat, Pageable pageable);
 	
}
