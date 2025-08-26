import { useEffect, useState } from 'react';
import api from '../../../api/api';

type DefItem = {
    termo: string;
    definicao: string;
};

export default function PdfScraping() {
    const [items, setItems] = useState<DefItem[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        (async () => {
            try {
                const { data } = await api.get('/api/guia/termos');
                setItems(Array.isArray(data) ? data : []);
            } catch (e) {
                setError('Erro ao carregar os termos do guia.');
            } finally {
                setLoading(false);
            }
        })();
    }, []);

    function formatTerm(term: string): string {
        if (!term) return "";

        return term
            .split(" ")
            .map((word, index) => {
                const lower = word.toLowerCase();
                if (index === 0) {
                    return lower.charAt(0).toUpperCase() + lower.slice(1);
                }
                return lower;
            })
            .join(" ");
    }

    function formatDefinition(def: string): string {
        if (!def) return "";

        return def
            .split(/([.!?]\s*)/)
            .map((part) => {
                if (/^[.!?]\s*$/.test(part)) return part;

                const trimmed = part.trim();
                if (trimmed.length === 0) return "";

                const lower = trimmed.toLowerCase();
                return lower.charAt(0).toUpperCase() + lower.slice(1);
            })
            .join("")
            .trim();
    }



    if (loading) {
        return (
            <div className="pt-10 px-6 max-w-6xl mx-auto">
                <h2 className="text-2xl font-semibold mb-6">Termos do Guia</h2>
                <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
                    {Array.from({ length: 6 }).map((_, i) => (
                        <div
                            key={i}
                            className="rounded-xl border border-gray-200 dark:border-gray-800 p-4 bg-white dark:bg-neutral-900 shadow-sm animate-pulse"
                        >
                            <div className="h-5 w-2/3 bg-gray-200 dark:bg-neutral-800 rounded mb-3" />
                            <div className="h-3 w-full bg-gray-200 dark:bg-neutral-800 rounded mb-2" />
                            <div className="h-3 w-5/6 bg-gray-200 dark:bg-neutral-800 rounded" />
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
            <h2 className="text-2xl font-semibold mb-6">Termos do Guia</h2>

            {items.length === 0 ? (
                <p className="text-gray-600 dark:text-gray-300">Nenhum termo encontrado.</p>
            ) : (
                <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
                    {items.map((item, idx) => (
                        <article
                            key={`${item.termo}-${idx}`}
                            className="rounded-xl border border-gray-200 dark:border-gray-800 p-5 bg-white dark:bg-neutral-900 shadow-sm hover:shadow-md transition-shadow"
                        >
                            <h3 className="text-lg font-semibold mb-2 text-gray-900 dark:text-gray-100">
                                {formatTerm(item.termo)}
                            </h3>
                            <p className="text-sm leading-relaxed text-gray-700 dark:text-gray-300 whitespace-pre-wrap">
                                {formatDefinition(item.definicao)}
                            </p>
                        </article>
                    ))}
                </div>
            )}
        </div>
    );
}