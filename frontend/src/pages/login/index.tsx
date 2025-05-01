import { useState } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { Eye, EyeOff, Lock, Mail } from 'lucide-react';
import api from '../../api/api';

const loginRoutes = {
    admin: {
        title: 'Administrador',
        apiRoute: '/admin/login',
        redirect: '/admin/dash',
        registerRoute: '/admin/register',
    },
    doctor: {
        title: 'Médico',
        apiRoute: '/doctor/login',
        redirect: '/doctor/dash',
    },
    patient: {
        title: 'Paciente',
        apiRoute: '/patients/login',
        redirect: '/patient/dash',
        registerRoute: '/patient/register',
    },
} as const;

export default function Login() {
    const { userType = 'admin' } = useParams<{ userType: keyof typeof loginRoutes }>();
    const config = loginRoutes[userType];

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const navigate = useNavigate();

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError(null);

        try {
            await api.post(config.apiRoute, { email, password });

            if (userType === 'doctor') {
                localStorage.setItem("email", email);

                const res = await api.get(`/doctor/search?email=${email}`);
                const crm = res.data[0]?.crm;
                if (crm) localStorage.setItem("crm", crm);
            }

            if(userType === 'patient'){
                const res = await api.get('patients/list');
                const patient = res.data.find((p: any) => p.email === email);
                if(patient.cpf){
                    localStorage.setItem('cpf', patient.cpf);
                } else{
                    console.warn("Paciente não encontrado!");
                }
            }

            navigate(config.redirect);
        } catch (err) {
            setError('Credenciais inválidas. Verifique seu e-mail e senha.');
        }
    };

    return (
        <div className="min-h-screen bg-gray-100 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
            <div className="sm:mx-auto sm:w-full sm:max-w-md">
                <div className="flex justify-center items-center">
                    <img className="w-32 h-32" src="/logo.png" alt="Logo" />
                </div>
                <h1 className="text-center text-3xl font-extrabold text-blue-600">MedAgenda</h1>
                <h2 className="mt-6 text-center text-2xl font-bold text-gray-900">
                    Você está acessando como <span className="text-blue-600">{config.title}</span>
                </h2>
            </div>

            <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
                <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
                    <form className="space-y-6" onSubmit={handleSubmit}>
                        <div>
                            <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                                Email
                            </label>
                            <div className="mt-1 relative rounded-md shadow-sm">
                                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <Mail className="h-5 w-5 text-gray-400" aria-hidden="true" />
                                </div>
                                <input
                                    id="email"
                                    name="email"
                                    type="email"
                                    autoComplete="email"
                                    required
                                    className="focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 sm:text-sm border-gray-300 rounded-md"
                                    placeholder="you@example.com"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                />
                            </div>
                        </div>

                        <div>
                            <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                                Senha
                            </label>
                            <div className="mt-1 relative rounded-md shadow-sm">
                                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <Lock className="h-5 w-5 text-gray-400" aria-hidden="true" />
                                </div>
                                <input
                                    id="password"
                                    name="password"
                                    type={showPassword ? 'text' : 'password'}
                                    autoComplete="current-password"
                                    required
                                    className="focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 pr-10 sm:text-sm border-gray-300 rounded-md"
                                    placeholder="Sua senha"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                />
                                <div className="absolute inset-y-0 right-0 pr-3 flex items-center">
                                    <button
                                        type="button"
                                        onClick={togglePasswordVisibility}
                                        className="text-gray-400 hover:text-gray-500 focus:outline-none focus:text-gray-500"
                                    >
                                        {showPassword ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                                    </button>
                                </div>
                            </div>
                        </div>

                        {error && <div className="text-red-500 text-sm">{error}</div>}

                        <div className="flex items-center justify-between">
                            <div className="flex items-center">
                                <input
                                    id="remember-me"
                                    name="remember-me"
                                    type="checkbox"
                                    className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                                />
                                <label htmlFor="remember-me" className="ml-2 block text-sm text-gray-900">
                                    Lembrar-me
                                </label>
                            </div>

                            {'registerRoute' in config && (
                                <div id="register" className="text-sm">
                                    <Link to={config.registerRoute} className="font-medium text-blue-600 hover:text-blue-500">
                                        Ainda não tem conta? Registrar
                                    </Link>
                                </div>
                            )}
                        </div>

                        <div>
                            <button
                                type="submit"
                                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                            >
                                Login
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}
