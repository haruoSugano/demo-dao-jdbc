package model.dao.impl;//impl abreviacao de implementacao "implement"

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		PreparedStatement st = null;
		try{
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+"VALUES "
					+"(?, ?, ?, ?, ?) ",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);//Fechar aqui porque foi criado dentro do if, e como ela nao vai existir no escopo do finally
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try{
			st = conn.prepareStatement(
					"UPDATE seller "
					+"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " 
					+"WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
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
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			st = conn.prepareStatement(//Importante no final de cada linha dar um espaco, se nao, causa erro
					"SELECT seller.*,department.Name as DepName "//vai buscar todos os vendedores e departamento
					+"FROM seller INNER JOIN department "//fazer inner join
					+"ON seller.DepartmentId = department.Id "
					+"ORDER BY Name ");//ordenar por nome
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();//Criar uma estrutura Map vazia
			//Guarudar neste map qualquer departamento que instanciar, cada vez que passar pelo while, ele faz o teste se o departament existe
			
			while(rs.next()) {//Percorrer o resultSet enquanto estiver o proximo
				//Para cada valor do resultSet
				
				Department dep = map.get(rs.getInt("DepartmentId"));//testar se o departamento ja existe
			//Faz o teste se departamento existe/ map.get busca o departamento que tem rs.getInt("DepartmentId")
				//Significa que esta buscando dentro o map se existe o departamento escolhido, se for null instancia o departamento
				
				if (dep == null) {//Controle para nao repetir departamento
					dep = instantiateDepartment(rs);//precisa instanciar o departamento
					map.put(rs.getInt("DepartmentId"), dep);//guarda o departamento instanciado
				}
				
				Seller obj = instantiateSeller(rs, dep);//instanciar o vendedor
				list.add(obj);//adicionar o vendedor na lista
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());//lancar minha excessao personalizada
		}
		finally {//fehcando meus recursos
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {//Como sao varios valores, precisa ser lista
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			st = conn.prepareStatement(//Importante no final de cada linha dar um espaco, se nao, causa erro
					"SELECT seller.*,department.Name as DepName "
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+"WHERE DepartmentId = ? "
					+"ORDER BY Name ");
			
			st.setInt(1,department.getId());// ? e o id do departamento department.getId
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();//Criar uma estrutura Map vazia
			//Guarudar neste map qualquer departamento que instanciar, cada vez que passar pelo while, ele faz o teste se o departament existe
			
			while(rs.next()) {//Percorrer o resultSet enquanto estiver o proximo
				//Para cada valor do resultSet
				Department dep = map.get(rs.getInt("DepartmentId"));//testar se o departamento ja existe
			//Faz o teste se departamento existe/ map.get busca o departamento que tem rs.getInt("DepartmentId")
				//Significa que esta buscando dentro o map se existe o departamento escolhido, se for null instancia o departamento
				if (dep == null) {//Controle para nao repetir departamento
					dep = instantiateDepartment(rs);//precisa instanciar o departamento
					map.put(rs.getInt("DepartmentId"), dep);//guarda o departamento instanciado
				}
				
				Seller obj = instantiateSeller(rs, dep);//instanciar o vendedor
				list.add(obj);//adicionar o vendedor na lista
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());//lancar minha excessao personalizada
		}
		finally {//fehcando meus recursos
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
