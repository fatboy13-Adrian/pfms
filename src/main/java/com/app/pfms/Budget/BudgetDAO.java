package com.app.pfms.Budget;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetDAO extends JpaRepository <Budget, Long> {
	//Find by month or months in between
	Optional <Budget> findByMonth(YearMonth month);
	List <Budget> findByMonthBetween(YearMonth start, YearMonth end);
}