<div align="center">
    <img src="https://github.com/nathanmota-dev/final-project-poo2/blob/main/frontend/public/logo2.png" alt="Logo" />
</div>

# Med Agenda

Med Agenda é um sistema para gerenciamento de consultório médico, contemplando pacientes, médicos e consultas.  
O back-end foi implementado em Spring Boot com PostgreSQL, seguindo 5 padrões de projeto e 3 princípios de design.  
O front-end foi desenvolvido em React.

A documentação completa está disponível na [Wiki do projeto](https://github.com/nathanmota-dev/final-project-poo2/wiki).

## Tecnologias

- **Back-end**: Java + Spring Boot  
- **Banco de Dados**: PostgreSQL + Neon.tech
- **Front-end**: React + Vite

## Rotas da API

| Rota                                     | Função                    | Descrição                                                                 |
|------------------------------------------|---------------------------|---------------------------------------------------------------------------|
| **HelloWorld**                           |                           |                                                                           |
| `/`                                      | Hello World               | Exibe a mensagem “Hello World”.                                           |
| **Admin**                                |                           |                                                                           |
| `POST /admin/login`                      | Login do Admin            | Autentica o administrador por e-mail e senha.                             |
| `POST /admin/create`                     | Criar Admin               | Cadastra um novo administrador.                                           |
| `GET /admin/{id}`                        | Exibir Admin              | Retorna informações do administrador pelo ID.                             |
| `DELETE /admin/{id}`                     | Deletar Admin             | Remove o administrador pelo ID.                                           |
| **Patient**                              |                           |                                                                           |
| `POST /patients/create`                  | Criar Paciente            | Cadastra um novo paciente.                                                |
| `POST /patients/login`                   | Login do Paciente         | Autentica o paciente por CPF e senha.                                     |
| `GET /patients/{cpf}`                    | Exibir Paciente           | Retorna informações do paciente pelo CPF.                                 |
| `GET /patients/list`                     | Listar Pacientes          | Retorna todos os pacientes.                                               |
| `PUT /patients/update/{cpf}`             | Atualizar Paciente        | Atualiza dados do paciente pelo CPF.                                      |
| `DELETE /patients/delete/{cpf}`          | Deletar Paciente          | Remove o paciente pelo CPF.                                               |
| **Doctor**                               |                           |                                                                           |
| `POST /doctor/create`                    | Criar Doutor              | Cadastra um novo médico.                                                  |
| `POST /doctor/login`                     | Login do Médico           | Autentica o médico por CRM e senha.                                       |
| `PUT /doctor/{crm}`                      | Atualizar Doutor          | Atualiza dados do médico pelo CRM.                                        |
| `DELETE /doctor/{crm}`                   | Deletar Doutor            | Remove o médico pelo CRM.                                                 |
| `GET /doctor`                            | Listar Doutores           | Retorna todos os médicos.                                                 |
| `GET /doctor/search?crm={crm}`           | Buscar por CRM            | Pesquisa médicos pelo CRM.                                                |
| `GET /doctor/search?name={name}`         | Buscar por Nome           | Pesquisa médicos pelo nome completo ou parcial.                           |
| `GET /doctor/search?specialty={specialty}` | Buscar por Especialidade  | Pesquisa médicos pela especialidade.                                      |
| `GET /doctor/search?email={email}`       | Buscar por E-mail         | Pesquisa médicos pelo endereço de e-mail.                                 |
| `GET /doctor/consultations/{crm}`        | Datas de Consultas        | Lista apenas as datas das consultas agendadas para o CRM informado.       |
| **Consultation**                         |                           |                                                                           |
| `POST /consultations/create`             | Agendar Consulta          | Cria nova consulta e envia lembrete por e-mail ao paciente.               |
| `PUT /consultations/update`              | Atualizar Consulta        | Atualiza consulta existente com base nos dados fornecidos.                |
| `GET /consultations/{id}`                | Exibir Consulta           | Retorna informações da consulta pelo ID.                                  |
| `GET /consultations/all`                 | Listar Consultas          | Retorna todas as consultas.                                               |
| `DELETE /consultations/{id}`             | Cancelar Consulta         | Cancela a consulta pelo ID.                                               |
| `GET /consultations/patient-history/{cpf}` | Histórico do Paciente     | Retorna histórico de consultas de um paciente pelo CPF.                   |
| **Diagnosis**                            |                           |                                                                           |
| `POST /diagnosis`                        | Criar Diagnóstico         | Cadastra diagnóstico com descrição, data e CID vinculados à consulta.     |
