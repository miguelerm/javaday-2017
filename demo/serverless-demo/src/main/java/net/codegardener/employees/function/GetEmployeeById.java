package net.codegardener.employees.function;

import java.io.PrintWriter;
import java.io.StringWriter;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import net.codegardener.employees.model.Employee;
import net.codegardener.employees.model.EmployeesError;
import net.codegardener.employees.model.ServerlessInput;
import net.codegardener.employees.model.ServerlessJsonOutput;
import net.codegardener.employees.model.ServerlessOutput;
import net.codegardener.employees.repository.EmployeesRepository;

public class GetEmployeeById implements RequestHandler<ServerlessInput, ServerlessOutput> {

	

	@Override
	public ServerlessOutput handleRequest(ServerlessInput input, Context context) {

		ServerlessOutput output;

		try {

			String id = input.getPathParameter("id");

			if (id == null) {
				throw new Exception("Parameter employee id in path must be provided!");
			}

			EmployeesRepository repository = new EmployeesRepository();
			Employee employee = repository.getSingle(id);
			
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
