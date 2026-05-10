export default function DateInput({name, label = "Date", value, onChange, disabled}) {
  return (
    <div className = "mb-3">
      <label htmlFor = {name}>Date</label>
        <input
          id = {name}
          type = "date"
          name = {name}
          value = {value}
          onChange = {onChange}
          disabled = {disabled}
          max = {new Date().toISOString().split("T")[0]}
          className = "form-control"
          required
        />
    </div>
  );
}