package com.app.pfms.Budget;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.pfms.Exceptions.Budget.BudgetNotFoundException;
import com.app.pfms.Exceptions.Budget.MonthAlreadyExistsException;
import com.app.pfms.Exceptions.Budget.MonthNotFoundException;
import com.app.pfms.Expense.ExpenseDAO;

@ExtendWith(MockitoExtension.class) 
public class BudgetServiceTest {
    @InjectMocks    //Service under test
    private BudgetService budgetSvc;

    @Mock   //Mocked DAO layer
    private BudgetDAO dao;

    @Mock
    private ExpenseDAO expDAO;

    private Budget b;
    private BudgetDTO dto;

    @BeforeEach
    public void setUp() {
        //Intialize DTO with sample data
        dto = new BudgetDTO();
        dto.setMonth(YearMonth.of(2026, 4));
        dto.setIncome(BigDecimal.valueOf(5000.0));
        dto.setRetirement(BigDecimal.valueOf(500.0));
        dto.setInsurance(BigDecimal.valueOf(155.0));
        dto.setTax(BigDecimal.valueOf(10.02));
        dto.setMortgage(BigDecimal.valueOf(1000.0));
        dto.setAllowancesForParents(BigDecimal.valueOf(800.0));
        dto.setDebt(BigDecimal.valueOf(650.0));
        dto.setTransport(BigDecimal.valueOf(122.0));
        dto.setUtilities(BigDecimal.valueOf(45.0));
        dto.setMobilePhone(BigDecimal.valueOf(15.0));
        dto.setInternet(BigDecimal.valueOf(90.46));
        dto.setFood(BigDecimal.valueOf(650.0));
        dto.setGroceries(BigDecimal.valueOf(100.0));
        dto.setHaircut(BigDecimal.valueOf(15.0));
        dto.setMedical(BigDecimal.valueOf(12.0));
        dto.setMisc(BigDecimal.valueOf(200.0));

        //Initialize budget matching DTO values
        b = new Budget();
        b.setId(1L);
        b.setMonth(dto.getMonth());
        b.setIncome(dto.getIncome());
        b.setRetirement(dto.getRetirement());
        b.setInsurance(dto.getInsurance());
        b.setTax(dto.getTax());
        b.setMortgage(dto.getMortgage());
        b.setAllowancesForParents(dto.getAllowancesForParents());
        b.setDebt(dto.getDebt());
        b.setTransport(dto.getTransport());
        b.setUtilities(dto.getUtilities());
        b.setMobilePhone(dto.getMobilePhone());
        b.setInternet(dto.getInternet());
        b.setFood(dto.getFood());
        b.setGroceries(dto.getGroceries());
        b.setHaircut(dto.getHaircut());
        b.setMedical(dto.getMedical());
        b.setMisc(dto.getMisc());
    }

    @Test
    public void testCreateBudget() {
        //Arrange: month does not exist yet
        when(dao.findByMonth(dto.getMonth()))
        .thenReturn(Optional.empty());
        when(expDAO.findByDateBetween(any(LocalDate.class), 
        any(LocalDate.class)))
        .thenReturn(List.of());

        when(dao.save(any(Budget.class)))
        .thenReturn(b);

        //Act: create budget
        BudgetDTO result = budgetSvc.createBudget(dto);

        //Assert: verify creation success
        assertNotNull(result);
        assertEquals(dto.getMonth(), result.getMonth());

        //Verify save operation occurred once
        verify(dao, times(1)).save(any(Budget.class));
    }

