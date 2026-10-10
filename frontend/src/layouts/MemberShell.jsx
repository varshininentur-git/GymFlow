import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const links = [['/member','▦','Dashboard'],['/member/profile','♙','My Profile'],['/member/membership','◷','My Membership'],['/member/attendance','↗','My Attendance'],['/member/payments','₹','My Payments'],['/member/fitness','⌁','Progress & goals'],['/member/workouts','✦','Workouts'],['/member/achievements','☆','Achievements'],['/member/nutrition','◉','Nutrition & water'],['/member/notifications','▤','Notifications']];
export default function MemberShell() {
  const { user, logout } = useAuth(); const navigate = useNavigate();
  const signOut = async () => { try { await logout(); } finally { navigate('/login',{replace:true}); } };
  return <div className="member-layout"><aside className="member-sidebar"><div className="brand"><span className="brand-icon">G</span><span>gymflow<small>MEMBER SPACE</small></span></div><p className="nav-label">YOUR ACCOUNT</p><nav aria-label="Member navigation">{links.map(([to,icon,label])=><NavLink to={to} end={to==='/member'} key={to}>{icon}<span>{label}</span></NavLink>)}</nav><div className="sidebar-bottom"><div className="avatar" aria-hidden="true">{user?.username?.slice(0,2).toUpperCase()}</div><div>{user?.username}<small>Member</small></div><button className="logout-button" type="button" aria-label="Sign out" title="Sign out" onClick={signOut}>↪</button></div></aside><main className="member-main"><header><div><span className="eyebrow">GYMFLOW MEMBER SPACE</span><h1>Your fitness, at a glance</h1></div><div className="date-pill">◷ &nbsp; Member portal</div></header><Outlet/><footer>GYMFLOW <span aria-hidden="true">•</span> YOUR CLUB, IN GOOD SHAPE</footer></main></div>;
}
