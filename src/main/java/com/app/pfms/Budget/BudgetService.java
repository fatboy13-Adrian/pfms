package com.app.pfms.Budget;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.app.pfms.Exceptions.Budget.BudgetNotFoundException;
import com.app.pfms.Exceptions.Budget.MonthAlreadyExistsException;
import com.app.pfms.Exceptions.Budget.MonthNotFoundException;
import com.app.pfms.Exceptions.ExportExcelFailedException;
import com.app.pfms.Expense.ExpenseService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetService {
    //Data access layer for budget
    private final BudgetDAO dao;

    //Service used to compute expense breakdowns
    private final ExpenseService expSvc;

    @Transactional
    public BudgetDTO createBudget(BudgetDTO dto) {
        //Checks if month already exists
        if (dao.findByMonth(dto.getMonth()).isPresent()) throw new MonthAlreadyExistsException(dto.getMonth());
        
        //Create new budget
        Budget b = new Budget();

        //Copy user input fields
        applyUserFields(b, dto);

        //Auto recalculate expense breakdown snapshot
        applySnapshot(b);

        //Persist to DB
        Budget saved = dao.save(b);
        
        //Convert budget to DTO for response
        return toDTO(saved);
    }

    public List<BudgetDTO> retrieveBudgets() {
        //Prep DTO list
        List<BudgetDTO> result = new ArrayList<>();

        //Convert budget to DTO
        for (Budget b : dao.findAll()) {
            result.add(toDTO(b));
        }

        //Return all budgets
        return result;
    }

    public BudgetDTO retrieveBudget(YearMonth month) {
        //Fetch budget or throw exception
        Budget b = findBudget(month);

        //Convert & return DTO
        return toDTO(b);
    }

    public List<BudgetSummaryDTO> retrieveYearlySummaries() {
        //Retrieve all budget records
        List<Budget> budgets = dao.findAll();

        //Store yearly summaries
        List<BudgetSummaryDTO> summaries = new ArrayList<>();

        //Loop through all budgets
        for (Budget b : budgets) {
            int year = b.getMonth().getYear();

            //Find existing summary for year
            BudgetSummaryDTO existingDto = null;

            for (BudgetSummaryDTO dto : summaries) {
                if (dto.getYear() == year) {
                    existingDto = dto;
                    break;
                }
            }

            //If not found, create new DTO
            if (existingDto == null) {
                existingDto = new BudgetSummaryDTO();
                existingDto.setYear(year);

                //Initialize fields to ZERO
                initializeSummary(existingDto);
                summaries.add(existingDto);
            }

            //Accumulate values
            calculateYearlyBudget(existingDto, b);
        }

        //Return all yearly summaries
        return summaries;
    }

    @Transactional
    public BudgetDTO updateBudgetById(Long id, BudgetDTO dto) {
        //Fetch existing record
        Budget existing = dao.findById(id)
        .orElseThrow(() -> new BudgetNotFoundException("Record not found"));

        //Copy user input fields
        applyUserFields(existing, dto);

        //Auto recalculate expense breakdown snapshot
        applySnapshot(existing);

        //Save updated record
        Budget updated = dao.save(existing);
        
        //Return updated DTO
        return toDTO(updated);
    }

    @Transactional
    public BudgetDTO updateBudgetByMonth(YearMonth month, BudgetDTO dto) {
        Budget existing = findBudget(month);

        //Prevent duplicate month conflict when changing month
        if (!month.equals(dto.getMonth()) && dao.findByMonth(dto.getMonth()).isPresent()) throw new MonthAlreadyExistsException(dto.getMonth());
        
        applyUserFields(existing, dto);
        applySnapshot(existing);
        Budget updated = dao.save(existing);
        return toDTO(updated);
    }

    private void applyUserFields(Budget b, BudgetDTO dto) {
        //Set month, income & retirement
         b.setMonth(dto.getMonth());
        b.setIncome(dto.getIncome());
        b.setRetirement(dto.getRetirement());
    }

    public void applySnapshot(Budget b) {
        //Extract month for calculation
        YearMonth month = b.getMonth();

        //Fetch expense totals from expense service
        BigDecimal insurance = expSvc.calculateInsurance(month);
        BigDecimal mobile = expSvc.calculateMobilePhone(month);
        BigDecimal internet = expSvc.calculateInternet(month);
        BigDecimal utilities = expSvc.calculateUtilities(month);
        BigDecimal tax = expSvc.calculateTax(month);
        BigDecimal mortgage = expSvc.calculateMortgage(month);
        BigDecimal debt = expSvc.calculateDebt(month);
        BigDecimal parents = expSvc.calculateAllowancesForParents(month);
        BigDecimal transport = expSvc.calculateTransport(month);
        BigDecimal food = expSvc.calculateFood(month);
        BigDecimal groceries = expSvc.calculateGroceries(month);
        BigDecimal haircut = expSvc.calculateHaircut(month);
        BigDecimal medical = expSvc.calculateMedical(month);
        BigDecimal misc = expSvc.calculateMisc(month);

        //Store calculated snapshots into budget
        b.setInsurance(insurance);
        b.setMobilePhone(mobile);
        b.setInternet(internet);
        b.setUtilities(utilities);
        b.setTax(tax);
        b.setMortgage(mortgage);
        b.setDebt(debt);
        b.setAllowancesForParents(parents);
        b.setTransport(transport);
        b.setFood(food);
        b.setGroceries(groceries);
        b.setHaircut(haircut);
        b.setMedical(medical);
        b.setMisc(misc);

        //Calculate savings after subtracting all expenses
        b.setSavings(calculateSavings(b));
    }

    private BudgetDTO toDTO(Budget b) {
        //Create DTO
        BudgetDTO dto = new BudgetDTO();

        //Map all fields from budget to DTO
        dto.setId(b.getId());
        dto.setMonth(b.getMonth());
        dto.setIncome(b.getIncome());
        dto.setRetirement(b.getRetirement());
        dto.setInsurance(b.getInsurance());
        dto.setMobilePhone(b.getMobilePhone());
        dto.setInternet(b.getInternet());
        dto.setUtilities(b.getUtilities());
        dto.setTax(b.getTax());
        dto.setMortgage(b.getMortgage());
        dto.setDebt(b.getDebt());
        dto.setAllowancesForParents(b.getAllowancesForParents());
        dto.setTransport(b.getTransport());
        dto.setFood(b.getFood());
        dto.setGroceries(b.getGroceries());
        dto.setHaircut(b.getHaircut());
        dto.setMedical(b.getMedical());
        dto.setMisc(b.getMisc());
        dto.setSavings(b.getSavings());

        //Return DTO
        return dto;
    }

    private Budget findBudget(YearMonth month) {
        //Fetch budget or throw exception if not found
        return dao.findByMonth(month)
        .orElseThrow(() -> new MonthNotFoundException(month));
    }

    @Transactional
    public void refreshCurrentMonthBudget() {
        //Get current month
        YearMonth currentMonth = YearMonth.now();

        //Find by month or return null if not found
        Budget budget = dao.findByMonth(currentMonth)
        .orElse(null);

        //Exit sliently if no records
        if (budget == null) return;

        //Auto recalculate expense breakdown snapshot
        applySnapshot(budget);

        //Save updated record
        dao.save(budget);
    }

    public BigDecimal calculateSavings(Budget b) {
        //Income subtract expenses
        return b.getIncome()
                .subtract(
                        b.getRetirement()
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
                        .add(b.getMisc())
                );
    }

    private void initializeSummary(BudgetSummaryDTO dto) {
        dto.setIncome(BigDecimal.ZERO);
        dto.setRetirement(BigDecimal.ZERO);
        dto.setInsurance(BigDecimal.ZERO);
        dto.setMobilePhone(BigDecimal.ZERO);
        dto.setInternet(BigDecimal.ZERO);
        dto.setUtilities(BigDecimal.ZERO);
        dto.setTax(BigDecimal.ZERO);
        dto.setMortgage(BigDecimal.ZERO);
        dto.setDebt(BigDecimal.ZERO);
        dto.setAllowancesForParents(BigDecimal.ZERO);
        dto.setTransport(BigDecimal.ZERO);
        dto.setFood(BigDecimal.ZERO);
        dto.setGroceries(BigDecimal.ZERO);
        dto.setHaircut(BigDecimal.ZERO);
        dto.setMedical(BigDecimal.ZERO);
        dto.setMisc(BigDecimal.ZERO);
        dto.setSavings(BigDecimal.ZERO);
    }

    public void calculateYearlyBudget(BudgetSummaryDTO dto, Budget b) {
        //Calculate total budget for the current year
        dto.setIncome(dto.getIncome().add(b.getIncome()));
        dto.setRetirement(dto.getRetirement().add(b.getRetirement()));
        dto.setInsurance(dto.getInsurance().add(b.getInsurance()));
        dto.setMobilePhone(dto.getMobilePhone().add(b.getMobilePhone()));
        dto.setInternet(dto.getInternet().add(b.getInternet()));
        dto.setUtilities(dto.getUtilities().add(b.getUtilities()));
        dto.setTax(dto.getTax().add(b.getTax()));
        dto.setMortgage(dto.getMortgage().add(b.getMortgage()));
        dto.setDebt(dto.getDebt().add(b.getDebt()));
        dto.setAllowancesForParents(dto.getAllowancesForParents().add(b.getAllowancesForParents()));
        dto.setTransport(dto.getTransport().add(b.getTransport()));
        dto.setFood(dto.getFood().add(b.getFood()));
        dto.setGroceries(dto.getGroceries().add(b.getGroceries()));
        dto.setHaircut(dto.getHaircut().add(b.getHaircut()));
        dto.setMedical(dto.getMedical().add(b.getMedical()));
        dto.setMisc(dto.getMisc().add(b.getMisc()));
        dto.setSavings(dto.getSavings().add(b.getSavings()));        
    }

    private List <Budget> getYearlyBudgets(Year year) {
        //Find by year or throw exception if not found
		if (year == null) throw new IllegalArgumentException("Year cannot be null");
        
        //Jan to Dec
		YearMonth start = YearMonth.of(year.getValue(), 1);
		YearMonth end = YearMonth.of(year.getValue(), 12);

        //Fetch year range
		return dao.findByMonthBetween(start, end);
	}
    
    //Helper method to export budget records
	public ByteArrayInputStream exportBudgetRecords(Year year) {
		//Retrieve all budget records
	    List<Budget> budgets = getYearlyBudgets(year);

		//Build & return excel file as byte array input stream
		return buildExcel(budgets, "Budget Records For Year " + year);
	}

	//Helper method to build excel file
	private ByteArrayInputStream buildExcel(List <Budget> budgets, String sheetName) {
		//Create workbook & output stream using try catch block to auto close
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			//Create new worksheet in workbook with given file name
			Sheet sheet = workbook.createSheet(sheetName);

			//Create style & font for header cells
			CellStyle headerStyle = workbook.createCellStyle();
			Font font = workbook.createFont();

			//Create, set font & alignment as center for header to bold
			font.setBold(true);
        	headerStyle.setFont(font);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);

			//Create currency style & set alignment as center
			CellStyle currencyStyle = workbook.createCellStyle();
			currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("[$$-en-SG]#,##0.00"));
			currencyStyle.setAlignment(HorizontalAlignment.CENTER);

			//Define column headers for excel sheet
			String[] columns = {
				"Month",
				"Income",
				"Retirement", 
				"Insurance",
                "Mobile Phone",
                "Internet",
                "Utilities",
                "Tax",
                "Mortgage",
                "Debt",
                "Allowances For Parents",
                "Transport",
                "Food",
                "Groceries",
                "Haircut",
                "Medical",
                "Misc",
                "Savings"
			};

			//Create header row at index 0
			Row header = sheet.createRow(0);
			for (int i = 0; i < columns.length; i++) {
				//Create cell, set column title, apply bold header style
				Cell cell = header.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerStyle);
			}

			//Loop through budget list using index
			for (int i = 0; i < budgets.size(); i++) {
				//Retrieve budget object at current index
				Budget b = budgets.get(i);

				//Create new row starting from row 1 (row 0 = header row)
				Row row = sheet.createRow(i + 1);

				//Fill each column with budget data
				row.createCell(0).setCellValue(b.getMonth().toString());
                
				BigDecimal[] values = {
					b.getIncome(),
                    b.getRetirement(),
                    b.getInsurance(),
                    b.getMobilePhone(),
					b.getInternet(),
                    b.getUtilities(),
                    b.getTax(),
                    b.getMortgage(),
                    b.getDebt(),
                    b.getAllowancesForParents(),
                    b.getTransport(),
                    b.getFood(),
					b.getGroceries(),
					b.getHaircut(),
					b.getMedical(),
					b.getMisc(),
                    b.getSavings()
				};

				//Loop to create cells & apply $ format
				for (int j = 0; j < values.length; j++) {
					Cell cell = row.createCell(j + 1);
					cell.setCellValue(values[j].doubleValue());
					cell.setCellStyle(currencyStyle);
				}
			}

			//Auto size all columns for better readability
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			//Write workbook data into output stream
			workbook.write(output);

			//Convert output to input stream for download
			return new ByteArrayInputStream(output.toByteArray());
		} catch (Exception e) {
			//Handle error
			throw new ExportExcelFailedException("Excel export failed", e);
		}
	}

    public ByteArrayInputStream exportYearlySummaries() {
        List <BudgetSummaryDTO> summaries = retrieveYearlySummaries();
        return buildYearlySummaryExcel(summaries);
    }

    private ByteArrayInputStream buildYearlySummaryExcel(List <BudgetSummaryDTO> summaries) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Yearly Summaries");
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(
                    workbook.createDataFormat().getFormat("[$$-en-SG]#,##0.00")
            );
            currencyStyle.setAlignment(HorizontalAlignment.CENTER);

            String[] columns = {
                    "Year",
                    "Income",
                    "Retirement",
                    "Insurance",
                    "Mobile Phone",
                    "Internet",
                    "Utilities",
                    "Tax",
                    "Mortgage",
                    "Debt",
                    "Allowances For Parents",
                    "Transport",
                    "Food",
                    "Groceries",
                    "Haircut",
                    "Medical",
                    "Misc",
                    "Savings"
            };

            //Header row
            Row header = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            //Data rows
            for (int i = 0; i < summaries.size(); i++) {
                BudgetSummaryDTO dto = summaries.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(dto.getYear());
                BigDecimal[] values = {
                                    dto.getIncome(),
                                    dto.getRetirement(),
                                    dto.getInsurance(),
                                    dto.getMobilePhone(),
                                    dto.getInternet(),
                                    dto.getUtilities(),
                                    dto.getTax(),
                                    dto.getMortgage(),
                                    dto.getDebt(),
                                    dto.getAllowancesForParents(),
                                    dto.getTransport(),
                                    dto.getFood(),
                                    dto.getGroceries(),
                                    dto.getHaircut(),
                                    dto.getMedical(),
                                    dto.getMisc(),
                                    dto.getSavings()
                            };

                for (int j = 0; j < values.length; j++) {
                    Cell cell = row.createCell(j + 1);
                    cell.setCellValue(values[j].doubleValue());
                    cell.setCellStyle(currencyStyle);
                }
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(output);
            return new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            throw new ExportExcelFailedException("Yearly summary export failed", e);
        }
    }
}