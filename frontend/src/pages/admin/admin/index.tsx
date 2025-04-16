import { useState } from 'react';
import api from '../../../api/api';

export default function Admin() {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [adminId, setAdminId] = useState('');
    const [adminData, setAdminData] = useState<any>(null);
    const [message, setMessage] = useState('');

    // Função para cadastrar um novo administrador
    const handleCreateAdmin = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setMessage('');

        try {
            await api.post('/admin/create', { name, email, password });
            setMessage('Administrador cadastrado com sucesso!');
            setName('');
            setEmail('');
            setPassword('');
        } catch (error) {
            setMessage('Erro ao cadastrar administrador. Tente novamente.');
            console.error(error);
        }
    };

    // Função para buscar um administrador pelo ID
    const handleFetchAdminById = async () => {
        setMessage('');
        setAdminData(null);

        try {
            const response = await api.get(`/admin/${adminId}`);
            setAdminData(response.data);
        } catch (error) {
            setMessage('Erro ao buscar administrador. Verifique o ID e tente novamente.');
            console.error(error);
        }
    };

    return (
        <div className="p-6 bg-white rounded-lg shadow-md">
            <h2 className="text-2xl font-bold mb-4">Administração de Administradores</h2>

            {/* Cadastro de Administrador */}
            <div className="mb-8">
                <h3 className="text-xl font-semibold mb-4">Cadastrar Novo Administrador</h3>
                <form onSubmit={handleCreateAdmin} className="space-y-4">
                    <div>
                        <label className="block text-gray-700">Nome</label>
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                            required
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700">Email</label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                            required
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700">Senha</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                            required
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-blue-600 text-white p-2 rounded mt-4 hover:bg-blue-700"
                    >
                        Cadastrar Administrador
                    </button>
                </form>
                {message && <p className="mt-4 text-center text-red-600">{message}</p>}
            </div>

            {/* Buscar Administrador pelo ID */}
            <div className="mb-8">
                <h3 className="text-xl font-semibold mb-4">Buscar Administrador por ID</h3>
                <div className="flex space-x-4">
                    <input
                        type="text"
                        placeholder="Digite o ID do administrador"
                        value={adminId}
                        onChange={(e) => setAdminId(e.target.value)}
                        className="p-2 border border-gray-300 rounded w-full"
                    />
                    <button
                        onClick={handleFetchAdminById}
                        className="bg-green-600 text-white p-2 rounded hover:bg-green-700"
                    >
                        Buscar
                    </button>
                </div>
                {adminData && (
                    <div className="mt-4 p-4 border border-gray-300 rounded bg-gray-50">
                        <h4 className="font-semibold">Dados do Administrador:</h4>
                        <p><strong>ID:</strong> {adminData.id}</p>
                        <p><strong>Nome:</strong> {adminData.name}</p>
                        <p><strong>Email:</strong> {adminData.email}</p>
                    </div>
                )}
            </div>
        </div>
    );
}
