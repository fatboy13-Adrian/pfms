import {useNavigate} from "react-router-dom";
import Button from "../components/Button";
import "../styles/Home.css"; 

export default function Home() {
    const navigate = useNavigate();

    return (
        <div className = "home-wrapper">
            <div className = "home-container">
                <h1>Personal Finance Management System</h1>
                <div className = "buttons-group-inline">
                   <Button type = "button" onClick = {() => navigate("/expenses")} className = "full-button">
                        Expense
                    </Button>
                    <Button type = "button" onClick = {() => navigate("/budgets")} className = "full-button">
                        Budget
                    </Button>
                    <Button type = "button" onClick = {() => navigate("/budgets/summary")} className = "full-button">
                        Summary
                    </Button>
                </div>
            </div>
        </div>
    );
};