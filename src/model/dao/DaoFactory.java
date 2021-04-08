package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;
//Classe auxiliar para instanciar o meus Dao
public class DaoFactory {
	//operacao estaticas
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());//e obrigado vc passar o argumento de conexao
		//A minha classe vai expor, um metodo que retorna o tipo da interface
		//mas internamente ela vai instanciar uma implementacao
		//um macete para nao precisar expor a implementacao, deixando somente a interface
	}

}
