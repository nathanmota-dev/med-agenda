import AdminForm from '../../components/register-form/AdminForm';
import PatientForm from '../../components/register-form/PatientForm';
import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

export default function Register() {
    const location = useLocation();
    const [userType, setUserType] = useState<'admin' | 'patient'>('admin');

    useEffect(() => {
        const path = location.pathname.split('/')[1];
        console.log('Caminho atual:', location.pathname);
        console.log('UserType detectado:', path);
        if (path === 'patient' || path === 'admin') {
            setUserType(path);
        }
    }, [location]);

    return (
        <div className="min-h-screen bg-gray-100 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
            <div className="sm:mx-auto sm:w-full sm:max-w-md">
                <div className="flex justify-center items-center">
                    <img className="w-32 h-32" src="/logo.png" alt="Logo" />
                </div>
                <h1 className="text-center text-3xl font-extrabold text-blue-600">MedAgenda</h1>
                <h2 className="mt-6 text-center text-2xl font-bold text-gray-900">
                    Registro de <span className="text-blue-600">{userType === 'admin' ? 'Administrador' : 'Paciente'}</span>
                </h2>
            </div>

            <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
                {userType === 'admin' ? <AdminForm /> : <PatientForm />}
            </div>
        </div>
    );
}
