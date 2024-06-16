package br.com.spindola.atm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
  
  public List<Withdrawal> findAllByBankAccount(Long bankAccountId) {
    BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
        .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada para o id: " + bankAccountId));
    return withdrawalRepository.findByBankAccount(bankAccount);
  }

  @Transactional
  public Withdrawal withdraw(Withdrawal obj) {
    

    if (obj.getValue() <= 0 || obj.getValue() % 10 != 0) {
      throw new RuntimeException("Valor de saque inválido. Deve ser um valor positivo e múltiplo de 10.");      
    }
    
    
    Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(obj.getBankAccountId());
    BankAccount bankAccount = bankAccountOptional.get();
    
    if (bankAccount == null) {
      throw new RuntimeException("Conta bancária não encontrada para o id: " + obj.getBankAccountId());
    }

    Double balance = bankAccount.getBalance();    

    if (balance.compareTo(obj.getValue()) < 0) {
      throw new RuntimeException("Saldo insuficiente para o saque selecionado!");
    }

    balance = balance - obj.getValue();
    bankAccount.setBalance(balance);
    bankAccountRepository.save(bankAccount);
    
    Map<Integer, Integer> notes = getWithdrawal(obj.getValue());
    System.out.println("Notes: "+notes);
    ObjectMapper mapper = new ObjectMapper();
    String notesJson;
    try {
        notesJson = mapper.writeValueAsString(notes);
    } catch (JsonProcessingException e) {
        throw new RuntimeException("Failed to convert notes to JSON", e);
    }
    
    System.out.println("NotesJason: "+notesJson);

    Withdrawal withdrawal = new Withdrawal();
    withdrawal.setBankAccountId(obj.getBankAccountId());
    withdrawal.setValue(obj.getValue());
    withdrawal.setNotes(notesJson);
    System.out.println(notesJson);

    Withdrawal withdraw = this.withdrawalRepository.save(withdrawal);

    return withdraw;
  }

  public Map<Integer, Integer> getWithdrawal(Double withdrawalValue) {
    
    int[] notes = {100, 50, 20, 10};        

    Map<Integer, Integer> quantityNotes = new HashMap<>();

    Double remainingValue = withdrawalValue;

    for (int i = 0; i < notes.length; i++) {
      if (remainingValue >= notes[i]) {
        Double quantityDouble = remainingValue / notes[i];
        remainingValue = remainingValue % notes[i];  
        Integer quantity = quantityDouble.intValue();      
        quantityNotes.put(notes[i], quantity);
      }
  }

    if (remainingValue != 0) {
        throw new RuntimeException("Não foi possível realizar o saque com as notas disponíveis");
    } 
    
    return quantityNotes;
  }
}
