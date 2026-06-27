package application;

import java.sql.Connection;
import db.DB;
import model.entities.Department;

public class Program {
    public static void main(String[] args) {
        System.out.println("=== Teste de Conexao ===");
        try {
            Connection conn = DB.getConnection();
            System.out.println("Conectado com sucesso ao MySQL!");
            DB.closeConnection();
            System.out.println("Conexao fechada de forma segura.");
        } catch (Exception e) {
            System.out.println("Erro ao tentar conectar: " + e.getMessage());
        }

        Department dep = new Department(1, "Software");
        System.out.println(dep);
    }
}
