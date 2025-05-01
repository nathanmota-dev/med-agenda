import { useEffect, useState } from "react";
import { Label } from "../../../components/ui/label";
import {
  Select,
  SelectTrigger,
  SelectValue,
  SelectContent,
  SelectItem,
} from "../../../components/ui/select";
import { Separator } from "../../../components/ui/separator";
import { Textarea } from "../../../components/ui/textarea";
import { Input } from "../../../components/ui/input";
import { Button } from "../../../components/ui/button";
import api from "../../../api/api";

interface FullConsulta {
  consultationId: string;
  date: string;
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

  useEffect(() => {
    const crm = localStorage.getItem("crm");
    if (!crm) return;

    api.get<FullConsulta[]>("/consultations/all").then((res) => {
      const minhas = res.data.filter((c) => c.doctor.crm === crm);
      setConsultas(minhas);
    });
  }, []);

  const handleSubmit = async () => {
    if (
      !consultaSelecionada ||
      !descricao.trim() ||
      !data ||
      !cid.trim()
    ) {
      setMensagem("Preencha todos os campos.");
      return;
    }

    try {
      await api.post("/diagnosis", {
        descricao,
        data,
        cid,
        consulta: {
          consultationId: consultaSelecionada,
        },
      });
      setMensagem("Diagnóstico registrado com sucesso.");
      setDescricao("");
      setData("");
      setCid("");
      setConsultaSelecionada("");
    } catch (error) {
      setMensagem("Erro ao registrar diagnóstico.");
    }
  };

  return (
    <div className="p-6 space-y-6">
      <h1 className="text-2xl font-bold">Registrar Diagnóstico</h1>

      <div>
        <Label>Consulta:</Label>
        <Select
          value={consultaSelecionada}
          onValueChange={setConsultaSelecionada}
        >
          <SelectTrigger className="w-[300px]">
            <SelectValue placeholder="Selecione uma consulta" />
          </SelectTrigger>
          <SelectContent>
            {consultas.map((c) => {
              const dateStr = new Date(c.date + "T00:00:00")
                .toLocaleDateString();
              return (
                <SelectItem
                  key={c.consultationId}
                  value={c.consultationId}
                >
                  {c.patient.name} — {dateStr}
                </SelectItem>
              );
            })}
          </SelectContent>
        </Select>
      </div>

      <div>
        <Label>Descrição:</Label>
        <Textarea
          placeholder="Descreva o diagnóstico..."
          value={descricao}
          onChange={(e) => setDescricao(e.target.value)}
        />
      </div>

      <div>
        <Label>Data do Diagnóstico:</Label>
        <Input
          type="date"
          value={data}
          onChange={(e) => setData(e.target.value)}
        />
      </div>

      <div>
        <Label>CID:</Label>
        <Input
          placeholder="Ex: N39.0"
          value={cid}
          onChange={(e) => setCid(e.target.value)}
        />
      </div>

      <Button onClick={handleSubmit}>Registrar Diagnóstico</Button>

      {mensagem && (
        <p className="text-sm text-muted-foreground">{mensagem}</p>
      )}

      <Separator />
    </div>
  );
}
