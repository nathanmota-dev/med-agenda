import { Routes, Route } from 'react-router-dom';
import LandingPage from './pages/landing-page';
import Login from './pages/login';
import Register from './pages/register';
import AdminDashboard from './pages/dashboard.tsx';
import AdminDoctors from './pages/doctors/index.tsx';
import AdminConsultations from './pages/consultations/index.tsx';
import DoctorDashboard from './pages/doctor/dashboard/index.tsx';
import PatientDashboard from './pages/patient/dashboard/index.tsx';

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
      <Route path="/admin/dash" element={<AdminDashboard />} />
      <Route path="/admin/doctors" element={<AdminDoctors />} />
      <Route path="/admin/patients" element={<AdminConsultations />} />
      <Route path="/admin/consultations" element={<AdminConsultations />} />

      {/* Doctor */}
      <Route path="/doctor/dash" element={<DoctorDashboard />} />

      {/* Patient */}
      <Route path="/patient/dash" element={<PatientDashboard />} />
    </Routes>
  )
}