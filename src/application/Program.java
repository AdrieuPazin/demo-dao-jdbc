package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao();

		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		System.out.println("=====================\n");
		
		Department department = new Department(2, null);
		//List<Seller> list = sellerDao.findByDepartment(department);
		
		List<Seller> list = sellerDao.findAll();
		for (Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("\n INSERT");
		Seller seller2 = new Seller(null, "Nicolle", "passos@hotmail.com", new Date(), 4000.0, department);
		sellerDao.insert(seller2);
		System.out.println("Inserido! Novo ID = " + seller2.getId());
		
	}

}
