import { useEffect, useState } from 'react'
import { Calendar, Users, UserRound, Stethoscope, LayoutDashboard, ClipboardList, UserPlus, FileOutput, Settings, LogOut } from 'lucide-react'
import { Link, useNavigate } from 'react-router-dom'
import api from '../../api/api'

export default function Dashboard() {
    const [activeTab, setActiveTab] = useState('Dashboard')
    const [totalConsultations, setTotalConsultations] = useState<number>(0)
    const [totalPatients, setTotalPatients] = useState<number>(0)
    const [totalDoctors, setTotalDoctors] = useState<number>(0)
    const navigate = useNavigate()

    const sidebarItems = [
        { name: 'Dashboard', icon: LayoutDashboard, url: '/dash' },
        { name: 'Cadastro', icon: UserPlus, url: '/admin' },
        { name: 'Pacientes', icon: Users, url: '/patients' },
        { name: 'Médicos', icon: Stethoscope, url: '/doctors' },
        { name: 'Consultas', icon: ClipboardList, url: '/consultations' },
        { name: 'Exportar dados', icon: FileOutput, url: '#' },
        { name: 'Configurações', icon: Settings, url: '#' },
        { name: 'Logout', icon: LogOut, url: '/login' }
    ]

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
            {/* Sidebar */}
            <aside className="w-64 bg-white shadow-md">
                <div className='flex justify-center items-center'>
                    <div className="p-4">
                        <div className='w-32 h-32'>
                            <img src="/logo.png" alt="logo" />
                        </div>
                        <h1 className="text-2xl font-bold text-blue-600">Med Agenda</h1>
                    </div>
                </div>
                <nav className="mt-6">
                    {sidebarItems.map((item) => (
                        <Link
                            key={item.name}
                            to={item.url}
                            className={`flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600 ${activeTab === item.name ? 'bg-blue-50 text-blue-600' : ''}`}
                            onClick={() => {
                                setActiveTab(item.name)
                                if (item.name === 'Logout') {
                                    navigate('/login')
                                }
                            }}
                        >
                            <item.icon className="h-5 w-5 mr-3" />
                            {item.name}
                        </Link>
                    ))}
                </nav>
            </aside>

            {/* Main content */}
            <div className="flex-1 flex flex-col overflow-hidden">
                {/* Header */}
                <header className="bg-blue-600 shadow-md">
                    <div className="max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
                        <h2 className="text-2xl font-bold text-white">Med Agenda</h2>
                        <div className="flex items-center">
                            <span className="text-white mr-4">Usuário</span>
                            <div className="h-10 w-10 rounded-full bg-white flex items-center justify-center text-blue-600 font-bold">
                                US
                            </div>
                        </div>
                    </div>
                </header>

                {/* Dashboard content */}
                <main className="flex-1 overflow-x-hidden overflow-y-auto bg-gray-100">
                    <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
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
