package net.codegardener.employees.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.MysqlDataSource;

import net.codegardener.employees.model.Employee;
import net.codegardener.employees.model.EmployeeSummary;
import net.codegardener.employees.model.EmployeesResult;

public class EmployeesRepository {

	private static final String dbServer = System.getenv("DB_SRV");
	private static final int dbPort = Integer.parseInt(System.getenv("DB_PORT"));
	private static final String dbName = System.getenv("DB_NAME");
	private static final String dbUser = System.getenv("DB_USR");
	private static final String dbPass = System.getenv("DB_PWD");
	
	private static final int itemsPerPage = 20;
	
	
	public EmployeesResult getAll(int page) throws SQLException {
		
		List<EmployeeSummary> employees;
		int count;
		
		Connection conn = getConnection();
		
		count = countAllEmployees(conn);
		employees = count > 0 ? getAllEmployees(page, conn) : null;		
		
		conn.close();
		
		System.out.println("employees count: " + count);
		return new EmployeesResult(page, itemsPerPage, count, employees);
	}



	private int countAllEmployees(Connection conn) throws SQLException {
		int count = 0;
		PreparedStatement stmt = getCountStatement(conn);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			count = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		return count;
	}



	private static List<EmployeeSummary> getAllEmployees(int page, Connection conn) throws SQLException {
		List<EmployeeSummary> employees = new ArrayList<EmployeeSummary>();
		int offset = getQueryOffset(page);
		PreparedStatement stmt2 = getSelectAllStatement(conn, itemsPerPage, offset);
		ResultSet rs2 = stmt2.executeQuery();

		while (rs2.next()) {
			EmployeeSummary employee = createEmployeeSummary(rs2);
			employees.add(employee);
		}
		
		rs2.close();
		stmt2.close();
		
		System.out.println("get employees limit: " + itemsPerPage + " offset: " + offset);
		return employees;
	}


	
	public Employee getSingle(String id) throws SQLException {
		Employee employee = null;
		
		Connection conn = getConnection();
		PreparedStatement stmt = getSelectSingleStatement(conn, id);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			employee = createEmployee(rs);
		}

		rs.close();
		stmt.close();
		conn.close();
		
		return employee;
	}

	public Boolean delete(String id) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement stmt = getDeleteStatement(conn, id);
		int recordsDeleted = stmt.executeUpdate();
		stmt.close();
		conn.close();
		
		return recordsDeleted > 0;
	}
	
	public Boolean update(Employee employee) throws SQLException {
		
		Connection conn = getConnection();
		PreparedStatement stmt = getUpdateStatement(conn, employee);
		int recordsUpdated = stmt.executeUpdate();
		stmt.close();
		conn.close();
		
		return recordsUpdated > 0;
	}

	private static PreparedStatement getUpdateStatement(Connection conn, Employee employee) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				"update employees set birth_date = ?, first_name = ?, last_name = ?, gender = ?, hire_date = ? where emp_no = ?");

		stmt.setDate(1, new Date(employee.getBirthDate().getTime()));
		stmt.setString(2, employee.getFirstName());
		stmt.setString(3, employee.getLastName());
		stmt.setString(4, employee.getGender());
		stmt.setDate(5, new Date(employee.getHireDate().getTime()));
		stmt.setString(6, employee.getId());
		return stmt;
	}
	
	
	private static PreparedStatement getSelectSingleStatement(Connection conn, String id) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				"select emp_no, birth_date, first_name, last_name, gender, hire_date from employees where emp_no = ? limit 1");

		stmt.setString(1, id);
		return stmt;
	}
	
	private static PreparedStatement getDeleteStatement(Connection conn, String id) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("delete from employees where emp_no = ?");

		stmt.setString(1, id);
		return stmt;
	}
	
	private static PreparedStatement getSelectAllStatement(Connection conn, int limit, int offset) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				"select emp_no, concat(first_name, \" \", last_name) full_name from employees limit ? offset ?");

		stmt.setInt(1, limit);
		stmt.setInt(2, offset);
		return stmt;
	}
	
	private static PreparedStatement getCountStatement(Connection conn) throws SQLException {
		return conn.prepareStatement("select count(*) from employees");
	}
	
	private static int getQueryOffset(int page) {
		int limit = (page - 1) * itemsPerPage;
		return limit;
	}

	private static Connection getConnection() throws SQLException {
		MysqlDataSource ds = new MysqlDataSource();

		ds.setDatabaseName(dbName);
		ds.setUser(dbUser);
		ds.setPassword(dbPass);
		ds.setPortNumber(dbPort);
		ds.setServerName(dbServer);
		
		return ds.getConnection();
	}

	private static Employee createEmployee(ResultSet rs) throws SQLException {
		Employee employee;
		employee = new Employee();
		employee.setId(rs.getString("emp_no"));
		employee.setBirthDate(rs.getDate("birth_date"));
		employee.setFirstName(rs.getString("first_name"));
		employee.setLastName(rs.getString("last_name"));
		employee.setGender(rs.getString("gender"));
		employee.setHireDate(rs.getDate("hire_date"));
		return employee;
	}
	
	private static EmployeeSummary createEmployeeSummary(ResultSet rs) throws SQLException {
		EmployeeSummary employee;
		employee = new EmployeeSummary();
		employee.setId(rs.getString("emp_no"));
		employee.setFullName(rs.getString("full_name"));
		return employee;
	}





}
