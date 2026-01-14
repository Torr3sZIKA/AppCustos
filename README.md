# AppCustos - Relatório do Trabalho  
## Desenvolvimento para Plataformas Móveis  
### Aplicação de Finanças Pessoais

**Curso:** Licenciatura em Engenharia em Desenvolvimento de Jogos Digitais  
**Aluno:** Guilherme Sousa Torres  
**Número:** 31486  
**Data:** Janeiro de 2026  

---

## Índice

- [Introdução](#introdução)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Lista de Funcionalidades da Aplicação](#lista-de-funcionalidades-da-aplicação)
- [Desenhos, Esquemas e Protótipos](#desenhos-esquemas-e-protótipos)
- [Modelo de Dados](#modelo-de-dados)
- [Tecnologias Usadas](#tecnologias-usadas)
- [Implementação do Projeto](#implementação-do-projeto)
- [Dificuldades](#dificuldades)
- [Conclusões](#conclusões)

---

## Introdução

No âmbito da disciplina de **Desenvolvimento de Jogos para Plataformas Móveis**, decidi criar uma aplicação de gestão de finanças pessoais, focada na monitorização de custos e no controlo orçamental de forma dinâmica e simples.

O objetivo principal da aplicação é oferecer ao utilizador a capacidade de distinguir e organizar fluxos financeiros, mesmo quando paga em dinheiro.

A aplicação foi desenhada para resolver problemas de persistência e acessibilidade de dados, utilizando o **Firebase** como motor de *Cloud*, garantindo que a informação é mantida de forma segura e isolada por utilizador.  
Além disso, o sistema incorpora mecanismos de feedback em tempo real, como notificações de alerta de orçamento, assegurando que o utilizador mantém controlo total sobre a sua saúde financeira.

---

## Estrutura do Projeto

A aplicação foi desenvolvida seguindo o padrão de arquitetura **MVVM (Model–View–ViewModel)**, garantindo a separação de responsabilidades e facilitando a manutenção do código.

### Organização da Estrutura

- **Model**  
  - Classe de dados `Gasto`, que define a estrutura de cada registo.

- **View**  
  - Interfaces desenvolvidas em **Jetpack Compose**:
    - `TelaDeLogin`
    - `TelaDeRegistro`
    - `TelaPrincipal`

- **ViewModel**  
  - `ViewModelLogin`
  - `ViewModelGastos`  
  Responsáveis pela gestão do estado da interface e comunicação com o Firebase.

- **Helper / Services**  
  - Ficheiro `Notificoes`, responsável pela lógica de notificações do sistema Android.

---

## Lista de Funcionalidades da Aplicação

A aplicação oferece um sistema completo de gestão financeira pessoal, incluindo:

- **Autenticação Segura**
  - Login e registo de utilizadores com **Firebase Authentication**.

- **Gestão de Gastos**
  - Adicionar, visualizar e eliminar gastos em tempo real.

- **Definição de Orçamento**
  - Definição de um limite mensal de gastos.

- **Dashboard Visual**
  - Gráfico de distribuição por categorias (Alimentação, Transporte, Saúde, etc.).
  - Barra de progresso do orçamento.

- **Notificações Inteligentes**
  - Confirmação ao adicionar um novo gasto.
  - Alerta crítico quando o orçamento mensal é ultrapassado.

---

## Desenhos, Esquemas e Protótipos

A interface da aplicação foi projetada com um design **Dark/Fosco moderno**, priorizando a usabilidade e a clareza visual.

Não foram criados desenhos, esquemas ou protótipos prévios, uma vez que se optou por uma abordagem simples e visualmente atrativa.

### Diretrizes Visuais

- **Esquema de Cores**
  - Fundo: Preto Profundo `#121212`
  - Cartões: Cinza Elevação `#1E1E1E`

- **Componentes**
  - Cards arredondados (`12.dp`)
  - `LinearProgressIndicator` para feedback visual imediato do orçamento

---

## Modelo de Dados

Os dados estão estruturados de forma hierárquica e segura no **Firebase Firestore**, garantindo isolamento total entre utilizadores.

### Estrutura

- **Coleção Principal:** `usuarios`
  - **Documento:** `UID_do_Utilizador`
    - **Campo:** `orcamento` (Double)

- **Subcoleção:** `gastos`
  - **Documento de Gasto**
    - `descricao` (String)
    - `valor` (Double)
    - `categoria` (String)
    - `data` (Timestamp)

---

## Tecnologias Usadas

A aplicação foi desenvolvida no **Android Studio**, utilizando as seguintes tecnologias:

- **Kotlin**  
  Linguagem base do projeto, escolhida pela sua expressividade e segurança.

- **Jetpack Compose**  
  Framework declarativo para construção da interface, eliminando ficheiros XML.

- **Firebase Authentication**  
  Gestão de autenticação por email e palavra-passe.

- **Firebase Firestore**  
  Base de dados NoSQL para armazenamento de orçamentos e gastos em tempo real.

- **Material Design 3**  
  Biblioteca de componentes modernos com suporte a *Dark Mode*.

- **Android NotificationManager**  
  API utilizada para alertas de orçamento e confirmações de registo.

---

## Implementação do Projeto

A implementação focou-se num sistema flexível e sincronizado com a nuvem, dividindo-se em três eixos principais:

### Arquitetura e Organização
Foi utilizado o padrão **MVVM**, separando a interface da lógica de negócio.  
O **ViewModel** é responsável por calcular o total de gastos e verificar se o orçamento foi atingido.

### Gestão de Dados em Tempo Real
A aplicação está ligada ao **Firebase Firestore**, permitindo atualizações instantâneas:
- Adição de gastos refletida imediatamente na lista e no gráfico.
- Remoção de gastos atualiza automaticamente o total e a barra de progresso.

### Sistema de Alertas Automáticos
Sempre que um novo gasto é adicionado:
- O total é comparado com o orçamento mensal.
- Caso o limite seja ultrapassado, é enviado um alerta através do `NotificationManager`.

---

## Dificuldades

Durante o desenvolvimento, surgiram alguns desafios importantes:

- **Gestão de Permissões de Notificação**
  - Com o Android 13, tornou-se necessário pedir permissões em tempo de execução.

- **Cálculo em Tempo Real**
  - Garantir sincronização precisa entre orçamento, gastos e interface.

- **UI/UX em Jetpack Compose**
  - Adaptação de componentes como `ScrollableTabRow` e `LazyColumn` para um tema Dark Mode personalizado.

---

## Conclusões

O projeto cumpre todos os requisitos propostos, apresentando uma solução dinâmica e visualmente atrativa para a gestão de finanças pessoais.

A integração com serviços *Cloud* garante segurança e acessibilidade dos dados, enquanto o sistema de notificações e orçamento adiciona uma **camada essencial de valor preventivo**.

O uso do **Jetpack Compose** mostrou-se eficiente, permitindo uma interface moderna com menos código e maior facilidade de manutenção.
