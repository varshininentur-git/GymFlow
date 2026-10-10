import { useEffect, useState } from 'react';
import PageHeader from '../components/PageHeader';
import { Empty, ErrorMessage, Loading, Notice } from '../components/Feedback';
import { api, localDateString } from '../services/api';

const today = localDateString();

export default function Attendance() {
  const [rows, setRows] = useState([]), [members, setMembers] = useState([]);
  const [memberId, setMemberId] = useState(''), [date, setDate] = useState(today), [checkId, setCheckId] = useState('');
  const [error, setError] = useState(''), [notice, setNotice] = useState(''), [busy, setBusy] = useState(false), [loading, setLoading] = useState(true);
  const load = () => { setLoading(true); setError(''); api.attendance({ ...(memberId ? { memberId } : {}), ...(date ? { date } : {}) }).then(setRows).catch(e => setError(e.message)).finally(() => setLoading(false)); };
  useEffect(() => { load(); }, [memberId, date]);
  useEffect(() => { api.members().then(setMembers).catch(e => setError(e.message)); }, []);
  const action = async kind => {
    if (!checkId) { setError('Choose a member first.'); return; }
    setBusy(true); setError(''); setNotice('');
    try { const record = kind === 'in' ? await api.checkIn(checkId) : await api.checkOut(checkId); setNotice(kind === 'in' ? `${record.member.fullName} checked in.` : `${record.member.fullName} checked out.`); load(); }
    catch (problem) { setError(problem.message); } finally { setBusy(false); }
  };
  return <>
    <PageHeader eyebrow="DAILY CHECK-INS" title="Attendance" description="Record member visits and review attendance history." />
    <ErrorMessage>{error}</ErrorMessage><Notice>{notice}</Notice>
    <div className="form-layout">
      <section className="form-card"><span className="eyebrow">QUICK CHECK-IN</span><h3>Member visit</h3>
        <label>Member<select value={checkId} onChange={event => setCheckId(event.target.value)}><option value="">Select a member</option>{members.map(member => <option value={member.id} key={member.id}>{member.fullName} · {member.memberCode}</option>)}</select></label>
        <div className="quick-actions"><button className="primary" disabled={busy || !checkId} onClick={() => action('in')}>{busy ? 'Working…' : 'Check in'}</button><button className="secondary" disabled={busy || !checkId} onClick={() => action('out')}>Check out</button></div>
        <p className="form-hint">Check-in requires an active membership. Duplicate open check-ins are rejected.</p>
      </section>
      <section className="content-card"><span className="eyebrow">VISIT HISTORY</span><h3>Attendance records</h3>
        <div className="inline-filters"><label className="sr-only" htmlFor="attendance-member">Filter by member</label><select id="attendance-member" value={memberId} onChange={event => setMemberId(event.target.value)}><option value="">All members</option>{members.map(member => <option value={member.id} key={member.id}>{member.fullName}</option>)}</select><label className="sr-only" htmlFor="attendance-date">Filter by date</label><input id="attendance-date" type="date" value={date} onChange={event => setDate(event.target.value)} /><button className="secondary" onClick={() => setDate('')}>Any date</button></div>
        <div className="table-wrap"><table><thead><tr><th>MEMBER</th><th>DATE</th><th>CHECK-IN</th><th>CHECK-OUT</th><th>VISIT</th></tr></thead><tbody>
          {loading && <tr><td colSpan="5"><Loading /></td></tr>}
          {!loading && rows.map(record => <tr key={record.id}><td><b>{record.member.fullName}</b><small>{record.member.memberCode}</small></td><td>{record.attendanceDate}</td><td>{new Date(record.checkInAt).toLocaleTimeString()}</td><td>{record.checkOutAt ? new Date(record.checkOutAt).toLocaleTimeString() : '—'}</td><td>{record.checkOutAt ? 'Complete' : 'In progress'}</td></tr>)}
          {!loading && !rows.length && <tr><td colSpan="5"><Empty>No attendance records for this filter.</Empty></td></tr>}
        </tbody></table></div>
      </section>
    </div>
  </>;
}
