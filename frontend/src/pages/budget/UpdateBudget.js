import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import Button from "../../components/Button";
import TextField from "../../components/TextField";
import "../../styles/budget/UpdateBudget.css";
import MonthInput from "../../components/MonthInput";

export default function UpdateBudget() {
    const navigate = useNavigate();
    const [searchMonth, setSearchMonth] = useState("");
    const [budgetId, setBudgetId] = useState(null);
    const [budget, setBudget] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState(false);

    const fieldGroups = [
        {
            title: "Income",
            fields: [
                {name: "income", label: "Income"}, 
            ],
        },

        {
            title: "Retirement",
            fields: [
                {name: "retirement", label: "Retirement"},
            ]
        },

        {
            title: "Utilities & Household Maintenance",
            fields: [
                {name: "mobilePhone", label: "Mobile Phone"},
                {name: "internet", label: "Internet"},
                {name: "utilities", label: "Utilities"},
            ]
        },

        {
            title: "Tax",
            fields: [
                {name: "tax", label: "Tax"},
            ]
        },

        {
            title: "Fixed Expenses",
            fields: [
                {name: "mortgage", label: "Mortgage"},
                {name: "debts", label: "Debts"},
                {name: "allowancesForParents", label: "Allowances For Parents"},
            ],
        },
        
        {
            title: "Transport",
            fields: [
                {name: "transport", label: "Transport"},
            ],
        },

        {
            title: "Food & Necessities",
            fields: [
                {name: "food", label: "Food"},
                {name: "groceries", label: "Groceries"},
                {name: "haircut", label: "Haircut"},
                {name: "medical", label: "Medical"},
            ],
        },

        {
            title: "Misc",
            fields: [
                {name: "misc", label: "Misc"},
            ],
        },

        {
            title: "Savings",
            fields: [
                {name: "savings", label: "Savings"},
            ],
        },
    ];

    const handleSearch = async () => {
        try {
            setLoading(true);
            setError("");
            setSuccess(false);
            const res = await axios.get(`http://localhost:8080/budgets/month/${searchMonth}`);
            const data = Array.isArray(res.data) ? res.data[0] : res.data;
            setBudget(data);
            setBudgetId(data.id);
        } catch {
            setError("No record found for this record.");
        } finally {
            setLoading(false);
      }
    };

    const onSubmit = async (e) => {
        e.preventDefault();
        try {
            setLoading(true);
            setError("");
            setSuccess(false);
            const payload = {
                ...budget,
                ...fieldGroups
                    .flatMap(g => g.fields)
                    .reduce((acc, f) => {
                        acc[f.name] = parseFloat(budget[f.name]) || 0;
                        return acc;
                   }, {})
            };

            await axios.put(`http://localhost:8080/budgets/${budgetId}`, payload);
            setSuccess(true);
            navigate("/budgets/retrieve");
        } catch (err) {
            setError(err.response?.data?.message || "Update failed");
        } finally {
            setLoading(false);
        }
    };

   const handleReset = () => {
    const resetValues = fieldGroups
                        .flatMap(group => group.fields)
                        .reduce((acc, field) => {
                            acc[field.name] = "";
                            return acc;
                        }, {});

        setBudget({...resetValues, month: budget.month});
        setError(null);
    };

    const onInputChange = (e) => {
        const {name, value} = e.target;
        setBudget((prev) => ({
            ...prev, [name]: value,
        }));
    };    

    return (
        <div className = "container-centered">
            <div className = "form-wrapper">
                <h2 className = "text-center">Update Budget Record</h2>
                <div className = "mb-4">
                    <MonthInput name = "searchMonth" value = {searchMonth} onChange = {(e) => setSearchMonth(e.target.value)} disabled = {loading}/>
                    <button type = "button" onClick = {handleSearch}>
                        Retrieve
                    </button>
                </div>

                {error && <div className = "alert alert-danger">{error}</div>}
                {success && <div className = "alert alert-success">Updated Successfully!</div>}
                {budget && (
                    <form onSubmit = {onSubmit} noValidate>
                        {fieldGroups.map(group => (
                            <div key = {group.title} className = "mb-4">
                                <h3>{group.title}</h3>
                                <div className = "field-group-container">
                                    {group.fields.map(field => (
                                        <TextField
                                            key = {field.name}
                                            name = {field.name}
                                            label = {field.label}
                                            type = {field.type}
                                            value = {budget[field.name] || ""}
                                            onChange = {onInputChange}
                                            disabled = {loading}
                                            className = "mb-3 full-width"
                                        />
                                    ))}
                                </div>
                            </div>
                        ))}

                        <div className = "button-wrapper">
                            <button type = "button" onClick = {() => navigate("/budgets/create")}>
                                Create
                            </button>
                            <button type="button" className="button" onClick={() => navigate("/budgets/retrieve")}>
                                Retrieve
                            </button>
                            <button type = "submit" disabled = {loading}>
                                {loading ? "Updating..." : "Update"}
                            </button>
                            <Button type = "button" onClick = {handleReset} disabled = {loading || success}>
                                Discard
                            </Button>
                            <button type="button" className="button" onClick={() => navigate("/budgets")}>
                                Budget
                            </button>
                            <button type = "button" onClick = {() => navigate("/home")}>
                                Home
                            </button>
                        </div>
                    </form>
                )}
            </div>
        </div>
    );
}