import { useEffect, useState } from 'react';
import PageHeader from '../components/PageHeader';
import { Empty, ErrorMessage, Loading, Status } from '../components/Feedback';
import { api } from '../services/api';
import { formatCurrency } from '../services/currency';
import { useAuth } from '../context/AuthContext';

const initial = { name: '', durationDays: 30, price: '', description: '', active: true };

export default function Plans() {
  const { isAdmin } = useAuth();
  const [rows, setRows] = useState([]);
  const [form, setForm] = useState(null);
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);
  const [loading, setLoading] = useState(true);
  const load = () => { setLoading(true); setError(''); api.plans().then(setRows).catch(e => setError(e.message)).finally(() => setLoading(false)); };
  useEffect(() => { load(); }, []);
  const change = event => setForm({ ...form, [event.target.name]: event.target.type === 'checkbox' ? event.target.checked : event.target.value });
  const save = async event => {
    event.preventDefault(); setBusy(true); setError('');
    try { await api.savePlan({ ...form, durationDays: Number(form.durationDays), price: Number(form.price) }, form.id); setForm(null); load(); }
    catch (problem) { setError(problem.message); } finally { setBusy(false); }
  };
  const deactivate = async id => {
    if (!confirm('Deactivate this plan? Existing member history will remain unchanged.')) return;
    try { await api.deactivatePlan(id); load(); } catch (problem) { setError(problem.message); }
  };
  return <>
    <PageHeader eyebrow="MEMBERSHIP OPTIONS" title="Plans" description="Create and maintain your club’s membership plans." action={isAdmin && <button className="primary" onClick={() => setForm({ ...initial })}>＋ Create plan</button>} />
    <ErrorMessage>{error}</ErrorMessage>
    <div className="plan-grid">
      {loading && <Loading />}
      {!loading && rows.map(plan => <article className="plan-card" key={plan.id}>
        <div className="plan-mark" aria-hidden="true">✳</div><Status value={plan.active ? 'ACTIVE' : 'INACTIVE'} />
        <h3>{plan.name}</h3><div className="price">{formatCurrency(plan.price)}<small> / plan</small></div>
        <p>{plan.description || 'Flexible gym access.'}</p><div className="plan-foot">◷ &nbsp; {plan.durationDays} days</div>
        {isAdmin && <div className="table-actions"><button aria-label={`Edit ${plan.name}`} onClick={() => setForm({ ...plan })}>Edit</button>{plan.active && <button aria-label={`Deactivate ${plan.name}`} onClick={() => deactivate(plan.id)}>Deactivate</button>}</div>}
      </article>)}
      {!loading && !rows.length && <Empty>No plans yet. Create your first membership plan.</Empty>}
    </div>
    {form && <div className="overlay" onClick={() => setForm(null)}><form className="dialog" onSubmit={save} onClick={event => event.stopPropagation()}>
      <div className="panel-title"><h3>{form.id ? 'Edit plan' : 'Create a membership plan'}</h3><button type="button" aria-label="Close plan form" onClick={() => setForm(null)}>×</button></div>
      <ErrorMessage>{error}</ErrorMessage>
      <label>Plan name<input required name="name" value={form.name} onChange={change} /></label>
      <div className="form-grid"><label>Duration (days)<input required type="number" min="1" max="365" name="durationDays" value={form.durationDays} onChange={change} /></label><label>Price<input required type="number" min="0.01" step="0.01" name="price" value={form.price} onChange={change} /></label></div>
      <label>Description<input name="description" value={form.description || ''} onChange={change} /></label>
      {form.id && <label className="check-label"><input type="checkbox" name="active" checked={form.active} onChange={change} /> Plan is active</label>}
      <div className="dialog-actions"><button type="button" className="secondary" onClick={() => setForm(null)}>Cancel</button><button className="primary" disabled={busy}>{busy ? 'Saving…' : 'Save plan'}</button></div>
    </form></div>}
  </>;
}
