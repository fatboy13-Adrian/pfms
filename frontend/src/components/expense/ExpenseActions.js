import "../styles/ExpenseTable.css";   

export default function ExpenseActions({expense, onUpdate, disabled}) {
    return (
        <div className = "action-buttons">
            <button
                onClick={() => onUpdate(expense)}
                disabled = {disabled}
            >
                Update
            </button>
        </div>
    );
}