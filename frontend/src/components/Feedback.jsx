export function ErrorMessage({ children }) { return children ? <div className="alert" role="alert" aria-live="assertive">{children}</div> : null; }
export function Notice({ children }) { return children ? <div className="notice-box" role="status" aria-live="polite">{children}</div> : null; }
export function Loading() { return <div className="empty" role="status" aria-live="polite">Loading your gym records…</div>; }
export function Empty({ children = 'No records found.' }) { return <div className="empty" role="status">{children}</div>; }
export function Status({ value }) { const active = ['ACTIVE', 'SUCCESS'].includes(value); const warn = ['PENDING'].includes(value); return <span className={`badge ${active ? 'active' : warn ? 'pending' : 'muted'}`} aria-label={`Status: ${value}`}>{value}</span>; }
