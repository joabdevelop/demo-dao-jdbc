# ☕ demo-dao-jdbc - Minha Camada de Persistência Java Standalone

Bem-vindo ao meu repositório! Este projeto é a minha implementação prática de uma camada de persistência de dados de nível corporativo, desenvolvida em **Java Standard Edition (Java SE)** utilizando **JDBC puro (Java Database Connectivity)** e o padrão de projeto **DAO (Data Access Object)**.

Eu desenvolvi este projeto com o objetivo de ir além dos frameworks prontos (como Hibernate/JPA) e entender a fundo como a comunicação de baixo nível com bancos de dados relacionais funciona, lidando diretamente com conexões, transações manuais e otimização de recursos na JVM.

---

## 🏗️ Minhas Decisões de Arquitetura e Design Patterns

Para garantir a qualidade, testabilidade e separação de conceitos no código, apliquei os seguintes padrões arquiteturais:

1. **DAO (Data Access Object):** Encapsulei toda a comunicação com o banco em interfaces (`SellerDao`, `DepartmentDao`). Dessa forma, as minhas classes de negócio e entidades não conhecem SQL ou conexões físicas, o que me dá a liberdade de mudar o banco ou a tecnologia de banco de dados no futuro sem quebrar o core do sistema.
2. **Factory Pattern (`DaoFactory`):** Centralizei a criação e a injeção de dependências concretas dos DAOs (`SellerDaoJDBC`, `DepartamentDaoJDBC`). O restante da aplicação consome apenas os contratos de interface.
3. **Singleton Pattern (`DB.java`):** Garanti a reutilização de uma única conexão ativa (`Connection`), evitando a sobrecarga de abrir e fechar conexões repetidas vezes de forma desnecessária.
4. **Fechamento Seguro de Recursos:** Utilizei rigorosamente blocos `try-catch-finally` para garantir que statements de execução (`PreparedStatement`) e leitores de registros (`ResultSet`) sejam fechados imediatamente após o uso, eliminando qualquer chance de vazamentos de recursos físicos (*Resource Leaks*).
5. **Gerenciamento de Transações (ACID):** Controlei o estado das transações de forma manual (`setAutoCommit(false)`, `commit()` e `rollback()`), garantindo que alterações complexas e dependentes aconteçam de forma atômica (tudo ou nada).
6. **Otimização de Heap (Identity Map Simplificado):** Implementei uma estrutura de mapa local (`Map<Integer, Department>`) durante a leitura de grandes consultas. Isso evita que eu instancie departamentos duplicados na memória Heap do Java para vendedores que pertencem ao mesmo departamento, reduzindo o consumo de memória RAM e economizando ciclos de processamento do Garbage Collector.

---

## 🧠 Meu Processo de Aprendizado e Mentoria com IA

Neste projeto, o meu foco principal foi dominar o **"Como Pensar"** e solidificar minha lógica de programação backend. 

Para alcançar esse nível de maturidade:
- **Testes de Mesa Manuais:** Desenhei o rastreamento de variáveis passo a passo (simulando a stack e heap do computador) para planejar a execução lógica de estruturas repetitivas complexas e interações de loops do console.
- **Parceria com IA (Antigravity):** Desenvolvi o projeto em parceria de *Pair Programming* com a IA Antigravity (Google DeepMind). A IA atuou estritamente como minha **mentora de engenharia**. Ela não gerou códigos prontos para mim; em vez disso, ela me desafiou com analogias, testes de mesa explicativos e análises profundas de controle de fluxo de dados (como o refinamento da leitura de dados no menu de deleção). Essa dinâmica foi essencial para expandir o meu raciocínio lógico e entender o impacto real de cada linha de código que escrevi.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 25.0.3 (Oracle JDK)
- **Banco de Dados:** MySQL (gerenciado localmente via Laragon/Workbench)
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
Configure o banco de dados relacional MySQL localmente e crie as tabelas `department` e `seller`.

### 3. Configurar as Propriedades de Conexão
Crie um arquivo chamado `db.properties` na raiz do projeto com as credenciais do seu banco de dados:
```properties
dburl=jdbc:mysql://localhost:3306/coursejdbc
user=seu_usuario
password=sua_senha
useSSL=false
```

---

```text
          (  )   (   )
           ) (    ) (
          (   )  (   )
         .-------------.
        /               \___
       |  J A V A       |   \
       |  D A O         |    |
       |  J D B C       |___/
        \               /
         '-------------'
           \_________/
```
