package br.com.spindola.atm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.spindola.atm.model.BankAccount;
import br.com.spindola.atm.repository.BankAccountRepository;

public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        // Arrange
        Long id = 1L;
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(id);
        when(bankAccountRepository.findById(id)).thenReturn(Optional.of(bankAccount));

        // Act
        BankAccount result = bankAccountService.findById(id);

        // Assert
        assertEquals(bankAccount, result);
    }

    @Test
    public void testFindByIdNotFound() {
        // Arrange
        Long id = 1L;
        when(bankAccountRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bankAccountService.findById(id));
    }

    @Test
    public void testCreate() {
        // Arrange
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(null);
        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        // Act
        BankAccount result = bankAccountService.create(bankAccount);

        // Assert
        assertEquals(bankAccount, result);
    }

    @Test
    public void testUpdate() {
        // Arrange
        Long id = 1L;
        BankAccount existingBankAccount = new BankAccount();
        existingBankAccount.setId(id);
        when(bankAccountRepository.findById(id)).thenReturn(Optional.of(existingBankAccount));
        when(bankAccountRepository.save(existingBankAccount)).thenReturn(existingBankAccount);

        // Act
        BankAccount result = bankAccountService.update(existingBankAccount);

        // Assert
        assertEquals(existingBankAccount, result);
    }

    @Test
    public void testDelete() {
        // Arrange
        Long id = 1L;
        BankAccount existingBankAccount = new BankAccount();
        existingBankAccount.setId(id);
        when(bankAccountRepository.findById(id)).thenReturn(Optional.of(existingBankAccount));

        // Act
        bankAccountService.delete(id);

        // Assert
        verify(bankAccountRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteNotFound() {
        // Arrange
        Long id = 1L;
        when(bankAccountRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bankAccountService.delete(id));
    }
}