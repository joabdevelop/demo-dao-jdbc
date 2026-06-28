# ☕ demo-dao-jdbc - Camada de Persistência Java Standalone

Este repositório contém a implementação completa de uma camada de persistência de dados utilizando **Java Standard Edition (Java SE)**, **JDBC puro (Java Database Connectivity)** e o padrão de projeto **DAO (Data Access Object)**.

O principal objetivo deste projeto é o aprendizado prático e profundo sobre como o ecossistema Java gerencia conexões, executa consultas SQL de forma segura e estrutura o isolamento lógico da persistência de dados em aplicações profissionais de nível corporativo.

---

## 🏗️ Arquitetura e Padrões de Projeto

O projeto foi construído do zero, sem o uso de frameworks ORM (como Hibernate/JPA), para garantir a compreensão exata da comunicação de baixo nível com bancos de dados relacionais (MySQL). As seguintes práticas e padrões arquiteturais foram implementados:

1. **DAO (Data Access Object):** Encapsula todo o acesso aos dados em interfaces (`SellerDao`, `DepartmentDao`). A lógica de negócio e as entidades do sistema não conhecem SQL ou conexões físicas, tornando a aplicação flexível para futuras migrações de tecnologias de persistência.
2. **Factory Pattern (`DaoFactory`):** Centraliza a criação e injeção de dependências concretas dos DAOs (`SellerDaoJDBC`, `DepartamentDaoJDBC`), permitindo que a aplicação consuma apenas os contratos de interface.
3. **Singleton Pattern (`DB.java`):** Garante a reutilização de uma única conexão de banco de dados (`Connection`), controlando de forma segura a abertura e fechamento de conexões físicas.
4. **Tratamento Seguro de Recursos:** Utilização rigorosa dos blocos `try-catch-finally` para garantir que statements de execução (`PreparedStatement`) e leitores de registros (`ResultSet`) sejam fechados no banco de dados, prevenindo vazamentos de recursos físicos (*Resource Leaks*).
5. **Gerenciamento de Transações (ACID):** Lógica transacional manual utilizando `setAutoCommit(false)`, `commit()` e `rollback()` para assegurar que múltiplas operações no banco de dados ocorram em lote e em formato "tudo ou nada", mantendo a integridade referencial.
6. **Otimização de Heap (Identity Map Simplificado):** Implementação de controle com estruturas de mapeamento (`Map<Integer, Department>`) durante a leitura de ResultSets volumosos. Isso evita a duplicação redundante de entidades (instâncias repetidas de departamentos com a mesma chave) na memória Heap do Java, reduzindo o uso de memória e a pressão sobre a CPU e o Garbage Collector.

---

## 🧠 Metodologia de Estudos & Parceria com IA

O desenvolvimento deste projeto seguiu uma metodologia focada no **"Como Pensar"** e na consolidação sólida da lógica de programação. 

Em vez de apenas codificar de forma reativa, o processo de evolução contou com:
- **Testes de Mesa Manuais:** Rastreamento sistemático passo a passo da stack e heap de variáveis para entender o fluxo de estruturas repetitivas, loops de console e prevenção de comportamentos de sobreescrita de memória.
- **Mentoria Tecnológica com IA (Antigravity):** O desenvolvimento foi realizado em parcerias de *Pair Programming* com a IA Antigravity (desenvolvida pela Google DeepMind). O auxílio da IA foi estritamente didático e estratégico, focado em refinar a lógica de algoritmos complexos (como o tratamento de controle do loop de exclusão de departamentos) e analisar impactos de concorrência em transações, elevando as competências de engenharia de software do desenvolvedor ao invés de apenas fornecer códigos automáticos.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 25.0.3 (Oracle JDK)
- **Banco de Dados:** MySQL (configurado via ambiente local)
- **Conectividade:** JDBC (MySQL Connector/J)
- **IDE / Editor:** Antigravity IDE (VS Code) / Eclipse
- **Controle de Versão:** Git & GitHub CLI

---

## ⚙️ Como Executar o Projeto

### 1. Clonar o Repositório
```bash
git clone https://github.com/joabdevelop/demo-dao-jdbc.git
cd demo-dao-jdbc
```

### 2. Configurar o Banco de Dados
Crie um esquema de banco de dados no seu servidor MySQL (ex: Laragon ou MySQL Workbench) e rode as tabelas de `department` e `seller`.

### 3. Configurar as Propriedades de Conexão
Crie ou edite o arquivo `db.properties` na raiz do projeto com as suas credenciais locais:
```properties
dburl=jdbc:mysql://localhost:3306/coursejdbc
user=seu_usuario
password=sua_senha
useSSL=false
```

---

```text
       ~
      ( )
     (   )
    .-----.
   /       \   __
  |         | /  \
  |   JAVA  |/   |
   \       / \__/
    '-----'
```
