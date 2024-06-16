package br.com.spindola.atm.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import br.com.spindola.atm.model.BankAccount;
import br.com.spindola.atm.model.Client;
import br.com.spindola.atm.model.Withdrawal;
import br.com.spindola.atm.repository.WithdrawalRepository;

@DataJpaTest
@ActiveProfiles("test")
public class WithdrawalRepositoryTest {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByBankAccount() {
        Client client = new Client();
        client.setName("John Doe");
        client.setCpf("12345678901");
        entityManager.persist(client);
      
        // Create a bank account
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAgency("123456");
        bankAccount.setAccount("1234567");
        bankAccount.setBalance(10000.0);
        bankAccount.setClientId(client.getId());
        entityManager.persist(bankAccount);

        // Create some withdrawals for the bank account
        Withdrawal withdrawal1 = new Withdrawal();
        withdrawal1.setBankAccountId(bankAccount.getId());
        withdrawal1.setValue(1000.0); 
        withdrawal1.setNotes("teste"); 
        entityManager.persist(withdrawal1);

        Withdrawal withdrawal2 = new Withdrawal();
        withdrawal2.setBankAccountId(bankAccount.getId());
        withdrawal2.setValue(2000.0); 
        withdrawal2.setNotes("teste 2"); 

        // Find withdrawals by bank account
        List<Withdrawal> withdrawals = withdrawalRepository.findByBankAccount(bankAccount);

        // Assertions
        assertNotNull(withdrawals);
        assertEquals(2, withdrawals.size());
        assertEquals(withdrawal1.getValue(), withdrawals.get(0).getValue());
        assertEquals(withdrawal2.getValue(), withdrawals.get(1).getValue());
    }
}