package br.com.spindola.atm.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.spindola.atm.model.Withdrawal;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
  List<Withdrawal> findByBankAccountId(Long bankAccountId);
}
