import "../../styles/budget/SummaryTable.css";

function cleanText(text) {
    if (!text) return "-";
    const trimmed = text.toString().trim();
    return trimmed.length === 0 ? "-" : trimmed;
}

function formatMoney(value) {
  if (value === null || value === undefined || value === "") return "-";
  return `$${Number(value).toFixed(2)}`;
}

export default function SummaryTable({summaries = [],}) {
  return (
    <div className = "summary-table-wrapper">
      <table className = "summary-table">
        <thead>
          <tr>
            <th>Year</th>
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
          {summaries.map((summary, index) => (
            <tr key={index}>
              <td>{cleanText(summary.year)}</td>
              <td>{formatMoney(summary.income)}</td>
              <td>{formatMoney(summary.retirement)}</td>
              <td>{formatMoney(summary.insurance)}</td>
              <td>{formatMoney(summary.mobilePhone)}</td>
              <td>{formatMoney(summary.internet)}</td>
              <td>{formatMoney(summary.utilities)}</td>
              <td>{formatMoney(summary.tax)}</td>
              <td>{formatMoney(summary.mortgage)}</td>
              <td>{formatMoney(summary.debt)}</td>
              <td>{formatMoney(summary.allowancesForParents)}</td>
              <td>{formatMoney(summary.transport)}</td>
              <td>{formatMoney(summary.food)}</td>
              <td>{formatMoney(summary.groceries)}</td>
              <td>{formatMoney(summary.haircut)}</td>
              <td>{formatMoney(summary.medical)}</td>
              <td>{formatMoney(summary.misc)}</td>
              <td>{formatMoney(summary.savings)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}