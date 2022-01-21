package org.sid.dao;


import org.sid.entities.Client;
import org.sid.entities.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client, Long>{
	@Query("select c from Client c where c.nom =:x")
	public Client findByName(@Param("x") String cl);
	
}
