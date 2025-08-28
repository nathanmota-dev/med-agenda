import { useEffect, useState } from 'react';

import api from '../../../api/api'; 

type Specialty = {
    id: number;
    nome: string;
    tipo: string;
};

export default function DoctorInformation() {
    
    const [specialties, setSpecialties] = useState<Specialty[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    
    useEffect(() => {
        (async () => {
            try {
                
                const { data } = await api.get('/scrape/extrair-e-salvar');
                setSpecialties(Array.isArray(data) ? data : []);
            } catch (e) {
                setError('Erro ao carregar as especialidades médicas.');
                console.error("API Error:", e); 
            } finally {
                setLoading(false);
            }
        })();
    }, []);

    
    if (loading) {
        return (
            <div className="pt-10 px-6 max-w-6xl mx-auto">
                <h2 className="text-2xl font-semibold mb-6">Carregando Especialidades...</h2>
                <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
                    {Array.from({ length: 6 }).map((_, i) => (
                        <div
                            key={i}
                            className="rounded-xl border border-gray-200 dark:border-gray-800 p-4 bg-white dark:bg-neutral-900 shadow-sm animate-pulse"
                        >
                            <div className="h-5 w-2/3 bg-gray-200 dark:bg-neutral-800 rounded mb-3" />
                            <div className="h-3 w-full bg-gray-200 dark:bg-neutral-800 rounded mb-2" />
                        </div>
                    ))}
                </div>
            </div>
        );
    }

   
    if (error) {
        return (
            <div className="pt-10 px-6 max-w-3xl mx-auto text-center">
                <p className="text-red-600 dark:text-red-400">{error}</p>
            </div>
        );
    }

    
    return (
        <div className="pt-10 px-6 max-w-6xl mx-auto">
            <h2 className="text-2xl font-semibold mb-6">Especialidades Médicas</h2>

            {specialties.length === 0 ? (
                <p className="text-gray-600 dark:text-gray-300">Nenhuma especialidade encontrada.</p>
            ) : (
                <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
                    {specialties.map((specialty) => (
                        <article
                            
                            key={specialty.id}
                            className="rounded-xl border border-gray-200 dark:border-gray-800 p-5 bg-white dark:bg-neutral-900 shadow-sm hover:shadow-md transition-shadow"
                        >
                            <h3 className="text-lg font-semibold mb-2 text-gray-900 dark:text-gray-100">
                                {specialty.nome}
                            </h3>
                            <p className="text-sm text-gray-700 dark:text-gray-300">
                                
                                Tipo: {specialty.tipo}
                            </p>
                        </article>
                    ))}
                </div>
            )}
        </div>
    );
}