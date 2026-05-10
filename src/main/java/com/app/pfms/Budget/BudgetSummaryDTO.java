package com.app.pfms.Budget;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetSummaryDTO {
    //Unique identifier for each record
    private Long id;

    //Year of record
    private int year;

    private BigDecimal income = BigDecimal.ZERO;
    private BigDecimal retirement = BigDecimal.ZERO;
    private BigDecimal insurance = BigDecimal.ZERO;
    private BigDecimal mobilePhone = BigDecimal.ZERO;
    private BigDecimal internet = BigDecimal.ZERO;
    private BigDecimal utilities = BigDecimal.ZERO;
    private BigDecimal tax = BigDecimal.ZERO;
    private BigDecimal mortgage = BigDecimal.ZERO;
    private BigDecimal debt = BigDecimal.ZERO;
    private BigDecimal allowancesForParents = BigDecimal.ZERO;
    private BigDecimal transport = BigDecimal.ZERO;
    private BigDecimal food = BigDecimal.ZERO;
    private BigDecimal groceries = BigDecimal.ZERO;
    private BigDecimal haircut = BigDecimal.ZERO;
    private BigDecimal medical = BigDecimal.ZERO;
    private BigDecimal misc = BigDecimal.ZERO;
    private BigDecimal savings = BigDecimal.ZERO;
}