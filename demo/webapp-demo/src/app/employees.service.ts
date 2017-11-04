import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import 'rxjs/add/observable/of';
import 'rxjs/add/operator/delay';

const apiBaseUrl = 'test'; //'https://71jw3r1w69.execute-api.us-east-1.amazonaws.com/Prod';

@Injectable()
export class EmployeesService {

  constructor(private http: HttpClient) { }

  public getAll(page: number = 1) {
    if (apiBaseUrl === 'test') {
      return Observable.of(getTestEmployees(page)).delay(100);
    }
    return this.get(`/employees/?page=${page}`) as Observable<EmployeesResult>;
  }

  public getSingle(id: string) {
    let result: Observable<Employee>;

    if (apiBaseUrl === 'test') {
      result = Observable.of(getTestEmployee(id)).delay(100);
    } else {
      result = this.get(`/employees/${id}/`) as Observable<Employee>;
    }

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
    return this.http.put(path, JSON.stringify(data), { headers: { 'Content-Type' : 'application/json' }});
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

function getTestEmployee(id: string): Employee {
  return {
    'id': '10001',
    'birthDate': 'Sep 2, 1953',
    'firstName': 'Georgi',
    'lastName': 'Facello',
    'gender': Gender.Male,
    'hireDate': 'Jun 26, 1986'
  };
}

function getTestEmployees(page: number): EmployeesResult {
  return {
    'employees': [
      {
        'id': '10001',
        'fullName': 'Georgi Facello'
      },
      {
        'id': '10002',
        'fullName': 'Bezalel Simmel'
      },
      {
        'id': '10003',
        'fullName': 'Parto Bamford'
      },
      {
        'id': '10004',
        'fullName': 'Chirstian Koblick'
      },
      {
        'id': '10005',
        'fullName': 'Kyoichi Maliniak'
      },
      {
        'id': '10006',
        'fullName': 'Anneke Preusig'
      },
      {
        'id': '10007',
        'fullName': 'Tzvetan Zielinski'
      },
      {
        'id': '10008',
        'fullName': 'Saniya Kalloufi'
      },
      {
        'id': '10009',
        'fullName': 'Sumant Peac'
      },
      {
        'id': '10010',
        'fullName': 'Duangkaew Piveteau'
      },
      {
        'id': '10011',
        'fullName': 'Mary Sluis'
      },
      {
        'id': '10012',
        'fullName': 'Patricio Bridgland'
      },
      {
        'id': '10013',
        'fullName': 'Eberhardt Terkki'
      },
      {
        'id': '10014',
        'fullName': 'Berni Genin'
      },
      {
        'id': '10015',
        'fullName': 'Guoxiang Nooteboom'
      },
      {
        'id': '10016',
        'fullName': 'Kazuhito Cappelletti'
      },
      {
        'id': '10017',
        'fullName': 'Cristinel Bouloucos'
      },
      {
        'id': '10018',
        'fullName': 'Kazuhide Peha'
      },
      {
        'id': '10019',
        'fullName': 'Lillian Haddadi'
      },
      {
        'id': '10020',
        'fullName': 'Mayuko Warwick'
      }
    ],
    'page': page,
    'count': 300024,
    'itemsPerPage': 20
  };
}
