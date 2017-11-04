import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Employee, EmployeesService } from '../employees.service';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html'
})
export class DetailsComponent implements OnInit {

  public employee: Employee;
  public error: boolean;

  constructor(
    private employeesService: EmployeesService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit() {
    const id = this.getId();
    this.employeesService.getSingle(id).subscribe(employee => this.employee = employee);
  }

  private getId() {
    return this.route.snapshot.paramMap.get('id');
  }

}
