import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import PageHeader from '../components/PageHeader';
import { ErrorMessage, Loading } from '../components/Feedback';
import { api, localDateString } from '../services/api';

const blank = {
  fullName: '', email: '', phone: '', category: 'GIRLS',
  joiningDate: localDateString(), address: ''
};

export default function MemberForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState(blank);
  const [membership, setMembership] = useState(null);
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);
  const [loading, setLoading] = useState(Boolean(id));

  useEffect(() => {
    if (id) api.member(id)
      .then(member => { setForm({ ...blank, ...member }); setMembership(member); })
      .catch(problem => setError(problem.message))
      .finally(() => setLoading(false));
  }, [id]);

  const change = event => setForm({ ...form, [event.target.name]: event.target.value });
  const save = async event => {
    event.preventDefault();
    setBusy(true);
    setError('');
    try {
      const payload = {
        fullName: form.fullName, email: form.email, phone: form.phone,
        category: form.category, dateOfBirth: form.dateOfBirth || null,
        address: form.address || '', joiningDate: form.joiningDate
      };
      await api.saveMember(payload, id);
      navigate('/members');
    } catch (problem) { setError(problem.message); }
    finally { setBusy(false); }
  };

  return <>
    <PageHeader eyebrow="YOUR PEOPLE" title={id ? 'Edit member' : 'Add member'} description="Keep member contact details current. Membership changes are recorded through payments." />
    <ErrorMessage>{error}</ErrorMessage>
    {loading ? <Loading /> : <form className="form-card form-card-narrow" onSubmit={save}>
      {membership && <div className="notice-box">Current membership: {membership.plan?.name || 'No plan'} · {membership.membershipStatus}{membership.membershipEnd ? ` through ${membership.membershipEnd}` : ''}. Plans and membership dates change only after successful payment.</div>}
      <label>Full name<input required name="fullName" value={form.fullName} onChange={change} /></label>
      <div className="form-grid">
        <label>Email<input required type="email" name="email" value={form.email} onChange={change} /></label>
        <label>Phone<input required name="phone" value={form.phone} onChange={change} /></label>
      </div>
      <div className="form-grid">
        <label>Category<select name="category" value={form.category} onChange={change}><option value="GIRLS">Girls</option><option value="BOYS">Boys</option></select></label>
        <label>Joining date<input required type="date" name="joiningDate" value={form.joiningDate} onChange={change} /></label>
      </div>
      <label>Address<input name="address" value={form.address || ''} onChange={change} /></label>
      {!id && <p className="form-hint">Create the member profile first, then use Payments to purchase a membership plan.</p>}
      <div className="dialog-actions"><Link className="secondary" to="/members">Cancel</Link><button className="primary" disabled={busy}>{busy ? 'Saving…' : 'Save member'}</button></div>
    </form>}
  </>;
}
