package br.com.spindola.atm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.spindola.atm.model.BankAccount;
import br.com.spindola.atm.model.Withdrawal;
import br.com.spindola.atm.repository.BankAccountRepository;
import br.com.spindola.atm.repository.WithdrawalRepository;

public class WithdrawalServiceTest {

    @Mock
    private WithdrawalRepository withdrawalRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private WithdrawalService withdrawalService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        // Arrange
        Long withdrawalId = 1L;
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(withdrawalId);
        when(withdrawalRepository.findById(withdrawalId)).thenReturn(Optional.of(withdrawal));

        // Act
        Withdrawal result = withdrawalService.findById(withdrawalId);

        // Assert
        assertEquals(withdrawal, result);
    }

    @Test
    public void testFindAllByBankAccount() {
        // Arrange
        Long bankAccountId = 1L;
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(bankAccountId);
        List<Withdrawal> withdrawals = new ArrayList<>();
        withdrawals.add(new Withdrawal());
        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccount));
        when(withdrawalRepository.findByBankAccount(bankAccount)).thenReturn(withdrawals);

        // Act
        List<Withdrawal> result = withdrawalService.findAllByBankAccount(bankAccountId);

        // Assert
        assertEquals(withdrawals, result);
    }

    @Test
    public void testWithdrawWithInvalidValue() {
        // Arrange
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setValue(-100.0);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> withdrawalService.withdraw(withdrawal));
    }

    @Test
    public void testWithdrawWithInsufficientBalance() {
        // Arrange
        Long bankAccountId = 1L;
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(bankAccountId);
        bankAccount.setBalance(100.0);
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setBankAccountId(bankAccountId);
        withdrawal.setValue(200.0);
        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccount));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> withdrawalService.withdraw(withdrawal));
    }

    @Test
    public void testWithdraw() {
        // Arrange
        Long bankAccountId = 1L;
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(bankAccountId);
        bankAccount.setBalance(1000.0);
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setBankAccountId(bankAccountId);
        withdrawal.setValue(100.0);
        Map<Integer, Integer> notes = new HashMap<>();
        notes.put(100, 1);
        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccount));
        when(withdrawalRepository.save(withdrawal)).thenReturn(withdrawal);
        when(withdrawalService.getWithdrawal(withdrawal.getValue())).thenReturn(notes);

        // Act
        Withdrawal result = withdrawalService.withdraw(withdrawal);

        // Assert
        assertEquals(withdrawal, result);
    }

    @Test
    public void testGetWithdrawal() {
        // Arrange
        Double withdrawalValue = 150.0;
        Map<Integer, Integer> expectedNotes = new HashMap<>();
        expectedNotes.put(100, 1);
        expectedNotes.put(50, 1);

        // Act
        Map<Integer, Integer> result = withdrawalService.getWithdrawal(withdrawalValue);

        // Assert
        assertEquals(expectedNotes, result);
    }
}