import { useEffect, useState } from 'react';
import PageHeader from '../components/PageHeader';
import { Empty, ErrorMessage, Loading, Status } from '../components/Feedback';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';

const empty = { name: '', email: '', phone: '', specialization: '', availability: '', active: true };

export default function Trainers() {
  const { isAdmin } = useAuth();
  const [rows, setRows] = useState([]);
  const [search, setSearch] = useState('');
  const [active, setActive] = useState('true');
  const [form, setForm] = useState(null);
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);
  const [loading, setLoading] = useState(true);
  const load = () => {
    setLoading(true); setError('');
    api.trainers(search, active === '' ? undefined : active === 'true').then(setRows).catch(e => setError(e.message)).finally(() => setLoading(false));
  };
  useEffect(() => { load(); }, [search, active]);
  const change = event => setForm({ ...form, [event.target.name]: event.target.value });
  const save = async event => {
    event.preventDefault(); setBusy(true); setError('');
    try { await api.saveTrainer(form, form.id); setForm(null); load(); }
    catch (problem) { setError(problem.message); } finally { setBusy(false); }
  };
  const deactivate = async trainer => {
    if (!confirm(`Deactivate ${trainer.name}?`)) return;
    try { await api.deactivateTrainer(trainer.id); load(); } catch (problem) { setError(problem.message); }
  };
  return <>
    <PageHeader eyebrow="PEOPLE WHO COACH" title="Trainers" description="Manage your coaching team and their availability." action={isAdmin && <button className="primary" onClick={() => setForm({ ...empty })}>＋ Add trainer</button>} />
    <ErrorMessage>{error}</ErrorMessage>
    <div className="inline-filters"><label className="sr-only" htmlFor="trainer-search">Search name or specialization</label><input id="trainer-search" placeholder="Search name or specialization" value={search} onChange={event => setSearch(event.target.value)} /><label className="sr-only" htmlFor="trainer-status">Filter trainers by status</label><select id="trainer-status" value={active} onChange={event => setActive(event.target.value)}><option value="true">Active trainers</option><option value="false">Inactive trainers</option><option value="">All trainers</option></select></div>
    <div className="table-wrap"><table><thead><tr><th>TRAINER</th><th>PHONE</th><th>SPECIALIZATION</th><th>AVAILABILITY</th><th>STATUS</th><th></th></tr></thead><tbody>
      {loading && <tr><td colSpan="6"><Loading /></td></tr>}
      {!loading && rows.map(trainer => <tr key={trainer.id}><td><b>{trainer.name}</b><small>{trainer.email}</small></td><td>{trainer.phone}</td><td>{trainer.specialization}</td><td>{trainer.availability}</td><td><Status value={trainer.active ? 'ACTIVE' : 'INACTIVE'} /></td><td className="table-actions">{isAdmin && <><button aria-label={`Edit ${trainer.name}`} onClick={() => setForm({ ...trainer })}>Edit</button>{trainer.active && <button aria-label={`Deactivate ${trainer.name}`} onClick={() => deactivate(trainer)}>Deactivate</button>}</>}</td></tr>)}
      {!loading && !rows.length && <tr><td colSpan="6"><Empty>No trainers match these filters.</Empty></td></tr>}
    </tbody></table></div>
    {form && <div className="overlay" onClick={() => setForm(null)}><form className="dialog" onSubmit={save} onClick={event => event.stopPropagation()}>
      <div className="panel-title"><h3>{form.id ? 'Edit trainer' : 'Add trainer'}</h3><button type="button" aria-label="Close trainer form" onClick={() => setForm(null)}>×</button></div>
      <ErrorMessage>{error}</ErrorMessage>
      <label>Name<input required name="name" value={form.name} onChange={change} /></label>
      <div className="form-grid"><label>Email<input required type="email" name="email" value={form.email} onChange={change} /></label><label>Phone<input required name="phone" value={form.phone} onChange={change} /></label></div>
      <label>Specialization<input required name="specialization" value={form.specialization} onChange={change} /></label>
      <label>Availability<input required name="availability" placeholder="Weekdays, 8am–2pm" value={form.availability} onChange={change} /></label>
      <div className="dialog-actions"><button type="button" className="secondary" onClick={() => setForm(null)}>Cancel</button><button className="primary" disabled={busy}>{busy ? 'Saving…' : 'Save trainer'}</button></div>
    </form></div>}
  </>;
}
