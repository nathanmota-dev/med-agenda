<div align="center">
    <img src="https://github.com/nathanmota-dev/final-project-poo2/blob/main/frontend/public/logo2.png" alt="Logo" />
</div>

# Med Agenda

Med Agenda é um projeto desenvolvido para o gerenciamento de um consultório médico, incluindo a gestão de pacientes, doutores e consultas. Foi feito com Spring Boot e PostgreSQL, e atende aos requisitos de utilizar 5 padrões de projeto e 3 princípios de design.

A documentação completa está disponível na [Wiki do projeto](https://github.com/nathanmota-dev/final-project-poo2/wiki).

## Rotas da API

| Rota                             | Função               | Descrição                                                       |
|----------------------------------|----------------------|-----------------------------------------------------------------|
| **HelloWorld**                   |                      |                                                                 |
| `/`                              | Hello World          | Exibe uma mensagem "Hello World".                               |
| **Admin**                        |                      |                                                                 |
| `/admin/login`                   | Login do Admin       | Autentica o admin com e-mail e senha.                           |
| `/admin/create`                  | Criar Admin          | Cria um novo admin.                                             |
| `/admin/{id}`                    | Exibir Admin         | Retorna as informações do admin pelo id.                        |
| `/admin/{id}`                    | Deletar Admin        | Remove o admin com o id especificado.                           |
| **Patient**                      |                      |                                                                 |
| `/patients/create`               | Criar Paciente       | Adiciona um novo paciente.                                      |
| `/patients/{cpf}`                | Exibir Paciente      | Retorna as informações do paciente pelo CPF.                    |
| `/patients/list`                 | Listar Pacientes     | Retorna todos os pacientes.                                     |
| `/patients/update/{cpf}`         | Atualizar Paciente   | Atualiza as informações do paciente com o CPF especificado.     |
| `/patients/delete/{cpf}`         | Deletar Paciente     | Remove o paciente com o CPF especificado.                       |
| **Doctor**                       |                      |                                                                 |
| `/doctor/create`                 | Criar Doutor         | Adiciona um novo médico.                                        |
| `/doctor/{crm}`                  | Atualizar Doutor     | Atualiza as informações do médico com o CRM especificado.       |
| `/doctor/{crm}`                  | Deletar Doutor       | Remove o médico com o CRM especificado.                         |
| `/doctor`                        | Listar Doutores      | Retorna todos os médicos.                                       |
| `/doctor/search?crm={crm}`       | Buscar por CRM       | Pesquisa médicos pelo CRM.                                      |
| `/doctor/search?name={name}`     | Buscar por Nome      | Pesquisa médicos pelo nome.                                     |
| `/doctor/search?specialty={specialty}` | Buscar por Especialidade | Pesquisa médicos pela especialidade.                  |
| **Consultation**                 |                      |                                                                 |
| `/consultations/create`          | Agendar Consulta     | Cria uma nova consulta.                                         |
| `/consultations/update`          | Atualizar Consulta   | Atualiza uma consulta com base nos dados fornecidos.            |
| `/consultations/{id}`            | Exibir Consulta      | Retorna as informações da consulta pelo id.                     |
| `/consultations/all`             | Listar Consultas     | Retorna todas as consultas.                                     |
| `/consultations/{id}`            | Cancelar Consulta    | Cancela a consulta com o id especificado.                       |
| `/consultations/patient-history/{cpf}` | Histórico do Paciente | Retorna o histórico de consultas de um paciente pelo CPF.     |
