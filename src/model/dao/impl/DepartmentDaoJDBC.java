package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department department) {
		PreparedStatement st = null;

		try {

			String sql = "INSERT INTO department (name) VALUES (?)";

			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, department.getName());

			int row = st.executeUpdate();

			if (row > 0) {

				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					department.setId(rs.getInt(1));
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
	public void update(Department department) {
		PreparedStatement st = null;
		try {
			
			String sql = "UPDATE department " + 
					"SET Name = ? " + 
					"WHERE Id = ?";
			
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, department.getName());
			st.setInt(2, department.getId());
			
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
			
			String sql = "DELETE FROM department " + 
					"WHERE Id = ?";
			
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, id);
			
			int rows = st.executeUpdate();
						
			if (rows == 0) {
				throw new DbException("Registro inexistente");
			}
			
		} catch (SQLException e) {
			
			throw new DbException(e.getMessage());
			
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) {
		return null;
	}

	@Override
	public List<Department> findAll() {
		return null;
	}

}
