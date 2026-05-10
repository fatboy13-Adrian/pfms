import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import Button from "../../components/Button";
import TextField from "../../components/TextField";
import MonthInput from "../../components/MonthInput";
import "../../styles/budget/CreateBudget.css";

const initialState = {
    month: "",
    income: "",
    retirement: "",
    insurance: "",
    mobilePhone: "",
    internet: "",
    utilities: "",
    tax: "",
    mortgage: "",
    debt: "",
    allowancesForParents: "",
    transport: "",
    food: "",
    groceries: "",
    haircut: "",
    medical: "",
    misc: "",
    savings: "",
};

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

export default function CreateBudget() {
    const navigate = useNavigate();
    const [budget, setBudget] = useState(initialState);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);
    const [loading, setLoading] = useState(false);

    const onInputChange = (e) => {
        const {name, value} = e.target;
        setBudget((prev) => ({
            ...prev,
            [name]: value,
       }));
    };

    const onSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setSuccess(false);
        setLoading(true);

        const payload = {
            month: budget.month,
            ...Object.keys(initialState)
                .filter((k) => k !== "month")
                .reduce((acc, key) => {
                    const val = budget[key];
                    acc[key] = val === "" ? 0 : Number(val);
                    return acc;
                }, {}),
        };

        try {
            await axios.post("http://localhost:8080/budgets/createBudget", payload);
            setSuccess(true);
            navigate("/budgets/retrieve", {state: {refresh: true}});
        } catch (err) {
            console.error(err);
            const message = err?.response?.data?.message || err?.response?.data || 
            err?.message || "Server error";
            setError(message);
        } finally {
            setLoading(false);
        }
    };

    const handleReset = () => {
        setBudget(initialState);
        setError(null);
    };

    return (
        <div className = "container-centered">
            <div className = "form-wrapper">
                <h2 className = "text-center">Create Budget Record</h2>

                {error && <div className = "alert alert-danger">{error}</div>}
                {success && <div className = "alert alert-success">Created Successfully!</div>}

                <form onSubmit = {onSubmit} noValidate>

                    <MonthInput name="month" value={budget.month} onChange={onInputChange} disabled={loading}/>
                    {fieldGroups.map((group) => (
                        <div key = {group.title} className = "mb-4">
                            <h3>{group.title}</h3>
                            <div className = "field-group-container">
                                {group.fields.map((field) => (
                                    <TextField
                                        key = {field.name}
                                        name = {field.name}
                                        label = {field.label}
                                        type = "number"
                                        value = {budget[field.name]}
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
                        <button type = "button" className = "button" onClick = {() => navigate("/budgets/retrieve")}>
                            Retrieve
                        </button>
                        <button type = "button" className = "button" onClick={() => navigate("/budgets/update")}>
                            Update
                        </button>
                        <Button type = "button" onClick = {handleReset} disabled = {loading || success}>
                            Discard
                        </Button>
                        <button type = "button" className = "button" onClick = {() => navigate("/budgets")}>
                            Budget
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