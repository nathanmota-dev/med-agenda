import { useState } from 'react';
import api from '../../../api/api';

interface ConsultationData {
    consultationId: string;
    dateTime: string;
    patient: { cpf: string };
    doctor: { crm: string };
    isUrgent: boolean;
    observation: string;
}

export default function AdminConsultations() {
    const [dateTime, setDateTime] = useState('');              // datetime-local
    const [patientCpf, setPatientCpf] = useState('');
    const [doctorCrm, setDoctorCrm] = useState('');
    const [isUrgent, setIsUrgent] = useState(false);
    const [observation, setObservation] = useState('');
    const [consultationId, setConsultationId] = useState('');
    const [consultationData, setConsultationData] = useState<ConsultationData | null>(null);
    const [message, setMessage] = useState('');


    const handleCreateConsultation = async (e: React.FormEvent) => {
        e.preventDefault();
        setMessage('');

        try {
            await api.post('/consultations/create', {
                patient: { cpf: patientCpf },
                doctor: { crm: doctorCrm },
                dateTime,
                urgent: isUrgent,
                observation
            });
            setMessage('Consulta agendada com sucesso!');
            setDateTime('');
            setPatientCpf('');
            setDoctorCrm('');
            setIsUrgent(false);
            setObservation('');
        } catch {
            setMessage('Erro ao agendar consulta. Tente novamente.');
        }
    };

    // Buscar consulta por ID
    const handleFetchConsultationById = async () => {
        setMessage('');
        setConsultationData(null);

        try {
            const res = await api.get<ConsultationData>(`/consultations/${consultationId}`);
            setConsultationData(res.data);
        } catch {
            setMessage('Erro ao buscar consulta. Verifique o ID e tente novamente.');
        }
    };

    return (
        <div className="p-6 bg-white rounded-lg shadow-md">
            <h2 className="text-2xl font-bold mb-4">Gerenciamento de Consultas</h2>

            {/* Agendar Consulta */}
            <div className="mb-8">
                <h3 className="text-xl font-semibold mb-4">Agendar Nova Consulta</h3>
                <form onSubmit={handleCreateConsultation} className="space-y-4">
                    <div>
                        <label className="block text-gray-700">Data e Hora</label>
                        <input
                            type="datetime-local"
                            value={dateTime}
                            onChange={e => setDateTime(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                            required
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700">CPF do Paciente</label>
                        <input
                            type="text"
                            value={patientCpf}
                            onChange={e => setPatientCpf(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                            required
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700">CRM do Médico</label>
                        <input
                            type="text"
                            value={doctorCrm}
                            onChange={e => setDoctorCrm(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                            required
                        />
                    </div>
                    <div className="flex items-center">
                        <input
                            type="checkbox"
                            checked={isUrgent}
                            onChange={e => setIsUrgent(e.target.checked)}
                            className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                        />
                        <label className="ml-2 text-gray-700">Consulta Urgente</label>
                    </div>
                    <div>
                        <label className="block text-gray-700">Observações</label>
                        <textarea
                            value={observation}
                            onChange={e => setObservation(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700"
                    >
                        Agendar Consulta
                    </button>
                </form>
                {message && <p className="mt-4 text-center text-red-600">{message}</p>}
            </div>

            {/* Buscar Consulta */}
            <div className="mb-8">
                <h3 className="text-xl font-semibold mb-4">Buscar Consulta por ID</h3>
                <div className="flex space-x-4">
                    <input
                        type="text"
                        placeholder="Digite o ID da consulta"
                        value={consultationId}
                        onChange={e => setConsultationId(e.target.value)}
                        className="p-2 border border-gray-300 rounded w-full"
                    />
                    <button
                        onClick={handleFetchConsultationById}
                        className="bg-green-600 text-white p-2 rounded hover:bg-green-700"
                    >
                        Buscar
                    </button>
                </div>
                {consultationData && (
                    <div className="mt-4 p-4 border border-gray-300 rounded bg-gray-50">
                        <h4 className="font-semibold">Dados da Consulta:</h4>
                        <p><strong>ID:</strong> {consultationData.consultationId}</p>
                        <p>
                            <strong>Data e Hora:</strong>{' '}
                            {new Date(consultationData.dateTime).toLocaleString('pt-BR')}
                        </p>
                        <p><strong>Paciente (CPF):</strong> {consultationData.patient.cpf}</p>
                        <p><strong>Médico (CRM):</strong> {consultationData.doctor.crm}</p>
                        <p>
                            <strong>Urgente:</strong>{' '}
                            {consultationData.isUrgent ? 'Sim' : 'Não'}
                        </p>
                        <p><strong>Observações:</strong> {consultationData.observation}</p>
                    </div>
                )}
            </div>
        </div>
    );
}
