import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import Button from "../../components/Button";
import TextField from "../../components/TextField";
import "../../styles/expense/CreateExpense.css";
import DateInput from "../../components/DateInput";

const getTodayDate = () => new window.Date().toISOString().split('T')[0];

export default function CreateExpense () {
    const navigate = useNavigate();

    const [expense, setExpense] = useState({
        date: getTodayDate(),
        aia: "",
        criticare: "",
        termProtector: "",
        mobilePhone: "",
        internet: "",
        utilities: "",
        incomeTax: "",
        propertyTax: "",
        mortgage: "",
        debt: "",
        allowancesForParents: "",
        publicTransport: "",
        privateTransport: "",
        breakfast: "",
        lunch: "",
        dinner: "",
        eatingOut: "",
        groceries: "",
        haircut: "",
        medical: "",
        entertainment: "",
        holiday: "",
        shopping: "",
        sports: "",
        tech: "",
        others: "",
    });

    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);
    const [loading, setLoading] = useState(false);

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
    ]

    const onInputChange = (e) => {
        const {name, value} = e.target;
        setExpense((prevExpense) => ({
            ...prevExpense, 
            [name]: value
        }));
    };

   const onSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(false);
    setLoading(true);

    const payload = {
        date: expense.date,
        ...fieldGroups
            .flatMap(g => g.fields)
            .reduce((acc, f) => {
                const val = expense[f.name];
                acc[f.name] = val === "" || val === null || val === undefined ? 0 : Number(val);
                return acc;
            }, {})
    };

    try {
        await axios.post("http://localhost:8080/expenses/createExpense", payload);
        setSuccess(true);
        navigate("/expenses/retrieve", {state: {refresh: true}});
    } catch (err) {
        if (err.response?.status === 409) {
            setError("Record with this date already exists.");
        } else if (err.response.data?.message) {
            setError(err.response.data.message);
        } else {
            setError("Network error. Please check your connection.");
        }
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
        <div className = "container-centered expense-page">
            <div className = "form-wrapper">
            <h2 className = "text-center">Create Expense Record</h2>
            {error && <div className = "alert alert-danger">{error}</div>}
            {success && <div className = "alert alert-success">Created Successfully!</div>}
            <form onSubmit = {onSubmit} noValidate>
                <DateInput name = "date" value = {expense.date} onChange = {onInputChange} disabled = {loading}/>
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
                                value = {expense[field.name]}
                                onChange = {onInputChange}
                                disabled = {loading}
                                className = "mb-3 full-width" 
                                />
                            ))}
                        </div>
                    </div>
                ))}

                <div className = "button-wrapper">
                <Button type = "submit" disabled = {loading || success}>
                    {loading ? "Creating..." : "Create"}
                </Button>
                <button className = "button" onClick={() => navigate("/expenses/retrieve")}>
                    Retrieve
                </button>
                <Button type = "button" onClick = {() => navigate("/expenses/update")} className = "full-button">
                    Update
                </Button>
                <Button type="button" onClick = {handleReset} disabled = {loading || success}>
                    Discard
                </Button>
                <button className = "button" onClick={() => navigate("/expenses")}>
                    Expense
                </button>
                <Button type = "button" onClick = {() => navigate("/home")} disabled = {loading || success}>
                    Home
                </Button>
                </div>
            </form>
            </div>
        </div>
    );
}