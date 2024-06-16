package br.com.spindola.atm.model;

import java.util.Date;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = Withdrawal.TABLE_NAME)
public class Withdrawal {
  public interface CreateWithdrawal {}
  public interface UpdateWithdrawal {}
  
  public static final String TABLE_NAME = "withdrawal";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;  

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bank_account_id", nullable=false, updatable=false)
  @JsonIgnore
  private BankAccount bankAccount;
  
  @Column(name = "value", nullable = false)
  @NotNull(groups = {CreateWithdrawal.class})
  private Double value;

  @Column(name = "date", nullable = false, updatable = false)
  @CreationTimestamp
  private Date date;  

  @Column(name = "notes", nullable = false)
  private String notes; 

  public void setBankAccountId(Long bankAccountId) {
    if (bankAccountId != null) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(bankAccountId);
        this.bankAccount = bankAccount;
    }
  }

  public Long getBankAccountId() {
      return (this.bankAccount != null) ? this.bankAccount.getId() : null;
  }

  public BankAccount getBankAccount() {
    return this.bankAccount;
  }

  public void setBankAccount(BankAccount bankAccount) {
    this.bankAccount = bankAccount;
  }

  public String getNotes() {
    return this.notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Withdrawal() {
  }

  public Withdrawal(Double value, Date date, String notes) {    
    this.value = value;
    this.date = date;
    this.notes = notes;
  }


  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getValue() {
    return this.value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public Date getDate() {
    return this.date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;    
    if (obj == null) 
      return false;      
    if (!(obj instanceof Withdrawal)) {
      return false;
    }
    
    Withdrawal other = (Withdrawal) obj;
    if (this.id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!this.id.equals(other.id)) {
      return false;
    }
    return Objects.equals(this.id, other.id) && Objects.equals(this.date, other.date) 
      && Objects.equals(this.value, other.value)
      && Objects.equals(this.notes, other.notes);      
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.id == null) ? 0 : id.hashCode());
    return result;
  }

}
