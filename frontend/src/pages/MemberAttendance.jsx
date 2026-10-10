import PageHeader from '../components/PageHeader';
import { Empty, ErrorMessage, Loading } from '../components/Feedback';
import { useResource } from '../hooks/useResource';
import { api } from '../services/api';
export default function MemberAttendance(){const {data,error,loading}=useResource(()=>api.memberAttendance(),[]);return <><PageHeader eyebrow="VISIT HISTORY" title="My Attendance" description="A read-only history of your recorded gym visits."/><ErrorMessage>{error}</ErrorMessage>{loading?<Loading/>:data?.length?<div className="table-wrap"><table><thead><tr><th>DATE</th><th>CHECK-IN</th><th>CHECK-OUT</th></tr></thead><tbody>{data.map((visit,index)=><tr key={`${visit.checkInAt}-${index}`}><td>{visit.attendanceDate}</td><td>{new Date(visit.checkInAt).toLocaleTimeString()}</td><td>{visit.checkOutAt?new Date(visit.checkOutAt).toLocaleTimeString():'In progress'}</td></tr>)}</tbody></table></div>:<Empty>No attendance visits have been recorded yet.</Empty>}</>}