    @Test
    void testCreateBudgetMonthAlreadyExists() {
        //Arrange: month already exists in DB
        YearMonth month = dto.getMonth();

        when(dao.findByMonth(month))
        .thenReturn(Optional.of(b));

        //Act & Assert: exception expected
        MonthAlreadyExistsException ex =
        assertThrows(MonthAlreadyExistsException.class,
        () -> budgetSvc.createBudget(dto));
        assertNotNull(ex);

        //Ensure no save happens
        verify(dao, never()).save(any(Budget.class));
    }


@Test
    void testRetreiveBudgets() {
        //Arrange: budget exists
        YearMonth month = YearMonth.of(2026, 4);
        when(dao.findByMonth(month)).thenReturn(Optional.of(b));

        //Act
        BudgetDTO result = budgetSvc.retrieveBudget(month);

        //Assert
        assertNotNull(result);
        assertEquals(dto.getMonth(), result.getMonth());
        verify(dao, times(1)).findByMonth(month);
    }

    @Test
    void testRetrieveBudgetsEmpty() {
        //Arrange: no budgets exist
        when(dao.findAll()).thenReturn(List.of());

        //Act
        List<BudgetDTO> result = budgetSvc.retrieveBudgets();

        //Assert empty list
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(dao, times(1)).findAll();
    }

    @Test
    void testRetrieveBudget() {
        //Arrange
        YearMonth month = YearMonth.of(2026, 4);
        when(dao.findByMonth(month)).thenReturn(Optional.of(b));

        //Act
        BudgetDTO result = budgetSvc.retrieveBudget(month);

        //Assert
        assertNotNull(result);
        assertEquals(dto.getMonth(), result.getMonth());
        verify(dao, times(1)).findByMonth(month);
    }

    @Test
    void testRetrieveBudgetNotFound() {
        //Arrange: no record found
        YearMonth month = YearMonth.of(2026, 4);
        when(dao.findByMonth(month)).thenReturn(Optional.empty());

        //Act & Assert
        MonthNotFoundException exception =
        assertThrows(MonthNotFoundException.class,
        () -> budgetSvc.retrieveBudget(month));
        assertNotNull(exception);
        verify(dao, times(1)).findByMonth(month);
    }

