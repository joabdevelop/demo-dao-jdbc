package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.interfaces.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== Teste: Seller findById ===");
        Seller seller = sellerDao.findById(3);

        System.out.println(seller);

        System.out.println();

        System.out.println("\n=== Teste: Seller findByDepartment ===");

        Department dep = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(dep);

        for (Seller s : list) {
            System.out.println(s);
        }

        System.out.println("\n=== Teste: Seller findAll ===");

        List<Seller> listAll = sellerDao.findAll();

        for (Seller s : listAll) {
            System.out.println(s);
        }

        System.out.println("\n=== Teste: Seller insert ===");
        Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", 
        new java.util.Date(), 2500.0, dep);
        sellerDao.insert(newSeller);
        System.out.println("Inserido: " + newSeller.getId());

    }
}
