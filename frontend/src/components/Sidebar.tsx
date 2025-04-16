import { Link, useNavigate } from 'react-router-dom';
import { LayoutDashboard, UserPlus, Users, Stethoscope, ClipboardList, FileOutput, Settings, LogOut } from 'lucide-react';

const sidebarItems = [
    { name: 'Dashboard', icon: LayoutDashboard, url: '/admin/dash' },
    { name: 'Cadastro', icon: UserPlus, url: '/admin/dash/admin' },
    { name: 'Pacientes', icon: Users, url: '/admin/dash/patients' },
    { name: 'Médicos', icon: Stethoscope, url: '/admin/dash/doctors' },
    { name: 'Consultas', icon: ClipboardList, url: '/admin/dash/consultations' },
    { name: 'Exportar dados', icon: FileOutput, url: '#' },
    { name: 'Configurações', icon: Settings, url: '#' },
    { name: 'Logout', icon: LogOut, url: '/admin/login' }
];

export default function Sidebar() {
    const navigate = useNavigate();
    const handleLogout = () => navigate('/admin/login');

    return (
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
                        className="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600"
                        onClick={() => item.name === 'Logout' && handleLogout()}
                    >
                        <item.icon className="h-5 w-5 mr-3" />
                        {item.name}
                    </Link>
                ))}
            </nav>
        </aside>
    );
}
