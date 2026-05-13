import {useNavigate} from "react-router-dom";
import Button from "../../components/Button"; 
import "../../styles/budget/Budget.css"; 

export default function Budget() {
    const navigate = useNavigate();

    return (
            <div className = "budget-wrapper">
                <div className = "budget-container">
                    <h1>Bduget Calculator</h1>
                    <div className = "buttons-group-inline">
                        <Button type = "button" onClick = {() => navigate("/budgets/create")} className = "full-button">
                            New
                        </Button>
                        <Button type = "button" onClick = {() => navigate("/budgets/retrieve")} className = "full-button">
                            View
                        </Button>
                        <Button type = "button" onClick = {() => navigate("/budgets/update")} className = "full-button">
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