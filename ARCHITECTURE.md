# 📐 Documentação Arquitetural e Fluxo de Processos

Este documento detalha a estrutura de classes e o fluxo síncrono de execução da camada de persistência implementada neste projeto.

---

## 🗺️ Diagrama de Classes (UML)

O diagrama abaixo ilustra o desacoplamento obtido através do padrão DAO. A classe cliente (`ProgramDepartment` ou `Program`) interage apenas com as interfaces e com a fábrica (`DaoFactory`), sem expor os detalhes de conexão JDBC ou sintaxe SQL do banco MySQL.

```mermaid
classDiagram
    class DB {
        -Connection conn$
        -Properties loadProperties()$
        +getConnection() Connection$
        +closeConnection()$
        +closeStatement(Statement st)$
        +closeResultSet(ResultSet rs)$
    }
    class DaoFactory {
        +createSellerDao() SellerDao$
        +createDepartmentDao() DepartmentDao$
    }
    class DepartmentDao {
        <<interface>>
        +insert(Department obj)
        +update(Department obj)
        +deleteById(Integer id)
        +findById(Integer id) Department
        +findAll() List~Department~
    }
    class SellerDao {
        <<interface>>
        +insert(Seller obj)
        +update(Seller obj)
        +deleteById(Integer id)
        +findById(Integer id) Seller
        +findAll() List~Seller~
        +findByDepartment(Department department) List~Seller~
    }
    class DepartamentDaoJDBC {
        -Connection conn
        -hasSellers(Integer departmentId) boolean
        +insert(Department obj)
        +update(Department obj)
        +deleteById(Integer id)
        +findById(Integer id) Department
        +findAll() List~Department~
    }
    class SellerDaoJDBC {
        -Connection conn
        +insert(Seller obj)
        +update(Seller obj)
        +deleteById(Integer id)
        +findById(Integer id) Seller
        +findAll() List~Seller~
        +findByDepartment(Department department) List~Seller~
    }
    class Department {
        -Integer id
        -String name
    }
    class Seller {
        -Integer id
        -String name
        -String email
        -Date birthDate
        -Double baseSalary
        -Department department
    }
    
    DepartamentDaoJDBC ..|> DepartmentDao
    SellerDaoJDBC ..|> SellerDao
    DepartamentDaoJDBC --> DB : "usa Connection"
    SellerDaoJDBC --> DB : "usa Connection"
    DaoFactory --> DepartamentDaoJDBC : "instancia"
    DaoFactory --> SellerDaoJDBC : "instancia"
    Seller --> Department : "composição (tem um)"
```

---

## 🔄 Fluxo de Processo Linear e Síncrono

Abaixo está o fluxo detalhado de quando o programa tenta realizar a deleção de um departamento. Este processo ilustra a **validação preventiva de integridade referencial (Opção B)** escolhida para proteger a consistência relacional.

```mermaid
sequenceDiagram
    autonumber
    actor Program as Classe Cliente (ProgramDepartment)
    participant Factory as DaoFactory
    participant DAO as DepartamentDaoJDBC
    participant MySQL as Banco de Dados (MySQL)

    Note over Program, MySQL: FASE 1: Inicialização do Contrato
    Program->>Factory: createDepartmentDao()
    Note over Factory: Recupera Conexão ativa via DB.getConnection()
    Factory->>DAO: Instancia DepartamentDaoJDBC(conn)
    Factory-->>Program: Retorna interface DepartmentDao (Polimorfismo)

    Note over Program, MySQL: FASE 2: Solicitação e Validação Preventiva (Síncrona)
    Program->>DAO: deleteById(id)
    DAO->>MySQL: SELECT COUNT(*) FROM seller WHERE DepartmentId = ?
    MySQL-->>DAO: Retorna a contagem de registros

    alt Caso A: Existem Vendedores Vinculados (Count > 0)
        Note over DAO: Bloqueio Lógico de Integridade
        DAO-->>Program: Dispara DbIntegrityException
        Note over Program: Try-Catch captura a exceção, exibe erro na tela e continua o loop do Scanner
    else Caso B: Não existem Vendedores Vinculados (Count == 0)
        Note over Program, MySQL: FASE 3: Comando Síncrono de Deleção
        DAO->>MySQL: DELETE FROM department WHERE Id = ?
        MySQL-->>DAO: Confirmação de linhas afetadas
        DAO-->>Program: Retorna void (sucesso)
        Note over Program: Define success = true e sai do laço do Scanner
    end
```

---

## 📝 Fases Detalhadas do Processo

1.  **Fase de Inicialização:** A classe `Program` solicita um DAO abstrato para a `DaoFactory`. A fábrica cria a conexão com o banco e injeta essa conexão na instância concreta da implementação JDBC, mantendo a classe cliente desacoplada da infraestrutura.
2.  **Fase de Validação Preventiva:** Ao chamar `deleteById(id)`, a classe de implementação executa primeiro um `SELECT COUNT(*)` na tabela de vendedores. Essa verificação é síncrona e impede comandos de remoção inconsistentes antes mesmo que o banco de dados precise rejeitar a operação.
3.  **Fase de Tomada de Decisão:**
    *   **Violação de Integridade:** Se o contador retornar maior que zero, o fluxo de exclusão é abortado e relança uma `DbIntegrityException`. A classe de aplicação captura esse erro no bloco `try-catch`, mantendo o laço de repetição aberto para o usuário tentar outro ID.
    *   **Exclusão Autorizada:** Se o contador for igual a zero, a query de exclusão física (`DELETE`) é enviada ao banco de dados e executada com sucesso.
