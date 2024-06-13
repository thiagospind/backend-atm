package br.com.spindola.atm.service;

import java.util.Optional;

import javax.swing.text.html.Option;

import org.aspectj.apache.bcel.generic.InstructionConstants.Clinit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.spindola.atm.model.Client;
import br.com.spindola.atm.repository.BankAccountRepository;
import br.com.spindola.atm.repository.ClientRepository;
import jakarta.transaction.Transactional;

@Service
public class ClientService {

  @Autowired
  private ClientRepository clientRepository;
  
  @Autowired
  private BankAccountRepository bankAccountRepository;

  public Client findById(Long id) {
    Optional<Client> client = this.clientRepository.findById(id);
    return client.orElseThrow(() -> new RuntimeException(
      "Cliente não encontrado! Id: " + id + ", Tipo: " + Client.class.getName()
    ));
  
  }
  
  @Transactional
  public Client create(Client obj) {
    obj.setId(null);
    obj = this.clientRepository.save(obj);
    return obj;
  }
  
  @Transactional
  public Client update(Client obj) {
    Client newObj = this.findById(obj.getId());    
    return this.clientRepository.save(newObj);
  }

  public void delete(Long id) {
    this.findById(id);
    try {
      this.clientRepository.deleteById(id);
    } catch (Exception e) {
      throw new RuntimeException("Não é possível excluir um cliente que possui contas bancárias vinculadas");
    }
  }
}
