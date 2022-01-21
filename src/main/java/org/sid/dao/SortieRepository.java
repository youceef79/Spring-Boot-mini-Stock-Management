package org.sid.dao;

import org.sid.entities.Sortie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SortieRepository extends JpaRepository<Sortie, Long>{

	    @Query("select s from Sortie s where s.produit.categorie.id =:c")
		public Page<Sortie> listSortis(@Param("c") Long cat, Pageable pageable);
	
}
