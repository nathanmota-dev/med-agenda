import { useEffect, useState } from 'react'
import { Calendar, Users, UserRound } from 'lucide-react'
import api from '../../../api/api'

export default function AdminDashboard() {
    const [totalConsultations, setTotalConsultations] = useState<number>(0)
    const [totalPatients, setTotalPatients] = useState<number>(0)
    const [totalDoctors, setTotalDoctors] = useState<number>(0)

    useEffect(() => {
        const fetchData = async () => {
            try {
                const consultationsResponse = await api.get('/consultations/all')
                setTotalConsultations(consultationsResponse.data.length)

                const patientsResponse = await api.get('/patients/list')
                setTotalPatients(patientsResponse.data.length)

                const doctorsResponse = await api.get('/doctor')
                setTotalDoctors(doctorsResponse.data.length)
            } catch (error) {
                console.error('Erro ao carregar contagens:', error)
            }
        }

        fetchData()
    }, [])

    const stats = [
        { name: 'Total de Consultas', value: totalConsultations, icon: Calendar },
        { name: 'Total de Pacientes', value: totalPatients, icon: Users },
        { name: 'Total de Médicos', value: totalDoctors, icon: UserRound },
    ]

    return (
        <div className="flex h-screen bg-gray-100">
            <div className="flex-1 flex flex-col overflow-hidden">
                <main className="flex-1 overflow-x-hidden overflow-y-auto bg-gray-100">
                    <div className="sm:px-6 lg:px-8">
                        {/* Stats */}
                        <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
                            {stats.map((item) => (
                                <div key={item.name} className="bg-white overflow-hidden shadow rounded-lg">
                                    <div className="p-5">
                                        <div className="flex items-center">
                                            <div className="flex-shrink-0">
                                                <item.icon className="h-6 w-6 text-gray-400" aria-hidden="true" />
                                            </div>
                                            <div className="ml-5 w-0 flex-1">
                                                <dl>
                                                    <dt className="text-sm font-medium text-gray-500 truncate">{item.name}</dt>
                                                    <dd className="text-3xl font-semibold text-gray-900">{item.value}</dd>
                                                </dl>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </main>
            </div>
        </div>
    )
}
