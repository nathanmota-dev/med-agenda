import { useEffect, useState } from "react";
import { Card, CardContent } from "../../../components/ui/card";
import { Separator } from "../../../components/ui/separator";
import api from "../../../api/api";

interface AgendaAgrupada {
  data: string;
  horarios: string[];
}

export default function DoctorAgenda() {
  const [agenda, setAgenda] = useState<AgendaAgrupada[]>([]);

  useEffect(() => {
    const crm = localStorage.getItem("crm");
    if (!crm) return;

    api.get<string[]>(`/doctor/consultations/${crm}`)
      .then(res => {
        const agrupado: Record<string, string[]> = {};

        res.data.forEach(iso => {         
          const fullDate = new Date(iso);
          if (isNaN(fullDate.getTime())) return;
          
          const data = fullDate.toLocaleDateString("pt-BR");          
          const hora = fullDate.toLocaleTimeString("pt-BR", {
            hour: "2-digit",
            minute: "2-digit",
          });

          if (!agrupado[data]) agrupado[data] = [];
          agrupado[data].push(hora);
        });

        const resultado: AgendaAgrupada[] = Object.entries(agrupado).map(
          ([data, horarios]) => ({
            data,
            horarios: horarios.sort(),
          })
        );

        setAgenda(resultado);
      })
      .catch(err => {
        console.error("Erro ao carregar agenda:", err);
      });
  }, []);

  return (
    <div className="p-6 space-y-6">
      <h1 className="text-2xl font-bold">Minha Agenda</h1>
      <Separator />

      {agenda.length === 0 ? (
        <p className="text-muted-foreground">
          Nenhuma consulta agendada.
        </p>
      ) : (
        agenda.map(dia => (
          <Card key={dia.data}>
            <CardContent className="p-4 space-y-2">
              <h2 className="font-semibold">{dia.data}</h2>
              <ul className="list-disc list-inside">
                {dia.horarios.map((hora, i) => (
                  <li key={i}>{hora}</li>
                ))}
              </ul>
            </CardContent>
          </Card>
        ))
      )}
    </div>
  );
}