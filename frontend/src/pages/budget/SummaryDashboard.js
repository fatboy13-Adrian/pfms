import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import SummaryTable from "../../components/budget/SummaryTable";
import "../../styles/budget/SummaryDashboard.css";

export default function SummaryDashboard() {
    const [summary, setSummary] = useState([]);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        setLoading(true);
        setError("");

        axios.get("http://localhost:8080/budgets/summary")
            .then((res) => setSummary(res.data))
            .catch((error) => {
                setError(
                    error.response?.data?.message ||
                    "Failed to retrieve summary records."
                );
            })
            .finally(() => setLoading(false));
    }, []);

    const downloadExcel = async () => {
        try {
            const response = await fetch("http://localhost:8080/budgets/summary/export");

            if (!response.ok) {
                throw new Error("Failed to download file");
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);

            const link = document.createElement("a");
            link.href = url;
            link.download = "Yearly_Summary.xlsx";

            document.body.appendChild(link);
            link.click();
            link.remove();

            window.URL.revokeObjectURL(url);
        } catch (err) {
            setError(err.message || "Download failed");
        }
    };
    return (
    <div className = "summaryDashboard-center">
        <h1>View Yearly Summary Records</h1>
        {loading ? (
            <p>Loading...</p>
        ) : error ? (
            <div className = "error-message" role = "alert">
                {error}
            </div>
        ) : (
            <>
                {summary.length === 0 ? (
                    <p>No yearly summary records found.</p>
                ) : (
                    <SummaryTable summaries = {summary} />
                )}
                <div className = "buttons-center">
                    <button className = "custom-btn" onClick = {() => navigate("/budgets")}>
                        Budget
                    </button>
                    <button className = "custom-btn" onClick = {() => navigate("/expenses")}>
                        Expense
                    </button>
                    <button onClick = {downloadExcel}>
                        Download
                    </button>
                    <button className = "custom-btn" onClick = {() => navigate("/home")}>
                        Home
                    </button>
                </div>
            </>
        )}
    </div>
);
}