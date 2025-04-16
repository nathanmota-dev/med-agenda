import { useState } from 'react';
import api from '../../api/api';

export default function AdminConsultations() {
    const [date, setDate] = useState('');
    const [patientId, setPatientId] = useState('');
    const [doctorId, setDoctorId] = useState('');
    const [isUrgent, setIsUrgent] = useState(false);
    const [observation, setObservation] = useState('');
    const [consultationId, setConsultationId] = useState('');
    const [consultationData, setConsultationData] = useState<any>(null);
    const [message, setMessage] = useState('');

    // Função para agendar uma nova consulta
    const handleCreateConsultation = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setMessage('');

        try {
            await api.post('/consultations/create', {
                date,
                patientId,
                doctorId,
                isUrgent,
                observation
            });
            setMessage('Consulta agendada com sucesso!');
            setDate('');
            setPatientId('');
            setDoctorId('');
            setIsUrgent(false);
            setObservation('');
        } catch (error) {
            setMessage('Erro ao agendar consulta. Tente novamente.');
            console.error(error);
        }
    };

    // Função para buscar uma consulta pelo ID
    const handleFetchConsultationById = async () => {
        setMessage('');
        setConsultationData(null);

        try {
            const response = await api.get(`/consultations/${consultationId}`);
            setConsultationData(response.data);
        } catch (error) {
            setMessage('Erro ao buscar consulta. Verifique o ID e tente novamente.');
            console.error(error);
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
                        <label className="block text-gray-700">Data da Consulta</label>
                        <input
                            type="date"
                            value={date}
                            onChange={(e) => setDate(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                            required
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700">CPF do Paciente</label>
                        <input
                            type="text"
                            value={patientId}
                            onChange={(e) => setPatientId(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                            required
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700">CRM do Médico</label>
                        <input
                            type="text"
                            value={doctorId}
                            onChange={(e) => setDoctorId(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                            required
                        />
                    </div>
                    <div className="flex items-center">
                        <input
                            type="checkbox"
                            checked={isUrgent}
                            onChange={(e) => setIsUrgent(e.target.checked)}
                            className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                        />
                        <label className="ml-2 text-gray-700">Consulta Urgente</label>
                    </div>
                    <div>
                        <label className="block text-gray-700">Observações</label>
                        <textarea
                            value={observation}
                            onChange={(e) => setObservation(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                        ></textarea>
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-blue-600 text-white p-2 rounded mt-4 hover:bg-blue-700"
                    >
                        Agendar Consulta
                    </button>
                </form>
                {message && <p className="mt-4 text-center text-red-600">{message}</p>}
            </div>

            {/* Buscar Consulta pelo ID */}
            <div className="mb-8">
                <h3 className="text-xl font-semibold mb-4">Buscar Consulta por ID</h3>
                <div className="flex space-x-4">
                    <input
                        type="text"
                        placeholder="Digite o ID da consulta"
                        value={consultationId}
                        onChange={(e) => setConsultationId(e.target.value)}
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
                        <p><strong>ID:</strong> {consultationData.id}</p>
                        <p><strong>Data:</strong> {consultationData.date}</p>
                        <p><strong>Paciente (CPF):</strong> {consultationData.patientId}</p>
                        <p><strong>Médico (CRM):</strong> {consultationData.doctorId}</p>
                        <p><strong>Urgente:</strong> {consultationData.isUrgent ? 'Sim' : 'Não'}</p>
                        <p><strong>Observações:</strong> {consultationData.observation}</p>
                    </div>
                )}
            </div>
        </div>
    );
}
