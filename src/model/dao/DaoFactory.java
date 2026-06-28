package model.dao;

import db.DB;
import model.dao.implement.DepartamentDaoJDBC;
import model.dao.implement.SellerDaoJDBC;
import model.dao.interfaces.DepartmentDao;
import model.dao.interfaces.SellerDao;

public class DaoFactory {
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}

	public static DepartmentDao createDepartmentDao() {
		return new DepartamentDaoJDBC(DB.getConnection());
	}
}
