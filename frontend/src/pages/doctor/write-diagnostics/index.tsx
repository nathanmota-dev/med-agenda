import { useEffect, useMemo, useState } from "react";
import { Label } from "../../../components/ui/label";
import {
    Select, SelectTrigger, SelectValue, SelectContent, SelectItem,
} from "../../../components/ui/select";
import { Separator } from "../../../components/ui/separator";
import { Textarea } from "../../../components/ui/textarea";
import { Input } from "../../../components/ui/input";
import { Button } from "../../../components/ui/button";
import api from "../../../api/api";
import { getCids, CidDTO } from "../../../api/cid";

interface FullConsulta {
    consultationId: string;
    dateTime: string;
    doctor: { crm: string };
    patient: { name: string; cpf: string };
}

export default function DoctorWriteDiagnostics() {
    const [consultas, setConsultas] = useState<FullConsulta[]>([]);
    const [consultaSelecionada, setConsultaSelecionada] = useState<string>("");
    const [descricao, setDescricao] = useState("");
    const [data, setData] = useState("");
    const [cid, setCid] = useState("");
    const [mensagem, setMensagem] = useState("");
    const [loadingCids, setLoadingCids] = useState(true);
    const [cids, setCids] = useState<string[]>([]);
    const [erroCid, setErroCid] = useState<string>("");

    useEffect(() => {
        const crm = typeof window !== "undefined" ? localStorage.getItem("crm") : null;
        if (!crm) return;

        api.get<FullConsulta[]>("/consultations/all")
            .then((res) => {
                const minhas = res.data.filter((c) => c.doctor.crm === crm);
                setConsultas(minhas);
            })
            .catch((err) => console.error("Erro ao carregar consultas:", err));
    }, []);

    useEffect(() => {
        let mounted = true;
        setLoadingCids(true);
        getCids()
            .then((list: CidDTO[]) => { if (!mounted) return; setCids(list.map((c) => c.code)); setErroCid(""); })
            .catch(() => { if (!mounted) return; setErroCid("Falha ao carregar CIDs. Tente novamente mais tarde."); setCids([]); })
            .finally(() => mounted && setLoadingCids(false));
        return () => { mounted = false; };
    }, []);

    const podeEnviar = useMemo(
        () => !!consultaSelecionada && !!descricao.trim() && !!data && !!cid,
        [consultaSelecionada, descricao, data, cid]
    );

    const handleSubmit = async () => {
        setMensagem("");
        if (!podeEnviar) { setMensagem("Preencha todos os campos."); return; }

        try {
            await api.post("/diagnosis", {
                descricao,
                data, // yyyy-MM-dd
                cid,
                consulta: { consultationId: consultaSelecionada },
            });
            setMensagem("Diagnóstico registrado com sucesso.");
            setDescricao(""); setData(""); setCid(""); setConsultaSelecionada("");
        } catch (error) {
            console.error(error);
            setMensagem("Erro ao registrar diagnóstico.");
        }
    };

    return (
        <div className="p-6 space-y-6">
            <h1 className="text-2xl font-bold">Registrar Diagnóstico</h1>

            <div>
                <Label>Consulta:</Label>
                <Select value={consultaSelecionada} onValueChange={setConsultaSelecionada}>
                    <SelectTrigger className="w-[300px]">
                        <SelectValue placeholder="Selecione uma consulta" />
                    </SelectTrigger>
                    <SelectContent>
                        {consultas.map((c) => {
                            const dt = new Date(c.dateTime);
                            const dateStr = dt.toLocaleDateString("pt-BR", { day: "2-digit", month: "2-digit", year: "numeric" });
                            const timeStr = dt.toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" });
                            return (
                                <SelectItem key={c.consultationId} value={c.consultationId}>
                                    {c.patient.name} — {dateStr} {timeStr}
                                </SelectItem>
                            );
                        })}
                    </SelectContent>
                </Select>
            </div>

            <div>
                <Label>Descrição:</Label>
                <Textarea placeholder="Descreva o diagnóstico..." value={descricao} onChange={(e) => setDescricao(e.target.value)} />
            </div>

            <div>
                <Label>Data do Diagnóstico:</Label>
                <Input type="date" value={data} onChange={(e) => setData(e.target.value)} />
            </div>

            <div>
                <Label>CID:</Label>
                <Select value={cid} onValueChange={setCid} disabled={loadingCids || !!erroCid}>
                    <SelectTrigger className="w-[300px]">
                        <SelectValue placeholder={loadingCids ? "Carregando CIDs..." : erroCid ? "Erro ao carregar CIDs" : "Selecione um CID"} />
                    </SelectTrigger>
                    <SelectContent className="max-h-[320px]">
                        {cids.map((code) => (
                            <SelectItem key={code} value={code}>{code}</SelectItem>
                        ))}
                    </SelectContent>
                </Select>
                {erroCid && <p className="text-sm text-red-600 mt-1">{erroCid}</p>}
            </div>

            <Button onClick={handleSubmit} disabled={!podeEnviar}>Registrar Diagnóstico</Button>
            {mensagem && <p className="text-sm text-muted-foreground">{mensagem}</p>}
            <Separator />
        </div>
    );
}
