export default function MonthInput({name, value, onChange, disabled}) {
    return (
        <div>
            <label htmlFor="month-input">Select Month: </label>

            <input
                id="month-input"
                type="month"
                name={name}
                value={value || ""}
                onChange={onChange}
                disabled={disabled}
            />
        </div>
    );
}