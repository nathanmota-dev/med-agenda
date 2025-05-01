import { useState } from 'react';
import api from '../../../api/api';

export default function AdminPatients() {
    const [name, setName] = useState('');
    const [cpf, setCpf] = useState('');
    const [dateOfBirth, setDateOfBirth] = useState('');
    const [address, setAddress] = useState('');
    const [medicalHistory, setMedicalHistory] = useState('');
    const [patientCpf, setPatientCpf] = useState('');
    const [patientData, setPatientData] = useState<any>(null);
    const [message, setMessage] = useState('');

    // Função para cadastrar um novo paciente
    const handleCreatePatient = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setMessage('');

        try {
            await api.post('/patients/create', {
                name,
                cpf,
                dateOfBirth,
                address,
                medicalHistory
            });
            setMessage('Paciente cadastrado com sucesso!');
            setName('');
            setCpf('');
            setDateOfBirth('');
            setAddress('');
            setMedicalHistory('');
        } catch (error) {
            setMessage('Erro ao cadastrar paciente. Tente novamente.');
            console.error(error);
        }
    };

    // Função para buscar um paciente pelo CPF
    const handleFetchPatientByCpf = async () => {
        setMessage('');
        setPatientData(null);

        try {
            const response = await api.get(`/patients/${patientCpf}`);
            setPatientData(response.data);
        } catch (error) {
            setMessage('Erro ao buscar paciente. Verifique o CPF e tente novamente.');
            console.error(error);
        }
    };

    return (
        <div className="p-6 bg-white rounded-lg shadow-md">
            <h2 className="text-2xl font-bold mb-4">Gerenciamento de Pacientes</h2>
            
            {/* Cadastro de Paciente */}
            <div className="mb-8">
                <h3 className="text-xl font-semibold mb-4">Cadastrar Novo Paciente</h3>
                <form onSubmit={handleCreatePatient} className="space-y-4">
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
                        <label className="block text-gray-700">CPF</label>
                        <input
                            type="text"
                            value={cpf}
                            onChange={(e) => setCpf(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                            required
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700">Data de Nascimento</label>
                        <input
                            type="date"
                            value={dateOfBirth}
                            onChange={(e) => setDateOfBirth(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                            required
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700">Endereço</label>
                        <input
                            type="text"
                            value={address}
                            onChange={(e) => setAddress(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700">Histórico Médico</label>
                        <textarea
                            value={medicalHistory}
                            onChange={(e) => setMedicalHistory(e.target.value)}
                            className="mt-1 p-2 border border-gray-300 rounded w-full"
                        ></textarea>
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-blue-600 text-white p-2 rounded mt-4 hover:bg-blue-700"
                    >
                        Cadastrar Paciente
                    </button>
                </form>
                {message && <p className="mt-4 text-center text-red-600">{message}</p>}
            </div>

            {/* Buscar Paciente pelo CPF */}
            <div className="mb-8">
                <h3 className="text-xl font-semibold mb-4">Buscar Paciente por CPF</h3>
                <div className="flex space-x-4">
                    <input
                        type="text"
                        placeholder="Digite o CPF do paciente"
                        value={patientCpf}
                        onChange={(e) => setPatientCpf(e.target.value)}
                        className="p-2 border border-gray-300 rounded w-full"
                    />
                    <button
                        onClick={handleFetchPatientByCpf}
                        className="bg-green-600 text-white p-2 rounded hover:bg-green-700"
                    >
                        Buscar
                    </button>
                </div>
                {patientData && (
                    <div className="mt-4 p-4 border border-gray-300 rounded bg-gray-50">
                        <h4 className="font-semibold">Dados do Paciente:</h4>
                        <p><strong>CPF:</strong> {patientData.cpf}</p>
                        <p><strong>Nome:</strong> {patientData.name}</p>
                        <p><strong>Data de Nascimento:</strong> {patientData.dateOfBirth}</p>
                        <p><strong>Endereço:</strong> {patientData.address}</p>
                        <p><strong>Histórico Médico:</strong> {patientData.medicalHistory}</p>
                    </div>
                )}
            </div>
        </div>
    );
}