    @Test
    void testRetrieveYearlySummaries() {
        when(dao.findAll()).thenReturn(List.of(b));
        List<BudgetSummaryDTO> result = budgetSvc.retrieveYearlySummaries();
        verify(dao, times(1)).findAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testRetrieveYearlySummariesEmpty() {
        when(dao.findAll()).thenReturn(List.of());
        List<BudgetSummaryDTO> result = budgetSvc.retrieveYearlySummaries();
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(dao, times(1)).findAll();
    }

    @Test
    void testUpdateBudgetByMonth() {
        YearMonth month = YearMonth.of(2026, 4);

        //Arrange: existing budget found
        when(dao.findByMonth(month)).thenReturn(Optional.of(b));
        when(dao.save(any(Budget.class))).thenReturn(b);

        //Act
        BudgetDTO result = budgetSvc.updateBudgetByMonth(month, dto);

        //Assert
        assertNotNull(result);
        assertEquals(dto.getMonth(), result.getMonth());
        verify(dao, times(1)).findByMonth(month);
        verify(dao, times(1)).save(any(Budget.class));
    }

    @Test
    void testUpdateBudgetNotFound() {
        //Arrange: no budget found
        YearMonth month = YearMonth.of(2026, 4);
        when(dao.findByMonth(month)).thenReturn(Optional.empty());

        //Act & Assert
        MonthNotFoundException exception = 
        assertThrows(MonthNotFoundException.class,
        () -> budgetSvc.updateBudgetByMonth(month, dto));

        assertNotNull(exception);
        verify(dao, times(1)).findByMonth(month);
        verify(dao, never()).save(any(Budget.class));
    }

    @Test
    void testUpdateBudgetById() {
        Long id = dto.getId();

        //Arrange: existing budget found
        when(dao.findById(id)).thenReturn(Optional.of(b));
        when(dao.save(any(Budget.class))).thenReturn(b);

        //Act
        BudgetDTO result = budgetSvc.updateBudgetById(id, dto);

        //Assert
        assertNotNull(result);
        assertEquals(dto.getMonth(), result.getMonth());
        verify(dao, times(1)).findById(id);
        verify(dao, times(1)).save(any(Budget.class));
    }

    @Test
    void testUpdateBudgetByIdNotFound() {
        //Arrange: no budget found
        Long id = dto.getId();
        when(dao.findById(id)).thenReturn(Optional.empty());

        //Act & Assert
        BudgetNotFoundException exception = 
        assertThrows(BudgetNotFoundException.class,
        () -> budgetSvc.updateBudgetById(id, dto));

        assertNotNull(exception);
        verify(dao, times(1)).findById(id);
        verify(dao, never()).save(any(Budget.class));
    }

    @Test
    void testCalculateSavings() {
        //Calculate expected savings manually
        BigDecimal savings = budgetSvc.calculateSavings(b);
        BigDecimal income = dto.getIncome();
        BigDecimal expenses = b.getRetirement()
                             .add(b.getInsurance())
                             .add(b.getMobilePhone())
                             .add(b.getInternet())
                             .add(b.getUtilities())
                             .add(b.getTax())
                             .add(b.getMortgage())
                             .add(b.getDebt())
                             .add(b.getAllowancesForParents())
                             .add(b.getTransport())
                             .add(b.getFood())
                             .add(b.getGroceries())
                             .add(b.getHaircut())
                             .add(b.getMedical())
                             .add(b.getMisc());
        BigDecimal expectedSavings = income.subtract(expenses);
        assertEquals(expectedSavings, savings, "Total savings calculation should match expected value");
    }

    @Test
    void calculateSavingsZeroValue() {
        //Set all values to zero
        b.setIncome(BigDecimal.ZERO);
        b.setRetirement(BigDecimal.ZERO);
        b.setInsurance(BigDecimal.ZERO);
        b.setTax(BigDecimal.ZERO);
        b.setMortgage(BigDecimal.ZERO);
        b.setAllowancesForParents(BigDecimal.ZERO);
        b.setDebt(BigDecimal.ZERO);
        b.setTransport(BigDecimal.ZERO);
        b.setUtilities(BigDecimal.ZERO);
        b.setMobilePhone(BigDecimal.ZERO);
        b.setInternet(BigDecimal.ZERO);
        b.setFood(BigDecimal.ZERO);
        b.setGroceries(BigDecimal.ZERO);
        b.setHaircut(BigDecimal.ZERO);
        b.setMedical(BigDecimal.ZERO);
        b.setMisc(BigDecimal.ZERO);
        BigDecimal savings = budgetSvc.calculateSavings(b);
        assertEquals(BigDecimal.ZERO, savings, "Total savings should be zero");
    }

    @Test
    void testCalculateSavingsNegativeValue() {
        //Arrange: expenses exceed income (negative savings case)
        b.setIncome(BigDecimal.valueOf(5000.0));
        b.setRetirement(BigDecimal.valueOf(1000.0));
        b.setInsurance(BigDecimal.valueOf(150.0));
        b.setTax(BigDecimal.valueOf(123.45));
        b.setMortgage(BigDecimal.valueOf(1234.0));
        b.setAllowancesForParents(BigDecimal.valueOf(123.0));
        b.setDebt(BigDecimal.valueOf(650.0));
        b.setTransport(BigDecimal.valueOf(122.0));
        b.setUtilities(BigDecimal.valueOf(45.67));
        b.setMobilePhone(BigDecimal.valueOf(15.0));
        b.setInternet(BigDecimal.valueOf(123.45));
        b.setFood(BigDecimal.valueOf(666.66));
        b.setGroceries(BigDecimal.valueOf(234.56));
        b.setHaircut(BigDecimal.valueOf(15.0));
        b.setMedical(BigDecimal.valueOf(12.34));
        b.setMisc(BigDecimal.valueOf(2345.67));
        BigDecimal savings = budgetSvc.calculateSavings(b);
        assertNotNull(savings);
    }
}