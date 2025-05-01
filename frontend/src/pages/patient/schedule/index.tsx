import { useState, useEffect } from 'react';
import api from '../../../api/api';

export default function PatientSchedule() {
  const [dateTime, setDateTime] = useState('');
  const [doctorCrm, setDoctorCrm] = useState('');
  const [doctors, setDoctors] = useState<{ crm: string; name: string }[]>([]);
  const [observation, setObservation] = useState('');
  const [isUrgent, setIsUrgent] = useState(false);
  const [message, setMessage] = useState<string>('');

  const patientCpf = localStorage.getItem('cpf') || '';
  const patientEmail = localStorage.getItem('email') || '';

  useEffect(() => {
    api.get('/doctor')
      .then(res => setDoctors(res.data))
      .catch(err => console.error('Erro ao carregar médicos:', err));
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage('');

    if (!patientCpf || !patientEmail) {
      setMessage('Faça login novamente para agendar.');
      return;
    }
    if (!doctorCrm) {
      setMessage('Selecione um médico.');
      return;
    }
    if (!dateTime) {
      setMessage('Escolha data e hora da consulta.');
      return;
    }

    try {
      await api.post('/consultations/create', {
        patient: { cpf: patientCpf, email: patientEmail },
        doctor: { crm: doctorCrm },
        dateTime,
        urgent: isUrgent,
        observation,
      });

      setMessage('Consulta agendada com sucesso!');
      setDateTime('');
      setDoctorCrm('');
      setObservation('');
      setIsUrgent(false);
    } catch (err) {
      console.error(err);
      setMessage('Erro ao agendar consulta. Verifique os dados e tente novamente.');
    }
  };

  return (
    <div className="p-6 bg-white rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-4">Agendar Nova Consulta</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Input datetime-local */}
        <div>
          <label className="block text-gray-700">Data e Hora da Consulta</label>
          <input
            type="datetime-local"
            value={dateTime}
            onChange={e => setDateTime(e.target.value)}
            className="mt-1 p-2 border border-gray-300 rounded w-full"
            required
          />
        </div>

        {/* Seleção de médico */}
        <div>
          <label className="block text-gray-700">Médico</label>
          <select
            value={doctorCrm}
            onChange={e => setDoctorCrm(e.target.value)}
            className="mt-1 p-2 border border-gray-300 rounded w-full"
            required
          >
            <option value="">Selecione um médico</option>
            {doctors.map(doc => (
              <option key={doc.crm} value={doc.crm}>
                {doc.name} ({doc.crm})
              </option>
            ))}
          </select>
        </div>

        {/* Urgente */}
        <div className="flex items-center">
          <input
            type="checkbox"
            checked={isUrgent}
            onChange={e => setIsUrgent(e.target.checked)}
            className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
          />
          <label className="ml-2 text-gray-700">Consulta Urgente</label>
        </div>

        {/* Observações */}
        <div>
          <label className="block text-gray-700">Observações</label>
          <textarea
            value={observation}
            onChange={e => setObservation(e.target.value)}
            className="mt-1 p-2 border border-gray-300 rounded w-full"
            rows={3}
          />
        </div>

        <button
          type="submit"
          className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700"
        >
          Agendar Consulta
        </button>
      </form>

      {message && (
        <p className="mt-4 text-center text-red-600">{message}</p>
      )}
    </div>
  );
}
