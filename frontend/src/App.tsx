import { Routes, Route } from 'react-router-dom';

import LandingPage from './pages/landing-page';

import Login from './pages/login';

import Register from './pages/register';

import Admin from './pages/admin/admin';
import AdminDashboard from './pages/admin/dashboard.tsx';
import AdminDoctors from './pages/admin/doctors';
import AdminPatients from './pages/admin/patients/index.tsx';
import AdminConsultations from './pages/admin/consultations';

import DoctorDashboard from './pages/doctor/dashboard';
import DoctorAgenda from './pages/doctor/agenda';
import DoctorDiagnostics from './pages/doctor/diagnostics';
import DoctorWriteDiagnostics from './pages/doctor/write-diagnostics';

import PatientDashboard from './pages/patient/dashboard';
import PatientSchedule from './pages/patient/schedule';
import PatientConsultation from './pages/patient/consultation';
import PatientCancel from './pages/patient/cancel';
import PatientHistory from './pages/patient/history';
import PatientPayments from './pages/patient/payments';

import Layout from './components/others/Layout.tsx';
import ChatAI from './components/ai/ChatAI.tsx';
import PdfScraping from './pages/scraping/pdf-information/index.tsx';
import PublicLayout from './components/layout/public-layout/index.tsx';
import NewsInformation from './pages/scraping/news-information/index.tsx';
import DoctorInformation from './pages/scraping/doctor-information/index.tsx';


export default function App() {
  return (
    <>
      <Routes>
        {/* Landing Page */}
        <Route element={<PublicLayout />}>
          <Route path="/" element={<LandingPage />} />
          <Route path="/information/pdf" element={<PdfScraping />} />
          <Route path="/information/news" element={<NewsInformation />} />
          <Route path="/information/doctor" element={<DoctorInformation />} />
        </Route>

        {/* Login */}
        <Route path="/:userType/login" element={<Login />} />

        {/* Register */}
        <Route path="/:userType/register" element={<Register />} />

        {/* Admin */}
        <Route path="/admin/*" element={<Layout />}>
          <Route path="dash" element={<AdminDashboard />} />
          <Route path="dash/admin" element={<Admin />} />
          <Route path="dash/doctors" element={<AdminDoctors />} />
          <Route path="dash/patients" element={<AdminPatients />} />
          <Route path="dash/consultations" element={<AdminConsultations />} />
        </Route>

        {/* Doctor */}
        <Route path="/doctor/*" element={<Layout />}>
          <Route path="dash" element={<DoctorDashboard />} />
          <Route path="agenda" element={<DoctorAgenda />} />
          <Route path="diagnostics" element={<DoctorDiagnostics />} />
          <Route path="write-diagnostics" element={<DoctorWriteDiagnostics />} />
        </Route>

        {/* Patient */}
        <Route path="/patient/*" element={<Layout />}>
          <Route path="dash" element={<PatientDashboard />} />
          <Route path="schedule" element={<PatientSchedule />} />
          <Route path="consultations" element={<PatientConsultation />} />
          <Route path="cancel" element={<PatientCancel />} />
          <Route path="history" element={<PatientHistory />} />
          <Route path="payments" element={<PatientPayments />} />
        </Route>
      </Routes>
      <ChatAI />
    </>
  )
}