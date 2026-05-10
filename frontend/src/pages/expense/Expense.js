import {useNavigate} from "react-router-dom";
import Button from "../../components/Button"; 
import "../../styles/expense/Expense.css"; 

export default function Expense() {
    const navigate = useNavigate();

    return (
            <div className = "expense-wrapper">
                <div className = "expense-container">
                    <h1>Expense Calculator</h1>
                    <div className = "buttons-group-inline">
                       <Button type = "button" onClick = {() => navigate("/expenses/create")} className = "full-button">
                            Create
                        </Button>
                        <Button type = "button" onClick = {() => navigate("/expenses/retrieve")} className = "full-button">
                            Retrieve
                        </Button>
                        <Button type = "button" onClick = {() => navigate("/expenses/update")} className = "full-button">
                            Update
                        </Button>
                        <Button type = "button" onClick = {() => navigate("/home")} className = "full-button">
                            Home
                        </Button>
                    </div>
                </div>
            </div>
        );
};