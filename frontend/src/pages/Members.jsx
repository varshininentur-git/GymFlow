import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import PageHeader from '../components/PageHeader';
import { Empty, ErrorMessage, Loading, Status } from '../components/Feedback';
import { api } from '../services/api';
import {useAuth} from '../context/AuthContext';

export default function Members() {
  const {isAdmin}=useAuth();
  const [rows, setRows] = useState([]);
  const [search, setSearch] = useState('');
  const [error, setError] = useState('');
  const [notice, setNotice] = useState('');
  const [loading, setLoading] = useState(true);
  const load = () => { setLoading(true); api.members(search).then(setRows).catch(e => setError(e.message)).finally(() => setLoading(false)); };
  useEffect(() => { load(); }, [search]);
  const remove = async id => {
    if (!confirm('Delete this member? Records with attendance or payment history are protected.')) return;
    try { await api.deleteMember(id); setNotice('Member deleted.'); load(); }
    catch (e) { setError(e.message); }
  };
  return <>
    <PageHeader eyebrow="YOUR PEOPLE" title="Members" description="Get to know the people who make your gym." action={<Link className="button-link" to="/members/new">＋ Add member</Link>} />
    <ErrorMessage>{error}</ErrorMessage>{notice && <div className="notice-box">{notice}</div>}
    <div className="toolbar"><label className="sr-only" htmlFor="member-search">Search name or member ID</label><input id="member-search" placeholder="⌕  Search name or member ID" value={search} onChange={e => setSearch(e.target.value)} /><span>{rows.length} members</span></div>
    <div className="table-wrap"><table><thead><tr><th>MEMBER</th><th>MEMBER ID</th><th>CATEGORY</th><th>PLAN</th><th>STATUS</th><th></th></tr></thead><tbody>
      {!loading && rows.map(m => <tr key={m.id}><td><b>{m.fullName}</b><small>{m.email}</small></td><td>{m.memberCode}</td><td>{m.category}</td><td>{m.plan?.name || '—'}</td><td><Status value={m.membershipStatus} /></td><td className="table-actions"><Link aria-label={`Edit ${m.fullName}`} to={`/members/${m.id}/edit`}>Edit</Link>{isAdmin&&<button aria-label={`Delete ${m.fullName}`} onClick={() => remove(m.id)}>Delete</button>}</td></tr>)}
      {loading && <tr><td colSpan="6"><Loading /></td></tr>}
      {!loading && !rows.length && <tr><td colSpan="6"><Empty>No members found. Add your first member to get started.</Empty></td></tr>}
    </tbody></table></div>
  </>;
}
