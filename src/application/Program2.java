package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {

		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		Department dep = new Department(5, "COMIDA");
		System.out.println("INSERT");
		//departmentDao.insert(dep);
		//System.out.println("Id inserido : " + dep.getId());
		
		System.out.println("\n UPDATE");
		//departmentDao.update(dep);
		System.out.println("Atualizado\n");
		
		System.out.println("DELETE");
		//departmentDao.deleteById(6);
		
		System.out.println("\n FIND BY ID");
		dep = departmentDao.findById(2);
		System.out.println(dep);
		
		System.out.println("\nFIND ALL");
		List<Department> list = departmentDao.findAll();
		for (Department obj : list) {
			System.out.println(obj);
		}
		
		
	}

}
