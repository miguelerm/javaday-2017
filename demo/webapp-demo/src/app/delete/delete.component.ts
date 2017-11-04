import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Employee, EmployeesService } from '../employees.service';

@Component({
  selector: 'app-delete',
  templateUrl: './delete.component.html'
})
export class DeleteComponent implements OnInit {

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

  onSubmit() {
    const id = this.getId();
    this.employeesService.remove(id).subscribe(() => this.router.navigate(['/list']), () => this.error = true);
  }

  private getId() {
    return this.route.snapshot.paramMap.get('id');
  }
}
