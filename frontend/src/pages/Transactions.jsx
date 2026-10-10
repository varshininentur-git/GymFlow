import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import PageHeader from '../components/PageHeader';
import { Empty, ErrorMessage, Loading, Status } from '../components/Feedback';
import { api } from '../services/api';
import { formatCurrency } from '../services/currency';

const initial = { reference: '', status: '', method: '', from: '', to: '' };

export default function Transactions() {
  const [rows, setRows] = useState([]), [filters, setFilters] = useState(initial), [error, setError] = useState(''), [loading, setLoading] = useState(true);
  const load = () => { setLoading(true); setError(''); api.payments(Object.fromEntries(Object.entries(filters).filter(([, value]) => value))).then(setRows).catch(e => setError(e.message)).finally(() => setLoading(false)); };
  useEffect(() => { load(); }, [filters]);
  const change = event => setFilters({ ...filters, [event.target.name]: event.target.value });
  return <>
    <PageHeader eyebrow="PAYMENT LEDGER" title="Transaction history" description="Search and filter recorded payment requests and gateway outcomes." action={<Link className="button-link" to="/payments">＋ New payment</Link>} />
    <ErrorMessage>{error}</ErrorMessage>
    <div className="inline-filters">
      <label className="sr-only" htmlFor="payment-reference">Transaction reference</label><input id="payment-reference" name="reference" placeholder="Transaction reference" value={filters.reference} onChange={change} />
      <label className="sr-only" htmlFor="payment-status">Payment status</label><select id="payment-status" name="status" value={filters.status} onChange={change}><option value="">All statuses</option>{['PENDING', 'SUCCESS', 'FAILED', 'CANCELLED'].map(value => <option key={value}>{value}</option>)}</select>
      <label className="sr-only" htmlFor="payment-method">Payment method</label><select id="payment-method" name="method" value={filters.method} onChange={change}><option value="">All methods</option>{['CASH', 'UPI', 'CARD', 'BANK_TRANSFER'].map(value => <option key={value}>{value}</option>)}</select>
      <label>From <input type="date" name="from" value={filters.from} onChange={change} /></label><label>To <input type="date" name="to" value={filters.to} onChange={change} /></label>
      <button className="secondary" onClick={() => setFilters(initial)}>Clear</button>
    </div>
    <div className="table-wrap"><table><thead><tr><th>TRANSACTION</th><th>MEMBER</th><th>PURPOSE</th><th>AMOUNT</th><th>METHOD</th><th>STATUS</th><th>REQUESTED</th><th></th></tr></thead><tbody>
      {loading && <tr><td colSpan="8"><Loading /></td></tr>}
      {!loading && rows.map(payment => <tr key={payment.id}><td><b>{payment.transactionReference}</b><small>{payment.gatewayReference || 'Mock reference pending'}</small></td><td>{payment.member.fullName}<small>{payment.member.memberCode}</small></td><td>{payment.purpose}</td><td>{formatCurrency(payment.amount)}</td><td>{payment.method}</td><td><Status value={payment.status} /></td><td>{new Date(payment.requestedAt).toLocaleString()}</td><td>{payment.status === 'SUCCESS' && <Link to={`/payments/${payment.id}/receipt`}>Receipt</Link>}</td></tr>)}
      {!loading && !rows.length && <tr><td colSpan="8"><Empty>No payments match your search.</Empty></td></tr>}
    </tbody></table></div>
  </>;
}
