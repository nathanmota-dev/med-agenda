import { useEffect, useState } from 'react';
import api from '../../../api/api';

type Noticia = {
  id: number;
  titulo: string;
  link: string;
  data: string; // ISO string
};

type Page<T> = {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
};

export default function NewsInformation() {
  const [items, setItems] = useState<Noticia[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  function normalize(data: any): Noticia[] {
    if (Array.isArray(data)) return data;
    if (data && Array.isArray((data as Page<Noticia>).content)) return (data as Page<Noticia>).content;
    return [];
  }

  async function carregar(limit = 5) {
    setLoading(true);
    setError('');
    try {
      // baseURL = http://localhost:8080  → rota correta é /api/news
      const { data } = await api.get<Page<Noticia>>('/api/news', { params: { limit } });
      console.log('GET /api/news →', data); // debug
      setItems(normalize(data));
      if (normalize(data).length === 0) setError('Nenhuma notícia retornada. Veja o Network no DevTools.');
    } catch (e) {
      console.error(e);
      setError('Erro ao carregar as notícias.');
    } finally {
      setLoading(false);
    }
  }

  async function atualizar(limit = 5) {
    setLoading(true);
    setError('');
    try {
      // dispara scraping+save no backend (seu NewsController precisa dessa rota)
      await api.get('/api/news/refresh', { params: { limit } });
      await carregar(limit);
    } catch (e) {
      console.error(e);
      setError('Erro ao atualizar as notícias.');
      setLoading(false);
    }
  }

  useEffect(() => {
    carregar(5);
  }, []);

  function formatDate(iso?: string) {
    if (!iso) return '';
    return new Date(iso).toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' });
  }

  if (loading) {
    return (
      <div className="pt-10 px-6 max-w-6xl mx-auto">
        <h2 className="text-2xl font-semibold mb-6">Últimas notícias</h2>
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {Array.from({ length: 6 }).map((_, i) => (
            <div key={i} className="rounded-xl border border-gray-200 dark:border-gray-800 p-4 bg-white dark:bg-neutral-900 shadow-sm animate-pulse">
              <div className="h-5 w-2/3 bg-gray-200 dark:bg-neutral-800 rounded mb-3" />
              <div className="h-3 w-full bg-gray-200 dark:bg-neutral-800 rounded mb-2" />
              <div className="h-3 w-5/6 bg-gray-200 dark:bg-neutral-800 rounded" />
            </div>
          ))}
        </div>
      </div>
    );
  }

  return (
    <div className="pt-10 px-6 max-w-6xl mx-auto">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-2xl font-semibold">Últimas notícias</h2>
        <button
          onClick={() => atualizar(5)}
          className="px-4 py-2 rounded bg-blue-600 text-white hover:bg-blue-700"
        >
          Atualizar
        </button>
      </div>

      {error && <div className="mb-4 text-sm text-red-600 dark:text-red-400">{error}</div>}

      {items.length === 0 ? (
        <p className="text-gray-600 dark:text-gray-300">Nenhuma notícia encontrada.</p>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {items.map((n) => (
            <article
              key={n.id}
              className="rounded-xl border border-gray-200 dark:border-gray-800 p-5 bg-white dark:bg-neutral-900 shadow-sm hover:shadow-md transition-shadow text-left"
            >
              <h3 className="text-lg font-semibold mb-2 text-gray-900 dark:text-gray-100 line-clamp-3">
                <a href={n.link} target="_blank" rel="noreferrer" className="hover:underline" title={n.titulo}>
                  {n.titulo}
                </a>
              </h3>
              <div className="text-xs text-gray-500 dark:text-gray-400">
                Publicado: {formatDate(n.data)}
              </div>
            </article>
          ))}
        </div>
      )}
    </div>
  );
}
