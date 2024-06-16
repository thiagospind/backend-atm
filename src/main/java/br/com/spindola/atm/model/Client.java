package br.com.spindola.atm.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = Client.TABLE_NAME)
public class Client {

  public interface CreateClient {}
  public interface UpdateClient {}

  public static final String TABLE_NAME = "client";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;
  
  @Column(name = "name", length = 255, unique = false, nullable = false)
  @NotEmpty(groups = {CreateClient.class, UpdateClient.class})
  private String name;
  
  @Column(name = "cpf", length = 11, unique = true, nullable = false)
  @NotEmpty(groups = {CreateClient.class, UpdateClient.class})
  private String cpf;
  
  @OneToOne(mappedBy = "client", fetch = FetchType.LAZY)
  private BankAccount bankAccount;

  public Client() {
  }

  public Client(Long id, String name, String cpf) {    
    this.name = name;
    this.cpf = cpf;
  }


  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCpf() {
    return this.cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;    
    if (obj == null) 
      return false;      
    if (!(obj instanceof Client)) {
      return false;
    }
    
    Client other = (Client) obj;
    if (this.id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!this.id.equals(other.id)) {
      return false;
    }
    return Objects.equals(this.id, other.id) && Objects.equals(this.name, other.name) 
    && Objects.equals(this.cpf, other.cpf);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.id == null) ? 0 : id.hashCode());
    return result;
  }
}
