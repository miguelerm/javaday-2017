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
    return this.get(`/employees/${id}/`) as Observable<Employee>;
  }

  private get(path: string) {
    return this.http.get(apiBaseUrl + path);
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
  id: string;
  birthDate: Date;
  firstName: string;
  lastName: string;
  gender: Gender;
  hireDate: Date;
}

export enum Gender {
  Male = 'M',
  Female = 'F',
}
