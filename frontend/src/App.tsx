import { Routes, Route } from 'react-router-dom';
import LandingPage from './pages/landing-page';
import Login from './pages/login';
import Register from './pages/register';
import AdminDashboard from './pages/admin/dashboard.tsx/index.tsx';
import AdminDoctors from './pages/admin/doctors/index.tsx';
import AdminConsultations from './pages/admin/consultations/index.tsx';
import DoctorDashboard from './pages/doctor/dashboard/index.tsx';
import PatientDashboard from './pages/patient/dashboard/index.tsx';
import Admin from './pages/admin/admin/index.tsx';
import Layout from './components/Layout.tsx';

export default function App() {
  return (
    <Routes>
      {/* Landing Page */}
      <Route path="/" element={<LandingPage />} />

      {/* Login */}
      <Route path="/:userType/login" element={<Login />} />

      {/* Register */}
      <Route path="/:userType/register" element={<Register />} />

      {/* Admin */}
      <Route element={<Layout />}>
        <Route path="/admin/dash" element={<AdminDashboard />} />
        <Route path="/admin/dash/admin" element={<Admin />} />
        <Route path="/admin/dash/doctors" element={<AdminDoctors />} />
        <Route path="/admin/dash/patients" element={<AdminConsultations />} />
        <Route path="/admin/dash/consultations" element={<AdminConsultations />} />
      </Route>

      {/* Doctor */}
      <Route path="/doctor/dash" element={<DoctorDashboard />} />

      {/* Patient */}
      <Route path="/patient/dash" element={<PatientDashboard />} />
    </Routes>
  )
}