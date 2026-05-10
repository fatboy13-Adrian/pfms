package com.app.pfms.Expense;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.app.pfms.Exceptions.Expense.DateAlreadyExistsException;
import com.app.pfms.Exceptions.Expense.DateNotFoundException;
import com.app.pfms.Exceptions.Expense.ExpenseNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {
    @InjectMocks    //Service under test
    private ExpenseService expSvc;

    @Mock   //Mock DAO layer to simulate DB operations
    private ExpenseDAO dao;

    private LocalDate date;
    private Long id;
    private Expense e;
    private ExpenseDTO dto;

    @BeforeEach
    public void setUp() {
        date = LocalDate.of(2026, 1, 1);

        //DTO used as input for service methods
        dto = new ExpenseDTO();
        dto.setDate(date);
        dto.setBreakfast(BigDecimal.valueOf(5.1));
        dto.setLunch(BigDecimal.valueOf(6.4));
        dto.setDinner(BigDecimal.valueOf(7.1));
        dto.setGroceries(BigDecimal.valueOf(9.99));
        dto.setMedical(BigDecimal.valueOf(22.34));

        //Expense representing DB model
        e = new Expense();
        e.setDate(date);
        e.setBreakfast(BigDecimal.valueOf(5.1));
        e.setLunch(BigDecimal.valueOf(6.4));
        e.setDinner(BigDecimal.valueOf(7.1));
        e.setGroceries(BigDecimal.valueOf(9.99));
        e.setMedical(BigDecimal.valueOf(22.34));
    }

    @Test
    void testCreateExpense() {
        //Mock: no existing expense for this date
        when(dao.findByDate(date)).thenReturn(Optional.empty());

        //Mock: save returns the entity
        when(dao.save(any(Expense.class))).thenReturn(e);

        //Call service method
        ExpenseDTO result = expSvc.createExpense(dto);

        //Verify result mapping
        assertNotNull(result);
        assertEquals(dto.getDate(), result.getDate());
        assertEquals(dto.getBreakfast(), result.getBreakfast());

        //Verify save was called once
        verify(dao, times(1)).save(any(Expense.class));
    }

    @Test
    void testCreateExpenseDateAlreadyExists() {
        //Mock: expense already exists for this date
        when(dao.findByDate(date)).thenReturn(Optional.of(e));

        //Expect exception when creating duplicate expense
        DateAlreadyExistsException exception =
        assertThrows(DateAlreadyExistsException.class,
        () -> expSvc.createExpense(dto));
        assertNotNull(exception);

        //Ensure save is never called
        verify(dao, never()).save(any(Expense.class));
    }

    @Test
    void testRetrieveExpenses() {
        //Mock: return list with one expense
        when(dao.findAll()).thenReturn(List.of(e));
        List<ExpenseDTO> result = expSvc.retrieveExpenses();
        assertNotNull(result);
        assertEquals(1, result.size());

        //Validate mapping of values
        assertEquals(dto.getBreakfast(), result.get(0).getBreakfast());
        verify(dao, times(1)).findAll();
    }

    @Test
    void testRetrieveExpensesEmpty() {
        //Mock: empty database result
        when(dao.findAll()).thenReturn(List.of());
        List<ExpenseDTO> result = expSvc.retrieveExpenses();
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(dao, times(1)).findAll();
    }

    @Test
    void testRetrieveExpense() {
        //Mock: expense exists for given date
        when(dao.findByDate(date)).thenReturn(Optional.of(e));
        ExpenseDTO result = expSvc.retrieveExpense(date);
        assertNotNull(result);
        assertEquals(dto.getDate(), result.getDate());
        verify(dao, times(1)).findByDate(date);
    }

    @Test
    void testRetrieveExpenseNotFound() {
        //Mock: no expense found
        when(dao.findByDate(date)).thenReturn(Optional.empty());

        //Expect exception when not found
        DateNotFoundException exception =
        assertThrows(DateNotFoundException.class,
        () -> expSvc.retrieveExpense(date));
        assertNotNull(exception);
        verify(dao, times(1)).findByDate(date);
    }

    @Test
    void testUpdateExpenseById() {
        //Mock: existing record found
        when(dao.findById(id)).thenReturn(Optional.of(e));

        //Mock: save operation
        when(dao.save(any(Expense.class))).thenReturn(e);

        //Modify input DTO values
        dto.setBreakfast(BigDecimal.valueOf(10.0));
        dto.setMedical(BigDecimal.valueOf(5.0));
        ExpenseDTO result = expSvc.updateExpenseById(id, dto);
        assertNotNull(result);
        assertEquals(dto.getBreakfast(), result.getBreakfast());
        assertEquals(dto.getMedical(), result.getMedical());
        verify(dao, times(1)).findById(id);
        verify(dao, times(1)).save(any(Expense.class));
    }

    @Test
    void testUpdateExpenseByIdNotFound() {
        //Mock: no record exists
        when(dao.findById(id)).thenReturn(Optional.empty());

        //Expect exception
        ExpenseNotFoundException exception =
        assertThrows(ExpenseNotFoundException.class,
        () -> expSvc.updateExpenseById(id, dto));
        assertNotNull(exception);
        verify(dao, times(1)).findById(id);
        verify(dao, never()).save(any(Expense.class));
    }

    @Test
    void testUpdateExpenseByDate() {
        //Mock: existing record found
        when(dao.findByDate(date)).thenReturn(Optional.of(e));

        //Mock: save operation
        when(dao.save(any(Expense.class))).thenReturn(e);

        //Modify input DTO values
        dto.setBreakfast(BigDecimal.valueOf(10.0));
        dto.setMedical(BigDecimal.valueOf(5.0));
        ExpenseDTO result = expSvc.updateExpenseByDate(date, dto);
        assertNotNull(result);
        assertEquals(dto.getBreakfast(), result.getBreakfast());
        assertEquals(dto.getMedical(), result.getMedical());
        verify(dao, times(1)).findByDate(date);
        verify(dao, times(1)).save(any(Expense.class));
    }

    @Test
    void testUpdateExpenseByDateNotFound() {
        //Mock: no record exists
        when(dao.findByDate(date)).thenReturn(Optional.empty());

        //Expect exception
        DateNotFoundException exception =
        assertThrows(DateNotFoundException.class,
        () -> expSvc.updateExpenseByDate(date, dto));
        assertNotNull(exception);
        verify(dao, times(1)).findByDate(date);
        verify(dao, never()).save(any(Expense.class));
    }

    @Test
    void testCalculateInsurance() {
        //Setup monthly data
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setAia(BigDecimal.valueOf(10));
        e1.setCriticare(BigDecimal.valueOf(10));
        Expense e2 = new Expense();
        e2.setAia(BigDecimal.valueOf(10));
        e2.setCriticare(BigDecimal.valueOf(10));

        //Mock DAO date range query
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateInsurance(month);

        //Expected sum across all insurance fields
        BigDecimal expected = e1.getAia()
                              .add(e1.getCriticare())
                              .add(e1.getTermProtector())
                              .add(e2.getAia()).add(e2.getCriticare())
                              .add(e2.getTermProtector());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateInsuranceReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);

        //Mock empty dataset
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateInsurance(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }


    @Test
    void testCalculateMobilePhone() {
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setMobilePhone(BigDecimal.valueOf(15));
        Expense e2 = new Expense();
        e2.setMobilePhone(BigDecimal.valueOf(10));
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateMobilePhone(month);
        BigDecimal expected = e1.getMobilePhone()
                            .add(e2.getMobilePhone());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateMobilePhoneReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateMobilePhone(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateInternet() {
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setInternet(BigDecimal.valueOf(90.46));
        Expense e2 = new Expense();
        e2.setInternet(BigDecimal.valueOf(90.46));
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateInternet(month);
        BigDecimal expected = e1.getInternet()
                            .add(e2.getInternet());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateInternetReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateMobilePhone(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateUtilities() {
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setUtilities(BigDecimal.valueOf(15.46));
        Expense e2 = new Expense();
        e2.setUtilities(BigDecimal.valueOf(42.46));
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateUtilities(month);
        BigDecimal expected = e1.getUtilities()
                            .add(e2.getUtilities());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateUtilitiesReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateUtilities(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateTax() {
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setIncomeTax(BigDecimal.valueOf(10.02));
        e1.setPropertyTax(BigDecimal.valueOf(0.0));
        Expense e2 = new Expense();
        e2.setIncomeTax(BigDecimal.valueOf(10));
        e2.setPropertyTax(BigDecimal.valueOf(10));
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateTax(month);
        BigDecimal expected = e1.getIncomeTax()
                            .add(e1.getPropertyTax())
                            .add(e2.getIncomeTax())
                            .add(e2.getPropertyTax());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateTaxReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateTax(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateMortgage() {
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setMortgage(BigDecimal.valueOf(1115.46));
        Expense e2 = new Expense();
        e2.setMortgage(BigDecimal.valueOf(412.46));
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateMortgage(month);
        BigDecimal expected = e1.getMortgage()
                              .add(e2.getMortgage());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateMortgageReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateMortgage(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateDebt() {
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setDebt(BigDecimal.valueOf(650.46));
        Expense e2 = new Expense();
        e2.setDebt(BigDecimal.valueOf(412.46));
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateDebt(month);
        BigDecimal expected = e1.getDebt()
                            .add(e2.getDebt());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculatDebtReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateDebt(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateAllowancesForParents() {
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setAllowancesForParents(BigDecimal.valueOf(650.46));
        Expense e2 = new Expense();
        e2.setAllowancesForParents(BigDecimal.valueOf(412.46));
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateAllowancesForParents(month);
        BigDecimal expected = e1.getAllowancesForParents()
                            .add(e2.getAllowancesForParents());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateAllowancesForParentsReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateAllowancesForParents(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateTransport() {
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setPublicTransport(BigDecimal.valueOf(110.02));
        e1.setPrivateTransport(BigDecimal.valueOf(0.0));
        Expense e2 = new Expense();
        e2.setPublicTransport(BigDecimal.valueOf(110));
        e2.setPrivateTransport(BigDecimal.valueOf(120));
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateTransport(month);
        BigDecimal expected = e1.getPublicTransport()
                            .add(e1.getPrivateTransport())
                            .add(e2.getPublicTransport())
                            .add(e2.getPrivateTransport());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateTransportReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateTransport(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateFood() {
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setBreakfast(BigDecimal.valueOf(10.02));
        e1.setLunch(BigDecimal.valueOf(10.0));
        e1.setDinner(BigDecimal.valueOf(12.34));
        Expense e2 = new Expense();
        e2.setBreakfast(BigDecimal.valueOf(11.0));
        e2.setPrivateTransport(BigDecimal.valueOf(12.0));
        e2.setDinner(BigDecimal.valueOf(12.34));
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateFood(month);
        BigDecimal expected = e1.getBreakfast()
                            .add(e1.getLunch())
                            .add(e1.getDinner())
                            .add(e2.getBreakfast())
                            .add(e2.getLunch())
                            .add(e2.getDinner());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateFoodReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateFood(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateGroceries() {
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setGroceries(BigDecimal.valueOf(60.46));
        Expense e2 = new Expense();
        e2.setGroceries(BigDecimal.valueOf(12.46));
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateGroceries(month);
        BigDecimal expected = e1.getGroceries()
                            .add(e2.getGroceries());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateGroceriesReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateGroceries(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateHaircut() {
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setHaircut(BigDecimal.valueOf(16.46));
        Expense e2 = new Expense();
        e2.setHaircut(BigDecimal.valueOf(12.46));
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateHaircut(month);
        BigDecimal expected = e1.getHaircut()
                            .add(e2.getHaircut());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateHaircutReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateHaircut(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateMedical() {
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setMedical(BigDecimal.valueOf(16.46));
        Expense e2 = new Expense();
        e2.setMedical(BigDecimal.valueOf(12.46));
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateMedical(month);
        BigDecimal expected = e1.getMedical()
                            .add(e2.getMedical());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateMedicalReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateMedical(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateMisc() {
        YearMonth month = YearMonth.of(2026, 1);
        Expense e1 = new Expense();
        e1.setEntertainment(BigDecimal.valueOf(190));
        e1.setShopping(BigDecimal.valueOf(100.23));
        e1.setHoliday(BigDecimal.valueOf(102.23));
        e1.setSports(BigDecimal.valueOf(12.23));
        e1.setTech(BigDecimal.valueOf(132.23));
        e1.setOthers(BigDecimal.valueOf(32.23));
        Expense e2 = new Expense();
        e2.setEntertainment(BigDecimal.valueOf(190));
        e2.setShopping(BigDecimal.valueOf(100.23));
        e2.setHoliday(BigDecimal.valueOf(102.23));
        e2.setSports(BigDecimal.valueOf(12.23));
        e2.setTech(BigDecimal.valueOf(132.23));
        e2.setOthers(BigDecimal.valueOf(32.23));
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of(e1, e2));
        BigDecimal result = expSvc.calculateMisc(month);
        BigDecimal expected = e1.getEntertainment()
                            .add(e1.getShopping())
                            .add(e1.getHoliday())
                            .add(e1.getSports())
                            .add(e1.getTech())
                            .add(e1.getOthers())
                            .add(e2.getEntertainment())
                            .add(e2.getShopping())
                            .add(e2.getHoliday())
                            .add(e2.getSports())
                            .add(e2.getTech())
                            .add(e2.getOthers());
        assertEquals(expected, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }

    @Test
    void testCalculateMiscReturnZero() {
        YearMonth month = YearMonth.of(2026, 1);
        when(dao.findByDateBetween(month.atDay(1), 
        month.atEndOfMonth())).thenReturn(List.of());
        BigDecimal result = expSvc.calculateMisc(month);
        assertEquals(BigDecimal.ZERO, result);
        verify(dao).findByDateBetween(month.atDay(1), month.atEndOfMonth());
    }
}