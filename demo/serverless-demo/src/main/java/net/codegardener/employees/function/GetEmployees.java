package net.codegardener.employees.function;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import net.codegardener.employees.model.Employee;
import net.codegardener.employees.model.ServerlessInput;
import net.codegardener.employees.model.ServerlessJsonOutput;
import net.codegardener.employees.model.ServerlessOutput;

public class GetEmployees implements RequestHandler<ServerlessInput, ServerlessOutput> {

	@Override
	public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {

		List<Employee> employees = new ArrayList<Employee>();

		employees.add(new Employee());
		employees.add(new Employee());

		return new ServerlessJsonOutput(employees);
	}
}