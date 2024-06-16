package br.com.spindola.atm.repository;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.spindola.atm.model.Client;
import br.com.spindola.atm.repository.ClientRepository;
import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ClientRepositoryTest {

  @Autowired
  EntityManager entityManager;

  @Autowired
  ClientRepository clientRepository;

  @Test
  @DisplayName("Should return a client by id")
  void findByClientIdSuccess() {
    String name = "John Doe";
    String cpf = "12345678901";
    Client client = new Client();
    client.setName(name);
    client.setCpf(cpf);
    Client clientNew = this.createClient(client);

    Optional<Client> clientFound = this.clientRepository.findById(clientNew.getId());

    assertThat(clientFound.isPresent()).isTrue();
  }

  @Test
  @DisplayName("Should not return a client when client not exists")
  void findByClientIdNotSuccess() {
    Long id = -1L;    
    // Client clientNew = this.createClient(client);

    Optional<Client> clientFound = this.clientRepository.findById(id);

    assertThat(clientFound.isEmpty()).isTrue();
  }

  private Client createClient(Client client) {
    Client newClient = new Client();
    newClient.setName(client.getName());
    newClient.setCpf(client.getCpf());
    this.entityManager.persist(newClient);
    return newClient;
  }
  
}
