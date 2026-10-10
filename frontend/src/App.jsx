import { Navigate, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import RequireAuth from './components/RequireAuth';
import Accounts from './pages/Accounts';
import Attendance from './pages/Attendance';
import AuthShell from './layouts/Shell';
import MemberShell from './layouts/MemberShell';
import Login from './pages/Login';
import ActivateMember from './pages/ActivateMember';
import Dashboard from './pages/Dashboard';
import MemberDashboard from './pages/MemberDashboard';
import MemberProfile from './pages/MemberProfile';
import MemberMembership from './pages/MemberMembership';
import MemberAttendance from './pages/MemberAttendance';
import MemberPayments from './pages/MemberPayments';
import Members from './pages/Members';
import MemberForm from './pages/MemberForm';
import Plans from './pages/Plans';
import Trainers from './pages/Trainers';
import Payments from './pages/Payments';
import Transactions from './pages/Transactions';
import Receipt from './pages/Receipt';
import Reports from './pages/Reports';
import MemberProgress from './pages/MemberProgress';
import MemberWorkouts from './pages/MemberWorkouts';
import MemberAchievements from './pages/MemberAchievements';
import MemberNutrition from './pages/MemberNutrition';
import MemberNotifications from './pages/MemberNotifications';
import Announcements from './pages/Announcements';

export default function App() {
  return <AuthProvider><Routes>
    <Route path="/login" element={<Login />} />
    <Route path="/activate" element={<ActivateMember />} />
    <Route element={<RequireAuth memberOnly />}><Route path="/member" element={<MemberShell />}>
      <Route index element={<MemberDashboard />} />
      <Route path="profile" element={<MemberProfile />} />
      <Route path="membership" element={<MemberMembership />} />
      <Route path="attendance" element={<MemberAttendance />} />
      <Route path="payments" element={<MemberPayments />} />
      <Route path="payments/:id/receipt" element={<Receipt member />} />
      <Route path="fitness" element={<MemberProgress />} />
      <Route path="workouts" element={<MemberWorkouts />} />
      <Route path="achievements" element={<MemberAchievements />} />
      <Route path="nutrition" element={<MemberNutrition />} />
      <Route path="notifications" element={<MemberNotifications />} />
    </Route></Route>
    <Route element={<RequireAuth staffOnly />}><Route element={<AuthShell />}>
      <Route index element={<Dashboard />} />
      <Route path="members" element={<Members />} />
      <Route path="members/new" element={<MemberForm />} />
      <Route path="members/:id/edit" element={<MemberForm />} />
      <Route path="plans" element={<Plans />} />
      <Route path="trainers" element={<Trainers />} />
      <Route path="attendance" element={<Attendance />} />
      <Route path="payments" element={<Payments />} />
      <Route path="transactions" element={<Transactions />} />
      <Route path="payments/:id/receipt" element={<Receipt />} />
      <Route path="reports" element={<Reports />} />
      <Route element={<RequireAuth adminOnly />}><Route path="accounts" element={<Accounts />} /></Route>
      <Route element={<RequireAuth adminOnly />}><Route path="announcements" element={<Announcements />} /></Route>
    </Route></Route>
    <Route path="*" element={<Navigate to="/" replace />} />
  </Routes></AuthProvider>;
}
