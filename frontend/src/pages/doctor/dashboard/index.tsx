import { useEffect, useState } from "react";
import { Calendar } from "lucide-react";
import api from "../../../api/api";

export default function DoctorDashboard() {
  const [totalConsultas, setTotalConsultas] = useState<number>(0);

  useEffect(() => {
    const crm = localStorage.getItem("crm");
    if (!crm) return;

    const fetchConsultas = async () => {
      try {
        const res = await api.get(`/doctor/consultations/${crm}`);
        setTotalConsultas(res.data.length);
      } catch (err) {
        console.error("Erro ao carregar consultas do médico:", err);
      }
    };

    fetchConsultas();
  }, []);

  return (
    <div className="flex h-screen bg-gray-100">
      <div className="flex-1 flex flex-col overflow-hidden">
        <main className="flex-1 overflow-x-hidden overflow-y-auto bg-gray-100">
          <div className="sm:px-6 lg:px-8 py-6">
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
              <div className="bg-white overflow-hidden shadow rounded-lg">
                <div className="p-5">
                  <div className="flex items-center">
                    <div className="flex-shrink-0">
                      <Calendar className="h-6 w-6 text-gray-400" aria-hidden="true" />
                    </div>
                    <div className="ml-5 w-0 flex-1">
                      <dl>
                        <dt className="text-sm font-medium text-gray-500 truncate">
                          Consultas Realizadas
                        </dt>
                        <dd className="text-3xl font-semibold text-gray-900">
                          {totalConsultas}
                        </dd>
                      </dl>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  );
}
