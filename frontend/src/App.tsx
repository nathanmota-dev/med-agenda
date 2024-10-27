import { Route, Routes } from "react-router-dom";
import Login from "./pages/login";
import Dashboard from "./pages/dashboard.tsx";
import Register from "./pages/register/index.tsx";
import Admin from "./pages/admin/index.tsx";
import Patients from "./pages/patients/index.tsx";
import Doctors from "./pages/doctors/index.tsx";
import Consultations from "./pages/consultations/index.tsx";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/dash" element={<Dashboard />} />
      <Route path="/admin" element={<Admin />} />
      <Route path="/patients" element={<Patients />} />
      <Route path="/doctors" element={<Doctors />} />
      <Route path="/consultations" element={<Consultations />} />
    </Routes>
  )
}

