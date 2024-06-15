package br.com.spindola.atm.service;

import java.util.HashMap;
import java.util.List;
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

  public List<Withdrawal> findByBankAccountId(Long id) {
    List<Withdrawal> withdraws = this.withdrawalRepository.findByBankAccountId(id);
    if (withdraws.isEmpty()) {
      throw new RuntimeException(
          "Saques não encontrados para a conta bancária! Id: " + id + ", Tipo: " + Withdrawal.class.getName()
      );
    }
    return withdraws;
  }

  @Transactional
  public Withdrawal withdraw(Withdrawal obj) {
    System.out.println("withdraw bank:"+obj.getBankAccountId());    
    
    if (obj.getValue() <= 0) {
      throw new RuntimeException("Valor de saque inválido. Deve ser um valor positivo.");
    }

    if (obj.getValue() <= 0 || obj.getValue() % 10 != 0) {
      throw new RuntimeException("Valor de saque inválido. Deve ser um valor positivo e múltiplo de 10.");      
    }
    
    BankAccount bankAccount = obj.getBankAccount();
    // Optional<BankAccount> bankAccountOptional = this.bankAccountRepository.findById(accountId);
    
    if (bankAccount == null) {
      throw new RuntimeException("Conta bancária não encontrada para o id: " + obj.getBankAccountId());
    }

    // BankAccount bankAccount = bankAccountOptional.get();
    Double balance = bankAccount.getBalance();
    bankAccount.getBalance();

    if (balance.compareTo(obj.getValue()) < 0) {
      throw new RuntimeException("Saldo insuficiente para o saque selecionado!");
    }

    balance = balance - obj.getValue();
    bankAccount.setBalance(balance);
    bankAccountRepository.save(bankAccount);
    
    //Implementar metodo para salvar o saque e chama-lo aqui

    Map<Integer, Double> notes = getWithdrawal(obj.getValue());

    Withdrawal withdrawal = new Withdrawal();
    withdrawal.setBankAccountId(obj.getBankAccountId());
    withdrawal.setValue(obj.getValue());
    withdrawal.setNotes(notes.toString());
    System.out.println(notes.toString());

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
