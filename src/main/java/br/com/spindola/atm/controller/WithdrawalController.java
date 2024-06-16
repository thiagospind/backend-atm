package br.com.spindola.atm.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.spindola.atm.model.BankAccount;
import br.com.spindola.atm.model.Withdrawal;
import br.com.spindola.atm.model.Withdrawal.CreateWithdrawal;
import br.com.spindola.atm.service.WithdrawalService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/withdrawal")
@Validated
public class WithdrawalController {
  @Autowired
  private WithdrawalService withdrawalService;

  @GetMapping("/{id}")
  public ResponseEntity<Withdrawal> findById(@PathVariable Long id) {
    Withdrawal withdrawal = this.withdrawalService.findById(id);
    return ResponseEntity.ok().body(withdrawal);
  }
  
  @GetMapping("/all/{id}")
  public ResponseEntity<List<Withdrawal>> findByBankAccountId(@PathVariable Long id) {
    List<Withdrawal> withdrawals = this.withdrawalService.findAllByBankAccount(id);
    return ResponseEntity.ok().body(withdrawals);
  }

  @PostMapping
  @Validated(CreateWithdrawal.class)
  public ResponseEntity<Void> create(@Valid @RequestBody Withdrawal obj) {
    if (obj.getBankAccountId() != null) {
      BankAccount bankAccount = new BankAccount();
      bankAccount.setId(obj.getBankAccountId());
      obj.setBankAccount(bankAccount);
    } else {
      return ResponseEntity.badRequest().build();
    }
    Withdrawal withdrawal = this.withdrawalService.withdraw(obj);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{id}").buildAndExpand(withdrawal.getId()).toUri();
    return ResponseEntity.created(uri).build();
  }
}
