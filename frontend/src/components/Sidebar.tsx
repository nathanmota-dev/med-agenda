import { useLocation, Link } from 'react-router-dom';
import {
    LayoutDashboard,
    UserPlus,
    Users,
    Stethoscope,
    ClipboardList,
    FileOutput,    
    LogOut,
    BookOpen,
    Calendar,
    PencilLine,
    History,
    DollarSign
} from 'lucide-react';

export default function Sidebar() {
    const { pathname } = useLocation();
    const userType = pathname.split('/')[1] as 'admin' | 'doctor' | 'patient';

    const sidebarItems: Record<string, { name: string; icon: any; url: string }[]> = {
        admin: [
            { name: 'Dashboard', icon: LayoutDashboard, url: '/admin/dash' },
            { name: 'Cadastro', icon: UserPlus, url: '/admin/dash/admin' },
            { name: 'Pacientes', icon: Users, url: '/admin/dash/patients' },
            { name: 'Médicos', icon: Stethoscope, url: '/admin/dash/doctors' },
            { name: 'Consultas', icon: ClipboardList, url: '/admin/dash/consultations' },            
            { name: 'Logout', icon: LogOut, url: '/admin/login' },
        ],
        doctor: [
            { name: 'Dashboard', icon: LayoutDashboard, url: '/doctor/dash' },
            { name: 'Agenda', icon: Calendar, url: '/doctor/agenda' },
            { name: 'Diagnósticos', icon: History, url: '/doctor/diagnostics' },
            { name: 'Escrever Diagnóstico', icon: PencilLine, url: '/doctor/write-diagnostics' },
            { name: 'Logout', icon: LogOut, url: '/doctor/login' },
        ],
        patient: [
            { name: 'Dashboard', icon: LayoutDashboard, url: '/patient/dash' },
            { name: 'Agendar Consulta', icon: Calendar, url: '/patient/schedule' },
            { name: 'Minhas Consultas', icon: ClipboardList, url: '/patient/consultations' },
            { name: 'Cancelar Consulta', icon: FileOutput, url: '/patient/cancel' },
            { name: 'Histórico Médico', icon: BookOpen, url: '/patient/history' },
            { name: 'Pagamentos', icon: DollarSign, url: '/patient/payments' },
            { name: 'Logout', icon: LogOut, url: '/patient/login' },
        ],
    };

    const items = sidebarItems[userType] || sidebarItems.admin;

    return (
        <aside className="w-64 bg-white shadow-md">
            <div className="flex justify-center items-center p-4">
                <img className="w-32 h-32" src="/logo.png" alt="logo" />
                <h1 className="text-2xl font-bold text-blue-600">Med Agenda</h1>
            </div>
            <nav className="mt-6">
                {items.map(item => (
                    <Link
                        key={item.name}
                        to={item.url}
                        className="flex items-center px-6 py-3 text-gray-600 hover:bg-blue-50 hover:text-blue-600"
                    >
                        <item.icon className="h-5 w-5 mr-3" />
                        {item.name}
                    </Link>
                ))}
            </nav>
        </aside>
    );
}
