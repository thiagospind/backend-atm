package br.com.spindola.atm.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.spindola.atm.model.BankAccount;
import br.com.spindola.atm.model.Withdrawal;
import br.com.spindola.atm.repository.BankAccountRepository;
import br.com.spindola.atm.repository.WithdrawalRepository;
import jakarta.transaction.Transactional;

@Service
public class WithdrawalService {
  
  @Autowired
  public WithdrawalRepository withdrawalRepository;

  @Autowired
  public BankAccountRepository bankAccountRepository;

  public Withdrawal findById(Long id) {
    Optional<Withdrawal> withdraw = this.withdrawalRepository.findById(id);
    return withdraw.orElseThrow(() -> new RuntimeException(
      "Saque não encontrado! Id: " + id + ", Tipo: " + Withdrawal.class.getName()
    ));
  }

  // Implementar os metodos que vão buscar o saldo na conta, verificar se possui saldo 
  // maior que o soliticado para saque, caso contrário vai informar o saldo.
  // Caso o saldo seja maior ou igual o valor solicitado, efetuar o saque e salvar o valor sacado e atualizar o saldo do cliente.

  @Transactional
  public Withdrawal withdraw(Long accountId, Double withdrawalAmount) {
    
    if (withdrawalAmount <= 0) {
      throw new RuntimeException("Valor de saque inválido. Deve ser um valor positivo.");
    }

    if (withdrawalAmount <= 0 || withdrawalAmount % 10 != 0) {
      throw new RuntimeException("Valor de saque inválido. Deve ser um valor positivo e múltiplo de 10.");      
    }
    
    Optional<BankAccount> bankAccountOptional = this.bankAccountRepository.findById(accountId);
    
    if (!bankAccountOptional.isPresent()) {
      throw new RuntimeException("Conta bancária não encontrada para o id: " + accountId);
    }

    BankAccount bankAccount = bankAccountOptional.get();
    Double balance = bankAccount.getBalance();

    if (balance.compareTo(withdrawalAmount) < 0) {
      throw new RuntimeException("Saldo insuficiente para o saque selecionado!");
    }

    balance = balance - withdrawalAmount;
    bankAccount.setBalance(balance);
    bankAccountRepository.save(bankAccount);
    
    //Implementar metodo para salvar o saque e chama-lo aqui

    Map<Integer, Double> notes = getWithdrawal(withdrawalAmount);

    Withdrawal withdrawal = new Withdrawal();
    withdrawal.setBankAccountId(accountId);
    withdrawal.setValue(withdrawalAmount);
    withdrawal.setNotes(notes.toString());

    Withdrawal withdraw = this.withdrawalRepository.save(withdrawal);

    return withdraw;
  }

  public Map<Integer, Double> getWithdrawal(Double withdrawalValue) {
    
    int[] notes = {100, 50, 20, 10};        

    Map<Integer, Double> quantityNotes = new HashMap<>();

    Double remainingValue = withdrawalValue;

    for (int i = 0; i < notes.length; i++) {
      if (remainingValue >= notes[i]) {
        Double quantity = remainingValue / notes[i];
        remainingValue = remainingValue % notes[i];
        quantityNotes.put(notes[i], quantity);
      }
  }

    if (remainingValue != 0) {
        throw new RuntimeException("Não foi possível realizar o saque com as notas disponíveis");
    } 
    
    return quantityNotes;
  }
}
