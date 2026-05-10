export default function Button({ type, className = "", children, ...props }) {
  return (
    <button type = {type} className = {`custom-btn small-btn ${className}`} {...props}>
      {children}
    </button>
  );
}