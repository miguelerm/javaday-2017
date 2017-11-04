import { Component, OnInit } from '@angular/core';
import { EmployeesService, EmployeeSummary } from '../employees.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html'
})
export class ListComponent implements OnInit {

  public title = 'app';
  public employees: EmployeeSummary[];
  public pageRecords: number;
  public totalRecords: number;
  public isFirstPage: boolean;
  public isLastPage: boolean;
  public pagesCount: number;
  public pages: number[];

  private currentPage: number;

  constructor(
    private employeesService: EmployeesService,
    private router: Router) { }

  ngOnInit(): void {
    this.getEmployees();
  }

  previousPage(event: Event) {
    event.preventDefault();
    this.changePage(-1);
  }

  nextPage(event: Event) {
    event.preventDefault();
    this.changePage(+1);
  }

  setPage(event: Event, page: number) {
    event.preventDefault();

    if (this.isCurrentPage(page) || this.isPlaceholderPage(page)) {
      return;
    }

    this.currentPage = page;
    this.getEmployees();
  }

  isCurrentPage(page: number) {
    return this.currentPage === page;
  }

  isPlaceholderPage(page: number) {
    return page < 1;
  }

  goToEdit(employee: EmployeeSummary) {
    this.router.navigate(['/edit', employee.id]);
  }

  goToDelete(employee: EmployeeSummary) {
    this.router.navigate(['/delete', employee.id]);
  }

  private changePage(addendum: number) {
    this.currentPage += addendum;
    this.getEmployees();
  }

  private getEmployees() {
    this.employeesService.getAll(this.currentPage).subscribe(result => {
      this.employees = result.employees;
      this.totalRecords = result.count;
      this.pageRecords = result.itemsPerPage;
      this.currentPage = result.page;
      this.isFirstPage = this.currentPage === 1;
      this.pagesCount = this.totalRecords === 0 ? 0 : Math.ceil(this.totalRecords / this.pageRecords);
      this.isLastPage = this.currentPage === this.pageRecords;
      this.computePageNumbers();
    });
  }

  private computePageNumbers() {
    this.pages = [];

    if (this.pagesCount > 1) {
      this.pages.push(1);

      let start = this.currentPage - 3;
      let end = this.currentPage + 3;

      if (start < 1) {
        start = 1;
        end = 7;
      }

      if (start > 2) {
        this.pages.push(-1);
      }

      if (end > this.pagesCount) {
        start = this.pagesCount - 6;
        end = this.pagesCount;
      }

      for (let page = start; page <= end; page++) {

        if (page > 1 && page < this.pagesCount) {
          this.pages.push(page);
        }

      }

      if (end < this.pagesCount - 1) {
        this.pages.push(-1);
      }

      this.pages.push(this.pagesCount);
    }

  }
}
