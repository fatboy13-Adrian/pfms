package com.app.pfms.Expense;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseDAO extends JpaRepository <Expense, Long> {
	//Find by date or dates between
	Optional <Expense> findByDate(LocalDate date);
	List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
}