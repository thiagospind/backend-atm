package br.com.spindola.atm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = BankAccount.TABLE_NAME)
public class BankAccount {

  public interface CreateBankAccount {}
  public interface UpdateBankAccount {}
  
  public static final String TABLE_NAME = "bank_account";
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)  
  private Long id;
  
  @Column(name = "account", length = 10, unique = true, nullable = false)
  @NotEmpty(groups = {CreateBankAccount.class})
  private String account;
  
  @Column(name = "agency", length = 6, nullable = false)
  @NotEmpty(groups = {CreateBankAccount.class})
  private String agency;
  
  @Column(name = "balance", nullable = false)
  @NotNull(groups = {CreateBankAccount.class, UpdateBankAccount.class})
  private Double balance;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_id", nullable = false)
  @NotNull(groups = {CreateBankAccount.class})
  private Client client;

  @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Withdrawal> withdrawals = new ArrayList<>();

  public BankAccount() {
  }

  public BankAccount(Long id, String account, String agency, Double balance) {
    this.id = id;
    this.account = account;
    this.agency = agency;
    this.balance = balance;
  }


  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAccount() {
    return this.account;
  }

  public void setClientId(Long clientId) {
    if (clientId != null) {
        Client client = new Client();
        client.setId(clientId);
        this.client = client;
    }
  }

  public Long getClientId() {
      return (this.client != null) ? this.client.getId() : null;
  }

  public Client getClient() {
    return this.client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public List<Withdrawal> getWithdrawals() {
    return this.withdrawals;
  }

  public void setWithdrawals(List<Withdrawal> withdrawals) {
    this.withdrawals = withdrawals;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getAgency() {
    return this.agency;
  }

  public void setAgency(String agency) {
    this.agency = agency;
  }

  public Double getBalance() {
    return this.balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;    
    if (obj == null) 
      return false;      
    if (!(obj instanceof BankAccount)) {
      return false;
    }
    
    BankAccount other = (BankAccount) obj;
    if (this.id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!this.id.equals(other.id)) {
      return false;
    }
    return Objects.equals(this.id, other.id) && Objects.equals(this.account, other.account) 
      && Objects.equals(this.agency, other.agency) && Objects.equals(this.balance, other.balance);
      // && Objects.equals(this.clientId, other.clientId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.id == null) ? 0 : id.hashCode());
    return result;
  }

}
