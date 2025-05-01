import { useEffect, useState } from "react";
import { Card, CardContent } from "../../../components/ui/card";
import { Separator } from "../../../components/ui/separator";
import api from "../../../api/api";

interface Consulta {
  consultationId: string;
  date: string;
  doctor: { crm: string };
  patient: { name: string; cpf: string };
  observation: string;
  isUrgent: boolean;
  diagnostico?: { descricao: string; data: string; cid: string };
}

export default function DoctorDiagnostics() {
  const [consultas, setConsultas] = useState<Consulta[]>([]);
  const crm = localStorage.getItem("crm");

  useEffect(() => {
    if (!crm) return;

    // 1) Pega todas as consultas do médico
    api.get<Consulta[]>("/consultations/all").then(async (res) => {
      const minhas = res.data.filter((c) => c.doctor.crm === crm);

      // 2) Para cada consulta, busca o diagnóstico
      const withDiag = await Promise.all(
        minhas.map(async (c) => {
          try {
            const resp = await api.get<{
              descricao: string;
              data: string;
              cid: string;
            }>(`/diagnosis/consultation/${c.consultationId}`);
            return { ...c, diagnostico: resp.data };
          } catch {
            // sem diagnóstico retorna c inalterado
            return c;
          }
        })
      );

      setConsultas(withDiag);
    });
  }, []);

  return (
    <div className="p-6 space-y-6">
      <h1 className="text-2xl font-bold">Meus Diagnósticos</h1>
      <Separator />

      {consultas.length === 0 ? (
        <p className="text-muted-foreground">Nenhuma consulta encontrada.</p>
      ) : (
        consultas.map((c) => (
          <Card key={c.consultationId}>
            <CardContent className="space-y-2 p-4">
              <h2 className="font-semibold">
                Consulta em{" "}
                {new Date(c.date + "T00:00:00").toLocaleDateString()} — Paciente:{" "}
                {c.patient.name}
              </h2>

              {c.diagnostico ? (
                <>
                  <p>
                    <strong>Descrição:</strong> {c.diagnostico.descricao}
                  </p>
                  <p>
                    <strong>Data do Diagnóstico:</strong>{" "}
                    {new Date(
                      c.diagnostico.data + "T00:00:00"
                    ).toLocaleDateString()}
                  </p>
                  <p>
                    <strong>CID:</strong> {c.diagnostico.cid}
                  </p>
                </>
              ) : (
                <p className="text-muted-foreground italic">
                  Sem diagnóstico registrado.
                </p>
              )}
            </CardContent>
          </Card>
        ))
      )}
    </div>
  );
}
