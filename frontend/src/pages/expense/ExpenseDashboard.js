import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import ExpenseTable from "../../components/expense/ExpenseTable";
import "../../styles/expense/ExpenseDashboard.css";

export default function ExpenseDashboard() {
    const [expenses, setExpenses] = useState([]);
    const [date, setDate] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();

    useEffect(() => {
        setLoading(true);
        setError("");
        axios.get("http://localhost:8080/expenses")
            .then((res) => {
                setExpenses(res.data);
            })
            .catch((error) => {
                setError(error.response?.data?.message || "Failed to retrieve expense list.");
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    const filteredExpenses = expenses
    .filter((expense) => expense.date)
    .filter((expense) => {
        const matchesSelectedDate = date === "" || expense.date.substring(0, 10) === date;
        return matchesSelectedDate;
    }).sort((a, b) => new Date(b.date) - new Date(a.date));

    return (
        <div className = "expenseDashboard-center">
            <h1>View Expense Records</h1>
            <div className = "filter-section">
                <input type = "date" value = {date} onChange = {(e) => setDate(e.target.value)} className = "input" max = {new Date().toISOString().split("T")[0]}/>
            </div>
            {loading ? (
                <p>Loading...</p>
            ) : error ? (
                <div className = "error-message" role = "alert">
                    {error}
                </div>
            ) : (
                <>
                    <div className = "expense-table-wrapper">
                        <ExpenseTable expenses = {filteredExpenses}/>
                    </div>

                    <div className = "buttons-center">
                        <button className = "custom-btn" onClick={() => navigate("/expenses/create")}>
                            New
                        </button>
                        <button className = "custom-btn" onClick={() => navigate("/expenses/update")}>
                            Update
                        </button>
                        <button className = "custom-btn" onClick = {() => setDate("")}>
                            Clear Filter
                        </button>
                        <button className = "custom-btn" onClick={() => navigate("/expenses")}>
                            Expense
                        </button>
                        <button className = "custom-btn" onClick={() => navigate("/home")}>
                            Home
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}