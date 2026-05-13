import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import Button from "../../components/Button";
import TextField from "../../components/TextField";
import DateInput from "../../components/DateInput";
import "../../styles/expense/UpdateExpense.css";

export default function UpdateExpense() {
    const navigate = useNavigate();
    const [searchDate, setSearchDate] = useState("");
    const [expenseId, setExpenseId] = useState(null);
    const [expense, setExpense] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState(false);

    const fieldGroups = [
        {
            title: "Insurances",
            fields: [
                {name: "aia", label: "AIA", type: "number"},
                {name: "criticare", label: "Critical Care", type: "number"},
                {name: "termProtector", label: "Term Protector", type: "number"},
            ],
        },

        {
            title: "Utilities & Household Maintenance",
            fields: [
                {name: "mobilePhone", label: "Mobile Phone", type: "number"},
                {name: "internet", label: "Internet", type: "number"},
                {name: "utilities", label: "Utilities", type: "number"},
            ],
        },

        {
            title: "Tax",
            fields: [
                {name: "incomeTax", label: "Income Tax", type: "number"},
                {name: "propertyTax", label: "Property Tax", type: "number"},
            ],
        },
        
        {
            title: "Fixed Expenses",
            fields: [
                {name: "mortgage", label: "Mortgage", type: "number"},
                {name: "debt", label: "Debt", type: "number"},
                {name: "allowancesForParents", label: "Allowances For Parents", type: "number"},
            ],
        },

        {
            title: "Transport",
            fields: [
                {name: "publicTransport", label: "Public Transport", type: "number"},
                {name: "privateTransport", label: "Private Transport", type: "number"},
            ]
        },
        
        {
            title: "Food & Necessities",
            fields: [
                {name: "breakfast", label: "Breakfast", type: "number"},
                {name: "lunch", label: "Lunch", type: "number"},
                {name: "dinner", label: "Dinner", type: "number"},
                {name: "eatingOut", label: "Eating Out", type: "number"},
                {name: "groceries", label: "Groceries", type: "number"},
                {name: "haircut", label: "Haircut", type: "number"},
                {name: "medical", label: "Medical", type: "number"},
            ],
        },

        {
            title: "Miscellaneous",
            fields: [
                {name: "entertainment", label: "Entertainment", type: "number"},
                {name: "holiday", label: "Holiday", type: "number"},
                {name: "shopping", label: "Shopping", type: "number"},
                {name: "sports", label: "Sports", type: "number"},
                {name: "tech", label: "Tech", type: "number"},
                {name: "others", label: "Others", type: "number"},
            ],
        },
    ];
    
    const handleSearch = async () => {
        try {
            setLoading(true);
            setError("");
            setSuccess(false);
            const res = await axios.get(`http://localhost:8080/expenses/date/${searchDate}`);
            const data = Array.isArray(res.data) ? res.data[0] : res.data;
            setExpense(data);
            setExpenseId(data.id);
       } catch {
            setError("No record found for this date.");
       } finally {
            setLoading(false);
       }
   };

    const onInputChange = (e) => {
        const {name, value} = e.target;
        setExpense((prev) => ({
            ...prev, [name]: value,
        }));
   };

    const onSubmit = async (e) => {
        e.preventDefault();
        try {
            setLoading(true);
            setError("");
            setSuccess(false);
            const payload = {
                ...expense,
                ...fieldGroups
                    .flatMap(g => g.fields)
                    .reduce((acc, f) => {
                        acc[f.name] = parseFloat(expense[f.name]) || 0;
                        return acc;
                    }, {})
           };

            await axios.put(`http://localhost:8080/expenses/${expenseId}`, payload);
            setSuccess(true);
            navigate("/expenses/retrieve");
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

        setExpense({...resetValues, date: expense.date});
        setError(null);
    };

    return (
        <div className = "container-centered">
            <div className = "form-wrapper">
                <h2 className = "text-center">Update Expense Record</h2>
                <div className = "mb-4">
                    <DateInput name = "searchDate" value = {searchDate} onChange = {(e) => setSearchDate(e.target.value)} disabled = {loading}/>
                    <button type = "button" onClick = {handleSearch}>
                        Retrieve
                    </button>
                </div>

                {error && <div className = "alert alert-danger">{error}</div>}
                {success && <div className = "alert alert-success">Updated Existing Record Successfully!</div>}
                {expense && (
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
                                            value = {expense[field.name] || ""}
                                            onChange = {onInputChange}
                                            disabled = {loading}
                                            className = "mb-3 full-width"
                                        />
                                    ))}
                                </div>
                            </div>
                        ))}

                        <div className = "button-wrapper">
                            <Button type = "button" onClick = {() => navigate("/expenses/create")}>
                                New
                            </Button>
                            <Button type = "button" onClick = {() => navigate("/expenses/retrieve")}>
                                View
                            </Button>
                            <Button type = "submit" disabled = {loading}>
                                {loading ? "Updating..." : "Update"}
                            </Button>
                            <Button type = "button" onClick = {handleReset} disabled = {loading || success}>
                                Discard
                            </Button>
                            <Button type = "button" onClick = {() => navigate("/expenses")}>
                                Expense
                            </Button>
                            <Button type = "button" onClick = {() => navigate("/home")}>
                                Home
                            </Button>
                        </div>
                    </form>
                )}
            </div>
        </div>
    );
}