package com.app.pfms.Expense;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ExpenseControllerTest {
    //Mock service layer
    @Mock
    private ExpenseService expSvc;

    //Controller under test with mocks injected
    @InjectMocks
    private ExpenseController controller;

    //Reusable test values for consistency
    private static final BigDecimal BREAKFAST = BigDecimal.valueOf(5.15);
    private static final BigDecimal LUNCH = BigDecimal.valueOf(7.15);
    private static final BigDecimal DINNER = BigDecimal.valueOf(6.15);
    private static final BigDecimal GROCERIES = BigDecimal.valueOf(9.99);
    private static final BigDecimal MEDICAL = BigDecimal.valueOf(15.15);

    //Helper method to create a standard DTO for tests
    private ExpenseDTO createExpenseDTO() {
        ExpenseDTO dto = new ExpenseDTO();

        //Fixed date to avoid flaky tests
        dto.setDate(LocalDate.of(2026, 1, 1));

        dto.setBreakfast(BREAKFAST);
        dto.setLunch(LUNCH);
        dto.setDinner(DINNER);
        dto.setGroceries(GROCERIES);
        dto.setMedical(MEDICAL);

        return dto;
    }

    @Test
    public void testCreateExpense() {
        ExpenseDTO dto = createExpenseDTO();

        //Mock service response
        when(expSvc.createExpense(dto)).thenReturn(dto);

        //Call controller
        ResponseEntity<ExpenseDTO> response = controller.createExpense(dto);

        //Check HTTP status
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ExpenseDTO body = response.getBody();

        //Validate returned values
        assertEquals(BREAKFAST, body.getBreakfast());
        assertEquals(LUNCH, body.getLunch());
        assertEquals(DINNER, body.getDinner());
        assertEquals(GROCERIES, body.getGroceries());
        assertEquals(MEDICAL, body.getMedical());
    }

    @Test
    public void testRetrieveExpenses() {
        ExpenseDTO dto = createExpenseDTO();

        //Mock list response from service
        when(expSvc.retrieveExpenses()).thenReturn(List.of(dto));

        //Call controller
        ResponseEntity<List<ExpenseDTO>> response = controller.retrieveExpenses();

        //Check status, list size & data
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        ExpenseDTO result = response.getBody().get(0);
        assertEquals(BREAKFAST, result.getBreakfast());
    }

    @Test
    public void testRetrieveExpense() {
        //Create sample DTO & extract test date
        ExpenseDTO dto = createExpenseDTO();
        LocalDate testDate = dto.getDate();

        //Mock service response for retrieving by date
        when(expSvc.retrieveExpense(testDate)).thenReturn(dto);

        //Act: call controller
        ResponseEntity<ExpenseDTO> response = controller.retrieveExpense(testDate);

        //Assert: verify HTTP status, returned date & breakfast value
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDate, response.getBody().getDate());
        assertEquals(BREAKFAST, response.getBody().getBreakfast());
    }

    @Test
    public void testUpdateExpenseById() {
        //Create original DTO & test date
        ExpenseDTO dto = createExpenseDTO();
        Long id = dto.getId();

        //Create updated DTO with modified values
        ExpenseDTO updatedDto = createExpenseDTO();
        updatedDto.setBreakfast(BigDecimal.valueOf(5.25));
        updatedDto.setMedical(BigDecimal.valueOf(15.25));

        //Mock service update behaviour
        when(expSvc.updateExpenseById(id, updatedDto)).thenReturn(updatedDto);

        //Call controller update method
        ResponseEntity<ExpenseDTO> response = controller.updateExpenseById(id, updatedDto);

        //Assert: verify HTTP status
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //Extract response body for validation
        ExpenseDTO body = response.getBody();

        //Assert: verify updated values
        assertEquals(BigDecimal.valueOf(5.25), body.getBreakfast());
        assertEquals(BigDecimal.valueOf(15.25), body.getMedical());
    }

    @Test
    public void testUpdateExpenseByDate() {
        //Create original DTO & test date
        ExpenseDTO dto = createExpenseDTO();
        LocalDate testDate = dto.getDate();

        //Create updated DTO with modified values
        ExpenseDTO updatedDto = createExpenseDTO();
        updatedDto.setBreakfast(BigDecimal.valueOf(5.25));
        updatedDto.setMedical(BigDecimal.valueOf(15.25));

        //Mock service update behaviour
        when(expSvc.updateExpenseByDate(testDate, updatedDto)).thenReturn(updatedDto);

        //Call controller update method
        ResponseEntity<ExpenseDTO> response = controller.updateExpenseByDate(testDate, updatedDto);

        //Assert: verify HTTP status
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //Extract response body for validation
        ExpenseDTO body = response.getBody();

        //Assert: verify updated values
        assertEquals(BigDecimal.valueOf(5.25), body.getBreakfast());
        assertEquals(BigDecimal.valueOf(15.25), body.getMedical());
    }
}