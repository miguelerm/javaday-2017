package net.codegardener.employees.model;

public class EmployeesError {
	private String message;
	private String stack;

	public EmployeesError() {
	}

	public EmployeesError(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStack() {
		return stack;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}
}
