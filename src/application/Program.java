package application;

import java.sql.Connection;
import java.util.Date;

import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

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

        Seller seller = new Seller(1, "Bob", "bob@gmail.com", new Date(), 3000.00, dep);

        SellerDao sellerDao = DaoFactory.createSellerDao();

        

        System.out.println(seller);
    }
}
