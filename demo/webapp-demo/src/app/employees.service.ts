import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

const apiBaseUrl = 'https://71jw3r1w69.execute-api.us-east-1.amazonaws.com/Prod';

@Injectable()
export class EmployeesService {

  constructor(private http: HttpClient) { }

  public getAll(page: number = 1) {
    return this.get(`/employees/?page=${page}`) as Observable<EmployeesResult>;
  }

  public getSingle(id: string) {
    let result: Observable<Employee>;

    result = this.get(`/employees/${id}/`) as Observable<Employee>;

    result.subscribe(x => {
      x.birthDate = new Date(x.birthDate as string);
      x.hireDate = new Date(x.hireDate as string);
    });

    return result;
  }

  public remove(id: string) {
    return this.delete(`/employees/${id}/`) as Observable<Object>;
  }

  public save(id: string, employee: Employee) {
    const firstName = employee.firstName;
    const lastName = employee.lastName;
    const birthDate = employee.birthDate;
    const gender = employee.gender;
    const hireDate = employee.hireDate;
    return this.put(`/employees/${id}/`, { firstName, lastName, birthDate, gender, hireDate } );
  }

  private get(path: string) {
    return this.http.get(apiBaseUrl + path);
  }

  private delete(path: string) {
    return this.http.delete(apiBaseUrl + path);
  }

  private put(path: string, data: Object) {
    return this.http.put(apiBaseUrl + path, JSON.stringify(data), { headers: { 'Content-Type' : 'application/json' }});
  }
}

export interface EmployeesResult {
  page: number;
  count: number;
  itemsPerPage: number;
  employees: EmployeeSummary[];
}

export interface EmployeeSummary {
  id: string;
  fullName: string;
}

export interface Employee {
  id?: string;
  birthDate?: string|Date;
  firstName?: string;
  lastName?: string;
  gender?: Gender;
  hireDate?: string|Date;
}

export enum Gender {
  Male = 'M',
  Female = 'F',
}
