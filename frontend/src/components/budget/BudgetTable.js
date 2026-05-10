import "../../styles/budget/BudgetTable.css";

function cleanText(text) {
  if (!text) return "-";
  const trimmed = text.toString().trim();
  return trimmed.length === 0 ? "-" : trimmed;
}

function formatMoney(value) {
    if (value === null || value === undefined || value === "") return "-";
    return `$${Number(value).toFixed(2)}`;
}

export default function BudgetTable({budgets = [], onUpdate, actionsDisabled}) {
    return (
        <div className = "budget-table-wrapper">
            <table className = "budget-table">
                <thead>
                    <tr>
                        <th>Month</th>
                        <th>Income</th>
                        <th>Retirement</th>
                        <th>Insurance</th>
                        <th>Mobile Phone</th>
                        <th>Internet</th>
                        <th>Utilities</th>
                        <th>Tax</th>
                        <th>Mortgage</th>
                        <th>Debt</th>
                        <th>Allowances For Parents</th>
                        <th>Transport</th>
                        <th>Food</th>
                        <th>Groceries</th>
                        <th>Haircut</th>
                        <th>Medical</th>
                        <th>Misc</th>
                        <th>Savings</th>
                    </tr>
                </thead>

                <tbody>
                    {budgets.map((budget =>
                        <tr key = {budget.id}>
                            <td>{cleanText(budget.month)}</td>
                            <td>{formatMoney(budget.income)}</td>
                            <td>{formatMoney(budget.retirement)}</td>
                            <td>{formatMoney(budget.insurance)}</td>
                            <td>{formatMoney(budget.mobilePhone)}</td>
                            <td>{formatMoney(budget.internet)}</td>
                            <td>{formatMoney(budget.utilities)}</td>
                            <td>{formatMoney(budget.tax)}</td>
                            <td>{formatMoney(budget.mortgage)}</td>
                            <td>{formatMoney(budget.debt)}</td>
                            <td>{formatMoney(budget.allowancesForParents)}</td>
                            <td>{formatMoney(budget.transport)}</td>
                            <td>{formatMoney(budget.food)}</td>
                             <td>{formatMoney(budget.groceries)}</td>
                             <td>{formatMoney(budget.haircut)}</td>
                             <td>{formatMoney(budget.medical)}</td>
                             <td>{formatMoney(budget.misc)}</td>
                            <td>{formatMoney(budget.savings)}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}