import { Route, Routes } from "react-router-dom";
import Login from "./pages/login";
import Dashboard from "./pages/dashboard.tsx";
import Register from "./pages/register/index.tsx";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/dash" element={<Dashboard />} />
    </Routes>
  )
}

