package net.codegardener.employees.model;

import java.util.List;

public class EmployeesResult {

	private List<EmployeeSummary> employees;
	private int page;
	private int count;
	private int itemsPerPage;
	
	public EmployeesResult(int page, int itemsPerPage, int count, List<EmployeeSummary> employees) {
		this.page = page;
		this.itemsPerPage = itemsPerPage;
		this.count = count;
		this.employees = employees;
	}
	
	public List<EmployeeSummary> getEmployees() {
		return employees;
	}	
	public int getPage() {
		return page;
	}
	public int getItemsPerPage() {
		return itemsPerPage;
	}
	public int getCount() {
		return count;
	}
	
}
