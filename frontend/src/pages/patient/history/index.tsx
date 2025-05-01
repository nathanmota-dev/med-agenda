import { useState, useEffect } from 'react'
import api from '../../../api/api'

interface ConsultationHistory {
  consultationId: string
  dateTime: string
  doctor: {
    name: string
    specialty: string
    telephone: string
  }
  observation: string
}

function formatDate(iso: string) {
  const [date] = iso.split('T')
  const [year, month, day] = date.split('-')
  return `${day}/${month}/${year}`
}

export default function PatientHistory() {
  const [history, setHistory] = useState<ConsultationHistory[]>([])
  const [message, setMessage] = useState<string>('')
  const patientCpf = localStorage.getItem('cpf') || ''

  useEffect(() => {
    if (!patientCpf) {
      setMessage('CPF do paciente não encontrado. Faça login novamente.')
      return
    }

    api
      .get<ConsultationHistory[]>(`/consultations/patient-history/${patientCpf}`)
      .then(res => {
        const today = new Date()
        today.setHours(0, 0, 0, 0)

        const past = res.data.filter(c => {
          const cDate = new Date(c.dateTime)
          cDate.setHours(0, 0, 0, 0)
          return cDate < today
        })

        setHistory(past)
      })
      .catch(err => {
        console.error('Erro ao carregar histórico:', err)
        setMessage('Erro ao carregar histórico médico. Tente novamente mais tarde.')
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
      <h2 className="text-2xl font-bold">Histórico Médico</h2>

      {history.length === 0 ? (
        <p className="text-gray-700">
          Você ainda não tem histórico de consultas.
        </p>
      ) : (
        history.map(c => (
          <div
            key={c.consultationId}
            className="border rounded-lg p-4 shadow hover:shadow-md transition"
          >
            <div className="flex justify-between items-center mb-2">
              <span className="text-lg font-semibold">
                {formatDate(c.dateTime)}
              </span>
            </div>

            <p className="text-gray-800">
              <span className="font-medium">Médico:</span> {c.doctor.name}
            </p>
            <p className="text-gray-800">
              <span className="font-medium">Especialidade:</span>{' '}
              {c.doctor.specialty}
            </p>
            <p className="text-gray-800">
              <span className="font-medium">Contato:</span>{' '}
              {c.doctor.telephone}
            </p>
            <p className="text-gray-800 mt-2">
              <span className="font-medium">Observações:</span>{' '}
              {c.observation || '—'}
            </p>
          </div>
        ))
      )}
    </div>
  )
}