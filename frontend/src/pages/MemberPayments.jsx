import { Link } from 'react-router-dom';
import PageHeader from '../components/PageHeader';
import { Empty, ErrorMessage, Loading, Status } from '../components/Feedback';
import { useResource } from '../hooks/useResource';
import { api } from '../services/api';
import { formatCurrency } from '../services/currency';
export default function MemberPayments(){const {data,error,loading}=useResource(()=>api.memberPayments(),[]);return <><PageHeader eyebrow="YOUR TRANSACTION RECORDS" title="My Payments" description="Successful payments include a printable receipt. GymFlow uses a demonstration mock gateway; no real money is processed."/><ErrorMessage>{error}</ErrorMessage>{loading?<Loading/>:data?.length?<div className="table-wrap"><table><thead><tr><th>DATE</th><th>TRANSACTION</th><th>PURPOSE</th><th>AMOUNT</th><th>METHOD</th><th>STATUS</th><th></th></tr></thead><tbody>{data.map(payment=><tr key={payment.id}><td>{new Date(payment.completedAt||payment.requestedAt).toLocaleString()}</td><td>{payment.transactionReference}</td><td>{payment.purpose}</td><td>{formatCurrency(payment.amount)}</td><td>{payment.method}</td><td><Status value={payment.status}/></td><td>{payment.status==='SUCCESS'&&<Link to={`/member/payments/${payment.id}/receipt`}>Receipt</Link>}</td></tr>)}</tbody></table></div>:<Empty>No payment history is available.</Empty>}</>}
