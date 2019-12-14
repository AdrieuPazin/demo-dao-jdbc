package model.dao.impl;

import java.sql.Connection;
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

public class SellerDaoJDBC implements SellerDao{

	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		
		PreparedStatement st = null;
		try {
			
			String sql = "INSERT INTO seller " + 
					"(Name, Email, BirthDate, BaseSalary, DepartmentId) " + 
					"VALUES " + 
					"(?, ?, ?, ?, ?)";
			
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rows = st.executeUpdate();
			
			
			if (rows > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					//atribuindo o id inserido no db no objeto
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Erro inesperado. Nehuma linha alterada");
			}
			
			
		} catch (SQLException e) {
			
			throw new DbException(e.getMessage());
			
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			
			String sql = "UPDATE seller " + 
					"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + 
					"WHERE Id = ?";
			
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
						
		} catch (SQLException e) {
			
			throw new DbException(e.getMessage());
			
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			
			String sql = "DELETE FROM seller " + 
					"WHERE Id = ?";
			
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, id);
			
			st.executeUpdate();
						
		} catch (SQLException e) {
			
			throw new DbException(e.getMessage());
			
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			String sql = "SELECT seller.*,department.Name as DepName " + 
					"FROM seller INNER JOIN department " + 
					"ON seller.DepartmentId = department.Id " + 
					"WHERE seller.Id = ?";
			
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			rs = st.executeQuery();
			
			//testando se veio algum resultado, se true pega os dados do result e instancia os objetos
			if (rs.next()) {
				Department dep = inicializacaoDepartment(rs);
				
				Seller obj = inicializacaoSeller(rs, dep);
				return obj;
				
			}
			
			return null;
			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

		
		
	}

	private Seller inicializacaoSeller(ResultSet rs, Department dep) throws SQLException {

		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		
		return obj;
	}

	private Department inicializacaoDepartment(ResultSet rs) throws SQLException {
		
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			String sql = "SELECT seller.*,department.Name as DepName " + 
					"FROM seller INNER JOIN department " + 
					"ON seller.DepartmentId = department.Id " + 
					"ORDER BY Name";
			
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			
			//testando se veio algum resultado, se true pega os dados do result e instancia os objetos
			List<Seller> list  = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while (rs.next()) {
			    //verifico se j� existe algum departamento com o ID, se for nullo a� sim instancio um novo departamento
				//Se j� existir um departamento, n�o � instanciado um novo
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					dep = inicializacaoDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = inicializacaoSeller(rs, dep);
				list.add(obj);
				
				
			}
			return list;
			

			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public List<Seller> findByDepartment(Department department) {

		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			String sql = "SELECT seller.*,department.Name as DepName " + 
					"FROM seller INNER JOIN department " + 
					"ON seller.DepartmentId = department.Id " + 
					"WHERE DepartmentId = ? " + 
					"ORDER BY Name";
			
			st = conn.prepareStatement(sql);
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			//testando se veio algum resultado, se true pega os dados do result e instancia os objetos
			List<Seller> list  = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while (rs.next()) {
			    //verifico se j� existe algum departamento com o ID, se for nullo a� sim instancio um novo departamento
				//Se j� existir um departamento, n�o � instanciado um novo
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					dep = inicializacaoDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = inicializacaoSeller(rs, dep);
				list.add(obj);
				
				
			}
			return list;
			

			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	
	
	
}
