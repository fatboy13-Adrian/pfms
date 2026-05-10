import "../../styles/expense/ExpenseTable.css";

function cleanText(text) {
  if (!text) return "-";
  const trimmed = text.toString().trim();
  return trimmed.length === 0 ? "-" : trimmed;
}

function formatMoney(value) {
    if (value === null || value === undefined || value === "") return "-";
    return `$${Number(value).toFixed(2)}`;
}

export default function ExpenseTable({expenses = [], onUpdate, actionsDisabled}) {
    return (
        <div className = "expense-table-wrapper">
            <table className = "expense-table">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>AIA</th>
                        <th>Criticare</th>
                        <th>Term Protector</th>
                        <th>Mobile Phone</th>
                        <th>Internet</th>
                        <th>Utilities</th>
                        <th>Income Tax</th>
                        <th>Property Tax</th>
                        <th>Mortgage</th>
                        <th>Debt</th>
                        <th>Allowances for Parents</th>
                        <th>Public Transport</th>
                        <th>Private Transport</th>
                        <th>Breakfast</th>
                        <th>Lunch</th>
                        <th>Dinner</th>
                        <th>Eating Out</th>
                        <th>Grocery</th>
                        <th>Medical</th>
                        <th>Entertainment</th>
                        <th>Holiday</th>
                        <th>Shopping</th>
                        <th>Sports</th>
                        <th>Tech</th>
                        <th>Others</th>
                    </tr>
                </thead>

                <tbody>
                    {(expenses || []).map((expense) => (
                        <tr key = {expense.id}>
                            <td>{cleanText(expense.date)}</td>
                            <td>{formatMoney(expense.aia)}</td>
                            <td>{formatMoney(expense.criticare)}</td>
                            <td>{formatMoney(expense.termProtector)}</td>
                            <td>{formatMoney(expense.mobilePhone)}</td>
                            <td>{formatMoney(expense.internet)}</td>
                            <td>{formatMoney(expense.utilities)}</td>
                            <td>{formatMoney(expense.incomeTax)}</td>
                            <td>{formatMoney(expense.propertyTax)}</td>
                            <td>{formatMoney(expense.mortgage)}</td>
                            <td>{formatMoney(expense.debt)}</td>
                            <td>{formatMoney(expense.allowancesForParents)}</td>
                            <td>{formatMoney(expense.publicTransport)}</td>
                            <td>{formatMoney(expense.privateTransport)}</td>
                            <td>{formatMoney(expense.breakfast)}</td>
                            <td>{formatMoney(expense.lunch)}</td>
                            <td>{formatMoney(expense.dinner)}</td>
                            <td>{formatMoney(expense.eatingOut)}</td>
                            <td>{formatMoney(expense.grocery)}</td>
                            <td>{formatMoney(expense.medical)}</td>
                            <td>{formatMoney(expense.entertainment)}</td>
                            <td>{formatMoney(expense.holiday)}</td>
                            <td>{formatMoney(expense.shopping)}</td>
                            <td>{formatMoney(expense.sports)}</td>
                            <td>{formatMoney(expense.tech)}</td>
                            <td>{formatMoney(expense.others)}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}