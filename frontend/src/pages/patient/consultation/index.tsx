import { useState, useEffect } from 'react'
import api from '../../../api/api'

interface Consultation {
  consultationId: string
  dateTime: string
  doctor: {
    name: string
    specialty: string
    telephone: string
  }
  observation: string
  isUrgent: boolean
}

function formatDateTime(iso: string) {
  const [date, time] = iso.split('T')
  const [y, m, d] = date.split('-')
  return `${d}/${m}/${y} ${time.slice(0, 5)}`
}

export default function PatientConsultation() {
  const [consultations, setConsultations] = useState<Consultation[]>([])
  const [message, setMessage] = useState<string>('')
  const patientCpf = localStorage.getItem('cpf') || ''

  useEffect(() => {
    if (!patientCpf) {
      setMessage('CPF do paciente não encontrado. Faça login novamente.')
      return
    }

    api
      .get<Consultation[]>(`/consultations/patient-history/${patientCpf}`)
      .then(res => {
        const today = new Date().toISOString().slice(0, 10)
        const upcoming = res.data.filter(c =>
          c.dateTime.slice(0, 10) >= today
        )
        setConsultations(upcoming)
      })
      .catch(err => {
        console.error('Erro ao buscar consultas:', err)
        setMessage('Erro ao carregar suas consultas. Tente novamente mais tarde.')
      })
  }, [patientCpf])

  if (message) {
    return (
      <div className="p-6 bg-white rounded-lg shadow-md">
        <p className="text-red-600">{message}</p>
      </div>
    )
  }

  return (
    <div className="p-6 bg-white rounded-lg shadow-md space-y-6">
      <h2 className="text-2xl font-bold">Minhas Consultas</h2>

      {consultations.length === 0 && (
        <p className="text-gray-700">Você não tem consultas agendadas.</p>
      )}

      {consultations.map(c => (
        <div
          key={c.consultationId}
          className="border rounded-lg p-4 shadow hover:shadow-md transition"
        >
          <div className="flex justify-between items-center mb-2">
            <span className="text-lg font-semibold">
              {formatDateTime(c.dateTime)}
            </span>
            {c.isUrgent && (
              <span className="text-sm text-red-600 font-medium">URGENTE</span>
            )}
          </div>

          <p className="text-gray-800">
            <span className="font-medium">Médico:</span> {c.doctor.name}
          </p>
          <p className="text-gray-800">
            <span className="font-medium">Especialidade:</span>{' '}
            {c.doctor.specialty}
          </p>
          <p className="text-gray-800">
            <span className="font-medium">Telefone:</span>{' '}
            {c.doctor.telephone}
          </p>
          <p className="text-gray-800 mt-2">
            <span className="font-medium">Observações:</span>{' '}
            {c.observation || '—'}
          </p>
        </div>
      ))}
    </div>
  )
}