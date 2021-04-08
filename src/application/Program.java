package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		//Dessa forma o programa nao reconhece a implementacao, ela reconhece apenas a interface
		SellerDao sellerDao = DaoFactory.createSellerDao();//e uma forma de fazer a injecao de independecia, sem explicitar a implementacao
		
		System.out.println("=== TEST 1: seller findById ====");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("=== TEST 1: seller findById ====");
		Department department = new Department (2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		for(Seller obj : list) {
			System.out.println(obj);
		}

	}

}
