package com.app.pfms.Budget;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class BudgetControllerTest {
    //Mock service layer (prevents real business logic execution)
    @Mock
    private BudgetService budgetSvc;

    //Controller under test with mocks injected
    @InjectMocks
    private BudgetController controller;

    //Setup phase before each test
    @BeforeEach
    public void setUp() {
        //Standalone setup for controller testing (no full Spring context)
        MockMvcBuilders.standaloneSetup(controller).build();
    }

    //Helper method to create a standard BudgetDTO
    public BudgetDTO createBudgetDTO() {
        BudgetDTO dto = new BudgetDTO();

        //Set sample budget values
        dto.setIncome(BigDecimal.valueOf(5000.0));
        dto.setRetirement(BigDecimal.valueOf(500.0));
        dto.setInsurance(BigDecimal.valueOf(123.45));

        return dto;
    }

    @Test
    public void testCreateBudget() {
        BudgetDTO dto = createBudgetDTO();

        //Mock service response
        when(budgetSvc.createBudget(dto)).thenReturn(dto);

        //Call controller method
        ResponseEntity<BudgetDTO> response = controller.createBudget(dto);

        //Validate HTTP status
        assertEquals(HttpStatus.OK, response.getStatusCode());

        BudgetDTO body = response.getBody();

        //Validate returned values
        assertEquals(BigDecimal.valueOf(5000.0), body.getIncome());
        assertEquals(BigDecimal.valueOf(500.0), body.getRetirement());
        assertEquals(BigDecimal.valueOf(123.45), body.getInsurance());
    }

    @Test
    public void testRetrieveBudgets() {
        BudgetDTO dto = createBudgetDTO();

        //Mock service returning list of budgets
        when(budgetSvc.retrieveBudgets()).thenReturn(List.of(dto));

        //Call controller method
        ResponseEntity<List<BudgetDTO>> response = controller.retrieveBudgets();

        //Validate HTTP status
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //Validate list size
        assertEquals(1, response.getBody().size());

        BudgetDTO result = response.getBody().get(0);

        //Validate returned data
        assertEquals(BigDecimal.valueOf(5000.0), result.getIncome());
    }

    @Test
	public void testRetrieveBudget() {
		//Use fixed value instead of YearMonth.now() (avoids flaky tests)
		YearMonth testMonth = YearMonth.of(2026, 5);
		BudgetDTO dto = createBudgetDTO();

		//Set DTO month
		dto.setMonth(testMonth);

		//Mock service response
		when(budgetSvc.retrieveBudget(testMonth)).thenReturn(dto);

		//Call controller method
		ResponseEntity<BudgetDTO> response = controller.retrieveBudget(testMonth);

		//Validate HTTP status
		assertEquals(HttpStatus.OK, response.getStatusCode());

		//Validate returned month
		assertEquals(testMonth, response.getBody().getMonth());
	}

    @Test
    public void testRetrieveYearlySummaries() {
        int year = 2026;
        BudgetSummaryDTO dto = new BudgetSummaryDTO();
        dto.setYear(year);
        List<BudgetSummaryDTO> list = List.of(dto);
        when(budgetSvc.retrieveYearlySummaries()).thenReturn(list);
        ResponseEntity<List<BudgetSummaryDTO>> response = controller.retrieveYearlySummaries();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(year, response.getBody().get(0).getYear());
    }

    @Test
	public void testUpdateBudgetById() {
		//DTO representing updated values
		BudgetDTO updatedDto = createBudgetDTO();
        Long id = updatedDto.getId();
		updatedDto.setIncome(BigDecimal.valueOf(5000.0));
		updatedDto.setInsurance(BigDecimal.valueOf(155.0));

		//Mock service layer update call
		when(budgetSvc.updateBudgetById(id, updatedDto)).thenReturn(updatedDto);

		//Call controller
		ResponseEntity<BudgetDTO> response = controller.updateBudgetById(id, updatedDto);

		//Verify response status
		assertEquals(HttpStatus.OK, response.getStatusCode());
		BudgetDTO body = response.getBody();

		//Verify updated values
		assertEquals(BigDecimal.valueOf(5000.0), body.getIncome());
		assertEquals(BigDecimal.valueOf(155.0), body.getInsurance());
	}

	@Test
	public void testUpdateBudgetByMonth() {
		//Original DTO
		YearMonth testMonth = YearMonth.now();

		//DTO representing updated values
		BudgetDTO updatedDto = createBudgetDTO();
		updatedDto.setIncome(BigDecimal.valueOf(5000.0));
		updatedDto.setInsurance(BigDecimal.valueOf(155.0));

		//Mock service layer update call
		when(budgetSvc.updateBudgetByMonth(testMonth, updatedDto)).thenReturn(updatedDto);

		//Call controller
		ResponseEntity<BudgetDTO> response = controller.updateBudgetByMonth(testMonth, updatedDto);

		//Verify response status
		assertEquals(HttpStatus.OK, response.getStatusCode());
		BudgetDTO body = response.getBody();

		//Verify updated values
		assertEquals(BigDecimal.valueOf(5000.0), body.getIncome());
		assertEquals(BigDecimal.valueOf(155.0), body.getInsurance());
	}
}