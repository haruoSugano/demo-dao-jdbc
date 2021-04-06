package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		//Dessa forma o programa nao reconhece a implementacao, ela reconhece apenas a interface
		SellerDao sellerDao = DaoFactory.createSellerDao();//e uma forma de fazer a injecao de independecia, sem explicitar a implementacao
		
		Seller seller = sellerDao.findById(3);
		
		System.out.println(seller);

	}

}
