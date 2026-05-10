package com.app.pfms.Expense;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {
    //Unique identifier for each record
    private Long id;

    //Date of record
    private LocalDate date;

    private BigDecimal aia = BigDecimal.ZERO;
    private BigDecimal criticare = BigDecimal.ZERO;
    private BigDecimal termProtector = BigDecimal.ZERO;
    private BigDecimal mobilePhone = BigDecimal.ZERO;
    private BigDecimal internet = BigDecimal.ZERO;
    private BigDecimal utilities = BigDecimal.ZERO;
    private BigDecimal incomeTax = BigDecimal.ZERO;
    private BigDecimal propertyTax = BigDecimal.ZERO;
    private BigDecimal mortgage = BigDecimal.ZERO;
    private BigDecimal debt = BigDecimal.ZERO;
    private BigDecimal allowancesForParents = BigDecimal.ZERO;
    private BigDecimal publicTransport = BigDecimal.ZERO;
    private BigDecimal privateTransport = BigDecimal.ZERO;
    private BigDecimal breakfast = BigDecimal.ZERO;
    private BigDecimal lunch = BigDecimal.ZERO;
    private BigDecimal dinner = BigDecimal.ZERO;
    private BigDecimal eatingOut = BigDecimal.ZERO;
    private BigDecimal groceries = BigDecimal.ZERO;
    private BigDecimal haircut = BigDecimal.ZERO;
    private BigDecimal medical = BigDecimal.ZERO;
    private BigDecimal entertainment = BigDecimal.ZERO;
    private BigDecimal holiday = BigDecimal.ZERO;
    private BigDecimal shopping = BigDecimal.ZERO;
    private BigDecimal sports = BigDecimal.ZERO;
    private BigDecimal tech = BigDecimal.ZERO;
    private BigDecimal others = BigDecimal.ZERO;
}