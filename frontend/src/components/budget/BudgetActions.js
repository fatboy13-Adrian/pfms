import "../../styles/budget/BudgetTable.css";   

export default function BudgetActions({budget, onUpdate, disabled}) {
    return (
        <div className = "action-buttons">
            <button
                onClick={() => onUpdate(budget)}
                disabled = {disabled}
            >
                Update
            </button>
        </div>
    );
}