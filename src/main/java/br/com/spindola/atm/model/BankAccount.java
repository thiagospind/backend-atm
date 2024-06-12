package br.com.spindola.atm.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = BankAccount.TABLE_NAME)
public class BankAccount {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;
  @Column(name = "account", length = 10, unique = true, nullable = false)
  private String account;
  @Column(name = "agency", length = 6, nullable = false)
  private String agency;
  @Column(name = "balance", nullable = false)
  private BigDecimal balance;
  public static final String TABLE_NAME = "bank_account";
}
