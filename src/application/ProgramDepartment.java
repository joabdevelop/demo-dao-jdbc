package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.interfaces.DepartmentDao;
import model.entities.Department;

public class ProgramDepartment {

    public static void main(String[] args) {
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        Scanner sc = new Scanner(System.in);

        System.out.println("=== Teste: Department findById ===");
        Department department = departmentDao.findById(1);
        System.out.println(department);

        System.out.println("\n=== Teste: Department findAll ===");
        List<Department> list = departmentDao.findAll();
        for (Department d : list) {
            System.out.println(d);
        }

        System.out.println("\n=== Teste: Department insert ===");
        Department newDepartment = new Department(null, "Music");
        departmentDao.insert(newDepartment);
        System.out.println("Inserido: " + newDepartment.getId());

        System.out.println("\n=== Teste: Department update ===");
        department = departmentDao.findById(1);
        department.setName("Music");
        departmentDao.update(department);
        System.out.println("Updated completed");

        System.out.println("\n=== Teste: Department delete ===");
        boolean success = false;

        while (!success) {
            try{

                System.out.print("Digite o id para deletar ou 0 para sair: ");
                int id = sc.nextInt();

                if (id == 0) {
                    System.out.println("Saindo...");
                    break;
                }

                departmentDao.deleteById(id);
                System.out.println("Delete completed");

                System.out.println("\n=== Teste: Department findAll ===");
                list = departmentDao.findAll();
                for (Department d : list) {
                    System.out.println(d);
                }

                System.out.println("\n=== Deseja deletar mais algum ? (s/n) ===");
                String resp = sc.next();
                
                if (resp.equalsIgnoreCase("s")) {
                    success = false;
                } else {
                    System.out.println("Saindo...");
                    success = true;
                }

            } catch(db.DbIntegrityException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Tente deletar outro id de departamento.");
            } 
            catch(db.DbException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Tente novamente.");
            }
        }

        sc.close();

    }

}
