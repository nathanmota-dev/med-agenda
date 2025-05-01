import { useState, useEffect } from 'react';
import api from '../../../api/api';

interface Consultation {
  consultationId: string;
  date: string;
  doctor: {
    name: string;
  };
}

function formatDate(iso: string) {
  const [year, month, day] = iso.split('-');
  return `${day}/${month}/${year}`;
}

export default function PatientCancel() {
  const [consultations, setConsultations] = useState<Consultation[]>([]);
  const [selectedId, setSelectedId] = useState<string>('');
  const [message, setMessage] = useState<string>('');
  const patientCpf = localStorage.getItem('cpf') || '';

  useEffect(() => {
    if (!patientCpf) {
      setMessage('CPF do paciente não encontrado. Faça login novamente.');
      return;
    }
    api
      .get<Consultation[]>(`/consultations/patient-history/${patientCpf}`)
      .then(res => {
        // filtra apenas consultas com data >= hoje
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        const upcoming = res.data.filter(c => {
          const cDate = new Date(c.date);
          cDate.setHours(0, 0, 0, 0);
          return cDate >= today;
        });
        setConsultations(upcoming);
      })
      .catch(err => {
        console.error('Erro ao carregar consultas:', err);
        setMessage('Erro ao carregar suas consultas.');
      });
  }, [patientCpf]);

  const handleCancel = async () => {
    if (!selectedId) {
      setMessage('Selecione uma consulta para cancelar.');
      return;
    }
    try {
      await api.delete(`/consultations/${selectedId}`);
      setConsultations(prev =>
        prev.filter(c => c.consultationId !== selectedId)
      );
      setMessage('Consulta cancelada com sucesso!');
      setSelectedId('');
    } catch (err) {
      console.error('Erro ao cancelar consulta:', err);
      setMessage('Falha ao cancelar consulta. Tente novamente.');
    }
  };

  return (
    <div className="p-6 bg-white rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-4">Cancelar Consulta</h2>

      {message && (
        <p className="mb-4 text-center text-red-600">{message}</p>
      )}

      {consultations.length === 0 ? (
        <p className="text-gray-700">Você não tem consultas futuras para cancelar.</p>
      ) : (
        <div className="space-y-4">
          <label className="block text-gray-700">Escolha a consulta:</label>
          <select
            value={selectedId}
            onChange={e => setSelectedId(e.target.value)}
            className="w-full p-2 border border-gray-300 rounded"
          >
            <option value="">-- Selecione --</option>
            {consultations.map(c => (
              <option key={c.consultationId} value={c.consultationId}>
                {formatDate(c.date)} — {c.doctor.name}
              </option>
            ))}
          </select>

          <button
            onClick={handleCancel}
            className="w-full bg-red-600 text-white p-2 rounded hover:bg-red-700"
          >
            Cancelar Consulta
          </button>
        </div>
      )}
    </div>
  );
}
