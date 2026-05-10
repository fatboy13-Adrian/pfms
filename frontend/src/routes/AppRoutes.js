import {Routes, Route, Navigate} from "react-router-dom";
import Home from "../pages/Home";
import Expense from "../pages/expense/Expense";
import CreateExpense from "../pages/expense/CreateExpense";
import UpdateExpense from "../pages/expense/UpdateExpense";
import ExpenseDashboard from "../pages/expense/ExpenseDashboard";
import Budget from "../pages/budget/Budget";
import CreateBudget from "../pages/budget/CreateBudget";
import UpdateBudget from "../pages/budget/UpdateBudget";
import BudgetDashboard from "../pages/budget/BudgetDashboard";
import SummaryDashboard from "../pages/budget/SummaryDashboard";

export default function AppRoutes() {
    return (
        <Routes>
            <Route path  =  "/" element  =  {<Navigate to  =  "/home" replace/>}/>
            <Route path  =  "/home" element  =  {<Home/>}/>
            <Route path = "/expenses" element = {<Expense/>}/>
            <Route path = "/expenses/create" element = {<CreateExpense/>}/>
            <Route path = "/expenses/update" element = {<UpdateExpense/>}/>
            <Route path = "/expenses/retrieve" element = {<ExpenseDashboard/>}/>
            <Route path = "/budgets" element = {<Budget/>}/>
            <Route path = "/budgets/create" element = {<CreateBudget/>}/>
            <Route path = "/budgets/update" element = {<UpdateBudget/>}/>
            <Route path = "/budgets/retrieve" element = {<BudgetDashboard/>}/>
            <Route path = "/budgets/summary" element = {<SummaryDashboard/>}/>
            <Route path = "*" element = {<Navigate to = "/home" replace/>}/>
        </Routes>
    );
}