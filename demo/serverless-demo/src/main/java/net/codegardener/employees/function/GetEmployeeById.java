package net.codegardener.employees.function;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mysql.cj.jdbc.MysqlDataSource;

import net.codegardener.employees.model.Employee;
import net.codegardener.employees.model.EmployeesError;
import net.codegardener.employees.model.ServerlessInput;
import net.codegardener.employees.model.ServerlessJsonOutput;
import net.codegardener.employees.model.ServerlessOutput;

public class GetEmployeeById implements RequestHandler<ServerlessInput, ServerlessOutput> {

	private static final String dbServer = System.getenv("DB_SRV");
	private static final int dbPort = Integer.parseInt(System.getenv("DB_PORT"));
	private static final String dbName = System.getenv("DB_NAME");
	private static final String dbUser = System.getenv("DB_USR");
	private static final String dbPass = System.getenv("DB_PWD");

	@Override
	public ServerlessOutput handleRequest(ServerlessInput input, Context context) {

		MysqlDataSource ds = new MysqlDataSource();

		ds.setDatabaseName(dbName);
		ds.setUser(dbUser);
		ds.setPassword(dbPass);
		ds.setPortNumber(dbPort);
		ds.setServerName(dbServer);

		ServerlessOutput output;

		try {
			Employee employee = null;

			String id = input.getPathParameter("id");

			if (id == null) {
				throw new Exception("Parameter employee id in path must be provided!");
			}

			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(
					"select emp_no, birth_date, first_name, last_name, gender, hire_date from employees where emp_no = ? limit 1");

			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				employee = new Employee();
				employee.setId(rs.getString("emp_no"));
				employee.setBirthDate(rs.getDate("birth_date"));
				employee.setFirstName(rs.getString("first_name"));
				employee.setLastName(rs.getString("last_name"));
				employee.setGender(rs.getString("gender"));
				employee.setHireDate(rs.getDate("hire_date"));
			}

			rs.close();
			stmt.close();
			conn.close();

			if (employee == null) {
				output = new ServerlessJsonOutput(404, new EmployeesError("Employee doesn't exists"));
			} else {
				output = new ServerlessJsonOutput(employee);
			}

		} catch (Exception e) {

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));

			EmployeesError error = new EmployeesError();
			error.setMessage(e.getMessage());
			error.setStack(sw.toString());

			output = new ServerlessJsonOutput(500, error);
		}

		return output;
	}

}
