package model.dao.interfaces;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
    void insert(Seller obj); // Insere um vendedor

    void update(Seller obj); // Atualiza um vendedor

    void deleteById(Integer id); // Deleta um vendedor por id

    Seller findById(Integer id); // Busca um vendedor por id

    List<Seller> findAll(); // Busca todos os vendedores

    List<Seller> findByDepartment(Department department); // Busca vendedores por departamento
}