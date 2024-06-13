package br.com.spindola.atm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.spindola.atm.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
  
}
