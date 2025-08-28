import api from './api';

export type CidDTO = { code: string; createdAt?: string };

export async function getCids(): Promise<CidDTO[]> {
    const { data } = await api.get<CidDTO[]>('/cid');
    return data;
}
