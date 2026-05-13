import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import BudgetTable from "../../components/budget/BudgetTable";
import "../../styles/budget/BudgetDashboard.css";
import MonthInput from "../../components/MonthInput";

export default function BudgetDashboard() {
    const [budgets, setBudgets] = useState([]);
    const [month, setMonth] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();

    useEffect(() => {
        setLoading(true);
        setError("");
        axios.get("http://localhost:8080/budgets")
            .then((res) => {
                setBudgets(res.data);
            })
            .catch((error) => {
                setError(error.response?.data?.message || "Failed to retrieve budget records.");
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    const filteredBudgets = budgets
    .filter((budget) => budget.month)
    .filter((budget) => {
        const budgetMonth = new Date(budget.month);
        const matchedMonths = month === "" || budgetMonth.toISOString().slice(0,7) === month;
        return matchedMonths
    });

    const downloadExcel = async () => {
        const year = new Date().getFullYear();
        const response = await fetch(`http://localhost:8080/budgets/export/${year}`, {
            method: "GET"
        });

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.download = `Budget Records For Year ${year}.xlsx`;
        document.body.appendChild(link);
        link.click();
        link.remove();
        window.URL.revokeObjectURL(url);
    };

    return (
        <div className = "budgetDashboard-center">
            <h1>View Budget Records</h1>
            <div className = "filter-section">
                <MonthInput name = "month" value = {month} onChange = {(e) => setMonth(e.target.value)}/>
            </div>
            {loading ? (
                <p>Loading...</p>
            ) : error ? (
                <div className = "error-message" role = "alert">
                    {error}
                </div>
            ) : (
                <>
                    <div className = "month-table-wrapper">
                        <BudgetTable budgets = {filteredBudgets}/>
                    </div>
                    <div className = "buttons-center">
                        <button className = "custom-btn" onClick = {() => navigate("/budgets/create")}>
                            New
                        </button>
                        <button className = "custom-btn" onClick = {() => navigate("/budgets/update")}>
                            Update
                        </button>
                        <button onClick = {downloadExcel}>
                            Download
                        </button>
                        <button className = "custom-btn" onClick = {() => setMonth("")}>
                            Clear Filter
                        </button>
                        <button className = "custom-btn" onClick = {() => navigate("/budgets")}>
                            Budget
                        </button>
                        <button className = "custom-btn" onClick = {() => navigate("/home")}>
                            Home
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}