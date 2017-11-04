package net.codegardener.employees.function;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import net.codegardener.employees.model.EmployeesError;
import net.codegardener.employees.model.EmployeesResult;
import net.codegardener.employees.model.ServerlessInput;
import net.codegardener.employees.model.ServerlessJsonOutput;
import net.codegardener.employees.model.ServerlessOutput;
import net.codegardener.employees.repository.EmployeesRepository;

public class GetEmployees implements RequestHandler<ServerlessInput, ServerlessOutput> {

	@Override
	public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {

		ServerlessOutput output;
		
		int page = getPage(serverlessInput);
		
		try {
			
			EmployeesRepository repository = new EmployeesRepository();
			EmployeesResult employees = repository.getAll(page);
			output = new ServerlessJsonOutput(employees);
			
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

	private int getPage(ServerlessInput serverlessInput) {
		int page = 1;
		String pageParameter = serverlessInput.getQueryStringParameter("page");
		
		if (pageParameter != null) {
			try {
				page = Integer.parseInt(pageParameter);
				if (page < 1) {
					page = 1;
				}
			} catch (Exception e) {
				page = 1;
			}
		}
		return page;
	}
}