package br.com.spindola.atm.repository;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.spindola.atm.model.BankAccount;
import br.com.spindola.atm.model.Client;
import br.com.spindola.atm.repository.BankAccountRepository;
import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BankAccountRepositoryTest {
  
  @Autowired
  EntityManager entityManager;

  @Autowired
  BankAccountRepository bankAccountRepository;

  @Test
  @DisplayName("Should return a bank account by id")
  void findByBankAccountIdSuccess() {
    String name = "John Doe";
    String cpf = "12345678901";
    
    Client client = new Client();
    client.setName(name);
    client.setCpf(cpf);
    Client clientNew = this.createClient(client);
    BankAccount bankAccountNew = this.createBankAccount(clientNew);

    Optional<BankAccount> bankAccountFound = this.bankAccountRepository.findById(bankAccountNew.getId());

    assertThat(bankAccountFound.isPresent()).isTrue();
  }

  @Test
  @DisplayName("Should not return a bank account by id when bank account id not exists")
  void findByBankAccountIdNotSuccess() {
    Long id = -1L;
    Optional<BankAccount> bankAccountFound = this.bankAccountRepository.findById(id);

    assertThat(bankAccountFound.isEmpty()).isTrue();
  }

  private Client createClient(Client client) {
    Client newClient = new Client();
    newClient.setName(client.getName());
    newClient.setCpf(client.getCpf());
    this.entityManager.persist(newClient);
    return newClient;
  }

  private BankAccount createBankAccount(Client client) {
    BankAccount newBankAccount = new BankAccount();
    newBankAccount.setClientId(client.getId());
    newBankAccount.setBalance(10000.0);
    newBankAccount.setAgency("1234");
    newBankAccount.setAccount("123456-7");    
    this.entityManager.persist(newBankAccount);
    return newBankAccount;
  }
}
