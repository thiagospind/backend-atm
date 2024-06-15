package br.com.spindola.atm.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.spindola.atm.model.BankAccount;
import br.com.spindola.atm.model.BankAccount.CreateBankAccount;
import br.com.spindola.atm.model.BankAccount.UpdateBankAccount;
import br.com.spindola.atm.model.Client;
import br.com.spindola.atm.service.BankAccountService;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/bankaccount")
@Validated
public class BankAccountController {
  
  @Autowired
  private BankAccountService bankAccountService;

  @GetMapping("/{id}")
  public ResponseEntity<BankAccount> findById(@PathVariable Long id) {
    BankAccount bankAccount = this.bankAccountService.findById(id);
    return ResponseEntity.ok().body(bankAccount);
  }

  @PostMapping
  @Validated(CreateBankAccount.class)
  public ResponseEntity<Void> create(@Valid @RequestBody BankAccount obj) {
    if (obj.getClientId() != null) {
      Client client = new Client();
      client.setId(obj.getClientId());
      obj.setClient(client);
    } else {
      return ResponseEntity.badRequest().build();
    }
    this.bankAccountService.create(obj);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{id}").buildAndExpand(obj.getId()).toUri();
    return ResponseEntity.created(uri).build();
  }

  @PutMapping
  @Validated(UpdateBankAccount.class)
  public ResponseEntity<Void> update(@Valid @RequestBody BankAccount obj, @PathVariable Long id) {
    obj.setId(id);
    this.bankAccountService.update(obj);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    this.bankAccountService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
