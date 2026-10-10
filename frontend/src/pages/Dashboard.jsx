import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api, localDateString } from '../services/api';
import { formatCurrency } from '../services/currency';
import { Empty, ErrorMessage, Loading, Status } from '../components/Feedback';

export default function Dashboard() {
  const [data, setData] = useState(null), [report, setReport] = useState(null), [error, setError] = useState('');
  const [loading, setLoading] = useState(true), [reportLoading, setReportLoading] = useState(true);

  useEffect(() => {
    api.dashboard().then(setData).catch(e => setError(e.message)).finally(() => setLoading(false));
    const today = localDateString();
    api.paymentReport(today, today).then(setReport).catch(e => setError(e.message)).finally(() => setReportLoading(false));
  }, []);

  const stats = [
    ['TOTAL MEMBERS', data?.totalMembers ?? '—', '♙', 'blue'],
    ['ACTIVE MEMBERSHIPS', data?.activeMemberships ?? '—', '✓', 'green'],
    ['SUCCESSFUL COLLECTIONS', data ? formatCurrency(data.successfulPayments) : '—', '₹', 'purple'],
    ["TODAY’S ATTENDANCE", data?.attendanceToday ?? '—', '↗', 'amber'],
  ];

  return <>
    <section className="welcome"><div><span className="eyebrow">YOUR CLUB AT A GLANCE</span><h2>Every great day starts with a strong community.</h2><p>Here’s what’s happening at your gym today.</p></div><div className="welcome-art" aria-hidden="true">✦<i>✦</i><b>G</b></div></section>
    <ErrorMessage>{error && `Could not load live dashboard: ${error}`}</ErrorMessage>
    <section className="stats">{stats.map(([label, value, icon, color]) => <article className="stat" key={label}><div className={`stat-icon ${color}`} aria-hidden="true">{icon}</div><span>{label}</span><strong>{value}</strong><small>{loading ? 'Loading live data…' : 'Live from your gym records'}</small></article>)}</section>
    <section className="lower">
      <article className="panel"><div className="panel-title"><div><span className="eyebrow">TRANSACTION LOG</span><h3>Recent payments</h3></div><Link to="/transactions">View history →</Link></div>
        {reportLoading ? <Loading /> : report?.recentTransactions?.length ? <div className="mini-list">{report.recentTransactions.slice(0, 5).map(p => <div className="mini-row" key={p.id}><div className="person-dot" aria-hidden="true">₹</div><div className="person-info">{p.member.fullName}<small>{p.transactionReference} · {formatCurrency(p.amount)}</small></div><Status value={p.status} /></div>)}</div> : <Empty>No transaction records yet.</Empty>}
      </article>
      <article className="panel side-panel"><span className="eyebrow">QUICK START</span><h3>Keep your club moving.</h3><p>Manage your members, record attendance, and take membership payments.</p><Link className="quick-link" to="/members">＋ &nbsp; Register a member</Link><Link className="quick-link" to="/attendance">↗ &nbsp; Record attendance</Link><Link className="quick-link" to="/payments">₹ &nbsp; Take a payment</Link></article>
    </section>
  </>;
}
