import "../styles/TextField.css";
//The TextField component accepts several props for customizable behavior
export default function TextField({id, label, name, type = "text", value, onChange, autoComplete, disabled = false, required = false, className = ""}) 
{
  return (
    <div className="form-row">
      <label htmlFor={id} className="form-label">{label}</label>
      <input
        id={id}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        disabled={disabled}
        autoComplete={autoComplete}
        required={required}
        className={`form-input ${className}`.trim()}
      />
    </div>
  );
}