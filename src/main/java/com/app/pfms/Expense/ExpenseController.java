package com.app.pfms.Expense;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/expenses")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Expenses", description = "Personal Finance Management System (PFMS)")
public class ExpenseController {
	//Injects the service dependency automatically
	@Autowired	
	private ExpenseService svc;

	@PostMapping("/createExpense")
	@Operation(summary = "Create a new expense")
	public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO dto) {
		//Calls the service layer to create a new record & returns HTTP 200 OK with the created record in the response body
		ExpenseDTO createdExpense = svc.createExpense(dto);
		return ResponseEntity.ok(createdExpense);
	}

	@GetMapping
	@Operation(summary = "Retrieve all expenses")
	public ResponseEntity<List<ExpenseDTO>> retrieveExpenses() {
		//Calls the service layer to fetch all records & returns HTTP 200 OK with the list of records
		List<ExpenseDTO> dtos = svc.retrieveExpenses();
		return ResponseEntity.ok(dtos);
	}

	@GetMapping("/date/{date}")
	@Operation(summary = "Retrieve expense by date")
	public ResponseEntity<ExpenseDTO> retrieveExpense(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		//Calls the service layer to fetch the record by date & returns HTTP 200 OK with the record info 
		ExpenseDTO dto = svc.retrieveExpense(date);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update an existing expense")
	public ResponseEntity<ExpenseDTO> updateExpenseById(@PathVariable Long id, @RequestBody ExpenseDTO dto) {
		//Calls the service layer to update the record & returns HTTP 200 OK with the updated record info
		ExpenseDTO updatedDto = svc.updateExpenseById(id, dto);
		return ResponseEntity.ok(updatedDto);
	}

	@PutMapping("/date/{date}")
	@Operation(summary = "Update an existing expense")
	public ResponseEntity<ExpenseDTO> updateExpenseByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestBody ExpenseDTO dto) {
		ExpenseDTO updatedDto = svc.updateExpenseByDate(date, dto);
		return ResponseEntity.ok(updatedDto);
	}
}