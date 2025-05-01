import { useState, useEffect } from 'react';
import { Calendar } from 'lucide-react';
import api from '../../../api/api';

export default function PatientDashboard() {
    const [totalConsultations, setTotalConsultations] = useState<number>(0);    
    const patientCpf = localStorage.getItem('cpf') || '';

    useEffect(() => {
        const fetchPatientConsultations = async () => {
            if (!patientCpf) return;
            try {
                const response = await api.get(`/consultations/patient-history/${patientCpf}`);
                setTotalConsultations(response.data.length);
            } catch (error) {
                console.error('Erro ao carregar consultas do paciente:', error);
            }
        };

        fetchPatientConsultations();
    }, [patientCpf]);

    return (
        <div className="flex h-screen bg-gray-100">
            <div className="flex-1 flex flex-col overflow-hidden">
                <main className="flex-1 overflow-x-hidden overflow-y-auto bg-gray-100 p-6">
                    <div className="max-w-7xl mx-auto">
                        {/* Estatísticas */}
                        <div className="grid grid-cols-1 gap-5 sm:grid-cols-1 lg:grid-cols-1">
                            <div className="bg-white overflow-hidden shadow rounded-lg">
                                <div className="p-5">
                                    <div className="flex items-center">
                                        <div className="flex-shrink-0">
                                            <Calendar className="h-6 w-6 text-gray-400" aria-hidden="true" />
                                        </div>
                                        <div className="ml-5 w-0 flex-1">
                                            <dl>
                                                <dt className="text-sm font-medium text-gray-500 truncate">
                                                    Consultas Agendadas
                                                </dt>
                                                <dd className="text-3xl font-semibold text-gray-900">
                                                    {totalConsultations}
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
