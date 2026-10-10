import { useEffect, useState } from 'react';
import PageHeader from '../components/PageHeader';
import { Empty, ErrorMessage, Loading, Status } from '../components/Feedback';
import { api, localDateString } from '../services/api';
import { formatCurrency } from '../services/currency';

const today = localDateString(), currentMonth = today.slice(0, 7);

export default function Reports() {
  const [from, setFrom] = useState(today), [to, setTo] = useState(today);
  const [data, setData] = useState(null), [daily, setDaily] = useState(null), [monthly, setMonthly] = useState(null), [attendance, setAttendance] = useState(null);
  const [error, setError] = useState(''), [reportLoading, setReportLoading] = useState(true), [summaryLoading, setSummaryLoading] = useState(true);
  useEffect(() => { setReportLoading(true); setError(''); api.paymentReport(from, to).then(setData).catch(e => setError(e.message)).finally(() => setReportLoading(false)); }, [from, to]);
  useEffect(() => {
    Promise.all([api.dailyCollections(today), api.monthlyCollections(currentMonth), api.attendanceToday()])
      .then(([dailyValue, monthlyValue, attendanceValue]) => { setDaily(dailyValue); setMonthly(monthlyValue); setAttendance(attendanceValue); })
      .catch(e => setError(e.message)).finally(() => setSummaryLoading(false));
  }, []);
  return <>
    <PageHeader eyebrow="CLUB PERFORMANCE" title="Reports" description="Collections include only successful payments. Pending and declined requests are shown separately." />
    <ErrorMessage>{error}</ErrorMessage>
    <section className="stats">
      <article className="stat"><div className="stat-icon green" aria-hidden="true">₹</div><span>ALL-TIME COLLECTIONS</span><strong>{reportLoading ? '—' : formatCurrency(data?.totalSuccessfulCollections)}</strong><small>Successful transactions only</small></article>
      <article className="stat"><div className="stat-icon blue" aria-hidden="true">₹</div><span>TODAY’S COLLECTIONS</span><strong>{summaryLoading ? '—' : formatCurrency(daily)}</strong><small>{today}</small></article>
      <article className="stat"><div className="stat-icon purple" aria-hidden="true">₹</div><span>THIS MONTH</span><strong>{summaryLoading ? '—' : formatCurrency(monthly)}</strong><small>{currentMonth}</small></article>
      <article className="stat"><div className="stat-icon amber" aria-hidden="true">↗</div><span>TODAY’S ATTENDANCE</span><strong>{summaryLoading ? '—' : attendance}</strong><small>Visits recorded</small></article>
    </section>
    <div className="inline-filters"><label>From <input aria-label="Report start date" type="date" value={from} onChange={event => setFrom(event.target.value)} /></label><label>To <input aria-label="Report end date" type="date" value={to} onChange={event => setTo(event.target.value)} /></label></div>
    <section className="stat-grid-three">
      <article className="stat"><span>PENDING PAYMENTS</span><strong>{reportLoading ? '—' : data?.pendingPayments ?? 0}</strong></article>
      <article className="stat"><span>FAILED PAYMENTS</span><strong>{reportLoading ? '—' : data?.failedPayments ?? 0}</strong></article>
      <article className="stat"><span>COLLECTIONS IN RANGE</span><strong>{reportLoading ? '—' : formatCurrency(data?.collectionsInRange)}</strong></article>
    </section>
    <section className="content-card"><span className="eyebrow">TRANSACTION ACTIVITY</span><h3>Recent transactions</h3>
      {reportLoading ? <Loading /> : data?.recentTransactions?.length ? <div className="table-wrap"><table><thead><tr><th>REFERENCE</th><th>MEMBER</th><th>AMOUNT</th><th>METHOD</th><th>STATUS</th><th>DATE</th></tr></thead><tbody>{data.recentTransactions.map(payment => <tr key={payment.id}><td>{payment.transactionReference}</td><td>{payment.member.fullName}</td><td>{formatCurrency(payment.amount)}</td><td>{payment.method}</td><td><Status value={payment.status} /></td><td>{new Date(payment.requestedAt).toLocaleDateString()}</td></tr>)}</tbody></table></div> : <Empty>No transaction data in this report.</Empty>}
    </section>
  </>;
}
