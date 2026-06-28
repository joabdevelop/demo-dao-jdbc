package model.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.interfaces.DepartmentDao;
import model.entities.Department;

public class DepartamentDaoJDBC implements DepartmentDao {
    
    private Connection conn;

    public DepartamentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                "INSERT INTO department (Name) VALUES (?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            st.setString(1, obj.getName());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    obj.setId(rs.getInt(1));
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Erro ao inserir departamento.");
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao inserir departamento: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                "UPDATE department SET Name = ? WHERE Id = ?"
            );
            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao atualizar departamento: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        if (hasSellers(id)) {
            throw new DbException("Erro ao deletar departamento: O departamento possui vendedores cadastrados.");
        }
        
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                "DELETE FROM department WHERE Id = ?"
            );
            st.setInt(1, id);
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("Erro ao deletar departamento: Nenhum departamento encontrado com o id: " + id);
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao deletar departamento: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    private boolean hasSellers(Integer departmentId) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                "SELECT COUNT(*) FROM seller WHERE DepartmentId = ?"
            );
            st.setInt(1, departmentId);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new DbException("Erro ao verificar vendedores: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                "SELECT * FROM department WHERE Id = ?"
            );
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Department obj = new Department();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                return obj;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar departamento: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                "SELECT * FROM department ORDER BY Name"
            );
            rs = st.executeQuery();

            List<Department> list = new ArrayList<>();
            while (rs.next()) {
                Department obj = new Department();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                list.add(obj);
            }
            return list;

        } catch (SQLException e) {
            throw new DbException("Erro ao buscar departamentos: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
