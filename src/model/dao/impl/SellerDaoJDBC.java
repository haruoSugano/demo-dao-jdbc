package model.dao.impl;//impl abreviacao de implementacao "implement"

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{//SellerDaoJDBC implementacao do JDBC do sellerDao
	
	private Connection conn;//Dao vai ter uma dependecia com a conexao
	// fechar conexao em cada funcao, pois o mesmo objeto dao pode ser feita varias operacao
	public SellerDaoJDBC(Connection conn) {// para forcar a dependencia
		this.conn = conn;
	}
	
	//Esqueleto da implementacao
	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByIde(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {// id que chegou como parametro da funcao
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "//precisa de um espaço no final se nao da erro
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			st.setInt(1,id);//1° interrogacao vai receber o id que chegou como parametro da funcao
			rs = st.executeQuery();//o comando sql(st) vai ser excutado e cair no resultSet
			if(rs.next()) {//Testar se veio algum resultado, se minha consulta nao retornou nenhum registro rs = st.executeQuery vai dar falso
				//se rs.next() der verdadeiro entao significa que existe o ID
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);
				return obj;//retornar o objeto seller
				}
			return null;//rs dar falso ele retorna nullo ou seja, nao existe um vendedor com este ID
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());//lancar minha excessao personalizada
		}
		finally {//fehcando meus recursos
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();//instanciando o vendedor
		obj.setId(rs.getInt("Id"));//id do vendedor
		obj.setName(rs.getString("Name"));//nome do vendedor
		obj.setEmail(rs.getString("Email"));// email do vendedor
		obj.setBaseSalary(rs.getDouble("BaseSalary"));//salario minimo do vendedor
		obj.setBirthDate(rs.getDate("BirthDate"));// nascimento do vendedor
		obj.setDepartment(dep);//departamento associado com o vendedor, e um objeto inteiro dep
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();//instanciando o departamento e setando
		dep.setId(rs.getInt("DepartmentId"));//id do departamento set
		dep.setName(rs.getString("DepName"));//nome do departamento set
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
