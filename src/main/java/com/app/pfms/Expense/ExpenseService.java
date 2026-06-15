package com.app.pfms.Expense;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.pfms.Budget.BudgetService;
import com.app.pfms.Exceptions.Expense.DateAlreadyExistsException;
import com.app.pfms.Exceptions.Expense.DateNotFoundException;
import com.app.pfms.Exceptions.Expense.ExpenseNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    //Data access layer for expense
    private final ExpenseDAO dao;

    //Service used to recalculate & refresh budget data
    private final BudgetService budgetSvc;

    @Transactional
    public ExpenseDTO createExpense(ExpenseDTO dto) {
        //Checks uf date already exists
        dao.findByDate(dto.getDate()).ifPresent(existing -> {
            throw new DateAlreadyExistsException(dto.getDate());
        });

        //Convert DTO to expense & save to DB
        Expense saved = dao.save(toEntity(dto));

        //Refresh budget after new expense record is created
        budgetSvc.refreshBudget(dto.getDate());

        //Convert saved expense to DTO & return response
        return toDTO(saved);
    }

    public List<ExpenseDTO> retrieveExpenses() {
        //Fetch all records from DB
        List<Expense> expenses = dao.findAll();

        //Prepare DTO list for response
        List<ExpenseDTO> result = new java.util.ArrayList<>();

        //Convert expense into DTO
        for (Expense e : expenses) {
            result.add(toDTO(e));
        }

        //Return all records in DTO
        return result;
    }

    public ExpenseDTO retrieveExpense(LocalDate date) {
        //Fetch expense by date or throw exception
        Expense e = dao.findByDate(date)
        .orElseThrow(() -> new DateNotFoundException(date));

        //Convert expense to DTO & return
        return toDTO(e);
    }

    @Transactional
    public ExpenseDTO updateExpenseById(Long id, ExpenseDTO dto) {
        //Fetch expense by date or throw exception
        Expense existing = dao.findById(id)
        .orElseThrow(() -> new ExpenseNotFoundException("Record not found"));

        //Apply updated values from DTO into existing record
        applyUpdates(existing, dto);

        //Save updated record to DB
        Expense saved = dao.save(existing);

        //Refresh budget after an existing record is updated
        budgetSvc.refreshBudget(dto.getDate());

        //Return updated DTO
        return toDTO(saved);
    }

    @Transactional
    public ExpenseDTO updateExpenseByDate(LocalDate date, ExpenseDTO dto) {
        Expense existing = dao.findByDate(date)
        .orElseThrow(() -> new DateNotFoundException(date));
        applyUpdates(existing, dto);
        Expense saved = dao.save(existing);
        budgetSvc.refreshBudget(dto.getDate());
        return toDTO(saved);
    }

    private void applyUpdates(Expense e, ExpenseDTO dto) {
        //Update all fields from DTO into expense
        e.setDate(dto.getDate());
        e.setAia(dto.getAia());
        e.setCriticare(dto.getCriticare());
        e.setTermProtector(dto.getTermProtector());
        e.setMobilePhone(dto.getMobilePhone());
        e.setInternet(dto.getInternet());
        e.setUtilities(dto.getUtilities());
        e.setIncomeTax(dto.getIncomeTax());
        e.setPropertyTax(dto.getPropertyTax());
        e.setMortgage(dto.getMortgage());
        e.setDebt(dto.getDebt());
        e.setAllowancesForParents(dto.getAllowancesForParents());
        e.setPublicTransport(dto.getPublicTransport());
        e.setPrivateTransport(dto.getPrivateTransport());
        e.setBreakfast(dto.getBreakfast());
        e.setLunch(dto.getLunch());
        e.setDinner(dto.getDinner());
        e.setEatingOut(dto.getEatingOut());
        e.setGroceries(dto.getGroceries());
        e.setHaircut(dto.getHaircut());
        e.setMedical(dto.getMedical());
        e.setEntertainment(dto.getEntertainment());
        e.setHoliday(dto.getHoliday());
        e.setShopping(dto.getShopping());
        e.setSports(dto.getSports());
        e.setTech(dto.getTech());
        e.setOthers(dto.getOthers());
    }

    private ExpenseDTO toDTO(Expense e) {
        //Create empty DTO
        ExpenseDTO dto = new ExpenseDTO();

        //Map expense to DTO fields
        dto.setId(e.getId());
        dto.setDate(e.getDate());
        dto.setAia(e.getAia());
        dto.setCriticare(e.getCriticare());
        dto.setTermProtector(e.getTermProtector());
        dto.setMobilePhone(e.getMobilePhone());
        dto.setInternet(e.getInternet());
        dto.setUtilities(e.getUtilities());
        dto.setIncomeTax(e.getIncomeTax());
        dto.setPropertyTax(e.getPropertyTax());
        dto.setMortgage(e.getMortgage());
        dto.setDebt(e.getDebt());
        dto.setAllowancesForParents(e.getAllowancesForParents());
        dto.setPublicTransport(e.getPublicTransport());
        dto.setPrivateTransport(e.getPrivateTransport());
        dto.setBreakfast(e.getBreakfast());
        dto.setLunch(e.getLunch());
        dto.setDinner(e.getDinner());
        dto.setEatingOut(e.getEatingOut());
        dto.setGroceries(e.getGroceries());
        dto.setHaircut(e.getHaircut());
        dto.setMedical(e.getMedical());
        dto.setEntertainment(e.getEntertainment());
        dto.setHoliday(e.getHoliday());
        dto.setShopping(e.getShopping());
        dto.setSports(e.getSports());
        dto.setTech(e.getTech());
        dto.setOthers(e.getOthers());

        //Return mapped DTO
        return dto;
    }

    private Expense toEntity(ExpenseDTO dto) {
        //Create new expense
        Expense e = new Expense();

        //Map DTO to expense fields
        e.setDate(dto.getDate());
        e.setAia(dto.getAia());
        e.setCriticare(dto.getCriticare());
        e.setTermProtector(dto.getTermProtector());
        e.setMobilePhone(dto.getMobilePhone());
        e.setInternet(dto.getInternet());
        e.setUtilities(dto.getUtilities());
        e.setIncomeTax(dto.getIncomeTax());
        e.setPropertyTax(dto.getPropertyTax());
        e.setMortgage(dto.getMortgage());
        e.setDebt(dto.getDebt());
        e.setAllowancesForParents(dto.getAllowancesForParents());
        e.setPublicTransport(dto.getPublicTransport());
        e.setPrivateTransport(dto.getPrivateTransport());
        e.setBreakfast(dto.getBreakfast());
        e.setLunch(dto.getLunch());
        e.setDinner(dto.getDinner());
        e.setEatingOut(dto.getEatingOut());
        e.setGroceries(dto.getGroceries());
        e.setHaircut(dto.getHaircut());
        e.setMedical(dto.getMedical());
        e.setEntertainment(dto.getEntertainment());
        e.setHoliday(dto.getHoliday());
        e.setShopping(dto.getShopping());
        e.setSports(dto.getSports());
        e.setTech(dto.getTech());
        e.setOthers(dto.getOthers());

        //Return expense ready for persistance
        return e;
    }
}