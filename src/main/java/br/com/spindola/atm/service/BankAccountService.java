package br.com.spindola.atm.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.spindola.atm.model.BankAccount;
import br.com.spindola.atm.repository.BankAccountRepository;
import jakarta.transaction.Transactional;

@Service
public class BankAccountService {
  
  @Autowired
  private BankAccountRepository bankAccountRepository;


  public BankAccount findById(Long id) {
    Optional<BankAccount> bankAccount = this.bankAccountRepository.findById(id);
    return bankAccount.orElseThrow(() -> new RuntimeException(
      "Conta bancária não encontrada! Id: " + id + ", Tipo: " + BankAccount.class.getName()
    ));
  }

  @Transactional
  public BankAccount create(BankAccount obj) {
    obj.setId(null);
    obj = this.bankAccountRepository.save(obj);
    return obj;
  }
  
  @Transactional
  // Atulizar esse método para permitir somente atualizar o saldo da conta!
  public BankAccount update(BankAccount obj) {
    BankAccount newObj = this.findById(obj.getId());    
    return this.bankAccountRepository.save(newObj);
  }

  public void delete(Long id) {
    this.findById(id);
    try {
      this.bankAccountRepository.deleteById(id);
    } catch (Exception e) {
      throw new RuntimeException("Não é possível excluir a conta bancária!");
    }
  }  
}
