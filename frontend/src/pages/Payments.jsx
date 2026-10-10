import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import PageHeader from '../components/PageHeader';
import { Empty, ErrorMessage, Loading, Notice, Status } from '../components/Feedback';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { formatCurrency } from '../services/currency';

export default function Payments() {
  const { isAdmin } = useAuth();
  const [members, setMembers] = useState([]), [plans, setPlans] = useState([]), [payments, setPayments] = useState([]);
  const [pending, setPending] = useState(null), [error, setError] = useState(''), [notice, setNotice] = useState('');
  const [busy, setBusy] = useState(false), [loading, setLoading] = useState(true), [lookupsLoading, setLookupsLoading] = useState(true);
  const [form, setForm] = useState({ memberId: '', purpose: 'MEMBERSHIP_PURCHASE', planId: '', method: 'CASH', amount: '' });
  const load = () => { setLoading(true); api.payments().then(setPayments).catch(e => setError(e.message)).finally(() => setLoading(false)); };
  useEffect(() => {
    Promise.all([api.members(), api.plans()]).then(([memberRows, planRows]) => { setMembers(memberRows); setPlans(planRows); }).catch(e => setError(e.message)).finally(() => setLookupsLoading(false));
    load();
  }, []);
  const change = event => setForm({ ...form, [event.target.name]: event.target.value });
  const create = async event => {
    event.preventDefault(); setBusy(true); setError(''); setNotice('');
    try {
      const payment = await api.createPayment({ memberId: Number(form.memberId), planId: form.purpose === 'OUTSTANDING_FEE' ? null : Number(form.planId), purpose: form.purpose, method: form.method, idempotencyKey: crypto.randomUUID(), ...(form.purpose === 'OUTSTANDING_FEE' ? { amount: Number(form.amount) } : {}) });
      setPending(payment); setNotice('Payment request saved as pending. Choose the mock gateway result to finish this demonstration transaction.'); load();
    } catch (problem) { setError(problem.message); } finally { setBusy(false); }
  };
  const process = async approved => {
    if (!pending) return;
    setBusy(true); setError('');
    try { const payment = await api.processPayment(pending.id, approved); setPending(payment); setNotice(payment.status === 'SUCCESS' ? 'Mock payment approved and recorded successfully.' : 'Mock payment declined. No membership dates or fees were changed.'); load(); }
    catch (problem) { setError(problem.message); } finally { setBusy(false); }
  };
  const cancel = async () => {
    if (!pending || !confirm('Cancel this pending payment request?')) return;
    setBusy(true); setError('');
    try { const payment = await api.cancelPayment(pending.id); setPending(payment); setNotice('Payment request cancelled.'); load(); }
    catch (problem) { setError(problem.message); } finally { setBusy(false); }
  };
  return <>
    <PageHeader eyebrow="MEMBERSHIP CHECKOUT" title="Payments" description="Record a member payment through the labelled mock gateway." action={<Link className="button-link" to="/transactions">Transaction history →</Link>} />
    <div className="notice-box" role="note">Mock gateway simulation only. It never contacts a bank or moves money. Do not enter any card or banking credentials.</div>
    <ErrorMessage>{error}</ErrorMessage><Notice>{notice}</Notice>
    <div className="form-layout">
      <form className="form-card" onSubmit={create}><span className="eyebrow">NEW PAYMENT REQUEST</span><h3>Payment details</h3>
        <label>Member<select required disabled={lookupsLoading} name="memberId" value={form.memberId} onChange={change}><option value="">{lookupsLoading ? 'Loading members…' : 'Select a registered member'}</option>{members.map(member => <option value={member.id} key={member.id}>{member.fullName} · {member.memberCode}</option>)}</select></label>
        <label>Payment purpose<select name="purpose" value={form.purpose} onChange={change}><option value="MEMBERSHIP_PURCHASE">Membership purchase</option><option value="RENEWAL">Membership renewal</option><option value="OUTSTANDING_FEE">Outstanding fee</option></select></label>
        {form.purpose !== 'OUTSTANDING_FEE' ? <label>Plan<select required name="planId" value={form.planId} onChange={change}><option value="">{lookupsLoading ? 'Loading plans…' : 'Select plan'}</option>{plans.filter(plan => plan.active).map(plan => <option value={plan.id} key={plan.id}>{plan.name} · {formatCurrency(plan.price)}</option>)}</select></label> : <label>Amount due<input type="number" required min="0.01" step="0.01" name="amount" value={form.amount} onChange={change} /><small>Backend checks the registered outstanding balance.</small></label>}
        <label>Payment method<select name="method" value={form.method} onChange={change}><option>CASH</option><option>UPI</option><option>CARD</option><option>BANK_TRANSFER</option></select></label>
        <button className="primary" disabled={busy || lookupsLoading || !members.length || (form.purpose !== 'OUTSTANDING_FEE' && !plans.some(plan => plan.active))}>{busy ? 'Working…' : 'Create payment request'}</button>
      </form>
      <section className="content-card"><span className="eyebrow">GATEWAY DECISION</span><h3>{pending ? 'Mock payment gateway' : 'Waiting for a request'}</h3>
        {pending ? <div className="payment-result"><div className="mini-row"><div className="person-info">{pending.member.fullName}<small>{pending.transactionReference}</small></div><Status value={pending.status} /></div><p><b>{formatCurrency(pending.amount)}</b> · {pending.method} · {pending.purpose}</p>
          {pending.status === 'PENDING' && isAdmin ? <><p>Choose the simulated issuer response:</p><div className="quick-actions"><button className="primary" disabled={busy} onClick={() => process(true)}>Simulate approved</button><button className="secondary" disabled={busy} onClick={() => process(false)}>Simulate declined</button><button className="secondary" disabled={busy} onClick={cancel}>Cancel request</button></div></> : pending.status === 'SUCCESS' ? <Link className="button-link" to={`/payments/${pending.id}/receipt`}>Open printable receipt</Link> : <button className="secondary" onClick={() => setPending(null)}>Start a new request</button>}
        </div> : <p className="empty">A successful transaction updates membership dates only after the mock gateway approves it.</p>}
      </section>
    </div>
    <section className="content-card"><div className="panel-title"><div><span className="eyebrow">LATEST ACTIVITY</span><h3>Recent payments</h3></div><Link to="/transactions">See all →</Link></div>
      {loading ? <Loading /> : payments.length ? <div className="table-wrap"><table><thead><tr><th>REFERENCE</th><th>MEMBER</th><th>AMOUNT</th><th>METHOD</th><th>STATUS</th></tr></thead><tbody>{payments.slice(0, 6).map(payment => <tr key={payment.id}><td>{payment.transactionReference}</td><td>{payment.member.fullName}</td><td>{formatCurrency(payment.amount)}</td><td>{payment.method}</td><td><Status value={payment.status} /></td></tr>)}</tbody></table></div> : <Empty>No recent payment requests.</Empty>}
    </section>
  </>;
}
