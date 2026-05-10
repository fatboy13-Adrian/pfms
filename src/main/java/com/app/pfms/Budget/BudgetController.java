package com.app.pfms.Budget;
import java.io.ByteArrayInputStream;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
@RequestMapping("/budgets")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Budgets", description = "Personal Finance Management System (PFMS)")
public class BudgetController {
	@Autowired	//Injects budget service dependency
	private BudgetService budgetSvc;

	@PostMapping("/createBudget")
	@Operation(summary = "Create a new budget")
	public ResponseEntity<BudgetDTO> createBudget(@RequestBody BudgetDTO dto) {
		//Calls the service layer to create a new budget record & returns HTTP 200 OK with the created budget record in the response body
		BudgetDTO createdBudget = budgetSvc.createBudget(dto);
		return ResponseEntity.ok(createdBudget);
	}

	@GetMapping
	@Operation(summary = "Retrieve all budgets")
	public ResponseEntity<List<BudgetDTO>> retrieveBudgets() {
		//Calls the service layer to fetch all budget records &  returns HTTP 200 OK with the list of records
		List<BudgetDTO> dtos = budgetSvc.retrieveBudgets();
		return ResponseEntity.ok(dtos);
	}

	@GetMapping("month/{month}")
	@Operation(summary = "Retrieve budget by month")
	public ResponseEntity<BudgetDTO> retrieveBudget(@PathVariable YearMonth month) {
		//Calls the service layer to fetch the budget record by month & returns HTTP 200 OK with the record info
		BudgetDTO dto = budgetSvc.retrieveBudget(month);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/summary")
	@Operation(summary = "Retrieve all yearly budget summaries")
	public ResponseEntity<List<BudgetSummaryDTO>> retrieveYearlySummaries() {
		//Calls the service layer to fetch all summary records & returns HTTP 200 OK with the record info
		List <BudgetSummaryDTO> summaries = budgetSvc.retrieveYearlySummaries();		
		return ResponseEntity.ok(summaries);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update an existing budget")
	public ResponseEntity<BudgetDTO> updateBudgetById(@PathVariable Long id, @RequestBody BudgetDTO dto) {
		//Calls the service layer to fetch the expense record by ID & returns HTTP 200 OK with the record info
		BudgetDTO updatedDto = budgetSvc.updateBudgetById(id, dto);
		return ResponseEntity.ok(updatedDto);
	}

	@PutMapping("month/{month}")
	@Operation(summary = "Update an existing budget")
	public ResponseEntity<BudgetDTO> updateBudgetByMonth(@PathVariable YearMonth month, @RequestBody BudgetDTO dto) {
		//Calls the service layer to fetch the expense record by ID & returns HTTP 200 OK with the record info
		BudgetDTO updatedDto = budgetSvc.updateBudgetByMonth(month, dto);
		return ResponseEntity.ok(updatedDto);
	}

	@GetMapping("/export/{year}")
	public ResponseEntity<InputStreamResource> exportBudgetRecords(@PathVariable Year year) {
		//Generate excel file from budget service & wrap it as input stream resource
		InputStreamResource file = new InputStreamResource(budgetSvc.exportBudgetRecords(year));

		//Return excel file as downloadable response
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = Budget Records For Year " + year + ".xlsx")
				.contentType(MediaType.parseMediaType(
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(file);
	}

	@GetMapping("/summary/export")
	public ResponseEntity<InputStreamResource> exportYearlySummaries() {
		ByteArrayInputStream file = budgetSvc.exportYearlySummaries();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename= Yearly Summaries.xlsx");

		return ResponseEntity.ok()
				.headers(headers)
				.contentType(MediaType.parseMediaType(
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
				))
				.body(new InputStreamResource(file));
	}
}